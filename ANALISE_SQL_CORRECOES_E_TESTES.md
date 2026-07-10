# Analise SQL - Correcoes propostas e testes de validacao

Este documento prepara as correcoes sem alterar ainda os scripts principais.
Use-o como checklist antes de aplicar mudancas definitivas nos ficheiros SQL.

## Scripts analisados

- `Script de Tabelas - 1-MODULO ENTRADA.sql`
- `Script de Tabelas - 2-MODULO DE REQUISICOES.sql`
- `Script de Tabelas - 3-MODULO FACTURACAO.sql`
- `Script de triggers - MODULO REQUISICOES.sql`
- `Script de triggers.sql`

## Ordem recomendada de execucao

1. `Script de Tabelas - 1-MODULO ENTRADA.sql`
2. `Script de Tabelas - 2-MODULO DE REQUISICOES.sql`
3. `Script de Tabelas - 3-MODULO FACTURACAO.sql`
4. `Script de triggers - MODULO REQUISICOES.sql`
5. `Script de triggers.sql`
6. `VALIDACAO_SQL_PRE_IMPLEMENTACAO.sql`

## Correcao 1 - movimentos sem tipoOrigem

### Problema

A tabela `movimento` define `tipoOrigem` como `NOT NULL`, mas alguns triggers inserem movimentos sem informar esse campo.

Locais envolvidos:

- `Script de Tabelas - 2-MODULO DE REQUISICOES.sql`: definicao de `movimento.tipoOrigem`
- `Script de triggers - MODULO REQUISICOES.sql`: trigger `Tg_registaMovimentoEntrada`
- `Script de triggers.sql`: trigger `Tg_C_Saida`

### Correcao proposta

No trigger de entrada por lote, trocar:

```sql
INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idLote)
VALUES ('Entrada', NEW.dataEntrada, NEW.quantidadeAtual,NEW.idArmazem, NEW.idProduto, NEW.id);
```

por:

```sql
INSERT INTO movimento (tipoOrigem, tipoMovimento, dataMovimento, quantidade, idArmazem, idProduto, idLote)
VALUES ('Compra', 'Entrada', NEW.dataEntrada, NEW.quantidadeAtual, NEW.idArmazem, NEW.idProduto, NEW.id);
```

No trigger de venda `Tg_C_Saida`, incluir `tipoOrigem` nos dois `INSERT INTO movimento`:

```sql
INSERT INTO movimento (
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idProduto,
    idArmazem,
    idLote
) VALUES (
    'Venda',
    'Saida',
    DATE(NEW.data_adicao),
    qtd_restante,
    NEW.fk_id_produto,
    NEW.fk_id_armazem,
    lote_id
);
```

E o mesmo no bloco que usa `qtd_lote`.

## Correcao 2 - estado invalido em requisicao

### Problema

`requisicao.estado` nao aceita o valor `'Em processamento'`, mas `Tg_estadoRequisicao` tenta gravar esse valor.

### Correcao proposta

Escolher um valor ja existente no enum. A opcao mais alinhada ao fluxo actual e usar `'Submetida'` ou `'AguardandoAprovacao'`.

Sugestao conservadora:

```sql
UPDATE requisicao
SET estado = 'Submetida'
WHERE id = NEW.idRequisicao
  AND estado = 'Pendente';
```

Se o processo exigir aprovacao formal antes da reserva, usar:

```sql
UPDATE requisicao
SET estado = 'AguardandoAprovacao'
WHERE id = NEW.idRequisicao
  AND estado = 'Pendente';
```

## Correcao 3 - calculo de imposto

### Problema

`imposto.percentagem` e gravado como percentagem, por exemplo `17.000`, mas o calculo actual multiplica directo:

```sql
SET p_Venda = p_precoBase + (p_precoBase * imposto_taxa);
SET v_precoVenda = v_precoBase + (v_precoBase * v_imposto);
```

Com imposto `17.000` e preco base `100`, o resultado vira `1800`, quando o esperado para 17% e `117`.

### Correcao proposta

```sql
SET p_Venda = p_precoBase + ((p_precoBase * imposto_taxa) / 100);
SET v_precoVenda = v_precoBase + ((v_precoBase * v_imposto) / 100);
```

## Correcao 4 - procedure aponta para tabela inexistente

### Problema

`procedure_updatePrecoVenda()` faz `UPDATE precovenda`, mas nos scripts analisados nao existe tabela `precovenda`.

### Correcao proposta

Se a tabela correcta e `precoProdutoArmazem`, trocar:

```sql
UPDATE precovenda
SET estado = 'desactivo'
WHERE dataValidade <= CURDATE() AND estado = 'activo';
```

por:

```sql
UPDATE precoProdutoArmazem
SET estado = 'desactivo',
    dataFimVigencia = COALESCE(dataFimVigencia, CURDATE())
WHERE id > 0
  AND dataFimVigencia <= CURDATE()
  AND estado = 'activo';
```

Ou remover esta procedure/evento se `evt_marcar_expiracao_precos` ja cobrir o caso.

## Correcao 5 - scripts nao sao reexecutaveis

### Problema

Os scripts usam `CREATE DATABASE`, `CREATE TABLE`, `CREATE TRIGGER`, `CREATE PROCEDURE` e `CREATE EVENT` sem `IF NOT EXISTS` ou `DROP ... IF EXISTS`.

### Correcao proposta

Para desenvolvimento, existem duas opcoes:

- criar um script de reset controlado, com `DROP DATABASE IF EXISTS _sysface; CREATE DATABASE _sysface;`
- ou tornar os scripts idempotentes com `CREATE TABLE IF NOT EXISTS` e `DROP TRIGGER/PROCEDURE/EVENT IF EXISTS` antes de recriar rotinas

Como ha perda de dados ao dropar a base, a opcao de reset deve ser usada apenas em ambiente de teste.

## Testes manuais esperados

Executar `VALIDACAO_SQL_PRE_IMPLEMENTACAO.sql` depois dos scripts principais.

Antes das correcoes, espera-se:

- falha ao inserir `lote`, por falta de `tipoOrigem` no movimento de entrada
- falha ao inserir `itemRequisicao`, se o trigger tentar usar estado invalido
- falha ou valor errado no calculo de preco com IVA
- falha ao criar/executar evento/procedure que referencia `precovenda`

Depois das correcoes, espera-se:

- `lote` cria movimento de `Compra/Entrada`
- `itemfactura` cria movimento de `Venda/Saida`
- `requisicao` muda para estado valido
- preco base `100` com IVA `17.000` gera preco venda `117.00`
- saldo de stock acompanha entradas e saidas
