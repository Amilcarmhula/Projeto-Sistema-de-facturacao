use _sysface;

-- Inicio de Trigger e procedimentos para tratamento de precos
DELIMITER //
create trigger trg_antesDeInserirPreco
BEFORE INSERT ON precoProdutoArmazem
FOR EACH ROW
BEGIN
    DECLARE desconto_taxa DECIMAL(4,2);
    DECLARE imposto_taxa DECIMAL(4,2);

    -- imposto (exemplo simples)
    SELECT i.percentagem
    INTO imposto_taxa
    FROM imposto i
    JOIN produto p ON p.idImposto = i.id
    WHERE p.id = NEW.idProduto
    LIMIT 1;

    IF imposto_taxa IS NULL THEN
        SET imposto_taxa = 0;
    END IF;

    -- preço venda
    SET NEW.precoVenda = NEW.precoBase + ((NEW.precoBase * imposto_taxa) / 100);

    -- desconto activo
    SELECT d.percentagem
    INTO desconto_taxa
    FROM produtodesconto pd
    JOIN desconto d ON d.id = pd.idDesconto
    WHERE pd.idProduto = NEW.idProduto
      AND pd.idArmazem = NEW.idArmazem
      AND CURRENT_DATE BETWEEN pd.dataInicio AND pd.dataFim
    LIMIT 1;

    -- preço final
    IF desconto_taxa IS NOT NULL THEN
        SET NEW.precoFinal =
            NEW.precoVenda - ((NEW.precoVenda * desconto_taxa) / 100);
    ELSE
        SET NEW.precoFinal = NEW.precoVenda;
    END IF;

    SET NEW.estado = 'activo';
    SET NEW.dataInicioVigencia = CURRENT_DATE;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_registar_preco (
    IN p_idProduto INT,
    IN p_idArmazem INT,
    IN p_precoBase DECIMAL(10,2)
)
BEGIN
    START TRANSACTION;

    -- 1️ desactivar preço activo anterior
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo',
        dataFimVigencia = CURRENT_DATE
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo';

    -- 2️ inserir novo preço
    INSERT INTO precoProdutoArmazem
    (idProduto, idArmazem, precoBase)
    VALUES
    (p_idProduto, p_idArmazem, p_precoBase);

    COMMIT;
END;
//
DELIMITER ;

-- CALL sp_registar_preco(1, 1, 500);
-- CALL sp_registar_preco(1, 1, 550);
-- CALL sp_registar_preco(1, 1, 700);

SET GLOBAL event_scheduler = ON;
CREATE EVENT evt_marcar_expiracao_precos
ON SCHEDULE EVERY 1 DAY
DO
UPDATE precoProdutoArmazem
SET dataFimVigencia = CURRENT_DATE
WHERE estado = 'activo'
  AND dataFimVigencia IS NOT NULL
  AND dataFimVigencia < CURRENT_DATE;
  
-- Fim de Trigger e procedimentos para tratamento de precos

-- Para Controlo de Stock
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
    -- Declaração de uma variável para armazenar o saldo
    DECLARE saldo_atual integer;

    -- Busca o saldo atual do produto no armazém correspondente
    SELECT saldo
    INTO saldo_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem LIMIT 1;
    
    set NEW.quantidadeInicial= saldo_atual;
END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
	-- Registra movimento de entrada de lote
	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idLote) 
		VALUES ('Entrada', NEW.dataEntrada, NEW.quantidadeAtual,NEW.idArmazem, NEW.idProduto, NEW.id);
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
CREATE TRIGGER Tg_RegistaMovimentoSaida
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    -- Registra movimento de saide de lote
	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idItemRequisicao) 
		VALUES ('Saida', CURDATE(), NEW.quantidade,NEW.idArmazem, NEW.idProduto, NEW.id);
        
	-- update requisicao set estado = 'Em processamento' where idItemRequisicao=NEW.idRequisicao;

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

    IF NEW.tipoMovimento = 'Saida' THEN
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
            SET MESSAGE_TEXT = 'Stock insuficiente para completar a saída (FIFO).';
        END IF;
    END IF;
END//
DELIMITER ;

-- Para Controlo de Stock


