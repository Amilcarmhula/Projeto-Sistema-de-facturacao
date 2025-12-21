use _sysface;

DELIMITER //
CREATE TRIGGER trg_precovenda_after_produtodesconto
AFTER INSERT ON produtodesconto
FOR EACH ROW
BEGIN
    DECLARE preco_base DECIMAL(10,2);
    DECLARE preco_venda DECIMAL(10,2);
    DECLARE imposto_val DECIMAL(10,2);
    DECLARE desconto_val DECIMAL(10,2);
    DECLARE preco_final DECIMAL(10,2);
    DECLARE id_imposto INT;

    -- 1) Desactivar preços anteriores do mesmo produto e armazém
    UPDATE precovenda
    SET estado = 'desactivo'
    WHERE idProduto = NEW.idProduto
      AND idArmazem = NEW.idArmazem
      AND estado = 'activo';

    -- 2) Buscar o preço base e o imposto associado ao produto
    SELECT ppa.preco, pr.fk_id_imposto
    INTO preco_base, id_imposto
    FROM precoProdutoArmazem ppa
    JOIN produto pr ON pr.id = ppa.idProduto
    WHERE ppa.idProduto = NEW.idProduto
      AND ppa.idArmazem = NEW.idArmazem;

    -- 3) Calcular imposto
    IF id_imposto IS NOT NULL THEN
        SELECT IFNULL(percentagem,0) INTO imposto_val
        FROM imposto
        WHERE id = id_imposto;
        SET imposto_val = preco_base * (imposto_val / 100);
    ELSE
        SET imposto_val = 0;
    END IF;

    -- 4) Calcular desconto
    SET desconto_val = preco_base * (NEW.percentagem / 100);

    -- 5) Calcular preços
    SET preco_venda = preco_base + imposto_val;
    SET preco_final = preco_venda - desconto_val;

    -- 6) Inserir novo preço de venda como activo
    INSERT INTO precovenda (
        precoNormal,
        precoVenda,
        precoFinal,
        dataValidade,
        estado,
        idProduto,
        idArmazem,
        fk_id_usuario,
        data_criacao
    ) VALUES (
        preco_base,
        preco_venda,
        preco_final,
        NEW.dataFim,
        'activo',
        NEW.idProduto,
        NEW.idArmazem,
        1,   -- ajustar: ID do usuário logado
        NOW()
    );
END;
//
DELIMITER ;


DELIMITER //


-- Trigger AFTER UPDATE em produtodesconto
CREATE TRIGGER trg_precovenda_update_produtodesconto
AFTER UPDATE ON produtodesconto
FOR EACH ROW
BEGIN
    DECLARE preco_base DECIMAL(10,2);
    DECLARE preco_venda DECIMAL(10,2);
    DECLARE imposto_val DECIMAL(10,2);
    DECLARE desconto_val DECIMAL(10,2);
    DECLARE preco_final DECIMAL(10,2);
    DECLARE id_imposto INT;

    -- Buscar o preço base e o imposto associado ao produto
    SELECT ppa.preco, pr.fk_id_imposto
    INTO preco_base, id_imposto
    FROM precoProdutoArmazem ppa
    JOIN produto pr ON pr.id = ppa.idProduto
    WHERE ppa.idProduto = NEW.idProduto
      AND ppa.idArmazem = NEW.idArmazem;

    -- Calcular imposto
    IF id_imposto IS NOT NULL THEN
        SELECT IFNULL(percentagem,0) INTO imposto_val
        FROM imposto
        WHERE id = id_imposto;
        SET imposto_val = preco_base * (imposto_val / 100);
    ELSE
        SET imposto_val = 0;
    END IF;

    -- Calcular desconto atualizado
    SET desconto_val = preco_base * (NEW.percentagem / 100);

    -- Calcular preços
    SET preco_venda = preco_base + imposto_val;
    SET preco_final = preco_venda - desconto_val;

    -- Atualizar precovenda correspondente
    UPDATE precovenda
    SET 
        precoNormal = preco_base,
        precoVenda = preco_venda,
        precoFinal = preco_final,
        dataValidade = NEW.dataFim,
        data_criacao = NOW()
    WHERE idProduto = NEW.idProduto
      AND idArmazem = NEW.idArmazem
      AND dataValidade = OLD.dataFim;
END;
//

DELIMITER ;



-- Triggers para controlo de estoque e registo de movimentos(Saidas & Entradas)
-- Automatiza a preparação de stock para o novo produto em todos os armazéns existentes, evitando esquecimentos manuais
-- delimiter //
-- CREATE TRIGGER Tg_C_Stock
-- 	AFTER INSERT ON produto
-- 	FOR EACH ROW
-- 	BEGIN
-- 		IF NEW.controle_stock THEN
-- 			INSERT INTO stock (fk_id_produto, fk_id_armazem, saldo)
-- 			SELECT NEW.id, id, 0 FROM armazem;
-- 		END IF;
-- 	END//
-- delimiter ;

