-- ============================================================
-- 03_DADOS_TESTE_VALIDACAO.sql
-- JM-InvSM / SysFace - SCRIPT AGRUPADO DE DADOS E VALIDACAO
-- Banco de teste: _sysface_teste
--
-- Modulos contidos neste ficheiro:
-- 1. MODULO BASE
--    Empresa, utilizador, fornecedor, armazem, categoria, unidade,
--    imposto e produto controlado por stock.
-- 2. MODULO INVENTARIO
--    Entrada por lote e validacao de stock/movimento de compra.
-- 3. MODULO REQUISICOES
--    Fluxo completo: Pendente -> Submetida -> Aprovada -> Finalizada,
--    com reserva, picking, FIFO, movimento_lote e stock reservado.
-- 4. MODULO PRECOS
--    Calculo de preco com IVA tratado como percentagem.
-- 5. MODULO FACTURACAO / PAGAMENTO
--    Factura, itemfactura, baixa por venda, pagamento a vista e caixa.
--
-- Execute depois de:
-- 00_TABELAS_COMPLETO.sql
-- 02_PROCEDURES_COMPLETO.sql
-- 01_TRIGGERS_COMPLETO.sql
-- ============================================================
USE _sysface_teste;
-- ============================================================
-- LIMPEZA E DADOS MINIMOS
-- Este bloco deixa o script reexecutavel na base de teste.
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE caixa;
TRUNCATE TABLE contacorrente;
TRUNCATE TABLE transacoes;
TRUNCATE TABLE parcelas;
TRUNCATE TABLE itemcotacao;
TRUNCATE TABLE itemfactura;
TRUNCATE TABLE factura;
TRUNCATE TABLE documentocomercial;
TRUNCATE TABLE produtodesconto;
TRUNCATE TABLE precoProdutoArmazem;
TRUNCATE TABLE desconto;
TRUNCATE TABLE contacto_cliente;
TRUNCATE TABLE endereco_cliente;
TRUNCATE TABLE cliente;
TRUNCATE TABLE movimento_lote;
TRUNCATE TABLE movimento;
TRUNCATE TABLE stock_transito;
TRUNCATE TABLE item_transferencia;
TRUNCATE TABLE transferencia_stock;
TRUNCATE TABLE reserva_stock;
TRUNCATE TABLE picking;
TRUNCATE TABLE itemRequisicao;
TRUNCATE TABLE requisicao;
TRUNCATE TABLE lote;
TRUNCATE TABLE stock;
TRUNCATE TABLE produto;
TRUNCATE TABLE imposto;
TRUNCATE TABLE unidadeMedida;
TRUNCATE TABLE categoria;
TRUNCATE TABLE contacto_armazem;
TRUNCATE TABLE endereco_armazem;
TRUNCATE TABLE armazem;
TRUNCATE TABLE contacto_fornecedor;
TRUNCATE TABLE endereco_fornecedor;
TRUNCATE TABLE fornecedor;
TRUNCATE TABLE usuario;
TRUNCATE TABLE permissao;
TRUNCATE TABLE nivel_acesso;
TRUNCATE TABLE contabancaria;
TRUNCATE TABLE contacto_empresa;
TRUNCATE TABLE endereco_empresa;
TRUNCATE TABLE empresa;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO empresa (nome, razaosocial, nuit) VALUES
('JM-Tecnologias', 'JM-Tecnologia Sociedade Unipessoal Limitada', 402150831);
INSERT INTO endereco_empresa (provincia, cidade, bairro, avenida, rua, codigo_postal, numero, tipo, fk_id_empresa) VALUES
('Maputo', 'Matola', 'Patrice Lumumba', 'Patrice Lumumba', '21376', 1112, 104, 'Facturação', 1);
INSERT INTO contacto_empresa (contacto, email, website, fk_id_empresa)
VALUES 
    ('+258847100793', 'comercial@jm-tecnologias.co.mz', 'jm-tecnologias.co.mz', 1);
INSERT INTO contabancaria (nome_banco, numero, nib, nuib, fk_id_empresa) VALUES
('Banco Comercial e de Investimentos', '13945108110001', '000800003945108110195', '123456789', 1);

INSERT INTO nivel_acesso (id, descricao)
VALUES (1, 'Administrador');

INSERT INTO permissao (id, tipo, descricao)
VALUES (1, 'CRUD', 'Todas permissoes');

INSERT INTO usuario (nome, email, usuario, senha, estado, fk_nivel_acesso, fk_permissao)
VALUES
('Amilcar Mula', 'amilcar.mula@example.com', 'root', 'senha123', 1, 1, 1);

