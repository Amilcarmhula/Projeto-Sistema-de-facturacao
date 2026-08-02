-- ============================================================
-- DADOS DE TESTE E VALIDACAO
-- MODULO DE COMPRAS, RECEBIMENTO E CONTAS A PAGAR
-- Sistema stand-alone JM-InvSM
-- ============================================================
--
-- Dependencias:
-- 1. 00_TABELAS_COMPLETO.sql
-- 2. 01_TRIGGERS_COMPLETO.sql
-- 3. 04_TABELAS_COMPRAS_CONTAS_PAGAR.sql
-- 4. 05_REGRAS_COMPRAS_CONTAS_PAGAR.sql
--
-- Fluxo validado:
-- Requisicao aprovada
--   -> ordem_compra
--   -> recebimento_compra
--   -> lote + movimento Compra/Entrada + stock.saldo
--   -> factura_fornecedor
--   -> conta_pagar
--   -> pagamento_fornecedor por Caixa
--   -> pagamento_fornecedor por Banco
--   -> conta_pagar liquidada
--   -> factura_fornecedor paga
--   -> movimento_financeiro de saida
-- ============================================================

SET NAMES utf8mb4;

-- ============================================================
-- 1. LIMPEZA DOS DADOS DE TESTE ANTERIORES
-- ============================================================

DELETE FROM movimento_financeiro
WHERE idPagamentoFornecedor IN (
    SELECT pf.id
    FROM pagamento_fornecedor pf
    JOIN conta_pagar cp ON cp.id = pf.idContaPagar
    JOIN factura_fornecedor ff ON ff.id = cp.idFacturaFornecedor
    WHERE ff.numero = 'FF-COMPRA-TESTE-001'
)
OR descricao LIKE 'Pagamento teste fornecedor%';

DELETE FROM caixa
WHERE fk_id_transacoes IN (
    SELECT id
    FROM transacoes
    WHERE descricao LIKE 'SALDO INICIAL TESTE COMPRAS%'
       OR descricao LIKE 'Pagamento teste fornecedor%'
);

DELETE FROM contacorrente
WHERE fk_id_transacoes IN (
    SELECT id
    FROM transacoes
    WHERE descricao LIKE 'SALDO INICIAL TESTE COMPRAS%'
       OR descricao LIKE 'Pagamento teste fornecedor%'
);

DELETE FROM transacoes
WHERE descricao LIKE 'SALDO INICIAL TESTE COMPRAS%'
   OR descricao LIKE 'Pagamento teste fornecedor%';

DELETE pf
FROM pagamento_fornecedor pf
JOIN conta_pagar cp ON cp.id = pf.idContaPagar
JOIN factura_fornecedor ff ON ff.id = cp.idFacturaFornecedor
WHERE ff.numero = 'FF-COMPRA-TESTE-001';

DELETE cp
FROM conta_pagar cp
JOIN factura_fornecedor ff ON ff.id = cp.idFacturaFornecedor
WHERE ff.numero = 'FF-COMPRA-TESTE-001';

DELETE iff
FROM item_factura_fornecedor iff
JOIN factura_fornecedor ff ON ff.id = iff.idFacturaFornecedor
WHERE ff.numero = 'FF-COMPRA-TESTE-001';

DELETE FROM factura_fornecedor
WHERE numero = 'FF-COMPRA-TESTE-001';

DELETE FROM item_recebimento_compra
WHERE codigoLote = 'LOTE-COMPRA-TEST-01'
   OR idRecebimentoCompra IN (
        SELECT id
        FROM recebimento_compra
        WHERE numero = 'RC-COMPRA-TESTE-001'
   );

DELETE FROM recebimento_compra
WHERE numero = 'RC-COMPRA-TESTE-001';

DELETE FROM item_ordem_compra
WHERE idOrdemCompra IN (
    SELECT id
    FROM ordem_compra
    WHERE numero LIKE 'OC-%-R9001-F9001'
       OR idRequisicao = 9001
);

