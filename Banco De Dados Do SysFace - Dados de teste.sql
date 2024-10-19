use _sysface;
-- Dados da Factura
SELECT 
    p.nome, 
    p.descricao, 
    i.quantidade, 
    pv.preco, 
    ip.identificador, 
    ip.percentagem AS taxa_imposto, 
    d.identificador AS titulo_desconto, 
    d.percentagem AS percentagem_desconto,
    i.subtotal
FROM 
    factura f
    LEFT JOIN itemfactura i ON i.fk_numero = f.numero
    LEFT JOIN desconto d ON d.id = i.fk_id_desconto
    LEFT JOIN produto p ON p.id = i.fk_id_produto
    LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id
    LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto
WHERE 
    f.numero = '2024-1010/2';
--     
-- -- Calculo de totais para fecho da factura
SELECT 
    -- Calculo de total sem taxa nem desconto
    sum(ii.quantidade * pv2.preco) as subtotal,
    
    -- Calculando o valor total do desconto
    sum((((ii.quantidade * pv2.preco) * ip2.percentagem)+ii.quantidade * pv2.preco) * dd.percentagem) AS desconto,
    
    -- Calculando o valor total do imposto
    sum(ii.quantidade * pv2.preco * ip2.percentagem) AS taxas,
    -- Soma do total geral
    sum(ii.subtotal) as total_geral
FROM 
    factura ff
    LEFT JOIN itemfactura ii ON ii.fk_numero = ff.numero
    LEFT JOIN desconto dd ON dd.id = ii.fk_id_desconto
    LEFT JOIN produto pp ON pp.id = ii.fk_id_produto
    LEFT JOIN precovenda pv2 ON pv2.fk_id_produto = pp.id
    LEFT JOIN taxa_imposto ip2 ON ip2.id = pp.fk_id_taxa_imposto    
WHERE 
    ff.numero = '2024-1010/2';
--     
-- -- Dados do cliente da factura
-- SELECT f.numero, f.data_criacao, f.data_vencimento,
-- 	dc.nome as tipo,
-- 	c.apelido, c.nuit,
--     CONCAT( e.avenida, ', ',e.rua, ', ',e.numero, '; ',e.bairro, '-,',e.cidade, ', ',e.provincia, '-',e.codigo_postal ) as endereco,
-- 	GROUP_CONCAT(DISTINCT ct.contacto SEPARATOR ' ') AS contatos,ct.email
-- FROM 
--     factura f
--     LEFT JOIN documentocomercial dc ON f.fk_id_doc = dc.id
--     LEFT JOIN cliente c ON c.id = f.fk_id_cliente
--     LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id
--     LEFT JOIN endereco_cliente e ON e.fk_id_cliente = c.id
-- WHERE 
--     f.numero = '2024-262/3';
-- select * from itemfactura;

















INSERT INTO cliente (tipo, nome, razaosocial, nuit, fk_id_usuario)
VALUES
('Pessoa Física', 'Carlos Mendes', 'Carlinhos', 123456789, 1),
('Pessoa Jurídica', 'Tech Solutions', 'Tech Soluções Ltda', 987654321, 1),
('Pessoa Física', 'Maria Ferreira', 'Ferreirinha', 456789123, 1),
('Pessoa Jurídica', 'Construtora Ideal', 'Ideal Construtora Ltda', 147258369, 1),
('Pessoa Física', 'João Silva', 'Marcos', 852963741, 1),
('Pessoa Física', 'Ana Gomes', 'Aninha', 321654987, 1),
('Pessoa Jurídica', 'Supermercado Bom Preço', 'Bom Preço Ltda', 654987321, 1),
('Pessoa Jurídica', 'Transporte Rápido', 'TransRápido Ltda', 963852741, 1),
('Pessoa Física', 'Fernanda Oliveira', 'Nanda', 741852963, 1),
('Pessoa Física', 'Roberto Souza', 'Beto', 369258147, 1);


