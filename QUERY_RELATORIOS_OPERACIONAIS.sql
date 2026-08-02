-- ============================================================
-- QUERIES PARA RELATORIOS OPERACIONAIS
-- JM-InvSM / Facturacao, Compras, Stock e Contas a Pagar
-- ============================================================
--
-- Como usar:
--   1. Ajuste as variaveis abaixo conforme o filtro pretendido.
--   2. Execute apenas a query do relatorio desejado.
--
-- Filtros opcionais:
--   - Use NULL para nao filtrar por esse campo.
--   - Datas usam formato 'YYYY-MM-DD'.
--
-- Nota:
--   Estas queries foram escritas sem filtros deleteStatus para ficarem
--   compativeis com as estruturas de tabela base. Se a sua base ja tiver
--   delete logico aplicado, pode acrescentar:
--     AND tabela.deleteStatus = 0
--   nas tabelas correspondentes.
-- ============================================================
USE _sysface_teste;
select * from lote;
SET @data_inicio = '2026-07-01';
SET @data_fim = '2026-07-31';
SET @id_fornecedor = 1;
SET @id_cliente = 1;
SET @id_armazem = 1;
SET @id_produto = 2;
SET @estado = Null;

-- ============================================================
-- 1. RELATORIO DE FORNECEDORES, PRODUTOS, ARMAZENS, QUANTIDADES E LOTES
-- ============================================================
-- Uma linha por lote fornecido.
-- Mostra o fornecedor, produto, armazem, saldo do lote e totais agregados
-- por fornecedor/produto/armazem.

SELECT
    f.id AS fornecedor_id,
    f.tipo AS fornecedor_tipo,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit,

    p.id AS produto_id,
    p.nome AS produto_nome,
    p.codigo_barra AS produto_codigo_barra,
    p.tipoProduto AS produto_tipo,
    p.controle_stock AS produto_controla_stock,

    a.id AS armazem_id,
    a.nome AS armazem_nome,
    a.tipo AS armazem_tipo,

    l.id AS lote_id,
    l.codigo AS lote_codigo,
    l.dataEntrada AS lote_data_entrada,
    l.quantidadeInicial AS lote_quantidade_inicial,
    l.quantidadeAtual AS lote_quantidade_actual,
    l.custoUnitario AS lote_custo_unitario,
    l.valorTotal AS lote_valor_total,
    l.estado AS lote_estado,

    resumo.total_lotes,
    resumo.quantidade_inicial_total,
    resumo.quantidade_actual_total,
    resumo.valor_total_actual

FROM fornecedor f
JOIN lote l
    ON l.idFornecedor = f.id
JOIN produto p
    ON p.id = l.idProduto
JOIN armazem a
    ON a.id = l.idArmazem
JOIN (
    SELECT
        idFornecedor,
        idProduto,
        idArmazem,
        COUNT(*) AS total_lotes,
        SUM(COALESCE(quantidadeInicial, 0)) AS quantidade_inicial_total,
        SUM(COALESCE(quantidadeAtual, 0)) AS quantidade_actual_total,
        SUM(COALESCE(valorTotal, 0)) AS valor_total_actual
    FROM lote
    GROUP BY idFornecedor, idProduto, idArmazem
) resumo
    ON resumo.idFornecedor = l.idFornecedor
   AND resumo.idProduto = l.idProduto
   AND resumo.idArmazem = l.idArmazem
WHERE (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@id_armazem IS NULL OR a.id = @id_armazem)
  AND (@estado IS NULL OR l.estado = @estado)
  AND (@data_inicio IS NULL OR l.dataEntrada >= @data_inicio)
  AND (@data_fim IS NULL OR l.dataEntrada <= @data_fim)
ORDER BY f.razaosocial, p.nome, a.nome, l.dataEntrada, l.id;

-- ============================================================
-- 2. RELATORIO DE CLIENTES, FACTURAS E QUANTIDADE DE FACTURAS POR CLIENTE
-- ============================================================
-- Uma linha por factura do cliente.
-- Inclui total de facturas, total facturado, total pago e saldo por cliente.

