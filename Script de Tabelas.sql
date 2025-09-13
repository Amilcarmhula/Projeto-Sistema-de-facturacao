drop database _sysface;
create database _sysface;

use _sysface;

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
 
 create table cliente(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa Física/Jurídica)
    nome varchar(100) not null,
    razaosocial varchar(100), -- Razao social
    nuit integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_usuario) references usuario(id)
);
create table endereco_cliente(
	id integer primary key auto_increment,
    provincia varchar(100),
    cidade varchar(100),
    bairro varchar(100),
    avenida varchar(100),
    rua varchar(50),
    codigo_postal integer,
    numero integer not null,
    tipo varchar(10) default 'Facturação',
	fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_cliente) references cliente(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id),
    check(tipo in('Facturação','Envio'))
);
create table contacto_cliente(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(100),
    website varchar(100),
    fk_id_cliente integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_cliente) references cliente(id) on delete cascade,
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
	fk_id_fornecedor integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_fornecedor) references fornecedor(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id)
);

create table contacto_fornecedor(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(100) unique,
    website varchar(100) ,
    fk_id_fornecedor integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_fornecedor) references fornecedor(id) on delete cascade,
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
	fk_id_armazem integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id)
);


create table contacto_armazem(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50),
    fk_id_armazem integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id)
);

create table categoria(
	id integer primary key auto_increment,
    tipo varchar(30) not null,
    grupo varchar(50) not null,
    subgrupo varchar(50),
    familia varchar(50),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    check(tipo in('Produtos','Serviços'))
);

create table unidade_medida(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    sigla varchar(10) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 
-- create table taxa_imposto(
-- 	id integer primary key auto_increment,
--     identificador varchar(50) not null,
--     percentagem decimal(4,3),
--     descricao text,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- create table desconto(
-- 	id integer primary key auto_increment,
--     identificador varchar(100) not null,
--     percentagem decimal(4,3),
--     descricao text,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- create table produto(
-- 	id integer primary key auto_increment,
--     nome varchar(50) not null,
--     codigo_barra varchar(50),
--     descricao text,
--     controle_stock boolean not null, -- define condicao se sera armazenado no stock pelo trigger_registra_produto
--     nivelstock integer default 0,
--     fk_id_categoria integer not null,
--     fk_id_unidade_medida integer,
--     -- fk_id_taxa_imposto integer,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--   --  foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
--     foreign key (fk_id_categoria) references categoria(id),
--     foreign key (fk_id_unidade_medida) references unidade_medida(id)
-- );

-- CREATE TABLE precovenda (
--     -- id integer PRIMARY KEY AUTO_INCREMENT,
--     precoNormal DECIMAL(10, 2) default 0.01 NULL,
--     precoInclTaxa DECIMAL(10, 2) default 0.01 NULL,
--     precoExclTaxa DECIMAL(10, 2) default 0.01 NULL,
--     data_inicio DATE,
--     data_fim DATE,
--     estado varchar(20) default 'activo' not null,
--     fk_id_produto integer not null,
--     fk_id_taxa_imposto integer, --
--     fk_id_desconto integer, --
--     fk_id_usuario integer not null,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (fk_id_produto) REFERENCES produto(id) on delete cascade,
--     foreign key (fk_id_usuario) references usuario(id),
--     PRIMARY KEY(fk_id_produto,fk_id_taxa_imposto,fk_id_desconto,data_inicio,data_fim)
-- );
select * from imposto;
create table imposto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    percentagem decimal(6,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from stock;
create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    preco DECIMAL(10, 2) default 0.01 NULL,
    descricao text,
    controle_stock boolean not null,
    nivelstock integer default 0,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer,
    fk_id_imposto integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_imposto) references imposto(id),
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id)
);
select * from produtodesconto;
create table produtodesconto(
	idProduto integer not null,
    idDesconto integer not null,
    idArmazem integer not null,
    dataInicio date not null,
    dataFim date not null,
    percentagem decimal(4,3),
    foreign key (idProduto) references produto(id) on delete cascade,
    foreign key (idDesconto) references desconto(id) on delete cascade,
    foreign key (idArmazem) references armazem(id)on delete cascade,
    primary key(idProduto, idDesconto, idArmazem, dataInicio, dataFim)
);

select * from precovenda;
select * from produto;
select * from imposto;
select * from desconto;
select * from produtodesconto;
CREATE TABLE precovenda (
    id integer PRIMARY KEY AUTO_INCREMENT, 
    precoNormal DECIMAL(10, 2) default 0.01 NULL,
    precoVenda DECIMAL(10, 2) default 0.01 NULL,
    precoFinal DECIMAL(10, 2) default 0.01 NULL,
    dataValidade DATE,
    estado varchar(20) default 'activo' not null,
    fk_id_produto integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_id_produto) REFERENCES produto(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id)
    -- PRIMARY KEY(fk_id_produto,fk_id_taxa_imposto,fk_id_desconto,data_inicio,data_fim)
);
-- Trigger antes de inserir um PrecoVenda


