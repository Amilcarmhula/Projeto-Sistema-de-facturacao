-- drop database _sysface;
-- create database _sysface;


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
VALUE
('Amilcar Mula', 'amilcar.mula@example.com', 'root', 'senha123', 1, 1, 15);  -- Permissão: Create, Read, Update
-- ('Maria Oliveira', 'maria.oliveira@example.com', 'mariao', 'senha123', 1, 2, 1),  -- Permissão: Create
-- ('Carlos Santos', 'carlos.santos@example.com', 'carloss', 'senha123', 1, 3, 1);  -- Permissão: Read, Update, Delete
 
 
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
    foreign key (fk_id_cliente) references cliente(id),
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
INSERT INTO fornecedor (tipo, razaosocial, nuit, fk_id_usuario) VALUE
('Pessoa Jurídica', 'Fornecedor Padrao.LDA', 100000001, (Select id from usuario order by id DESC LIMIT 1));
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
    foreign key (fk_id_fornecedor) references fornecedor(id),
    foreign key (fk_id_usuario) references usuario(id)
);
INSERT INTO endereco_fornecedor (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, fk_id_fornecedor,fk_id_usuario) 
SELECT provincia, cidade, bairro, avenida, rua, codigo_postal, numero,(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM endereco_empresa order by id DESC LIMIT 1;
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
INSERT INTO contacto_fornecedor (pessoa_de_contacto, contacto, email, website, fk_id_fornecedor,fk_id_usuario) 
SELECT 'Fornecedor Padrao',contacto, email,website,(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM contacto_empresa order by id DESC LIMIT 1;

create table armazem(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    tipo varchar(50) not null, -- de Armazém: (Central, Regional, Loja, etc.)
    descricao varchar(50),
    estado boolean default 1 not null, -- Ativo/Inativo.
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO armazem (nome, tipo, descricao, estado) VALUE
('Armazém Central', 'Central', 'Armazem Principalo', 1);

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
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id)
);
INSERT INTO endereco_armazem (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, fk_id_armazem,fk_id_usuario) 
SELECT provincia, cidade, bairro, avenida, rua, codigo_postal, numero,(Select id from armazem order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM endereco_empresa order by id DESC LIMIT 1;


create table contacto_armazem(
	id integer primary key auto_increment,
    pessoa_de_contacto varchar(100),
    contacto varchar(20) not null,
	email varchar(50),
    fk_id_armazem integer,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_armazem) references armazem(id),
    foreign key (fk_id_usuario) references usuario(id)
);
INSERT INTO contacto_armazem (pessoa_de_contacto, contacto, email, fk_id_armazem,fk_id_usuario) 
SELECT 'Gerente de Armazam',contacto, email,(Select id from armazem order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM contacto_empresa order by id DESC LIMIT 1;

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
INSERT INTO categoria (tipo, grupo, subgrupo, familia, descricao) VALUE
('Produtos', 'Padrao', 'SubPadrao', 'Categoria Padrao', 'Categoria padrao para qualquer produto');
create table unidade_medida(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    sigla varchar(10) not null,
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 
INSERT INTO unidade_medida (identificador, sigla, descricao)
VALUE 
('Unidade', 'Un', 'Unidade padrao');
create table taxa_imposto(
	id integer primary key auto_increment,
    identificador varchar(50) not null,
    percentagem decimal(4,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO taxa_imposto (identificador, percentagem, descricao) VALUE
('Isento', 0.00, 'Produtos isentos de imposto');
create table desconto(
	id integer primary key auto_increment,
    identificador varchar(100) not null,
    percentagem decimal(4,3),
    descricao text,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO desconto (identificador, percentagem, descricao) VALUE
('Isento', 0.0, 'Desconto aplicado em produtos sem desconto');


create table produto(
	id integer primary key auto_increment,
    nome varchar(50) not null,
    codigo_barra varchar(50),
    descricao text,
    contole_stock boolean not null, -- define condicao se sera armazenado no stock pelo trigger_registra_produto
    nivelstock integer default 0,
    fk_id_categoria integer not null,
    fk_id_unidade_medida integer,
    fk_id_taxa_imposto integer,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (fk_id_taxa_imposto) references taxa_imposto(id),
    foreign key (fk_id_categoria) references categoria(id),
    foreign key (fk_id_unidade_medida) references unidade_medida(id)
);
INSERT INTO produto (nome, codigo_barra, descricao, contole_stock,fk_id_categoria,fk_id_unidade_medida,fk_id_taxa_imposto) 
SELECT 'Produto Padrao','1000000000001', 'Produto Padrao',1,(Select id from categoria order by id DESC LIMIT 1),(Select id from unidade_medida order by id DESC LIMIT 1),(Select id from taxa_imposto order by id DESC LIMIT 1);


CREATE TABLE precovenda (
    id integer PRIMARY KEY AUTO_INCREMENT,
    preco DECIMAL(10, 2) default 0.01 NULL,
    data_inicio DATE,
    data_fim DATE,
    estado boolean default 1 not null,
    fk_id_produto integer not null,
    fk_id_usuario integer not null,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_id_produto) REFERENCES produto(id),
    foreign key (fk_id_usuario) references usuario(id)
);
select * from precovenda;

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
INSERT INTO produto_fornecido (quantidade, data_fornecida, custo, fk_id_armazem,fk_id_produto,fk_id_fornecedor,fk_id_usuario) 
SELECT 0,current_date(), 0.00, (Select id from armazem order by id DESC LIMIT 1),(Select id from produto order by id DESC LIMIT 1),(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1);




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
('FR','Fatura Recibo', 1, 'Fatura Recibo: Documento que combina a fatura e o recibo, emitido simultaneamente à venda e ao pagamento.'), -- tem pagamento imediato
('FS','Fatura Simplificada', 1, 'Fatura Simplificada: Utilizada para vendas de baixo valor ou operações simples, devendo ser emitida no momento da venda.'), -- tem pagamento imediato
('GT','Guia de Transporte', 1, 'Guia de Transporte: Documento que deve acompanhar o transporte de mercadorias, emitido antes da movimentação dos produtos.'),
('NC','Nota de Crédito', 30, 'Nota de Crédito: Documento emitido para corrigir ou anular uma fatura anterior, geralmente emitida até 30 dias após a transação original.'),
('ND','Nota de Débito', 30, 'Nota de Débito: Documento emitido para cobrar valores adicionais de um cliente, emitido até 30 dias após a identificação do valor a ser cobrado.');


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
insert into metodo_pagamento(nome, descricao) values 
('Dinheiro (Espécie)','O método mais tradicional de pagamento, usando moedas e cédulas físicas.'),
('Pagamento Terminal POS','Utilizado em estabelecimentos comerciais para processar pagamentos feitos com cartões de crédito, débito e, em alguns casos, carteiras móveis via NFC ou QR Code'),
('Transferências Bancárias','Movimentação direta entre contas da mesma ou diferentes instituição financeira.'),
('Carteiras Móveis','Plataformas que armazenam informações de pagamento e permitem a realização de transações por meio de dispositivos móveis.'),
('Pagamentos com Cheques','Um método de pagamento em que o emissor autoriza o banco a pagar um valor específico ao beneficiário');

create table parcelas(
	id integer primary key auto_increment,
    parcelaNumero integer not null,
    taxaJuros decimal(4,3) not null,
    taxaDeAtraso decimal(4,3) not null, -- 50% da taxa de juro normal
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

-- drop trigger _updateParcela;
delimiter //
create trigger _updateParcela
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

-- drop trigger _updateSaldoPendente_TotalPago_Factura;
-- criar logica para actualizar o saldo pendente da e o total pago da factura sempre que as parcelas sofrerem um update(usar trigger)
delimiter //
create trigger _updateSaldoPendente_TotalPago_Factura
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
        -- logica para actualizar o saldo pendente conforme as parcelas sao pagas
		
    end//
delimiter ;

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

-- drop trigger _updateCaixa_ContaCorrente;
delimiter //
create trigger _updateCaixa_ContaCorrente
	after insert on transacoes
    for each row
    begin
		DECLARE saldo_anterior DECIMAL(10, 2) DEFAULT 0;
			-- Verifica se o método de pagamento é igual a 1 (ajuste conforme necessário)
            IF NEW.fk_id_metodo_pagamento = 1 THEN
				-- Tenta buscar o saldo atual da última linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM caixa
					ORDER BY id DESC LIMIT 1;

				-- Se não encontrar saldo anterior, assume que o saldo inicial é 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO caixa (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial é o saldo atual anterior ou 0 se não houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);
			ELSE
				-- Tenta buscar o saldo atual da última linha da tabela caixa
				SELECT saldo_atual INTO saldo_anterior
					FROM contacorrente
					ORDER BY id DESC LIMIT 1;

				-- Se não encontrar saldo anterior, assume que o saldo inicial é 0
				IF saldo_anterior IS NULL THEN
					SET saldo_anterior = 0;
				END IF;

				-- Insere os valores na tabela caixa
				INSERT INTO contacorrente (saldo_inicial, saldo_atual, fk_id_transacoes)
					VALUES (
					saldo_anterior,                 -- Saldo inicial é o saldo atual anterior ou 0 se não houver
					saldo_anterior + NEW.valorPago,     -- Novo saldo é o saldo anterior somado ao valor da transação
					NEW.id                         -- FK da transação inserida
					);				
            END IF;
	end//
delimiter ;


-- Este trigger actualiza os dados da factura quando a condicao de pagamento e a vista
delimiter //
create trigger _updateFactura
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

