USE _sysface_teste;
-- ============================================================
-- VALIDACAO PRE-IMPLEMENTACAO
-- Execute este ficheiro numa base de teste.
-- Objectivo: demonstrar os pontos que precisam de correcao
-- antes de aplicar mudancas definitivas nos scripts principais.
-- ============================================================

-- Limpeza apenas dos dados usados neste teste.
-- Se houver dados reais nesta base, nao execute este bloco.

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

-- ============================================================
-- Dados minimos
-- ============================================================

INSERT INTO empresa (id, nome, razaosocial, nuit)
VALUES (1, 'Empresa Teste', 'Empresa Teste Lda', 123456789);

INSERT INTO nivel_acesso (id, descricao)
VALUES (1, 'Administrador');

INSERT INTO permissao (id, tipo, descricao)
VALUES (1, 'CRUD', 'Todas permissoes');

INSERT INTO usuario (id, nome, email, usuario, senha, estado, fk_nivel_acesso, fk_permissao)
VALUES (1, 'Utilizador Teste', 'teste@local.test', 'teste', '123', 1, 1, 1);

INSERT INTO fornecedor (id, tipo, razaosocial, nuit)
VALUES (1, 'Juridica', 'Fornecedor Teste', 987654321);

INSERT INTO armazem (id, nome, tipo, descricao, estado)
VALUES (1, 'Armazem Central', 'Central', 'Teste', 1);

INSERT INTO categoria (id, grupo, subgrupo, familia, descricao)
VALUES (1, 'Geral', 'Geral', 'Geral', 'Categoria de teste');

INSERT INTO unidadeMedida (id, nome, sigla, descricao)
VALUES (1, 'Unidade', 'UN', 'Unidade simples');

INSERT INTO imposto (id, nome, percentagem, descricao)
VALUES (1, 'IVA', 16.000, 'IVA de teste');

INSERT INTO produto (id, nome, codigo_barra, tipoProduto, unidadePorTipo, descricao, controle_stock, nivelstock, idCategoria, idUnidade, idImposto)
VALUES (1, 'Produto Teste', 'P001', 'Simples', 1, 'Produto de teste', 1, 0, 1, 1, 1);

-- ============================================================
-- TESTE 1: entrada por lote deve criar movimento Compra/Entrada
-- Antes da correcao, este INSERT tende a falhar porque
-- Tg_registaMovimentoEntrada nao informa movimento.tipoOrigem.
-- Depois da correcao, deve inserir lote e movimento.
-- ============================================================

INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES ('L001', 1, 1, 1, CURDATE(), 100, 10.00);

SELECT
    'TESTE 1 - Movimento de entrada por lote' AS teste,
    m.tipoOrigem,
    m.tipoMovimento,
    m.quantidade,
    m.idProduto,
    m.idArmazem,
    m.idLote
FROM movimento m
ORDER BY m.id DESC
LIMIT 1;

SELECT
    'TESTE 1 - Stock apos entrada' AS teste,
    s.idProduto,
    s.idArmazem,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- ============================================================
-- TESTE 2: requisicao deve usar estado valido
-- Antes da correcao, o INSERT em itemRequisicao tende a falhar
-- porque Tg_estadoRequisicao tenta gravar 'Em processamento',
-- que nao existe no enum da tabela requisicao.
-- Depois da correcao, o estado deve virar 'Submetida'
-- ou 'AguardandoAprovacao', conforme decisao adoptada.
-- ============================================================

INSERT INTO requisicao (id, dataNecessidade, estado, motivo)
VALUES (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'Pendente', 'Teste de requisicao');

INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES (10, 1, 1, 1);

SELECT
    'TESTE 2 - Estado da requisicao' AS teste,
    r.id,
    r.estado
FROM requisicao r
WHERE r.id = 1;

-- ============================================================
-- TESTE 3: calculo de preco com IVA
-- Antes da correcao, precoBase 100 e imposto 17.000 podem gerar
-- precoVenda 1800.00. Depois da correcao, deve gerar 117.00.
-- ============================================================

CALL sp_registar_preco(1, 1, 100.00);

SELECT
    'TESTE 3 - Preco com IVA' AS teste,
    precoBase,
    precoVenda,
    precoFinal,
    CASE
        WHEN ROUND(precoVenda, 2) = 117.00 THEN 'OK'
        ELSE 'ERRO: esperado precoVenda 117.00'
    END AS resultado
