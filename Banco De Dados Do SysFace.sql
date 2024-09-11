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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);
 insert into usuario(nome, usuario, senha) value ('Jr Mula','user',1111);
 create table agente(
	id integer primary key auto_increment,
    tipo varchar(50) not null,
    nome varchar(100) not null,
    apelido varchar(50),
    nuit integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_usuario) references usuario(id)
);
create table endereco_agente(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
    fk_id_agente integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_agente) references agente(id),
    check(tipo in('Facturação','Envio'))
);
create table contacto_agente(
	id integer primary key auto_increment,
    contacto varchar(20) not null,
	email varchar(50) unique,
    fk_id_agente integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_agente) references agente(id)
);

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
    active_or_not boolean default 1 not null, -- deletado ou nao
    check(tipo in('Produto','Serviço'))
);
select * from unidade_medida;
create table unidade_medida(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    sigla varchar(10) not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
); 

create table taxa_imposto(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    percentagem decimal(3,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);
create table armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null unique,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);
-- insert into armazem (nome, cidade, bairro,avenida, rua,codigo_postal,numero) value ('Armazem 3', 'Maracuene', 'Jardim', 'av. Cardoso', 'Junta', 1122,300);
-- ALTER TABLE armazem DROP COLUMN provincia;

create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(3,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    fk_id_agente integer not null,
    fk_id_usuario integer not null,
	data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_agente) references agente(id)
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
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
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
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    active_or_not boolean default 1 not null, -- deletado ou nao
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
    fk_id_agente integer not null,
    fk_id_usuario integer not null,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_empresa) references empresa(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_agente) references agente(id)
);
-- tipo,quantidade,preco_unitario,subtotal,fk_id_desconto,fk_id_produto,fk_id_factura,fk_id_servico,fk_id_usuario
-- drop table item_factura;
create table item_factura(
	id integer primary key auto_increment,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
	fk_id_precovenda integer not null,
    fk_id_desconto integer,
    fk_id_taxa_imposto integer not null,
    fk_id_produto integer not null,
    fk_id_factura integer not null,
    fk_id_usuario integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_precovenda) references precovenda(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_desconto) references desconto(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_factura) references factura(id),
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id)
);

create table metodo_pagamento(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   active_or_not boolean default 1 not null -- deletado ou nao
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
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_metodo_pagamento) references metodo_pagamento(id),
    foreign key (fk_id_factura) references factura(id)
);

