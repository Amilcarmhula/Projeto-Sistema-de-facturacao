-- drop database _sysface;

create database _sysface;
 
use _sysface;


create table usuario(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    usuario varchar(50) not null,
    senha integer not null,
    codigo integer
);

create table categoria(
	id integer primary key auto_increment,
    tipo varchar(30) not null,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table unidade_medida(
	id integer primary key auto_increment,
    nome varchar(50), -- se e uma unidade de area, agraria, volume, massa, etc.. 
    identificador varchar(50) not null,
    sigla varchar(10) not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 
create table taxa_imposto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(3,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    numero integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(3,2),
    valor_fixo decimal(10,2),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



create table fornecedor(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    nuit integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table contacto_fornecedor(
	id integer primary key auto_increment,
    contacto varchar(20) not null,
	email varchar(50) unique,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_fornecedor) references fornecedor(id)
);
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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_fornecedor) references fornecedor(id)
);

create table servico(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_categoria integer not null,
    fk_id_taxa_imposto integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
     foreign key (fk_id_usuario) references usuario(id)
);

create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer not null,
    fk_id_taxa_imposto integer not null,
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id),
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id)
);
create table produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornedida datetime,
    custo decimal(10,2) not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_armazem integer not null,
    fk_id_produto integer not null,
    fk_id_fornecedor integer not null,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_fornecedor) references fornecedor(id)
);

create table movimento_produto(
	id integer primary key auto_increment,
    tipo_movimento varchar(8) not null,
    data_movimento datetime,
    quantidade integer not null,
    motivo_movimento varchar(50),
    fk_id_produto integer not null,
    fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_armazem) references armazem(id)
);






 create table cliente(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    apelido varchar(30),
    nuit integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_usuario integer not null,
    foreign key (fk_id_usuario) references usuario(id)
);
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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_cliente) references cliente(id)
);
create table contacto_cliente(
	id integer primary key auto_increment,
    contacto varbinary(20) not null,
	email varchar(50) unique,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_cliente) references cliente(id)
);



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
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_empresa integer not null,
    foreign key (fk_id_empresa) references empresa(id)
);

create table contacto_empresa(
	id integer primary key auto_increment,
    contacto varbinary(20) not null,
	email varchar(50) unique,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_empresa integer not null,
    foreign key (fk_id_empresa) references empresa(id)
);

 create table contabancaria(
	id integer primary key auto_increment,
    nome_banco varchar(50) not null,
    numero integer,
    nib integer,
    nuib integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_empresa integer not null,
    foreign key (fk_id_empresa) references empresa(id)
);

-- Tabelas para armazenar dados das facturacoes
create table factura(
	id integer primary key auto_increment,
    tipo varchar(30) not null,
    numero integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_vencimento datetime not null,
    estado boolean not null,
    total decimal(10,2) not null,
    fk_id_empresa integer not null,
    fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_empresa) references empresa(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_cliente) references cliente(id)
);
create table item_factura(
	id integer primary key auto_increment,
    tipo  varchar(50) not null,
    quantidade decimal(10,2) not null,
    preco_unitario decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_desconto integer not null,
    fk_id_produto integer not null,
    fk_id_factura integer not null,
    fk_id_servico integer not null,
    fk_id_usuario integer not null,
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
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao text
);

create table pagamento(
	id integer primary key auto_increment,
    data_pagamento datetime,
    referencia_numero_fatura integer not null,
    valor decimal(10,2) not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_id_metodo_pagamento integer not null,
    fk_id_factura integer not null,
    fk_id_usuario integer not null,
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_metodo_pagamento) references metodo_pagamento(id),
    foreign key (fk_id_factura) references factura(id)
);




-- alter table categoria add tipo varchar(30); 

-- insert into categoria (nome, descricao,sub_nome)values ('Vestuário ','Roupas para Criança', 'Criança Masculino'),('Vestuário ','Roupas para Criança', 'Criança Femenino'),('Vestuário ','Roupas para Adulto', 'Adulto Masculino'),('Vestuário ','Roupas para Adulto', 'Adulto Femenino');


-- Tabela de unidades de produtos com descriacao da unidade, isto e, nome da unidisa e as siglas no sistema de unidades no sistema internacional USI

-- insert into e_unidade_medida (descricao,usi) values ('Kilograma ','kg'),('Litro ','l'),('Metro ','m');
-- insert into e_unidade_medida (descricao,usi)values ('Smal ','s'),('Large ','l'),('Extra-Large ','xl'),('Medium ','m');



-- insert into e_armazem (nome, provincia, cidade, bairro, avenida, numero) values ('Armazém 1 ','Maputo','Cidade de Maputo','Patrice Lumumba','av. Amilcar Cabral',100),('Armazém 2 ','Maputo','Cidade de Maputo','Machava Socimol','av. Steve Jbos',201),('Armazém 3 ','Maputo','Cidade da Matola','C700','Rua da resistencia',3001);


-- insert into e_fornecedor (nome, nuit) values ('Fornecedor 1 ',123456789),('Fornecedor 2 ',223456789),('Fornecedor 3 ',333456789),('Fornecedor 4 ',444456789);



-- insert into e_contacto_fornecedor (contacto, email,fk_id_fornecedor) values ('+258878201993','email.teste-1@mail.com',1), ('+258847100793','email.teste-2@mail.com',1), ('+258870000000','email.teste-3@mail.com',2);




-- insert into e_endereco_fornecedor (provincia, cidade, bairro, avenida, numero,fk_id_fornecedor) values ('Gaza','Xai-xai','Bairro 1','av.Cabral Amilcar', 6062, 1);







-- drop table fe_produto;

-- insert into fe_produto (nome, codigo_barra, descricao, fk_id_categoria, fk_id_unidade_medida)values ('Camisete','77010010987','Camisete sem gola',1, 4);


-- drop table e_produto_fornecido;
-- insert into e_produto_fornecido (quantidade, data_fornedida, custo, fk_id_armazem, fk_id_produto, fk_id_fornecedor) values (90,'2024-08-17 02:28:00',7684.80,2,1,3);


-- drop table fe_movimento_estoque;
-- insert into fe_movimento_estoque (tipo_movimento, data_movimento, quantidade, motivo_movimento, fk_id_produto) values ('Entrada','2024-08-17 01:05:56',40,'',2);
-- update fe_movimento_estoque set data_movimento = '2024-08-17 00:00:00' where id=1;
 

















