-- ============================================================
-- IMPLEMENTACAO COMPLETA PARA TESTE - JM-InvSM / SysFace
-- Banco separado: _sysface_teste
-- Gerado para validar tabelas, triggers, procedures e eventos
-- sem alterar a base principal _sysface.
--
-- ATENCAO: este script apaga e recria _sysface_teste.
-- Nao execute em ambiente de producao.
-- ============================================================

DROP DATABASE IF EXISTS _sysface_teste;
CREATE DATABASE _sysface_teste CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE _sysface_teste;

-- Caso precise dos eventos activos, execute manualmente com usuario administrador:
-- SET GLOBAL event_scheduler = ON;


-- ============================================================
-- INICIO: Script de Tabelas - 1-MODULO ENTRADA.sql
-- ============================================================

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

-- ============================================================
-- FIM: Script de Tabelas - 1-MODULO ENTRADA.sql
-- ============================================================


-- ============================================================
-- INICIO: Script de Tabelas - 2-MODULO DE REQUISICOES.sql
-- ============================================================

-- __________________________________________________ Tabelas para suportar funcionalidades de requisicoes __________________________________________________

create table fornecedor(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa FÃ­sica/JurÃ­dica)
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
    tipo varchar(50) not null, -- de ArmazÃ©m: (Central, Regional, Loja, etc.)
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
ALTER TABLE stock
ADD reservado INT DEFAULT 0,
ADD disponivel INT GENERATED ALWAYS AS (saldo - reservado);
create table lote (
    id integer primary key auto_increment,
    codigo varchar(20) unique not null, -- Ex: 'L1', 'L2', 'L3'
    idProduto integer not null,
    idArmazem integer not null,
    idFornecedor integer default 1 not null,
    dataEntrada date not null,
    quantidadeInicial integer,
    quantidadeAtual integer not null, -- deve ser quantidadeComprada
    custoUnitario decimal(10,2) not null,
    valorTotal decimal(12,2) generated always as (quantidadeAtual * custoUnitario) stored,
    estado enum('Ativo','Esgotado') default 'Ativo',
    foreign key (idProduto) references produto(id),
    foreign key (idFornecedor) references fornecedor(id),
    foreign key (idArmazem) references armazem(id)
);
create table requisicao(
	id integer primary key auto_increment,
    dataRequisicao date default (current_date),
    dataNecessidade date not null,
    estado ENUM(
    'Pendente',
	'Submetida',
	'AguardandoAprovacao',
	'Aprovada',
	'EmPicking',
	'Finalizada',
	'Cancelada'),
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
    tipoOrigem ENUM(
    'Compra',
	'Requisicao',
    'Transferencia',
    'Ajuste',
    'Devolucao',
    'Venda'
) not null,
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
    (tipoMovimento = 'Entrada' AND ((idLote IS NOT NULL AND idItemRequisicao IS NULL) OR tipoOrigem = 'Transferencia'))
    OR
    (tipoMovimento = 'Saida' AND (
        idItemRequisicao IS NOT NULL OR (tipoOrigem = 'Transferencia'  OR tipoOrigem = 'Venda')
    ))
)
);
ALTER TABLE movimento
CHANGE COLUMN tipoOrigem tipoOrigem ENUM('Compra', 'Requisicao', 'Transferencia', 'Ajuste', 'Devolucao', 'Venda') NOT NULL ;

CREATE TABLE movimento_lote (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idMovimento INT,
    idLote INT,
    quantidadeRetirada INT,
    FOREIGN KEY (idMovimento) REFERENCES movimento(id),
    FOREIGN KEY (idLote) REFERENCES lote(id)
);
CREATE TABLE picking(
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('Transferencia','Venda','Requisicao'),
    estado ENUM(
        'Aguardando',
        'Separando',
        'Separado',
        'Confirmado'
    ),
    operador INT,
    dataInicio DATETIME,
    dataFim DATETIME
);
ALTER TABLE picking
ADD idRequisicao INT,
ADD FOREIGN KEY(idRequisicao) REFERENCES requisicao(id);
CREATE TABLE reserva_stock(
    id INT AUTO_INCREMENT PRIMARY KEY,
    idProduto INT,
    idArmazem INT,
    idRequisicao INT,
    quantidade INT,
    estado ENUM(
        'Reservado',
        'Liberado',
        'Consumido',
        'Cancelado'
    ) DEFAULT 'Reservado',

    dataReserva DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(idProduto) REFERENCES produto(id),
    FOREIGN KEY(idArmazem) REFERENCES armazem(id),
    FOREIGN KEY(idRequisicao) REFERENCES requisicao(id)
);
CREATE TABLE transferencia_stock(
    id INT AUTO_INCREMENT PRIMARY KEY,
    idArmazemOrigem integer,
    idArmazemDestino integer,
    estado ENUM(
        'Pendente',
        'Reservado',
        'Em Transito',
        'Recebido',
        'Cancelado'
    ),
    dataCriacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(idArmazemOrigem) references armazem(id),
    foreign key(idArmazemDestino) references armazem(id)
);

CREATE TABLE item_transferencia(
    id INT AUTO_INCREMENT PRIMARY KEY,
    idTransferencia integer,
    idProduto integer,
    quantidade integer,
    quantidadeRecebida integer DEFAULT 0,
	foreign key(idTransferencia) references transferencia_stock(id)
);
CREATE TABLE stock_transito(
    id INT AUTO_INCREMENT PRIMARY KEY,
    idTransferencia INT,
    idProduto INT,
    quantidade INT,
    estado ENUM(
        'Em Transito',
        'Recebido'
    ),
    FOREIGN KEY(idTransferencia) REFERENCES transferencia_stock(id)
);



-- INSERT INTO fornecedor(tipo, razaosocial, nuit) VALUES
-- ('JurÃ­dica', 'AgroComercial Lda', 123456789),
-- ('JurÃ­dica', 'Distribuidora Zambezia', 987654321);

