use _sysface;
select * from cliente;

SELECT p.id as ID_Prod, p.nome, p.codigo_barra, p.descricao, p.controle_stock, p.nivelstock, 
                    c.id as id_categoria, c.familia as categoria,
                    u.id as id_unidade, u.identificador, u.sigla,
                    t.id as id_taxa, t.identificador as taxa, t.percentagem,
                    s.saldo, s.fk_id_armazem, 
                    a.nome AS nome_armazem, a.estado AS estado_armazem 
                    FROM produto p 
                    LEFT JOIN categoria c ON c.id = p.fk_id_categoria 
                    LEFT JOIN unidade_medida u ON u.id = p.fk_id_unidade_medida 
                    LEFT JOIN taxa_imposto t ON t.id = p.fk_id_taxa_imposto 
                    LEFT JOIN stock s ON s.fk_id_produto=p.id 
                    LEFT JOIN armazem a ON a.id = s.fk_id_armazem;


select curdate() + interval 65 day as Datas_de_parcelas;

update parcelas set dataVencimento = '2024-11-8' where id = 3;
  
select * from produto;
select * from armazem;
select * from movimento;
select * from stock;
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



     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

