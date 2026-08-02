package jminvsm.views.modulo_compra.recebimentoCompraView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.ServiceException;
import jminvsm.service.compra.ServiceOrdemCompra;
import jminvsm.service.compra.ServiceRecebimentoCompra;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.precoProdutoArmazem.ServicePrecoProdutoArmazem;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class RecebimentoCompraViewController implements Initializable {

    private ServiceRecebimentoCompra serviceRecebimentoCompra;
    private ServiceOrdemCompra serviceOrdemCompra;
    private ServiceFornecedor serviceFornecedor;
    private ServiceProdutoFornecido servicoprodFornecido;
    private ServiceProduto serviceProduto;
    private ServiceArmazem serviceArmazem;
    private ServicePrecoProdutoArmazem servicePrecoProdutoArmazem;
    private Usuario userData;
    private boolean sincronizandoItemOrdem;

    private final ObservableList<ItemRecebimentoCompra> itensNovoRecebimento = FXCollections.observableArrayList();
    private ObservableList<RecebimentoCompra> listaRecebimentos = FXCollections.observableArrayList();
    private RecebimentoCompra recebimentoSelecionado;

    @FXML
    private TabPane tabPaneRecebimentos;
    @FXML
    private Label labResumoCabecalho;
    @FXML
    private TextField txtNumero;
    @FXML
    private ComboBox<OrdemCompra> combOrdemCompra;
    @FXML
    private ComboBox<Fornecedor> combFornecedor;
    @FXML
    private TextField txtDocumentoFornecedor;
    @FXML
    private TextArea txtObservacao;
    @FXML
    private ComboBox<ItemOrdemCompra> combItemOrdem;
    @FXML
    private ComboBox<Produto> combProduto;
    @FXML
    private ComboBox<Armazem> combArmazem;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtCustoUnitario;
    @FXML
    private TextField txtCodigoLote;
    @FXML
    private TableView<ItemRecebimentoCompra> tabelaItensNova;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colItemProduto;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colItemArmazem;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Integer> colItemQuantidade;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Double> colItemCusto;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colItemLote;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Double> colItemSubtotal;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colItemEstado;

    @FXML
    private TextField txtPesquisa;
    @FXML
    private ComboBox<String> combFiltroEstado;
    @FXML
    private TableView<RecebimentoCompra> tabelaRecebimentos;
    @FXML
    private TableColumn<RecebimentoCompra, Integer> colRcId;
    @FXML
    private TableColumn<RecebimentoCompra, String> colRcNumero;
    @FXML
    private TableColumn<RecebimentoCompra, String> colRcOrdem;
    @FXML
    private TableColumn<RecebimentoCompra, String> colRcFornecedor;
    @FXML
    private TableColumn<RecebimentoCompra, String> colRcDocumento;
    @FXML
    private TableColumn<RecebimentoCompra, String> colRcEstado;
    @FXML
    private TableColumn<RecebimentoCompra, Double> colRcTotal;

    @FXML
    private Label labRcId;
    @FXML
    private Label labRcNumero;
    @FXML
    private Label labRcOrdem;
    @FXML
    private Label labRcFornecedor;
    @FXML
    private Label labRcEstado;
    @FXML
    private Label labRcTotal;
    @FXML
    private Label labRcSelecionadoAux;

    @FXML
    private TableView<ItemRecebimentoCompra> tabelaItensDetalhe;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colDetalheProduto;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colDetalheArmazem;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Integer> colDetalheQuantidade;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Double> colDetalheCusto;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colDetalheLote;
    @FXML
    private TableColumn<ItemRecebimentoCompra, Double> colDetalheSubtotal;
    @FXML
    private TableColumn<ItemRecebimentoCompra, String> colDetalheEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaItensNova.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaRecebimentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensDetalhe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userData = SessaoAplicacao.getUserData();
        inicializarServices();
        configurarCombos();
        configurarTabelas();
        tabelaItensNova.setItems(itensNovoRecebimento);
        atualizarDados();
    }

    @FXML
    private void selecionarOrdemFormulario() {
        OrdemCompra ordem = combOrdemCompra.getSelectionModel().getSelectedItem();
        if (ordem == null) {
            combItemOrdem.setItems(FXCollections.<ItemOrdemCompra>observableArrayList());
            limparCamposItemRecebimento();
            return;
        }
        selecionarComboFornecedor(ordem.getFornecedor());
        combItemOrdem.setItems(serviceOrdemCompra != null
                ? serviceOrdemCompra.consultaItens(ordem.getId())
                : FXCollections.<ItemOrdemCompra>observableArrayList());
        limparCamposItemRecebimento();
    }

    @FXML
    private void selecionarItemOrdem() {
        ItemOrdemCompra itemOrdem = combItemOrdem.getSelectionModel().getSelectedItem();
        if (itemOrdem == null) {
            return;
        }
        System.out.println("Custo double: " + itemOrdem.getCustoUnitario() + " custo bigdecimal: " + itemOrdem.getCustoUnitarioDecimal());

        preencherCamposPeloItemOrdem(itemOrdem, false);
    }

    @FXML
    private void adicionarItem() {
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        Armazem armazem = combArmazem.getSelectionModel().getSelectedItem();
        ItemOrdemCompra itemOrdem = combItemOrdem.getSelectionModel().getSelectedItem();
        Integer quantidade = parseInteger(txtQuantidade.getText());
        Double custo = parseDouble(txtCustoUnitario.getText());
        if (produto == null || armazem == null || quantidade == null || quantidade <= 0 || custo == null || custo < 0) {
            showErroAlert("Seleccione produto, armazem, quantidade e custo validos!");
            return;
        }
        ItemRecebimentoCompra item = serviceRecebimentoCompra.novoItem(produto.getId(), armazem.getId(),
                quantidade, custo, txtCodigoLote.getText(), itemOrdem != null ? itemOrdem.getId() : null);
        if (item == null) {
            showErroAlert("Dados do item invalidos!");
            return;
        }
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setItemOrdemCompra(itemOrdem);
        item.setSubtotal(quantidade * custo);
        itensNovoRecebimento.add(item);
        txtQuantidade.clear();
        txtCustoUnitario.clear();
        txtCodigoLote.clear();
        combItemOrdem.getSelectionModel().clearSelection();
    }

    @FXML
    private void removerItem() {
        ItemRecebimentoCompra item = tabelaItensNova.getSelectionModel().getSelectedItem();
        if (item != null) {
            itensNovoRecebimento.remove(item);
        }
    }

    @FXML
    private void guardarRecebimento() {
        OrdemCompra ordem = combOrdemCompra.getSelectionModel().getSelectedItem();
        Fornecedor fornecedor = combFornecedor.getSelectionModel().getSelectedItem();
        if (ordem == null || fornecedor == null) {
            showErroAlert("Seleccione a ordem de compra e o fornecedor!");
            return;
        }
        RecebimentoCompra recebimento = new RecebimentoCompra();
        recebimento.setNumero(numeroRecebimento());
        recebimento.setOrdemCompra(ordem);
        recebimento.setFornecedor(fornecedor);
        recebimento.setDataRecebimento(LocalDateTime.now());
        recebimento.setDocumentoFornecedor(txtDocumentoFornecedor.getText());
        recebimento.setEstado("Rascunho");
        recebimento.setObservacao(txtObservacao.getText());
        recebimento.setUsuario(userData);
        try {
            serviceRecebimentoCompra.registaRecebimento(recebimento, FXCollections.observableArrayList(itensNovoRecebimento));
            if (serviceRecebimentoCompra.isOpsSuccess()) {
                showSuccessAlert("Recebimento registado com sucesso!");
                limparFormulario();
                atualizarDados();
                tabPaneRecebimentos.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void limparFormulario() {
        txtNumero.clear();
        txtDocumentoFornecedor.clear();
        txtObservacao.clear();
        txtQuantidade.clear();
        txtCustoUnitario.clear();
        txtCodigoLote.clear();
        combOrdemCompra.getSelectionModel().clearSelection();
        combFornecedor.getSelectionModel().clearSelection();
        combItemOrdem.getSelectionModel().clearSelection();
        combProduto.getSelectionModel().clearSelection();
        combArmazem.getSelectionModel().clearSelection();
        combItemOrdem.setItems(FXCollections.<ItemOrdemCompra>observableArrayList());
        itensNovoRecebimento.clear();
    }

    @FXML
    private void atualizarDados() {
        if (serviceRecebimentoCompra == null) {
            return;
        }
        String estado = combFiltroEstado != null ? combFiltroEstado.getSelectionModel().getSelectedItem() : null;
        listaRecebimentos = (estado == null || "Todos".equals(estado))
                ? serviceRecebimentoCompra.listaRecebimentos()
                : serviceRecebimentoCompra.listaRecebimentosPorEstado(estado);
        aplicarFiltroPesquisa();
    }

    @FXML
    private void selecionarRecebimento(MouseEvent event) {
        RecebimentoCompra recebimento = tabelaRecebimentos.getSelectionModel().getSelectedItem();
        if (recebimento != null) {
            setRecebimentoSelecionado(serviceRecebimentoCompra.consultaRecebimentoByID(recebimento.getId()));
        }
    }

    @FXML
    private void carregarDetalhesSelecionados() {
        if (hasRecebimentoSelecionado()) {
            setRecebimentoSelecionado(serviceRecebimentoCompra.consultaRecebimentoByID(recebimentoSelecionado.getId()));
        }
    }

    @FXML
    private void confirmarRecebimento() {
        if (hasRecebimentoSelecionado()) {
            try {
                if (serviceRecebimentoCompra.confirmarRecebimento(recebimentoSelecionado.getId())) {
                    showSuccessAlert("Recebimento confirmado e stock atualizado com sucesso!");
                    atualizarDados();
                    recarregarSelecionado();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void cancelarRecebimento() {
        if (hasRecebimentoSelecionado()) {
            try {
                if (serviceRecebimentoCompra.cancelarRecebimento(recebimentoSelecionado.getId())) {
                    showSuccessAlert("Recebimento cancelado com sucesso!");
                    atualizarDados();
                    recarregarSelecionado();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    private void inicializarServices() {
        try {
            serviceRecebimentoCompra = new ServiceRecebimentoCompra();
            serviceOrdemCompra = new ServiceOrdemCompra();
            serviceFornecedor = new ServiceFornecedor();
            servicoprodFornecido = new ServiceProdutoFornecido();
            serviceProduto = new ServiceProduto();
            serviceArmazem = new ServiceArmazem();
            servicePrecoProdutoArmazem = new ServicePrecoProdutoArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(RecebimentoCompraViewController.class.getName()).log(Level.SEVERE, null, ex);
            showErroAlert("Erro ao iniciar servicos de recebimento!");
        }
    }

    private void configurarCombos() {
        combFiltroEstado.setItems(FXCollections.observableArrayList("Todos", "Rascunho", "Confirmado", "Cancelado"));
        combFiltroEstado.getSelectionModel().select("Todos");
        combFiltroEstado.setOnAction(e -> atualizarDados());
        txtPesquisa.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroPesquisa());

        combOrdemCompra.setConverter(new StringConverter<OrdemCompra>() {
            @Override
            public String toString(OrdemCompra ordem) {
                return ordem == null ? "" : "#" + ordem.getId() + " - " + safe(ordem.getNumero()) + " - " + nomeFornecedor(ordem);
            }

            @Override
            public OrdemCompra fromString(String string) {
                return null;
            }
        });
        combFornecedor.setConverter(new StringConverter<Fornecedor>() {
            @Override
            public String toString(Fornecedor fornecedor) {
                return fornecedor == null ? "" : fornecedor.getId() + " - " + safe(fornecedor.getRazaosocial_forn());
            }

            @Override
            public Fornecedor fromString(String string) {
                return null;
            }
        });
        combItemOrdem.setConverter(new StringConverter<ItemOrdemCompra>() {
            @Override
            public String toString(ItemOrdemCompra item) {
                return item == null ? "" : "#" + item.getId() + " - " + nomeProduto(item) + " | pendente: " + item.getQuantidadePendente();
            }

            @Override
            public ItemOrdemCompra fromString(String string) {
                return null;
            }
        });
        combProduto.setConverter(new StringConverter<Produto>() {
            @Override
            public String toString(Produto produto) {
                return produto == null ? "" : produto.getId() + " - " + safe(produto.getNome());
            }

            @Override
            public Produto fromString(String string) {
                return null;
            }
        });
        combArmazem.setConverter(new StringConverter<Armazem>() {
            @Override
            public String toString(Armazem armazem) {
                return armazem == null ? "" : armazem.getId() + " - " + safe(armazem.getNome_arm());
            }

            @Override
            public Armazem fromString(String string) {
                return null;
            }
        });

        combProduto.setOnAction(e -> preencherItemOrdemPorProdutoSelecionado());
        combArmazem.setOnAction(e -> preencherItemOrdemPorProdutoSelecionado());

        if (serviceOrdemCompra != null) {
            combOrdemCompra.setItems(serviceOrdemCompra.listaOrdensCompraPorEstado("Enviada"));
        }
        if (serviceFornecedor != null) {
            combFornecedor.setItems(serviceFornecedor.listaTodosFornecedores());
        }
        if (serviceProduto != null) {
            combProduto.setItems(serviceProduto.listaTodosProdutos());
        }
        if (serviceArmazem != null) {
            combArmazem.setItems(serviceArmazem.listaTodosArmazens());
        }
    }

    private void configurarTabelas() {
        colItemProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colItemArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colItemQuantidade.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Integer>("quantidadeRecebida"));
        colItemCusto.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Double>("custoUnitario"));
        colItemLote.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, String>("codigoLote"));
        colItemSubtotal.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Double>("subtotal"));
        colItemEstado.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, String>("estado"));

        colRcId.setCellValueFactory(new PropertyValueFactory<RecebimentoCompra, Integer>("id"));
        colRcNumero.setCellValueFactory(new PropertyValueFactory<RecebimentoCompra, String>("numero"));
        colRcOrdem.setCellValueFactory(cell -> new SimpleStringProperty(numeroOrdem(cell.getValue())));
        colRcFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(nomeFornecedor(cell.getValue())));
        colRcDocumento.setCellValueFactory(new PropertyValueFactory<RecebimentoCompra, String>("documentoFornecedor"));
        colRcEstado.setCellValueFactory(new PropertyValueFactory<RecebimentoCompra, String>("estado"));
        colRcTotal.setCellValueFactory(new PropertyValueFactory<RecebimentoCompra, Double>("totalRecebido"));

        colDetalheProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colDetalheArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colDetalheQuantidade.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Integer>("quantidadeRecebida"));
        colDetalheCusto.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Double>("custoUnitario"));
        colDetalheLote.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, String>("codigoLote"));
        colDetalheSubtotal.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, Double>("subtotal"));
        colDetalheEstado.setCellValueFactory(new PropertyValueFactory<ItemRecebimentoCompra, String>("estado"));
    }

    private void aplicarFiltroPesquisa() {
        if (tabelaRecebimentos == null || listaRecebimentos == null) {
            return;
        }
        FilteredList<RecebimentoCompra> dadosFiltrados = new FilteredList<RecebimentoCompra>(listaRecebimentos, rec -> true);
        String filtro = txtPesquisa != null ? txtPesquisa.getText() : null;
        if (filtro != null && !filtro.trim().isEmpty()) {
            final String termo = filtro.trim().toLowerCase();
            dadosFiltrados.setPredicate(rec -> recebimentoContem(rec, termo));
        }
        SortedList<RecebimentoCompra> sortedData = new SortedList<RecebimentoCompra>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaRecebimentos.comparatorProperty());
        tabelaRecebimentos.setItems(sortedData);
    }

    private boolean recebimentoContem(RecebimentoCompra recebimento, String termo) {
        return String.valueOf(recebimento.getId()).contains(termo)
                || contains(recebimento.getNumero(), termo)
                || contains(recebimento.getEstado(), termo)
                || contains(recebimento.getDocumentoFornecedor(), termo)
                || contains(numeroOrdem(recebimento), termo)
                || contains(nomeFornecedor(recebimento), termo);
    }

    private void setRecebimentoSelecionado(RecebimentoCompra recebimento) {
        recebimentoSelecionado = recebimento;
        if (recebimento == null) {
            limparDetalhes();
            return;
        }
        labRcId.setText(String.valueOf(recebimento.getId()));
        labRcNumero.setText(safe(recebimento.getNumero()));
        labRcOrdem.setText(numeroOrdem(recebimento));
        labRcFornecedor.setText(nomeFornecedor(recebimento));
        labRcEstado.setText(safe(recebimento.getEstado()));
        labRcTotal.setText(String.valueOf(recebimento.getTotalRecebido()));
        labRcSelecionadoAux.setText("#" + recebimento.getId() + " - " + safe(recebimento.getNumero()));
        labResumoCabecalho.setText("Recebimento #" + recebimento.getId() + " seleccionado");
        tabelaItensDetalhe.setItems(serviceRecebimentoCompra != null
                ? serviceRecebimentoCompra.consultaItens(recebimento.getId())
                : FXCollections.<ItemRecebimentoCompra>observableArrayList());
    }

    private void recarregarSelecionado() {
        if (recebimentoSelecionado != null) {
            setRecebimentoSelecionado(serviceRecebimentoCompra.consultaRecebimentoByID(recebimentoSelecionado.getId()));
        }
    }

    private boolean hasRecebimentoSelecionado() {
        if (recebimentoSelecionado == null || recebimentoSelecionado.getId() <= 0) {
            showErroAlert("Seleccione um recebimento!");
            return false;
        }
        return true;
    }

    private void limparDetalhes() {
        labRcId.setText("-");
        labRcNumero.setText("-");
        labRcOrdem.setText("-");
        labRcFornecedor.setText("-");
        labRcEstado.setText("-");
        labRcTotal.setText("-");
        labRcSelecionadoAux.setText("-");
        tabelaItensDetalhe.setItems(FXCollections.<ItemRecebimentoCompra>observableArrayList());
    }

    private void preencherItemOrdemPorProdutoSelecionado() {
        if (sincronizandoItemOrdem) {
            return;
        }
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        if (produto == null || combItemOrdem.getItems() == null || combItemOrdem.getItems().isEmpty()) {
            preencherCustoProdutoArmazem();
            return;
        }
        ItemOrdemCompra itemOrdem = localizarItemOrdem(produto, combArmazem.getSelectionModel().getSelectedItem());
        if (itemOrdem != null) {
            preencherCamposPeloItemOrdem(itemOrdem, true);
            return;
        }
        preencherCustoProdutoArmazem();
    }

    private ItemOrdemCompra localizarItemOrdem(Produto produto, Armazem armazem) {
        ItemOrdemCompra primeiroProduto = null;
        for (ItemOrdemCompra item : combItemOrdem.getItems()) {
            if (item == null || item.getProduto() == null || item.getProduto().getId() != produto.getId()) {
                continue;
            }
            if (primeiroProduto == null && item.getQuantidadePendente() > 0) {
                primeiroProduto = item;
            }
            if (armazem != null && item.getArmazem() != null && item.getArmazem().getId() == armazem.getId()
                    && item.getQuantidadePendente() > 0) {
                return item;
            }
        }
        return primeiroProduto;
    }

    private void preencherCamposPeloItemOrdem(ItemOrdemCompra itemOrdem, boolean selecionarItemOrdem) {
        sincronizandoItemOrdem = true;
        try {
            if (selecionarItemOrdem) {
                combItemOrdem.getSelectionModel().select(itemOrdem);
            }
            selecionarComboProduto(itemOrdem.getProduto());
            selecionarComboArmazem(itemOrdem.getArmazem());
            txtQuantidade.setText(String.valueOf(Math.max(0, itemOrdem.getQuantidadePendente())));

            double custo = itemOrdem.getCustoUnitario();
//            System.out.println("Custo double: "+itemOrdem.getCustoUnitario()+" custo bigdecimal: "+itemOrdem.getCustoUnitarioDecimal());
            if (custo <= 0) {
                custo = custoProdutoArmazem(itemOrdem.getProduto(), itemOrdem.getArmazem());
            }
            txtCustoUnitario.setText(String.valueOf(custo));
            ProdutoFornecido pf = servicoprodFornecido.getLastCodeLote();
            txtCodigoLote.setText(pf == null ? "" : String.valueOf(pf.getCodigo()));
        } finally {
            sincronizandoItemOrdem = false;
        }
    }

    private void preencherCustoProdutoArmazem() {
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        Armazem armazem = combArmazem.getSelectionModel().getSelectedItem();
        double custo = custoProdutoArmazem(produto, armazem);
        if (custo > 0) {
            txtCustoUnitario.setText(String.valueOf(custo));
        }
    }

    private double custoProdutoArmazem(Produto produto, Armazem armazem) {
        if (produto == null || armazem == null || servicePrecoProdutoArmazem == null) {
            return 0.0;
        }
        ObservableList<PrecoProdutoArmazem> precos = servicePrecoProdutoArmazem.consultaPrecosVendas(produto.getId(), armazem.getId());
        if (precos == null || precos.isEmpty()) {
            return 0.0;
        }
        PrecoProdutoArmazem preco = precos.get(0);
        return preco != null && preco.getPrecoBase() != null ? preco.getPrecoBase() : 0.0;
    }

    private void limparCamposItemRecebimento() {
        combItemOrdem.getSelectionModel().clearSelection();
        combProduto.getSelectionModel().clearSelection();
        combArmazem.getSelectionModel().clearSelection();
        txtQuantidade.clear();
        txtCustoUnitario.clear();
        txtCodigoLote.clear();
    }

    private void selecionarComboProduto(Produto produto) {
        if (produto == null) {
            return;
        }
        for (Produto p : combProduto.getItems()) {
            if (p.getId() == produto.getId()) {
                combProduto.getSelectionModel().select(p);
                return;
            }
        }
    }

    private void selecionarComboFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return;
        }
        for (Fornecedor f : combFornecedor.getItems()) {
            if (f.getId() == fornecedor.getId()) {
                combFornecedor.getSelectionModel().select(f);
                return;
            }
        }
        combFornecedor.getSelectionModel().select(fornecedor);
    }

    private void selecionarComboArmazem(Armazem armazem) {
        if (armazem == null) {
            return;
        }
        for (Armazem a : combArmazem.getItems()) {
            if (a.getId() == armazem.getId()) {
                combArmazem.getSelectionModel().select(a);
                return;
            }
        }
    }

    private String numeroRecebimento() {
        String numero = txtNumero.getText();
        if (numero == null || numero.trim().isEmpty()) {
            return "RC-" + System.currentTimeMillis();
        }
        return numero.trim();
    }

    private Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Double.valueOf(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean contains(String value, String termo) {
        return value != null && value.toLowerCase().contains(termo);
    }

    private String numeroOrdem(RecebimentoCompra recebimento) {
        return recebimento != null && recebimento.getOrdemCompra() != null ? safe(recebimento.getOrdemCompra().getNumero()) : "";
    }

    private String nomeFornecedor(OrdemCompra ordem) {
        return ordem != null && ordem.getFornecedor() != null ? safe(ordem.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeFornecedor(RecebimentoCompra recebimento) {
        return recebimento != null && recebimento.getFornecedor() != null ? safe(recebimento.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeProduto(ItemOrdemCompra item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    private String nomeProduto(ItemRecebimentoCompra item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    private String nomeArmazem(ItemRecebimentoCompra item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