SELECT
    c.id AS cliente_id,
    c.tipo AS cliente_tipo,
    c.nome AS cliente_nome,
    c.razaosocial AS cliente_razao_social,
    c.nuit AS cliente_nuit,

    ec.provincia AS cliente_provincia,
    ec.cidade AS cliente_cidade,
    ec.bairro AS cliente_bairro,
    ec.avenida AS cliente_avenida,
    ec.rua AS cliente_rua,
    ec.numero AS cliente_endereco_numero,

    cc.pessoa_de_contacto AS cliente_pessoa_contacto,
    cc.contacto AS cliente_contacto,
    cc.email AS cliente_email,
    cc.website AS cliente_website,

    f.numero AS factura_numero,
    f.data_emissao AS factura_data_emissao,
    f.data_vencimento AS factura_data_vencimento,
    f.total AS factura_total,
    f.totalPago AS factura_total_pago,
    f.saldoPendente AS factura_saldo_pendente,
    f.estado AS factura_estado,
    f.condicaoPagemento AS factura_condicao_pagamento,
    dc.nome AS documento_nome,
    dc.sigla AS documento_sigla,

    resumo.quantidade_facturas_cliente,
    resumo.total_facturado_cliente,
    resumo.total_pago_cliente,
    resumo.saldo_pendente_cliente

FROM cliente c
LEFT JOIN endereco_cliente ec
    ON ec.id = (
        SELECT ec2.id
        FROM endereco_cliente ec2
        WHERE ec2.fk_id_cliente = c.id
        ORDER BY CASE WHEN ec2.tipo = 'Facturacao' THEN 0 ELSE 1 END, ec2.id
        LIMIT 1
    )
LEFT JOIN contacto_cliente cc
    ON cc.id = (
        SELECT cc2.id
        FROM contacto_cliente cc2
        WHERE cc2.fk_id_cliente = c.id
        ORDER BY cc2.id
        LIMIT 1
    )
LEFT JOIN factura f
    ON f.fk_id_cliente = c.id
LEFT JOIN documentocomercial dc
    ON dc.id = f.fk_id_doc
LEFT JOIN (
    SELECT
        fk_id_cliente,
        COUNT(*) AS quantidade_facturas_cliente,
        SUM(COALESCE(total, 0)) AS total_facturado_cliente,
        SUM(COALESCE(totalPago, 0)) AS total_pago_cliente,
        SUM(COALESCE(saldoPendente, 0)) AS saldo_pendente_cliente
    FROM factura
    WHERE (@data_inicio IS NULL OR data_emissao >= @data_inicio)
      AND (@data_fim IS NULL OR data_emissao <= @data_fim)
    GROUP BY fk_id_cliente
) resumo
    ON resumo.fk_id_cliente = c.id
WHERE (@id_cliente IS NULL OR c.id = @id_cliente)
  AND (@estado IS NULL OR f.estado = @estado)
  AND (@data_inicio IS NULL OR f.data_emissao >= @data_inicio OR f.numero IS NULL)
  AND (@data_fim IS NULL OR f.data_emissao <= @data_fim OR f.numero IS NULL)
ORDER BY c.nome, f.data_emissao, f.numero;

-- ============================================================
-- 3. RELATORIO DE ARMAZENS, STOCK, LOTES E FORNECEDORES
-- ============================================================
-- Uma linha por lote/produto/armazem.
-- Inclui saldo global do produto no armazem e dados do fornecedor do lote.