DELIMITER //

CREATE TRIGGER trg_precovenda_after_produtodesconto
AFTER INSERT ON produtodesconto
FOR EACH ROW
BEGIN
    DECLARE preco_base DECIMAL(10,2);
    DECLARE preco_venda DECIMAL(10,2);
    DECLARE imposto_val DECIMAL(10,2);
    DECLARE desconto_val DECIMAL(10,2);
    DECLARE preco_final DECIMAL(10,2);

    -- Obtém o preço base do produto
    SELECT preco, fk_id_imposto INTO preco_base, @id_imposto
    FROM produto
    WHERE id = NEW.idProduto;

    -- Calcula o valor do imposto (0 se não houver)
    IF @id_imposto IS NOT NULL THEN
        SELECT IFNULL(percentagem,0) INTO imposto_val
        FROM imposto
        WHERE id = @id_imposto;
        SET imposto_val = preco_base * (imposto_val / 100);
    ELSE
        SET imposto_val = 0;
    END IF;

    -- Calcula o valor do desconto (percentagem da tabela produtodesconto)
    SET desconto_val = preco_base * (NEW.percentagem / 100);

    -- Calcula o preço final
    SET preco_final = preco_base + imposto_val - desconto_val;
    SET preco_venda = preco_base + imposto_val;

    -- Insere o registro na tabela PrecoVenda
    INSERT INTO precovenda (
        precoNormal,
        precoVenda,
        precoFinal,
        dataValidade,
        estado,
        fk_id_produto,
        fk_id_usuario,
        data_criacao
    ) VALUES (
        preco_base,
        preco_venda,       -- Pode ajustar se quiser outro valor de venda inicial
        preco_final,
        NEW.dataFim,      -- usa dataFim do desconto como validade
        'activo',
        NEW.idProduto,
        1,                -- fk_id_usuario, ajustar conforme o contexto
        NOW()
    );
END;
//

DELIMITER ;

DELIMITER //


CREATE TRIGGER trg_precovenda_update_produtodesconto
AFTER UPDATE ON produtodesconto
FOR EACH ROW
BEGIN
    DECLARE preco_base DECIMAL(10,2);
    DECLARE preco_venda DECIMAL(10,2);
    DECLARE imposto_val DECIMAL(10,2);
    DECLARE desconto_val DECIMAL(10,2);
    DECLARE preco_final DECIMAL(10,2);
    DECLARE id_imposto INT;

    -- Obtém o preço base do produto e o imposto associado
    SELECT preco, fk_id_imposto INTO preco_base, id_imposto
    FROM produto
    WHERE id = NEW.idProduto;

    -- Calcula o valor do imposto (0 se não houver)
    IF id_imposto IS NOT NULL THEN
        SELECT IFNULL(percentagem,0) INTO imposto_val
        FROM imposto
        WHERE id = id_imposto;
        SET imposto_val = preco_base * (imposto_val / 100);
    ELSE
        SET imposto_val = 0;
    END IF;

    -- Calcula o valor do desconto com base na nova percentagem
    SET desconto_val = preco_base * (NEW.percentagem / 100);

    -- Calcula o preço final
    SET preco_final = preco_base + imposto_val - desconto_val;
    SET preco_venda = preco_base + imposto_val;

    -- Atualiza o registro correspondente em precovenda
    UPDATE precovenda
    SET 
        precoNormal = preco_base,
        precoVenda = preco_venda,
        precoFinal = preco_final,
        dataValidade = NEW.dataFim,
        data_criacao = NOW()
    WHERE fk_id_produto = NEW.idProduto
      AND dataValidade = OLD.dataFim; -- garante que atualiza o registro correto do desconto antigo