FROM precoProdutoArmazem
WHERE idProduto = 1 AND idArmazem = 1
ORDER BY id DESC
LIMIT 1;

-- ============================================================
-- TESTE 4: venda deve criar movimento Venda/Saida
-- Antes da correcao, Tg_C_Saida tende a falhar porque nao informa
-- movimento.tipoOrigem nos INSERTs em movimento.
-- Depois da correcao, deve abater lote via FIFO e registar movimento.
-- ============================================================

INSERT INTO cliente (id, tipo, nome, razaosocial, nuit, fk_id_usuario)
VALUES (1, 'Pessoa Fisica', 'Cliente Teste', 'Cliente LDA', 111222333, 1);

INSERT INTO documentocomercial (sigla, nome, diasuteis, descricao) VALUES
('FA','Fatura', 5, 'Fatura: Documento comercial que representa a venda de produtos ou serviços, devendo ser emitida no ato da venda ou até 5 dias úteis após a transação.'),
('FP','Fatura Proforma', 30, 'Fatura Proforma: Documento preliminar usado para cotações e propostas comerciais. Não possui validade fiscal, com uma validade aproximada de 30 dias.'),
('FR','Fatura Recibo', 1, 'Fatura Recibo: Documento que combina a fatura e o recibo, emitido simultaneamente à venda e ao pagamento.'), -- tem pagamento imediato
('FS','Fatura Simplificada', 1, 'Fatura Simplificada: Utilizada para vendas de baixo valor ou operações simples, devendo ser emitida no momento da venda.'), -- tem pagamento imediato
('GT','Guia de Transporte', 1, 'Guia de Transporte: Documento que deve acompanhar o transporte de mercadorias, emitido antes da movimentação dos produtos.'),
('NC','Nota de Crédito', 30, 'Nota de Crédito: Documento emitido para corrigir ou anular uma fatura anterior, geralmente emitida até 30 dias após a transação original.'),
('ND','Nota de Débito', 30, 'Nota de Débito: Documento emitido para cobrar valores adicionais de um cliente, emitido até 30 dias após a identificação do valor a ser cobrado.');


INSERT INTO desconto (nome, descricao, percentagem) VALUES
('BENVINDO10', 'Desconto de boas-vindas para a primeira compra no site.', 10.00);

INSERT INTO factura (numero, data_emissao, data_vencimento, total, fk_id_empresa, fk_id_cliente, fk_id_usuario, fk_id_doc)
VALUES ('FT-TESTE-001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 117.00, 1, 1, 1, 1);
select * from itemfactura;
INSERT INTO itemfactura (quantidade, subtotal, qtd_por_unidade, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem)
VALUES (2, 234.00, 1, 1, 'FT-TESTE-001', 1, 1);

SELECT
    'TESTE 4 - Movimento de saida por venda' AS teste,
    m.tipoOrigem,
    m.tipoMovimento,
    m.quantidade,
    m.idProduto,
    m.idArmazem,
    m.idLote
FROM movimento m
WHERE m.tipoOrigem = 'Venda'
ORDER BY m.id DESC
LIMIT 1;

SELECT
    'TESTE 4 - Lote apos venda' AS teste,
    l.codigo,
    l.quantidadeAtual,
    l.estado
FROM lote l
WHERE l.codigo = 'LOTE-TESTE-001';

SELECT
    'TESTE 4 - Movimento/lote da venda' AS teste,
    ml.idMovimento,
    ml.idLote,
    ml.quantidadeRetirada
FROM movimento_lote ml
JOIN movimento m ON m.id = ml.idMovimento
WHERE m.tipoOrigem = 'Venda'
ORDER BY ml.id DESC
LIMIT 1;

SELECT
    'TESTE 4 - Stock apos venda' AS teste,
    s.idProduto,
    s.idArmazem,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- ============================================================
-- TESTE 5: procedure/evento de preco nao deve apontar para tabela inexistente
-- Se procedure_updatePrecoVenda continuar usando 'precovenda',
-- a chamada abaixo deve falhar. Depois da correcao, deve executar.
-- ============================================================

CALL procedure_updatePrecoVenda();

SELECT 'TESTE 5 - procedure_updatePrecoVenda executou sem erro' AS teste;