SELECT
    a.id AS armazem_id,
    a.nome AS armazem_nome,
    a.tipo AS armazem_tipo,
    a.descricao AS armazem_descricao,
    a.estado AS armazem_estado,

    p.id AS produto_id,
    p.nome AS produto_nome,
    p.codigo_barra AS produto_codigo_barra,
    p.controle_stock AS produto_controla_stock,
    p.nivelstock AS produto_nivel_stock,

    s.saldo AS stock_saldo,
    s.reservado AS stock_reservado,
    s.disponivel AS stock_disponivel,

    l.id AS lote_id,
    l.codigo AS lote_codigo,
    l.dataEntrada AS lote_data_entrada,
    l.quantidadeInicial AS lote_quantidade_inicial,
    l.quantidadeAtual AS lote_quantidade_actual,
    l.custoUnitario AS lote_custo_unitario,
    l.valorTotal AS lote_valor_total,
    l.estado AS lote_estado,

    f.id AS fornecedor_id,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit

FROM armazem a
JOIN stock s
    ON s.idArmazem = a.id
JOIN produto p
    ON p.id = s.idProduto
LEFT JOIN lote l
    ON l.idProduto = p.id
   AND l.idArmazem = a.id
LEFT JOIN fornecedor f
    ON f.id = l.idFornecedor
WHERE (@id_armazem IS NULL OR a.id = @id_armazem)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@estado IS NULL OR l.estado = @estado)
ORDER BY a.nome, p.nome, l.dataEntrada, l.id;

-- ============================================================
-- 4. RELATORIO DE REQUISICOES RELACIONADAS A FORNECEDOR,
--    PRODUTOS A PAGAR E UTILIZADORES QUE EMITIRAM PAGAMENTOS
-- ============================================================
-- Liga requisicao -> ordem_compra -> recebimento -> factura_fornecedor
-- -> conta_pagar -> pagamento_fornecedor.
-- Uma linha por pagamento/produto relacionado a requisicao.

SELECT
    r.id AS requisicao_id,
    r.dataRequisicao AS requisicao_data,
    r.dataNecessidade AS requisicao_data_necessidade,
    r.estado AS requisicao_estado,
    r.motivo AS requisicao_motivo,

    f.id AS fornecedor_id,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit,

    p.id AS produto_id,
    p.nome AS produto_nome,
    ioc.quantidadeSolicitada AS quantidade_solicitada_compra,
    ioc.quantidadeRecebida AS quantidade_recebida_compra,
    ioc.custoUnitario AS custo_unitario_compra,
    ioc.totalLinha AS total_linha_compra,

    ff.id AS factura_fornecedor_id,
    ff.numero AS factura_fornecedor_numero,
    ff.total AS factura_fornecedor_total,
    cp.id AS conta_pagar_id,
    cp.valorOriginal AS conta_valor_original,
    cp.valorPago AS conta_valor_pago,
    cp.saldoPendente AS conta_saldo_pendente,
    cp.estado AS conta_estado,

    pf.id AS pagamento_id,
    pf.dataPagamento AS pagamento_data,
    pf.valorPago AS pagamento_valor,
    pf.origemPagamento AS pagamento_origem,
    mp.nome AS metodo_pagamento,
    u.id AS usuario_pagamento_id,
    u.nome AS usuario_pagamento_nome,
    u.usuario AS usuario_pagamento_login

FROM requisicao r
JOIN ordem_compra oc
    ON oc.idRequisicao = r.id
JOIN fornecedor f
    ON f.id = oc.idFornecedor
JOIN item_ordem_compra ioc
    ON ioc.idOrdemCompra = oc.id
JOIN produto p
    ON p.id = ioc.idProduto
LEFT JOIN recebimento_compra rc
    ON rc.idOrdemCompra = oc.id
LEFT JOIN factura_fornecedor ff
    ON ff.idOrdemCompra = oc.id
   AND (ff.idRecebimentoCompra = rc.id OR ff.idRecebimentoCompra IS NULL)
LEFT JOIN conta_pagar cp
    ON cp.idFacturaFornecedor = ff.id
LEFT JOIN pagamento_fornecedor pf
    ON pf.idContaPagar = cp.id
LEFT JOIN metodo_pagamento mp
    ON mp.id = pf.idMetodoPagamento
