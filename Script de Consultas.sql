use _sysface;
-- CALL atualizar_estado_precovenda();
SET GLOBAL event_scheduler = ON;

select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-11-8' where id = 1;
  
select * from factura;
select * from transacoes;
select * from parcelas;
select id from parcelas order by id desc limit 1;
select * from itemfactura;
select * from taxa_imposto;
select * from contacorrente;
select * from caixa;


drop PROCEDURE procedure_updateSaldoFactura_com_Atraso_de_Parcela;
drop EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela;

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

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