INSERT INTO categoria (grupo, subgrupo, familia, descricao) VALUES
( 'Alimentares', 'Bebidas', 'Refrigerantes', 'Categoria de bebidas não alcoólicas'),
( 'Alimentares', 'Congelados', 'Carnes', 'Categoria de carnes congeladas'),
( 'Eletrônicos', 'Computadores', 'Laptops', 'Categoria de laptops e notebooks'),
( 'Manutenção', 'Equipamentos', 'Computadores', 'Serviços de manutenção de computadores'),
( 'Consultoria', 'TI', 'Infraestruturas', 'Consultoria em infraestrutura de TI');

INSERT INTO unidadeMedida (nome, sigla, descricao)
VALUES 
('Quilograma', 'kg', 'Unidade de massa usada no sistema métrico'),
('Litro', 'L', 'Unidade de volume usada para medir líquidos'),
('Lote', 'Lt', 'Volume por unidades'),
('Metro', 'm', 'Unidade de comprimento usada no sistema métrico'),
('Centímetro', 'cm', 'Unidade menor de comprimento no sistema métrico'),
('Mililitro', 'ml', 'Unidade de volume usada para pequenas quantidades de líquidos'),
('Grama', 'g', 'Unidade de massa usada para pequenas quantidades'),
('Tonelada', 't', 'Unidade de massa usada para grandes quantidades'),
('Polegada', 'in', 'Unidade de comprimento usada no sistema imperial'),
('Galão', 'gal', 'Unidade de volume usada no sistema imperial'),
('Onça', 'oz', 'Unidade de peso usada no sistema imperial');

INSERT INTO imposto (nome, percentagem, descricao) VALUES
('IVA', 16.0, 'Imposto sobre o valor acrescentado (IVA)'),
('ISPC', 5.0, 'Imposto Simplificado para Pequenos Contribuintes (ISPC)'),
('Taxa Reduzida', 7.5, 'Taxa de imposto reduzida para produtos essenciais');

INSERT INTO desconto (nome, percentagem, descricao) VALUES
('Desconto de Lançamento', 5.0, 'Desconto aplicado em novos produtos no lançamento'),
('Desconto de Fim de Ano', 37.0, 'Desconto especial para vendas de final de ano'),
('Desconto Black Friday',50.0, 'Desconto massivo durante a Black Friday');

insert into metodo_pagamento(nome, descricao) values 
('Dinheiro (Espécie)','O método mais tradicional de pagamento, usando moedas e cédulas físicas.'),
('Pagamento Terminal POS','Utilizado em estabelecimentos comerciais para processar pagamentos feitos com cartões de crédito, débito e, em alguns casos, carteiras móveis via NFC ou QR Code'),
('Transferências Bancárias','Movimentação direta entre contas da mesma ou diferentes instituição financeira.'),
('Carteiras Móveis','Plataformas que armazenam informações de pagamento e permitem a realização de transações por meio de dispositivos móveis.'),
('Pagamentos com Cheques','Um método de pagamento em que o emissor autoriza o banco a pagar um valor específico ao beneficiário');

INSERT INTO documentocomercial (sigla, nome, diasuteis, descricao) VALUES
('FA','Fatura', 5, 'Fatura: Documento comercial que representa a venda de produtos ou serviços, devendo ser emitida no ato da venda ou até 5 dias úteis após a transação.'),
('FP','Fatura Proforma', 30, 'Fatura Proforma: Documento preliminar usado para cotações e propostas comerciais. Não possui validade fiscal, com uma validade aproximada de 30 dias.'),
('FR','Fatura Recibo', 1, 'Fatura Recibo: Documento que combina a fatura e o recibo, emitido simultaneamente à venda e ao pagamento.'), -- tem pagamento imediato
('FS','Fatura Simplificada', 1, 'Fatura Simplificada: Utilizada para vendas de baixo valor ou operações simples, devendo ser emitida no momento da venda.'), -- tem pagamento imediato
('GT','Guia de Transporte', 1, 'Guia de Transporte: Documento que deve acompanhar o transporte de mercadorias, emitido antes da movimentação dos produtos.'),
('NC','Nota de Crédito', 30, 'Nota de Crédito: Documento emitido para corrigir ou anular uma fatura anterior, geralmente emitida até 30 dias após a transação original.'),
('ND','Nota de Débito', 30, 'Nota de Débito: Documento emitido para cobrar valores adicionais de um cliente, emitido até 30 dias após a identificação do valor a ser cobrado.');

