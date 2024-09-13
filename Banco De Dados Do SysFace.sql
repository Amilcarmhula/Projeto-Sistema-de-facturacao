-- drop database _sysface;
-- create database _sysface;

 
use _sysface;
 -- select * from agente inner join contacto_agente on agente.id=contacto_agente.fk_id_agente;
-- select contacto, email, contacto_agente.data_criacao from contacto_agente inner join agente on agente.id=contacto_agente.fk_id_agente;
-- select * from contacto_agente where fk_id_agente=2;
-- select * from contacto_agente;


--  insert into usuario(nome, usuario, senha) value ('Jr Mula','user',1111);
-- update usuario set nome='Jr Mula', usuario='root', senha=4321, codigo=407 where id=1;

--  select * from usuario;
-- select * from endereco_agente;
-- select * from contacto_agente;

-- 	UPDATE agente
-- 	INNER JOIN endereco_agente ON agente.id = endereco_agente.fk_id_agente
-- 	INNER JOIN contacto_agente ON agente.id = contacto_agente.fk_id_agente
-- 	SET agente.active_or_not = true,
-- 		endereco_agente.active_or_not = true,
-- 		contacto_agente.active_or_not = true
-- 	WHERE agente.id = 1;

create table usuario(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    usuario varchar(50) not null unique,
    senha integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 insert into usuario(nome, usuario, senha) value ('Amilcar Mula','user',1111);
 
 
 create table cliente(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa Física/Jurídica)
    nome varchar(100) not null,
    apelido varchar(50), -- Razao social
    nuit integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id)
);

INSERT INTO cliente (tipo, nome, apelido, nuit, fk_id_usuario)
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

create table endereco_cliente(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
	fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_cliente) references cliente(id),
    foreign key (fk_id_usuario) references usuario(id),
    check(tipo in('Facturação','Envio'))
);

SELECT c.id, c.nome, c.apelido, c.nuit, 
       -- e.provincia, e.cidade, e.bairro, e.avenida, e.rua, e.codigo_postal, e.numero, e.tipo AS tipo_endereco,
       ct.pessoa_de_contacto, ct.contacto, ct.email, ct.website
FROM cliente c
-- LEFT JOIN endereco_cliente e ON e.fk_id_cliente = c.id
LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id;

SELECT c.id, 
       c.nome, 
       c.apelido, 
       c.nuit, 
       GROUP_CONCAT(CONCAT(ct.pessoa_de_contacto, ': ', ct.contacto, ', ', ct.email, ', ', ct.website) SEPARATOR ' | ') AS contatos
FROM cliente c
LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id
GROUP BY c.id, c.nome, c.apelido, c.nuit;

SELECT c.id, 
	   c.tipo,
       c.nome, 
       c.apelido as razaosocial, 
       c.nuit, 
       GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos
FROM cliente c
LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id
GROUP BY c.id, c.nome, c.apelido, c.nuit;

SELECT c.id,c.tipo,c.nome,c.apelido as razaosocial,c.nuit, 
                    GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos 
                    FROM cliente c 
                    LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id 
                    GROUP BY c.id, c.nome, c.apelido, c.nuit;



-- Dados de endereços
INSERT INTO endereco_cliente (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_cliente, fk_id_usuario)
VALUES
('Maputo', 'Maputo', 'Alto Maé', 'Avenida 25 de Setembro', 'Rua 1', 1100, 123, 'Facturação', 1, 1),
('Maputo', 'Maputo', 'Polana', 'Avenida Julius Nyerere', 'Rua 2', 1200, 456, 'Envio', 2, 1),
('Sofala', 'Beira', 'Munhava', 'Avenida Samora Machel', 'Rua 3', 2200, 789, 'Facturação', 3, 1),
('Nampula', 'Nampula', 'Namicopo', 'Avenida Eduardo Mondlane', 'Rua 4', 3300, 101, 'Envio', 4, 1),
('Maputo', 'Matola', 'T3', 'Avenida 4 de Outubro', 'Rua 5', 1400, 202, 'Facturação', 5, 1),
('Inhambane', 'Inhambane', 'Chilwane', 'Avenida Patrice Lumumba', 'Rua 6', 5500, 303, 'Envio', 6, 1),
('Gaza', 'Xai-Xai', 'Chongoene', 'Avenida dos Heróis', 'Rua 7', 6600, 404, 'Facturação', 7, 1),
('Cabo Delgado', 'Pemba', 'Paquitequete', 'Avenida 25 de Junho', 'Rua 8', 7700, 505, 'Envio', 8, 1),
('Zambézia', 'Quelimane', 'Sagrada Família', 'Avenida Marginal', 'Rua 9', 8800, 606, 'Facturação', 9, 1),
('Manica', 'Chimoio', '3 de Fevereiro', 'Avenida 7 de Abril', 'Rua 10', 9900, 707, 'Envio', 10, 1);
select * from contacto_cliente;