-- Dados de endereços
INSERT INTO endereco_cliente (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_cliente, fk_id_usuario)
VALUES
('Maputo', 'Maputo', 'Alto Maé', '25 de Setembro', ' 1', 1100, 123, 'Facturação', 1, 1),
('Maputo', 'Maputo', 'Polana', 'Julius Nyerere', ' 2', 1200, 456, 'Envio', 2, 1),
('Sofala', 'Beira', 'Munhava', 'Samora Machel', ' 3', 2200, 789, 'Facturação', 3, 1),
('Nampula', 'Nampula', 'Namicopo', 'Eduardo Mondlane', ' 4', 3300, 101, 'Envio', 4, 1),
('Maputo', 'Matola', 'T3', '4 de Outubro', ' 5', 1400, 202, 'Facturação', 5, 1),
('Inhambane', 'Inhambane', 'Chilwane', 'Patrice Lumumba', ' 6', 5500, 303, 'Envio', 6, 1),
('Gaza', 'Xai-Xai', 'Chongoene', 'dos Heróis', ' 7', 6600, 404, 'Facturação', 7, 1),
('Cabo Delgado', 'Pemba', 'Paquitequete', '25 de Junho', ' 8', 7700, 505, 'Envio', 8, 1),
('Zambézia', 'Quelimane', 'Sagrada Família', 'Marginal', ' 9', 8800, 606, 'Facturação', 9, 1),
('Manica', 'Chimoio', '3 de Fevereiro', '7 de Abril', ' 10', 9900, 707, 'Envio', 10, 1);

-- Inserir dados fictícios na tabela contacto_cliente
INSERT INTO contacto_cliente (pessoa_de_contacto, contacto, email, website, fk_id_cliente, fk_id_usuario)
VALUES
('Carlos Mendes', '841234567', 'carlos.mendes@exemplo.com', 'www.carlosmendes.com', 1, 1),
('Ana Joaquim', '842345678', 'ana.joaquim@exemplo.com', 'www.anajoaquim.com', 2, 1),
('Pedro Langa', '843456789', 'pedro.langa@exemplo.com', 'www.pedrolanga.com', 3, 1),
('Maria Xavier', '844567890', 'maria.xavier@exemplo.com', 'www.mariaxavier.com', 4, 1),
('Joaquim Sambo', '845678901', 'joaquim.sambo@exemplo.com', 'www.joaquimsambo.com', 5, 1),
('Rita Mabunda', '846789012', 'rita.mabunda@exemplo.com', 'www.ritamabunda.com', 6, 1),
('Fernando Tamele', '847890123', 'fernando.tamele@exemplo.com', 'www.fernandotamele.com', 7, 1),
('Cláudia Nhampossa', '848901234', 'claudia.nhampossa@exemplo.com', 'www.claudianhampossa.com', 8, 1),
('Lucas Bique', '849012345', 'lucas.bique@exemplo.com', 'www.lucasbique.com', 9, 1),
('Sónia Cossa', '841012345', 'sonia.cossa@exemplo.com', 'www.soniacossa.com', 10, 1);



-- Inserir mais 10 contatos fictícios na tabela contacto_cliente
INSERT INTO contacto_cliente (pessoa_de_contacto, contacto, email, website, fk_id_cliente, fk_id_usuario)
VALUES
('José Chongo', '841234568', 'jose.chongo@exemplo.com', 'www.josechongo.com', 1, 1),
('Patrícia Domingos', '842345679', 'patricia.domingos@exemplo.com', 'www.patriciadomingos.com', 2, 1),
('Manuel Macamo', '843456780', 'manuel.macamo@exemplo.com', 'www.manuelmacamo.com', 3, 1),
('Carla Magalhães', '844567891', 'carla.magalhaes@exemplo.com', 'www.carlamagalhaes.com', 4, 1),
('Paulo Samito', '845678902', 'paulo.samito@exemplo.com', 'www.paulosamito.com', 5, 1),
('Helena Mutemba', '846789013', 'helena.mutemba@exemplo.com', 'www.helenamutemba.com', 6, 1),
('Sérgio Moiane', '847890124', 'sergio.moiane@exemplo.com', 'www.sergiomoiane.com', 7, 1),
('Eunice Dhlakama', '848901235', 'eunice.dhlakama@exemplo.com', 'www.eunicedhlakama.com', 8, 1),
('Luís Nhaca', '849012346', 'luis.nhaca@exemplo.com', 'www.luisnhaca.com', 9, 1),
('Mónica Mucheto', '841012346', 'monica.mucheto@exemplo.com', 'www.monicamucheto.com', 10, 1);


