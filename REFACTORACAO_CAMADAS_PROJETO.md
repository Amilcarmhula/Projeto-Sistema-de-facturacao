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
18. Services auxiliares de categoria, desconto, taxa/imposto, unidade de medida, parcela, lote, movimento, reserva, preco por produto/armazem e auditoria tambem deixaram de abrir alertas directamente.
19. Controllers de categoria, desconto, taxas, unidade, produto, pagamentos e configuracao/auditoria foram ajustados para tratar `ServiceException` na camada visual.
20. O login deixou de instanciar `EmpresaDAO` directamente e passou a obter dados da empresa por `ServiceEmpresa`.
21. Foi criado `ServiceCalculoPagamento` para centralizar o calculo de troco e valor pago, removendo regra de negocio duplicada dos controllers de pagamento.
22. `ServiceOrdemCompra` passou a calcular subtotal/total dos itens de ordem de compra, incluindo edicoes de custo feitas na tela.
23. `ServiceRecebimentoCompra` passou a calcular subtotal dos itens recebidos, retirando essa regra do controller de recebimento.
24. `ServiceContaPagar` passou a centralizar a regra de contas pagaveis e o calculo do saldo aberto.
25. `ServiceFacturaFornecedor` e `ServicePagamentoFornecedor` receberam metodos orientados ao fluxo da interface, validando objectos seleccionados e reduzindo validacoes de negocio nos controllers.
26. `VendasPorFacturacaoViewController` passou a delegar emissao/conversao de factura, preparacao de itens, regras de cotacao/proforma, calculo de totais e criterio de pesquisa de produtos para services de venda.
27. Foram criados `ServiceVendaFacturacao`, `ServiceCotacao`, `ServiceCalculoFactura` e `ServiceItemFactura` para reduzir responsabilidades de negocio dentro da interface de venda por facturacao.
28. `ProdutoController` passou a delegar cadastro/actualizacao/exclusao, preparacao de opcoes de combos, criterio de pesquisa, regras de preco/desconto e validacao de abertura de stock para services de produto.
29. Foram criados `ServiceProdutoCadastro`, `ServiceProdutoPreco`, `ServiceProdutoConfiguracao`, `ServicePesquisaProduto` e `OpcoesProdutoCadastro` para reduzir responsabilidades de inventario/produto dentro da interface.
30. `AddClienteController` passou a delegar cadastro/actualizacao de cliente, contactos e enderecos para services especificos, mantendo a interface focada em eventos, preenchimento visual e mensagens.
31. Foram criados `ServiceClienteCadastro`, `ServiceContactoCliente` e `ServiceEnderecoCliente` para centralizar validacoes, conversao de IDs/numeros, consultas auxiliares e chamadas aos services antigos de cliente.
32. `AddFornecedorController` passou a delegar cadastro/actualizacao de fornecedor, contactos e enderecos para services especificos, seguindo o mesmo padrao aplicado ao cadastro composto de cliente.
33. Foram criados `ServiceFornecedorCadastro`, `ServiceContactoFornecedor` e `ServiceEnderecoFornecedor` para concentrar conversao de campos da interface, validacao de fornecedor guardado e consultas auxiliares do modulo de fornecedor.
34. `AddArmazemController` passou a delegar cadastro/actualizacao de armazem, contactos e enderecos para services especificos, retirando conversoes de IDs/numeros e consultas auxiliares da interface.
35. Foram criados `ServiceArmazemCadastro`, `ServiceContactoArmazem` e `ServiceEnderecoArmazem` para aplicar o mesmo padrao de cadastro composto usado em cliente e fornecedor.
36. `AddStockController` passou a delegar entrada manual de stock, consulta de lotes/resumo/detalhes, listagem de armazens/fornecedores e calculo de valor total para `ServiceEntradaStock`.
37. `ServiceTransferenciaStock` recebeu uma sobrecarga orientada a interface para transferencia directa administrativa usando produto, armazens e quantidade textual, retirando parse/validacao do controller.
38. `DashboardController` passou a apresentar um resumo pronto do dashboard, deixando de agregar directamente services de factura, cliente, fornecedor, produto, stock, armazem, caixa e conta corrente.
39. Foram criados `ServiceDashboard`, `DashboardResumo` e `DashboardIndicadores` para centralizar consultas, filtros de categoria/armazem, movimentos, totais financeiros e textos dos indicadores.
40. `RequisicaoTransferenciaController` passou a delegar pesquisa, formatação de produtos/armazens/transferencias e conversao de quantidade para um service auxiliar.
41. Foi criado `ServiceTransferenciaConsulta` para concentrar filtros textuais, rotulos de apresentacao e validacao textual da quantidade na requisicao de transferencia.
42. Telas de pesquisa/listagem de produto, factura, cliente, fornecedor, armazem e categoria passaram a delegar filtros textuais e textos seguros de apresentacao para um service comum.
43. Foi criado `ServicePesquisaListagem` para concentrar filtros e formatacao de colunas em listagens, reduzindo duplicacao de predicados e acessos encadeados dentro dos controllers.
44. DAOs antigos de cliente, fornecedor, produto, stock, factura, itens, empresa, armazem, categoria, imposto, unidade, desconto, parcela, caixa, conta corrente, lotes, movimentos, reservas, configuracao e auditoria deixaram de abrir alertas JavaFX/Swing.
45. Chamadas de `AlertUtilities`, `JOptionPane` e blocos comentados antigos com UI foram removidos da camada DAO; erros tecnicos passaram a ser registados por `DaoErrorHandler.log`.
46. Controllers de requisicao, ordem de compra, recebimento e financeiro do fornecedor passaram a delegar filtros, textos seguros e formatacao de listagens para `ServicePesquisaListagem`.
47. Foram criados `ServiceOrdemCompraFormulario` e `ServiceRecebimentoCompraFormulario` para centralizar montagem de itens, conversao de valores textuais, validacao de itens vinculados a requisicao/ordem e consulta de custo/lote no fluxo de compra.
48. `PagamentoController` e `MiniPagamentoViewController` passaram a usar `ServiceFluxoPagamento`, removendo instanciacoes de services dentro dos metodos de pagamento, consulta de factura, transacoes e metodos de pagamento.

## Regras de dependencia

- `model` nao deve importar `dao`, `service`, `views`, JavaFX, Swing ou relatorios.
- `dao` deve depender de `model` e `util` de infraestrutura, mas nao deve chamar controllers/FXML.
- `service` deve depender de `dao`, `model` e `util`, mas nao deve carregar telas nem abrir alertas.
- `views` pode chamar `service` e adaptar mensagens para o utilizador.
- `views` nao deve importar ou instanciar DAOs; qualquer consulta, persistencia ou regra operacional deve passar por `service`.
- `views` deve limitar-se a eventos, preenchimento de campos, configuracao visual de tabelas/combos e apresentacao de mensagens.
- Regras de negocio devem comunicar falhas por `ServiceException`.
- DAOs devem registar erro tecnico por logging e nao por JavaFX/Swing.
- `SysFact` deve continuar como bootstrap da aplicacao e nao como deposito de estado de negocio.

## Pendencias recomendadas

1. Fazer uma varredura final em todos os services restantes para confirmar se algum modulo nao listado ainda usa `AlertUtilities` ou Swing.
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