create table contacto_cliente(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50) unique,
    website varchar(50) unique,
    fk_id_cliente integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_cliente) references cliente(id),
    foreign key (fk_id_usuario) references usuario(id)
);
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

select * from contacto_cliente;

create table fornecedor(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa Física/Jurídica)
    razaosocial varchar(100) not null,-- Razao social
    nuit integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id)
);
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

create table endereco_fornecedor(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
	fk_id_fornecedor integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_fornecedor) references fornecedor(id),
    foreign key (fk_id_usuario) references usuario(id)
);
INSERT INTO endereco_fornecedor (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_fornecedor, fk_id_usuario) VALUES
('Maputo', 'Maputo', 'Bairro Central', 'Avenida 25 de Setembro', 'Rua das Acácias', 1101, 23, 'Comercial', 1, 1),
('Maputo', 'Matola', 'Bairro Liberdade', 'Avenida da Liberdade', 'Rua das Flores', 1102, 45, 'Comercial', 2, 1),
('Gaza', 'Xai-Xai', 'Bairro Patrice Lumumba', 'Avenida Eduardo Mondlane', 'Rua do Comércio', 1201, 12, 'Comercial', 3, 1),
('Inhambane', 'Maxixe', 'Bairro Maxixe', 'Avenida 7 de Abril', 'Rua do Mar', 1301, 5, 'Comercial', 4, 1),
('Sofala', 'Beira', 'Ponta-Gêa', 'Avenida Samora Machel', 'Rua da Beira', 1401, 78, 'Comercial', 5, 1),
('Manica', 'Chimoio', 'Bairro 25 de Setembro', 'Avenida 25 de Setembro', 'Rua da Paz', 1501, 18, 'Comercial', 6, 1),
('Zambézia', 'Quelimane', 'Bairro Sinacura', 'Avenida Maguiguana', 'Rua Zambeze', 1601, 35, 'Comercial', 7, 1),
('Nampula', 'Nampula', 'Bairro Muhala', 'Avenida das Industrias', 'Rua 24 de Julho', 1701, 10, 'Comercial', 8, 1),
('Cabo Delgado', 'Pemba', 'Bairro Cariaco', 'Avenida dos Heróis', 'Rua dos Pescadores', 1801, 27, 'Comercial', 9, 1),
('Niassa', 'Lichinga', 'Bairro Lichinga', 'Avenida da República', 'Rua das Montanhas', 1901, 50, 'Comercial', 10, 1),
('Tete', 'Tete', 'Bairro Matundo', 'Avenida do Zambeze', 'Rua do Mercado', 2001, 8, 'Comercial', 11, 1),
('Maputo', 'Matola', 'Bairro T-3', 'Avenida Julius Nyerere', 'Rua do Lazer', 1103, 120, 'Comercial', 12, 1),
('Gaza', 'Chokwe', 'Bairro Ngala', 'Avenida Samora Machel', 'Rua Agrária', 1202, 36, 'Comercial', 13, 1),
('Inhambane', 'Inhambane', 'Bairro Cemento', 'Avenida da Independência', 'Rua do Farol', 1302, 15, 'Comercial', 14, 1),
('Manica', 'Manica', 'Bairro 7 de Abril', 'Avenida 7 de Abril', 'Rua das Mangueiras', 1502, 70, 'Comercial', 15, 1);

