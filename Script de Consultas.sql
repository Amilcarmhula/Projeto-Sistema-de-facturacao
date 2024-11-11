use _sysface;
-- CALL atualizar_estado_precovenda();
SET GLOBAL event_scheduler = ON;

select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-11-8' where id = 2;
  
select * from factura;
select * from transacoes;
select * from parcelas;
select id from parcelas order by id desc limit 1;
select * from itemfactura;
select * from itemcotacao;
select * from taxa_imposto;
select * from contacorrente;
select * from caixa;



     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

