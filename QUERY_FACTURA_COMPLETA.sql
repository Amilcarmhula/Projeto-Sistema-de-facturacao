-- ============================================================
-- QUERY COMPLETA DE FACTURA
-- JM-InvSM / Facturacao
-- ============================================================
--
-- Uso:
--   SET @numero_factura = 'FT-0001';
--   Executar o SELECT abaixo.
--
-- Retorno:
--   Uma linha por item da factura, repetindo os dados de cabecalho.
--   Ideal para impressao, JasperReport ou montagem de DTO de factura.
--
-- Observacao:
--   Esta versao usa apenas as colunas presentes nas tabelas informadas.
--   Se o script de delete logico ja foi aplicado, pode acrescentar filtros
--   "AND tabela.deleteStatus = 0" nos JOINs/WHERE.
--
-- Nota sobre collations:
--   Algumas instalacoes misturam utf8mb4_unicode_ci e utf8mb4_0900_ai_ci
--   entre factura.numero e itemfactura.fk_numero. Por isso a comparacao do
--   numero da factura usa COLLATE utf8mb4_unicode_ci explicitamente.
-- ============================================================

SET @numero_factura = 'INFORME_AQUI_O_NUMERO';

SELECT
    -- Documento comercial
    dc.id AS doc_id,
    dc.sigla AS doc_sigla,
    dc.nome AS doc_nome,
    dc.diasuteis AS doc_dias_uteis,
    dc.descricao AS doc_descricao,

    -- Factura
    f.numero AS factura_numero,
    f.data_emissao AS factura_data_emissao,
    f.data_vencimento AS factura_data_vencimento,
    f.total AS factura_total,
    f.totalPago AS factura_total_pago,
    f.saldoPendente AS factura_saldo_pendente,
    f.estado AS factura_estado,
    f.condicaoPagemento AS factura_condicao_pagamento,
    f.numeroParcelas AS factura_numero_parcelas,
    f.fk_id_empresa AS factura_id_empresa,
    f.fk_id_usuario AS factura_id_usuario,

    -- Cliente
    c.id AS cliente_id,
    c.tipo AS cliente_tipo,
    c.nome AS cliente_nome,
    c.razaosocial AS cliente_razao_social,
    c.nuit AS cliente_nuit,
    c.data_criacao AS cliente_data_criacao,

    -- Endereco principal do cliente para facturacao
    ec.id AS endereco_id,
    ec.tipo AS endereco_tipo,
    ec.provincia AS endereco_provincia,
    ec.cidade AS endereco_cidade,
    ec.bairro AS endereco_bairro,
    ec.avenida AS endereco_avenida,
    ec.rua AS endereco_rua,
    ec.numero AS endereco_numero,
    ec.codigo_postal AS endereco_codigo_postal,

    -- Contacto principal do cliente
    cc.id AS contacto_id,
    cc.pessoa_de_contacto AS contacto_pessoa,
    cc.contacto AS contacto_telefone,
    cc.email AS contacto_email,
    cc.website AS contacto_website,

    -- Item da factura
    it.id AS item_id,
    it.quantidade AS item_quantidade,
    it.qtd_por_unidade AS item_qtd_por_unidade,
    (it.quantidade * COALESCE(it.qtd_por_unidade, 1)) AS item_quantidade_total_unidades,
    it.subtotal AS item_subtotal,
    it.data_adicao AS item_data_adicao,

    -- Produto
    p.id AS produto_id,
    p.nome AS produto_nome,
    p.codigo_barra AS produto_codigo_barra,
    p.tipoProduto AS produto_tipo,
    p.unidadePorTipo AS produto_unidade_por_tipo,
    p.descricao AS produto_descricao,
    p.controle_stock AS produto_controle_stock,
    p.nivelstock AS produto_nivel_stock,

    -- Armazem do item
    a.id AS armazem_id,
    a.nome AS armazem_nome,
    a.tipo AS armazem_tipo,
    a.descricao AS armazem_descricao,

    -- Imposto do produto/item
    imp.id AS imposto_id,
    imp.nome AS imposto_nome,
    imp.percentagem AS imposto_percentagem,
    imp.descricao AS imposto_descricao,

    -- Preco activo do produto no armazem da factura
    ppa.id AS preco_id,
    ppa.precoBase AS preco_base,
    ppa.precoVenda AS preco_venda,
    ppa.precoFinal AS preco_final,
    ppa.dataInicioVigencia AS preco_data_inicio_vigencia,
    ppa.dataFimVigencia AS preco_data_fim_vigencia,
    ppa.estado AS preco_estado,

    -- Desconto activo do produto no armazem da factura
    d.id AS desconto_id,
    d.nome AS desconto_nome,
    d.percentagem AS desconto_percentagem,
    d.descricao AS desconto_descricao,
    pd.dataInicio AS desconto_data_inicio,
    pd.dataFim AS desconto_data_fim,
    pd.estado AS desconto_estado,

    -- Valores calculados de apoio
    ROUND(COALESCE(ppa.precoBase, 0) * it.quantidade, 2) AS valor_base_item,
    ROUND(COALESCE(ppa.precoVenda, 0) * it.quantidade, 2) AS valor_venda_item,
    ROUND(COALESCE(ppa.precoFinal, it.subtotal) * it.quantidade, 2) AS valor_final_calculado_item,
    ROUND(
        (COALESCE(ppa.precoVenda, 0) * it.quantidade)
        * (COALESCE(d.percentagem, 0) / 100),
        2
    ) AS valor_desconto_calculado,
    ROUND(
        (COALESCE(ppa.precoBase, 0) * it.quantidade)
        * (COALESCE(imp.percentagem, 0) / 100),
        2
    ) AS valor_imposto_calculado

