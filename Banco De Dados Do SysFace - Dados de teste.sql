use _sysface;

INSERT INTO cliente (tipo, nome, razaosocial, nuit, fk_id_usuario)
VALUES
('Pessoa Jurídica', 'Tech Solutions', 'Tech Soluções Ltda', 987654321, 1),
('Pessoa Jurídica', 'Construtora Ideal', 'Ideal Construtora Ltda', 147258369, 1),
('Pessoa Física', 'João Silva', 'Marcos', 852963741, 1),
('Pessoa Jurídica', 'Transporte Rápido', 'TransRápido Ltda', 963852741, 1),
('Pessoa Física', 'Roberto Souza', 'Beto', 369258147, 1);


-- Dados de endereços
INSERT INTO endereco_cliente (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_cliente, fk_id_usuario)
VALUES
('Maputo', 'Maputo', 'Alto Maé', '25 de Setembro', ' 1', 1100, 123, 'Facturação', 1, 1),
('Maputo', 'Maputo', 'Polana', 'Julius Nyerere', ' 2', 1200, 456, 'Envio', 2, 1),
('Inhambane', 'Inhambane', 'Chilwane', 'Patrice Lumumba', ' 6', 5500, 303, 'Envio', 3, 1),
('Gaza', 'Xai-Xai', 'Chongoene', 'dos Heróis', ' 7', 6600, 404, 'Facturação', 4, 1),
('Cabo Delgado', 'Pemba', 'Paquitequete', '25 de Junho', ' 8', 7700, 505, 'Envio', 5, 1);

-- Inserir dados fictícios na tabela contacto_cliente
INSERT INTO contacto_cliente (pessoa_de_contacto, contacto, email, website, fk_id_cliente, fk_id_usuario)
VALUES
('Carlos Mendes', '841234567', 'carlos.mendes@exemplo.com', 'www.carlosmendes.com', 1, 1),
('Ana Joaquim', '842345678', 'ana.joaquim@exemplo.com', 'www.anajoaquim.com', 2, 1),
('Pedro Langa', '843456789', 'pedro.langa@exemplo.com', 'www.pedrolanga.com', 3, 1),
('Maria Xavier', '844567890', 'maria.xavier@exemplo.com', 'www.mariaxavier.com', 4, 1),
('Joaquim Sambo', '845678901', 'joaquim.sambo@exemplo.com', 'www.joaquimsambo.com', 5, 1);



-- Inserir mais 10 contatos fictícios na tabela contacto_cliente
INSERT INTO contacto_cliente (pessoa_de_contacto, contacto, email, website, fk_id_cliente, fk_id_usuario)
VALUES
('José Chongo', '841234568', 'jose.chongo@exemplo.com', 'www.josechongo.com', 1, 1),
('Patrícia Domingos', '842345679', 'patricia.domingos@exemplo.com', 'www.patriciadomingos.com', 2, 1),
('Manuel Macamo', '843456780', 'manuel.macamo@exemplo.com', 'www.manuelmacamo.com', 3, 1),
('Carla Magalhães', '844567891', 'carla.magalhaes@exemplo.com', 'www.carlamagalhaes.com', 4, 1),
('Paulo Samito', '845678902', 'paulo.samito@exemplo.com', 'www.paulosamito.com', 5, 1);


INSERT INTO fornecedor (tipo, razaosocial, nuit, fk_id_usuario) VALUES
('Pessoa Jurídica', 'Alpha Distribuidora LTDA', 123456789, 1),
('Pessoa Jurídica', 'Beta Comércio e Serviços', 987654321, 1),
('Pessoa Física', 'Carlos Eduardo Silva', 789456123, 1);