-- INSERT INTO fornecedor (id, tipo, razaosocial, nuit)
-- VALUES (1, 'Juridica', 'Fornecedor Teste', 987654321);

-- INSERT INTO armazem (id, nome, tipo, descricao, estado)
-- VALUES (1, 'Armazem Central', 'Central', 'Teste', 1);

-- INSERT INTO produto (id, nome, codigo_barra, tipoProduto, unidadePorTipo, descricao, controle_stock, nivelstock, idCategoria, idUnidade, idImposto)
-- VALUES (1, 'Arroz', 'REQ001', 'Simples', 1, 'Produto para teste de requisicao', 1, 10, 1, 1, 1);

-- INSERT INTO cliente (tipo, nome, razaosocial, nuit, fk_id_usuario) 
-- VALUES ('Jurídica', 'Tech Solutions', 'Tech Soluções Lda', 1234567, 1);


-- INSERT INTO stock (idProduto, idArmazem, saldo, reservado)
-- VALUES (1, 1, 0, 0)
-- ON DUPLICATE KEY UPDATE saldo = 0, reservado = 0;

-- INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
-- VALUES ('REQ-LOTE-001', 1, 1, 1, CURDATE(), 100, 10.00);

-- SELECT
--     'SETUP - Stock apos entrada' AS teste,
--     s.idProduto,
--     s.idArmazem,
--     s.saldo,
--     s.reservado,
--     s.disponivel
-- FROM stock s
-- WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- -- 1. Criar requisicao pendente.
-- INSERT INTO requisicao (id, dataNecessidade, estado, motivo)
-- VALUES (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'Pendente', 'Teste completo de requisicao');

-- -- 2. Adicionar item deve mudar estado para Submetida.
-- INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
-- VALUES (30, 1, 1, 1);

-- SELECT
--     'TESTE 1 - Estado apos item' AS teste,
--     r.id,
--     r.estado
-- FROM requisicao r
-- WHERE r.id = 1;

-- -- 3. Aprovar deve reservar stock e criar picking.
-- CALL AprovarRequisicao(1);


-- SELECT
--     'TESTE 2 - Requisicao aprovada' AS teste,
--     r.id,
--     r.estado
-- FROM requisicao r
-- WHERE r.id = 1;


-- SELECT
--     'TESTE 2 - Reserva criada' AS teste,
--     rs.idProduto,
--     rs.idArmazem,
--     rs.quantidade,
--     rs.estado
-- FROM reserva_stock rs
-- WHERE rs.idRequisicao = 1;

-- SELECT
--     'TESTE 2 - Stock reservado' AS teste,
--     s.saldo,
--     s.reservado,
--     s.disponivel
-- FROM stock s
-- WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- SELECT
--     'TESTE 2 - Picking criado' AS teste,
--     p.tipo,
--     p.estado,
--     p.idRequisicao
-- FROM picking p
-- WHERE p.idRequisicao = 1;


-- -- 4. Confirmar picking deve consumir reserva, baixar stock e lotes.
-- CALL ConfirmarPicking(1);

-- SELECT
--     'TESTE 3 - Requisicao finalizada' AS teste,
--     r.id,
--     r.estado
-- FROM requisicao r
-- WHERE r.id = 1;

-- SELECT
--     'TESTE 3 - Reserva consumida' AS teste,
--     rs.quantidade,
--     rs.estado
-- FROM reserva_stock rs
-- WHERE rs.idRequisicao = 1;

-- SELECT
--     'TESTE 3 - Picking confirmado' AS teste,
--     p.estado,
--     p.dataFim
-- FROM picking p
-- WHERE p.idRequisicao = 1;

-- SELECT
--     'TESTE 3 - Movimento de saida' AS teste,
--     m.tipoOrigem,
--     m.tipoMovimento,
--     m.quantidade,
--     m.idProduto,
--     m.idArmazem
-- FROM movimento m
-- WHERE m.tipoOrigem = 'Requisicao'
-- ORDER BY m.id DESC
-- LIMIT 1;


-- SELECT
--     'TESTE 3 - Movimento/lote FIFO' AS teste,
--     ml.idMovimento,
--     ml.idLote,
--     ml.quantidadeRetirada
-- FROM movimento_lote ml
-- JOIN movimento m ON m.id = ml.idMovimento
-- WHERE m.tipoOrigem = 'Requisicao'
-- ORDER BY ml.id DESC
-- LIMIT 1;

