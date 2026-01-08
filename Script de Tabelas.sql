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
    nuit integer
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
	idFornecedor integer,
    foreign key (idFornecedor) references fornecedor(id) on delete cascade
);

create table contacto_fornecedor(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(100) unique,
    website varchar(100) ,
    idFornecedor integer,
    foreign key (idFornecedor) references fornecedor(id) on delete cascade
);

create table armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    tipo varchar(50) not null, -- de Armazém: (Central, Regional, Loja, etc.)
    descricao varchar(50),
    estado boolean default 1 not null -- Ativo/Inativo.
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
	idArmazem integer,
    foreign key (idArmazem) references armazem(id) on delete cascade
);

create table contacto_armazem(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50),
    idArmazem integer,
    foreign key (idArmazem) references armazem(id) on delete cascade
);

create table categoria(
	id integer primary key auto_increment,
    grupo varchar(50) not null,
    subgrupo varchar(50),
    familia varchar(50),
    descricao text
);
    
create table unidadeMedida(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    sigla varchar(10) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    tipoProduto enum('Simples', 'Composto') default 'Simples',
    unidadePorTipo integer default 1,
    descricao text,
    controle_stock boolean not null,
    nivelstock integer default 0,
    idCategoria integer not null,
    idUnidade integer,
    idImposto integer,
    foreign key (idImposto) references imposto(id),
    foreign key (idCategoria) references categoria(id),
    foreign key (idUnidade) references unidadeMedida(id)
);
create table stock(
	idProduto integer,
    idArmazem integer,
    saldo integer not null,
    primary key (idProduto,idArmazem),
    foreign key(idProduto) references produto(id) on delete cascade,
    foreign key(idArmazem) references armazem(id) on delete cascade
);
delimiter //
CREATE TRIGGER Tg_C_Stock
	AFTER INSERT ON produto
	FOR EACH ROW
	BEGIN
		IF NEW.controle_stock THEN
			INSERT INTO stock (idProduto, idArmazem, saldo)
			SELECT NEW.id, id, 0 FROM armazem;
		END IF;
	END//
delimiter ;


create table lote (
    id integer primary key auto_increment,
    codigo varchar(20) unique not null, -- Ex: 'L1', 'L2', 'L3'
    idProduto integer not null,
    idArmazem integer not null,
    idFornecedor integer default 1 not null,
    dataEntrada date not null,
    quantidadeInicial integer,
    quantidadeAtual integer not null,
    custoUnitario decimal(10,2) not null,
    valorTotal decimal(12,2) generated always as (quantidadeAtual * custoUnitario) stored,
    estado enum('Ativo','Esgotado') default 'Ativo',
    foreign key (idProduto) references produto(id),
    foreign key (idFornecedor) references fornecedor(id),
    foreign key (idArmazem) references armazem(id)
);


DELIMITER //
CREATE TRIGGER copiar_saldo_para_lote
BEFORE INSERT ON lote
FOR EACH ROW
BEGIN
    -- Declaração de uma variável para armazenar o saldo
    DECLARE saldo_atual integer;

    -- Busca o saldo atual do produto no armazém correspondente
    SELECT saldo
    INTO saldo_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem LIMIT 1;
    
    set NEW.quantidadeInicial= saldo_atual;
END //
DELIMITER ;

create table requisicao(
	id integer primary key auto_increment,
    dataRequisicao date default (current_date),
    dataNecessidade date not null,
    estado varchar(30) not null default 'Pendente', -- Pendente: nao tem itens associados a ela, Em processamento: tem itens associados, Aprovada: confirma que existe stock suficiente, Finalizada:confirma retirada do stock
    motivo text not null
);

create table itemRequisicao(
	id integer primary key auto_increment,
    quantidade integer not null,
    idRequisicao integer not null,
    idProduto integer not null,
    idArmazem integer not null,
    foreign key(idRequisicao) references requisicao(id),
    foreign key(idProduto) references produto(id),
    foreign key(idArmazem) references armazem(id)
);



create table movimento(
	id integer primary key auto_increment,
    tipoMovimento enum('Entrada','Saida')not null,
    dataMovimento date,
    quantidade integer not null,
    idArmazem integer default 1 not null,
    idProduto integer not null,
    idLote integer,
    idItemRequisicao integer NULL,
    FOREIGN KEY (idArmazem) REFERENCES armazem(id),
    FOREIGN KEY (idProduto) REFERENCES produto(id),
    FOREIGN KEY (idLote) REFERENCES lote(id),
    FOREIGN KEY (idItemRequisicao) REFERENCES itemRequisicao(id),
    CONSTRAINT chk_movimento_origem CHECK (
        (tipoMovimento = 'Entrada' AND idLote IS NOT NULL AND idItemRequisicao IS NULL)
        OR
        (tipoMovimento = 'Saida' AND idItemRequisicao IS NOT NULL AND idLote IS NULL)
    )
);

DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
	-- Registra movimento de entrada de lote
	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idLote) 
		VALUES ('Entrada', NEW.dataEntrada, NEW.quantidadeAtual,NEW.idArmazem, NEW.idProduto, NEW.id);
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_actualizaStock_IO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE totalMovimentado INT;
    DECLARE estoque_atual INT;

    SET totalMovimentado = NEW.quantidade;

    SELECT saldo INTO estoque_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem
    LIMIT 1;

    -- Apenas ENTRADAS
    IF NEW.tipoMovimento = 'Entrada' THEN
        UPDATE stock
        SET saldo = estoque_atual + totalMovimentado
        WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;

-- Para rastrear quais lotes foram usados em cada saída (ex: vendas grandes que usam vários lotes),
-- recomenda-se criar uma tabela adicional movimento_lote.
CREATE TABLE movimento_lote (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idMovimento INT,
    idLote INT,
    quantidadeRetirada INT,
    FOREIGN KEY (idMovimento) REFERENCES movimento(id),
    FOREIGN KEY (idLote) REFERENCES lote(id)
);

-- A requisicao quando ela e criada tem o seu estado inicial como Pendente


DELIMITER //
CREATE TRIGGER Tg_RegistaMovimentoSaida
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    -- Registra movimento de saide de lote
	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idItemRequisicao) 
		VALUES ('Saida', CURDATE(), NEW.quantidade,NEW.idArmazem, NEW.idProduto, NEW.id);
        
	-- update requisicao set estado = 'Em processamento' where idItemRequisicao=NEW.idRequisicao;

END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_FIFO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE qtdRestante INT;
    DECLARE loteID INT;
    DECLARE qtdLote INT;
    DECLARE done INT DEFAULT 0;

    DECLARE curLotes CURSOR FOR
        SELECT id, quantidadeAtual
        FROM lote
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
          AND quantidadeAtual > 0
        ORDER BY dataEntrada ASC;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    IF NEW.tipoMovimento = 'Saida' THEN
        SET qtdRestante = NEW.quantidade;

        OPEN curLotes;

        read_loop: LOOP
            FETCH curLotes INTO loteID, qtdLote;
            IF done = 1 OR qtdRestante <= 0 THEN
                LEAVE read_loop;
            END IF;

            IF qtdLote >= qtdRestante THEN
                -- Retira o restante do lote atual
                UPDATE lote
                SET quantidadeAtual = quantidadeAtual - qtdRestante,
                    estado = CASE WHEN quantidadeAtual - qtdRestante = 0 THEN 'Esgotado' ELSE estado END
                WHERE id = loteID;

                -- Registra no log
                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdRestante);

                SET qtdRestante = 0;
            ELSE
                -- Lote esgotado completamente
                UPDATE lote
                SET quantidadeAtual = 0,
                    estado = 'Esgotado'
                WHERE id = loteID;

                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdLote);

                SET qtdRestante = qtdRestante - qtdLote;
            END IF;
        END LOOP;

        CLOSE curLotes;

        -- Atualiza saldo geral no stock
        UPDATE stock
        SET saldo = saldo - NEW.quantidade
        WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;

        IF qtdRestante > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente para completar a saída (FIFO).';
        END IF;
    END IF;
END//
DELIMITER ;


create table precoProdutoArmazem(
	id integer AUTO_INCREMENT, 
	idProduto integer,
    idArmazem integer,
    preco DECIMAL(10, 2) default 0.01 NULL,
    precoVenda DECIMAL(10, 2) default 0.01 NULL,
    precoFinal DECIMAL(10, 2) default 0.01 NULL,
    dataValidade DATE,
    estado varchar(20) default 'activo' not null,
    foreign key (idProduto) references produto(id) on delete cascade,
    foreign key (idArmazem) references armazem(id)on delete cascade,
    primary key(id, idProduto, idArmazem)
);