END;
//

DELIMITER ;



create table produto_fornecido(
	id integer primary key auto_increment,
    quantidade integer,
    data_fornecida date,
    custo decimal(10,2) not null,
    fk_id_armazem integer default 1 not null,
    fk_id_produto integer not null,
    fk_id_fornecedor integer default 1 not null,
    fk_id_usuario integer not null,
	data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_fornecedor) references fornecedor(id)
);
-- Nova tabela para controlo de vendas por unidades e lotes
ALTER TABLE produto_fornecido
ADD COLUMN unidade_fornecida VARCHAR(20) DEFAULT 'Unidade',
ADD COLUMN qtd_por_unidade INT DEFAULT 1;


create table movimento(
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
-- Nova tabela para controlo de vendas por unidades e lotes
ALTER TABLE movimento
ADD COLUMN unidade_movimentada VARCHAR(20) DEFAULT 'Unidade',
ADD COLUMN qtd_por_unidade INT DEFAULT 1;


create table stock(
	fk_id_produto integer,
    fk_id_armazem integer,
    saldo integer not null,
    primary key (fk_id_produto,fk_id_armazem),
    foreign key(fk_id_produto) references produto(id) on delete cascade,
    foreign key(fk_id_armazem) references armazem(id) on delete cascade
);

-- Armazena as siglas das facturas e o numero de dias uteis
create table documentocomercial(
	id integer primary key auto_increment,
    sigla varchar(10) not null,
    nome varchar(30) not null,
    diasuteis integer not null,
    descricao text not null    
);



-- Tabelas para armazenar dados das facturacoes
CREATE TABLE factura (
    numero VARCHAR(20) PRIMARY KEY,
    data_emissao DATE,
    data_vencimento DATE NOT NULL,
    total decimal(10,2) NOT NULL,
    totalPago decimal(10,2) default 0.0,
    saldoPendente decimal(10,2) default 0.0, -- Devido ainda por pagar
    estado varchar(30) default 'Pendente', -- Pago/ Pendente/ Cancelado
    condicaoPagemento varchar(30) default 'A vista', -- A vista/Parcelado
    numeroParcelas integer default 0 not null,
    fk_id_empresa INTEGER NOT NULL,
    fk_id_cliente INTEGER NOT NULL,
    fk_id_usuario INTEGER NOT NULL,
    fk_id_doc INTEGER NOT NULL,
    FOREIGN KEY (fk_id_doc) REFERENCES documentocomercial(id),
    FOREIGN KEY (fk_id_empresa) REFERENCES empresa(id),
    FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (fk_id_cliente) REFERENCES cliente(id)
);

ALTER TABLE factura
ADD COLUMN fk_id_armazem INT,
ADD CONSTRAINT fk_factura_armazem FOREIGN KEY (fk_id_armazem) REFERENCES armazem(id);
 
 
create table itemfactura(
	id integer primary key auto_increment,
   -- preco decimal(10,2) not null, o protudo ja tem relacao com o preco de venda
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    -- fk_id_desconto integer, o produto ja tem relacao com o desconto na tabela ProdutoDesconto
    fk_id_produto integer not null,
    fk_numero varchar(20) not null,
    fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_desconto) references desconto(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_numero) references factura(numero)
);

