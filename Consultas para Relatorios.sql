use _sysface;
-- CALL atualizar_estado_precovenda();


select curdate() + interval 65 day as Datas_de_parcelas;



select count(*) from parcelas where fk_factura = '2024-845/2';

  
select * from factura;
select * from transacoes;


update parcelas set valorPagoParcela = 0.00, estado = 'Pendente' where id = 3;

select * from parcelas;


select * from itemfactura;
select * from caixa;

select * from contacorrente;




        
SELECT (SELECT SUM(t.valorPago) FROM transacoes t WHERE t.fk_parcela = p.id AND p.fk_factura ='2024-860/4'  ) as transacoes_pagas 
		FROM parcelas p where p.id =3;

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