-- Quando um produto é fornecido, atualizar o stock automaticamente no armazém indicado
-- Automatiza o registo de entrada de stock
-- Mantém histórico de movimentação
-- Garante consistência entre fornecimento e saldo de stock

-- DELIMITER //
-- CREATE TRIGGER Tg_CU_Stock_Entrada
-- AFTER INSERT ON produto_fornecido
-- FOR EACH ROW
-- BEGIN
--     -- Todas as variáveis precisam estar no início do bloco
--     DECLARE tipoProduto VARCHAR(20);
--     DECLARE done INT DEFAULT 0;
--     DECLARE comp_id INT;
--     DECLARE comp_qtd INT;
--     DECLARE total_unidades INT;

--     -- Declara cursor e handler também no início
--     DECLARE cur CURSOR FOR
--         SELECT idSimples, quantidadePorUnidade
--         FROM produto_composicao
--         WHERE idComposto = NEW.fk_id_produto;

--     DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

--     -- Descobre se o produto fornecido é simples ou composto
--     SELECT tipo INTO tipoProduto
--     FROM produto
--     WHERE id = NEW.fk_id_produto;

--     IF tipoProduto = 'Simples' THEN
--         -- Entrada direta no produto simples
--         INSERT INTO stock (fk_id_produto, fk_id_armazem, saldo)
--         VALUES (NEW.fk_id_produto, NEW.fk_id_armazem, NEW.quantidade)
--        -- ON DUPLICATE KEY UPDATE saldo = saldo + NEW.quantidade*NEW.qtd_por_unidade;
--         ON DUPLICATE KEY UPDATE saldo = saldo + NEW.quantidade*NEW.qtd_por_unidade;

--         -- Registra movimento
--         INSERT INTO movimento (
--             tipo_movimento, data_movimento, quantidade,
--             tipoUnidadeMov, uniPorTipoMove, fk_id_produto,
--             fk_id_usuario, fk_id_armazem
--         ) VALUES (
--             'Entrada', NEW.data_fornecida, NEW.quantidade,
--             NEW.unidade_fornecida, NEW.qtd_por_unidade,
--             NEW.fk_id_produto, NEW.fk_id_usuario, NEW.fk_id_armazem
--         );

--     ELSEIF tipoProduto = 'Composto' THEN
--         -- Entrada de produto composto: converter em simples
--         OPEN cur;
--         read_loop: LOOP
--             FETCH cur INTO comp_id, comp_qtd;
--             IF done THEN
--                 LEAVE read_loop;
--             END IF;

--             -- Total de unidades a lançar no stock simples
--             SET total_unidades = NEW.quantidade * comp_qtd;

--             -- Atualiza stock do produto simples
--             INSERT INTO stock (fk_id_produto, fk_id_armazem, saldo)
--             VALUES (comp_id, NEW.fk_id_armazem, total_unidades)
--             ON DUPLICATE KEY UPDATE saldo = saldo + total_unidades;

--             -- Registra movimento com referência ao simples
--             INSERT INTO movimento (
--                 tipo_movimento, data_movimento, quantidade,
--                 tipoUnidadeMov, uniPorTipoMove, fk_id_produto,
--                 fk_id_usuario, fk_id_armazem
--             ) VALUES (
--                 'Entrada', NEW.data_fornecida, NEW.quantidade,
--                 NEW.unidade_fornecida, comp_qtd,
--                 comp_id, NEW.fk_id_usuario, NEW.fk_id_armazem
--             );
--         END LOOP;
--         CLOSE cur;
--     END IF;
-- END//
-- DELIMITER ;


-- Quando um produto é vendido ou movimentado como "Saída", atualizar o stock.
-- Automatiza a subtração do stock com base na movimentação
-- Suporta múltiplas unidades (ex: caixas, pacotes)
-- DELIMITER //
-- CREATE TRIGGER Tg_U_Stock_Saida
-- AFTER INSERT ON movimento
-- FOR EACH ROW
-- BEGIN
--     DECLARE total_unidades INT;
--     DECLARE estoque_atual INT;

--     SET total_unidades = NEW.quantidade * NEW.uniPorTipoMove;

--     -- Busca o saldo atual do produto no armazém
--     SELECT saldo
--     INTO estoque_atual
--     FROM stock
--     WHERE fk_id_produto = NEW.fk_id_produto
--       AND fk_id_armazem = NEW.fk_id_armazem
--     LIMIT 1;

--     -- Verifica se é saída e se há estoque suficiente
--     IF NEW.tipo_movimento = 'Saída' AND estoque_atual >= total_unidades THEN
--         UPDATE stock
--         SET saldo = saldo - total_unidades
--         WHERE fk_id_produto = NEW.fk_id_produto
--           AND fk_id_armazem = NEW.fk_id_armazem;
--     END IF;
-- END//
-- DELIMITER ;

-- drop trigger Tg_C_Saida;
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

