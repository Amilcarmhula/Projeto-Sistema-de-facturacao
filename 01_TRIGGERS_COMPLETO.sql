-- ============================================================
-- 01_TRIGGERS_COMPLETO.sql
-- JM-InvSM / SysFace - SCRIPT AGRUPADO DE TRIGGERS
-- Banco de teste: _sysface_teste
--
-- Modulos contidos neste ficheiro:
-- 1. MODULO INVENTARIO
--    Criacao automatica de stock por produto, entrada por lote,
--    movimentos de compra e actualizacao do saldo em entradas.
-- 2. MODULO REQUISICOES
--    Validacao de stock disponivel, reserva/libertacao de stock,
--    transicao para Submetida e baixa FIFO ao confirmar picking.
-- 3. MODULO FACTURACAO / VENDA
--    Saida por venda a partir de itemfactura, baixa FIFO dos lotes,
--    movimento de Venda/Saida e actualizacao do stock.
-- 4. MODULO PAGAMENTOS
--    Actualizacao de parcelas, facturas, caixa e conta corrente.
--
-- Execute depois de 00_TABELAS_COMPLETO.sql.
-- ============================================================

USE _sysface_teste;

-- Limpeza para permitir reexecucao em ambiente de teste.
DROP TRIGGER IF EXISTS Tg_C_Stock;
DROP TRIGGER IF EXISTS copiar_saldo_para_lote;
DROP TRIGGER IF EXISTS Tg_reservaStock_validar;
DROP TRIGGER IF EXISTS Tg_reservaStock;
DROP TRIGGER IF EXISTS Tg_reservaStock_estado;
DROP TRIGGER IF EXISTS Tg_estadoRequisicao;
DROP TRIGGER IF EXISTS Tg_registaMovimentoEntrada;
DROP TRIGGER IF EXISTS Tg_actualizaStock_IO;
DROP TRIGGER IF EXISTS Tg_FIFO;
DROP TRIGGER IF EXISTS Tg_C_Saida;
DROP TRIGGER IF EXISTS _updateParcelasFactura;
DROP TRIGGER IF EXISTS _updateParcela_Factura;
DROP TRIGGER IF EXISTS _updateFactura_PagamentoParcelado;
DROP TRIGGER IF EXISTS _updateCaixa_ContaCorrente;
DROP TRIGGER IF EXISTS _updateFacturaPagamento_Avista;



DELIMITER //
CREATE TRIGGER Tg_reservaStock_validar
BEFORE INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    DECLARE disponivel_atual INT DEFAULT 0;

    SELECT COALESCE((
        SELECT saldo - reservado
        FROM stock
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
        LIMIT 1
    ), 0)
    INTO disponivel_atual;

    IF NEW.quantidade <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Quantidade reservada deve ser maior que zero';
    END IF;

    IF disponivel_atual < NEW.quantidade THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para reservar requisicao';
    END IF;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_reservaStock
AFTER INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_reservaStock_estado
AFTER UPDATE ON reserva_stock
FOR EACH ROW
BEGIN
    IF OLD.estado = 'Reservado' AND NEW.estado IN ('Consumido', 'Liberado', 'Cancelado') THEN
        UPDATE stock
        SET reservado = GREATEST(reservado - OLD.quantidade, 0)
        WHERE idProduto = OLD.idProduto
          AND idArmazem = OLD.idArmazem;
    ELSEIF OLD.estado IN ('Consumido', 'Liberado', 'Cancelado') AND NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_estadoRequisicao
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    UPDATE requisicao
    SET estado = 'Submetida'
    WHERE id = NEW.idRequisicao
      AND estado = 'Pendente';
END//
DELIMITER ;



DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
	-- Registra movimento de entrada de lote
	INSERT INTO movimento (tipoOrigem, tipoMovimento, dataMovimento, quantidade, idArmazem, idProduto, idLote)
		VALUES ('Compra', 'Entrada', NEW.dataEntrada, NEW.quantidadeAtual, NEW.idArmazem, NEW.idProduto, NEW.id);
END//
DELIMITER ;



DELIMITER //
CREATE TRIGGER Tg_actualizaStock_IO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE totalMovimentado INT;
    DECLARE estoque_atual INT;

    SET totalMovimentado = NEW.quantidade;

    SELECT saldo INTO estoque_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem
    LIMIT 1;

    -- Apenas ENTRADAS
    IF NEW.tipoMovimento = 'Entrada' THEN
        UPDATE stock
        SET saldo = estoque_atual + totalMovimentado
        WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;