DELETE FROM ordem_compra
WHERE numero LIKE 'OC-%-R9001-F9001'
   OR idRequisicao = 9001;

DELETE FROM movimento_lote
WHERE idLote IN (
    SELECT id
    FROM lote
    WHERE codigo = 'LOTE-COMPRA-TEST-01'
);

DELETE FROM movimento
WHERE idLote IN (
    SELECT id
    FROM lote
    WHERE codigo = 'LOTE-COMPRA-TEST-01'
);

DELETE FROM lote
WHERE codigo = 'LOTE-COMPRA-TEST-01';

DELETE FROM itemRequisicao
WHERE idRequisicao = 9001;

DELETE FROM requisicao
WHERE id = 9001;

-- Reset do stock do produto de teste.
DELETE FROM stock
WHERE idProduto = 9001;

-- ============================================================
-- 2. DADOS BASE MINIMOS
-- ============================================================

INSERT INTO empresa (id, nome, razaosocial, nuit)
VALUES (9001, 'Empresa Teste Compras', 'Empresa Teste Compras Lda', 900100001)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    razaosocial = VALUES(razaosocial),
    nuit = VALUES(nuit);

INSERT INTO contabancaria (id, nome_banco, numero, nib, nuib, fk_id_empresa)
VALUES (9001, 'Banco Teste Compras', '900100001', '900100001000000000001', '900100001', 9001)
ON DUPLICATE KEY UPDATE
    nome_banco = VALUES(nome_banco),
    numero = VALUES(numero),
    nib = VALUES(nib),
    nuib = VALUES(nuib),
    fk_id_empresa = VALUES(fk_id_empresa);

INSERT INTO nivel_acesso (id, descricao)
VALUES (9001, 'Nivel Teste Compras')
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao);

INSERT INTO permissao (id, tipo, descricao)
VALUES (9001, 'CRUD', 'Permissao teste compras')
ON DUPLICATE KEY UPDATE
    tipo = VALUES(tipo),
    descricao = VALUES(descricao);

INSERT INTO usuario (id, nome, email, usuario, senha, estado, fk_nivel_acesso, fk_permissao)
VALUES (9001, 'Utilizador Teste Compras', 'teste.compras@example.com', 'compras', 'senha123', 1, 9001, 9001)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    usuario = VALUES(usuario),
    senha = VALUES(senha),
    estado = VALUES(estado),
    fk_nivel_acesso = VALUES(fk_nivel_acesso),
    fk_permissao = VALUES(fk_permissao);

INSERT INTO fornecedor (id, tipo, razaosocial, nuit)
VALUES (9001, 'Juridica', 'Fornecedor Teste Compras', 900100002)
ON DUPLICATE KEY UPDATE
    tipo = VALUES(tipo),
    razaosocial = VALUES(razaosocial),
    nuit = VALUES(nuit);