INSERT INTO fornecedor (tipo, razaosocial, nuit, fk_id_usuario) VALUES
('Pessoa Jurídica', 'Alpha Distribuidora LTDA', 123456789, 1),
('Pessoa Jurídica', 'Beta Comércio e Serviços', 987654321, 1),
('Pessoa Física', 'Carlos Eduardo Silva', 789456123, 1),
('Pessoa Jurídica', 'Delta Engenharia S.A.', 741258963, 1),
('Pessoa Jurídica', 'Epsilon Equipamentos Industriais', 321654987, 1),
('Pessoa Física', 'Fernanda Maria Oliveira', 852963741, 1),
('Pessoa Jurídica', 'Gamma Transportes LTDA', 963852741, 1),
('Pessoa Jurídica', 'Omega Soluções Ltda', 654987321, 1),
('Pessoa Física', 'Marcos Antônio Freitas', 147258369, 1),
('Pessoa Jurídica', 'Sigma Construções Eireli', 951753852, 1),
('Pessoa Jurídica', 'Zeta Comércio de Alimentos', 369258147, 1),
('Pessoa Jurídica', 'Theta Soluções Tecnológicas', 258369147, 1),
('Pessoa Jurídica', 'Iota Importação e Exportação', 753951456, 1),
('Pessoa Física', 'Luciana Almeida de Souza', 456123789, 1),
('Pessoa Jurídica', 'Lambda Serviços Automotivos', 159753456, 1);


INSERT INTO endereco_fornecedor (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_fornecedor, fk_id_usuario) VALUES
('Maputo', 'Maputo', ' Central', ' 25 de Setembro', ' das Acácias', 1101, 23, 'Comercial', 1, 1),
('Maputo', 'Matola', ' Liberdade', ' da Liberdade', ' das Flores', 1102, 45, 'Comercial', 2, 1),
('Gaza', 'Xai-Xai', ' Patrice Lumumba', ' Eduardo Mondlane', ' do Comércio', 1201, 12, 'Comercial', 3, 1),
('Inhambane', 'Maxixe', ' Maxixe', ' 7 de Abril', ' do Mar', 1301, 5, 'Comercial', 4, 1),
('Sofala', 'Beira', 'Ponta-Gêa', ' Samora Machel', ' da Beira', 1401, 78, 'Comercial', 5, 1),
('Manica', 'Chimoio', ' 25 de Setembro', ' 25 de Setembro', ' da Paz', 1501, 18, 'Comercial', 6, 1),
('Zambézia', 'Quelimane', ' Sinacura', ' Maguiguana', ' Zambeze', 1601, 35, 'Comercial', 7, 1),
('Nampula', 'Nampula', ' Muhala', ' das Industrias', ' 24 de Julho', 1701, 10, 'Comercial', 8, 1),
('Cabo Delgado', 'Pemba', ' Cariaco', ' dos Heróis', ' dos Pescadores', 1801, 27, 'Comercial', 9, 1),
('Niassa', 'Lichinga', ' Lichinga', ' da República', ' das Montanhas', 1901, 50, 'Comercial', 10, 1),
('Tete', 'Tete', ' Matundo', ' do Zambeze', ' do Mercado', 2001, 8, 'Comercial', 11, 1),
('Maputo', 'Matola', ' T-3', ' Julius Nyerere', ' do Lazer', 1103, 120, 'Comercial', 12, 1),
('Gaza', 'Chokwe', ' Ngala', ' Samora Machel', ' Agrária', 1202, 36, 'Comercial', 13, 1),
('Inhambane', 'Inhambane', ' Cemento', ' da Independência', ' do Farol', 1302, 15, 'Comercial', 14, 1),
('Manica', 'Manica', ' 7 de Abril', ' 7 de Abril', ' das Mangueiras', 1502, 70, 'Comercial', 15, 1);


