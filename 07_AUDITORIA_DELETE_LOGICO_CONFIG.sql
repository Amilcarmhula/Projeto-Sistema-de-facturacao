-- ============================================================
-- AUDITORIA, DELETE LOGICO E CONFIGURACAO DE MODO OPERACIONAL
-- JM-InvSM / Sistema stand-alone generico
-- ============================================================
--
-- Objectivo:
-- 1. Criar estrutura central de auditoria para INSERT, UPDATE e DELETE.
-- 2. Adicionar colunas de delete logico nas tabelas existentes.
-- 3. Criar configuracao global para operar em:
--    - FACTURACAO
--    - FACTURACAO_STOCK
-- 4. Gerar o SQL dos triggers de auditoria para todas as tabelas da base actual.
--
-- Nota importante:
-- O utilizador da operacao e obtido pela variavel de sessao @app_user_id.
-- A aplicacao Java executa SET @app_user_id = <id_usuario> ao abrir
-- ou reutilizar uma conexao atraves de AuditoriaContext/Conexao.
-- ============================================================
use _sysface_teste;

SET SESSION group_concat_max_len = 10000000;

CREATE TABLE IF NOT EXISTS auditoria_registo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fk_id_usuario INT NULL,
    tabela VARCHAR(100) NOT NULL,
    id_registo VARCHAR(255) NULL,
    operacao ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    colunas_alteradas JSON NULL,
    valores_anteriores JSON NULL,
    valores_novos JSON NULL,
    data_operacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    origem VARCHAR(50) NOT NULL DEFAULT 'APP',
    INDEX idx_auditoria_tabela_registo (tabela, id_registo),
    INDEX idx_auditoria_usuario_data (fk_id_usuario, data_operacao),
    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS configuracao_sistema (
    id INT PRIMARY KEY AUTO_INCREMENT,
    modoOperacao ENUM('FACTURACAO', 'FACTURACAO_STOCK') NOT NULL DEFAULT 'FACTURACAO_STOCK',
    controlarStockGlobal BOOLEAN NOT NULL DEFAULT TRUE,
    permitirVendaSemStock BOOLEAN NOT NULL DEFAULT FALSE,
    observacao VARCHAR(255),
    fk_id_usuario INT NULL,
    dataCriacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dataActualizacao TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    deleteStatus TINYINT(1) NOT NULL DEFAULT 0,
    deletedAt DATETIME NULL,
    deletedBy INT NULL,
    CONSTRAINT fk_config_sistema_usuario
        FOREIGN KEY (fk_id_usuario) REFERENCES usuario(id),
    CONSTRAINT fk_config_sistema_deleted_by
        FOREIGN KEY (deletedBy) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS auditoria_trigger_sql (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tabela VARCHAR(100) NULL,
    trigger_name VARCHAR(100) NULL,
    tipo VARCHAR(20) NOT NULL,
    ordem INT NOT NULL,
    sql_text LONGTEXT NOT NULL,
    dataGeracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_auditoria_trigger_sql_ordem (ordem, id),
    INDEX idx_auditoria_trigger_sql_tabela (tabela)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO configuracao_sistema
    (id, modoOperacao, controlarStockGlobal, permitirVendaSemStock, observacao)
VALUES
    (1, 'FACTURACAO_STOCK', TRUE, FALSE, 'Configuracao padrao inicial')
ON DUPLICATE KEY UPDATE
    modoOperacao = modoOperacao;

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_add_column_if_not_exists $$
CREATE PROCEDURE sp_add_column_if_not_exists(
    IN p_table_name VARCHAR(100),
    IN p_column_name VARCHAR(100),
    IN p_column_definition TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = p_table_name
          AND COLUMN_NAME = p_column_name
    ) THEN
        SET @ddl_add_column = CONCAT(
            'ALTER TABLE `', REPLACE(p_table_name, '`', '``'),
            '` ADD COLUMN `', REPLACE(p_column_name, '`', '``'), '` ',
            p_column_definition
        );
        PREPARE stmt_add_column FROM @ddl_add_column;
        EXECUTE stmt_add_column;
        DEALLOCATE PREPARE stmt_add_column;
    END IF;
END $$

DROP PROCEDURE IF EXISTS sp_add_index_if_not_exists $$
CREATE PROCEDURE sp_add_index_if_not_exists(
    IN p_table_name VARCHAR(100),
    IN p_index_name VARCHAR(100),
    IN p_index_definition TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.STATISTICS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = p_table_name
          AND INDEX_NAME = p_index_name
    ) THEN
        SET @ddl_add_index = CONCAT(
            'ALTER TABLE `', REPLACE(p_table_name, '`', '``'),
            '` ADD INDEX `', REPLACE(p_index_name, '`', '``'), '` ',
            p_index_definition
        );
        PREPARE stmt_add_index FROM @ddl_add_index;
        EXECUTE stmt_add_index;
        DEALLOCATE PREPARE stmt_add_index;
    END IF;
END $$

DROP PROCEDURE IF EXISTS sp_apply_delete_logico_columns $$
CREATE PROCEDURE sp_apply_delete_logico_columns()
BEGIN
    DECLARE v_done INT DEFAULT 0;
    DECLARE v_table_name VARCHAR(100);

    DECLARE cur_tables CURSOR FOR
        SELECT TABLE_NAME
        FROM information_schema.TABLES
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_TYPE = 'BASE TABLE'
          AND TABLE_NAME NOT IN ('auditoria_registo', 'auditoria_trigger_sql');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    OPEN cur_tables;
    read_loop: LOOP
        FETCH cur_tables INTO v_table_name;
        IF v_done = 1 THEN
            LEAVE read_loop;
        END IF;

        CALL sp_add_column_if_not_exists(v_table_name, 'deleteStatus', 'TINYINT(1) NOT NULL DEFAULT 0');
        CALL sp_add_column_if_not_exists(v_table_name, 'deletedAt', 'DATETIME NULL');
        CALL sp_add_column_if_not_exists(v_table_name, 'deletedBy', 'INT NULL');
        CALL sp_add_index_if_not_exists(v_table_name, CONCAT('idx_', LEFT(v_table_name, 45), '_deleteStatus'), '(deleteStatus)');
    END LOOP;
    CLOSE cur_tables;
END $$

DROP PROCEDURE IF EXISTS sp_soft_delete $$
CREATE PROCEDURE sp_soft_delete(
    IN p_table_name VARCHAR(100),
    IN p_id_column VARCHAR(100),
    IN p_id_value VARCHAR(255),
    IN p_id_usuario INT
)
BEGIN
    SET @app_user_id = p_id_usuario;
    SET @soft_delete_sql = CONCAT(
        'UPDATE `', REPLACE(p_table_name, '`', '``'), '` ',
        'SET deleteStatus = 1, deletedAt = NOW(), deletedBy = ? ',
        'WHERE `', REPLACE(p_id_column, '`', '``'), '` = ? ',
        'AND deleteStatus = 0'
    );
    SET @soft_delete_user = p_id_usuario;
    SET @soft_delete_id = p_id_value;

    PREPARE stmt_soft_delete FROM @soft_delete_sql;
    EXECUTE stmt_soft_delete USING @soft_delete_user, @soft_delete_id;
    DEALLOCATE PREPARE stmt_soft_delete;
END $$

DROP PROCEDURE IF EXISTS sp_create_audit_triggers_for_table $$
DROP PROCEDURE IF EXISTS sp_create_all_audit_triggers $$
DROP PROCEDURE IF EXISTS sp_generate_audit_trigger_sql_for_table $$
CREATE PROCEDURE sp_generate_audit_trigger_sql_for_table(IN p_table_name VARCHAR(100))
BEGIN
    DECLARE v_new_json_args LONGTEXT;
    DECLARE v_old_json_args LONGTEXT;
    DECLARE v_columns_array LONGTEXT;
    DECLARE v_changed_columns_args LONGTEXT;
    DECLARE v_pk_new_parts LONGTEXT;
    DECLARE v_pk_old_parts LONGTEXT;
    DECLARE v_pk_new_expr LONGTEXT;
    DECLARE v_pk_old_expr LONGTEXT;
    DECLARE v_trigger_ai VARCHAR(64);
    DECLARE v_trigger_au VARCHAR(64);
    DECLARE v_trigger_bd VARCHAR(64);

    IF p_table_name IN ('auditoria_registo', 'auditoria_trigger_sql') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tabelas internas de auditoria nao devem auditar a si mesmas.';
    END IF;

    SELECT GROUP_CONCAT(CONCAT('''', COLUMN_NAME, ''', NEW.`', COLUMN_NAME, '`')
                        ORDER BY ORDINAL_POSITION SEPARATOR ', ')
      INTO v_new_json_args
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name;

    SELECT GROUP_CONCAT(CONCAT('''', COLUMN_NAME, ''', OLD.`', COLUMN_NAME, '`')
                        ORDER BY ORDINAL_POSITION SEPARATOR ', ')
      INTO v_old_json_args
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name;

    SELECT GROUP_CONCAT(CONCAT('''', COLUMN_NAME, '''')
                        ORDER BY ORDINAL_POSITION SEPARATOR ', ')
      INTO v_columns_array
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name;

    SELECT GROUP_CONCAT(
             CONCAT('IF(NOT (OLD.`', COLUMN_NAME, '` <=> NEW.`', COLUMN_NAME, '`), JSON_QUOTE(''', COLUMN_NAME, '''), NULL)')
             ORDER BY ORDINAL_POSITION SEPARATOR ', '
           )
      INTO v_changed_columns_args
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name;

    SELECT GROUP_CONCAT(CONCAT('CONCAT(''', COLUMN_NAME, '='', NEW.`', COLUMN_NAME, '`)')
                        ORDER BY ORDINAL_POSITION SEPARATOR ', ')
      INTO v_pk_new_parts
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND COLUMN_KEY = 'PRI';

    SELECT GROUP_CONCAT(CONCAT('CONCAT(''', COLUMN_NAME, '='', OLD.`', COLUMN_NAME, '`)')
                        ORDER BY ORDINAL_POSITION SEPARATOR ', ')
      INTO v_pk_old_parts
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND COLUMN_KEY = 'PRI';

    SET v_pk_new_expr = IF(v_pk_new_parts IS NULL, 'NULL', CONCAT('CONCAT_WS(''|'', ', v_pk_new_parts, ')'));
    SET v_pk_old_expr = IF(v_pk_old_parts IS NULL, 'NULL', CONCAT('CONCAT_WS(''|'', ', v_pk_old_parts, ')'));

    SET v_trigger_ai = CONCAT('trg_ai_aud_', LEFT(p_table_name, 51));
    SET v_trigger_au = CONCAT('trg_au_aud_', LEFT(p_table_name, 51));
    SET v_trigger_bd = CONCAT('trg_bd_aud_', LEFT(p_table_name, 51));

    INSERT INTO auditoria_trigger_sql (tabela, trigger_name, tipo, ordem, sql_text)
    VALUES
        (p_table_name, v_trigger_ai, 'DROP', 10, CONCAT('DROP TRIGGER IF EXISTS `', v_trigger_ai, '` $$')),
        (p_table_name, v_trigger_au, 'DROP', 20, CONCAT('DROP TRIGGER IF EXISTS `', v_trigger_au, '` $$')),
        (p_table_name, v_trigger_bd, 'DROP', 30, CONCAT('DROP TRIGGER IF EXISTS `', v_trigger_bd, '` $$'));

    SET @create_ai = CONCAT(
        'CREATE TRIGGER `', v_trigger_ai, '` AFTER INSERT ON `', REPLACE(p_table_name, '`', '``'), '` FOR EACH ROW ',
        'BEGIN ',
        'INSERT INTO auditoria_registo (fk_id_usuario, tabela, id_registo, operacao, colunas_alteradas, valores_anteriores, valores_novos) VALUES ',
        '(@app_user_id, ''', p_table_name, ''', ', v_pk_new_expr, ', ''INSERT'', JSON_ARRAY(', v_columns_array, '), NULL, JSON_OBJECT(', v_new_json_args, ')); ',
        'END $$'
    );
    INSERT INTO auditoria_trigger_sql (tabela, trigger_name, tipo, ordem, sql_text)
    VALUES (p_table_name, v_trigger_ai, 'CREATE', 40, @create_ai);

    SET @create_au = CONCAT(
        'CREATE TRIGGER `', v_trigger_au, '` AFTER UPDATE ON `', REPLACE(p_table_name, '`', '``'), '` FOR EACH ROW ',
        'BEGIN ',
        'INSERT INTO auditoria_registo (fk_id_usuario, tabela, id_registo, operacao, colunas_alteradas, valores_anteriores, valores_novos) VALUES ',
        '(@app_user_id, ''', p_table_name, ''', ', v_pk_new_expr, ', ''UPDATE'', ',
        'CONCAT(''['', CONCAT_WS('','', ', v_changed_columns_args, '), '']''), ',
        'JSON_OBJECT(', v_old_json_args, '), JSON_OBJECT(', v_new_json_args, ')); ',
        'END $$'
    );
    INSERT INTO auditoria_trigger_sql (tabela, trigger_name, tipo, ordem, sql_text)
    VALUES (p_table_name, v_trigger_au, 'CREATE', 50, @create_au);

    SET @create_bd = CONCAT(
        'CREATE TRIGGER `', v_trigger_bd, '` BEFORE DELETE ON `', REPLACE(p_table_name, '`', '``'), '` FOR EACH ROW ',
        'BEGIN ',
        'INSERT INTO auditoria_registo (fk_id_usuario, tabela, id_registo, operacao, colunas_alteradas, valores_anteriores, valores_novos) VALUES ',
        '(@app_user_id, ''', p_table_name, ''', ', v_pk_old_expr, ', ''DELETE'', JSON_ARRAY(', v_columns_array, '), JSON_OBJECT(', v_old_json_args, '), NULL); ',
        'END $$'
    );
    INSERT INTO auditoria_trigger_sql (tabela, trigger_name, tipo, ordem, sql_text)
    VALUES (p_table_name, v_trigger_bd, 'CREATE', 60, @create_bd);
END $$

DROP PROCEDURE IF EXISTS sp_generate_all_audit_trigger_sql $$
CREATE PROCEDURE sp_generate_all_audit_trigger_sql()
BEGIN
    DECLARE v_done INT DEFAULT 0;
    DECLARE v_table_name VARCHAR(100);

    DECLARE cur_tables CURSOR FOR
        SELECT TABLE_NAME
        FROM information_schema.TABLES
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_TYPE = 'BASE TABLE'
          AND TABLE_NAME NOT IN ('auditoria_registo', 'auditoria_trigger_sql');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    DELETE FROM auditoria_trigger_sql WHERE id > 0;
    INSERT INTO auditoria_trigger_sql (tipo, ordem, sql_text)
    VALUES ('CONTROL', 0, 'DELIMITER $$');

    OPEN cur_tables;
    read_loop: LOOP
        FETCH cur_tables INTO v_table_name;
        IF v_done = 1 THEN
            LEAVE read_loop;
        END IF;
        CALL sp_generate_audit_trigger_sql_for_table(v_table_name);
    END LOOP;
    CLOSE cur_tables;

    INSERT INTO auditoria_trigger_sql (tipo, ordem, sql_text)
    VALUES ('CONTROL', 999999, 'DELIMITER ;');
END $$

DELIMITER ;

CALL sp_apply_delete_logico_columns();
CALL sp_generate_all_audit_trigger_sql();

-- MySQL pode rejeitar CREATE TRIGGER dentro de PREPARE/EXECUTE em stored
-- procedures, gerando Error Code 1295. Por isso o script acima gera os
-- comandos finais abaixo. Copie o conteudo da coluna SCRIPT_TRIGGERS_AUDITORIA
-- e execute-o numa nova aba para criar/recriar todos os triggers de auditoria.
SELECT GROUP_CONCAT(sql_text ORDER BY ordem, id SEPARATOR '\n') AS SCRIPT_TRIGGERS_AUDITORIA
FROM auditoria_trigger_sql;

-- Resultado alternativo, uma instrucao por linha.
-- Use este resultado se o campo GROUP_CONCAT aparecer truncado ou dificil de copiar
-- no MySQL Workbench.
SELECT ordem, tabela, trigger_name, tipo, sql_text AS COMANDO_SQL
FROM auditoria_trigger_sql
ORDER BY ordem, id;

-- Diagnostico imediato:
-- Se TRIGGERS_AUDITORIA_CRIADOS = 0, a auditoria ainda NAO esta activa.
-- Nesse caso, copie e execute o conteudo de SCRIPT_TRIGGERS_AUDITORIA
-- ou todos os COMANDO_SQL acima, respeitando a ordem.
SELECT COUNT(*) AS TRIGGERS_AUDITORIA_CRIADOS
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = DATABASE()
  AND TRIGGER_NAME REGEXP '^trg_(ai|au|bd)_aud_';

SELECT t.TABLE_NAME AS TABELA_SEM_TRIGGER_AUDITORIA
FROM information_schema.TABLES t
WHERE t.TABLE_SCHEMA = DATABASE()
  AND t.TABLE_TYPE = 'BASE TABLE'
  AND t.TABLE_NAME NOT IN ('auditoria_registo', 'auditoria_trigger_sql')
  AND NOT EXISTS (
      SELECT 1
      FROM information_schema.TRIGGERS tr
      WHERE tr.TRIGGER_SCHEMA = DATABASE()
        AND tr.EVENT_OBJECT_TABLE = t.TABLE_NAME
        AND tr.TRIGGER_NAME REGEXP '^trg_(ai|au|bd)_aud_'
  )
ORDER BY t.TABLE_NAME;

-- ============================================================
-- Consultas uteis para validacao:
--
-- SELECT * FROM configuracao_sistema WHERE id = 1;
-- SELECT tabela, operacao, COUNT(*) FROM auditoria_registo GROUP BY tabela, operacao;
-- SELECT * FROM auditoria_registo ORDER BY id DESC LIMIT 20;
--
-- Exemplo de delete logico controlado:
-- SET @app_user_id = 1;
-- CALL sp_soft_delete('cliente', 'id', '1', @app_user_id);
-- ============================================================