select * from contacto_fornecedor;
create table contacto_fornecedor(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50) unique,
    website varchar(50) unique,
    fk_id_fornecedor integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_fornecedor) references fornecedor(id),
    foreign key (fk_id_usuario) references usuario(id)
);
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


create table armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    tipo varchar(50) not null, -- de Armazém: (Central, Regional, Loja, etc.)
    descricao varchar(50),
    estado boolean default 1 not null, -- Ativo/Inativo.
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table endereco_armazem(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
	fk_id_armazem integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id)
);

create table contacto_armazem(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50) unique,
    website varchar(50) unique,
    fk_id_armazem integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id)
);

-- create table endereco(
-- 	id integer primary key auto_increment,
--     provincia varchar(50),
--     cidade varchar(50),
--     bairro varchar(50),
--     avenida varchar(50),
--     rua varchar(30),
--     codigo_postal integer,
--     numero integer not null,
--     tipo varchar(10),
-- 	fk_id_cliente integer,
--     fk_id_fornecedor integer,
--     fk_id_armazem integer,
--     fk_id_usuario integer not null,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     foreign key (fk_id_cliente) references cliente(id),
--     foreign key (fk_id_fornecedor) references fornecedor(id),
--     foreign key (fk_id_armazem) references armazem(id),
--     foreign key (fk_id_usuario) references usuario(id),
--     check(tipo in('Facturação','Envio')),
--     check(
-- 		(fk_id_cliente is not null and (fk_id_fornecedor is null and fk_id_armazem is null))
--         or 
--         (fk_id_fornecedor is not null and (fk_id_cliente is null and fk_id_armazem is null))
--         or 
--         (fk_id_armazem is not null and (fk_id_cliente is null and fk_id_fornecedor is null))
-- 	)
-- );



-- create table categoria(
-- 	id integer primary key auto_increment,
--     tipo varchar(30) not null,
--     nome varchar(50) not null,
--     descricao text,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- version II
create table categoria(
	id integer primary key auto_increment,
    tipo varchar(30) not null,
    grupo varchar(50) not null,
    subgrupo varchar(50),
    familia varchar(50),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    check(tipo in('Produto','Serviço'))
);

create table unidade_medida(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    sigla varchar(10) not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

create table taxa_imposto(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    percentagem decimal(3,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- insert into armazem (nome, cidade, bairro,avenida, rua,codigo_postal,numero) value ('Armazem 3', 'Maracuene', 'Jardim', 'av. Cardoso', 'Junta', 1122,300);
-- ALTER TABLE armazem DROP COLUMN provincia;

create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(3,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
                     
create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer,
    fk_id_taxa_imposto integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id)
);


CREATE TABLE precovenda (
    id integer PRIMARY KEY AUTO_INCREMENT,
    preco DECIMAL(10, 2) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    fk_id_produto integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_id_produto) REFERENCES produto(id),
    foreign key (fk_id_usuario) references usuario(id)
);
-- SELECT p.id,
-- 			(SELECT nome FROM produto WHERE p.fk_id_produto = produto.id) AS nome_p,
--             (SELECT nome FROM agente WHERE p.fk_id_agente = agente.id) AS nome_f,
--             (SELECT nome FROM armazem WHERE p.fk_id_armazem = armazem.id) AS nome_a,
-- p.quantidade, p.custo, p.data_fornedida, p.active_or_not FROM produto_fornecido p;
          

create table produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornecida date,
    custo decimal(10,2) not null,
    fk_id_armazem integer not null,
    fk_id_produto integer not null,
    fk_id_fornecedor integer not null,
    fk_id_usuario integer not null,
	data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_fornecedor) references fornecedor(id)
);

-- select * from precovenda;
-- select sum(quantidade) from movimento_produto where id=1;

-- select sum(quantidade ) from movimento_produto where id=2;


-- select * from precovenda;
-- select sum(quantidade) from movimento_produto where id=1;


-- select sum(quantidade ) from movimento_produto where id=2;

-- select sum(quantidade) from movimento_produto;
-- select * from movimento_produto;

