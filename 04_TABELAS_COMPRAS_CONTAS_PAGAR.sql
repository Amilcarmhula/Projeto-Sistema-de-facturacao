-- ============================================================
-- MODULO DE COMPRAS, RECEBIMENTO E CONTAS A PAGAR
-- Sistema stand-alone JM-InvSM
-- ============================================================
--
-- Este script cria apenas as novas tabelas do fluxo:
--
-- Requisicao
--   -> ordem_compra
--   -> recebimento_compra
--   -> entrada no stock por lote/movimento
--   -> factura_fornecedor
--   -> conta_pagar
--   -> pagamento_fornecedor
--   -> movimento_financeiro
--
-- Regras base:
-- - Requisicao de compra nao actualiza stock/caixa/banco/contas a pagar.
-- - Ordem de compra nao actualiza stock/caixa/banco/contas a pagar.
-- - Recebimento actualiza stock apenas quando confirmado por procedure/trigger.
-- - Factura do fornecedor cria a obrigacao em conta_pagar.
-- - Pagamento ao fornecedor baixa caixa ou banco e liquida/reduz conta_pagar.
--
-- Observacao:
-- Este script nao cria triggers/procedures. Elas devem ser criadas num script
-- separado apos validarmos esta estrutura.
-- ============================================================
USE _sysface_teste;


SET NAMES utf8mb4;

-- ============================================================
-- 1. ORDEM DE COMPRA
-- ============================================================

CREATE TABLE IF NOT EXISTS ordem_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(30) NOT NULL UNIQUE,
    idRequisicao INT NULL,
    idFornecedor INT NOT NULL,
    dataEmissao DATE NOT NULL DEFAULT (CURRENT_DATE),
    dataPrevistaEntrega DATE NULL,
    estado ENUM(
        'Rascunho',
        'Submetida',
        'Aprovada',
        'Enviada',
        'ParcialmenteRecebida',
        'Recebida',
        'Cancelada'
    ) NOT NULL DEFAULT 'Rascunho',
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    desconto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    imposto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(12,2) GENERATED ALWAYS AS (subtotal - desconto + imposto) STORED,
    observacao TEXT,
    idUsuario INT NULL,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataActualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_ordem_compra_requisicao (idRequisicao),
    KEY idx_ordem_compra_fornecedor (idFornecedor),
    FOREIGN KEY (idRequisicao) REFERENCES requisicao(id),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    CONSTRAINT chk_ordem_compra_valores CHECK (
        subtotal >= 0 AND desconto >= 0 AND imposto >= 0
    )
);

CREATE TABLE IF NOT EXISTS item_ordem_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idOrdemCompra INT NOT NULL,
    idItemRequisicao INT NULL,
    idProduto INT NOT NULL,
    idArmazem INT NOT NULL,
    quantidadeSolicitada INT NOT NULL,
    quantidadeRecebida INT NOT NULL DEFAULT 0,
    custoUnitario DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    desconto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    imposto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(12,2) GENERATED ALWAYS AS (quantidadeSolicitada * custoUnitario) STORED,
    totalLinha DECIMAL(12,2) GENERATED ALWAYS AS ((quantidadeSolicitada * custoUnitario) - desconto + imposto) STORED,
    estado ENUM(
        'Pendente',
        'ParcialmenteRecebido',
        'Recebido',
        'Cancelado'
    ) NOT NULL DEFAULT 'Pendente',
    KEY idx_item_ordem_compra_produto (idProduto, idArmazem),
    FOREIGN KEY (idOrdemCompra) REFERENCES ordem_compra(id) ON DELETE CASCADE,
    FOREIGN KEY (idItemRequisicao) REFERENCES itemRequisicao(id),
    FOREIGN KEY (idProduto) REFERENCES produto(id),
    FOREIGN KEY (idArmazem) REFERENCES armazem(id),
    CONSTRAINT chk_item_ordem_compra_quantidade CHECK (
        quantidadeSolicitada > 0
        AND quantidadeRecebida >= 0
        AND quantidadeRecebida <= quantidadeSolicitada
    ),
    CONSTRAINT chk_item_ordem_compra_valores CHECK (
        custoUnitario >= 0 AND desconto >= 0 AND imposto >= 0
    )
);