LEFT JOIN usuario u
    ON u.id = pf.idUsuario
WHERE (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@estado IS NULL OR r.estado = @estado OR cp.estado = @estado)
  AND (@data_inicio IS NULL OR r.dataRequisicao >= @data_inicio)
  AND (@data_fim IS NULL OR r.dataRequisicao <= @data_fim)
ORDER BY r.id, f.razaosocial, p.nome, pf.dataPagamento, pf.id;

-- ============================================================
-- 5. RELATORIO DE ORDENS DE COMPRA, FORNECEDOR,
--    PRODUTOS A PAGAR E UTILIZADORES QUE EMITIRAM PAGAMENTOS
-- ============================================================

SELECT
    oc.id AS ordem_compra_id,
    oc.numero AS ordem_compra_numero,
    oc.dataEmissao AS ordem_data_emissao,
    oc.dataPrevistaEntrega AS ordem_data_prevista_entrega,
    oc.estado AS ordem_estado,
    oc.subtotal AS ordem_subtotal,
    oc.desconto AS ordem_desconto,
    oc.imposto AS ordem_imposto,
    oc.total AS ordem_total,
    uoc.nome AS usuario_ordem_nome,

    f.id AS fornecedor_id,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit,

    p.id AS produto_id,
    p.nome AS produto_nome,
    ioc.quantidadeSolicitada AS item_quantidade_solicitada,
    ioc.quantidadeRecebida AS item_quantidade_recebida,
    (ioc.quantidadeSolicitada - ioc.quantidadeRecebida) AS item_quantidade_pendente,
    ioc.custoUnitario AS item_custo_unitario,
    ioc.totalLinha AS item_total_linha,
    ioc.estado AS item_estado,

    ff.numero AS factura_fornecedor_numero,
    cp.id AS conta_pagar_id,
    cp.valorOriginal AS conta_valor_original,
    cp.valorPago AS conta_valor_pago,
    cp.saldoPendente AS conta_saldo_pendente,
    cp.estado AS conta_estado,

    pf.id AS pagamento_id,
    pf.dataPagamento AS pagamento_data,
    pf.valorPago AS pagamento_valor,
    pf.origemPagamento AS pagamento_origem,
    mp.nome AS metodo_pagamento,
    up.nome AS usuario_pagamento_nome

FROM ordem_compra oc
JOIN fornecedor f
    ON f.id = oc.idFornecedor
LEFT JOIN usuario uoc
    ON uoc.id = oc.idUsuario
JOIN item_ordem_compra ioc
    ON ioc.idOrdemCompra = oc.id
JOIN produto p
    ON p.id = ioc.idProduto
LEFT JOIN factura_fornecedor ff
    ON ff.idOrdemCompra = oc.id
LEFT JOIN conta_pagar cp
    ON cp.idFacturaFornecedor = ff.id
LEFT JOIN pagamento_fornecedor pf
    ON pf.idContaPagar = cp.id
LEFT JOIN metodo_pagamento mp
    ON mp.id = pf.idMetodoPagamento
LEFT JOIN usuario up
    ON up.id = pf.idUsuario
WHERE (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@estado IS NULL OR oc.estado = @estado OR cp.estado = @estado)
  AND (@data_inicio IS NULL OR oc.dataEmissao >= @data_inicio)
  AND (@data_fim IS NULL OR oc.dataEmissao <= @data_fim)
ORDER BY oc.dataEmissao, oc.numero, p.nome, pf.dataPagamento;

-- ============================================================
-- 6. RELATORIO DE ORDENS DE RECEBIMENTO, FORNECEDOR,
--    PRODUTOS A PAGAR E UTILIZADORES QUE EMITIRAM PAGAMENTOS
-- ============================================================