FROM factura f
JOIN documentocomercial dc
    ON dc.id = f.fk_id_doc
JOIN cliente c
    ON c.id = f.fk_id_cliente
LEFT JOIN endereco_cliente ec
    ON ec.id = (
        SELECT ec2.id
        FROM endereco_cliente ec2
        WHERE ec2.fk_id_cliente = c.id
        ORDER BY
            CASE WHEN ec2.tipo = 'Facturacao' THEN 0 ELSE 1 END,
            ec2.id
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
JOIN itemfactura it
    ON CONVERT(it.fk_numero USING utf8mb4) COLLATE utf8mb4_unicode_ci
     = CONVERT(f.numero USING utf8mb4) COLLATE utf8mb4_unicode_ci
JOIN produto p
    ON p.id = it.fk_id_produto
JOIN armazem a
    ON a.id = it.fk_id_armazem
LEFT JOIN imposto imp
    ON imp.id = p.idImposto
LEFT JOIN precoProdutoArmazem ppa
    ON ppa.id = (
        SELECT ppa2.id
        FROM precoProdutoArmazem ppa2
        WHERE ppa2.idProduto = it.fk_id_produto
          AND ppa2.idArmazem = it.fk_id_armazem
          AND ppa2.estado = 'activo'
        ORDER BY ppa2.dataInicioVigencia DESC, ppa2.id DESC
        LIMIT 1
    )
LEFT JOIN produtodesconto pd
    ON pd.id = (
        SELECT pd2.id
        FROM produtodesconto pd2
        WHERE pd2.idProduto = it.fk_id_produto
          AND pd2.idArmazem = it.fk_id_armazem
          AND pd2.estado = TRUE
          AND f.data_emissao BETWEEN pd2.dataInicio AND pd2.dataFim
        ORDER BY pd2.dataInicio DESC, pd2.id DESC
        LIMIT 1
    )
LEFT JOIN desconto d
    ON d.id = pd.idDesconto
WHERE CONVERT(f.numero USING utf8mb4) COLLATE utf8mb4_unicode_ci
    = CONVERT(@numero_factura USING utf8mb4) COLLATE utf8mb4_unicode_ci
ORDER BY it.id;

-- Diagnostico opcional para confirmar collations das colunas principais:
-- SELECT TABLE_NAME, COLUMN_NAME, CHARACTER_SET_NAME, COLLATION_NAME
-- FROM information_schema.COLUMNS
-- WHERE TABLE_SCHEMA = DATABASE()
--   AND (
--       (TABLE_NAME = 'factura' AND COLUMN_NAME = 'numero')
--       OR (TABLE_NAME = 'itemfactura' AND COLUMN_NAME = 'fk_numero')
--   );