-- Trigger para controlar fluxo de stock(saldo de determinado produto em seu armazarm) quando um movimento de saida e registado na tabela itemfactura
DELIMITER //
CREATE TRIGGER Tg_C_Saida
AFTER INSERT ON itemfactura
FOR EACH ROW
BEGIN
    DECLARE total_unidades INT;
    DECLARE estoque_atual INT;

    -- Calcula a quantidade total (ex.: caixas × unidades por caixa)
    SET total_unidades = NEW.quantidade * NEW.qtd_por_unidade;

    -- Busca saldo atual do produto no armazém
    SELECT saldo
    INTO estoque_atual
    FROM stock
    WHERE fk_id_produto = NEW.fk_id_produto
      AND fk_id_armazem = NEW.fk_id_armazem
    LIMIT 1;

    -- Só insere o movimento se houver saldo suficiente
    IF estoque_atual IS NOT NULL AND estoque_atual >= total_unidades THEN
        INSERT INTO movimento (
            tipo_movimento,
            data_movimento,
            quantidade,
            tipoUnidadeMov,
            uniPorTipoMove,
            fk_id_produto,
            fk_id_usuario,
            fk_id_armazem
        )
        VALUES (
            'Saída',
            NEW.data_adicao,
            NEW.quantidade,
            NEW.unidade_vendida,
            NEW.qtd_por_unidade,
            NEW.fk_id_produto,
            NEW.fk_id_usuario,
            NEW.fk_id_armazem
        );
    END IF;
END//
DELIMITER ;

-- ****************************************************************



-- Trigger para actualizar o numero de parcelas nas facturas quando elas vao ser pagas por parcelamento
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




-- Trigger para actualizar dados da parcela como valorPagoParcela e o estado; e saldo pendente da factura
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


-- Trigger para actualizar totalPago da factura e estado da factura conforme as parcelas form pagas
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


-- Trigger actualiza os saldos iniciais e actual de Caixa e ContaCorrente quando transacoes sao registadas;
delimiter //
create trigger _updateCaixa_ContaCorrente
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
			-- Verifica se o método de pagamento é igual a 1 (ajuste conforme necessário)
            IF NEW.fk_id_metodo_pagamento = 1 THEN
				-- Tenta buscar o saldo atual da última linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM caixa
					ORDER BY id DESC LIMIT 1;

				-- Se não encontrar saldo anterior, assume que o saldo inicial é 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO caixa (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial é o saldo atual anterior ou 0 se não houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);
			ELSE
				-- Tenta buscar o saldo atual da última linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM contacorrente
					ORDER BY id DESC LIMIT 1;

				-- Se não encontrar saldo anterior, assume que o saldo inicial é 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO contacorrente (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial é o saldo atual anterior ou 0 se não houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);				
            END IF;
	end//
delimiter ;


-- Este trigger actualiza total pago da factura, saldo pendente e estado da factura quando a condicao de pagamento e a vista
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



-- _______________________________________________________________________________________________________________PROCEDIMENTOS ARMACENADOS____________________________________________________________________________________________________________________

-- Procedimento para ajustar e manter o controle dos precos de um produto. O procedimento e chamado atraves de um evento que e executado diariamente
DELIMITER //
CREATE PROCEDURE procedure_updatePrecoVenda()
BEGIN
    UPDATE precovenda
    SET estado = 'desactivo'
    WHERE dataValidade <= CURDATE() AND estado = 'activo';
END//
DELIMITER ;

-- Eventro para chamar o procedimento procedure_updatePrecoVenda()
CREATE EVENT evento_updatePrecoVenda
	ON SCHEDULE EVERY 1 DAY
	DO
		CALL procedure_updatePrecoVenda();
        
-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar o estado das parcelas se estao atrasadas ou nao

DELIMITER //
CREATE PROCEDURE procedure_updateEstadoParcela()
BEGIN
    UPDATE parcelas
    SET estado = 'Atrasada'
    WHERE dataVencimento <= CURDATE() AND estado <> 'Paga';
END//
DELIMITER ;

CREATE EVENT evento_updateEstadoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateEstadoParcela();
    

-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar a taxa de juros em atraso das parcelas
DELIMITER //
CREATE PROCEDURE procedure_updateTaxaAtrasoParcela()
BEGIN
    UPDATE parcelas
    SET taxaDeAtraso = taxaJuros / 2
    WHERE estado = 'Atrasada';
END //
DELIMITER ;

CREATE EVENT evento_updateTaxaAtrasoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateTaxaAtrasoParcela();        


-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar a taxa de juros em atraso das parcelas
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

CREATE EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateSaldoFactura_com_Atraso_de_Parcela();