INSERT INTO endereco_fornecedor (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_fornecedor, fk_id_usuario) VALUES
('Maputo', 'Maputo', ' Central', ' 25 de Setembro', ' das Acácias', 1101, 23, 'Comercial', 1, 1),
('Maputo', 'Matola', ' Liberdade', ' da Liberdade', ' das Flores', 1102, 45, 'Comercial', 2, 1),
('Gaza', 'Xai-Xai', ' Patrice Lumumba', ' Eduardo Mondlane', ' do Comércio', 1201, 12, 'Comercial', 3, 1);


INSERT INTO contacto_fornecedor (pessoa_de_contacto, contacto, email, website, fk_id_fornecedor, fk_id_usuario) VALUES
('João Almeida', '823456789', 'joao.almeida@alphadistribuidora.com', 'www.alphadistribuidora.com', 1, 1),
('Ana Costa', '823987654', 'ana.costa@betacomercio.com', 'www.betacomercio.com', 2, 1),
('Fernanda Lopes', '825643987', 'fernanda.lopes@deltaengenharia.com', 'www.deltaengenharia.com', 3, 1);



INSERT INTO armazem (nome, tipo, descricao, estado) VALUES
('Armazém Central Maputo', 'Central', 'Principal em Maputo', 1),
('Armazém Regional Beira', 'Regional', 'Operações Sofala', 1),
('Armazém Loja Matola', 'Loja', 'Suporte Matola', 1);


INSERT INTO endereco_armazem (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, fk_id_armazem, fk_id_usuario) VALUES
('Maputo', 'Maputo', ' Central', ' 25 de Setembro', ' das Acácias', 1101, 23,  1, 1),
('Sofala', 'Beira', ' Ponta-Gêa', ' Samora Machel', ' da Beira', 1401, 10,  2, 1),
('Maputo', 'Matola', ' Liberdade', ' da Liberdade', ' das Flores', 1102, 45,  3, 1);


INSERT INTO contacto_armazem (pessoa_de_contacto, contacto, email, fk_id_armazem, fk_id_usuario) VALUES
('João Pereira', '823456789', 'joao.pereira@centralmaputo.com', 1, 1),
('Ana Martins', '843456789', 'ana.martins@regionalbeira.com', 2, 1),
('Carlos Silva', '823987654', 'carlos.silva@loja.matola.com', 3, 1);


INSERT INTO categoria (tipo, grupo, subgrupo, familia, descricao) VALUES
('Produtos', 'Alimentares', 'Bebidas', 'Refrigerantes', 'Categoria de bebidas não alcoólicas'),
('Produtos', 'Alimentares', 'Congelados', 'Carnes', 'Categoria de carnes congeladas'),
('Produtos', 'Eletrônicos', 'Computadores', 'Laptops', 'Categoria de laptops e notebooks'),
('Serviços', 'Manutenção', 'Equipamentos', 'Computadores', 'Serviços de manutenção de computadores'),
('Serviços', 'Consultoria', 'TI', 'Infraestruturas', 'Consultoria em infraestrutura de TI');


INSERT INTO unidade_medida (identificador, sigla, descricao)
VALUES 
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
('Taxa Reduzida', 0.075, 'Taxa de imposto reduzida para produtos essenciais');

INSERT INTO desconto (identificador, percentagem, descricao) VALUES
('Desconto de Lançamento', 0.1, 'Desconto aplicado em novos produtos no lançamento'),
('Desconto de Fim de Ano', 0.155, 'Desconto especial para vendas de final de ano'),
('Desconto por Quantidade', 0.575, 'Desconto aplicado para compras em grandes quantidades'),
('Desconto Promocional', 0.2, 'Desconto aplicado em promoções especiais por tempo limitado'),
('Desconto Fidelidade', 0.8, 'Desconto para clientes fiéis com mais de 1 ano de compras'),
('Desconto Black Friday', 0.3, 'Desconto massivo durante a Black Friday'),
('Desconto de Parceria', 0.125, 'Desconto exclusivo para parceiros comerciais'),
('Desconto de Natal', 0.1825, 'Desconto sazonal para o período de Natal');


    