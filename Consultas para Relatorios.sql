use _sysface;
CALL atualizar_estado_precovenda();

insert into transacoes(data_pagamento,valor,tipo_transacao,
    tipo_pagamento, descricao,fk_id_contabancaria,
    fk_id_metodo_pagamento, fk_numero, fk_id_usuario) value ('2024-10-22',1000,'Entrada','caixa',
    'Primeira parcela de pagamentto',1,1,'2024-162/2',1);

select * from transacoes;
select * from factura;
select * from caixa;
select * from contacorrente;

-- Primeiro metodo pronto
SELECT 
	 SUM(i.subtotal) AS total_fatura 
    FROM factura f 
	LEFT JOIN itemfactura i ON i.fk_numero = f.numero 
	where f.numero = '2024-615/2';
    
SELECT f.numero, f.data_criacao, f.data_vencimento, f.total, f.estado, f.fk_id_empresa, f.fk_id_cliente, f.fk_id_usuario, f.fk_id_doc,
		(SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) AS totalPago 
		FROM factura f 
        where f.numero = '2024-830/2';

SELECT (SELECT SUM(i.subtotal) FROM itemfactura i WHERE i.fk_numero = f.numero) AS total_fatura
		FROM factura f 
        where f.numero = '2024-315/2';

SELECT (SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) AS totalPago 
		FROM factura f 
        where f.numero = '2024-162/2';

select * from transacoes;
select * from factura;
select * from caixa;
select * from contacorrente;
drop trigger _updateestadofactura;
drop trigger _updateCaixa;
drop trigger _updateContacorrente;


insert into metodo_pagamento(nome, descricao) values 
('Dinheiro (Espécie)','O método mais tradicional de pagamento, usando moedas e cédulas físicas.'),
('Pagamento Terminal POS','Utilizado em estabelecimentos comerciais para processar pagamentos feitos com cartões de crédito, débito e, em alguns casos, carteiras móveis via NFC ou QR Code'),
('Transferências Bancárias','Movimentação direta entre contas da mesma ou diferentes instituição financeira.'),
('Carteiras Móveis','Plataformas que armazenam informações de pagamento e permitem a realização de transações por meio de dispositivos móveis.'),
('Pagamentos com Cheques','Um método de pagamento em que o emissor autoriza o banco a pagar um valor específico ao beneficiário');


delimiter //
create trigger _updateestadofactura
	after insert on transacoes
    for each row
	begin
		declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        
        SELECT (SELECT SUM(i.subtotal) FROM itemfactura i WHERE i.fk_numero = f.numero) INTO total_factura
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) INTO total_pago 
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        IF total_factura = total_pago THEN
			update factura set estado=1 where numero=NEW.fk_numero;
        END IF;
        
    end//
delimiter ;


delimiter //
create trigger _updateCaixa
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
        declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        
        SELECT (SELECT SUM(i.subtotal) FROM itemfactura i WHERE i.fk_numero = f.numero) INTO total_factura
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) INTO total_pago 
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        IF total_factura <> total_pago THEN
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
					saldo_anterior + NEW.valor,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);
			END IF;
        END IF;
	end//
delimiter ;

-- conta corrente
delimiter //
create trigger _updateContacorrente
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
        declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        
        SELECT (SELECT SUM(i.subtotal) FROM itemfactura i WHERE i.fk_numero = f.numero) INTO total_factura
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) INTO total_pago 
		FROM factura f 
        where f.numero = NEW.fk_numero;
        
        IF total_factura <> total_pago THEN
			-- Verifica se o método de pagamento é igual a 1 (ajuste conforme necessário)
            IF NEW.fk_id_metodo_pagamento <> 1 THEN
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
					saldo_anterior + NEW.valor,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);
            END IF;
            
        END IF;
	end//
delimiter ;         
                    
                     

                     
                    
                    
                    
                    

     
     
     
     
     
     

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