DELIMITER //
CREATE TRIGGER Tg_FIFO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE qtdRestante INT;
    DECLARE loteID INT;
    DECLARE qtdLote INT;
    DECLARE done INT DEFAULT 0;

    DECLARE curLotes CURSOR FOR
        SELECT id, quantidadeAtual
        FROM lote
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
          AND quantidadeAtual > 0
        ORDER BY dataEntrada ASC;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    IF NEW.tipoOrigem = 'Requisicao' AND NEW.tipoMovimento = 'Saida' THEN
        SET qtdRestante = NEW.quantidade;

        OPEN curLotes;

        read_loop: LOOP
            FETCH curLotes INTO loteID, qtdLote;
            IF done = 1 OR qtdRestante <= 0 THEN
                LEAVE read_loop;
            END IF;

            IF qtdLote >= qtdRestante THEN
                -- Retira o restante do lote atual
                UPDATE lote
                SET quantidadeAtual = quantidadeAtual - qtdRestante,
                    estado = CASE WHEN quantidadeAtual - qtdRestante = 0 THEN 'Esgotado' ELSE estado END
                WHERE id = loteID;

                -- Registra no log
                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdRestante);

                SET qtdRestante = 0;
            ELSE
                -- Lote esgotado completamente
                UPDATE lote
                SET quantidadeAtual = 0,
                    estado = 'Esgotado'
                WHERE id = loteID;

                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdLote);

                SET qtdRestante = qtdRestante - qtdLote;
            END IF;
        END LOOP;

        CLOSE curLotes;

        -- Atualiza saldo geral no stock
         UPDATE stock
          SET saldo = saldo - NEW.quantidade
          WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;

        IF qtdRestante > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente para completar a saÃ­da (FIFO).';
        END IF;
    END IF;
END//
DELIMITER ;

delimiter //
CREATE TRIGGER Tg_C_Stock
	AFTER INSERT ON produto
	FOR EACH ROW
	BEGIN
		IF NEW.controle_stock THEN
			INSERT INTO stock (idProduto, idArmazem, saldo)
			SELECT NEW.id, id, 0 FROM armazem;
		END IF;
	END//
delimiter ;


DELIMITER //
CREATE TRIGGER copiar_saldo_para_lote
BEFORE INSERT ON lote
FOR EACH ROW
BEGIN
    -- DeclaraÃ§Ã£o de uma variÃ¡vel para armazenar o saldo
    DECLARE saldo_atual integer;

    -- Busca o saldo atual do produto no armazÃ©m correspondente
    SELECT saldo
    INTO saldo_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem LIMIT 1;
    
    set NEW.quantidadeInicial= saldo_atual;
END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_C_Saida
AFTER INSERT ON itemfactura
FOR EACH ROW
BEGIN
    DECLARE qtd_restante INT;
    DECLARE qtd_total INT;
    DECLARE lote_id INT DEFAULT 0;
    DECLARE movimento_id INT;
    DECLARE qtd_lote INT;
    DECLARE qtd_saida INT;
    DECLARE saldo_atual INT DEFAULT 0;

    SET qtd_total = NEW.quantidade * NEW.qtd_por_unidade;
    SET qtd_restante = qtd_total;

    SELECT COALESCE((
        SELECT saldo
        FROM stock
        WHERE idProduto = NEW.fk_id_produto
          AND idArmazem = NEW.fk_id_armazem
        LIMIT 1
    ), 0)
    INTO saldo_atual;

    IF saldo_atual < qtd_total THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para concluir a venda';
    END IF;

    WHILE qtd_restante > 0 DO
        SET lote_id = 0;
        SET qtd_lote = 0;

        SELECT
            COALESCE((
                SELECT id
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0),
            COALESCE((
                SELECT quantidadeAtual
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0)
        INTO lote_id, qtd_lote;

        IF lote_id = 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Lote insuficiente para concluir a venda';
        END IF;

        SET qtd_saida = LEAST(qtd_lote, qtd_restante);

        UPDATE lote
        SET quantidadeAtual = quantidadeAtual - qtd_saida,
            estado = IF(quantidadeAtual - qtd_saida = 0, 'Esgotado', 'Ativo')
        WHERE id = lote_id;

        INSERT INTO movimento (
            tipoOrigem,
            tipoMovimento,
            dataMovimento,
            quantidade,
            idProduto,
            idArmazem,
            idLote
        ) VALUES (
            'Venda',
            'Saida',
            DATE(NEW.data_adicao),
            qtd_saida,
            NEW.fk_id_produto,
            NEW.fk_id_armazem,
            lote_id
        );

        SET movimento_id = LAST_INSERT_ID();

        INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
        VALUES (movimento_id, lote_id, qtd_saida);

        SET qtd_restante = qtd_restante - qtd_saida;
    END WHILE;

    UPDATE stock
    SET saldo = saldo - qtd_total
    WHERE idProduto = NEW.fk_id_produto
      AND idArmazem = NEW.fk_id_armazem;
END//
DELIMITER ;

delimiter //
create trigger _updateParcelasFactura
	after insert on parcelas
    for each row
    begin
		declare numero_de_parcelas int default 0;
        declare saldo decimal(10,2);
        
        select count(*) into numero_de_parcelas from parcelas where fk_factura = NEW.fk_factura;
        select SUM(valorParcela) into saldo from parcelas where fk_factura = NEW.fk_factura;
		update factura set numeroParcelas = numero_de_parcelas, condicaoPagemento = 'Parcelado', saldoPendente = saldo  where numero = new.fk_factura;
    end//
delimiter ;

delimiter //
create trigger _updateParcela_Factura
	after insert on transacoes
    for each row
    begin
        declare total_parcela decimal(10,2);
        declare total_pago decimal(10,2);
        declare condicao_pagemento varchar(30);
        declare transacoes_pagas decimal(10,2);
        declare saldo decimal(10,2);
        
        select f.saldoPendente into saldo from factura f where f.numero = NEW.fk_numero;
        SELECT p.valorParcela INTO total_parcela FROM parcelas p where p.id = NEW.fk_parcela;
        SELECT p.valorPagoParcela INTO total_pago FROM parcelas p where p.id = NEW.fk_parcela;
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valorPago) FROM transacoes t WHERE t.fk_parcela = p.id AND p.fk_factura =NEW.fk_numero  ) INTO transacoes_pagas 
		FROM parcelas p where p.id = NEW.fk_parcela;

        IF condicao_pagemento = 'Parcelado' THEN
			update parcelas set valorPagoParcela = transacoes_pagas  where id = new.fk_parcela;
        END IF;
        
        SELECT p.valorPagoParcela INTO total_pago FROM parcelas p where p.id = NEW.fk_parcela;
        IF total_parcela <= total_pago and condicao_pagemento = 'Parcelado' THEN
			update parcelas set estado = 'Paga', dataPagamento = NEW.data_pagamento where id = NEW.fk_parcela;
            update factura set saldoPendente = saldo - total_pago where numero = NEW.fk_numero;
        END IF;
        
	end//
