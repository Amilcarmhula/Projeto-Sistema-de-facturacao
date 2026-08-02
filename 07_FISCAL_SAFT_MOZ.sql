-- ============================================================================
-- JM-InvSM - Camada fiscal para comunicação mensal e SAF-T (Moz)
-- Versão: JM-AT-BASE-1.0
-- IMPORTANTE:
--   1) Este script implementa o modelo técnico provisório do JM-InvSM.
--   2) Os nomes/códigos devem ser revistos quando a AT fornecer a
--      especificação oficial e o respectivo XSD.
--   3) Execute primeiro numa cópia de segurança e num ambiente de testes.
-- ============================================================================

USE _sysface_teste;

-- NUIT é identificador, não quantidade. VARCHAR preserva zeros e evita
-- operações aritméticas indevidas.
ALTER TABLE empresa MODIFY nuit VARCHAR(20) NULL;
ALTER TABLE cliente MODIFY nuit VARCHAR(20) NULL;
ALTER TABLE fornecedor MODIFY nuit VARCHAR(20) NULL;

-- MySQL não suporta ADD COLUMN IF NOT EXISTS em todas as versões 8.x.
-- Esta rotina torna a migração repetível consultando information_schema.
DROP PROCEDURE IF EXISTS sp_jm_add_column;
DELIMITER $$
CREATE PROCEDURE sp_jm_add_column(
    IN p_table VARCHAR(64),
    IN p_column VARCHAR(64),
    IN p_definition TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = p_table
          AND COLUMN_NAME = p_column
    ) THEN
        SET @ddl_column = CONCAT(
            'ALTER TABLE `', REPLACE(p_table, '`', '``'),
            '` ADD COLUMN `', REPLACE(p_column, '`', '``'),
            '` ', p_definition
        );
        PREPARE stmt_column FROM @ddl_column;
        EXECUTE stmt_column;
        DEALLOCATE PREPARE stmt_column;
    END IF;
END$$
DELIMITER ;

CALL sp_jm_add_column('factura', 'serie', 'VARCHAR(20) NULL AFTER numero');
CALL sp_jm_add_column('factura', 'data_hora_registo', 'DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER data_emissao');
CALL sp_jm_add_column('factura', 'moeda', 'CHAR(3) NOT NULL DEFAULT ''MZN'' AFTER data_hora_registo');
CALL sp_jm_add_column('factura', 'taxa_cambio', 'DECIMAL(18,6) NOT NULL DEFAULT 1.000000 AFTER moeda');
CALL sp_jm_add_column('factura', 'total_liquido', 'DECIMAL(18,2) NULL AFTER total');
CALL sp_jm_add_column('factura', 'total_imposto', 'DECIMAL(18,2) NULL AFTER total_liquido');
CALL sp_jm_add_column('factura', 'documento_origem', 'VARCHAR(50) NULL AFTER estado');
CALL sp_jm_add_column('factura', 'motivo_anulacao', 'VARCHAR(255) NULL AFTER documento_origem');
CALL sp_jm_add_column('factura', 'data_anulacao', 'DATETIME NULL AFTER motivo_anulacao');
CALL sp_jm_add_column('factura', 'id_usuario_anulacao', 'INT NULL AFTER data_anulacao');
CALL sp_jm_add_column('factura', 'hash_documento', 'VARCHAR(64) NULL AFTER id_usuario_anulacao');

SET @fk_anulacao = (
    SELECT IF(COUNT(*) = 0,
        'ALTER TABLE factura ADD CONSTRAINT fk_factura_usuario_anulacao FOREIGN KEY (id_usuario_anulacao) REFERENCES usuario(id)',
        'SELECT 1')
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'factura'
      AND CONSTRAINT_NAME = 'fk_factura_usuario_anulacao'
);
PREPARE stmt_fk_anulacao FROM @fk_anulacao;
EXECUTE stmt_fk_anulacao;
DEALLOCATE PREPARE stmt_fk_anulacao;

CALL sp_jm_add_column('itemfactura', 'numero_linha', 'INT NULL AFTER id');
CALL sp_jm_add_column('itemfactura', 'unidade_vendida', 'VARCHAR(20) NULL AFTER qtd_por_unidade');
CALL sp_jm_add_column('itemfactura', 'preco_unitario', 'DECIMAL(18,6) NULL AFTER unidade_vendida');
CALL sp_jm_add_column('itemfactura', 'desconto_linha', 'DECIMAL(18,2) NOT NULL DEFAULT 0.00 AFTER preco_unitario');
CALL sp_jm_add_column('itemfactura', 'codigo_imposto', 'VARCHAR(20) NULL AFTER desconto_linha');
CALL sp_jm_add_column('itemfactura', 'taxa_imposto', 'DECIMAL(9,4) NULL AFTER codigo_imposto');
CALL sp_jm_add_column('itemfactura', 'motivo_isencao', 'VARCHAR(255) NULL AFTER taxa_imposto');
CALL sp_jm_add_column('itemfactura', 'valor_liquido', 'DECIMAL(18,2) NULL AFTER motivo_isencao');
CALL sp_jm_add_column('itemfactura', 'base_tributavel', 'DECIMAL(18,2) NULL AFTER valor_liquido');
CALL sp_jm_add_column('itemfactura', 'valor_imposto', 'DECIMAL(18,2) NULL AFTER base_tributavel');
CALL sp_jm_add_column('itemfactura', 'valor_bruto', 'DECIMAL(18,2) NULL AFTER valor_imposto');
CALL sp_jm_add_column('itemfactura', 'total_linha', 'DECIMAL(18,2) NULL AFTER valor_bruto');

