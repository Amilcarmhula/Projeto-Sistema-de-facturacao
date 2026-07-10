-- ============================================================
-- 02_PROCEDURES_COMPLETO.sql
-- JM-InvSM / SysFace - SCRIPT AGRUPADO DE PROCEDURES E EVENTOS
-- Banco de teste: _sysface_teste
--
-- Modulos contidos neste ficheiro:
-- 1. MODULO TRANSFERENCIAS
--    EnviarTransferencia e ReceberTransferencia.
-- 2. MODULO REQUISICOES
--    AprovarRequisicao e ConfirmarPicking com validacao de estado,
--    stock disponivel, reservas activas e transaccao atomica.
-- 3. MODULO PRECOS E DESCONTOS
--    sp_registar_preco e sp_aplicar_desconto_ao_preco, com IVA
--    tratado como percentagem.
-- 4. MODULO ROTINAS AGENDADAS
--    Procedures e eventos para expiracao de precos, parcelas em
--    atraso, taxa de atraso e saldo pendente da factura.
--
-- Execute depois de 00_TABELAS_COMPLETO.sql.
-- Os eventos podem exigir que o event_scheduler seja activado.
-- ============================================================

USE _sysface_teste;

-- Limpeza para permitir reexecucao em ambiente de teste.
DROP PROCEDURE IF EXISTS EnviarTransferencia;
DROP PROCEDURE IF EXISTS ReceberTransferencia;
DROP PROCEDURE IF EXISTS AprovarRequisicao;
DROP PROCEDURE IF EXISTS ConfirmarPicking;
DROP PROCEDURE IF EXISTS sp_registar_preco;
DROP PROCEDURE IF EXISTS sp_aplicar_desconto_ao_preco;
DROP PROCEDURE IF EXISTS procedure_updatePrecoVenda;
DROP PROCEDURE IF EXISTS procedure_updateEstadoParcela;
DROP PROCEDURE IF EXISTS procedure_updateTaxaAtrasoParcela;
DROP PROCEDURE IF EXISTS procedure_updateSaldoFactura_com_Atraso_de_Parcela;
DROP EVENT IF EXISTS evt_marcar_expiracao_precos;
DROP EVENT IF EXISTS evento_updatePrecoVenda;
DROP EVENT IF EXISTS evento_updateEstadoParcela;
DROP EVENT IF EXISTS evento_updateTaxaAtrasoParcela;
DROP EVENT IF EXISTS evento_updateSaldoFactura_com_Atraso_de_Parcela;

-- SET GLOBAL event_scheduler = ON; -- activar manualmente com permissao administrativa, se necessario.

DELIMITER //
CREATE PROCEDURE EnviarTransferencia(p_transferencia INT)
BEGIN

DECLARE id_produto INTEGER;
DECLARE id_armazem INTEGER;
DECLARE qtd INTEGER;
DECLARE saldo_stock INTEGER;

-- SAÃDA DO ARMAZÃ‰M ORIGEM
INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Saida',
    CURDATE(),
    quantidade,
    t.idArmazemOrigem,
    i.idProduto
FROM item_transferencia i
JOIN transferencia_stock t ON t.id=i.idTransferencia
WHERE i.idTransferencia=p_transferencia;

-- cria stock em trÃ¢nsito
INSERT INTO stock_transito(
    idTransferencia,
    idProduto,
    quantidade,
    estado
)
SELECT
    idTransferencia,
    idProduto,
    quantidade,
    'Em Transito'
FROM item_transferencia
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Em Transito'
WHERE id=p_transferencia;

	SELECT t.idArmazemOrigem
    INTO id_armazem
    FROM transferencia_stock t
    WHERE t.id = p_transferencia;
    
    SELECT it.quantidade
    INTO qtd
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT it.idProduto
    INTO id_produto
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT s.saldo into saldo_stock
    from stock s
    where s.idProduto=id_produto  and s.idArmazem=id_armazem;
    
UPDATE stock SET saldo = saldo_stock - qtd 
WHERE stock.idProduto = id_produto
AND stock.idArmazem = id_armazem;

END//
DELIMITER ;


delimiter //
CREATE PROCEDURE ReceberTransferencia(p_transferencia INT)
BEGIN

INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Entrada',
    CURDATE(),
    st.quantidade,
    t.idArmazemDestino,
    st.idProduto
FROM stock_transito st
JOIN transferencia_stock t ON t.id=st.idTransferencia
WHERE st.idTransferencia=p_transferencia;

UPDATE stock_transito
SET estado='Recebido'
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Recebido'
WHERE id=p_transferencia;

end//
delimiter ;


