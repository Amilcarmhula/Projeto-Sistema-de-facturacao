-- drop database _sysface;
create database _sysface;


use _sysface;
select * from usuario;
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
-- Inserindo três usuários na tabela `usuario`
INSERT INTO nivel_acesso (descricao)
VALUES
('Administrador'),
('Gerente de Finanças'),
('Operador de Estoque'),
('Operador de Vendas');
INSERT INTO permissao (tipo, descricao) VALUES
('0000', 'Nenhuma permissão'),
('0001', 'Delete'),
('0010', 'Update'),
('0011', 'Update, Delete'),
('0100', 'Read'),
('0101', 'Read, Delete'),
('0110', 'Read, Update'),
('0111', 'Read, Update, Delete'),
('1000', 'Create'),
('1001', 'Create, Delete'),
('1010', 'Create, Update'),
('1011', 'Create, Update, Delete'),
('1100', 'Create, Read'),
('1101', 'Create, Read, Delete'),
('1110', 'Create, Read, Update'),
('1111', 'Todas as permissões (CRUD completo)');
INSERT INTO usuario (nome, email, usuario, senha, estado, fk_nivel_acesso, fk_permissao)
VALUES
('Amilcar Mula', 'amilcar.mula@example.com', 'root', 'senha123', 1, 1, 15),  -- Permissão: Create, Read, Update
('Maria Oliveira', 'maria.oliveira@example.com', 'mariao', 'senha123', 1, 2, 1),  -- Permissão: Create
('Carlos Santos', 'carlos.santos@example.com', 'carloss', 'senha123', 1, 3, 1);  -- Permissão: Read, Update, Delete
 
 
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
	email varchar(100) unique,
    website varchar(100),
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
	email varchar(100) unique,
    website varchar(100) ,
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
create table desconto(
	id integer primary key auto_increment,
    identificador varchar(100) not null,
    percentagem decimal(4,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO desconto (identificador, percentagem, descricao) VALUES
('Isento', 0.0, 'Desconto aplicado em produtos sem desconto'),
('Desconto de Lançamento', 0.1, 'Desconto aplicado em novos produtos no lançamento'),
('Desconto de Fim de Ano', 0.155, 'Desconto especial para vendas de final de ano'),
('Desconto por Quantidade', 0.575, 'Desconto aplicado para compras em grandes quantidades'),
('Desconto Promocional', 0.2, 'Desconto aplicado em promoções especiais por tempo limitado'),
('Desconto Fidelidade', 0.8, 'Desconto para clientes fiéis com mais de 1 ano de compras'),
('Desconto Black Friday', 0.3, 'Desconto massivo durante a Black Friday'),
('Desconto de Parceria', 0.125, 'Desconto exclusivo para parceiros comerciais'),
('Desconto de Natal', 0.1825, 'Desconto sazonal para o período de Natal');


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
	-- CALL atualizar_estado_precovenda();

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

create table stock(
	fk_id_produto integer,
    fk_id_armazem integer,
    saldo integer not null,
    primary key (fk_id_produto,fk_id_armazem),
    foreign key(fk_id_produto) references produto(id),
    foreign key(fk_id_armazem) references armazem(id)
);

DELIMITER //

CREATE TRIGGER trigger_update_saldostock
    AFTER INSERT ON movimento_produto
    FOR EACH ROW
BEGIN
    -- Verifica se a combinação de fk_id_produto e fk_id_armazem já existe na tabela stock
    IF NOT EXISTS (
        SELECT 1 
        FROM stock 
        WHERE fk_id_produto = NEW.fk_id_produto 
          AND fk_id_armazem = NEW.fk_id_armazem
    ) THEN
        -- Insere um novo registro com saldo inicial 0 caso a combinação não exista
        INSERT INTO stock (fk_id_produto, fk_id_armazem, saldo)
        VALUES (NEW.fk_id_produto, NEW.fk_id_armazem, 0);
    END IF;

    -- Atualiza o saldo com base nos movimentos registrados
    UPDATE stock 
    SET saldo = (
        SELECT 
            (SUM(CASE WHEN tipo_movimento = 'Entrada' THEN quantidade ELSE 0 END) - 
             SUM(CASE WHEN tipo_movimento = 'Saída' THEN quantidade ELSE 0 END)) AS saldo_produto
        FROM movimento_produto 
        WHERE fk_id_produto = NEW.fk_id_produto 
          AND fk_id_armazem = NEW.fk_id_armazem
    )
    WHERE fk_id_produto = NEW.fk_id_produto 
      AND fk_id_armazem = NEW.fk_id_armazem;
END//

DELIMITER ;


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
    -- Informacao bases de uma empresa
INSERT INTO empresa (nome, razaosocial, nuit) VALUES
('JM-Tecnologias', 'JM-Tecnologias Sociedade Unipessoal, E.I', 122564800);
INSERT INTO endereco_empresa (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_empresa) VALUES
('Maputo', 'Matola', 'Patrice Lumumba', '24 de Julho', '21376', 1112, 211, 'Facturação', 1);
INSERT INTO contacto_empresa (contacto, email, website, fk_id_empresa)
VALUES 
    ('+258879201993', 'infol@jm-tecnologias.co.mz', 'www.jm-tecnologias.co.mz', 1);
INSERT INTO contabancaria (nome_banco, numero, nib, nuib, fk_id_empresa) VALUES
('Banco Comercial e de Investimentos', '13945108110001', '000800003945108110195', '123456789', 1);


-- Armazena as siglas das facturas e o numero de dias uteis
create table documentocomercial(
	id integer primary key auto_increment,
    sigla varchar(10) not null,
    nome varchar(30) not null,
    diasuteis integer not null,
    descricao text not null    
);
INSERT INTO documentocomercial (sigla, nome, diasuteis, descricao) VALUES
('FA','Fatura', 5, 'Fatura: Documento comercial que representa a venda de produtos ou serviços, devendo ser emitida no ato da venda ou até 5 dias úteis após a transação.'),
('FP','Fatura Proforma', 30, 'Fatura Proforma: Documento preliminar usado para cotações e propostas comerciais. Não possui validade fiscal, com uma validade aproximada de 30 dias.'),
('FR','Fatura Recibo', 1, 'Fatura Recibo: Documento que combina a fatura e o recibo, emitido simultaneamente à venda e ao pagamento.'),
('FS','Fatura Simplificada', 1, 'Fatura Simplificada: Utilizada para vendas de baixo valor ou operações simples, devendo ser emitida no momento da venda.'),
('GT','Guia de Transporte', 1, 'Guia de Transporte: Documento que deve acompanhar o transporte de mercadorias, emitido antes da movimentação dos produtos.'),
('NC','Nota de Crédito', 30, 'Nota de Crédito: Documento emitido para corrigir ou anular uma fatura anterior, geralmente emitida até 30 dias após a transação original.'),
('ND','Nota de Débito', 30, 'Nota de Débito: Documento emitido para cobrar valores adicionais de um cliente, emitido até 30 dias após a identificação do valor a ser cobrado.');



-- Tabelas para armazenar dados das facturacoes
CREATE TABLE factura (
    numero VARCHAR(20) PRIMARY KEY,
    data_criacao DATE,
    data_vencimento DATE NOT NULL,
    estado BOOLEAN DEFAULT 0 NOT NULL,
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
-- Trigger para tabela movimento_produto manter dados de movimento de ENTRADA, SAIDA ou AJUSTE de estoque
delimiter //
create trigger trigger_registra_saida
	after insert on itemfactura
    for each row
    begin
		insert into movimento_produto (tipo_movimento,data_movimento,quantidade,fk_id_produto,fk_id_usuario,fk_id_armazem)
        value ('Saída',new.data_adicao,new.quantidade,new.fk_id_produto,new.fk_id_usuario,new.fk_id_armazem);
    end//
delimiter ;

























create table metodo_pagamento(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- transacoes 
create table transacoes(
	id integer primary key auto_increment,
    data_pagamento date,
    valor decimal(10,2) not null,
    tipo_transacao VARCHAR(50) NOT NULL, -- Ex: Entrada, Saída
    tipo_pagamento varchar(20) not null, -- caixa, conta corrente
    descricao text,
    fk_id_contabancaria integer,
    fk_id_metodo_pagamento integer not null,
    fk_numero varchar(20),
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_contabancaria) references contabancaria(id),
    foreign key (fk_id_usuario) references usuario(id),
    foreign key (fk_id_metodo_pagamento) references metodo_pagamento(id),
    foreign key (fk_numero) references factura(numero)
);

					-- As tabelas abaixo serao populadas atravez de trigger's

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

