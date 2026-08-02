USE _sysface_teste;

-- ============================================================
-- VALIDACAO DO MODULO DE REQUISICOES
-- Execute apenas numa base de teste.
-- O bloco abaixo limpa dados operacionais usados pelo teste.
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

-- Dados minimos.
INSERT INTO empresa (id, nome, razaosocial, nuit)
VALUES (1, 'Empresa Teste', 'Empresa Teste Lda', 123456789);

INSERT INTO nivel_acesso (id, descricao)
VALUES (1, 'Administrador');

INSERT INTO permissao (id, tipo, descricao)
VALUES (1, 'CRUD', 'Todas permissoes');

INSERT INTO usuario (id, nome, email, usuario, senha, estado, fk_nivel_acesso, fk_permissao)
VALUES (1, 'Utilizador Teste', 'req@local.test', 'reqteste', '123', 1, 1, 1);

INSERT INTO fornecedor (id, tipo, razaosocial, nuit)
VALUES (1, 'Juridica', 'Fornecedor Teste', 987654321);

INSERT INTO armazem (id, nome, tipo, descricao, estado)
VALUES (1, 'Armazem Central', 'Central', 'Teste', 1);

INSERT INTO categoria (id, grupo, subgrupo, familia, descricao)
VALUES (1, 'Geral', 'Geral', 'Geral', 'Categoria de teste');

INSERT INTO unidadeMedida (id, nome, sigla, descricao)
VALUES (1, 'Unidade', 'UN', 'Unidade simples');

INSERT INTO imposto (id, nome, percentagem, descricao)
VALUES (1, 'IVA', 17.000, 'IVA de teste');

INSERT INTO produto (id, nome, codigo_barra, tipoProduto, unidadePorTipo, descricao, controle_stock, nivelstock, idCategoria, idUnidade, idImposto)
VALUES (1, 'Produto Requisicao', 'REQ001', 'Simples', 1, 'Produto para teste de requisicao', 1, 0, 1, 1, 1);

INSERT INTO stock (idProduto, idArmazem, saldo, reservado)
VALUES (1, 1, 0, 0)
ON DUPLICATE KEY UPDATE saldo = 0, reservado = 0;

INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
VALUES ('REQ-LOTE-001', 1, 1, 1, CURDATE(), 100, 10.00);

SELECT
    'SETUP - Stock apos entrada' AS teste,
    s.idProduto,
    s.idArmazem,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- 1. Criar requisicao pendente.
INSERT INTO requisicao (id, dataNecessidade, estado, motivo)
VALUES (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'Pendente', 'Teste completo de requisicao');

-- 2. Adicionar item deve mudar estado para Submetida.
INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES (30, 1, 1, 1);

SELECT
    'TESTE 1 - Estado apos item' AS teste,
    r.id,
    r.estado
FROM requisicao r
WHERE r.id = 1;

-- 3. Aprovar deve reservar stock e criar picking.
CALL AprovarRequisicao(1);


SELECT
    'TESTE 2 - Requisicao aprovada' AS teste,
    r.id,
    r.estado
FROM requisicao r
WHERE r.id = 1;


SELECT
    'TESTE 2 - Reserva criada' AS teste,
    rs.idProduto,
    rs.idArmazem,
    rs.quantidade,
    rs.estado
FROM reserva_stock rs
WHERE rs.idRequisicao = 1;

SELECT
    'TESTE 2 - Stock reservado' AS teste,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

SELECT
    'TESTE 2 - Picking criado' AS teste,
    p.tipo,
    p.estado,
    p.idRequisicao
FROM picking p
WHERE p.idRequisicao = 1;

-- 3. Iniciar picking muda a requisicao para EmPicking.
UPDATE picking
SET estado = 'Separando',
    operador = 1,
    dataInicio = COALESCE(dataInicio, NOW()),
    dataFim = NULL
WHERE idRequisicao = 1;

UPDATE requisicao
SET estado = 'EmPicking'
WHERE id = 1;

SELECT
    'TESTE 3 - Picking iniciado' AS teste,
    r.id,
    r.estado AS estadoRequisicao,
    p.estado AS estadoPicking
FROM requisicao r
JOIN picking p ON p.idRequisicao = r.id
WHERE r.id = 1;

-- 4. Confirmar picking deve consumir reserva, baixar stock e lotes.
CALL ConfirmarPicking(1);

SELECT
    'TESTE 4 - Requisicao finalizada' AS teste,
    r.id,
    r.estado
FROM requisicao r
WHERE r.id = 1;

SELECT
    'TESTE 4 - Reserva consumida' AS teste,
    rs.quantidade,
    rs.estado
FROM reserva_stock rs
WHERE rs.idRequisicao = 1;

SELECT
    'TESTE 4 - Picking confirmado' AS teste,
    p.estado,
    p.dataFim
FROM picking p
WHERE p.idRequisicao = 1;

SELECT
    'TESTE 4 - Movimento de saida' AS teste,
    m.tipoOrigem,
    m.tipoMovimento,
    m.quantidade,
    m.idProduto,
    m.idArmazem
FROM movimento m
WHERE m.tipoOrigem = 'Requisicao'
ORDER BY m.id DESC
LIMIT 1;


SELECT
    'TESTE 4 - Movimento/lote FIFO' AS teste,
    ml.idMovimento,
    ml.idLote,
    ml.quantidadeRetirada
FROM movimento_lote ml
JOIN movimento m ON m.id = ml.idMovimento
WHERE m.tipoOrigem = 'Requisicao'
ORDER BY ml.id DESC
LIMIT 1;

SELECT
    'TESTE 4 - Lote apos requisicao' AS teste,
    l.codigo,
    l.quantidadeAtual,
    l.estado
FROM lote l
WHERE l.codigo = 'REQ-LOTE-001';

SELECT
    'TESTE 4 - Stock final' AS teste,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- 5. Cancelar uma requisicao aprovada antes de finalizar deve libertar reserva.
INSERT INTO requisicao (id, dataNecessidade, estado, motivo)
VALUES (2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Pendente', 'Teste de cancelamento com reserva');

INSERT INTO itemRequisicao (quantidade, idRequisicao, idProduto, idArmazem)
VALUES (20, 2, 1, 1);

CALL AprovarRequisicao(2);

SELECT
    'TESTE 4 - Antes de cancelar' AS teste,
    r.id,
    r.estado,
    s.saldo,
    s.reservado,
    s.disponivel
FROM requisicao r
JOIN stock s ON s.idProduto = 1 AND s.idArmazem = 1
WHERE r.id = 2;

CALL CancelarRequisicao(2);

SELECT
    'TESTE 4 - Requisicao cancelada' AS teste,
    r.id,
    r.estado
FROM requisicao r
WHERE r.id = 2;

SELECT
    'TESTE 4 - Reserva cancelada' AS teste,
    rs.quantidade,
    rs.estado
FROM reserva_stock rs
WHERE rs.idRequisicao = 2;

SELECT
    'TESTE 4 - Stock reservado revertido' AS teste,
    s.saldo,
    s.reservado,
    s.disponivel
FROM stock s
WHERE s.idProduto = 1 AND s.idArmazem = 1;

-- Resultado esperado:
-- Estado final: Finalizada
-- Reserva: Consumido
-- Picking: Confirmado
-- Stock: saldo 70, reservado 0, disponivel 70
-- Lote: quantidadeAtual 70
-- Movimento: Requisicao/Saida com quantidade 30
-- Cancelamento: requisicao 2 Cancelada, reserva Cancelado, stock.reservado 0