INSERT INTO armazem (id, nome, tipo, descricao, estado)
VALUES (9001, 'Armazem Teste Compras', 'Central', 'Armazem usado em testes de compras', 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    tipo = VALUES(tipo),
    descricao = VALUES(descricao),
    estado = VALUES(estado);

INSERT INTO categoria (id, grupo, subgrupo, familia, descricao)
VALUES (9001, 'Teste', 'Compras', 'Material Teste', 'Categoria usada nos testes de compras')
ON DUPLICATE KEY UPDATE
    grupo = VALUES(grupo),
    subgrupo = VALUES(subgrupo),
    familia = VALUES(familia),
    descricao = VALUES(descricao);

INSERT INTO unidadeMedida (id, nome, sigla, descricao)
VALUES (9001, 'Unidade Teste', 'un', 'Unidade usada nos testes de compras')
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    sigla = VALUES(sigla),
    descricao = VALUES(descricao);

INSERT INTO imposto (id, nome, percentagem, descricao)
VALUES (9001, 'IVA Teste Compras', 0.000, 'Imposto neutro para teste de compras')
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    percentagem = VALUES(percentagem),
    descricao = VALUES(descricao);

-- Metodo 1 precisa representar dinheiro, pois a regra financeira existente
-- usa idMetodoPagamento = 1 para Caixa.
INSERT INTO metodo_pagamento (id, nome, descricao)
VALUES (1, 'Dinheiro (Especie)', 'Metodo em numerario usado para Caixa')
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    descricao = VALUES(descricao);

INSERT INTO metodo_pagamento (id, nome, descricao)
VALUES (3, 'Transferencia Bancaria', 'Metodo bancario usado para Conta Corrente')
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    descricao = VALUES(descricao);

INSERT INTO produto (
    id,
    nome,
    codigo_barra,
    tipoProduto,
    unidadePorTipo,
    descricao,
    controle_stock,
    nivelstock,
    idCategoria,
    idUnidade,
    idImposto
)
VALUES (
    9001,
    'Produto Teste Compras',
    'COMPRA-TESTE-001',
    'Simples',
    1,
    'Produto usado para validar ordem de compra e recebimento',
    1,
    5,
    9001,
    9001,
    9001
)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    codigo_barra = VALUES(codigo_barra),
    tipoProduto = VALUES(tipoProduto),
    unidadePorTipo = VALUES(unidadePorTipo),
    descricao = VALUES(descricao),
    controle_stock = VALUES(controle_stock),
    nivelstock = VALUES(nivelstock),
    idCategoria = VALUES(idCategoria),
    idUnidade = VALUES(idUnidade),
    idImposto = VALUES(idImposto);

INSERT INTO stock (idProduto, idArmazem, saldo, reservado)
VALUES (9001, 9001, 0, 0)
ON DUPLICATE KEY UPDATE
    saldo = 0,
    reservado = 0;

-- Saldos financeiros iniciais para validar pagamentos por Caixa e Banco.
INSERT INTO transacoes (
    data_pagamento,
    valorRecebido,
    valorPago,
    trocos,
    tipo_transacao,
    descricao,
    fk_id_contabancaria,
    fk_id_metodo_pagamento,
    fk_numero,
    fk_parcela,
    fk_id_usuario
)
VALUES (
    CURDATE(),
    10000.00,
    10000.00,
    0.00,
    'Entrada',
    'SALDO INICIAL TESTE COMPRAS - CAIXA',
    NULL,
    1,
    NULL,
    NULL,
    9001
);

INSERT INTO transacoes (
    data_pagamento,
    valorRecebido,
    valorPago,
    trocos,
    tipo_transacao,
    descricao,
    fk_id_contabancaria,
    fk_id_metodo_pagamento,
    fk_numero,
    fk_parcela,
    fk_id_usuario
)
VALUES (
    CURDATE(),
    10000.00,
    10000.00,
    0.00,
    'Entrada',
    'SALDO INICIAL TESTE COMPRAS - BANCO',
    9001,
    3,
    NULL,
    NULL,
    9001
);

-- ============================================================
-- 3. REQUISICAO APROVADA
-- ============================================================

INSERT INTO requisicao (id, dataRequisicao, dataNecessidade, estado, motivo)
VALUES (
    9001,
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 7 DAY),
    'Aprovada',
    'VALIDACAO COMPRA: requisicao para gerar ordem de compra'
);

INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES (20, 9001, 9001, 9001);

-- ============================================================
-- 4. CRIAR ORDEM DE COMPRA A PARTIR DA REQUISICAO
-- ============================================================

CALL CriarOrdemCompraDeRequisicao(
    9001,
    9001,
    DATE_ADD(CURDATE(), INTERVAL 10 DAY),
    9001
);

SELECT id
INTO @id_ordem_compra
FROM ordem_compra
WHERE idRequisicao = 9001
  AND idFornecedor = 9001
  AND estado <> 'Cancelada'
ORDER BY id DESC
LIMIT 1;

SELECT id
INTO @id_item_ordem_compra
FROM item_ordem_compra
WHERE idOrdemCompra = @id_ordem_compra
ORDER BY id
LIMIT 1;