SELECT
    rc.id AS recebimento_id,
    rc.numero AS recebimento_numero,
    rc.dataRecebimento AS recebimento_data,
    rc.documentoFornecedor AS recebimento_documento_fornecedor,
    rc.estado AS recebimento_estado,
    rc.totalRecebido AS recebimento_total,
    urc.nome AS usuario_recebimento_nome,

    oc.id AS ordem_compra_id,
    oc.numero AS ordem_compra_numero,

    f.id AS fornecedor_id,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit,

    p.id AS produto_id,
    p.nome AS produto_nome,
    irc.quantidadeRecebida AS item_quantidade_recebida,
    irc.custoUnitario AS item_custo_unitario,
    irc.subtotal AS item_subtotal,
    irc.codigoLote AS item_codigo_lote,
    l.codigo AS lote_codigo,
    l.quantidadeAtual AS lote_quantidade_actual,
    irc.estado AS item_estado,

    ff.numero AS factura_fornecedor_numero,
    cp.id AS conta_pagar_id,
    cp.valorOriginal AS conta_valor_original,
    cp.valorPago AS conta_valor_pago,
    cp.saldoPendente AS conta_saldo_pendente,
    cp.estado AS conta_estado,

    pf.id AS pagamento_id,
    pf.dataPagamento AS pagamento_data,
    pf.valorPago AS pagamento_valor,
    pf.origemPagamento AS pagamento_origem,
    mp.nome AS metodo_pagamento,
    up.nome AS usuario_pagamento_nome

FROM recebimento_compra rc
JOIN ordem_compra oc
    ON oc.id = rc.idOrdemCompra
JOIN fornecedor f
    ON f.id = rc.idFornecedor
LEFT JOIN usuario urc
    ON urc.id = rc.idUsuario
JOIN item_recebimento_compra irc
    ON irc.idRecebimentoCompra = rc.id
JOIN produto p
    ON p.id = irc.idProduto
LEFT JOIN lote l
    ON l.id = irc.idLote
LEFT JOIN factura_fornecedor ff
    ON ff.idRecebimentoCompra = rc.id
LEFT JOIN conta_pagar cp
    ON cp.idFacturaFornecedor = ff.id
LEFT JOIN pagamento_fornecedor pf
    ON pf.idContaPagar = cp.id
LEFT JOIN metodo_pagamento mp
    ON mp.id = pf.idMetodoPagamento
LEFT JOIN usuario up
    ON up.id = pf.idUsuario
WHERE (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@estado IS NULL OR rc.estado = @estado OR cp.estado = @estado)
  AND (@data_inicio IS NULL OR DATE(rc.dataRecebimento) >= @data_inicio)
  AND (@data_fim IS NULL OR DATE(rc.dataRecebimento) <= @data_fim)
ORDER BY rc.dataRecebimento, rc.numero, p.nome, pf.dataPagamento;

-- ============================================================
-- 7. RELATORIO DE CONTAS A PAGAR, FORNECEDOR,
--    PRODUTOS A PAGAR E UTILIZADORES QUE EMITIRAM PAGAMENTOS
-- ============================================================

SELECT
    cp.id AS conta_pagar_id,
    cp.dataEmissao AS conta_data_emissao,
    cp.dataVencimento AS conta_data_vencimento,
    cp.valorOriginal AS conta_valor_original,
    cp.valorPago AS conta_valor_pago,
    cp.saldoPendente AS conta_saldo_pendente,
    cp.estado AS conta_estado,

    f.id AS fornecedor_id,
    f.razaosocial AS fornecedor_razao_social,
    f.nuit AS fornecedor_nuit,

    ff.id AS factura_fornecedor_id,
    ff.numero AS factura_fornecedor_numero,
    ff.total AS factura_fornecedor_total,
    ff.estado AS factura_fornecedor_estado,

    p.id AS produto_id,
    p.nome AS produto_nome,
    iff.quantidade AS produto_quantidade_facturada,
    iff.custoUnitario AS produto_custo_unitario,
    iff.subtotal AS produto_subtotal,
    iff.totalLinha AS produto_total_linha,

    pf.id AS pagamento_id,
    pf.dataPagamento AS pagamento_data,
    pf.valorPago AS pagamento_valor,
    pf.origemPagamento AS pagamento_origem,
    pf.referencia AS pagamento_referencia,
    pf.estado AS pagamento_estado,
    mp.nome AS metodo_pagamento,
    up.id AS usuario_pagamento_id,
    up.nome AS usuario_pagamento_nome,
    up.usuario AS usuario_pagamento_login

