drop database _sysface;
create database _sysface;
use _sysface;

-- CREATE USER 'jmuser'@'%' IDENTIFIED BY 'Informatico12';
-- GRANT ALL PRIVILEGES ON _sysface.* TO 'jmuser'@'%';
-- FLUSH PRIVILEGES;


--  Banco de dados da empresa no sistema Sysface
 create table empresa(
	id integer primary key auto_increment,
    nome varchar(100) not null,
    razaosocial varchar(100),
    nuit integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table endereco_empresa(
	id integer primary key auto_increment,
    provincia varchar(100),
    cidade varchar(100),
    bairro varchar(100),
    avenida varchar(100),
    rua varchar(50),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10),
    fk_id_empresa integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_empresa) references empresa(id)
);
create table contacto_empresa(
	id integer primary key auto_increment,
    contacto varchar(20) not null,
	email varchar(50) unique,
    website varchar(50) unique,
    fk_id_empresa integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_empresa) references empresa(id)
);
 create table contabancaria(
	id integer primary key auto_increment,
    nome_banco varchar(50) not null,
    numero varchar(100),
    nib varchar(100),
    nuib varchar(100),
    fk_id_empresa integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_empresa) references empresa(id)
);
create table nivel_acesso(
	id integer primary key auto_increment,
    descricao varchar(50) not null
);
create table permissao( 
	id integer primary key auto_increment,
    tipo varchar(4) not null,
    descricao varchar(40) not null
);
create table usuario(
	id integer primary key auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    usuario varchar(15) not null,
    senha varchar(100) not null,
    estado boolean default 0 not null,
    fk_nivel_acesso integer not null,
    fk_permissao integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key(fk_nivel_acesso) references nivel_acesso(id),
    foreign key(fk_permissao) references permissao(id)
);