SELECT
    'TESTE 1 - Ordem de compra criada' AS teste,
    oc.id,
    oc.numero,
    oc.estado,
    oc.subtotal,
    oc.total
FROM ordem_compra oc
WHERE oc.id = @id_ordem_compra;

-- ============================================================
-- 5. CRIAR E CONFIRMAR RECEBIMENTO
-- ============================================================

INSERT INTO recebimento_compra (
    numero,
    idOrdemCompra,
    idFornecedor,
    dataRecebimento,
    documentoFornecedor,
    estado,
    observacao,
    idUsuario
)
VALUES (
    'RC-COMPRA-TESTE-001',
    @id_ordem_compra,
    9001,
    NOW(),
    'GUIA-TESTE-001',
    'Rascunho',
    'Recebimento de teste para compras',
    9001
);

SET @id_recebimento_compra = LAST_INSERT_ID();

INSERT INTO item_recebimento_compra (
    idRecebimentoCompra,
    idItemOrdemCompra,
    idProduto,
    idArmazem,
    quantidadeRecebida,
    custoUnitario,
    codigoLote,
    estado
)
VALUES (
    @id_recebimento_compra,
    @id_item_ordem_compra,
    9001,
    9001,
    20,
    25.00,
    'LOTE-COMPRA-TEST-01',
    'Pendente'
);

CALL ConfirmarRecebimentoCompra(@id_recebimento_compra);

SELECT
    'TESTE 2 - Recebimento confirmado' AS teste,
    rc.id,
    rc.numero,
    rc.estado,
    rc.totalRecebido
FROM recebimento_compra rc
WHERE rc.id = @id_recebimento_compra;

SELECT
    'TESTE 3 - Ordem actualizada pelo recebimento' AS teste,
    oc.estado AS estadoOrdem,
    ioc.quantidadeSolicitada,
    ioc.quantidadeRecebida,
    ioc.custoUnitario,
    ioc.totalLinha,
    oc.subtotal,
    oc.total,
    ioc.estado AS estadoItem
FROM ordem_compra oc
JOIN item_ordem_compra ioc ON ioc.idOrdemCompra = oc.id
WHERE oc.id = @id_ordem_compra;

SELECT
    'TESTE 4 - Stock apos recebimento' AS teste,
    s.idProduto,
    s.idArmazem,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 9001
  AND s.idArmazem = 9001;

SELECT
    'TESTE 5 - Lote criado no recebimento' AS teste,
    l.id,
    l.codigo,
    l.quantidadeAtual,
    l.custoUnitario,
    l.estado
FROM lote l
WHERE l.codigo = 'LOTE-COMPRA-TEST-01';

SELECT
    'TESTE 6 - Movimento Compra/Entrada' AS teste,
    m.id,
    m.tipoOrigem,
    m.tipoMovimento,
    m.quantidade,
    m.idLote
FROM movimento m
JOIN lote l ON l.id = m.idLote
WHERE l.codigo = 'LOTE-COMPRA-TEST-01';

-- ============================================================
-- 6. CRIAR FACTURA DO FORNECEDOR E CONTA A PAGAR
-- ============================================================

CALL CriarFacturaFornecedorDeRecebimento(
    'FF-COMPRA-TESTE-001',
    @id_recebimento_compra,
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 30 DAY),
    9001
);

SELECT id
INTO @id_factura_fornecedor
FROM factura_fornecedor
WHERE numero = 'FF-COMPRA-TESTE-001'
  AND idFornecedor = 9001
LIMIT 1;

SELECT id
INTO @id_conta_pagar
FROM conta_pagar
WHERE idFacturaFornecedor = @id_factura_fornecedor
LIMIT 1;

SELECT
    'TESTE 7 - Factura fornecedor criada' AS teste,
    ff.id,
    ff.numero,
    ff.total,
    ff.valorPago,
    ff.saldoPendente,
    ff.estado
