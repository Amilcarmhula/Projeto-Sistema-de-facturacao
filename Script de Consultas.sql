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

SELECT f.numero AS numero_factura, f.data_emissao, f.data_vencimento, f.total, f.totalPago, f.saldoPendente, f.estado, f.condicaoPagemento,
					GROUP_CONCAT(DISTINCT m.nome SEPARATOR '; ') AS metodoPagamento,
                    doc.sigla, doc.nome as documento,
                    cli.razaosocial, cli.nuit as nuit_cliente,
                    ec.avenida,ec.rua,ec.numero,ec.bairro,ec.cidade,ec.provincia,ec.codigo_postal,
                    cc.contacto AS contatos_cliente,cc.email as email_cliente, 
                    e.nome as nome_empresa, e.razaosocial AS razao_empresa, e.nuit as nuit_empresa, 
                    en.avenida as avenida_e,en.rua as rua_e,en.numero as numero_e,
                    en.bairro as bairro_e,en.cidade as cidade_e,en.provincia as provincia_e,en.codigo_postal as codigo_postal_e,
                    ce.contacto AS contatos_empresa, 
                    ce.email as email_empresa, ce.website,
                    a.nome_banco, a.numero as numeroConta, a.nib, a.nuib 
                    FROM factura f 
                    LEFT JOIN transacoes t ON t.fk_numero = f.numero
                    LEFT JOIN metodo_pagamento m ON m.id = t.fk_id_metodo_pagamento
                    LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
                    LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
                    LEFT JOIN contacto_cliente cc ON cc.fk_id_cliente = cli.id 
                    LEFT JOIN endereco_cliente ec ON ec.fk_id_cliente = cli.id 
                    LEFT JOIN empresa e ON f.fk_id_empresa=e.id 
                    LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa
                    LEFT JOIN contacto_empresa ce ON e.id = ce.fk_id_empresa 
                    LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa 
                    WHERE f.numero = '2024-480/2'



     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

