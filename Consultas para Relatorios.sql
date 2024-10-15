use _sysface;
CALL atualizar_estado_precovenda();
select * from endereco_empresa;
update endereco_empresa set avenida='Av. 24 de Julho' where id=1;
desc empresa;

update empresa set apelido='JM-Tecnologias Soc.Unipe.Lda' where id = 1;

SELECT 
		-- Dados da Factura e Cliente
		f.numero as numero_factura, f.data_criacao, f.data_vencimento,
        doc.sigla, doc.nome as documento,
        cli.razaosocial, cli.nuit as nuit_cliente,
        CONCAT(ec.avenida,', ',ec.rua,', ',ec.numero,'; ',ec.bairro,', ',ec.cidade,', ',ec.provincia,'- ',ec.codigo_postal ) as endereco_cliente,
        GROUP_CONCAT(DISTINCT cc.contacto ) AS contatos_cliente,cc.email as email_cliente, 
        
        -- Dados da Empresa
        e.nome as nome_empresa, e.razaosocial, e.nuit as nuit_empresa, 
        CONCAT(en.avenida,', ',en.rua,', ',en.numero,'; ',en.bairro,', ',en.cidade,', ',en.provincia,'- ',en.codigo_postal ) as endereco_empresa,
        GROUP_CONCAT(DISTINCT c.contacto) AS contatos_empresa, 
        c.email as email_empresa, c.website,
        a.nome_banco, a.numero as numeroConta, a.nib, a.nuib 
        
        FROM factura f 
        -- JOIN com Factura, Documentos e Cliente
        LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
        LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
        LEFT JOIN contacto_cliente cc ON cc.fk_id_cliente = cli.id 
        LEFT JOIN endereco_cliente ec ON ec.fk_id_cliente = cli.id 
        -- JOIN com Empresa
        LEFT JOIN empresa e ON f.fk_id_empresa=e.id
        LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa 
		LEFT JOIN contacto_empresa c ON e.id = c.fk_id_empresa 
        LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa
        WHERE f.numero = '2024-1010/2';
        
-- Consultas de dados da Factura
SELECT p.nome, p.descricao, pv.preco, 
                    i.quantidade, i.subtotal, 
                    ip.identificador as descricao_imposto, ip.percentagem AS imposto, 
                    d.nome AS descricao_desconto, d.percentagem AS desconto, 
                    f.numero 
                    FROM itemfactura i 
                    LEFT JOIN factura f ON f.numero = i.fk_numero 
                    LEFT JOIN desconto d ON d.id = i.fk_id_desconto 
                    LEFT JOIN produto p ON p.id = i.fk_id_produto 
                    LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id 
                    LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto 
                    WHERE i.fk_numero = '2024-1010/2';
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    