INSERT INTO contacto_fornecedor (pessoa_de_contacto, contacto, email, website, fk_id_fornecedor, fk_id_usuario) VALUES
('João Almeida', '823456789', 'joao.almeida@alphadistribuidora.com', 'www.alphadistribuidora.com', 1, 1),
('Maria Silva', '843456789', 'maria.silva@alphadistribuidora.com', NULL, 1, 1),
('Pedro Santos', '863456789', NULL, 'www.alphaditribuidora.com', 1, 1),

('Ana Costa', '823987654', 'ana.costa@betacomercio.com', 'www.betacomercio.com', 2, 1),
('Carlos Sousa', '843987654', NULL, 'www.betcomercio.com', 2, 1),

('Paulo Fernandes', '829876543', 'paulo.fernandes@carloseduardo.com', NULL, 3, 1),

('Fernanda Lopes', '825643987', 'fernanda.lopes@deltaengenharia.com', 'www.deltaengenharia.com', 4, 1),
('Roberto Garcia', '845643987', NULL, 'www.delaengenharia.com', 4, 1),

('Isabel Pinto', '824789123', 'isabel.pinto@epsilonindustriais.com', NULL, 5, 1),

('Ricardo Oliveira', '824563217', 'ricardo.oliveira@fernandamaria.com', NULL, 6, 1),

('Claudia Souza', '826543987', 'claudia.souza@gammatransportes.com', 'www.gammatransportes.com', 7, 1),
('José Lima', '846543987', NULL, 'www.gammatansportes.com', 7, 1),

('Leonardo Castro', '827654321', 'leonardo.castro@omegasolucoes.com', 'www.omegasolucoes.com', 8, 1),
('Gabriela Rocha', '847654321', NULL, NULL, 8, 1),

('Mateus Gonçalves', '826432198', 'mateus.goncalves@sigmaeireli.com', NULL, 9, 1);


INSERT INTO armazem (nome, tipo, descricao, estado) VALUES
('Armazém Central Maputo', 'Central', 'Principal em Maputo', 1),
('Armazém Regional Beira', 'Regional', 'Operações Sofala', 1),
('Armazém Loja Matola', 'Loja', 'Suporte Matola', 1),
('Armazém Central Nampula', 'Central', 'Principal em Nampula', 1),
('Armazém Regional Tete', 'Regional', 'Operações Tete', 1),
('Armazém Loja Xai-Xai', 'Loja', 'Suporte Gaza', 1),
('Armazém Central Pemba', 'Central', 'Principal em Cabo Delgado', 1),
('Armazém Regional Chimoio', 'Regional', 'Operações Manica', 1);


INSERT INTO endereco_armazem (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_armazem, fk_id_usuario) VALUES
('Maputo', 'Maputo', ' Central', ' 25 de Setembro', ' das Acácias', 1101, 23, 'Comercial', 1, 1),
('Sofala', 'Beira', ' Ponta-Gêa', ' Samora Machel', ' da Beira', 1401, 10, 'Comercial', 2, 1),
('Maputo', 'Matola', ' Liberdade', ' da Liberdade', ' das Flores', 1102, 45, 'Comercial', 3, 1),
('Nampula', 'Nampula', ' Muhala', ' das Industrias', ' 24 de Julho', 1701, 5, 'Comercial', 4, 1),
('Tete', 'Tete', ' Matundo', ' do Zambeze', ' do Mercado', 2001, 12, 'Comercial', 5, 1),
('Gaza', 'Xai-Xai', ' Patrice Lumumba', ' Eduardo Mondlane', ' do Comércio', 1201, 36, 'Comercial', 6, 1),
('Cabo Delgado', 'Pemba', ' Cariaco', ' dos Heróis', ' dos Pescadores', 1801, 27, 'Comercial', 7, 1),
('Manica', 'Chimoio', ' 25 de Setembro', ' 25 de Setembro', ' da Paz', 1501, 18, 'Comercial', 8, 1);
select * from contacto_armazem;