-- SELECT 
--     (SUM(CASE WHEN tipo_movimento = 'Entrada' THEN quantidade ELSE 0 END) - 
--      SUM(CASE WHEN tipo_movimento = 'Saída' THEN quantidade ELSE 0 END)) AS saldo_produto
-- FROM movimento_produto
-- WHERE fk_id_produto = 1
--   AND active_or_not = 1; -- Considerando apenas registros ativos
--  
-- update stock set  saldo=(SELECT 
--     (SUM(CASE WHEN tipo_movimento = 'Entrada' THEN quantidade ELSE 0 END) - 
--      SUM(CASE WHEN tipo_movimento = 'Saída' THEN quantidade ELSE 0 END)) AS saldo_produto
-- FROM movimento_produto
-- WHERE fk_id_produto = 2) where fk_id_produto=2;

-- select * from movimento_produto;
-- insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem ) 
-- value ('Saída','2024-09-10', 8, 2,1,1);

create table movimento_produto(
	id integer primary key auto_increment,
    tipo_movimento varchar(8) not null,
    data_movimento date,
    quantidade integer not null,
    fk_id_produto integer not null,
	fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    foreign key (fk_id_produto) references produto(id),
	foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_armazem) references armazem(id),
    check (tipo_movimento in('Entrada','Saída','Ajuste'))
);
-- Trigger para tabela movimento_produto manter dados de movimento de ENTRADA, SAIDA ou AJUSTE de estoque
delimiter //
create trigger trigger_registra_entrada
	after insert on produto_fornecido
    for each row
    begin
		insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem) value ('Entrada',new.data_fornecida,new.quantidade,new.fk_id_produto,new.fk_id_usuario,new.fk_id_armazem);
    end//
delimiter ;



create table stock(
	fk_id_produto integer primary key,
    saldo integer not null,
    foreign key(fk_id_produto) references produto(id)
);

-- Trigger para tabela stock para manter os dados de estoque actualizados
delimiter //
create trigger trigger_registra_produto
	after insert on produto
    for each row
    begin
		insert into stock (fk_id_produto,saldo) value (new.id,0);
    end//
delimiter ;




--  Banco de dados da empresa no sistema Sysface
 create table empresa(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    apelido varchar(30),
    nuit integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table endereco_empresa(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
    fk_id_empresa integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_empresa) references empresa(id),
    check (tipo in('Facturação','Envio'))
);

create table contacto_empresa(
	id integer primary key auto_increment,
    contacto varchar(20) not null,
	email varchar(50) unique,
    fk_id_empresa integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_empresa) references empresa(id)
);
-- id,nome_banco,numero,nib,nuib,fk_id_empresa,fk_id_usuario,data_criacao,active_or_not
    
 create table contabancaria(
	id integer primary key auto_increment,
    nome_banco varchar(50) not null,
    numero integer,
    nib integer,
    nuib integer,
    fk_id_empresa integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_empresa) references empresa(id)
);


-- Tabelas para armazenar dados das facturacoes
create table factura(
	id integer primary key auto_increment,
    tipo varchar(30) not null,
    numero integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_vencimento datetime not null,
    estado boolean default 0 not null,
    total decimal(10,2) not null,
    fk_id_empresa integer not null,
    fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_empresa) references empresa(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_cliente) references cliente(id)
);
-- tipo,quantidade,preco_unitario,subtotal,fk_id_desconto,fk_id_produto,fk_id_factura,fk_id_servico,fk_id_usuario
-- drop table item_factura;
create table item_factura(
	id integer primary key auto_increment,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
	fk_id_precovenda integer not null,
    fk_id_desconto integer,
    fk_id_produto integer not null,
    fk_id_factura integer not null,
    fk_id_usuario integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_precovenda) references precovenda(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_desconto) references desconto(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_factura) references factura(id)
);

create table metodo_pagamento(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table pagamento(
	id integer primary key auto_increment,
    data_pagamento datetime,
    referencia_numero_fatura integer not null,
    valor decimal(10,2) not null,
    fk_id_metodo_pagamento integer not null,
    fk_id_factura integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_metodo_pagamento) references metodo_pagamento(id),
    foreign key (fk_id_factura) references factura(id)
);

