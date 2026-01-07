-- SEMI CONCLUIDO MODULO DE ARMAZEM
-- FALTA TRIGGER PARA ACTUALIZAR ESTADO DA REQUISICAO E SO RETIRAR DO ESTOQUE SE O ESTADO FOR FINALIZADO
drop database inventarios;
create database inventarios;
use inventarios;

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

-- Trigger para actualizar saldos do stock segundo os movimentos
-- DELIMITER //
-- CREATE TRIGGER Tg_actualizaStock_IO
-- AFTER INSERT ON movimento
-- FOR EACH ROW
-- BEGIN
--     DECLARE totalMovimentado integer; -- podem ser entradas ou saidas
--     DECLARE estoque_atual integer; -- saldo no stock;
--     DECLARE estadoLote varchar(20);
--     DECLARE prodID integer;
--     DECLARE armID integer;
--     DECLARE loteID integer;

--     SET totalMovimentado = NEW.quantidade;
--     
--        -- Busca dados do lote relacionado ao movimento
--     SELECT idProduto, idArmazem
--     INTO  prodID, armID
--     FROM movimento
--     WHERE id = NEW.id
--     LIMIT 1;

--     -- Busca o saldo atual do stock
--     SELECT saldo
--     INTO estoque_atual
--     FROM stock
--     WHERE idProduto = prodID AND idArmazem = armID
--     LIMIT 1;

--     -- Se for SAÍDA e o lote estiver ativo
--     IF NEW.tipoMovimento = 'Saida' THEN
--         UPDATE stock
--         SET saldo = GREATEST(estoque_atual - totalMovimentado, 0)
--         WHERE idProduto = prodID AND idArmazem = armID;
--     END IF;

--     -- Se for ENTRADA e o lote estiver ativo
--     IF NEW.tipoMovimento = 'Entrada' THEN
--         UPDATE stock
--         SET saldo = estoque_atual + totalMovimentado
--         WHERE idProduto = prodID AND idArmazem = armID;
--     END IF;
-- END//
-- DELIMITER ;

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




-- create index idx_lote_fifoseq on lote(idProduto, idArmazem, dataEntrada);

-- create view vw_stock as
-- select 
--     idProduto,
--     idArmazem,
--     sum(quantidadeAtual) as saldo
-- from lote
-- group by idProduto, idArmazem;


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






select * from produto;
select * from lote;
select * from armazem;
select * from movimento;
select * from movimento_lote;
select * from stock;
select * from requisicao;
select * from itemRequisicao;

-- Dados base
INSERT INTO fornecedor (tipo, razaosocial, nuit) VALUES ('Jurídica', 'Fornecedor A', 123456789);
INSERT INTO armazem (nome, tipo, descricao) VALUES ('Central', 'Principal', 'Armazém Central');
INSERT INTO categoria (grupo, subgrupo, familia, descricao) VALUES ('Bebidas', 'Águas', 'Minerais', 'Águas minerais');
INSERT INTO unidadeMedida (nome, sigla) VALUES ('Litro', 'L');
INSERT INTO imposto (nome, percentagem) VALUES ('IVA', 17.000);

-- Produto
INSERT INTO produto (nome, codigo_barra, tipoProduto, unidadePorTipo, descricao, controle_stock, idCategoria, idUnidade, idImposto)
VALUES ('Água Mineral 1L', '7891234567890', 'Simples', 1, 'Garrafa de 1 litro', TRUE, 1, 1, 1);
use _sysface;
-- Lotes de entrada
INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES 
('L006', 3, 3, 1, '2026-01-07', 800, 60.00);
INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES 
('L002', 1, 1, 1, '2025-09-01', 1500, 70.00);
INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES 
('L003', 1, 1, 1, '2025-10-01', 1500, 75.00);INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES 
('L004', 1, 1, 1, '2025-10-01', 500, 70.00);

-- Criar trigger para alterar o estado da requisicao
Insert into requisicao (dataNecessidade, motivo) value ('2025-11-15','Transsferencia de Mercadoria');

INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES ( 3000, 1, 1, 1);

INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES (700, 1, 1, 1);