-- INSERT INTO endereco_fornecedor(provincia, cidade, bairro, avenida, rua, numero, idFornecedor) VALUES
-- ('ZambÃ©zia', 'Quelimane', 'Centro', 'Av. 25 de Setembro', 'Rua A', 10, 1),
-- ('ZambÃ©zia', 'Nicoadala', 'Central', 'Av. Principal', 'Rua B', 20, 2);

-- INSERT INTO contacto_fornecedor(pessoa_de_contacto, contacto, email, website, idFornecedor) VALUES
-- ('JoÃ£o Paulo', '841111111', 'agro@lda.co.mz', 'www.agrolda.co.mz', 1),
-- ('Maria JoÃ£o', '842222222', 'dist@zambezia.co.mz', 'www.distz.co.mz', 2);
--                      

-- INSERT INTO armazem(nome, tipo, descricao) VALUES
-- ('ArmazÃ©m Central', 'Central', 'Principal'),
-- ('Loja Quelimane', 'Loja', 'Venda ao pÃºblico');

-- INSERT INTO categoria(grupo, subgrupo, familia, descricao) VALUES
-- ('Alimentos', 'GrÃ£os', 'Arroz', 'Produtos alimentares'),
-- ('Bebidas', 'Refrigerantes', 'Soda', 'Bebidas gaseificadas');

-- INSERT INTO unidadeMedida(nome, sigla, descricao) VALUES
-- ('Kilograma', 'kg', 'Peso'),
-- ('Litro', 'L', 'Volume');


-- INSERT INTO imposto(nome, percentagem, descricao) VALUES
-- ('IVA', 17.000, 'Imposto padrÃ£o'),
-- ('Isento', 0.000, 'Sem imposto');

-- INSERT INTO produto(
-- nome, codigo_barra, tipoProduto, unidadePorTipo,
-- descricao, controle_stock, idCategoria, idUnidade, idImposto
-- ) VALUES
-- ('Arroz 50kg', 'P001', 'Simples', 1, 'Arroz branco', 1, 1, 1, 1),
-- ('Refrigerante Cola', 'P002', 'Simples', 1, 'Soda cola 2L', 1, 2, 2, 1);

-- -- Depois do insert de produtos, o trigger cria:
-- -- âœ” stock para cada armazÃ©m
-- -- âœ” saldo = 0
-- -- ---------------------------------------------------------
-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L001', 1, 1, 1, CURDATE(), 100, 25.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L002', 1, 1, 1, CURDATE(), 50, 26.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L003', 2, 1, 2, CURDATE(), 200, 15.00);

-- -- ---------------------------------------------------------

