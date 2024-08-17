-- Informatico@12
 
use _sysface;
-- As tabelas contem um prefixo e_ ou fe_ que significam que a tabela e pertecente ao sistema de estoque se iniciada com e_ e pertecente ao sistema de facturacao e estoque se iniciada com fe_

-- Tabela de categorizacao dos productos
create table e_categoria(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text
);
alter table e_categoria add sub_nome varchar(30);

insert into e_categoria (nome, descricao,sub_nome)
 values ('Vestuário ','Roupas para Criança', 'Criança Masculino'),
		('Vestuário ','Roupas para Criança', 'Criança Femenino'),
        ('Vestuário ','Roupas para Adulto', 'Adulto Masculino'),
        ('Vestuário ','Roupas para Adulto', 'Adulto Femenino');


-- Tabela de unidades de produtos com descriacao da unidade, isto e, nome da unidisa e as siglas no sistema de unidades no sistema internacional USI
create table e_unidade_medida(
	id integer primary key auto_increment,
    descricao varchar(50),
    usi varchar(10)
);

insert into e_unidade_medida (descricao,usi)
 values ('Kilograma ','kg'),
		('Litro ','l'),
        ('Metro ','m');
insert into e_unidade_medida (descricao,usi)
 values ('Smal ','s'),
		('Large ','l'),
        ('Extra-Large ','xl'),
        ('Medium ','m');

create table e_armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    numero integer not null
);
insert into e_armazem (nome, provincia, cidade, bairro, avenida, numero)
 values ('Armazém 1 ','Maputo','Cidade de Maputo','Patrice Lumumba','av. Amilcar Cabral',100),
		('Armazém 2 ','Maputo','Cidade de Maputo','Machava Socimol','av. Steve Jbos',201),
        ('Armazém 3 ','Maputo','Cidade da Matola','C700','Rua da resistencia',3001);

create table e_fornecedor(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    nuit int(9)
);
insert into e_fornecedor (nome, nuit)
 values ('Fornecedor 1 ',123456789),('Fornecedor 2 ',223456789),('Fornecedor 3 ',333456789),('Fornecedor 4 ',444456789);

create table e_contacto_fornecedor(
	id integer primary key auto_increment,
    contacto varbinary(20) not null,
	email varchar(50) unique,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);
alter table e_contacto_fornecedor modify contacto varchar(20);
insert into e_contacto_fornecedor (contacto, email,fk_id_fornecedor)
 values ('+258878201993','email.teste-1@mail.com',1),
 ('+258847100793','email.teste-2@mail.com',1),
 ('+258870000000','email.teste-3@mail.com',2);


create table e_endereco_fornecedor(
	id integer primary key auto_increment,
    provincia varchar(50),
    cidade varchar(50),
    bairro varchar(50),
    avenida varchar(50),
    numero integer not null,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);

insert into e_endereco_fornecedor (provincia, cidade, bairro, avenida, numero,fk_id_fornecedor)
 values ('Gaza','Xai-xai','Bairro 1','av.Cabral Amilcar', 6062, 1);


create table fe_produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer not null,
    foreign key (fk_id_categoria) references e_categoria(id),
    foreign key (fk_id_unidade_medida) references e_unidade_medida(id)
);
-- drop table fe_produto;

insert into fe_produto (nome, codigo_barra, descricao, fk_id_categoria, fk_id_unidade_medida)
 values ('Camisete','77010010987','Camisete sem gola',1, 4);

create table e_produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornedida datetime,
    custo decimal(10,2) not null,
    fk_id_armazem integer not null,
    fk_id_produto integer not null,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_armazem) references e_armazem(id),
    foreign key (fk_id_produto) references fe_produto(id),
    foreign key (fk_id_fornecedor) references e_fornecedor(id)
);
-- drop table e_produto_fornecido;
insert into e_produto_fornecido (quantidade, data_fornedida, custo, fk_id_armazem, fk_id_produto, fk_id_fornecedor)
 values (90,'2024-08-17 02:28:00',7684.80,2,1,3);

create table fe_movimento_estoque(
	id integer primary key auto_increment,
    tipo_movimento varchar(8) not null,
    data_movimento datetime,
    quantidade integer not null,
    motivo_movimento varchar(50),
    fk_id_produto integer not null,
    foreign key (fk_id_produto) references fe_produto(id)
);
-- drop table fe_movimento_estoque;
insert into fe_movimento_estoque (tipo_movimento, data_movimento, quantidade, motivo_movimento, fk_id_produto)
 values ('Entrada','2024-08-17 01:05:56',40,'',2);
 
 update fe_movimento_estoque set data_movimento = '2024-08-17 00:00:00' where id=1;
 
 
-- Tabela de teste
create table tabelateste(
	id integer primary key auto_increment,
    salario integer
);

insert into tabelateste (salario) value (1000);
-- Procedimento armazenado