-- Consultas obsoletas
SELECT 
		-- Dados da Factura e Cliente
		f.numero as numero_factura, f.data_criacao, f.data_vencimento,
        doc.sigla, doc.nome as documento,
        cli.apelido as razao_cliente, cli.nuit as nuit_cliente,
        CONCAT(ec.avenida,', ',ec.rua,', ',ec.numero,'; ',ec.bairro,', ',ec.cidade,', ',ec.provincia,'- ',ec.codigo_postal ) as endereco_cliente,
        GROUP_CONCAT(DISTINCT cc.contacto ) AS contatos_cliente,cc.email as email_cliente, 
        
        -- Dados da Empresa
        e.nome as nome_empresa, e.apelido AS razao_empresa, e.nuit as nuit_empresa, 
        CONCAT(en.avenida,', ',en.rua,', ',en.numero,'; ',en.bairro,', ',en.cidade,', ',en.provincia,'- ',en.codigo_postal ) as endereco_empresa,
        GROUP_CONCAT(DISTINCT c.contacto) AS contatos_empresa, 
        c.email as email_empresa, c.website,
        a.nome_banco, a.numero as numeroConta, a.nib, a.nuib ,
        
        -- Dados dos totais
        -- Calculo de total sem taxa nem desconto
		sum(i.quantidade * pv.preco)/2 as subtotal,
		-- Calculando o valor total do desconto
		sum((((i.quantidade * pv.preco) * ip.percentagem)+i.quantidade * pv.preco) * d.percentagem)/2 AS desconto,
		-- Calculando o valor total do imposto
		sum(i.quantidade * pv.preco * ip.percentagem)/2 AS taxas,
		-- Soma do total geral
		sum(i.subtotal)/2 as total_geral
        
        FROM factura f 
        -- JOIN com Factura, Documentos e Cliente
        LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
        LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
        LEFT JOIN contacto_cliente cc ON cc.fk_id_cliente = cli.id 
        LEFT JOIN endereco_cliente ec ON ec.fk_id_cliente = cli.id 
        -- JOIN com Empresa
        LEFT JOIN empresa e ON f.fk_id_empresa=e.id
        LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa 
		LEFT JOIN contacto_empresa c ON e.id = c.fk_id_empresa 
        LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa
        -- JOIN com tabelas para totais
        LEFT JOIN itemfactura i ON i.fk_numero = f.numero
		LEFT JOIN desconto d ON d.id = i.fk_id_desconto
		LEFT JOIN produto p ON p.id = i.fk_id_produto
		LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id
		LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto  
        -- WHERE f.numero = $P{NUMEROFACTURA};
        WHERE f.numero = '2024-777/2';
        
      -- AMBIENTE DE TESTE:  
SELECT 
    -- Dados da Factura e Cliente
    f.numero as numero_factura, f.data_criacao, f.data_vencimento,
    doc.sigla, doc.nome as documento,
    cli.apelido as razao_cliente, cli.nuit as nuit_cliente,
    CONCAT(ec.avenida,', ',ec.rua,', ',ec.numero,'; ',ec.bairro,', ',ec.cidade,', ',ec.provincia,'- ',ec.codigo_postal ) as endereco_cliente,
    GROUP_CONCAT(DISTINCT cc.contacto ) AS contatos_cliente, cc.email as email_cliente, 
    
    -- Dados da Empresa
    e.nome as nome_empresa, e.apelido AS razao_empresa, e.nuit as nuit_empresa, 
    CONCAT(en.avenida,', ',en.rua,', ',en.numero,'; ',en.bairro,', ',en.cidade,', ',en.provincia,'- ',en.codigo_postal ) as endereco_empresa,
    GROUP_CONCAT(DISTINCT c.contacto) AS contatos_empresa, 
    c.email as email_empresa, c.website,
    a.nome_banco, a.numero as numeroConta, a.nib, a.nuib,
    
    -- Dados dos totais
    sum(DISTINCT i.quantidade * pv.preco) as subtotal,
	SUM(DISTINCT ((i.quantidade * pv.preco * (1 + ip.percentagem)) * d.percentagem)) AS desconto,
    sum(DISTINCT i.quantidade * pv.preco * ip.percentagem) AS taxas,
    sum(i.subtotal)/2 as total_geral
    
FROM factura f 
LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id 
LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente 
LEFT JOIN contacto_cliente cc ON cc.fk_id_cliente = cli.id 
LEFT JOIN endereco_cliente ec ON ec.fk_id_cliente = cli.id 
LEFT JOIN empresa e ON f.fk_id_empresa=e.id
LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa 
LEFT JOIN contacto_empresa c ON e.id = c.fk_id_empresa 
LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa
LEFT JOIN itemfactura i ON i.fk_numero = f.numero
LEFT JOIN desconto d ON d.id = i.fk_id_desconto
LEFT JOIN produto p ON p.id = i.fk_id_produto
LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id
LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto  
WHERE f.numero = '2024-777/2'
-- WHERE f.numero = $P{NUMEROFACTURA}
GROUP BY f.numero;



    
    

  
     

                    
                    
                     

                     
                    
                    
                    
                    

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

