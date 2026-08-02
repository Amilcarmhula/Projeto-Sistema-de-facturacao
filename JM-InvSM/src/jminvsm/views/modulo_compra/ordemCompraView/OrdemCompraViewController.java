package jminvsm.views.modulo_compra.ordemCompraView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.compra.ServiceOrdemCompra;
import jminvsm.service.ServiceException;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.requisicao.ServiceRequisicao;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class OrdemCompraViewController implements Initializable {

    private ServiceOrdemCompra serviceOrdemCompra;
    private ServiceRequisicao serviceRequisicao;
    private ServiceFornecedor serviceFornecedor;
    private ServiceProduto serviceProduto;
    private ServiceArmazem serviceArmazem;
    private Usuario userData;

    private final ObservableList<ItemOrdemCompra> itensNovaOrdem = FXCollections.observableArrayList();
    private ObservableList<OrdemCompra> listaOrdens = FXCollections.observableArrayList();
    private OrdemCompra ordemSelecionada;

    @FXML
    private TabPane tabPaneOrdens;
    @FXML
    private Label labResumoCabecalho;
    @FXML
    private TextField txtNumero;
    @FXML
    private ComboBox<Fornecedor> combFornecedor;
    @FXML
    private ComboBox<Requisicao> combRequisicao;
    @FXML
    private DatePicker dataPrevistaPicker;
    @FXML
    private TextArea txtObservacao;
    @FXML
    private ComboBox<Produto> combProduto;
    @FXML
    private ComboBox<Armazem> combArmazem;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtCustoUnitario;
    @FXML
    private TableView<ItemOrdemCompra> tabelaItensNova;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colItemProduto;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colItemArmazem;
    @FXML
    private TableColumn<ItemOrdemCompra, Integer> colItemQuantidade;
    @FXML
    private TableColumn<ItemOrdemCompra, Double> colItemCusto;
    @FXML
    private TableColumn<ItemOrdemCompra, Double> colItemTotal;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colItemEstado;

    @FXML
    private TextField txtPesquisa;
    @FXML
    private ComboBox<String> combFiltroEstado;
    @FXML
    private TableView<OrdemCompra> tabelaOrdens;
    @FXML
    private TableColumn<OrdemCompra, Integer> colOcId;
    @FXML
    private TableColumn<OrdemCompra, String> colOcNumero;
    @FXML
    private TableColumn<OrdemCompra, String> colOcFornecedor;
    @FXML
    private TableColumn<OrdemCompra, String> colOcEmissao;
    @FXML
    private TableColumn<OrdemCompra, String> colOcEntrega;
    @FXML
    private TableColumn<OrdemCompra, String> colOcEstado;
    @FXML
    private TableColumn<OrdemCompra, Double> colOcTotal;

    @FXML
    private Label labOcId;
    @FXML
    private Label labOcNumero;
    @FXML
    private Label labOcFornecedor;
    @FXML
    private Label labOcEstado;
    @FXML
    private Label labOcTotal;
    @FXML
    private Label labOcSelecionadaAux;

    @FXML
    private TableView<ItemOrdemCompra> tabelaItensDetalhe;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colDetalheProduto;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colDetalheArmazem;
    @FXML
    private TableColumn<ItemOrdemCompra, Integer> colDetalheSolicitada;
    @FXML
    private TableColumn<ItemOrdemCompra, Integer> colDetalheRecebida;
    @FXML
    private TableColumn<ItemOrdemCompra, Integer> colDetalhePendente;
    @FXML
    private TableColumn<ItemOrdemCompra, Double> colDetalheCusto;
    @FXML
    private TableColumn<ItemOrdemCompra, Double> colDetalheTotal;
    @FXML
    private TableColumn<ItemOrdemCompra, String> colDetalheEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaItensNova.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOrdens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensDetalhe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userData = SessaoAplicacao.getUserData();
        inicializarServices();
        configurarCombos();
        configurarTabelas();
        dataPrevistaPicker.setValue(LocalDate.now().plusDays(7));
        tabelaItensNova.setItems(itensNovaOrdem);
        atualizarDados();
    }

    @FXML
    private void adicionarItem() {
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        Armazem armazem = combArmazem.getSelectionModel().getSelectedItem();
        Integer quantidade = parseInteger(txtQuantidade.getText());
        Double custo = parseDouble(txtCustoUnitario.getText());
        if (produto == null || armazem == null || quantidade == null || quantidade <= 0 || custo == null || custo < 0) {
            showErroAlert("Seleccione produto, armazem, quantidade e custo validos!");
            return;
        }
        ItemOrdemCompra item = serviceOrdemCompra.novoItem(produto.getId(), armazem.getId(), quantidade, custo, null);
        if (item == null) {
            showErroAlert("Dados do item invalidos!");
            return;
        }
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setCustoUnitario(custo);
        item.setSubtotal(quantidade * custo);
        item.setTotalLinha(quantidade * custo);
        itensNovaOrdem.add(item);
        txtQuantidade.clear();
        txtCustoUnitario.clear();
        combProduto.requestFocus();
    }

    @FXML
    private void removerItem() {
        ItemOrdemCompra item = tabelaItensNova.getSelectionModel().getSelectedItem();
        if (item != null) {
            itensNovaOrdem.remove(item);
        }
    }

    @FXML
    private void guardarOrdemCompra() {
        if (serviceOrdemCompra == null) {
            showErroAlert("Servico de ordens de compra indisponivel!");
            return;
        }
        Fornecedor fornecedor = combFornecedor.getSelectionModel().getSelectedItem();
        if (fornecedor == null) {
            showErroAlert("Seleccione o fornecedor!");
            return;
        }
        OrdemCompra ordem = new OrdemCompra();
        ordem.setNumero(numeroOrdem());
        ordem.setFornecedor(fornecedor);
        ordem.setRequisicao(combRequisicao.getSelectionModel().getSelectedItem());
        ordem.setDataEmissao(LocalDate.now());
        ordem.setDataPrevistaEntrega(dataPrevistaPicker.getValue());
        ordem.setEstado("Rascunho");
        ordem.setObservacao(txtObservacao.getText());
        ordem.setUsuario(userData);
        try {
            serviceOrdemCompra.registaOrdemCompra(ordem, FXCollections.observableArrayList(itensNovaOrdem));
            if (serviceOrdemCompra.isOpsSuccess()) {
                showSuccessAlert("Ordem de compra registada com sucesso!");
                limparFormulario();
                atualizarDados();
                tabPaneOrdens.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void criarDeRequisicao() {
        Requisicao requisicao = combRequisicao.getSelectionModel().getSelectedItem();
        Fornecedor fornecedor = combFornecedor.getSelectionModel().getSelectedItem();
        int idUsuario = userData != null ? userData.getId() : 0;
        if (requisicao == null || fornecedor == null) {
            showErroAlert("Seleccione a requisicao aprovada e o fornecedor!");
            return;
        }
        if (!"Aprovada".equalsIgnoreCase(safe(requisicao.getEstado()))) {
            showErroAlert("Apenas requisicoes aprovadas podem gerar ordem de compra!");
            return;
        }
        if (idUsuario <= 0) {
            showErroAlert("Utilizador nao informado!");
            return;
        }
        if (!garantirItensDaRequisicaoCarregados(requisicao)) {
            return;
        }
        if (!validarCustosItensRequisicao()) {
            return;
        }

        OrdemCompra ordem = new OrdemCompra();
        ordem.setNumero(numeroOrdem());
        ordem.setFornecedor(fornecedor);
        ordem.setRequisicao(requisicao);
        ordem.setDataEmissao(LocalDate.now());
        ordem.setDataPrevistaEntrega(dataPrevistaPicker.getValue());
        ordem.setEstado("Aprovada");
        ordem.setObservacao(txtObservacao.getText() == null || txtObservacao.getText().trim().isEmpty()
                ? "Gerada a partir de requisicao aprovada"
                : txtObservacao.getText());
        ordem.setUsuario(userData);

        try {
            serviceOrdemCompra.registaOrdemCompra(ordem, FXCollections.observableArrayList(itensNovaOrdem));
            if (serviceOrdemCompra.isOpsSuccess()) {
                showSuccessAlert("Ordem de compra criada a partir da requisicao com sucesso!");
                limparFormulario();
                atualizarDados();
                tabPaneOrdens.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void limparFormulario() {
        txtNumero.clear();
        txtObservacao.clear();
        txtQuantidade.clear();
        txtCustoUnitario.clear();
        dataPrevistaPicker.setValue(LocalDate.now().plusDays(7));
        combFornecedor.getSelectionModel().clearSelection();
        combRequisicao.getSelectionModel().clearSelection();
        combProduto.getSelectionModel().clearSelection();
        combArmazem.getSelectionModel().clearSelection();
        itensNovaOrdem.clear();
    }

    @FXML
    private void atualizarDados() {
        if (serviceOrdemCompra == null) {
            return;
        }
        String estado = combFiltroEstado != null ? combFiltroEstado.getSelectionModel().getSelectedItem() : null;
        listaOrdens = (estado == null || "Todos".equals(estado))
                ? serviceOrdemCompra.listaOrdensCompra()
                : serviceOrdemCompra.listaOrdensCompraPorEstado(estado);
        aplicarFiltroPesquisa();
    }

    @FXML
    private void selecionarOrdem(MouseEvent event) {
        OrdemCompra ordem = tabelaOrdens.getSelectionModel().getSelectedItem();
        if (ordem != null) {
            setOrdemSelecionada(serviceOrdemCompra.consultaOrdemCompraByID(ordem.getId()));
        }
    }

    @FXML
    private void carregarDetalhesSelecionados() {
        if (hasOrdemSelecionada()) {
            setOrdemSelecionada(serviceOrdemCompra.consultaOrdemCompraByID(ordemSelecionada.getId()));
        }
    }

    @FXML
    private void enviarOrdemCompra() {
        if (hasOrdemSelecionada()) {
            try {
                if (serviceOrdemCompra.enviarOrdemCompra(ordemSelecionada.getId())) {
                    showSuccessAlert("Ordem de compra marcada como enviada!");
                    atualizarDados();
                    recarregarSelecionada();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void cancelarOrdemCompra() {
        if (hasOrdemSelecionada()) {
            try {
                if (serviceOrdemCompra.cancelarOrdemCompra(ordemSelecionada.getId())) {
                    showSuccessAlert("Ordem de compra cancelada com sucesso!");
                    atualizarDados();
                    recarregarSelecionada();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    private void inicializarServices() {
        try {
            serviceOrdemCompra = new ServiceOrdemCompra();
            serviceRequisicao = new ServiceRequisicao();
            serviceFornecedor = new ServiceFornecedor();
            serviceProduto = new ServiceProduto();
            serviceArmazem = new ServiceArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(OrdemCompraViewController.class.getName()).log(Level.SEVERE, null, ex);
            showErroAlert("Erro ao iniciar servicos de ordem de compra!");
        }
    }

    private void configurarCombos() {
        combFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "Rascunho", "Submetida", "Aprovada", "Enviada",
                "ParcialmenteRecebida", "Recebida", "Cancelada"));
        combFiltroEstado.getSelectionModel().select("Todos");
        combFiltroEstado.setOnAction(e -> atualizarDados());
        txtPesquisa.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroPesquisa());

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
        combRequisicao.setConverter(new StringConverter<Requisicao>() {
            @Override
            public String toString(Requisicao requisicao) {
                return requisicao == null ? "" : "#" + requisicao.getId() + " - " + safe(requisicao.getEstado());
            }

            @Override
            public Requisicao fromString(String string) {
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

        if (serviceFornecedor != null) {
            combFornecedor.setItems(serviceFornecedor.listaTodosFornecedores());
        }
        if (serviceRequisicao != null) {
            combRequisicao.setItems(serviceRequisicao.listaRequisicoesPorEstado("Aprovada"));
            combRequisicao.setOnAction(e -> carregarItensRequisicaoSelecionada());
        }
        if (serviceProduto != null) {
            combProduto.setItems(serviceProduto.listaTodosProdutos());
        }
        if (serviceArmazem != null) {
            combArmazem.setItems(serviceArmazem.listaTodosArmazens());
        }
    }

    private void configurarTabelas() {
        tabelaItensNova.setEditable(true);
        colItemProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colItemArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colItemQuantidade.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Integer>("quantidadeSolicitada"));
        colItemCusto.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Double>("custoUnitario"));
        colItemCusto.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colItemCusto.setOnEditCommit(event -> {
            ItemOrdemCompra item = event.getRowValue();
            Double custo = event.getNewValue();
            if (item == null || custo == null || custo < 0) {
                tabelaItensNova.refresh();
                return;
            }
            item.setCustoUnitario(custo);
            actualizarTotaisItem(item);
            tabelaItensNova.refresh();
        });
        colItemTotal.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Double>("totalLinha"));
        colItemEstado.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, String>("estado"));

        colOcId.setCellValueFactory(new PropertyValueFactory<OrdemCompra, Integer>("id"));
        colOcNumero.setCellValueFactory(new PropertyValueFactory<OrdemCompra, String>("numero"));
        colOcFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(nomeFornecedor(cell.getValue())));
        colOcEmissao.setCellValueFactory(new PropertyValueFactory<OrdemCompra, String>("dataEmissao"));
        colOcEntrega.setCellValueFactory(new PropertyValueFactory<OrdemCompra, String>("dataPrevistaEntrega"));
        colOcEstado.setCellValueFactory(new PropertyValueFactory<OrdemCompra, String>("estado"));
        colOcTotal.setCellValueFactory(new PropertyValueFactory<OrdemCompra, Double>("total"));

        colDetalheProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colDetalheArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colDetalheSolicitada.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Integer>("quantidadeSolicitada"));
        colDetalheRecebida.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Integer>("quantidadeRecebida"));
        colDetalhePendente.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Integer>("quantidadePendente"));
        colDetalheCusto.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Double>("custoUnitario"));
        colDetalheTotal.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, Double>("totalLinha"));
        colDetalheEstado.setCellValueFactory(new PropertyValueFactory<ItemOrdemCompra, String>("estado"));
    }

    private void aplicarFiltroPesquisa() {
        if (tabelaOrdens == null || listaOrdens == null) {
            return;
        }
        FilteredList<OrdemCompra> dadosFiltrados = new FilteredList<OrdemCompra>(listaOrdens, ordem -> true);
        String filtro = txtPesquisa != null ? txtPesquisa.getText() : null;
        if (filtro != null && !filtro.trim().isEmpty()) {
            final String termo = filtro.trim().toLowerCase();
            dadosFiltrados.setPredicate(ordem -> ordemContem(ordem, termo));
        }
        SortedList<OrdemCompra> sortedData = new SortedList<OrdemCompra>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaOrdens.comparatorProperty());
        tabelaOrdens.setItems(sortedData);
    }

    private boolean ordemContem(OrdemCompra ordem, String termo) {
        return String.valueOf(ordem.getId()).contains(termo)
                || contains(ordem.getNumero(), termo)
                || contains(ordem.getEstado(), termo)
                || contains(nomeFornecedor(ordem), termo)
                || contains(ordem.getDataEmissao(), termo)
                || contains(ordem.getDataPrevistaEntrega(), termo);
    }

    private void setOrdemSelecionada(OrdemCompra ordem) {
        ordemSelecionada = ordem;
        if (ordem == null) {
            limparDetalhes();
            return;
        }
        labOcId.setText(String.valueOf(ordem.getId()));
        labOcNumero.setText(safe(ordem.getNumero()));
        labOcFornecedor.setText(nomeFornecedor(ordem));
        labOcEstado.setText(safe(ordem.getEstado()));
        labOcTotal.setText(String.valueOf(ordem.getTotal()));
        labOcSelecionadaAux.setText("#" + ordem.getId() + " - " + safe(ordem.getNumero()));
        labResumoCabecalho.setText("Ordem #" + ordem.getId() + " seleccionada");
        tabelaItensDetalhe.setItems(serviceOrdemCompra != null
                ? serviceOrdemCompra.consultaItens(ordem.getId())
                : FXCollections.<ItemOrdemCompra>observableArrayList());
    }

    private void recarregarSelecionada() {
        if (ordemSelecionada != null) {
            setOrdemSelecionada(serviceOrdemCompra.consultaOrdemCompraByID(ordemSelecionada.getId()));
        }
    }

    private void carregarItensRequisicaoSelecionada() {
        Requisicao requisicao = combRequisicao.getSelectionModel().getSelectedItem();
        if (requisicao == null || serviceRequisicao == null) {
            itensNovaOrdem.clear();
            return;
        }
        ObservableList<ItemRequisicao> itensRequisicao = serviceRequisicao.consultaItens(requisicao.getId());
        itensNovaOrdem.clear();
        for (ItemRequisicao itemRequisicao : itensRequisicao) {
            ItemOrdemCompra item = serviceOrdemCompra.novoItem(
                    itemRequisicao.getProduto().getId(),
                    itemRequisicao.getArmazem().getId(),
                    itemRequisicao.getQuantidade(),
                    0.00,
                    itemRequisicao.getId());
            if (item != null) {
                item.setProduto(itemRequisicao.getProduto());
                item.setArmazem(itemRequisicao.getArmazem());
                item.setItemRequisicao(itemRequisicao);
                actualizarTotaisItem(item);
                itensNovaOrdem.add(item);
            }
        }
    }

    private boolean garantirItensDaRequisicaoCarregados(Requisicao requisicao) {
        if (itensNovaOrdem.isEmpty()) {
            carregarItensRequisicaoSelecionada();
        }
        if (itensNovaOrdem.isEmpty()) {
            showErroAlert("A requisicao seleccionada nao possui itens para ordem de compra!");
            return false;
        }
        for (ItemOrdemCompra item : itensNovaOrdem) {
            if (item.getItemRequisicao() == null || item.getItemRequisicao().getId() <= 0) {
                showErroAlert("Os itens da ordem devem estar vinculados aos itens da requisicao!");
                return false;
            }
            if (item.getItemRequisicao().getRequisicao() == null
                    || item.getItemRequisicao().getRequisicao().getId() != requisicao.getId()) {
                showErroAlert("Existem itens que nao pertencem a requisicao seleccionada!");
                return false;
            }
        }
        return true;
    }

    private boolean validarCustosItensRequisicao() {
        for (ItemOrdemCompra item : itensNovaOrdem) {
            if (item.getCustoUnitario() <= 0) {
                showErroAlert("Informe custo unitario maior que zero para todos os itens da requisicao!");
                tabelaItensNova.getSelectionModel().select(item);
                tabelaItensNova.scrollTo(item);
                return false;
            }
            actualizarTotaisItem(item);
        }
        tabelaItensNova.refresh();
        return true;
    }

    private void actualizarTotaisItem(ItemOrdemCompra item) {
        if (item == null) {
            return;
        }
        double subtotal = item.getQuantidadeSolicitada() * item.getCustoUnitario();
        item.setSubtotal(subtotal);
        item.setTotalLinha(subtotal - item.getDesconto() + item.getImposto());
    }

    private boolean hasOrdemSelecionada() {
        if (ordemSelecionada == null || ordemSelecionada.getId() <= 0) {
            showErroAlert("Seleccione uma ordem de compra!");
            return false;
        }
        return true;
    }

    private void limparDetalhes() {
        labOcId.setText("-");
        labOcNumero.setText("-");
        labOcFornecedor.setText("-");
        labOcEstado.setText("-");
        labOcTotal.setText("-");
        labOcSelecionadaAux.setText("-");
        tabelaItensDetalhe.setItems(FXCollections.<ItemOrdemCompra>observableArrayList());
    }

    private String numeroOrdem() {
        String numero = txtNumero.getText();
        if (numero == null || numero.trim().isEmpty()) {
            return "OC-" + System.currentTimeMillis();
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

    private String nomeFornecedor(OrdemCompra ordem) {
        return ordem != null && ordem.getFornecedor() != null ? safe(ordem.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeProduto(ItemOrdemCompra item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    private String nomeArmazem(ItemOrdemCompra item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

