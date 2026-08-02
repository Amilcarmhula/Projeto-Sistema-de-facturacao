# Comunicação fiscal e SAF-T (Moz) — JM-InvSM

## 1. Estado regulatório e responsabilidade

O módulo foi implementado com uma camada fiscal canónica denominada
`JM-AT-BASE-1.0`. Ela permite gerar:

- Fase 1: Excel, CSV e pacote ZIP de comunicação mensal;
- Fase 2: XML SAF-T provisório e validação por XSD.

O modelo e o XSD incluídos no projecto **não são oficiais**. O aviso
n.º 40/AT/DGI/2025 orienta as empresas desenvolvedoras a contactar a
Autoridade Tributária para receber a estrutura de dados e os procedimentos de
certificação. Antes de submeter ficheiros reais, é obrigatório substituir o
mapeamento provisório pela especificação fornecida pela AT e concluir os testes
de aceitação/certificação.

Contacto divulgado para desenvolvedores: `efacturacao@at.gov.mz`.

Fontes públicas:

- https://ocam.org.mz/2025/06/perguntas-frequentes-sobre-a-facturacao-electronica/
- https://www.mef.gov.mz/index.php/publicacoes/politicas/bdpesoe/bdpesoe-2025/2484-bdpesoe-anual-2025-final/file

## 2. Artefactos

| Artefacto | Finalidade |
|---|---|
| `outputs/saft_jm_invsm/Modelo_Base_Fiscal_JM-InvSM.xlsx` | Modelo-base, exemplo, resumo IVA, listas e dicionário de mapeamento |
| `outputs/saft_jm_invsm/Modelo_Base_Facturas.csv` | Exemplo da estrutura plana de facturas |
| `07_FISCAL_SAFT_MOZ.sql` | Migração da camada fiscal, histórico e auditoria |
| `jminvsm/fiscal/` | Modelos, repositório, validação e exportadores |
| `saft-moz-provisional-1.0.xsd` | XSD técnico provisório |
| `fiscalExportView.fxml` | Ecrã de validação e exportação |

## 3. Instalação

1. Fazer cópia de segurança integral da base de dados.
2. Restaurar a cópia num ambiente de testes.
3. Executar `07_FISCAL_SAFT_MOZ.sql`.
4. Compilar e iniciar o JM-InvSM.
5. Entrar numa empresa.
6. Abrir **Contabilidade → SAF-T / Comunicação fiscal**.
7. Seleccionar um período curto e executar **Validar dados**.
8. Corrigir todos os erros antes de exportar.

Não executar inicialmente a migração directamente na base de produção.

## 4. Fase 1 — comunicação mensal

O botão **Exportar Fase 1 — Excel/CSV** gera um ZIP com:

- `facturas.csv`;
- `linhas.csv`;
- `resumo_iva.csv`;
- `facturacao_AAAAMMDD_AAAAMMDD.xlsx`;
- `validacao.csv`;
- `manifesto.txt`.

Os CSV são UTF-8 com BOM, delimitados por ponto e vírgula. O XLSX contém as
folhas Facturas, Linhas e Resumo IVA. O sistema calcula SHA-256 do pacote e
regista a exportação na tabela `fiscal_exportacao`.

Se o pacote ultrapassar 2 MB, o ecrã apresenta um aviso. Nesse caso, gerar
ficheiros para períodos menores.

## 5. Fase 2 — XML SAF-T provisório

O botão **Exportar Fase 2 — SAF-T XML** gera um ZIP com:

- XML `SAFT_MOZ_AAAAMMDD_AAAAMMDD.xml`;
- XSD provisório;
- relatório de validação;
- manifesto.

O XML contém:

- `Header`;
- `MasterFiles/Customers`;
- `MasterFiles/Products`;
- `MasterFiles/TaxTable`;
- `SourceDocuments/SalesInvoices`;
- linhas, impostos e totais documentais.

O ficheiro é validado localmente contra o XSD antes de ser entregue.

## 6. Regras bloqueantes

A exportação é bloqueada quando existir, entre outros:

- documento sem número, emitente, tipo ou data;
- número documental duplicado;
- factura fora do período;
- documento anulado sem motivo;
- factura sem linhas;
- linha sem quantidade positiva;
- isenção sem motivo;
- divergência superior a 0,02 MT entre cabeçalho e linhas;
- totais fiscais históricos não gravados.

Dados antigos sem snapshot de preço, taxa ou imposto geram avisos/erros para
revisão. O sistema não inventa dados fiscais históricos.

## 7. Captura no momento da emissão

As novas facturas passam a guardar:

- série e data/hora de registo;
- moeda e taxa de câmbio;
- total líquido e imposto;
- preço unitário histórico;
- desconto, código e taxa de imposto;
- base tributável, imposto e total por linha;
- hash SHA-256 interno.

O hash implementado é um controlo interno de integridade. Ele não deve ser
confundido com um algoritmo fiscal oficial até a AT confirmar o método.

## 8. Adaptação ao modelo oficial

Quando a AT fornecer o Excel/XSD:

1. guardar o original recebido, sem alterações;
2. preencher `CampoOficialAT` na folha Dicionário;
3. comparar códigos de documento, imposto, unidade e estado;
4. alterar somente os exportadores/mapeadores;
5. substituir namespace e XSD provisórios;
6. criar casos de teste a partir dos exemplos oficiais;
7. submeter ficheiros de homologação;
8. registar número/estado da certificação no sistema.

## 9. Pedido técnico sugerido à AT

**Assunto:** Pedido de requisitos técnicos SAF-T (Moz) e certificação do
software JM-InvSM

> Exmos. Senhores,
>
> Somos responsáveis pelo desenvolvimento do JM-InvSM, uma aplicação de
> facturação e gestão de stock para empresas moçambicanas. No âmbito do Aviso
> n.º 40/AT/DGI/2025, solicitamos a estrutura oficial do ficheiro de comunicação
> mensal, o XSD SAF-T (Moz), tabelas de códigos, exemplos válidos e procedimentos
> actualizados de certificação/homologação.
>
> Solicitamos igualmente a inclusão do nosso contacto no canal técnico mantido
> pela Autoridade Tributária para empresas desenvolvedoras.