-- ============================================================
-- 2. RECEBIMENTO DE COMPRA
-- ============================================================
CREATE TABLE IF NOT EXISTS recebimento_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(30) NOT NULL UNIQUE,
    idOrdemCompra INT NOT NULL,
    idFornecedor INT NOT NULL,
    dataRecebimento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    documentoFornecedor VARCHAR(50),
    estado ENUM(
        'Rascunho',
        'Confirmado',
        'Cancelado'
    ) NOT NULL DEFAULT 'Rascunho',
    totalRecebido DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    observacao TEXT,
    idUsuario INT NULL,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataActualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_recebimento_compra_ordem (idOrdemCompra),
    FOREIGN KEY (idOrdemCompra) REFERENCES ordem_compra(id),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    CONSTRAINT chk_recebimento_compra_valor CHECK (totalRecebido >= 0)
);

CREATE TABLE IF NOT EXISTS item_recebimento_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idRecebimentoCompra INT NOT NULL,
    idItemOrdemCompra INT NULL,
    idProduto INT NOT NULL,
    idArmazem INT NOT NULL,
    quantidadeRecebida INT NOT NULL,
    custoUnitario DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    codigoLote VARCHAR(20),
    idLote INT NULL,
    subtotal DECIMAL(12,2) GENERATED ALWAYS AS (quantidadeRecebida * custoUnitario) STORED,
    estado ENUM(
        'Pendente',
        'Confirmado',
        'Cancelado'
    ) NOT NULL DEFAULT 'Pendente',
    KEY idx_item_recebimento_compra_lote (idLote),
    FOREIGN KEY (idRecebimentoCompra) REFERENCES recebimento_compra(id) ON DELETE CASCADE,
    FOREIGN KEY (idItemOrdemCompra) REFERENCES item_ordem_compra(id),
    FOREIGN KEY (idProduto) REFERENCES produto(id),
    FOREIGN KEY (idArmazem) REFERENCES armazem(id),
    FOREIGN KEY (idLote) REFERENCES lote(id),
    CONSTRAINT chk_item_recebimento_quantidade CHECK (quantidadeRecebida > 0),
    CONSTRAINT chk_item_recebimento_valor CHECK (custoUnitario >= 0)
);

-- ============================================================
-- 3. FACTURA DO FORNECEDOR
-- ============================================================
CREATE TABLE IF NOT EXISTS factura_fornecedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(50) NOT NULL,
    idFornecedor INT NOT NULL,
    idOrdemCompra INT NULL,
    idRecebimentoCompra INT NULL,
    dataEmissao DATE NOT NULL,
    dataVencimento DATE NULL,
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    desconto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    imposto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(12,2) GENERATED ALWAYS AS (subtotal - desconto + imposto) STORED,
    valorPago DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    saldoPendente DECIMAL(12,2) GENERATED ALWAYS AS ((subtotal - desconto + imposto) - valorPago) STORED,
    estado ENUM(
        'Pendente',
        'ParcialmentePaga',
        'Paga',
        'Cancelada'
    ) NOT NULL DEFAULT 'Pendente',
    observacao TEXT,
    idUsuario INT NULL,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataActualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_factura_fornecedor_numero (numero, idFornecedor),
    KEY idx_factura_fornecedor_estado (estado),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id),
    FOREIGN KEY (idOrdemCompra) REFERENCES ordem_compra(id),
    FOREIGN KEY (idRecebimentoCompra) REFERENCES recebimento_compra(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    CONSTRAINT chk_factura_fornecedor_valores CHECK (
        subtotal >= 0
        AND desconto >= 0
        AND imposto >= 0
        AND valorPago >= 0
        AND valorPago <= (subtotal - desconto + imposto)
    )
);
CREATE TABLE IF NOT EXISTS item_factura_fornecedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idFacturaFornecedor INT NOT NULL,
    idItemRecebimentoCompra INT NULL,
    idProduto INT NOT NULL,
    descricao VARCHAR(255),
    quantidade INT NOT NULL,
    custoUnitario DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    desconto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    imposto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(12,2) GENERATED ALWAYS AS (quantidade * custoUnitario) STORED,
    totalLinha DECIMAL(12,2) GENERATED ALWAYS AS ((quantidade * custoUnitario) - desconto + imposto) STORED,
    FOREIGN KEY (idFacturaFornecedor) REFERENCES factura_fornecedor(id) ON DELETE CASCADE,
    FOREIGN KEY (idItemRecebimentoCompra) REFERENCES item_recebimento_compra(id),
    FOREIGN KEY (idProduto) REFERENCES produto(id),
    CONSTRAINT chk_item_factura_fornecedor_quantidade CHECK (quantidade > 0),
    CONSTRAINT chk_item_factura_fornecedor_valores CHECK (
        custoUnitario >= 0 AND desconto >= 0 AND imposto >= 0
    )
);

