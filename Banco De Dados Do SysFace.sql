-- Informatico@12
 
use _sysface;
-- As tabelas contem um prefixo e_ ou fe_ que significam que a tabela e pertecente ao sistema de estoque se iniciada com e_ e pertecente ao sistema de facturacao e estoque se iniciada com fe_

-- Tabela de categorizacao dos productos
create table e_categoria(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text
);
-- Tabela de unidades de produtos com descriacao da unidade, isto e, nome da unidisa e as siglas no sistema de unidades no sistema internacional USI
create table e_unidade_medida(
	id integer primary key auto_increment,
    descricao varchar(50),
    usi varchar(10)
);

create table e_armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    numero integer not null
);

create table e_fornecedor(
	id integer primary key auto_increment,
    nome varchar(50) not null
);

create table e_contacto_fornecedor(
	id integer primary key auto_increment,
    contacto varbinary(20) not null,
	email varchar(50) unique,
    fk_id_fornecedor integer,
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);


create table e_endereco_fornecedor(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    numero integer not null,
    fk_id_fornecedor integer,
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);


create table fe_produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    fk_id_categoria integer,
    fk_id_unidade_medida integer,
    foreign key (fk_id_categoria) references e_categoria(id),
    foreign key (fk_id_unidade_medida) references e_unidade_medida(id)
    
);

create table e_produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornedida datetime,
    custo decimal(10,2) not null,
    fk_id_armazem integer,
    fk_id_produto integer,
    fk_id_fornecedor integer,
    foreign key (fk_id_armazem) references e_armazem(id),
    foreign key (fk_id_produto) references fe_produto(id),
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);

create table fe_movimento_estoque(
	id integer primary key auto_increment,
    tipo_movimento varchar(8) not null,
    data_movimento datetime,
    quantidade integer,
    motivo_movimento varchar(50),
    fk_id_produto integer,
    foreign key (fk_id_produto) references fe_produto(id)
);
