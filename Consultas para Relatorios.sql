use _sysface;
CALL atualizar_estado_precovenda();

insert into transacoes(data_pagamento,valor,tipo_transacao,
    tipo_pagamento, descricao,fk_id_contabancaria,
    fk_id_metodo_pagamento, fk_numero, fk_id_usuario) value ('2024-10-22',1000,'Entrada','caixa',
    'Primeira parcela de pagamentto',1,1,'2024-162/2',1);

select * from transacoes;
select * from factura;

     SELECT f.numero AS numero_factura, f.data_criacao, f.data_vencimento, 
                    f.estado,
                    concat(doc.sigla,' - ', doc.nome) as Documento, 
                   -- sum(t.valor) as valorTransacao,
                    SUM(i.subtotal) AS total_fatura 
                    FROM factura f 
                    LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
                  --  LEFT JOIN transacoes t ON t.fk_numero=f.numero
                    LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
                    LEFT JOIN itemfactura i ON i.fk_numero = f.numero 
                    where cli.id=7
                    GROUP BY f.numero, f.data_criacao, f.data_vencimento, f.estado, 
                    doc.sigla, doc.nome 
                    ORDER BY f.numero;
                    
SELECT f.numero AS numero_factura, f.data_criacao, f.data_vencimento, f.estado,
       CONCAT(doc.sigla, ' - ', doc.nome) AS Documento, 
       -- Subconsulta para somar os subtotais dos itens de fatura
       (SELECT SUM(i.subtotal) FROM itemfactura i 
        WHERE i.fk_numero = f.numero) AS total_fatura,
       -- Subconsulta para somar o valor das transações relacionadas à fatura
       (SELECT SUM(t.valor) FROM transacoes t 
        WHERE t.fk_numero = f.numero) AS valor_transacoes
FROM factura f
LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
WHERE cli.id = 7
GROUP BY f.numero, f.data_criacao, f.data_vencimento, f.estado, doc.sigla, doc.nome 
ORDER BY f.numero;

SELECT  t.id, t.valor, t.data_pagamento, t.data_criacao, t.descricao, m.nome
	from transacoes t
	LEFT JOIN metodo_pagamento m ON m.id=t.fk_id_metodo_pagamento
 where fk_numero='2024-162/2';


                    
                    
                     

                     
                    
                    
                    
                    

     
     
     
     
     
     

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

