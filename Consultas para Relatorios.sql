use _sysface;
-- CALL atualizar_estado_precovenda();



select * from transacoes;
select * from factura;
select * from itemfactura;
select * from caixa;
select * from contacorrente;


        
SELECT f.total as total_factura FROM factura f where f.numero = '2024-511/4';
SELECT f.totalPago as TotalPagoFactura FROM factura f where f.numero = '2024-511/4';
SELECT f.saldoPendente as SaldoPendenteFactura FROM factura f where f.numero = '2024-511/4';
SELECT f.estado as EstadoFactura FROM factura f where f.numero = '2024-511/4';

delimiter //
create trigger _updateFactura
	after insert on transacoes
    for each row
    begin
        declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        declare saldo_pendente decimal(10,2);
        declare estado_factura varchar(30);
        declare transacoes_pagas decimal(10,2);
        
        SELECT f.total INTO total_factura FROM factura f where f.numero = NEW.fk_numero;
		SELECT f.totalPago INTO total_pago FROM factura f where f.numero =  NEW.fk_numero;
		SELECT f.saldoPendente INTO saldo_pendente FROM factura f where f.numero =  NEW.fk_numero;
		SELECT f.estado INTO estado_factura FROM factura f where f.numero =  NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valor) FROM transacoes t WHERE t.fk_numero = f.numero) INTO transacoes_pagas 
		FROM factura f 
		where f.numero = NEW.fk_numero;
        
        IF total_factura > total_pago THEN
			update factura set totalPago=transacoes_pagas, saldoPendente=(total_factura-transacoes_pagas) where numero=NEW.fk_numero; 
			-- Verifica se o método de pagamento é igual a 1 (ajuste conforme necessário)
            
	-- 	ELSE 
			-- IF total_factura = total_pago THEN
-- 				update factura set estado=1 where numero=NEW.fk_numero;
-- 			END IF;
        END IF;
	end//
delimiter ;
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

