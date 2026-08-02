-- ============================================================
-- REGRAS DE BANCO - COMPRAS, RECEBIMENTO E CONTAS A PAGAR
-- Sistema stand-alone JM-InvSM
-- ============================================================
--
-- Dependencias:
-- 1. Tabelas base do sistema: fornecedor, produto, armazem, usuario,
--    requisicao, itemRequisicao, lote, movimento, stock, metodo_pagamento,
--    contabancaria, caixa, contacorrente.
-- 2. Tabelas do script 04_TABELAS_COMPRAS_CONTAS_PAGAR.sql.
-- 3. Triggers base de stock:
--    - Tg_registaMovimentoEntrada: cria movimento Compra/Entrada ao inserir lote.
--    - Tg_actualizaStock_IO: actualiza stock.saldo para movimentos de Entrada.
--
-- Este script cria:
-- - CriarOrdemCompraDeRequisicao
-- - ConfirmarRecebimentoCompra
-- - CancelarRequisicao (liberta reservas antes da finalizacao)
-- - ConfirmarPicking (protegido contra requisicoes vinculadas a compra)
-- - CriarFacturaFornecedorDeRecebimento
-- - RegistarPagamentoFornecedor
-- - Trigger para criar conta_pagar quando factura_fornecedor for registada
-- - Triggers para validar/liquidar pagamento_fornecedor
-- ============================================================
USE _sysface_teste;

SET NAMES utf8mb4;

DROP PROCEDURE IF EXISTS CriarOrdemCompraDeRequisicao;
DROP PROCEDURE IF EXISTS ConfirmarRecebimentoCompra;
DROP PROCEDURE IF EXISTS CancelarRequisicao;
DROP PROCEDURE IF EXISTS ConfirmarPicking;
DROP PROCEDURE IF EXISTS CriarFacturaFornecedorDeRecebimento;
DROP PROCEDURE IF EXISTS RegistarPagamentoFornecedor;

DROP TRIGGER IF EXISTS Tg_factura_fornecedor_cria_conta_pagar;
DROP TRIGGER IF EXISTS Tg_pagamento_fornecedor_validar;
DROP TRIGGER IF EXISTS Tg_pagamento_fornecedor_liquidar;
DROP TRIGGER IF EXISTS _updateCaixa_ContaCorrente;

-- procedimento descontinuado
DELIMITER //
CREATE PROCEDURE CriarOrdemCompraDeRequisicao(
    IN p_id_requisicao INT,
    IN p_id_fornecedor INT,
    IN p_data_prevista_entrega DATE,
    IN p_id_usuario INT
)
BEGIN
    DECLARE v_estado_requisicao VARCHAR(30);
    DECLARE v_total_itens INT DEFAULT 0;
    DECLARE v_ordens_existentes INT DEFAULT 0;
    DECLARE v_ordem_id INT DEFAULT 0;
    DECLARE v_numero VARCHAR(30);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado_requisicao
    FROM requisicao
    WHERE id = p_id_requisicao
    LIMIT 1;

    IF v_estado_requisicao IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao inexistente';
    END IF;

    IF v_estado_requisicao <> 'Aprovada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas requisicoes aprovadas podem gerar ordem de compra';
    END IF;

    SELECT COUNT(*)
    INTO v_total_itens
    FROM itemRequisicao
    WHERE idRequisicao = p_id_requisicao;

    IF v_total_itens = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui itens para ordem de compra';
    END IF;

    SELECT COUNT(*)
    INTO v_ordens_existentes
    FROM ordem_compra
    WHERE idRequisicao = p_id_requisicao
      AND idFornecedor = p_id_fornecedor
      AND estado <> 'Cancelada';

    IF v_ordens_existentes > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ja existe ordem de compra activa para esta requisicao e fornecedor';
    END IF;

    SET v_numero = CONCAT(
        'OC-',
        DATE_FORMAT(CURRENT_DATE(), '%Y%m%d'),
        '-R',
        p_id_requisicao,
        '-F',
        p_id_fornecedor
    );

    INSERT INTO ordem_compra (
        numero,
        idRequisicao,
        idFornecedor,
        dataEmissao,
        dataPrevistaEntrega,
        estado,
        idUsuario,
        observacao
    )
    VALUES (
        v_numero,
        p_id_requisicao,
        p_id_fornecedor,
        CURRENT_DATE(),
        p_data_prevista_entrega,
        'Aprovada',
        p_id_usuario,
        'Gerada automaticamente a partir de requisicao aprovada'
    );

    SET v_ordem_id = LAST_INSERT_ID();

    INSERT INTO item_ordem_compra (
        idOrdemCompra,
        idItemRequisicao,
        idProduto,
        idArmazem,
        quantidadeSolicitada,
        custoUnitario,
        estado
    )
    SELECT
        v_ordem_id,
        ir.id,
        ir.idProduto,
        ir.idArmazem,
        ir.quantidade,
        0.00,
        'Pendente'
    FROM itemRequisicao ir
    WHERE ir.idRequisicao = p_id_requisicao;

    COMMIT;

    SELECT v_ordem_id AS idOrdemCompra, v_numero AS numeroOrdemCompra;