create table produtodesconto(
	idProduto integer not null,
    idDesconto integer not null,
    idArmazem integer not null,
    dataInicio date not null,
    dataFim date not null,
    percentagem decimal(4,2),
    foreign key (idProduto) references produto(id) on delete cascade,
    foreign key (idDesconto) references desconto(id) on delete cascade,
    foreign key (idArmazem) references armazem(id)on delete cascade,
    primary key(idProduto, idDesconto, idArmazem, dataInicio, dataFim)
);
drop table precoProdutoArmazem;
-- CREATE TABLE precovenda (
--     id integer PRIMARY KEY AUTO_INCREMENT, 
--     precoNormal DECIMAL(10, 2) default 0.01 NULL,
--     precoVenda DECIMAL(10, 2) default 0.01 NULL,
--     precoFinal DECIMAL(10, 2) default 0.01 NULL,
--     dataValidade DATE,
--     estado varchar(20) default 'activo' not null,
--     idProduto integer not null,
--     idArmazem integer not null,
--     fk_id_usuario integer not null,
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (idProduto) REFERENCES produto(id) on delete cascade,
--     foreign key (idArmazem) references armazem(id)on delete cascade,
--     foreign key (fk_id_usuario) references usuario(id)
-- );
-- create table stock(
-- 	fk_id_produto integer,
--     fk_id_armazem integer,
--     saldo integer not null,
--     primary key (fk_id_produto,fk_id_armazem),
--     foreign key(fk_id_produto) references produto(id) on delete cascade,
--     foreign key(fk_id_armazem) references armazem(id) on delete cascade
-- );
-- desc produto_fornecido;
-- create table produto_fornecido(
-- 	id integer primary key auto_increment,
--     quantidade integer,
--     unidade_fornecida enum('Simples', 'Composto') DEFAULT 'Simples',
--     qtd_por_unidade integer DEFAULT 1,
--     data_fornecida date,
--     custo decimal(10,2) not null,
--     fk_id_armazem integer default 1 not null,
--     fk_id_produto integer not null,
--     fk_id_fornecedor integer default 1 not null,
--     fk_id_usuario integer not null,
-- 	data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     foreign key (fk_id_armazem) references armazem(id),
--     foreign key (fk_id_produto) references produto(id),
--     foreign key (fk_id_usuario) references usuario(id),
--     foreign key (fk_id_fornecedor) references fornecedor(id)
-- );

-- create table movimento(
-- 	id integer primary key auto_increment,
--     tipo_movimento varchar(8) not null,
--     tipoUnidadeMov enum('Simples', 'Composto') default 'Simples',
--     uniPorTipoMove integer,
--     data_movimento date,
--     quantidade integer not null,
--     fk_id_produto integer not null,
-- 	fk_id_usuario integer not null,
--     fk_id_armazem integer not null,
--     foreign key (fk_id_produto) references produto(id),
-- 	foreign key (fk_id_usuario) references usuario(id),
--     foreign key (fk_id_armazem) references armazem(id),
--     check (tipo_movimento in('Entrada','Saída'))
-- );

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
 
create table itemfactura(
	id integer primary key auto_increment,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    unidade_vendida enum('Simples', 'Composto') default 'Simples',
    qtd_por_unidade integer DEFAULT 1,
    fk_id_produto integer not null,
    fk_numero varchar(20) not null,
    fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_numero) references factura(numero)
);
desc itemfactura;



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
-- tabelas ja existentes do modulo de requisicoes

-- create table requisicao(
-- 	id integer primary key auto_increment,
--     dataRequisicao date default (current_date),
--     dataNecessidade date not null,
--     estado varchar(30) not null default 'Pendente', -- Pendente, Em processamento, Aprovada, Finalizada
--     motivo text not null,
--     fk_usuario integer not null,
--     foreign key(fk_usuario) references usuario(id)
-- );

-- create table item_requisicao(
-- 	id integer primary key auto_increment,
--     qtd integer not null,
--     estado varchar(30) not null default 'Pendente', -- Pendente, Aprovada
--     fk_requisicao integer not null,
--     fk_unidade integer not null,
--     fk_produto integer not null,
--     fk_armazem integer not null,
--     foreign key(fk_requisicao) references requisicao(id),
--     foreign key(fk_unidade) references unidade_medida(id),
--     foreign key(fk_produto) references produto(id),
--     foreign key(fk_armazem) references armazem(id)
-- );

create table historico_requisicao(
	id integer auto_increment,
    fk_requisicao integer,
    estado varchar(30) not null, -- Pendente, Em processamento Aprovada, Finalizada
    dataEstado datetime default current_timestamp,
    observacao text,
    primary key(id, fk_requisicao),
    foreign key(fk_requisicao) references requisicao(id)
);