INSERT INTO contacto_armazem (pessoa_de_contacto, contacto, email, fk_id_armazem, fk_id_usuario) VALUES
('João Pereira', '823456789', 'joao.pereira@centralmaputo.com', 1, 1),
('Ana Martins', '843456789', 'ana.martins@regionalbeira.com', 2, 1),
('Carlos Silva', '823987654', 'carlos.silva@loja.matola.com', 3, 1),
('Pedro Andrade', '863456789', 'pedro.andrade@centralnampula.com', 4, 1),
('Mariana Costa', '823654987', 'mariana.costa@regionaltete.com', 5, 1),
('Luís Ferreira', '829876543', 'luis.ferreira@lojaxaixai.com', 6, 1),
('Rita Lopes', '823123456', 'rita.lopes@centralpemba.com', 7, 1),
('Fábio Nunes', '847654321', 'fabio.nunes@regionalchimoio.com', 8, 1);


INSERT INTO categoria (tipo, grupo, subgrupo, familia, descricao) VALUES
('Produtos', 'Alimentares', 'Bebidas', 'Refrigerantes', 'Categoria de bebidas não alcoólicas'),
('Produtos', 'Alimentares', 'Congelados', 'Carnes', 'Categoria de carnes congeladas'),
('Produtos', 'Eletrônicos', 'Computadores', 'Laptops', 'Categoria de laptops e notebooks'),
('Serviços', 'Manutenção', 'Equipamentos', 'Computadores', 'Serviços de manutenção de computadores'),
('Serviços', 'Consultoria', 'TI', 'Infraestruturas', 'Consultoria em infraestrutura de TI');


INSERT INTO unidade_medida (identificador, sigla, descricao)
VALUES 
('Unidade', 'Un', 'Unidade padrao'),
('Quilograma', 'kg', 'Unidade de massa usada no sistema métrico'),
('Litro', 'L', 'Unidade de volume usada para medir líquidos'),
('Metro', 'm', 'Unidade de comprimento usada no sistema métrico'),
('Centímetro', 'cm', 'Unidade menor de comprimento no sistema métrico'),
('Mililitro', 'ml', 'Unidade de volume usada para pequenas quantidades de líquidos'),
('Grama', 'g', 'Unidade de massa usada para pequenas quantidades'),
('Tonelada', 't', 'Unidade de massa usada para grandes quantidades'),
('Polegada', 'in', 'Unidade de comprimento usada no sistema imperial'),
('Galão', 'gal', 'Unidade de volume usada no sistema imperial'),
('Onça', 'oz', 'Unidade de peso usada no sistema imperial');


INSERT INTO taxa_imposto (identificador, percentagem, descricao) VALUES
('IVA', 0.17, 'Imposto sobre o valor acrescentado (IVA)'),
('ISPC', 0.5, 'Imposto Simplificado para Pequenos Contribuintes (ISPC)'),
('Taxa Reduzida', 0.075, 'Taxa de imposto reduzida para produtos essenciais'),
('Isento', 0.00, 'Produtos isentos de imposto');