END//

CREATE PROCEDURE ConfirmarRecebimentoCompra(
    IN p_id_recebimento INT
)
BEGIN
    DECLARE v_estado_recebimento VARCHAR(30);
    DECLARE v_id_ordem INT;
    DECLARE v_id_requisicao INT DEFAULT NULL;
    DECLARE v_id_fornecedor INT;
    DECLARE v_data_recebimento DATE;
    DECLARE v_total_itens INT DEFAULT 0;
    DECLARE v_itens_pendentes INT DEFAULT 0;

    DECLARE v_done INT DEFAULT 0;
    DECLARE v_item_recebimento_id INT;
    DECLARE v_item_ordem_id INT;
    DECLARE v_id_produto INT;
    DECLARE v_id_armazem INT;
    DECLARE v_quantidade INT;
    DECLARE v_custo_unitario DECIMAL(12,2);
    DECLARE v_codigo_lote VARCHAR(20);
    DECLARE v_lote_id INT;
    DECLARE v_qtd_restante_ordem INT DEFAULT 0;

    DECLARE cur_itens CURSOR FOR
        SELECT
            irc.id,
            irc.idItemOrdemCompra,
            irc.idProduto,
            irc.idArmazem,
            irc.quantidadeRecebida,
            irc.custoUnitario,
            irc.codigoLote
        FROM item_recebimento_compra irc
        WHERE irc.idRecebimentoCompra = p_id_recebimento
          AND irc.estado = 'Pendente'
        ORDER BY irc.id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado, idOrdemCompra, idFornecedor, DATE(dataRecebimento)
    INTO v_estado_recebimento, v_id_ordem, v_id_fornecedor, v_data_recebimento
    FROM recebimento_compra
    WHERE id = p_id_recebimento
    LIMIT 1;

    IF v_estado_recebimento IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Recebimento inexistente';
    END IF;

    IF v_estado_recebimento <> 'Rascunho' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas recebimentos em rascunho podem ser confirmados';
    END IF;

    SELECT idRequisicao
    INTO v_id_requisicao
    FROM ordem_compra
    WHERE id = v_id_ordem
    LIMIT 1;

    SELECT COUNT(*)
    INTO v_total_itens
    FROM item_recebimento_compra
    WHERE idRecebimentoCompra = p_id_recebimento;

    IF v_total_itens = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Recebimento nao possui itens';
    END IF;

    SELECT COUNT(*)
    INTO v_itens_pendentes
    FROM item_recebimento_compra
    WHERE idRecebimentoCompra = p_id_recebimento
      AND estado = 'Pendente';

    IF v_itens_pendentes = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Recebimento nao possui itens pendentes';
    END IF;

    OPEN cur_itens;

    read_loop: LOOP
        FETCH cur_itens
        INTO v_item_recebimento_id,
             v_item_ordem_id,
             v_id_produto,
             v_id_armazem,
             v_quantidade,
             v_custo_unitario,
             v_codigo_lote;

        IF v_done = 1 THEN
            LEAVE read_loop;
        END IF;

        IF v_item_ordem_id IS NOT NULL THEN
            SELECT quantidadeSolicitada - quantidadeRecebida
            INTO v_qtd_restante_ordem
            FROM item_ordem_compra
            WHERE id = v_item_ordem_id
            LIMIT 1;

            IF v_qtd_restante_ordem < v_quantidade THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Quantidade recebida excede a quantidade pendente da ordem de compra';
            END IF;
        END IF;

        IF v_codigo_lote IS NULL OR TRIM(v_codigo_lote) = '' THEN
            SET v_codigo_lote = CONCAT('RC-', p_id_recebimento, '-', v_item_recebimento_id);
        END IF;

        INSERT INTO lote (
            codigo,
            idProduto,
            idArmazem,
            idFornecedor,
            dataEntrada,
            quantidadeInicial,
            quantidadeAtual,
            custoUnitario,
            estado
        )
        VALUES (
            v_codigo_lote,
            v_id_produto,
            v_id_armazem,
            v_id_fornecedor,
            v_data_recebimento,
            v_quantidade,
            v_quantidade,
            v_custo_unitario,
            'Ativo'
        );

        SELECT id
        INTO v_lote_id
        FROM lote
        WHERE codigo = v_codigo_lote
        LIMIT 1;

        UPDATE item_recebimento_compra
        SET idLote = v_lote_id,
            codigoLote = v_codigo_lote,
            estado = 'Confirmado'
        WHERE id = v_item_recebimento_id;

        IF v_item_ordem_id IS NOT NULL THEN
            UPDATE item_ordem_compra
            SET quantidadeRecebida = quantidadeRecebida + v_quantidade,
                custoUnitario = CASE
                    WHEN custoUnitario <= 0 THEN v_custo_unitario
                    ELSE custoUnitario
                END,
                estado = CASE
                    WHEN quantidadeRecebida + v_quantidade >= quantidadeSolicitada THEN 'Recebido'
                    ELSE 'ParcialmenteRecebido'
                END
            WHERE id = v_item_ordem_id;
        END IF;
    END LOOP;

    CLOSE cur_itens;

    UPDATE recebimento_compra
    SET estado = 'Confirmado',
        totalRecebido = (
            SELECT COALESCE(SUM(subtotal), 0)
            FROM item_recebimento_compra
            WHERE idRecebimentoCompra = p_id_recebimento
              AND estado = 'Confirmado'
        )
    WHERE id = p_id_recebimento;

    UPDATE ordem_compra
    SET subtotal = (
        SELECT COALESCE(SUM(totalLinha), 0)
        FROM item_ordem_compra
        WHERE idOrdemCompra = v_id_ordem
    )
    WHERE id = v_id_ordem;

    UPDATE ordem_compra oc
    SET oc.estado = CASE
        WHEN NOT EXISTS (
            SELECT 1
            FROM item_ordem_compra ioc
            WHERE ioc.idOrdemCompra = oc.id
              AND ioc.estado <> 'Recebido'
        ) THEN 'Recebida'
        ELSE 'ParcialmenteRecebida'
    END
    WHERE oc.id = v_id_ordem;

    IF v_id_requisicao IS NOT NULL
       AND NOT EXISTS (
            SELECT 1
            FROM item_ordem_compra ioc
            WHERE ioc.idOrdemCompra = v_id_ordem
              AND ioc.estado <> 'Recebido'
       ) THEN
        UPDATE reserva_stock
        SET estado = 'Liberado'
        WHERE idRequisicao = v_id_requisicao
          AND estado = 'Reservado';

        UPDATE picking
        SET estado = 'Confirmado',
            dataFim = NOW()
        WHERE idRequisicao = v_id_requisicao
          AND estado <> 'Confirmado';

        UPDATE requisicao
        SET estado = 'Finalizada'
        WHERE id = v_id_requisicao;
    END IF;

    COMMIT;