-- ============================================================
-- 4. CONTAS A PAGAR E PAGAMENTOS AO FORNECEDOR
-- ============================================================

CREATE TABLE IF NOT EXISTS conta_pagar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idFacturaFornecedor INT NOT NULL UNIQUE,
    idFornecedor INT NOT NULL,
    dataEmissao DATE NOT NULL,
    dataVencimento DATE NULL,
    valorOriginal DECIMAL(12,2) NOT NULL,
    valorPago DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    saldoPendente DECIMAL(12,2) GENERATED ALWAYS AS (valorOriginal - valorPago) STORED,
    estado ENUM(
        'Aberta',
        'Parcial',
        'Liquidada',
        'Vencida',
        'Cancelada'
    ) NOT NULL DEFAULT 'Aberta',
    observacao TEXT,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataActualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_conta_pagar_estado (estado),
    FOREIGN KEY (idFacturaFornecedor) REFERENCES factura_fornecedor(id),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id),
    CONSTRAINT chk_conta_pagar_valores CHECK (
        valorOriginal >= 0
        AND valorPago >= 0
        AND valorPago <= valorOriginal
    )
);

CREATE TABLE IF NOT EXISTS pagamento_fornecedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idContaPagar INT NOT NULL,
    idFornecedor INT NOT NULL,
    dataPagamento DATE NOT NULL,
    valorPago DECIMAL(12,2) NOT NULL,
    idMetodoPagamento INT NOT NULL,
    origemPagamento ENUM('Caixa','Banco') NOT NULL,
    idContaBancaria INT NULL,
    referencia VARCHAR(80),
    descricao TEXT,
    estado ENUM(
        'Confirmado',
        'Cancelado'
    ) NOT NULL DEFAULT 'Confirmado',
    idUsuario INT NULL,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    KEY idx_pagamento_fornecedor_conta (idContaPagar),
    FOREIGN KEY (idContaPagar) REFERENCES conta_pagar(id),
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor(id),
    FOREIGN KEY (idMetodoPagamento) REFERENCES metodo_pagamento(id),
    FOREIGN KEY (idContaBancaria) REFERENCES contabancaria(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    CONSTRAINT chk_pagamento_fornecedor_valor CHECK (valorPago > 0),
    CONSTRAINT chk_pagamento_fornecedor_origem CHECK (
        (origemPagamento = 'Banco' AND idContaBancaria IS NOT NULL)
        OR origemPagamento = 'Caixa'
    )
);

-- ============================================================
-- 5. MOVIMENTO FINANCEIRO
-- ============================================================

CREATE TABLE IF NOT EXISTS movimento_financeiro (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dataMovimento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipoMovimento ENUM('Entrada','Saida') NOT NULL,
    origem ENUM(
        'Venda',
        'PagamentoFornecedor',
        'Ajuste',
        'Transferencia',
        'Outro'
    ) NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    origemPagamento ENUM('Caixa','Banco') NOT NULL,
    idContaBancaria INT NULL,
    idTransacao INT NULL,
    idPagamentoFornecedor INT NULL,
    saldoAnterior DECIMAL(12,2) NULL,
    saldoPosterior DECIMAL(12,2) NULL,
    descricao TEXT,
    idUsuario INT NULL,
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    KEY idx_movimento_financeiro_origem (origem, tipoMovimento),
    FOREIGN KEY (idContaBancaria) REFERENCES contabancaria(id),
    FOREIGN KEY (idTransacao) REFERENCES transacoes(id),
    FOREIGN KEY (idPagamentoFornecedor) REFERENCES pagamento_fornecedor(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id),
    CONSTRAINT chk_movimento_financeiro_valor CHECK (valor > 0),
    CONSTRAINT chk_movimento_financeiro_vinculo CHECK (
        idTransacao IS NOT NULL
        OR idPagamentoFornecedor IS NOT NULL
        OR origem IN ('Ajuste', 'Transferencia', 'Outro')
    ),
    CONSTRAINT chk_movimento_financeiro_banco CHECK (
        (origemPagamento = 'Banco' AND idContaBancaria IS NOT NULL)
        OR origemPagamento = 'Caixa'
    )
);

-- ============================================================
-- FIM DO SCRIPT
-- ============================================================
