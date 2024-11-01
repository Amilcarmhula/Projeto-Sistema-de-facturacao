use _sysface;
-- CALL atualizar_estado_precovenda();
SET GLOBAL event_scheduler = ON;

select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-10-29' where id = 6;
  
select * from factura;
select * from transacoes;
select * from parcelas;
select * from itemfactura;
select * from caixa;
select * from contacorrente;



select 
	p.id, p.parcelaNumero, p.taxaJuros, p.taxaDeAtraso, (p.valorParcela*p.taxaDeAtraso)+p.valorParcela as valorParcela, p.valorPagoParcela, p.dataVencimento, p.dataPagamento, p.estado, p.fk_factura, p.fk_usuario
    from parcelas p where fk_factura='2024-284/4';

select SUM((p.valorParcela*p.taxaDeAtraso)+p.valorParcela) AS saldo from parcelas p 
LEFT JOIN factura f on f.numero = p.fk_factura
	group by f.numero;


delimiter //
create procedure _updateSaldoFactura_com_Atraso_de_Parcela()
    begin 
    UPDATE factura f
    SET f.saldoPendente = (
        SELECT COALESCE(SUM((p.valorParcela * p.taxaDeAtraso) + p.valorParcela), 0)
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    );
    end//
delimiter ;
-- # Automatizando com um Evento
-- Para evitar a execução manual, você pode criar um evento que execute o procedimento periodicamente 
-- (por exemplo, diariamente). Veja como fazer isso:
CREATE EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela
ON SCHEDULE EVERY 1 DAY
DO
	CALL _updateSaldoFactura_com_Atraso_de_Parcela();



     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

