-- ============================================================
-- 00_TABELAS_COMPLETO.sql
-- JM-InvSM / SysFace - SCRIPT AGRUPADO DE TABELAS
-- Banco de teste: _sysface_teste
--
-- Modulos contidos neste ficheiro:
-- 1. MODULO ENTRADA / ADMINISTRACAO
--    Empresa, endereco/contacto da empresa, conta bancaria,
--    nivel de acesso, permissao e usuario.
-- 2. MODULO INVENTARIO E REQUISICOES
--    Fornecedor, armazem, categoria, unidade, imposto, produto,
--    stock, lote, requisicao, itens, movimentos, picking, reservas,
--    transferencias e stock em transito.
-- 3. MODULO FACTURACAO / PAGAMENTOS
--    Cliente, desconto, preco por produto/armazem, documentos
--    comerciais, factura, itemfactura, itemcotacao, parcelas,
--    transacoes, caixa e conta corrente.
--
-- ATENCAO: este ficheiro apaga e recria a base _sysface_teste.
-- Execute primeiro este script, depois procedures, triggers e dados.
-- ============================================================

DROP DATABASE IF EXISTS _sysface_teste;
CREATE DATABASE _sysface_teste CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE _sysface_teste;
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
    descricao_tipo VARCHAR(100) NULL,
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

-- ============================================================

-- __________________________________________________ Tabelas para suportar funcionalidades de requisicoes __________________________________________________

create table fornecedor(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa FÃƒÂ­sica/JurÃƒÂ­dica)
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
    tipo varchar(50) not null, -- de ArmazÃƒÂ©m: (Central, Regional, Loja, etc.)
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

CREATE TABLE transferencia_lote (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idTransferencia INT NOT NULL,
    idProduto INT NOT NULL,
    idLoteOrigem INT NOT NULL,
    idLoteDestino INT NULL,
    idMovimentoSaida INT NULL,
    quantidade INT NOT NULL,
    idFornecedor INT NOT NULL,
    custoUnitario DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado ENUM('Em Transito','Recebido') NOT NULL DEFAULT 'Em Transito',
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTransferencia) REFERENCES transferencia_stock(id),
    FOREIGN KEY (idProduto) REFERENCES produto(id),
    FOREIGN KEY (idLoteOrigem) REFERENCES lote(id),
    FOREIGN KEY (idLoteDestino) REFERENCES lote(id),
    FOREIGN KEY (idMovimentoSaida) REFERENCES movimento(id),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id)
);



-- INSERT INTO fornecedor(tipo, razaosocial, nuit) VALUES
-- ('JurÃƒÂ­dica', 'AgroComercial Lda', 123456789),
-- ('JurÃƒÂ­dica', 'Distribuidora Zambezia', 987654321);

-- INSERT INTO endereco_fornecedor(provincia, cidade, bairro, avenida, rua, numero, idFornecedor) VALUES
-- ('ZambÃƒÂ©zia', 'Quelimane', 'Centro', 'Av. 25 de Setembro', 'Rua A', 10, 1),
-- ('ZambÃƒÂ©zia', 'Nicoadala', 'Central', 'Av. Principal', 'Rua B', 20, 2);

-- INSERT INTO contacto_fornecedor(pessoa_de_contacto, contacto, email, website, idFornecedor) VALUES
-- ('JoÃƒÂ£o Paulo', '841111111', 'agro@lda.co.mz', 'www.agrolda.co.mz', 1),
-- ('Maria JoÃƒÂ£o', '842222222', 'dist@zambezia.co.mz', 'www.distz.co.mz', 2);
--                      

-- INSERT INTO armazem(nome, tipo, descricao) VALUES
-- ('ArmazÃƒÂ©m Central', 'Central', 'Principal'),
-- ('Loja Quelimane', 'Loja', 'Venda ao pÃƒÂºblico');

-- INSERT INTO categoria(grupo, subgrupo, familia, descricao) VALUES
-- ('Alimentos', 'GrÃƒÂ£os', 'Arroz', 'Produtos alimentares'),
-- ('Bebidas', 'Refrigerantes', 'Soda', 'Bebidas gaseificadas');

-- INSERT INTO unidadeMedida(nome, sigla, descricao) VALUES
-- ('Kilograma', 'kg', 'Peso'),
-- ('Litro', 'L', 'Volume');


-- INSERT INTO imposto(nome, percentagem, descricao) VALUES
-- ('IVA', 17.000, 'Imposto padrÃƒÂ£o'),
-- ('Isento', 0.000, 'Sem imposto');

-- INSERT INTO produto(
-- nome, codigo_barra, tipoProduto, unidadePorTipo,
-- descricao, controle_stock, idCategoria, idUnidade, idImposto
-- ) VALUES
-- ('Arroz 50kg', 'P001', 'Simples', 1, 'Arroz branco', 1, 1, 1, 1),
-- ('Refrigerante Cola', 'P002', 'Simples', 1, 'Soda cola 2L', 1, 2, 2, 1);

-- -- Depois do insert de produtos, o trigger cria:
-- -- Ã¢Å“â€ stock para cada armazÃƒÂ©m
-- -- Ã¢Å“â€ saldo = 0
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
-- VALUES (DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ReposiÃƒÂ§ÃƒÂ£o loja');
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
-- -- movimento saÃƒÂ­da
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
-- -- Ã¢Å“â€ devem ser IGUAIS

-- SELECT * FROM movimento_lote;
-- -- Ã¢Å“â€ deve mostrar distribuiÃƒÂ§ÃƒÂ£o entre lotes

-- SELECT * FROM stock;
-- -- Ã¢Å“â€ reservado nunca pode ultrapassar saldo



-- ============================================================

-- ============================================================

 create table cliente(
	id integer primary key auto_increment,
    tipo varchar(50) not null, --  (Pessoa FÃƒÂ­sica/JurÃƒÂ­dica)
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
    estado boolean default true,
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
    data_adicao date default (current_date),
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_produto) references produto(id),
    foreign key (fk_numero) references factura(numero)
);

create table itemcotacao(
	id integer primary key auto_increment,
    quantidade decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    qtd_por_unidade integer DEFAULT 1,
    fk_id_produto integer not null,
    fk_numero varchar(20) not null,
    fk_id_usuario integer not null,
    fk_id_armazem integer not null,
    data_adicao date default (current_date),
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id),
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
    tipo_transacao VARCHAR(50) NOT NULL, -- Ex: Entrada, SaÃƒÂ­da
    descricao text,
    fk_id_contabancaria integer,
    fk_id_metodo_pagamento integer,
    fk_numero varchar(20),
    fk_parcela integer,
    fk_id_usuario integer not null,
    data_criacao  date default (current_date),
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

SHOW TABLES;