END//

CREATE PROCEDURE CancelarRequisicao(p_id INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT COUNT(*), COALESCE(MAX(estado), '')
    INTO v_existe, v_estado
    FROM requisicao
    WHERE id = p_id;

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao inexistente';
    END IF;

    IF v_estado = 'Finalizada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao finalizada nao pode ser cancelada';
    END IF;

    IF v_estado = 'Cancelada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao ja esta cancelada';
    END IF;

    UPDATE reserva_stock
    SET estado = 'Cancelado'
    WHERE idRequisicao = p_id
      AND estado = 'Reservado';

    UPDATE picking
    SET dataFim = COALESCE(dataFim, NOW())
    WHERE idRequisicao = p_id
      AND estado <> 'Confirmado';

    UPDATE requisicao
    SET estado = 'Cancelada'
    WHERE id = p_id;

    COMMIT;
END//

CREATE PROCEDURE ConfirmarPicking(p_requisicao INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_reservas_ativas INT DEFAULT 0;
    DECLARE v_ordens_compra_ativas INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_requisicao
    LIMIT 1;

    IF v_estado NOT IN ('Aprovada', 'EmPicking') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas requisicoes aprovadas ou em picking podem confirmar picking';
    END IF;

    SELECT COUNT(*)
    INTO v_ordens_compra_ativas
    FROM ordem_compra
    WHERE idRequisicao = p_requisicao
      AND estado <> 'Cancelada';

    IF v_ordens_compra_ativas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao vinculada a ordem de compra deve ser finalizada pelo recebimento, nao pelo picking';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    IF v_reservas_ativas = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui reserva ativa para consumir';
    END IF;

    INSERT INTO movimento (
        tipoOrigem,
        tipoMovimento,
        dataMovimento,
        quantidade,
        idArmazem,
        idProduto,
        idItemRequisicao
    )
    SELECT
        'Requisicao',
        'Saida',
        CURDATE(),
        quantidade,
        idArmazem,
        idProduto,
        id
    FROM itemRequisicao
    WHERE idRequisicao = p_requisicao;

    UPDATE reserva_stock
    SET estado = 'Consumido'
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    UPDATE requisicao
    SET estado = 'Finalizada'
    WHERE id = p_requisicao;

    UPDATE picking
    SET estado = 'Confirmado',
        dataFim = NOW()
    WHERE idRequisicao = p_requisicao;

    COMMIT;
END//

CREATE PROCEDURE CriarFacturaFornecedorDeRecebimento(
    IN p_numero_factura VARCHAR(50),
    IN p_id_recebimento INT,
    IN p_data_emissao DATE,
    IN p_data_vencimento DATE,
    IN p_id_usuario INT
)
BEGIN
    DECLARE v_estado_recebimento VARCHAR(30);
    DECLARE v_id_fornecedor INT;
    DECLARE v_id_ordem INT;
    DECLARE v_subtotal DECIMAL(12,2) DEFAULT 0.00;
    DECLARE v_factura_id INT DEFAULT 0;
    DECLARE v_facturas_existentes INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado, idFornecedor, idOrdemCompra
    INTO v_estado_recebimento, v_id_fornecedor, v_id_ordem
    FROM recebimento_compra
    WHERE id = p_id_recebimento
    LIMIT 1;

    IF v_estado_recebimento IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Recebimento inexistente';
    END IF;

    IF v_estado_recebimento <> 'Confirmado' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas recebimentos confirmados podem gerar factura do fornecedor';
    END IF;

    SELECT COUNT(*)
    INTO v_facturas_existentes
    FROM factura_fornecedor
    WHERE numero = p_numero_factura
      AND idFornecedor = v_id_fornecedor;

    IF v_facturas_existentes > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Factura do fornecedor ja registada para este fornecedor';
    END IF;

    SELECT COALESCE(SUM(subtotal), 0)
    INTO v_subtotal
    FROM item_recebimento_compra
    WHERE idRecebimentoCompra = p_id_recebimento
      AND estado = 'Confirmado';

    IF v_subtotal <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Recebimento nao possui valores confirmados para facturar';
    END IF;

    INSERT INTO factura_fornecedor (
        numero,
        idFornecedor,
        idOrdemCompra,
        idRecebimentoCompra,
        dataEmissao,
        dataVencimento,
        subtotal,
        desconto,
        imposto,
        valorPago,
        estado,
        idUsuario,
        observacao
    )
    VALUES (
        p_numero_factura,
        v_id_fornecedor,
        v_id_ordem,
        p_id_recebimento,
        p_data_emissao,
        p_data_vencimento,
        v_subtotal,
        0.00,
        0.00,
        0.00,
        'Pendente',
        p_id_usuario,
        'Gerada a partir de recebimento confirmado'
    );

    SET v_factura_id = LAST_INSERT_ID();

    INSERT INTO item_factura_fornecedor (
        idFacturaFornecedor,
        idItemRecebimentoCompra,
        idProduto,
        descricao,
        quantidade,
        custoUnitario,
        desconto,
        imposto
    )
    SELECT
        v_factura_id,
        irc.id,
        irc.idProduto,
        CONCAT('Item recebido no recebimento ', p_id_recebimento),
        irc.quantidadeRecebida,
        irc.custoUnitario,
        0.00,
        0.00
    FROM item_recebimento_compra irc
    WHERE irc.idRecebimentoCompra = p_id_recebimento
      AND irc.estado = 'Confirmado';

    COMMIT;

    SELECT v_factura_id AS idFacturaFornecedor;
END//

CREATE PROCEDURE RegistarPagamentoFornecedor(
    IN p_id_conta_pagar INT,
    IN p_data_pagamento DATE,
    IN p_valor_pago DECIMAL(12,2),
    IN p_id_metodo_pagamento INT,
    IN p_origem_pagamento VARCHAR(10),
    IN p_id_conta_bancaria INT,
    IN p_referencia VARCHAR(80),
    IN p_descricao TEXT,
    IN p_id_usuario INT
)
BEGIN
    DECLARE v_id_fornecedor INT;
    DECLARE v_pagamento_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT idFornecedor
    INTO v_id_fornecedor
    FROM conta_pagar
    WHERE id = p_id_conta_pagar
    LIMIT 1;

    IF v_id_fornecedor IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conta a pagar inexistente';
    END IF;

    INSERT INTO pagamento_fornecedor (
        idContaPagar,
        idFornecedor,
        dataPagamento,
        valorPago,
        idMetodoPagamento,
        origemPagamento,
        idContaBancaria,
        referencia,
        descricao,
        estado,
        idUsuario
    )
    VALUES (
        p_id_conta_pagar,
        v_id_fornecedor,
        p_data_pagamento,
        p_valor_pago,
        p_id_metodo_pagamento,
        p_origem_pagamento,
        p_id_conta_bancaria,
        p_referencia,
        p_descricao,
        'Confirmado',
        p_id_usuario
    );

    SET v_pagamento_id = LAST_INSERT_ID();

    COMMIT;

    SELECT v_pagamento_id AS idPagamentoFornecedor;
END//

CREATE TRIGGER _updateCaixa_ContaCorrente
AFTER INSERT ON transacoes
FOR EACH ROW
BEGIN
    DECLARE v_saldo_anterior DECIMAL(12,2) DEFAULT 0.00;
    DECLARE v_saldo_posterior DECIMAL(12,2) DEFAULT 0.00;

    IF NEW.fk_id_metodo_pagamento = 1 THEN
        SELECT COALESCE(
            (
                SELECT c.saldo_atual
                FROM caixa c
                ORDER BY c.id DESC
                LIMIT 1
            ),
            0.00
        )
        INTO v_saldo_anterior;

        SET v_saldo_posterior = CASE
            WHEN NEW.tipo_transacao = 'Saida' THEN v_saldo_anterior - NEW.valorPago
            ELSE v_saldo_anterior + NEW.valorPago
        END;

        INSERT INTO caixa (saldo_inicial, saldo_atual, fk_id_transacoes)
        VALUES (v_saldo_anterior, v_saldo_posterior, NEW.id);
    ELSE
        SELECT COALESCE(
            (
                SELECT cc.saldo_atual
                FROM contacorrente cc
                ORDER BY cc.id DESC
                LIMIT 1
            ),
            0.00
        )
        INTO v_saldo_anterior;

        SET v_saldo_posterior = CASE
            WHEN NEW.tipo_transacao = 'Saida' THEN v_saldo_anterior - NEW.valorPago
            ELSE v_saldo_anterior + NEW.valorPago
        END;

        INSERT INTO contacorrente (saldo_inicial, saldo_atual, fk_id_transacoes)
        VALUES (v_saldo_anterior, v_saldo_posterior, NEW.id);
    END IF;
END//

CREATE TRIGGER Tg_factura_fornecedor_cria_conta_pagar
AFTER INSERT ON factura_fornecedor
FOR EACH ROW
BEGIN
    IF NEW.estado <> 'Cancelada' AND NEW.total > 0 THEN
        INSERT INTO conta_pagar (
            idFacturaFornecedor,
            idFornecedor,
            dataEmissao,
            dataVencimento,
            valorOriginal,
            valorPago,
            estado,
            observacao
        )
        VALUES (
            NEW.id,
            NEW.idFornecedor,
            NEW.dataEmissao,
            NEW.dataVencimento,
            NEW.total,
            NEW.valorPago,
            CASE
                WHEN NEW.valorPago >= NEW.total THEN 'Liquidada'
                WHEN NEW.valorPago > 0 THEN 'Parcial'
                ELSE 'Aberta'
            END,
            'Criada automaticamente a partir da factura do fornecedor'
        );
    END IF;
END//

CREATE TRIGGER Tg_pagamento_fornecedor_validar
BEFORE INSERT ON pagamento_fornecedor
FOR EACH ROW
BEGIN
    DECLARE v_saldo_pendente DECIMAL(12,2);
    DECLARE v_estado_conta VARCHAR(30);
    DECLARE v_fornecedor_conta INT;
    DECLARE v_saldo_financeiro DECIMAL(12,2) DEFAULT 0.00;

    SELECT saldoPendente, estado, idFornecedor
    INTO v_saldo_pendente, v_estado_conta, v_fornecedor_conta
    FROM conta_pagar
    WHERE id = NEW.idContaPagar
    LIMIT 1;

    IF v_saldo_pendente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conta a pagar inexistente';
    END IF;

    IF v_estado_conta IN ('Liquidada', 'Cancelada') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conta a pagar nao permite novos pagamentos';
    END IF;

    IF NEW.idFornecedor <> v_fornecedor_conta THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Fornecedor do pagamento diferente da conta a pagar';
    END IF;

    IF NEW.valorPago <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Valor pago deve ser maior que zero';
    END IF;

    IF NEW.valorPago > v_saldo_pendente THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Valor pago excede o saldo pendente da conta a pagar';
    END IF;

    IF NEW.origemPagamento = 'Banco' AND NEW.idContaBancaria IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Pagamento por banco exige conta bancaria';
    END IF;

    IF NEW.idUsuario IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Pagamento ao fornecedor exige utilizador';
    END IF;

    IF NEW.estado = 'Confirmado' THEN
        SELECT COALESCE(
            (
                SELECT mf.saldoPosterior
                FROM movimento_financeiro mf
                WHERE mf.origemPagamento = NEW.origemPagamento
                  AND (
                      NEW.origemPagamento = 'Caixa'
                      OR mf.idContaBancaria = NEW.idContaBancaria
                  )
                ORDER BY mf.id DESC
                LIMIT 1
            ),
            CASE
                WHEN NEW.origemPagamento = 'Caixa' THEN (
                    SELECT c.saldo_atual
                    FROM caixa c
                    ORDER BY c.id DESC
                    LIMIT 1
                )
                ELSE (
                    SELECT cc.saldo_atual
                    FROM contacorrente cc
                    ORDER BY cc.id DESC
                    LIMIT 1
                )
            END,
            0.00
        )
        INTO v_saldo_financeiro;

        IF v_saldo_financeiro < NEW.valorPago THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Saldo financeiro insuficiente para pagar fornecedor';
        END IF;
    END IF;
END//

CREATE TRIGGER Tg_pagamento_fornecedor_liquidar
AFTER INSERT ON pagamento_fornecedor
FOR EACH ROW
BEGIN
    DECLARE v_id_factura INT;
    DECLARE v_valor_original DECIMAL(12,2);
    DECLARE v_valor_pago_total DECIMAL(12,2);
    DECLARE v_saldo_anterior DECIMAL(12,2) DEFAULT 0.00;
    DECLARE v_saldo_posterior DECIMAL(12,2) DEFAULT 0.00;
    DECLARE v_transacao_id INT DEFAULT NULL;

    IF NEW.estado = 'Confirmado' THEN
        SELECT idFacturaFornecedor, valorOriginal, valorPago + NEW.valorPago
        INTO v_id_factura, v_valor_original, v_valor_pago_total
        FROM conta_pagar
        WHERE id = NEW.idContaPagar
        LIMIT 1;

        SELECT COALESCE(
            (
                SELECT mf.saldoPosterior
                FROM movimento_financeiro mf
                WHERE mf.origemPagamento = NEW.origemPagamento
                  AND (
                      NEW.origemPagamento = 'Caixa'
                      OR mf.idContaBancaria = NEW.idContaBancaria
                  )
                ORDER BY mf.id DESC
                LIMIT 1
            ),
            CASE
                WHEN NEW.origemPagamento = 'Caixa' THEN (
                    SELECT c.saldo_atual
                    FROM caixa c
                    ORDER BY c.id DESC
                    LIMIT 1
                )
                ELSE (
                    SELECT cc.saldo_atual
                    FROM contacorrente cc
                    ORDER BY cc.id DESC
                    LIMIT 1
                )
            END,
            0.00
        )
        INTO v_saldo_anterior;

        SET v_saldo_posterior = v_saldo_anterior - NEW.valorPago;

        INSERT INTO transacoes (
            data_pagamento,
            valorRecebido,
            valorPago,
            trocos,
            tipo_transacao,
            descricao,
            fk_id_contabancaria,
            fk_id_metodo_pagamento,
            fk_numero,
            fk_parcela,
            fk_id_usuario
        )
        VALUES (
            NEW.dataPagamento,
            0.00,
            NEW.valorPago,
            0.00,
            'Saida',
            COALESCE(NEW.descricao, 'Pagamento ao fornecedor'),
            NEW.idContaBancaria,
            NEW.idMetodoPagamento,
            NULL,
            NULL,
            NEW.idUsuario
        );

        SET v_transacao_id = LAST_INSERT_ID();

        UPDATE conta_pagar
        SET valorPago = v_valor_pago_total,
            estado = CASE
                WHEN v_valor_pago_total >= valorOriginal THEN 'Liquidada'
                ELSE 'Parcial'
            END
        WHERE id = NEW.idContaPagar;

        UPDATE factura_fornecedor
        SET valorPago = valorPago + NEW.valorPago,
            estado = CASE
                WHEN valorPago + NEW.valorPago >= total THEN 'Paga'
                ELSE 'ParcialmentePaga'
            END
        WHERE id = v_id_factura;

        INSERT INTO movimento_financeiro (
            dataMovimento,
            tipoMovimento,
            origem,
            valor,
            origemPagamento,
            idContaBancaria,
            idTransacao,
            idPagamentoFornecedor,
            saldoAnterior,
            saldoPosterior,
            descricao,
            idUsuario
        )
        VALUES (
            NOW(),
            'Saida',
            'PagamentoFornecedor',
            NEW.valorPago,
            NEW.origemPagamento,
            NEW.idContaBancaria,
            v_transacao_id,
            NEW.id,
            v_saldo_anterior,
            v_saldo_posterior,
            NEW.descricao,
            NEW.idUsuario
        );
    END IF;
END//

DELIMITER ;

-- ============================================================
-- FIM DO SCRIPT
-- ============================================================