-- SELECT s.id, fk_id_categoria, s.nome,
--                      (SELECT grupo FROM categoria WHERE s.fk_id_categoria = categoria.id) AS grupo,
--                      (SELECT identificador FROM taxa_imposto WHERE s.fk_id_taxa_imposto = taxa_imposto.id) AS taxa,
--                      s.descricao, s.active_or_not FROM servico s;
-- select * from unidade_medida where id = 2;
-- select id, nome, preco, codigo_barra,fk_id_taxa_imposto, fk_id_unidade_medida,descricao from produto;
-- select id, nome, preco, codigo_barra,
-- 	(select identificador from taxa_imposto where fk_id_taxa_imposto=taxa_imposto.id),
--     (select sigla from unidade_medida where fk_id_unidade_medida=unidade_medida.id ),descricao 
--    from produto;
-- OR
-- SELECT p.id, p.nome, p.preco, p.codigo_barra,
--                      (SELECT identificador FROM taxa_imposto WHERE p.fk_id_taxa_imposto = taxa_imposto.id) AS identificador_taxa,
--                      (SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,
--                     p.fk_id_categoria, p.fk_id_taxa_imposto, p.fk_id_unidade_medida, p.data_criacao, p.active_or_not 
--                      FROM produto p;
                     
-- SELECT p.id, p.nome, p.preco, p.codigo_barra, p.descricao,
--                      (SELECT identificador FROM taxa_imposto WHERE p.fk_id_taxa_imposto = taxa_imposto.id) AS identificador_taxa,
--                      (SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,
--                      p.active_or_not FROM produto p;
                     

-- Produto: Refrigerante Coca-Cola
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Coca-Cola 2L', '7894900011517', 'Refrigerante gaseificado Coca-Cola 2L', TRUE, 1, 3, 1);

-- Produto: Carne Bovina Congelada
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Carne Bovina Congelada', '1234567890123', 'Carne bovina congelada embalada em 1kg', TRUE, 2, 2, 4);

-- Produto: Laptop Dell Inspiron
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Laptop Dell Inspiron 15', '884116211649', 'Laptop Dell Inspiron de 15 polegadas', TRUE, 3, 1, 1);

-- Serviço: Manutenção de Computador
INSERT INTO produto (nome, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Serviço de Manutenção de Computador', 'Serviço de manutenção e reparo de computadores', FALSE, 4, 1, 4);

-- Serviço: Consultoria em Infraestrutura de TI
INSERT INTO produto (nome, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Consultoria em Infraestrutura de TI', 'Serviço de consultoria em infraestrutura de redes e servidores', FALSE, 5, 1, 2);

-- Produto: Fanta Laranja 350ml
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Fanta Laranja 350ml', '7891000100105', 'Refrigerante sabor laranja em lata de 350ml', TRUE, 1, 6, 1);

-- Produto: Frango Congelado Inteiro 1kg
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Frango Congelado Inteiro 1kg', '7891234567890', 'Frango congelado inteiro, embalado em pacotes de 1kg', TRUE, 2, 2, 4);

-- Produto: MacBook Pro 16''
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('MacBook Pro 16"', '190198246271', 'Laptop Apple MacBook Pro de 16 polegadas', TRUE, 3, 1, 1);

-- Produto: Coca-Cola 500ml
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Coca-Cola 500ml', '7894900011518', 'Refrigerante Coca-Cola em garrafa de 500ml', TRUE, 1, 6, 1);

-- Produto: Teclado Gamer Mecânico
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Teclado Gamer Mecânico', '7899999000010', 'Teclado mecânico RGB para gamers', TRUE, 3, 1, 1);

-- Produto: Carne Suína Congelada 1kg
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Carne Suína Congelada 1kg', '7891010101011', 'Carne suína congelada embalada em pacotes de 1kg', TRUE, 2, 2, 4);

-- Produto: Monitor Samsung 27''
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Monitor Samsung 27"', '8801643258293', 'Monitor LED Samsung de 27 polegadas', TRUE, 3, 1, 1);

-- Produto: Serviço de Atualização de Software
INSERT INTO produto (nome, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Serviço de Atualização de Software', 'Serviço de instalação e atualização de softwares', FALSE, 4, 1, 4);

-- Produto: Água Mineral 500ml
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Água Mineral 500ml', '7894321000074', 'Água mineral natural sem gás em garrafa de 500ml', TRUE, 1, 6, 3);

-- Produto: Consultoria em Segurança da Informação
INSERT INTO produto (nome, descricao, contole_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_taxa_imposto)
VALUES ('Consultoria em Segurança da Informação', 'Serviço de consultoria para implementação de políticas de segurança da informação', FALSE, 5, 1, 2);


-- INSERT INTO precovenda (preco, data_inicio, data_fim, estado, fk_id_produto, fk_id_usuario) VALUES 
-- (60.00, '2024-09-17', '2024-09-18', 1, 1, 1), 
-- (350.00, '2024-09-17', '2024-09-19', 1, 2, 1), 
-- (35000.00, '2024-09-17', '2024-09-20', 1, 3, 1), 
-- (1500.00, '2024-09-17', NULL, 1, 4, 1), 
-- (5000.00, '2024-09-17', NULL, 1, 5, 1), 
-- (30.00, '2024-09-17', '2024-09-21', 1, 6, 1), 
-- (400.00, '2024-09-17', '2024-09-22', 1, 7, 1), 
-- (45000.00, '2024-09-17', '2024-09-23', 1, 8, 1), 
-- (40.00, '2024-09-17', '2024-09-24', 1, 9, 1), 
-- (5000.00, '2024-09-17', '2024-09-25', 1, 10, 1), 
-- (375.00, '2024-09-17', '2024-09-26', 1, 11, 1), 
-- (12000.00, '2024-09-17', '2024-09-27', 1, 12, 1), 
-- (800.00, '2024-09-17', NULL, 1, 13, 1), 
-- (20.00, '2024-09-17', '2024-09-28', 1, 14, 1), 
-- (6000.00, '2024-09-17', NULL, 1, 15, 1);

SELECT    
		SUM(CASE WHEN mp.tipo_movimento = 'Entrada' THEN mp.quantidade ELSE 0 END) as entradas,
		SUM(CASE WHEN mp.tipo_movimento = 'Saída' THEN mp.quantidade ELSE 0 END) AS saidas,
        MONTH(mp.data_movimento) AS mes,
		YEAR(mp.data_movimento) AS ano
        
        FROM movimento_produto mp 
        LEFT JOIN produto p ON p.id = mp.fk_id_produto
        LEFT JOIN categoria c ON c.id = p.fk_id_categoria
        LEFT JOIN armazem a ON a.id = mp.fk_id_armazem
        WHERE (NULL IS NULL OR c.id = NULL) AND (NULL IS NULL OR a.id = NULL) and mp.data_movimento=current_date()
       --  WHERE (1 IS NULL OR c.id = 1) AND (1 IS NULL OR a.id = 1) and mp.data_movimento=current_date()
        GROUP BY mes, ano;
        
SELECT    
		SUM(CASE WHEN mp.tipo_movimento = 'Entrada' THEN mp.quantidade ELSE 0 END) as entradas,
		SUM(CASE WHEN mp.tipo_movimento = 'Saída' THEN mp.quantidade ELSE 0 END) AS saidas,
        MONTH(mp.data_movimento) AS mes,
		YEAR(mp.data_movimento) AS ano
        
        FROM movimento_produto mp 
        LEFT JOIN produto p ON p.id = mp.fk_id_produto
        LEFT JOIN categoria c ON c.id = p.fk_id_categoria
        LEFT JOIN armazem a ON a.id = mp.fk_id_armazem
        WHERE (NULL IS NULL OR c.id = NULL) AND (NULL IS NULL OR a.id = NULL) and MONTH(mp.data_movimento)=MONTH(current_date()) and YEAR(mp.data_movimento)=YEAR(current_date())
       --  WHERE (1 IS NULL OR c.id = 1) AND (1 IS NULL OR a.id = 1) and mp.data_movimento=current_date()
        GROUP BY mes, ano;


select MONTH(current_date());        
SELECT * FROM movimento_produto; 
update movimento_produto set data_movimento = '2024-9-18' where id=3;
    