DROP PROCEDURE IF EXISTS sp_jm_add_column;

CREATE TABLE IF NOT EXISTS fiscal_exportacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id INT NOT NULL,
    periodo_inicio DATE NOT NULL,
    periodo_fim DATE NOT NULL,
    formato ENUM('CSV','XLSX','SAFT_XML') NOT NULL,
    versao_modelo VARCHAR(30) NOT NULL DEFAULT 'JM-AT-BASE-1.0',
    nome_ficheiro VARCHAR(255) NOT NULL,
    hash_sha256 CHAR(64) NOT NULL,
    quantidade_documentos INT NOT NULL,
    total_documentos DECIMAL(18,2) NOT NULL,
    estado ENUM('GERADO','VALIDADO','COM_ERROS','SUBMETIDO','REJEITADO') NOT NULL,
    erros_validacao INT NOT NULL DEFAULT 0,
    avisos_validacao INT NOT NULL DEFAULT 0,
    gerado_por INT NULL,
    data_geracao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_submissao DATETIME NULL,
    referencia_submissao VARCHAR(100) NULL,
    FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    FOREIGN KEY (gerado_por) REFERENCES usuario(id),
    INDEX idx_fiscal_exportacao_periodo (empresa_id, periodo_inicio, periodo_fim)
);

CREATE TABLE IF NOT EXISTS fiscal_auditoria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entidade VARCHAR(50) NOT NULL,
    identificador VARCHAR(100) NOT NULL,
    operacao VARCHAR(30) NOT NULL,
    detalhe TEXT NULL,
    utilizador_id INT NULL,
    data_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilizador_id) REFERENCES usuario(id),
    INDEX idx_fiscal_auditoria_entidade (entidade, identificador),
    INDEX idx_fiscal_auditoria_data (data_evento)
);

-- Regista numeração de linhas em documentos antigos. Não tenta inventar
-- impostos/preços históricos; esses registos serão sinalizados pelo validador.
DROP TEMPORARY TABLE IF EXISTS tmp_jm_numero_linha;
CREATE TEMPORARY TABLE tmp_jm_numero_linha AS
SELECT id, rn
FROM (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY fk_numero ORDER BY id) AS rn
    FROM itemfactura
) dados_numerados;

ALTER TABLE tmp_jm_numero_linha ADD PRIMARY KEY (id);

UPDATE itemfactura i
JOIN tmp_jm_numero_linha numeracao ON numeracao.id = i.id
SET i.numero_linha = numeracao.rn
WHERE i.numero_linha IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_jm_numero_linha;

UPDATE factura
SET serie = CASE
    WHEN LOCATE('/', numero) > 0 THEN SUBSTRING_INDEX(numero, '/', 1)
    ELSE 'A'
END
WHERE serie IS NULL OR TRIM(serie) = '';

-- Vista canónica usada pelos exportadores. Campos NULL representam dados
-- históricos ainda não confirmados, nunca valores fiscais inventados.
CREATE OR REPLACE VIEW vw_fiscal_facturas AS
SELECT
    f.numero,
    COALESCE(f.serie, 'A') AS serie,
    f.data_emissao,
    f.data_hora_registo,
    f.moeda,
    f.taxa_cambio,
    f.estado,
    f.documento_origem,
    f.motivo_anulacao,
    f.data_anulacao,
    f.hash_documento,
    f.total,
    f.total_liquido,
    f.total_imposto,
    f.fk_id_empresa,
    f.fk_id_cliente,
    f.fk_id_usuario,
    d.sigla AS tipo_documento,
    e.nuit AS nuit_emitente,
    COALESCE(NULLIF(e.razaosocial, ''), e.nome) AS nome_emitente,
    c.nuit AS nuit_cliente,
    COALESCE(NULLIF(c.razaosocial, ''), c.nome) AS nome_cliente
FROM factura f
JOIN empresa e ON e.id = f.fk_id_empresa
JOIN cliente c ON c.id = f.fk_id_cliente
JOIN documentocomercial d ON d.id = f.fk_id_doc
WHERE COALESCE(f.deleteStatus, 0) = 0
  -- Proformas (FP) não são documentos fiscais emitidos. Guias de transporte
  -- pertencem ao fluxo de documentos de movimentação, fora desta exportação
  -- de facturação. A lista deverá ser alinhada com os códigos oficiais da AT.
  AND UPPER(TRIM(d.sigla)) IN ('FA', 'FR', 'FS', 'NC', 'ND');
