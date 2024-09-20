-- drop database _sysface;
-- create database _sysface;


use _sysface;

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


create table fornecedor(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa Física/Jurídica)
    razaosocial varchar(100) not null,-- Razao social
    nuit integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id)
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
	fk_id_fornecedor integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_fornecedor) references fornecedor(id),
    foreign key (fk_id_usuario) references usuario(id)
);

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
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

    
create table taxa_imposto(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    percentagem decimal(4,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- insert into armazem (nome, cidade, bairro,avenida, rua,codigo_postal,numero) value ('Armazem 3', 'Maracuene', 'Jardim', 'av. Cardoso', 'Junta', 1122,300);
-- ALTER TABLE armazem DROP COLUMN provincia;

create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(4,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    contole_stock boolean not null, -- define condicao se sera armazenado no stock pelo trigger_registra_produto
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer,
    fk_id_taxa_imposto integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id)
);


    

-- CALL atualizar_estado_precovenda();

CREATE TABLE precovenda (
    id integer PRIMARY KEY AUTO_INCREMENT,
    preco DECIMAL(10, 2) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    estado boolean default 1 not null,
    fk_id_produto integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_id_produto) REFERENCES produto(id),
    foreign key (fk_id_usuario) references usuario(id)
);

DELIMITER //
CREATE PROCEDURE atualizar_estado_precovenda()
BEGIN
    UPDATE precovenda
    SET estado = 0
    WHERE data_fim <= CURDATE() AND estado = 1;
END//

DELIMITER ;

-- # Automatizando com um Evento
-- Para evitar a execução manual, você pode criar um evento que execute o procedimento periodicamente 
-- (por exemplo, diariamente). Veja como fazer isso:

CREATE EVENT evento_atualizar_precovenda
ON SCHEDULE EVERY 1 DAY
DO
    CALL atualizar_estado_precovenda();


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
    check (tipo_movimento in('Entrada','Saída'))
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

select * from produto_fornecido;
select * from movimento_produto;
select * from stock;
create table stock(
	fk_id_produto integer primary key,
    saldo integer not null,
    foreign key(fk_id_produto) references produto(id)
);

delimiter //
create trigger trigger_update_saldo_stock
	after insert on movimento_produto
    for each row
    begin
		update stock set  saldo=(SELECT 
			(SUM(CASE WHEN tipo_movimento = 'Entrada' THEN quantidade ELSE 0 END) - 
			 SUM(CASE WHEN tipo_movimento = 'Saída' THEN quantidade ELSE 0 END)) AS saldo_produto
		FROM movimento_produto WHERE fk_id_produto=new.fk_id_produto)
        where fk_id_produto=new.fk_id_produto;
	end//
delimiter;

-- Trigger para tabela stock para manter os dados de estoque actualizados
delimiter //
create trigger trigger_registra_produto
	after insert on produto
    for each row
    begin
		if new.contole_stock = true then
			insert into stock (fk_id_produto,saldo) value (new.id,0);
        end if;
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
    foreign key (fk_id_empresa) references empresa(id)
);

create table contacto_empresa(
	id integer primary key auto_increment,
    contacto varchar(20) not null,
    pessoa_de_contacto varchar(100),
	email varchar(50) unique,
    website varchar(50) unique,
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
    numero varchar(100),
    nib varchar(100),
    nuib varchar(100),
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
    fk_id_desconto integer,
    fk_id_produto integer not null,
    fk_id_factura integer not null,
    fk_id_usuario integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

