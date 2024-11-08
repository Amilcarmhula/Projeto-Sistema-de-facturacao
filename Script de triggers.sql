use _sysface;

-- Procedimento para ajustar e manter o controle dos precos de um produto. O procedimento e chamado atraves de um evento que e executado diariamente
DELIMITER //
CREATE PROCEDURE procedure_updatePrecoVenda()
BEGIN
    UPDATE precovenda
    SET estado = 'desactivo'
    WHERE data_fim <= CURDATE() AND estado = 'activo';
END//
DELIMITER ;

-- Eventro para chamar o procedimento procedure_updatePrecoVenda()
CREATE EVENT evento_updatePrecoVenda
	ON SCHEDULE EVERY 1 DAY
	DO
		CALL procedure_updatePrecoVenda();


-- Trigger para registar movimento de entrada apos registo de fornecimento de produto na tabela pro produto_fornecido
delimiter //
create trigger trigger_registaEntrada
	after insert on produto_fornecido
    for each row
    begin
		insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem) value ('Entrada',new.data_fornecida,new.quantidade,new.fk_id_produto,new.fk_id_usuario,new.fk_id_armazem);
    end//
delimiter ;

-- Trigger para controlar fluxo de stock(saldo de determinado produto em seu armazarm) quando um movimento de entrada ou saida e registado na tabela movimento_produto
DELIMITER //
CREATE TRIGGER trigger_updateSaldoStock
    AFTER INSERT ON movimento_produto
    FOR EACH ROW
BEGIN
    -- Verifica se a combinação de fk_id_produto e fk_id_armazem já existe na tabela stock
    IF NOT EXISTS (
        SELECT 1 
        FROM stock 
        WHERE fk_id_produto = NEW.fk_id_produto 
          AND fk_id_armazem = NEW.fk_id_armazem
    ) THEN
        -- Insere um novo registro com saldo inicial 0 caso a combinação não exista
        INSERT INTO stock (fk_id_produto, fk_id_armazem, saldo)
        VALUES (NEW.fk_id_produto, NEW.fk_id_armazem, 0);
    END IF;

    -- Atualiza o saldo com base nos movimentos registrados
    UPDATE stock 
    SET saldo = (
        SELECT 
            (SUM(CASE WHEN tipo_movimento = 'Entrada' THEN quantidade ELSE 0 END) - 
             SUM(CASE WHEN tipo_movimento = 'Saída' THEN quantidade ELSE 0 END)) AS saldo_produto
        FROM movimento_produto 
        WHERE fk_id_produto = NEW.fk_id_produto 
          AND fk_id_armazem = NEW.fk_id_armazem
    )
    WHERE fk_id_produto = NEW.fk_id_produto 
      AND fk_id_armazem = NEW.fk_id_armazem;
END//
DELIMITER ;

-- Trigger para controlar fluxo de stock(saldo de determinado produto em seu armazarm) quando um movimento de saida e registado na tabela itemfactura
delimiter //
create trigger trigger_registra_saida
	after insert on itemfactura
    for each row
    begin
		insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem)
        value ('Saída',new.data_adicao,new.quantidade,new.fk_id_produto,new.fk_id_usuario,new.fk_id_armazem);
    end//
delimiter ;

-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar o estado das parcelas se estao atrasadas ou nao
DELIMITER //
CREATE PROCEDURE procedure_updateEstadoParcela()
BEGIN
    UPDATE parcelas
    SET estado = 'Atrasada'
    WHERE dataVencimento <= CURDATE() AND estado <> 'Pago';
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


-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar a taxa de juros em atraso das parcelas
delimiter //
create procedure procedure_updateSaldoFactura_com_Atraso_de_Parcela()
    begin
    UPDATE factura f
    SET f.saldoPendente = (
        SELECT COALESCE(SUM((p.valorParcela * p.taxaDeAtraso) + p.valorParcela), 0)
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    );
    end//
delimiter ;

CREATE EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateSaldoFactura_com_Atraso_de_Parcela();

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

