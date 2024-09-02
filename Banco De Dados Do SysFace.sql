-- drop database _sysface;
-- create database _sysface;
 
use _sysface;
-- select * from agente inner join contacto_agente on agente.id=contacto_agente.fk_id_agente;
-- select contacto, email, contacto_agente.data_criacao from contacto_agente inner join agente on agente.id=contacto_agente.fk_id_agente;
-- select * from contacto_agente where fk_id_agente=2;
-- select * from contacto_agente;


-- insert into usuario(nome, usuario, senha) value ('Jr Mula','user',4321);
-- update usuario set nome='Jr Mula', usuario='root', senha=4321, codigo=407 where id=1;



create table usuario(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    usuario varchar(50) not null unique,
    senha integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);

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

create table unidade_medida(
	id integer primary key auto_increment,
    tipo varchar(50), -- se e uma unidade de area, agraria, volume, massa, etc.. 
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
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    rua varchar(30),
    codigo_postal integer,
    numero integer not null unique,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);
create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(3,2),
    valor_fixo decimal(10,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null -- deletado ou nao
);

create table servico(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    fk_id_categoria integer not null,
    fk_id_taxa_imposto integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
     foreign key (fk_id_usuario) references usuario(id)
);

create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    preco decimal(10,2),
    descricao text,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer not null,
    fk_id_taxa_imposto integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id),
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id)
);
create table produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornedida datetime,
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

create table movimento_produto(
	id integer primary key auto_increment,
    tipo_movimento varchar(8) not null,
    data_movimento datetime,
    quantidade integer not null,
    -- motivo_movimento varchar(50),
    fk_id_produto integer not null,
	fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_produto) references produto(id),
	foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_armazem) references armazem(id),
    check (tipo_movimento in('Entrada','Saída','Ajuste'))
);

delimiter //
create trigger trigger_registra_entrada
	after insert on produto_fornecido
    for each row
    begin
		insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem) value ('Entrada',new.data_fornedida,new.quantidade,new.fk_id_produto,new.fk_id_usuario,new.fk_id_armazem);
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
    contacto varbinary(20) not null,
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
create table item_factura(
	id integer primary key auto_increment,
    tipo  varchar(50) not null,
    quantidade decimal(10,2) not null,
    preco_unitario decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    fk_id_desconto integer not null,
    fk_id_produto integer not null,
    fk_id_factura integer not null,
    fk_id_servico integer not null,
    fk_id_usuario integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active_or_not boolean default 1 not null, -- deletado ou nao
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_desconto) references desconto(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_servico) references servico(id),
    foreign key (fk_id_factura) references factura(id),
    check(
		(fk_id_produto is not null and fk_id_servico is null)
        or 
        (fk_id_produto is null and fk_id_servico is not null)
	)
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

show tables;