FROM factura_fornecedor ff
WHERE ff.id = @id_factura_fornecedor;

SELECT
    'TESTE 8 - Conta a pagar criada' AS teste,
    cp.id,
    cp.valorOriginal,
    cp.valorPago,
    cp.saldoPendente,
    cp.estado
FROM conta_pagar cp
WHERE cp.id = @id_conta_pagar;

-- ============================================================
-- 7. PAGAMENTO PARCIAL POR CAIXA
-- ============================================================

CALL RegistarPagamentoFornecedor(
    @id_conta_pagar,
    CURDATE(),
    200.00,
    1,
    'Caixa',
    NULL,
    'PG-CX-TESTE-001',
    'Pagamento teste fornecedor - caixa',
    9001
);

SELECT
    'TESTE 9 - Conta parcial apos pagamento por caixa' AS teste,
    cp.valorOriginal,
    cp.valorPago,
    cp.saldoPendente,
    cp.estado
FROM conta_pagar cp
WHERE cp.id = @id_conta_pagar;

SELECT
    'TESTE 10 - Ultimo saldo de caixa' AS teste,
    c.saldo_inicial,
    c.saldo_atual
FROM caixa c
ORDER BY c.id DESC
LIMIT 1;

-- ============================================================
-- 8. PAGAMENTO FINAL POR BANCO
-- ============================================================

CALL RegistarPagamentoFornecedor(
    @id_conta_pagar,
    CURDATE(),
    300.00,
    3,
    'Banco',
    9001,
    'PG-BK-TESTE-001',
    'Pagamento teste fornecedor - banco',
    9001
);

SELECT
    'TESTE 11 - Conta liquidada' AS teste,
    cp.valorOriginal,
    cp.valorPago,
    cp.saldoPendente,
    cp.estado
FROM conta_pagar cp
WHERE cp.id = @id_conta_pagar;

SELECT
    'TESTE 12 - Factura fornecedor paga' AS teste,
    ff.total,
    ff.valorPago,
    ff.saldoPendente,
    ff.estado
FROM factura_fornecedor ff
WHERE ff.id = @id_factura_fornecedor;

SELECT
    'TESTE 13 - Ultimo saldo bancario/conta corrente' AS teste,
    cc.saldo_inicial,
    cc.saldo_atual
FROM contacorrente cc
ORDER BY cc.id DESC
LIMIT 1;

SELECT
    'TESTE 14 - Movimentos financeiros de saida' AS teste,
    mf.id,
    mf.tipoMovimento,
    mf.origem,
    mf.valor,
    mf.origemPagamento,
    mf.saldoAnterior,
    mf.saldoPosterior
FROM movimento_financeiro mf
JOIN pagamento_fornecedor pf ON pf.id = mf.idPagamentoFornecedor
WHERE pf.idContaPagar = @id_conta_pagar
ORDER BY mf.id;

-- ============================================================
-- RESULTADO ESPERADO
-- ============================================================
--
-- TESTE 1: ordem_compra em estado Aprovada, total 0.00 antes do recebimento.
-- TESTE 2: recebimento_compra em estado Confirmado, totalRecebido 500.00.
-- TESTE 3: ordem_compra Recebida, item_ordem_compra Recebido, custoUnitario 25.00 e total 500.00.
-- TESTE 4: stock do produto 9001 no armazem 9001 com saldo 20.
-- TESTE 5: lote LOTE-COMPRA-TEST-01 criado e Ativo.
-- TESTE 6: movimento Compra / Entrada criado para o lote.
-- TESTE 7: factura_fornecedor Registada, total 500.00.
-- TESTE 8: conta_pagar Aberta, saldoPendente 500.00.
-- TESTE 9: conta_pagar Parcial, saldoPendente 300.00.
-- TESTE 11: conta_pagar Liquidada, saldoPendente 0.00.
-- TESTE 12: factura_fornecedor Paga.
-- TESTE 14: dois movimentos financeiros de Saida, um Caixa e um Banco.
-- ============================================================