-- INSERT INTO requisicao(dataNecessidade, motivo)
-- VALUES (DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ReposiÃ§Ã£o loja');
-- select * from requisicao;

-- INSERT INTO itemRequisicao(quantidade, idRequisicao, idProduto, idArmazem)
-- VALUES
-- (60, 1, 1, 1),
-- (30, 1, 2, 1);
-- select * from itemRequisicao;
-- CALL AprovarRequisicao(1);



-- -- esperado:
-- -- reserva_stock criada
-- select * from reserva_stock;
-- -- stock.reservado atualizado
-- select * from stock;
-- -- picking criado
-- select * from picking;

-- CALL ConfirmarPicking(1);
-- -- esperado:
-- -- movimento SAIDA criado
-- -- FIFO executa:
-- -- L001 reduz primeiro
-- -- depois L002
-- -- stock atualizado corretamente
-- -- ---------------------------------------------------------



-- INSERT INTO transferencia_stock(idArmazemOrigem, idArmazemDestino, estado)
-- VALUES (1, 2, 'Pendente');
-- select * from item_transferencia;
-- INSERT INTO item_transferencia(idTransferencia, idProduto, quantidade)
-- VALUES (1, 1, 1);

-- CALL EnviarTransferencia(1);
-- --    
-- -- esperado:
-- -- movimento saÃ­da
-- select * from movimento;
-- -- stock_transito criado
-- select * from stock_transito;
-- -- stock reduzido via FIFO
-- select * from stock;

-- CALL ReceberTransferencia(1);
-- -- esperado:
-- -- movimento entrada
-- select * from movimento;
-- -- stock aumenta no destino
-- select * from stock;
-- -- estado atualizado
-- select * from transferencia_stock;
-- select * from stock_transito;
-- -- ---------------------------------------------------------
-- SELECT SUM(quantidadeAtual) FROM lote WHERE idProduto = 1;
-- SELECT saldo FROM stock WHERE idProduto = 1;
-- -- âœ” devem ser IGUAIS

-- SELECT * FROM movimento_lote;
-- -- âœ” deve mostrar distribuiÃ§Ã£o entre lotes

-- SELECT * FROM stock;
-- -- âœ” reservado nunca pode ultrapassar saldo



-- ============================================================
-- FIM: Script de Tabelas - 2-MODULO DE REQUISICOES.sql
-- ============================================================


-- ============================================================
-- INICIO: Script de Tabelas - 3-MODULO FACTURACAO.sql
-- ============================================================

 create table cliente(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa FÃ­sica/JurÃ­dica)
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
    tipo varchar(10) default 'Facturacao',
	fk_id_cliente integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_cliente) references cliente(id) on delete cascade,
    foreign key (fk_id_usuario) references usuario(id),
    check(tipo in('Facturacao','Envio'))
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
create table desconto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    percentagem decimal(4,2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table precoProdutoArmazem(
	id integer AUTO_INCREMENT, 
	idProduto integer,
    idArmazem integer,
    precoBase DECIMAL(10, 2) default 0.01 NULL,
    precoVenda DECIMAL(10, 2) default 0.01 NULL,
    precoFinal DECIMAL(10, 2) default 0.01 NULL,
    dataInicioVigencia DATE,
    dataFimVigencia DATE,
    estado enum('activo','desactivo') default 'desactivo' not null,
    foreign key (idProduto) references produto(id) on delete cascade,
    foreign key (idArmazem) references armazem(id)on delete cascade,
    primary key(id)
);
create table produtodesconto(
	id integer primary key auto_increment,
	idProduto integer not null,
    idDesconto integer not null,
    idArmazem integer not null,
    dataInicio date not null,
    dataFim date not null,
    foreign key (idProduto) references produto(id) on delete cascade,
    foreign key (idDesconto) references desconto(id) on delete cascade,
    foreign key (idArmazem) references armazem(id)on delete cascade
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
 
create table itemfactura(
	id integer primary key auto_increment,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
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
    tipo_transacao VARCHAR(50) NOT NULL, -- Ex: Entrada, SaÃ­da
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


-- ============================================================
-- FIM: Script de Tabelas - 3-MODULO FACTURACAO.sql
-- ============================================================


-- ============================================================
-- INICIO: Script de triggers - MODULO REQUISICOES.sql
-- ============================================================

DELIMITER //
CREATE PROCEDURE EnviarTransferencia(p_transferencia INT)
BEGIN

DECLARE id_produto INTEGER;
DECLARE id_armazem INTEGER;
DECLARE qtd INTEGER;
DECLARE saldo_stock INTEGER;

-- SAÃDA DO ARMAZÃ‰M ORIGEM
INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Saida',
    CURDATE(),
    quantidade,
    t.idArmazemOrigem,
    i.idProduto
FROM item_transferencia i
JOIN transferencia_stock t ON t.id=i.idTransferencia
WHERE i.idTransferencia=p_transferencia;

-- cria stock em trÃ¢nsito
INSERT INTO stock_transito(
    idTransferencia,
    idProduto,
    quantidade,
    estado
)
SELECT
    idTransferencia,
    idProduto,
    quantidade,
    'Em Transito'
FROM item_transferencia
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Em Transito'
WHERE id=p_transferencia;

	SELECT t.idArmazemOrigem
    INTO id_armazem
    FROM transferencia_stock t
    WHERE t.id = p_transferencia;
    
    SELECT it.quantidade
    INTO qtd
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT it.idProduto
    INTO id_produto
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT s.saldo into saldo_stock
    from stock s
    where s.idProduto=id_produto  and s.idArmazem=id_armazem;
    
UPDATE stock SET saldo = saldo_stock - qtd 
WHERE stock.idProduto = id_produto
AND stock.idArmazem = id_armazem;

END//
DELIMITER ;

delimiter //
CREATE PROCEDURE ReceberTransferencia(p_transferencia INT)
BEGIN

INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Entrada',
    CURDATE(),
    st.quantidade,
    t.idArmazemDestino,
    st.idProduto
FROM stock_transito st
JOIN transferencia_stock t ON t.id=st.idTransferencia
WHERE st.idTransferencia=p_transferencia;

UPDATE stock_transito
SET estado='Recebido'
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Recebido'
WHERE id=p_transferencia;

end//
delimiter ;



DELIMITER //
CREATE TRIGGER Tg_reservaStock_validar
BEFORE INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    DECLARE disponivel_atual INT DEFAULT 0;

    SELECT COALESCE((
        SELECT saldo - reservado
        FROM stock
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
        LIMIT 1
    ), 0)
    INTO disponivel_atual;

    IF NEW.quantidade <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Quantidade reservada deve ser maior que zero';
    END IF;

    IF disponivel_atual < NEW.quantidade THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para reservar requisicao';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_reservaStock
AFTER INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_reservaStock_estado
AFTER UPDATE ON reserva_stock
FOR EACH ROW
BEGIN
    IF OLD.estado = 'Reservado' AND NEW.estado IN ('Consumido', 'Liberado', 'Cancelado') THEN
        UPDATE stock
        SET reservado = GREATEST(reservado - OLD.quantidade, 0)
        WHERE idProduto = OLD.idProduto
          AND idArmazem = OLD.idArmazem;
    ELSEIF OLD.estado IN ('Consumido', 'Liberado', 'Cancelado') AND NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_estadoRequisicao
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    UPDATE requisicao
    SET estado = 'Submetida'
    WHERE id = NEW.idRequisicao
      AND estado = 'Pendente';
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE AprovarRequisicao(p_id INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_total_itens INT DEFAULT 0;
    DECLARE v_reservas_ativas INT DEFAULT 0;
    DECLARE v_indisponiveis INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_id
    LIMIT 1;

    IF v_estado IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao inexistente';
    END IF;

    IF v_estado NOT IN ('Submetida', 'AguardandoAprovacao', 'Pendente') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao esta em estado aprovavel';
    END IF;

    SELECT COUNT(*)
    INTO v_total_itens
    FROM itemRequisicao
    WHERE idRequisicao = p_id;

    IF v_total_itens = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui itens';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_id
      AND estado = 'Reservado';

    IF v_reservas_ativas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao ja possui reserva ativa';
    END IF;

    SELECT COUNT(*)
    INTO v_indisponiveis
    FROM (
        SELECT
            ir.idProduto,
            ir.idArmazem,
            SUM(ir.quantidade) AS quantidade_solicitada,
            COALESCE(MAX(s.saldo - s.reservado), 0) AS quantidade_disponivel
        FROM itemRequisicao ir
        LEFT JOIN stock s
          ON s.idProduto = ir.idProduto
         AND s.idArmazem = ir.idArmazem
        WHERE ir.idRequisicao = p_id
        GROUP BY ir.idProduto, ir.idArmazem
        HAVING quantidade_disponivel < quantidade_solicitada
    ) indisponiveis;

    IF v_indisponiveis > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para aprovar requisicao';
    END IF;

    INSERT INTO reserva_stock(
        idProduto,
        idArmazem,
        idRequisicao,
        quantidade,
        estado
    )
    SELECT
        idProduto,
        idArmazem,
        idRequisicao,
        SUM(quantidade),
        'Reservado'
    FROM itemRequisicao
    WHERE idRequisicao = p_id
    GROUP BY idProduto, idArmazem, idRequisicao;

    UPDATE requisicao
    SET estado = 'Aprovada'
    WHERE id = p_id;

    INSERT INTO picking(tipo, estado, idRequisicao, dataInicio)
    SELECT 'Requisicao', 'Aguardando', p_id, NOW()
    WHERE NOT EXISTS (
        SELECT 1 FROM picking WHERE idRequisicao = p_id
    );

    COMMIT;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE ConfirmarPicking(p_requisicao INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_reservas_ativas INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_requisicao
    LIMIT 1;

    IF v_estado <> 'Aprovada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas requisicoes aprovadas podem confirmar picking';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    IF v_reservas_ativas = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui reserva ativa para consumir';
    END IF;

    INSERT INTO movimento (
        tipoOrigem,
        tipoMovimento,
        dataMovimento,
        quantidade,
        idArmazem,
        idProduto,
        idItemRequisicao
    )
    SELECT
        'Requisicao',
        'Saida',
        CURDATE(),
        quantidade,
        idArmazem,
        idProduto,
        id
    FROM itemRequisicao
    WHERE idRequisicao = p_requisicao;

    UPDATE reserva_stock
    SET estado = 'Consumido'
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    UPDATE requisicao
    SET estado = 'Finalizada'
    WHERE id = p_requisicao;

    UPDATE picking
    SET estado = 'Confirmado',
        dataFim = NOW()
    WHERE idRequisicao = p_requisicao;

    COMMIT;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
	-- Registra movimento de entrada de lote
	INSERT INTO movimento (tipoOrigem, tipoMovimento, dataMovimento, quantidade, idArmazem, idProduto, idLote)
		VALUES ('Compra', 'Entrada', NEW.dataEntrada, NEW.quantidadeAtual, NEW.idArmazem, NEW.idProduto, NEW.id);
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

    IF NEW.tipoOrigem = 'Requisicao' AND NEW.tipoMovimento = 'Saida' THEN
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
            SET MESSAGE_TEXT = 'Stock insuficiente para completar a saÃ­da (FIFO).';
        END IF;
    END IF;
END//
DELIMITER ;



-- INSERT INTO fornecedor(tipo, razaosocial, nuit) VALUES
-- ('JurÃ­dica', 'AgroComercial Lda', 123456789),
-- ('JurÃ­dica', 'Distribuidora Zambezia', 987654321);

-- INSERT INTO endereco_fornecedor(provincia, cidade, bairro, avenida, rua, numero, idFornecedor) VALUES
-- ('ZambÃ©zia', 'Quelimane', 'Centro', 'Av. 25 de Setembro', 'Rua A', 10, 1),
-- ('ZambÃ©zia', 'Nicoadala', 'Central', 'Av. Principal', 'Rua B', 20, 2);

-- INSERT INTO contacto_fornecedor(pessoa_de_contacto, contacto, email, website, idFornecedor) VALUES
-- ('JoÃ£o Paulo', '841111111', 'agro@lda.co.mz', 'www.agrolda.co.mz', 1),
-- ('Maria JoÃ£o', '842222222', 'dist@zambezia.co.mz', 'www.distz.co.mz', 2);
--                      

-- INSERT INTO armazem(nome, tipo, descricao) VALUES
-- ('ArmazÃ©m Central', 'Central', 'Principal'),
-- ('Loja Quelimane', 'Loja', 'Venda ao pÃºblico');

-- INSERT INTO categoria(grupo, subgrupo, familia, descricao) VALUES
-- ('Alimentos', 'GrÃ£os', 'Arroz', 'Produtos alimentares'),
-- ('Bebidas', 'Refrigerantes', 'Soda', 'Bebidas gaseificadas');

-- INSERT INTO unidadeMedida(nome, sigla, descricao) VALUES
-- ('Kilograma', 'kg', 'Peso'),
-- ('Litro', 'L', 'Volume');


-- INSERT INTO imposto(nome, percentagem, descricao) VALUES
-- ('IVA', 17.000, 'Imposto padrÃ£o'),
-- ('Isento', 0.000, 'Sem imposto');

-- INSERT INTO produto(
-- nome, codigo_barra, tipoProduto, unidadePorTipo,
-- descricao, controle_stock, idCategoria, idUnidade, idImposto
-- ) VALUES
-- ('Arroz 50kg', 'P001', 'Simples', 1, 'Arroz branco', 1, 1, 1, 1),
-- ('Refrigerante Cola', 'P002', 'Simples', 1, 'Soda cola 2L', 1, 2, 2, 1);

-- -- Depois do insert de produtos, o trigger cria:
-- -- âœ” stock para cada armazÃ©m
-- -- âœ” saldo = 0
-- -- ---------------------------------------------------------
-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L001', 1, 1, 1, CURDATE(), 100, 25.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L002', 1, 1, 1, CURDATE(), 50, 26.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L003', 2, 1, 2, CURDATE(), 200, 15.00);

-- -- ---------------------------------------------------------

-- INSERT INTO requisicao(dataNecessidade, motivo)
-- VALUES (DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ReposiÃ§Ã£o loja');
-- select * from requisicao;

-- INSERT INTO itemRequisicao(quantidade, idRequisicao, idProduto, idArmazem)
-- VALUES
-- (60, 1, 1, 1),
-- (30, 1, 2, 1);
-- select * from itemRequisicao;
-- CALL AprovarRequisicao(1);



-- -- esperado:
-- -- reserva_stock criada
-- select * from reserva_stock;
-- -- stock.reservado atualizado
-- select * from stock;
-- -- picking criado
-- select * from picking;

-- CALL ConfirmarPicking(1);
-- -- esperado:
-- -- movimento SAIDA criado
-- -- FIFO executa:
-- -- L001 reduz primeiro
-- -- depois L002
-- -- stock atualizado corretamente
-- -- ---------------------------------------------------------



-- INSERT INTO transferencia_stock(idArmazemOrigem, idArmazemDestino, estado)
-- VALUES (1, 2, 'Pendente');
-- select * from item_transferencia;
-- INSERT INTO item_transferencia(idTransferencia, idProduto, quantidade)
-- VALUES (1, 1, 1);

-- CALL EnviarTransferencia(1);
-- --    
-- -- esperado:
-- -- movimento saÃ­da
-- select * from movimento;
-- -- stock_transito criado
-- select * from stock_transito;
-- -- stock reduzido via FIFO
-- select * from stock;

-- CALL ReceberTransferencia(1);
-- -- esperado:
-- -- movimento entrada
-- select * from movimento;
-- -- stock aumenta no destino
-- select * from stock;
-- -- estado atualizado
-- select * from transferencia_stock;
-- select * from stock_transito;
-- -- ---------------------------------------------------------
-- SELECT SUM(quantidadeAtual) FROM lote WHERE idProduto = 1;
-- SELECT saldo FROM stock WHERE idProduto = 1;
-- -- âœ” devem ser IGUAIS

-- SELECT * FROM movimento_lote;
-- -- âœ” deve mostrar distribuiÃ§Ã£o entre lotes

-- SELECT * FROM stock;
-- -- âœ” reservado nunca pode ultrapassar saldo



-- ============================================================
-- FIM: Script de triggers - MODULO REQUISICOES.sql
-- ============================================================


-- ============================================================
-- INICIO: Script de triggers.sql
-- ============================================================

-- drop procedure sp_registar_preco;
DELIMITER //
CREATE PROCEDURE sp_registar_preco (
    IN p_idProduto INT,
    IN p_idArmazem INT,
    IN p_precoBase DECIMAL(10,2)
)
BEGIN
	DECLARE desconto_taxa DECIMAL(4,2);
    DECLARE imposto_taxa DECIMAL(4,2);
    DECLARE p_Venda DECIMAL(10,2);
    DECLARE p_Final DECIMAL(10,2);
    
    START TRANSACTION;
    -- 1ï¸ desactivar preÃ§o activo anterior
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo',
        dataFimVigencia = CURRENT_DATE
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo';
      
	-- imposto (exemplo simples)
    SELECT i.percentagem
    INTO imposto_taxa
    FROM imposto i
    JOIN produto p ON p.idImposto = i.id
    WHERE p.id = p_idProduto
    LIMIT 1;
	-- preco venda
    IF imposto_taxa IS NULL THEN
        SET imposto_taxa = 0;
    END IF;
    -- set p_Venda = p_precoBase + ((p_precoBase * imposto_taxa) / 100);
     set p_Venda = p_precoBase + ((p_precoBase * imposto_taxa) / 100);

    
    -- desconto activo
    SELECT d.percentagem
    INTO desconto_taxa
    FROM produtodesconto pd
    JOIN desconto d ON d.id = pd.idDesconto
    WHERE pd.idProduto = p_idProduto
      AND pd.idArmazem = p_idArmazem
      AND CURRENT_DATE BETWEEN pd.dataInicio AND pd.dataFim
    LIMIT 1;
    -- preÃ§o final
    IF desconto_taxa IS NOT NULL THEN
        SET p_Final = p_Venda - ((p_Venda * desconto_taxa) / 100);
    ELSE
        SET p_Final = p_Venda;
    END IF;

    -- 2ï¸ inserir novo preÃ§o
    INSERT INTO precoProdutoArmazem
    (idProduto, idArmazem, precoBase,precoVenda,precoFinal,dataInicioVigencia,estado)
    VALUES
    (p_idProduto, p_idArmazem, p_precoBase, p_Venda, p_Final,CURRENT_DATE,'activo');
    
    COMMIT;
END;
//
DELIMITER ;

-- drop procedure sp_aplicar_desconto_ao_preco;
DELIMITER //
CREATE PROCEDURE sp_aplicar_desconto_ao_preco(
    IN p_idProduto INT,
    IN p_idArmazem INT
)
BEGIN
    DECLARE v_precoBase DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_precoVenda DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_precoFinal DECIMAL(10,2) DEFAULT 0.01;
    DECLARE v_desconto DECIMAL(4,2) DEFAULT 0;
    DECLARE v_imposto DECIMAL(4,2) DEFAULT 0;

    -- 1ï¸âƒ£ Obter o Ãºltimo preÃ§o activo
    SELECT precoBase
    INTO v_precoBase
    FROM precoProdutoArmazem
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo'
    ORDER BY dataInicioVigencia DESC
    LIMIT 1;

    -- Se nÃ£o existir preÃ§o anterior, mantÃ©m 0.01
    IF v_precoBase IS NULL THEN
        SET v_precoBase = 0.01;
    END IF;

    -- 2ï¸âƒ£ Obter desconto activo
    SELECT d.percentagem
    INTO v_desconto
    FROM desconto d
    JOIN produtodesconto pd ON pd.idDesconto = d.id
    WHERE pd.idProduto = p_idProduto
      AND pd.idArmazem = p_idArmazem
      AND CURRENT_DATE BETWEEN pd.dataInicio AND pd.dataFim
    ORDER BY pd.dataInicio DESC
    LIMIT 1;

    IF v_desconto IS NULL THEN
        SET v_desconto = 0;
    END IF;

    -- 3ï¸âƒ£ Obter imposto do produto
    SELECT i.percentagem
    INTO v_imposto
    FROM produto p
    JOIN imposto i ON p.idImposto = i.id
    WHERE p.id = p_idProduto
    LIMIT 1;

    IF v_imposto IS NULL THEN
        SET v_imposto = 0;
    END IF;

    -- 4ï¸âƒ£ Desactivar preÃ§o activo anterior
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo', dataFimVigencia = CURRENT_DATE
    WHERE idProduto = p_idProduto
      AND idArmazem = p_idArmazem
      AND estado = 'activo';

    -- 5ï¸âƒ£ Calcular preÃ§os
    SET v_precoVenda = v_precoBase + ((v_precoBase * v_imposto) / 100);
    SET v_precoFinal = v_precoVenda - (v_precoVenda * v_desconto / 100);

    -- 6ï¸âƒ£ Inserir novo preÃ§o
    INSERT INTO precoProdutoArmazem
    (idProduto, idArmazem, precoBase, precoVenda, precoFinal, estado, dataInicioVigencia)
    VALUES
    (p_idProduto, p_idArmazem, v_precoBase, v_precoVenda, v_precoFinal, 'activo', CURRENT_DATE);

END;
//
DELIMITER ;


-- SET GLOBAL event_scheduler = ON; -- executar manualmente se necessario
CREATE EVENT evt_marcar_expiracao_precos
ON SCHEDULE EVERY 1 DAY
DO
UPDATE precoProdutoArmazem
SET dataFimVigencia = CURRENT_DATE
WHERE estado = 'activo'
  AND dataFimVigencia IS NOT NULL
  AND dataFimVigencia < CURRENT_DATE;
  
-- Fim de Trigger e procedimentos para tratamento de precos

-- Para Controlo de Stock
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

DELIMITER //
CREATE TRIGGER copiar_saldo_para_lote
BEFORE INSERT ON lote
FOR EACH ROW
BEGIN
    -- DeclaraÃ§Ã£o de uma variÃ¡vel para armazenar o saldo
    DECLARE saldo_atual integer;

    -- Busca o saldo atual do produto no armazÃ©m correspondente
    SELECT saldo
    INTO saldo_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem LIMIT 1;
    
    set NEW.quantidadeInicial= saldo_atual;
END //
DELIMITER ;


-- DELIMITER //
-- CREATE TRIGGER Tg_registaMovimentoEntrada
-- AFTER INSERT ON lote
-- FOR EACH ROW
-- BEGIN
-- 	-- Registra movimento de entrada de lote
-- 	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idLote) 
-- 		VALUES ('Entrada', NEW.dataEntrada, NEW.quantidadeAtual,NEW.idArmazem, NEW.idProduto, NEW.id);
-- END//
-- DELIMITER ;


-- DELIMITER //
-- CREATE TRIGGER Tg_actualizaStock_IO
-- AFTER INSERT ON movimento
-- FOR EACH ROW
-- BEGIN
--     DECLARE totalMovimentado INT;
--     DECLARE estoque_atual INT;

--     SET totalMovimentado = NEW.quantidade;

--     SELECT saldo INTO estoque_atual
--     FROM stock
--     WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem
--     LIMIT 1;

--     -- Apenas ENTRADAS
--     IF NEW.tipoMovimento = 'Entrada' THEN
--         UPDATE stock
--         SET saldo = estoque_atual + totalMovimentado
--         WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;
--     END IF;
-- END//
-- DELIMITER ;


-- DELIMITER //
-- CREATE TRIGGER Tg_RegistaMovimentoSaida
-- AFTER INSERT ON itemRequisicao
-- FOR EACH ROW
-- BEGIN
--     -- Registra movimento de saide de lote
-- 	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idItemRequisicao) 
-- 		VALUES ('Saida', CURDATE(), NEW.quantidade,NEW.idArmazem, NEW.idProduto, NEW.id);
--         
-- 	-- update requisicao SET estado = 'Submetida' where idItemRequisicao=NEW.idRequisicao;

-- END//
-- DELIMITER ;


-- DELIMITER //
-- CREATE TRIGGER Tg_FIFO
-- AFTER INSERT ON movimento
-- FOR EACH ROW
-- BEGIN
--     DECLARE qtdRestante INT;
--     DECLARE loteID INT;
--     DECLARE qtdLote INT;
--     DECLARE done INT DEFAULT 0;

--     DECLARE curLotes CURSOR FOR
--         SELECT id, quantidadeAtual
--         FROM lote
--         WHERE idProduto = NEW.idProduto
--           AND idArmazem = NEW.idArmazem
--           AND quantidadeAtual > 0
--         ORDER BY dataEntrada ASC;

--     DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

--     IF NEW.tipoMovimento = 'Saida' THEN
--         SET qtdRestante = NEW.quantidade;

--         OPEN curLotes;

--         read_loop: LOOP
--             FETCH curLotes INTO loteID, qtdLote;
--             IF done = 1 OR qtdRestante <= 0 THEN
--                 LEAVE read_loop;
--             END IF;

--             IF qtdLote >= qtdRestante THEN
--                 -- Retira o restante do lote atual
--                 UPDATE lote
--                 SET quantidadeAtual = quantidadeAtual - qtdRestante,
--                     estado = CASE WHEN quantidadeAtual - qtdRestante = 0 THEN 'Esgotado' ELSE estado END
--                 WHERE id = loteID;

--                 -- Registra no log
--                 INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
--                 VALUES (NEW.id, loteID, qtdRestante);

--                 SET qtdRestante = 0;
--             ELSE
--                 -- Lote esgotado completamente
--                 UPDATE lote
--                 SET quantidadeAtual = 0,
--                     estado = 'Esgotado'
--                 WHERE id = loteID;

--                 INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
--                 VALUES (NEW.id, loteID, qtdLote);

--                 SET qtdRestante = qtdRestante - qtdLote;
--             END IF;
--         END LOOP;

--         CLOSE curLotes;

--         -- Atualiza saldo geral no stock
--         UPDATE stock
--         SET saldo = saldo - NEW.quantidade
--         WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;

--         IF qtdRestante > 0 THEN
--             SIGNAL SQLSTATE '45000'
--             SET MESSAGE_TEXT = 'Stock insuficiente para completar a saÃ­da (FIFO).';
--         END IF;
--     END IF;
-- END//
-- DELIMITER ;


-- Trigger para controlar fluxo de stock(saldo de determinado produto em seu armazarm) quando um movimento de saida e registado na tabela itemfactura
-- DELIMITER //
-- CREATE TRIGGER Tg_C_Saida
-- AFTER INSERT ON itemfactura
-- FOR EACH ROW
-- BEGIN
--     DECLARE total_unidades INT;
--     DECLARE estoque_atual INT;

--     SET total_unidades = NEW.quantidade * NEW.qtd_por_unidade;


--     SELECT saldo
--     INTO estoque_atual
--     FROM stock
--     WHERE idProduto = NEW.fk_id_produto
--       AND idArmazem = NEW.fk_id_armazem
--     LIMIT 1;

--     -- SÃ³ insere o movimento se houver saldo suficiente
--     IF estoque_atual IS NOT NULL AND estoque_atual >= total_unidades THEN
--         INSERT INTO movimento (
--             tipoMovimento,
--             dataMovimento,
--             quantidade,
--             idProduto,
--             idArmazem
--         )
--         VALUES (
--             'SaÃ­da',
--             NEW.data_adicao,
--             NEW.quantidade,
--             NEW.fk_id_produto,
--             NEW.fk_id_armazem
--         );
--     END IF;
-- END//
-- DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_C_Saida
AFTER INSERT ON itemfactura
FOR EACH ROW
BEGIN
    DECLARE qtd_restante INT;
    DECLARE qtd_total INT;
    DECLARE lote_id INT DEFAULT 0;
    DECLARE movimento_id INT;
    DECLARE qtd_lote INT;
    DECLARE qtd_saida INT;
    DECLARE saldo_atual INT DEFAULT 0;

    SET qtd_total = NEW.quantidade * NEW.qtd_por_unidade;
    SET qtd_restante = qtd_total;

    SELECT COALESCE((
        SELECT saldo
        FROM stock
        WHERE idProduto = NEW.fk_id_produto
          AND idArmazem = NEW.fk_id_armazem
        LIMIT 1
    ), 0)
    INTO saldo_atual;

    IF saldo_atual < qtd_total THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para concluir a venda';
    END IF;

    WHILE qtd_restante > 0 DO
        SET lote_id = 0;
        SET qtd_lote = 0;

        SELECT
            COALESCE((
                SELECT id
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0),
            COALESCE((
                SELECT quantidadeAtual
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0)
        INTO lote_id, qtd_lote;

        IF lote_id = 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Lote insuficiente para concluir a venda';
        END IF;

        SET qtd_saida = LEAST(qtd_lote, qtd_restante);

        UPDATE lote
        SET quantidadeAtual = quantidadeAtual - qtd_saida,
            estado = IF(quantidadeAtual - qtd_saida = 0, 'Esgotado', 'Ativo')
        WHERE id = lote_id;

        INSERT INTO movimento (
            tipoOrigem,
            tipoMovimento,
            dataMovimento,
            quantidade,
            idProduto,
            idArmazem,
            idLote
        ) VALUES (
            'Venda',
            'Saida',
            DATE(NEW.data_adicao),
            qtd_saida,
            NEW.fk_id_produto,
            NEW.fk_id_armazem,
            lote_id
        );

        SET movimento_id = LAST_INSERT_ID();

        INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
        VALUES (movimento_id, lote_id, qtd_saida);

        SET qtd_restante = qtd_restante - qtd_saida;
    END WHILE;

    UPDATE stock
    SET saldo = saldo - qtd_total
    WHERE idProduto = NEW.fk_id_produto
      AND idArmazem = NEW.fk_id_armazem;
END//
DELIMITER ;

-- ****************************************************************



-- Trigger para actualizar o numero de parcelas nas facturas quando elas vao ser pagas por parcelamento
delimiter //
create trigger _updateParcelasFactura
	after insert on parcelas
    for each row
    begin
		declare numero_de_parcelas int default 0;
        declare saldo decimal(10,2);
        
        select count(*) into numero_de_parcelas from parcelas where fk_factura = NEW.fk_factura;
        select SUM(valorParcela) into saldo from parcelas where fk_factura = NEW.fk_factura;
		update factura set numeroParcelas = numero_de_parcelas, condicaoPagemento = 'Parcelado', saldoPendente = saldo  where numero = new.fk_factura;
    end//
delimiter ;




-- Trigger para actualizar dados da parcela como valorPagoParcela e o estado; e saldo pendente da factura
delimiter //
create trigger _updateParcela_Factura
	after insert on transacoes
    for each row
    begin
        declare total_parcela decimal(10,2);
        declare total_pago decimal(10,2);
        declare condicao_pagemento varchar(30);
        declare transacoes_pagas decimal(10,2);
        declare saldo decimal(10,2);
        
        select f.saldoPendente into saldo from factura f where f.numero = NEW.fk_numero;
        SELECT p.valorParcela INTO total_parcela FROM parcelas p where p.id = NEW.fk_parcela;
        SELECT p.valorPagoParcela INTO total_pago FROM parcelas p where p.id = NEW.fk_parcela;
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valorPago) FROM transacoes t WHERE t.fk_parcela = p.id AND p.fk_factura =NEW.fk_numero  ) INTO transacoes_pagas 
		FROM parcelas p where p.id = NEW.fk_parcela;

        IF condicao_pagemento = 'Parcelado' THEN
			update parcelas set valorPagoParcela = transacoes_pagas  where id = new.fk_parcela;
        END IF;
        
        SELECT p.valorPagoParcela INTO total_pago FROM parcelas p where p.id = NEW.fk_parcela;
        IF total_parcela <= total_pago and condicao_pagemento = 'Parcelado' THEN
			update parcelas set estado = 'Paga', dataPagamento = NEW.data_pagamento where id = NEW.fk_parcela;
            update factura set saldoPendente = saldo - total_pago where numero = NEW.fk_numero;
        END IF;
        
	end//
delimiter ;


-- Trigger para actualizar totalPago da factura e estado da factura conforme as parcelas form pagas
delimiter //
create trigger _updateFactura_PagamentoParcelado
	after update on parcelas
    for each row
	begin
		declare total_pago decimal(10,2);
        declare total_factura decimal(10,2);
        declare condicao_pagemento varchar(30); 
        
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_factura;
        SELECT f.total INTO total_factura from factura f where f.numero = NEW.fk_factura;
        SELECT SUM(p.valorPagoParcela) INTO total_pago FROM parcelas p where p.estado = 'Paga' AND p.fk_factura = NEW.fk_factura;
        
        IF condicao_pagemento = 'Parcelado'  THEN 
			update factura set totalPago = total_pago where numero = new.fk_factura;
        END IF;
        
        SELECT f.total INTO total_factura from factura f where f.numero = NEW.fk_factura;
        IF total_factura <= total_pago AND condicao_pagemento = 'Parcelado'  THEN 
			update factura set estado = 'Paga' where numero = new.fk_factura;
        END IF;		
    end//
delimiter ;


-- Trigger actualiza os saldos iniciais e actual de Caixa e ContaCorrente quando transacoes sao registadas;
delimiter //
create trigger _updateCaixa_ContaCorrente
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
			-- Verifica se o mÃ©todo de pagamento Ã© igual a 1 (ajuste conforme necessÃ¡rio)
            IF NEW.fk_id_metodo_pagamento = 1 THEN
				-- Tenta buscar o saldo atual da Ãºltima linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM caixa
					ORDER BY id DESC LIMIT 1;

				-- Se nÃ£o encontrar saldo anterior, assume que o saldo inicial Ã© 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO caixa (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial Ã© o saldo atual anterior ou 0 se nÃ£o houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo Ã© o saldo anterior somado ao valor da transaÃ§Ã£o
					NEW.id                         -- FK da transaÃ§Ã£o inserida
					);
			ELSE
				-- Tenta buscar o saldo atual da Ãºltima linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM contacorrente
					ORDER BY id DESC LIMIT 1;

				-- Se nÃ£o encontrar saldo anterior, assume que o saldo inicial Ã© 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO contacorrente (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial Ã© o saldo atual anterior ou 0 se nÃ£o houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo Ã© o saldo anterior somado ao valor da transaÃ§Ã£o
					NEW.id                         -- FK da transaÃ§Ã£o inserida
					);				
            END IF;
	end//
delimiter ;


-- Este trigger actualiza total pago da factura, saldo pendente e estado da factura quando a condicao de pagamento e a vista
delimiter //
create trigger _updateFacturaPagamento_Avista
	after insert on transacoes
    for each row
    begin
        declare total_factura decimal(10,2);
        declare total_pago decimal(10,2);
        declare transacoes_pagas decimal(10,2);
        declare condicao_pagemento varchar(30); 
        
        SELECT f.total INTO total_factura FROM factura f where f.numero = NEW.fk_numero;
		SELECT f.totalPago INTO total_pago FROM factura f where f.numero =  NEW.fk_numero;
        SELECT f.condicaoPagemento INTO condicao_pagemento from factura f where f.numero = NEW.fk_numero;
        
        SELECT (SELECT SUM(t.valorPago) FROM transacoes t WHERE t.fk_numero = f.numero) INTO transacoes_pagas 
		FROM factura f 
		where f.numero = NEW.fk_numero;
        
        IF total_factura > total_pago and condicao_pagemento = 'A vista' THEN
			update factura set totalPago=transacoes_pagas, saldoPendente=(total_factura-transacoes_pagas) where numero=NEW.fk_numero; 
        END IF;
        
        SELECT f.totalPago INTO total_pago FROM factura f where f.numero =  NEW.fk_numero;
        IF total_factura = total_pago and condicao_pagemento = 'A vista' THEN
			update factura set estado = 'Paga' where numero = NEW.fk_numero; 
        END IF;        
	end//
delimiter ;



-- _______________________________________________________________________________________________________________PROCEDIMENTOS ARMACENADOS____________________________________________________________________________________________________________________

-- Procedimento para ajustar e manter o controle dos precos de um produto. O procedimento e chamado atraves de um evento que e executado diariamente
DELIMITER //
CREATE PROCEDURE procedure_updatePrecoVenda()
BEGIN
    UPDATE precoProdutoArmazem
    SET estado = 'desactivo',
        dataFimVigencia = COALESCE(dataFimVigencia, CURDATE())
    WHERE id > 0
      AND dataFimVigencia <= CURDATE()
      AND estado = 'activo';
END//
DELIMITER ;

-- Eventro para chamar o procedimento procedure_updatePrecoVenda()
CREATE EVENT evento_updatePrecoVenda
	ON SCHEDULE EVERY 1 DAY
	DO
		CALL procedure_updatePrecoVenda();
        
-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar o estado das parcelas se estao atrasadas ou nao

DELIMITER //
CREATE PROCEDURE procedure_updateEstadoParcela()
BEGIN
    UPDATE parcelas
    SET estado = 'Atrasada'
    WHERE dataVencimento <= CURDATE() AND estado <> 'Paga';
END//
DELIMITER ;

CREATE EVENT evento_updateEstadoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateEstadoParcela();
    

-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar a taxa de juros em atraso das parcelas
DELIMITER //
CREATE PROCEDURE procedure_updateTaxaAtrasoParcela()
BEGIN
    UPDATE parcelas
    SET taxaDeAtraso = taxaJuros / 2
    WHERE estado = 'Atrasada';
END //
DELIMITER ;

CREATE EVENT evento_updateTaxaAtrasoParcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateTaxaAtrasoParcela();        


-- Procedimento armazenado que e chamado atraves de uma EVENT para controlar a taxa de juros em atraso das parcelas
delimiter //
CREATE PROCEDURE procedure_updateSaldoFactura_com_Atraso_de_Parcela()
BEGIN
    -- Atualizar o saldo pendente de cada fatura com base nas parcelas
    UPDATE factura f
    SET f.saldoPendente = (
        SELECT COALESCE(SUM(
            CASE 
                WHEN p.estado = 'Atrasada' THEN (p.valorParcela * p.taxaDeAtraso) + (p.valorParcela - p.valorPagoParcela)
                ELSE 
					case when p.estado = 'Pendente' THEN
						p.valorParcela - p.valorPagoParcela
					end
            END
        ), 0)
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    )
    WHERE EXISTS (
        SELECT 1 
        FROM parcelas p
        WHERE p.fk_factura = f.numero
    );
END//
delimiter ;

CREATE EVENT evento_updateSaldoFactura_com_Atraso_de_Parcela
	ON SCHEDULE EVERY 1 MINUTE
	DO
		CALL procedure_updateSaldoFactura_com_Atraso_de_Parcela();



-- ============================================================
-- FIM: Script de triggers.sql
-- ============================================================


-- ============================================================
-- VALIDACOES RAPIDAS DA ESTRUTURA
-- ============================================================
SHOW TABLES;
SHOW PROCEDURE STATUS WHERE Db = DATABASE();
SHOW TRIGGERS;
SHOW EVENTS;