-- SELECT
--     'TESTE 3 - Lote apos requisicao' AS teste,
--     l.codigo,
--     l.quantidadeAtual,
--     l.estado
-- FROM lote l
-- WHERE l.codigo = 'REQ-LOTE-001';

-- SELECT
--     'TESTE 3 - Stock final' AS teste,
--     s.saldo,
--     s.reservado,
--     s.disponivel
-- FROM stock s
-- WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- -- Resultado esperado:
-- -- Estado final: Finalizada
-- -- Reserva: Consumido
-- -- Picking: Confirmado
-- -- Stock: saldo 70, reservado 0, disponivel 70
-- -- Lote: quantidadeAtual 70
-- -- Movimento: Requisicao/Saida com quantidade 30

-- -- ============================================================
-- -- MODULO 4: PRECOS
-- -- Valida IVA percentual: preco base 100 com IVA 17 deve gerar 117.
-- -- ============================================================

-- CALL sp_registar_preco(1, 1, 100.00);

-- SELECT
--     'TESTE 4 - Preco com IVA' AS teste,
--     precoBase,
--     precoVenda,
--     precoFinal,
--     CASE WHEN ROUND(precoVenda, 2) = 117.00 THEN 'OK' ELSE 'ERRO' END AS resultado
-- FROM precoProdutoArmazem
-- WHERE idProduto = 1 AND idArmazem = 1
-- ORDER BY id DESC
-- LIMIT 1;

-- -- ============================================================
-- -- MODULO 5: FACTURACAO E PAGAMENTO
-- -- Vende 5 unidades depois da requisicao. Esperado:
-- -- saldo 65, reservado 0, disponivel 65, factura paga e caixa 585.
-- -- ============================================================

-- INSERT INTO cliente (id, tipo, nome, razaosocial, nuit, fk_id_usuario)
-- VALUES (1, 'Pessoa Fisica', 'Cliente Teste', 'Cliente Teste Lda', 111222333, 1);


-- INSERT INTO factura (numero, data_emissao, data_vencimento, total, fk_id_empresa, fk_id_cliente, fk_id_usuario, fk_id_doc, saldoPendente)
-- VALUES ('FA-TESTE-001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 585.00, 1, 1, 1, 1, 585.00);

-- INSERT INTO itemfactura (quantidade, subtotal, qtd_por_unidade, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem)
-- VALUES (5, 585.00, 1, 1, 'FA-TESTE-001', 1, 1);

-- INSERT INTO transacoes (data_pagamento, valorRecebido, valorPago, trocos, tipo_transacao, descricao, fk_id_contabancaria, fk_id_metodo_pagamento, fk_numero, fk_id_usuario)
-- VALUES (CURDATE(), 600.00, 585.00, 15.00, 'Entrada', 'Pagamento de factura', 1, 1, 'FA-TESTE-001', 1);

-- SELECT
--     'TESTE 5 - Movimento venda' AS teste,
--     tipoOrigem,
--     tipoMovimento,
--     quantidade,
--     idProduto,
--     idArmazem,
--     idLote
-- FROM movimento
-- WHERE tipoOrigem = 'Venda'
-- ORDER BY id DESC
-- LIMIT 1;

-- SELECT
--     'TESTE 5 - Movimento/lote venda' AS teste,
--     ml.idMovimento,
--     ml.idLote,
--     ml.quantidadeRetirada
-- FROM movimento_lote ml
-- JOIN movimento m ON m.id = ml.idMovimento
-- WHERE m.tipoOrigem = 'Venda'
-- ORDER BY ml.id DESC
-- LIMIT 1;

-- SELECT
--     'TESTE 5 - Stock final apos venda' AS teste,
--     saldo,
--     reservado,
--     disponivel
-- FROM stock
-- WHERE idProduto = 1 AND idArmazem = 1;

-- SELECT
--     'TESTE 5 - Lote final apos venda' AS teste,
--     codigo,
--     quantidadeAtual,
--     estado
-- FROM lote
-- WHERE codigo = 'REQ-LOTE-001';

-- SELECT
--     'TESTE 5 - Factura paga' AS teste,
--     numero,
--     total,
--     totalPago,
--     saldoPendente,
--     estado
-- FROM factura
-- WHERE numero = 'FA-TESTE-001';

-- SELECT
--     'TESTE 5 - Caixa' AS teste,
--     saldo_inicial,
--     saldo_atual
-- FROM caixa
-- ORDER BY id DESC
-- LIMIT 1;