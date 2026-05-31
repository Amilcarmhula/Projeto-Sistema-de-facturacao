use _sysface;
-- __________________________________________________ Tabelas para suportar funcionalidades de requisicoes __________________________________________________

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
    'Devolucao'
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
        idItemRequisicao IS NOT NULL OR tipoOrigem = 'Transferencia'
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



-- INSERT INTO fornecedor(tipo, razaosocial, nuit) VALUES
-- ('Jurídica', 'AgroComercial Lda', 123456789),
-- ('Jurídica', 'Distribuidora Zambezia', 987654321);

-- INSERT INTO endereco_fornecedor(provincia, cidade, bairro, avenida, rua, numero, idFornecedor) VALUES
-- ('Zambézia', 'Quelimane', 'Centro', 'Av. 25 de Setembro', 'Rua A', 10, 1),
-- ('Zambézia', 'Nicoadala', 'Central', 'Av. Principal', 'Rua B', 20, 2);

-- INSERT INTO contacto_fornecedor(pessoa_de_contacto, contacto, email, website, idFornecedor) VALUES
-- ('João Paulo', '841111111', 'agro@lda.co.mz', 'www.agrolda.co.mz', 1),
-- ('Maria João', '842222222', 'dist@zambezia.co.mz', 'www.distz.co.mz', 2);
--                      

-- INSERT INTO armazem(nome, tipo, descricao) VALUES
-- ('Armazém Central', 'Central', 'Principal'),
-- ('Loja Quelimane', 'Loja', 'Venda ao público');

-- INSERT INTO categoria(grupo, subgrupo, familia, descricao) VALUES
-- ('Alimentos', 'Grãos', 'Arroz', 'Produtos alimentares'),
-- ('Bebidas', 'Refrigerantes', 'Soda', 'Bebidas gaseificadas');

-- INSERT INTO unidadeMedida(nome, sigla, descricao) VALUES
-- ('Kilograma', 'kg', 'Peso'),
-- ('Litro', 'L', 'Volume');


-- INSERT INTO imposto(nome, percentagem, descricao) VALUES
-- ('IVA', 17.000, 'Imposto padrão'),
-- ('Isento', 0.000, 'Sem imposto');

-- INSERT INTO produto(
-- nome, codigo_barra, tipoProduto, unidadePorTipo,
-- descricao, controle_stock, idCategoria, idUnidade, idImposto
-- ) VALUES
-- ('Arroz 50kg', 'P001', 'Simples', 1, 'Arroz branco', 1, 1, 1, 1),
-- ('Refrigerante Cola', 'P002', 'Simples', 1, 'Soda cola 2L', 1, 2, 2, 1);

-- -- Depois do insert de produtos, o trigger cria:
-- -- ✔ stock para cada armazém
-- -- ✔ saldo = 0
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
-- VALUES (DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Reposição loja');
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
-- -- movimento saída
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
-- -- ✔ devem ser IGUAIS

-- SELECT * FROM movimento_lote;
-- -- ✔ deve mostrar distribuição entre lotes

-- SELECT * FROM stock;
-- -- ✔ reservado nunca pode ultrapassar saldo

