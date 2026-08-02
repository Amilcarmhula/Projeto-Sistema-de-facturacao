-- ============================================================
-- VALIDACAO DA AUDITORIA
-- JM-InvSM / Sistema stand-alone generico
-- ============================================================
--
-- Objectivo:
-- 1. Confirmar se os triggers de auditoria foram realmente criados.
-- 2. Identificar tabelas sem trigger de auditoria.
-- 3. Executar um teste controlado que deve gerar registo em auditoria_registo.
--
-- Importante:
-- Este script NAO cria os triggers. Ele valida se eles ja foram aplicados.
-- Para criar os triggers:
--   a) Execute 07_AUDITORIA_DELETE_LOGICO_CONFIG.sql.
--   b) Copie o resultado da coluna SCRIPT_TRIGGERS_AUDITORIA.
--   c) Execute o conteudo copiado numa nova aba do MySQL Workbench.
--   d) Execute este script 08 para validar.
-- ============================================================

USE _sysface_teste;


-- 1) Quantos triggers de auditoria existem neste schema?
SELECT COUNT(*) AS TRIGGERS_AUDITORIA_CRIADOS
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = DATABASE()
  AND TRIGGER_NAME REGEXP '^trg_(ai|au|bd)_aud_';

-- 2) Ver todos os triggers de auditoria instalados.
SELECT
    TRIGGER_NAME,
    EVENT_MANIPULATION,
    EVENT_OBJECT_TABLE,
    ACTION_TIMING,
    CREATED
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = DATABASE()
  AND TRIGGER_NAME REGEXP '^trg_(ai|au|bd)_aud_'
ORDER BY EVENT_OBJECT_TABLE, TRIGGER_NAME;

-- 3) Listar tabelas sem nenhum trigger de auditoria.
-- Se aparecerem tabelas aqui, elas ainda nao estao auditadas.
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

-- 4) Teste controlado.
-- Este UPDATE em configuracao_sistema deve gerar uma linha em auditoria_registo.
SET @app_user_id = (
    SELECT MIN(id)
    FROM usuario
);

UPDATE configuracao_sistema
SET observacao = CONCAT('Teste auditoria executado em ', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'))
WHERE id = 1
  AND deleteStatus = 0;

-- 5) Resultado esperado: pelo menos uma linha recente para configuracao_sistema.
SELECT
    id,
    fk_id_usuario,
    tabela,
    id_registo,
    operacao,
    colunas_alteradas,
    valores_anteriores,
    valores_novos,
    data_operacao,
    origem
FROM auditoria_registo
WHERE tabela = 'configuracao_sistema'
ORDER BY id DESC
LIMIT 10;

-- 6) Resumo geral da auditoria ja registada.
SELECT tabela, operacao, COUNT(*) AS total
FROM auditoria_registo
GROUP BY tabela, operacao
ORDER BY tabela, operacao;

-- ============================================================
-- Interpretacao:
--
-- TRIGGERS_AUDITORIA_CRIADOS = 0
--   Os comandos gerados pelo script 07 ainda nao foram executados.
--
-- A lista TABELA_SEM_TRIGGER_AUDITORIA tem linhas
--   Algumas tabelas ainda nao estao cobertas por auditoria.
--
-- O UPDATE de configuracao_sistema executa, mas nao aparece em auditoria_registo
--   O trigger dessa tabela nao existe ou falhou na criacao.
--
-- O UPDATE aparece em auditoria_registo
--   A auditoria esta activa e o problema era apenas falta de aplicacao dos triggers.
-- ============================================================