DELIMITER //
CREATE PROCEDURE AprovarRequisicao(p_id INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_total_itens INT DEFAULT 0;
    DECLARE v_reservas_ativas INT DEFAULT 0;
    DECLARE v_indisponiveis INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_id
    LIMIT 1;

    IF v_estado IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao inexistente';
    END IF;

    IF v_estado NOT IN ('Submetida', 'AguardandoAprovacao', 'Pendente') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao esta em estado aprovavel';
    END IF;

    SELECT COUNT(*)
    INTO v_total_itens
    FROM itemRequisicao
    WHERE idRequisicao = p_id;

    IF v_total_itens = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui itens';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_id
      AND estado = 'Reservado';

    IF v_reservas_ativas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao ja possui reserva ativa';
    END IF;

    SELECT COUNT(*)
    INTO v_indisponiveis
    FROM (
        SELECT
            ir.idProduto,
            ir.idArmazem,
            SUM(ir.quantidade) AS quantidade_solicitada,
            COALESCE(MAX(s.saldo - s.reservado), 0) AS quantidade_disponivel
        FROM itemRequisicao ir
        LEFT JOIN stock s
          ON s.idProduto = ir.idProduto
         AND s.idArmazem = ir.idArmazem
        WHERE ir.idRequisicao = p_id
        GROUP BY ir.idProduto, ir.idArmazem
        HAVING quantidade_disponivel < quantidade_solicitada
    ) indisponiveis;

    IF v_indisponiveis > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para aprovar requisicao';
    END IF;

    INSERT INTO reserva_stock(
        idProduto,
        idArmazem,
        idRequisicao,
        quantidade,
        estado
    )
    SELECT
        idProduto,
        idArmazem,
        idRequisicao,
        SUM(quantidade),
        'Reservado'
    FROM itemRequisicao
    WHERE idRequisicao = p_id
    GROUP BY idProduto, idArmazem, idRequisicao;

    UPDATE requisicao
    SET estado = 'Aprovada'
    WHERE id = p_id;

    INSERT INTO picking(tipo, estado, idRequisicao, dataInicio)
    SELECT 'Requisicao', 'Aguardando', p_id, NOW()
    WHERE NOT EXISTS (
        SELECT 1 FROM picking WHERE idRequisicao = p_id
    );

    COMMIT;
END//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE ConfirmarPicking(p_requisicao INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_reservas_ativas INT DEFAULT 0;

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

    IF v_estado <> 'Aprovada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas requisicoes aprovadas podem confirmar picking';
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
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_registar_preco (
    IN p_idProduto INT,
    IN p_idArmazem INT,
    IN p_precoBase DECIMAL(10,2)
)
BEGIN
	DECLARE desconto_taxa DECIMAL(4,2);
    DECLARE imposto_taxa DECIMAL(4,2);
    DECLARE p_Venda DECIMAL(10,2);
    DECLARE p_Final DECIMAL(10,2);
    
    START TRANSACTION;
    -- 1ï¸ desactivar preÃ§o activo anterior
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo',
        dataFimVigencia = CURRENT_DATE
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo';
      
	-- imposto (exemplo simples)
    SELECT i.percentagem
    INTO imposto_taxa
    FROM imposto i
    JOIN produto p ON p.idImposto = i.id
    WHERE p.id = p_idProduto
    LIMIT 1;
	-- preco venda
    IF imposto_taxa IS NULL THEN
        SET imposto_taxa = 0;
    END IF;
    -- set p_Venda = p_precoBase + ((p_precoBase * imposto_taxa) / 100);
     set p_Venda = p_precoBase + ((p_precoBase * imposto_taxa) / 100);

    
    -- desconto activo
    SELECT d.percentagem
    INTO desconto_taxa
    FROM produtodesconto pd
    JOIN desconto d ON d.id = pd.idDesconto
    WHERE pd.idProduto = p_idProduto
      AND pd.idArmazem = p_idArmazem
      AND CURRENT_DATE BETWEEN pd.dataInicio AND pd.dataFim
    LIMIT 1;
    -- preÃ§o final
    IF desconto_taxa IS NOT NULL THEN
        SET p_Final = p_Venda - ((p_Venda * desconto_taxa) / 100);
    ELSE
        SET p_Final = p_Venda;
    END IF;

    -- 2ï¸ inserir novo preÃ§o
    INSERT INTO precoProdutoArmazem
    (idProduto, idArmazem, precoBase,precoVenda,precoFinal,dataInicioVigencia,estado)
    VALUES
    (p_idProduto, p_idArmazem, p_precoBase, p_Venda, p_Final,CURRENT_DATE,'activo');
    
    COMMIT;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_aplicar_desconto_ao_preco(
    IN p_idProduto INT,
    IN p_idArmazem INT
)
BEGIN
    DECLARE v_precoBase DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_precoVenda DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_precoFinal DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_desconto DECIMAL(4,2) DEFAULT 0;
    DECLARE v_imposto DECIMAL(4,2) DEFAULT 0;

    -- 1ï¸âƒ£ Obter o Ãºltimo preÃ§o activo
    SELECT precoBase
    INTO v_precoBase
    FROM precoProdutoArmazem
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo'
    ORDER BY dataInicioVigencia DESC
    LIMIT 1;

    -- Se nÃ£o existir preÃ§o anterior, mantÃ©m 0.01
    IF v_precoBase IS NULL THEN
        SET v_precoBase = 0.01;
    END IF;

    -- 2ï¸âƒ£ Obter desconto activo
    SELECT d.percentagem
    INTO v_desconto
    FROM desconto d
    JOIN produtodesconto pd ON pd.idDesconto = d.id
    WHERE pd.idProduto = p_idProduto
      AND pd.idArmazem = p_idArmazem
      AND CURRENT_DATE BETWEEN pd.dataInicio AND pd.dataFim
    ORDER BY pd.dataInicio DESC
    LIMIT 1;

    IF v_desconto IS NULL THEN
        SET v_desconto = 0;
    END IF;

    -- 3ï¸âƒ£ Obter imposto do produto
    SELECT i.percentagem
    INTO v_imposto
    FROM produto p
    JOIN imposto i ON p.idImposto = i.id
    WHERE p.id = p_idProduto
    LIMIT 1;

    IF v_imposto IS NULL THEN
        SET v_imposto = 0;
    END IF;

    -- 4ï¸âƒ£ Desactivar preÃ§o activo anterior
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo', dataFimVigencia = CURRENT_DATE
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo';

    -- 5ï¸âƒ£ Calcular preÃ§os
    SET v_precoVenda = v_precoBase + ((v_precoBase * v_imposto) / 100);
    SET v_precoFinal = v_precoVenda - (v_precoVenda * v_desconto / 100);

    -- 6ï¸âƒ£ Inserir novo preÃ§o
    INSERT INTO precoProdutoArmazem
    (idProduto, idArmazem, precoBase, precoVenda, precoFinal, estado, dataInicioVigencia)
    VALUES
    (p_idProduto, p_idArmazem, v_precoBase, v_precoVenda, v_precoFinal, 'activo', CURRENT_DATE);

END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE procedure_updatePrecoVenda()
BEGIN
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo',
        dataFimVigencia = COALESCE(dataFimVigencia, CURDATE())
    WHERE id > 0
      AND dataFimVigencia <= CURDATE()
      AND estado = 'activo';
END//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE procedure_updateEstadoParcela()
BEGIN
    UPDATE parcelas
    SET estado = 'Atrasada'
    WHERE dataVencimento <= CURDATE() AND estado <> 'Paga';
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE procedure_updateTaxaAtrasoParcela()
BEGIN
    UPDATE parcelas
    SET taxaDeAtraso = taxaJuros / 2
    WHERE estado = 'Atrasada';
END //
DELIMITER ;

delimiter //
CREATE PROCEDURE procedure_updateSaldoFactura_com_Atraso_de_Parcela()
BEGIN
    -- Atualizar o saldo pendente de cada fatura com base nas parcelas
    UPDATE factura f
    SET f.saldoPendente = (
        SELECT COALESCE(SUM(
            CASE 
                WHEN p.estado = 'Atrasada' THEN (p.valorParcela * p.taxaDeAtraso) + (p.valorParcela - p.valorPagoParcela)
                ELSE 
					case when p.estado = 'Pendente' THEN
						p.valorParcela - p.valorPagoParcela
					end
            END
        ), 0)
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    )
    WHERE EXISTS (
        SELECT 1 
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    );
END//
delimiter ;

-- ============================================================
-- EVENTOS AGENDADOS
-- ============================================================

CREATE EVENT evt_marcar_expiracao_precos
ON SCHEDULE EVERY 1 DAY
DO
UPDATE precoProdutoArmazem
SET dataFimVigencia = CURRENT_DATE
WHERE estado = 'activo'
  AND dataFimVigencia IS NOT NULL
  AND dataFimVigencia < CURRENT_DATE;

CREATE EVENT evento_updatePrecoVenda
	ON SCHEDULE EVERY 1 DAY
	DO
		CALL procedure_updatePrecoVenda();


CREATE EVENT evento_updateEstadoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateEstadoParcela();


CREATE EVENT evento_updateTaxaAtrasoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateTaxaAtrasoParcela();


CREATE EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateSaldoFactura_com_Atraso_de_Parcela();

SHOW PROCEDURE STATUS WHERE Db = DATABASE();
SHOW EVENTS;
