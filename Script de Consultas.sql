use _sysface;
-- CALL atualizar_estado_precovenda();
SET GLOBAL event_scheduler = ON;

select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-11-8' where id = 5;
  
select * from precovenda;
select * from transacoes;
select * from parcelas;
select * from itemfactura;
select * from taxa_imposto;
select * from contacorrente;

update factura set numeroParcelas=0 where numero='2024-913/1';



SELECT p.nome, p.descricao, pv.precoNormal, 
                    i.quantidade, i.subtotal, 
                    ip.identificador as descricao_imposto, ip.percentagem AS imposto, 
                    d.identificador AS descricao_desconto, d.percentagem AS desconto, 
                    f.numero 
                    FROM itemfactura i 
                    LEFT JOIN factura f ON f.numero = i.fk_numero 
                    LEFT JOIN desconto d ON d.id = i.fk_id_desconto 
                    LEFT JOIN produto p ON p.id = i.fk_id_produto 
                    LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id and pv.estado='activo'
                    LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto 
                    WHERE i.fk_numero = '2024-270/1'


     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