FROM conta_pagar cp
JOIN fornecedor f
    ON f.id = cp.idFornecedor
JOIN factura_fornecedor ff
    ON ff.id = cp.idFacturaFornecedor
LEFT JOIN item_factura_fornecedor iff
    ON iff.idFacturaFornecedor = ff.id
LEFT JOIN produto p
    ON p.id = iff.idProduto
LEFT JOIN pagamento_fornecedor pf
    ON pf.idContaPagar = cp.id
LEFT JOIN metodo_pagamento mp
    ON mp.id = pf.idMetodoPagamento
LEFT JOIN usuario up
    ON up.id = pf.idUsuario
WHERE (@id_fornecedor IS NULL OR f.id = @id_fornecedor)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@estado IS NULL OR cp.estado = @estado OR ff.estado = @estado OR pf.estado = @estado)
  AND (@data_inicio IS NULL OR cp.dataEmissao >= @data_inicio)
  AND (@data_fim IS NULL OR cp.dataEmissao <= @data_fim)
ORDER BY cp.dataVencimento, f.razaosocial, ff.numero, p.nome, pf.dataPagamento;

-- ============================================================
-- 8. RELATORIO DE STOCKS E MOVIMENTOS
-- ============================================================
-- Uma linha por movimento.
-- Inclui produto, armazem, lote, stock actual e detalhe de movimento_lote.

SELECT
    m.id AS movimento_id,
    m.tipoOrigem AS movimento_origem,
    m.tipoMovimento AS movimento_tipo,
    m.dataMovimento AS movimento_data,
    m.quantidade AS movimento_quantidade,

    p.id AS produto_id,
    p.nome AS produto_nome,
    p.codigo_barra AS produto_codigo_barra,

    a.id AS armazem_id,
    a.nome AS armazem_nome,
    a.tipo AS armazem_tipo,

    s.saldo AS stock_saldo_actual,
    s.reservado AS stock_reservado_actual,
    s.disponivel AS stock_disponivel_actual,

    l.id AS lote_id,
    l.codigo AS lote_codigo,
    l.quantidadeAtual AS lote_quantidade_actual,
    l.custoUnitario AS lote_custo_unitario,
    l.estado AS lote_estado,

    ml.id AS movimento_lote_id,
    ml.quantidadeRetirada AS movimento_lote_quantidade_retirada,

    ir.id AS item_requisicao_id,
    r.id AS requisicao_id,
    r.estado AS requisicao_estado

FROM movimento m
JOIN produto p
    ON p.id = m.idProduto
JOIN armazem a
    ON a.id = m.idArmazem
LEFT JOIN stock s
    ON s.idProduto = m.idProduto
   AND s.idArmazem = m.idArmazem
LEFT JOIN lote l
    ON l.id = m.idLote
LEFT JOIN movimento_lote ml
    ON ml.idMovimento = m.id
LEFT JOIN itemRequisicao ir
    ON ir.id = m.idItemRequisicao
LEFT JOIN requisicao r
    ON r.id = ir.idRequisicao
WHERE (@id_armazem IS NULL OR a.id = @id_armazem)
  AND (@id_produto IS NULL OR p.id = @id_produto)
  AND (@estado IS NULL OR m.tipoOrigem = @estado OR m.tipoMovimento = @estado)
  AND (@data_inicio IS NULL OR m.dataMovimento >= @data_inicio)
  AND (@data_fim IS NULL OR m.dataMovimento <= @data_fim)
ORDER BY m.dataMovimento, m.id;

-- ============================================================
-- FIM
-- ============================================================
