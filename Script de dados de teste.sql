use _sysface;

    -- Informacao bases de uma empresa
INSERT INTO empresa (nome, razaosocial, nuit) VALUES
('JM-Tecnologias', 'JM-Tecnologia, E.I', 122564800);
INSERT INTO endereco_empresa (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_empresa) VALUES
('Maputo', 'Matola', 'Patrice Lumumba', '24 de Julho', '21376', 1112, 211, 'Facturação', 1);
INSERT INTO contacto_empresa (contacto, email, website, fk_id_empresa)
VALUES 
    ('+258879201993', 'infol@jm-tecnologias.co.mz', 'www.jm-tecnologias.co.mz', 1);
INSERT INTO contabancaria (nome_banco, numero, nib, nuib, fk_id_empresa) VALUES
('Banco Comercial e de Investimentos', '13945108110001', '000800003945108110195', '123456789', 1);

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
('Amilcar Mula', 'amilcar.mula@example.com', 'root', 'senha123', 1, 1, 15);

INSERT INTO fornecedor (tipo, razaosocial, nuit, fk_id_usuario) VALUE
('Pessoa Jurídica', 'Fornecedor Padrao.LDA', 100000001, (Select id from usuario order by id DESC LIMIT 1));

INSERT INTO endereco_fornecedor (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, fk_id_fornecedor,fk_id_usuario) 
SELECT provincia, cidade, bairro, avenida, rua, codigo_postal, numero,(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM endereco_empresa order by id DESC LIMIT 1;

INSERT INTO contacto_fornecedor (pessoa_de_contacto, contacto, email, website, fk_id_fornecedor,fk_id_usuario) 
SELECT 'Fornecedor Padrao',contacto, email,website,(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM contacto_empresa order by id DESC LIMIT 1;

INSERT INTO armazem (nome, tipo, descricao, estado) VALUE
('Armazem PL', 'Central', 'Armazem Principal', 1);

INSERT INTO endereco_armazem (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, fk_id_armazem,fk_id_usuario) 
SELECT provincia, cidade, bairro, avenida, rua, codigo_postal, numero,(Select id from armazem order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM endereco_empresa order by id DESC LIMIT 1;

INSERT INTO contacto_armazem (pessoa_de_contacto, contacto, email, fk_id_armazem,fk_id_usuario) 
SELECT 'Gerente de Armazam',contacto, email,(Select id from armazem order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1) FROM contacto_empresa order by id DESC LIMIT 1;

INSERT INTO categoria (tipo, grupo, subgrupo, familia, descricao) VALUE
('Produtos', 'Padrao', 'SubPadrao', 'Categoria Padrao', 'Categoria padrao para qualquer produto');

INSERT INTO unidade_medida (nome, sigla, descricao)
VALUE 
('Unidade', 'Un', 'Unidade padrao');

INSERT INTO imposto (nome, percentagem, descricao) VALUE
('Isento', 0.00, 'Produtos isentos de imposto');

INSERT INTO desconto (nome, descricao) VALUE
('Isento', 'Desconto aplicado em produtos sem desconto');

INSERT INTO produto (nome, codigo_barra, descricao, controle_stock,fk_id_categoria,fk_id_unidade_medida,fk_id_imposto) 
SELECT 'Produto Arm Z','1000000000001', 'Produto Padrao',1,(Select id from categoria order by id DESC LIMIT 1),(Select id from unidade_medida order by id DESC LIMIT 1),(Select id from imposto order by id DESC LIMIT 1);

INSERT INTO produto_fornecido (quantidade, data_fornecida,unidade_fornecida, qtd_por_unidade, custo, fk_id_armazem,fk_id_produto,fk_id_fornecedor,fk_id_usuario) 
SELECT 0,current_date(),'unidade',0, 0.00, (Select id from armazem order by id DESC LIMIT 1),(Select id from produto order by id DESC LIMIT 1),(Select id from fornecedor order by id DESC LIMIT 1),(Select id from usuario order by id DESC LIMIT 1);

INSERT INTO documentocomercial (sigla, nome, diasuteis, descricao) VALUES
('FA','Fatura', 5, 'Fatura: Documento comercial que representa a venda de produtos ou serviços, devendo ser emitida no ato da venda ou até 5 dias úteis após a transação.'),
('FP','Fatura Proforma', 30, 'Fatura Proforma: Documento preliminar usado para cotações e propostas comerciais. Não possui validade fiscal, com uma validade aproximada de 30 dias.'),
('FR','Fatura Recibo', 1, 'Fatura Recibo: Documento que combina a fatura e o recibo, emitido simultaneamente à venda e ao pagamento.'), -- tem pagamento imediato
('FS','Fatura Simplificada', 1, 'Fatura Simplificada: Utilizada para vendas de baixo valor ou operações simples, devendo ser emitida no momento da venda.'), -- tem pagamento imediato
('GT','Guia de Transporte', 1, 'Guia de Transporte: Documento que deve acompanhar o transporte de mercadorias, emitido antes da movimentação dos produtos.'),
('NC','Nota de Crédito', 30, 'Nota de Crédito: Documento emitido para corrigir ou anular uma fatura anterior, geralmente emitida até 30 dias após a transação original.'),
('ND','Nota de Débito', 30, 'Nota de Débito: Documento emitido para cobrar valores adicionais de um cliente, emitido até 30 dias após a identificação do valor a ser cobrado.');


insert into metodo_pagamento(nome, descricao) values 
('Dinheiro (Espécie)','O método mais tradicional de pagamento, usando moedas e cédulas físicas.'),
('Pagamento Terminal POS','Utilizado em estabelecimentos comerciais para processar pagamentos feitos com cartões de crédito, débito e, em alguns casos, carteiras móveis via NFC ou QR Code'),
('Transferências Bancárias','Movimentação direta entre contas da mesma ou diferentes instituição financeira.'),
('Carteiras Móveis','Plataformas que armazenam informações de pagamento e permitem a realização de transações por meio de dispositivos móveis.'),
('Pagamentos com Cheques','Um método de pagamento em que o emissor autoriza o banco a pagar um valor específico ao beneficiário');



-- 									Dados de teste secundarios
-- INSERT INTO cliente (tipo, nome, razaosocial, nuit, fk_id_usuario)
-- VALUES
-- ('Pessoa Jurídica', 'Tech Solutions', 'Tech Soluções Ltda', 987654321, 1),
-- ('Pessoa Jurídica', 'Construtora Ideal', 'Ideal Construtora Ltda', 147258369, 1),
-- ('Pessoa Física', 'João Silva', 'Marcos', 852963741, 1),
-- ('Pessoa Jurídica', 'Transporte Rápido', 'TransRápido Ltda', 963852741, 1),
-- ('Pessoa Física', 'Roberto Souza', 'Beto', 369258147, 1);

-- -- Dados de endereços
-- INSERT INTO endereco_cliente (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_cliente, fk_id_usuario)
-- VALUES
-- ('Maputo', 'Maputo', 'Alto Maé', '25 de Setembro', ' 1', 1100, 123, 'Facturação', 1, 1),
-- ('Maputo', 'Maputo', 'Polana', 'Julius Nyerere', ' 2', 1200, 456, 'Facturação', 2, 1),
-- ('Inhambane', 'Inhambane', 'Chilwane', 'Patrice Lumumba', ' 6', 5500, 303, 'Facturação', 3, 1),
-- ('Gaza', 'Xai-Xai', 'Chongoene', 'dos Heróis', ' 7', 6600, 404, 'Facturação', 4, 1),
-- ('Cabo Delgado', 'Pemba', 'Paquitequete', '25 de Junho', ' 8', 7700, 505, 'Facturação', 5, 1);

-- -- Inserir dados fictícios na tabela contacto_cliente
-- INSERT INTO contacto_cliente (pessoa_de_contacto, contacto, email, website, fk_id_cliente, fk_id_usuario)
-- VALUES
-- ('Carlos Mendes', '841234567', 'carlos.mendes@exemplo.com', 'www.carlosmendes.com', 1, 1),
-- ('Ana Joaquim', '842345678', 'ana.joaquim@exemplo.com', 'www.anajoaquim.com', 2, 1),
-- ('Pedro Langa', '843456789', 'pedro.langa@exemplo.com', 'www.pedrolanga.com', 3, 1),
-- ('Maria Xavier', '844567890', 'maria.xavier@exemplo.com', 'www.mariaxavier.com', 4, 1),
-- ('Joaquim Sambo', '845678901', 'joaquim.sambo@exemplo.com', 'www.joaquimsambo.com', 5, 1);

-- INSERT INTO categoria (tipo, grupo, subgrupo, familia, descricao) VALUES
-- ('Produtos', 'Alimentares', 'Bebidas', 'Refrigerantes', 'Categoria de bebidas não alcoólicas'),
-- ('Produtos', 'Alimentares', 'Congelados', 'Carnes', 'Categoria de carnes congeladas'),
-- ('Produtos', 'Eletrônicos', 'Computadores', 'Laptops', 'Categoria de laptops e notebooks'),
-- ('Serviços', 'Manutenção', 'Equipamentos', 'Computadores', 'Serviços de manutenção de computadores'),
-- ('Serviços', 'Consultoria', 'TI', 'Infraestruturas', 'Consultoria em infraestrutura de TI');


-- INSERT INTO unidade_medida (nome, sigla, descricao)
-- VALUES 
-- ('Quilograma', 'kg', 'Unidade de massa usada no sistema métrico'),
-- ('Litro', 'L', 'Unidade de volume usada para medir líquidos'),
-- ('Lote', 'Lt', 'Volume por unidades'),
-- ('Metro', 'm', 'Unidade de comprimento usada no sistema métrico'),
-- ('Centímetro', 'cm', 'Unidade menor de comprimento no sistema métrico'),
-- ('Mililitro', 'ml', 'Unidade de volume usada para pequenas quantidades de líquidos'),
-- ('Grama', 'g', 'Unidade de massa usada para pequenas quantidades'),
-- ('Tonelada', 't', 'Unidade de massa usada para grandes quantidades'),
-- ('Polegada', 'in', 'Unidade de comprimento usada no sistema imperial'),
-- ('Galão', 'gal', 'Unidade de volume usada no sistema imperial'),
-- ('Onça', 'oz', 'Unidade de peso usada no sistema imperial');


-- INSERT INTO imposto (nome, percentagem, descricao) VALUES
-- ('IVA', 16.0, 'Imposto sobre o valor acrescentado (IVA)'),
-- ('ISPC', 5.0, 'Imposto Simplificado para Pequenos Contribuintes (ISPC)'),
-- ('Taxa Reduzida', 7.5, 'Taxa de imposto reduzida para produtos essenciais');

-- INSERT INTO desconto (nome, descricao) VALUES
-- ('Desconto de Lançamento', 'Desconto aplicado em novos produtos no lançamento');
-- ('Desconto de Fim de Ano', 'Desconto especial para vendas de final de ano'),
-- ('Desconto por Quantidade', 'Desconto aplicado para compras em grandes quantidades'),
-- ('Desconto Promocional', 'Desconto aplicado em promoções especiais por tempo limitado'),
-- ('Desconto Fidelidade', 'Desconto para clientes fiéis com mais de 1 ano de compras'),
-- ('Desconto Black Friday', 'Desconto massivo durante a Black Friday'),
-- ('Desconto de Parceria', 'Desconto exclusivo para parceiros comerciais'),
-- ('Desconto de Natal', 'Desconto sazonal para o período de Natal');


--     




SELECT p.id AS idProduto,  p.nome AS nomeProduto,p.codigo_barra,
                      p.descricao, p.controle_stock, p.nivelstock, p.tipo, p.unidadePorTipo, a.id AS idArmazem,
                     a.nome AS nomeArmazem, a.tipo as tipoArmazem, c.id AS idCategoria, c.familia,
                     u.id AS idUnidade, u.nome as unidadeNome, u.sigla AS siglaUnidade, i.id AS idImposto, 
                        i.nome AS nomeImposto, pv.precoNormal, pv.precoVenda, 
                        pv.precoFinal, pv.estado as estadoPreco, s.saldo 
                    FROM stock s 
                    JOIN produto p ON s.fk_id_produto = p.id 
                    JOIN armazem a ON s.fk_id_armazem = a.id 
                    JOIN categoria c ON p.fk_id_categoria = c.id 
                    JOIN unidade_medida u ON p.fk_id_unidade_medida = u.id 
                    JOIN imposto i ON p.fk_id_imposto = i.id 
                    LEFT JOIN precovenda pv ON pv.id = (SELECT pv2.id 
                           FROM precovenda pv2 
                            WHERE pv2.idProduto = s.fk_id_produto 
                              AND pv2.idArmazem = s.fk_id_armazem 
                            ORDER BY pv2.data_criacao DESC 
                            LIMIT 1);


CREATE TABLE produto_composicao (
    idComposto INT NOT NULL,
    idSimples INT NOT NULL,
    quantidadePorUnidade INT NOT NULL,
    PRIMARY KEY (idComposto, idSimples),
    FOREIGN KEY (idComposto) REFERENCES produto(id) ON DELETE CASCADE,
    FOREIGN KEY (idSimples) REFERENCES produto(id) ON DELETE CASCADE
);

DELIMITER //
CREATE TRIGGER Tg_C_Saida
AFTER INSERT ON itemfactura
FOR EACH ROW
BEGIN
    -- Declarações devem vir primeiro
    DECLARE total_unidades INT;
    DECLARE estoque_atual INT;
    DECLARE tipoProduto VARCHAR(20);

    DECLARE done INT DEFAULT 0;
    DECLARE comp_id INT;
    DECLARE comp_qtd INT;

    DECLARE cur CURSOR FOR
        SELECT idSimples, quantidadePorUnidade
        FROM produto_composicao
        WHERE idComposto = NEW.fk_id_produto;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    -- Descobre se o produto é simples ou composto
    SELECT tipo INTO tipoProduto
    FROM produto
    WHERE id = NEW.fk_id_produto;

    IF tipoProduto = 'Simples' THEN
        -- Caso simples: calcula total direto
        SET total_unidades = NEW.quantidade * NEW.qtd_por_unidade;

        -- Verifica saldo
        SELECT saldo INTO estoque_atual
        FROM stock
        WHERE fk_id_produto = NEW.fk_id_produto
          AND fk_id_armazem = NEW.fk_id_armazem;

        IF estoque_atual >= total_unidades THEN
            INSERT INTO movimento (
                tipo_movimento, data_movimento, quantidade,
                tipoUnidadeMov, uniPorTipoMove, fk_id_produto,
                fk_id_usuario, fk_id_armazem
            ) VALUES (
                'Saída', NEW.data_adicao, NEW.quantidade,
                NEW.unidade_vendida, NEW.qtd_por_unidade,
                NEW.fk_id_produto, NEW.fk_id_usuario, NEW.fk_id_armazem
            );
        END IF;

    ELSEIF tipoProduto = 'Composto' THEN
        -- Caso composto: usar cursor para os itens do composto
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO comp_id, comp_qtd;
            IF done THEN
                LEAVE read_loop;
            END IF;

            SET total_unidades = NEW.quantidade * comp_qtd;

            -- Verifica saldo do produto simples
            SELECT saldo INTO estoque_atual
            FROM stock
            WHERE fk_id_produto = comp_id
              AND fk_id_armazem = NEW.fk_id_armazem;

            IF estoque_atual >= total_unidades THEN
                INSERT INTO movimento (
                    tipo_movimento, data_movimento, quantidade,
                    tipoUnidadeMov, uniPorTipoMove, fk_id_produto,
                    fk_id_usuario, fk_id_armazem
                ) VALUES (
                    'Saída', NEW.data_adicao, NEW.quantidade,
                    NEW.unidade_vendida, comp_qtd,
                    comp_id, NEW.fk_id_usuario, NEW.fk_id_armazem
                );
            END IF;
        END LOOP;
        CLOSE cur;
    END IF;
END//
DELIMITER ;






-- Produto simples
INSERT INTO produto (nome, codigo_barra, tipo, unidadePorTipo, descricao, controle_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_imposto)
VALUES ('Garrafa Água 500ml', '123456789', 'Simples', 1, 'Água mineral', TRUE, 1, 1, 1);

-- Produto composto
INSERT INTO produto (nome, codigo_barra, tipo, unidadePorTipo, descricao, controle_stock, fk_id_categoria, fk_id_unidade_medida, fk_id_imposto)
VALUES ('Caixa 12 Garrafas Água 500ml', '987654321', 'Composto', 12, 'Caixa com 12 unidades de 500ml', TRUE, 1, 1, 1);


INSERT INTO produto_composicao (idComposto, idSimples, quantidadePorUnidade)
VALUES (2, 1, 12);

INSERT INTO produto_fornecido (quantidade, unidade_fornecida, qtd_por_unidade, data_fornecida, custo, fk_id_armazem, fk_id_produto, fk_id_fornecedor, fk_id_usuario)
VALUES (2, 'Composto', 12, '2025-10-04', 50.00, 1, 2, 1, 1);

INSERT INTO factura (
    numero, data_emissao, data_vencimento, total, totalPago, saldoPendente, estado,
    condicaoPagemento, numeroParcelas, fk_id_empresa, fk_id_cliente, fk_id_usuario, fk_id_doc
) VALUES (
    'FAT2025001', 
    CURDATE(),               -- data_emissao
    DATE_ADD(CURDATE(), INTERVAL 30 DAY), -- data_vencimento (ex: 30 dias)
    120.00,                  -- total da venda
    0.00,                    -- ainda não pago
    120.00,                  -- saldo pendente = total - totalPago
    'Pendente',
    'A vista',               -- ou 'Parcelado' se houver parcelas
    0,                       -- numeroParcelas (0 para pagamento à vista)
    1,                       -- fk_id_empresa (empresa emissora)
    1,                       -- fk_id_cliente (cliente comprador)
    1,                       -- fk_id_usuario (usuário vendedor)
    1                        -- fk_id_doc (tipo de documento, ex: fatura, nota, etc.)
);


INSERT INTO itemfactura (quantidade, subtotal, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem, unidade_vendida, qtd_por_unidade)
VALUES (1, 120.00, 2, 'FAT2025001', 1, 1, 'Composto', 12);


select * from precovenda;
select *  from precoprodutoarmazem;
select * from produtodesconto;
select * from produto_fornecido;
select * from stock;
select * from factura;
select * from itemfactura;
select * from movimento;

INSERT INTO produto_fornecido (
    quantidade, unidade_fornecida, qtd_por_unidade, data_fornecida, custo, 
    fk_id_armazem, fk_id_produto, fk_id_fornecedor, fk_id_usuario
) VALUES 
(120, 'Simples', 1, '2025-10-04', 5.00, 1, 1, 1, 1);

INSERT INTO itemfactura (
    quantidade, subtotal, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem, unidade_vendida, qtd_por_unidade
) VALUES (
    2,        -- quantidade de garrafas
    20.00,    -- subtotal (ex: 10 MT por garrafa x 2)
    1,        -- produto simples
    'FAT2025001',
    1,        -- usuário
    1,        -- armazém
    'Simples',
    1         -- cada unidade = 1 garrafa
);