ALTER TABLE itemfactura
ADD COLUMN unidade_vendida VARCHAR(20) DEFAULT 'Unidade',
ADD COLUMN qtd_por_unidade INT DEFAULT 1;

create table itemcotacao(
	id integer primary key auto_increment,
    preco decimal(10,2) not null,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    fk_id_desconto integer,
    fk_id_produto integer not null,
    fk_numero varchar(20) not null,
    fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_desconto) references desconto(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_numero) references factura(numero)
);


create table metodo_pagamento(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table parcelas(
	id integer primary key auto_increment,
    parcelaNumero integer not null,
    taxaJuros decimal(4,3) not null,
    taxaDeAtraso decimal(4,3) default 0.00, -- 50% da taxa de juro normal
    valorParcela decimal(10,2) not null,
    valorPagoParcela decimal(10,2) default 0.0,
    dataVencimento date,
    dataPagamento date,
    estado varchar(30) default 'Pendente', -- Pago/ Pendente/ Atrasada
    fk_factura VARCHAR(20) not null,
    fk_usuario integer not null,
    foreign key (fk_usuario) references usuario(id),
    FOREIGN KEY (fk_factura) REFERENCES factura(numero)
);

-- transacoes 
create table transacoes(
	id integer primary key auto_increment,
    data_pagamento date,
    valorRecebido decimal(10,2) not null,
    valorPago decimal(10,2) not null,
    trocos decimal(10,2) not null,
    tipo_transacao VARCHAR(50) NOT NULL, -- Ex: Entrada, Saída
    descricao text,
    fk_id_contabancaria integer,
    fk_id_metodo_pagamento integer,
    fk_numero varchar(20),
    fk_parcela integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_contabancaria) references contabancaria(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_metodo_pagamento) references metodo_pagamento(id),
    foreign key (fk_numero) references factura(numero),
    foreign key (fk_parcela) references parcelas(id)
);

-- esta tabela sera actualizada com uma trigger
CREATE TABLE caixa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    saldo_inicial DECIMAL(10, 2) NOT NULL,
    saldo_atual DECIMAL(10, 2) NOT NULL,
    fk_id_transacoes integer not null,
    foreign key (fk_id_transacoes) references transacoes(id)
);

-- conta corrente
 create table contacorrente(
	id integer primary key auto_increment,
	saldo_inicial DECIMAL(10, 2) NOT NULL,
	saldo_atual DECIMAL(10, 2) NOT NULL, 
    fk_id_transacoes integer not null,
    foreign key (fk_id_transacoes) references transacoes(id)
);

-- __________________________________________________ Tabelas para suportar funcionalidades de requisicoes __________________________________________________
-- A requisicao quando ela e criada tem o seu estado inicial como Pendente
create table requisicao(
	id integer primary key auto_increment,
    dataRequisicao date default (current_date),
    dataNecessidade date not null,
    estado varchar(30) not null default 'Pendente', -- Pendente, Em processamento, Aprovada, Finalizada
    motivo text not null,
    fk_usuario integer not null,
    foreign key(fk_usuario) references usuario(id)
);

create table item_requisicao(
	id integer primary key auto_increment,
    qtd integer not null,
    estado varchar(30) not null default 'Pendente', -- Pendente, Aprovada
    fk_requisicao integer not null,
    fk_unidade integer not null,
    fk_produto integer not null,
    fk_armazem integer not null,
    foreign key(fk_requisicao) references requisicao(id),
    foreign key(fk_unidade) references unidade_medida(id),
    foreign key(fk_produto) references produto(id),
    foreign key(fk_armazem) references armazem(id)
);

create table historico_requisicao(
	id integer auto_increment,
    fk_requisicao integer,
    estado varchar(30) not null, -- Pendente, Em processamento Aprovada, Finalizada
    dataEstado datetime default current_timestamp,
    observacao text,
    primary key(id, fk_requisicao),
    foreign key(fk_requisicao) references requisicao(id)
);

