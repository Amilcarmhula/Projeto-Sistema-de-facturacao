# Refatoracao de camadas do JM-InvSM

## Objectivo

Organizar o projecto por responsabilidades claras, mantendo o comportamento actual compilavel:

- `jminvsm.model`: entidades, enums e objectos de dominio.
- `jminvsm.dao`: acesso a dados, SQL, mapeamento de `ResultSet` e interfaces DAO.
- `jminvsm.service`: regras de negocio, validacoes e orquestracao de fluxos.
- `jminvsm.views`: FXML e controllers JavaFX.
- `jminvsm.util`: utilitarios transversais sem regra de negocio especifica.
- `jminvsm.fiscal`: modulo fiscal isolado, com subcamadas proprias.
- `jminvsm.reports`: geracao documental/PDF que nao pertence aos models.

## Alteracoes aplicadas nesta etapa

1. A raiz `jminvsm` passou a conter apenas `SysFact.java`, a classe de arranque da aplicacao.
2. O controlador de login foi movido de `jminvsm.MainController` para `jminvsm.views.login.LoginController`.
3. O FXML de login foi movido de `jminvsm/Main.fxml` para `jminvsm/views/login/login.fxml`.
4. `SysFact` foi ajustado para carregar o novo FXML de login.
5. `ConfigurationManager` foi movido para `jminvsm.util`.
6. `ThermalPrinterExample` foi movido para `jminvsm.util.printer`.
7. Foi criada `jminvsm.util.SessaoAplicacao` para centralizar dados globais de sessao.
8. Controllers e utilitarios deixaram de depender directamente de `SysFact` para obter utilizador, empresa e dados temporarios.
9. Foram adicionados `package-info.java` nos pacotes principais para documentar o contrato de cada camada.
10. Foi criada `jminvsm.service.ServiceException` para erros de regra de negocio.
11. Services de compras, requisicoes, picking e transferencia passaram a lancar `ServiceException` em validacoes, sem abrir alertas.
12. Controllers de requisicao, ordem de compra, recebimento, financeiro do fornecedor e transferencia passaram a capturar `ServiceException` e mostrar mensagens ao utilizador.
13. DAOs de compras, requisicoes, picking e transferencia deixaram de usar `AlertUtilities`; erros tecnicos passam por `jminvsm.dao.DaoErrorHandler`.
14. Fontes DAO dos modulos novos foram restaurados e validados com compilacao limpa.
15. Services antigos de configuracao, usuario, cliente, fornecedor, armazem, produto, vendas e transacoes deixaram de abrir alertas directamente.
16. Subservices de contactos/enderecos de cliente, fornecedor e armazem tambem passaram a lancar `ServiceException`.
17. Controllers afectados passaram a capturar `ServiceException` e concentrar mensagens de sucesso/erro na camada `views`.

## Regras de dependencia

- `model` nao deve importar `dao`, `service`, `views`, JavaFX, Swing ou relatorios.
- `dao` deve depender de `model` e `util` de infraestrutura, mas nao deve chamar controllers/FXML.
- `service` deve depender de `dao`, `model` e `util`, mas nao deve carregar telas nem abrir alertas.
- `views` pode chamar `service` e adaptar mensagens para o utilizador.
- Regras de negocio devem comunicar falhas por `ServiceException`.
- DAOs devem registar erro tecnico por logging e nao por JavaFX/Swing.
- `SysFact` deve continuar como bootstrap da aplicacao e nao como deposito de estado de negocio.

## Pendencias recomendadas

1. Aplicar o mesmo padrao aos services auxiliares ainda fora dos modulos tratados, como categoria, desconto, imposto, unidade de medida, parcela, lote, movimento e reserva.
2. Padronizar resultados de service em objectos de resultado quando for necessario devolver mensagens de sucesso, avisos e dados juntos.
3. Rever o pacote `jminvsm.reports` para decidir se ele permanece como modulo documental ou se sera integrado ao modulo fiscal/documentos.
4. Remover classes em `teste` do build principal quando houver estrutura formal de testes.
5. Padronizar os nomes dos subpacotes que ainda misturam portugues/ingles ou camelCase, como `precoProdutoArmazem` e `unidademedida`.

## Validacao

Validado com compilacao normal e compilacao limpa:

```bash
ant -q compile
ant -q clean compile
```

Resultado: `BUILD SUCCESSFUL`.
