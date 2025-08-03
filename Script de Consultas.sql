use _sysface;
select * from cliente;




select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-11-8' where id = 3;
  
select * from factura;
select * from movimento;
select * from parcelas;
select id from parcelas order by id desc limit 1;
select * from itemfactura;
select * from itemcotacao;
select * from taxa_imposto;
select * from contacorrente;
select * from caixa;

SELECT  t.fk_parcela as refParcela, t.id, t.valorRecebido, t.valorPago, t.trocos, t.data_pagamento, t.data_criacao, t.descricao, m.nome 
                    from transacoes t 
                    LEFT JOIN metodo_pagamento m ON m.id=t.fk_id_metodo_pagamento 
                    where fk_numero='2024-980/1' order by fk_parcela;



     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