delimiter ;

delimiter //
create trigger _updateFactura_PagamentoParcelado
	after update on parcelas
    for each row
	begin
		declare total_pago decimal(10,2);
        declare total_factura decimal(10,2);
        declare condicao_pagemento varchar(30); 
        
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_factura;
        SELECT f.total INTO total_factura from factura f where f.numero = NEW.fk_factura;
        SELECT SUM(p.valorPagoParcela) INTO total_pago FROM parcelas p where p.estado = 'Paga' AND p.fk_factura = NEW.fk_factura;
        
        IF condicao_pagemento = 'Parcelado'  THEN 
			update factura set totalPago = total_pago where numero = new.fk_factura;
        END IF;
        
        SELECT f.total INTO total_factura from factura f where f.numero = NEW.fk_factura;
        IF total_factura <= total_pago AND condicao_pagemento = 'Parcelado'  THEN 
			update factura set estado = 'Paga' where numero = new.fk_factura;
        END IF;		
    end//
delimiter ;

delimiter //
create trigger _updateCaixa_ContaCorrente
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
			-- Verifica se o mÃ©todo de pagamento Ã© igual a 1 (ajuste conforme necessÃ¡rio)
            IF NEW.fk_id_metodo_pagamento = 1 THEN
				-- Tenta buscar o saldo atual da Ãºltima linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM caixa
					ORDER BY id DESC LIMIT 1;

				-- Se nÃ£o encontrar saldo anterior, assume que o saldo inicial Ã© 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO caixa (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial Ã© o saldo atual anterior ou 0 se nÃ£o houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo Ã© o saldo anterior somado ao valor da transaÃ§Ã£o
					NEW.id                         -- FK da transaÃ§Ã£o inserida
					);
			ELSE
				-- Tenta buscar o saldo atual da Ãºltima linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM contacorrente
					ORDER BY id DESC LIMIT 1;

				-- Se nÃ£o encontrar saldo anterior, assume que o saldo inicial Ã© 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO contacorrente (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial Ã© o saldo atual anterior ou 0 se nÃ£o houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo Ã© o saldo anterior somado ao valor da transaÃ§Ã£o
					NEW.id                         -- FK da transaÃ§Ã£o inserida
					);				
            END IF;
	end//
delimiter ;

delimiter //
create trigger _updateFacturaPagamento_Avista
	after insert on transacoes
    for each row
    begin
        declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        declare transacoes_pagas decimal(10,2);
        declare condicao_pagemento varchar(30); 
        
        SELECT f.total INTO total_factura FROM factura f where f.numero = NEW.fk_numero;
		SELECT f.totalPago INTO total_pago FROM factura f where f.numero =  NEW.fk_numero;
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valorPago) FROM transacoes t WHERE t.fk_numero = f.numero) INTO transacoes_pagas 
		FROM factura f 
		where f.numero = NEW.fk_numero;
        
        IF total_factura > total_pago and condicao_pagemento = 'A vista' THEN
			update factura set totalPago=transacoes_pagas, saldoPendente=(total_factura-transacoes_pagas) where numero=NEW.fk_numero; 
        END IF;
        
        SELECT f.totalPago INTO total_pago FROM factura f where f.numero =  NEW.fk_numero;
        IF total_factura = total_pago and condicao_pagemento = 'A vista' THEN
			update factura set estado = 'Paga' where numero = NEW.fk_numero; 
        END IF;        
	end//
delimiter ;

SHOW TRIGGERS;
