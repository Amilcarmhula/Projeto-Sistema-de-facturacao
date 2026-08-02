package jminvsm.views.modulo_compra.requisicaoView;

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
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.movimento.Movimento;
import jminvsm.model.picking.Picking;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.reserva.ReservaStock;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.ServiceException;
import jminvsm.service.movimento.ServiceMovimento;
import jminvsm.service.picking.ServicePicking;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.requisicao.ServiceRequisicao;
import jminvsm.service.reserva.ServiceReservaStock;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class RequisicaoViewController implements Initializable {

    private ServiceRequisicao serviceRequisicao;
    private ServiceProduto serviceProduto;
    private ServiceArmazem serviceArmazem;
    private ServiceReservaStock serviceReservaStock;
    private ServicePicking servicePicking;
    private ServiceMovimento serviceMovimento;
    private Usuario userData;

    private final ObservableList<ItemRequisicao> itensNovaRequisicao = FXCollections.observableArrayList();
    private ObservableList<Requisicao> listaRequisicoes = FXCollections.observableArrayList();
    private Requisicao requisicaoSelecionada;

    @FXML
    private TabPane tabPaneRequisicoes;
    @FXML
    private Label labResumoCabecalho;
    @FXML
    private DatePicker dataNecessidadePicker;
    @FXML
    private TextArea txtMotivo;
    @FXML
    private ComboBox<Produto> combProduto;
    @FXML
    private ComboBox<Armazem> combArmazem;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TableView<ItemRequisicao> tabelaItensNova;
    @FXML
    private TableColumn<ItemRequisicao, String> colItemProduto;
    @FXML
    private TableColumn<ItemRequisicao, String> colItemArmazem;
    @FXML
    private TableColumn<ItemRequisicao, Integer> colItemQuantidade;

    @FXML
    private TextField txtPesquisa;
    @FXML
    private ComboBox<String> combFiltroEstado;
    @FXML
    private TableView<Requisicao> tabelaRequisicoes;
    @FXML
    private TableColumn<Requisicao, Integer> colReqId;
    @FXML
    private TableColumn<Requisicao, String> colReqData;
    @FXML
    private TableColumn<Requisicao, String> colReqNecessidade;
    @FXML
    private TableColumn<Requisicao, String> colReqEstado;
    @FXML
    private TableColumn<Requisicao, String> colReqMotivo;

    @FXML
    private Label labReqId;
    @FXML
    private Label labReqEstado;
    @FXML
    private Label labReqData;
    @FXML
    private Label labReqNecessidade;
    @FXML
    private Label labReqMotivo;
    @FXML
    private Label labReqSelecionadaAux;

    @FXML
    private TableView<ItemRequisicao> tabelaItensDetalhe;
    @FXML
    private TableColumn<ItemRequisicao, String> colDetalheProduto;
    @FXML
    private TableColumn<ItemRequisicao, String> colDetalheArmazem;
    @FXML
    private TableColumn<ItemRequisicao, Integer> colDetalheQuantidade;

    @FXML
    private TableView<ReservaStock> tabelaReservas;
    @FXML
    private TableColumn<ReservaStock, String> colReservaProduto;
    @FXML
    private TableColumn<ReservaStock, String> colReservaArmazem;
    @FXML
    private TableColumn<ReservaStock, Integer> colReservaQuantidade;
    @FXML
    private TableColumn<ReservaStock, String> colReservaEstado;

    @FXML
    private Label labPickingInfo;
    @FXML
    private TableView<Movimento> tabelaMovimentos;
    @FXML
    private TableColumn<Movimento, String> colMovData;
    @FXML
    private TableColumn<Movimento, String> colMovOrigem;
    @FXML
    private TableColumn<Movimento, String> colMovTipo;
    @FXML
    private TableColumn<Movimento, String> colMovProduto;
    @FXML
    private TableColumn<Movimento, String> colMovArmazem;
    @FXML
    private TableColumn<Movimento, Integer> colMovQuantidade;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaItensNova.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaRequisicoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensDetalhe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaReservas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaMovimentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userData = SessaoAplicacao.getUserData();
        inicializarServices();
        configurarCombos();
        configurarTabelas();
        dataNecessidadePicker.setValue(LocalDate.now().plusDays(1));
        tabelaItensNova.setItems(itensNovaRequisicao);
        atualizarDados();
    }

    @FXML
    private void adicionarItem() {
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        Armazem armazem = combArmazem.getSelectionModel().getSelectedItem();
        Integer quantidade = parseInteger(txtQuantidade.getText());
        if (produto == null || armazem == null || quantidade == null || quantidade <= 0) {
            showErroAlert("Seleccione produto, armazem e quantidade valida!");
            return;
        }
        ItemRequisicao item = serviceRequisicao.novoItem(produto.getId(), armazem.getId(), quantidade);
        if (item == null) {
            showErroAlert("Dados do item invalidos!");
            return;
        }
        item.setProduto(produto);
        item.setArmazem(armazem);
        itensNovaRequisicao.add(item);
        txtQuantidade.clear();
        combProduto.requestFocus();
    }

    @FXML
    private void removerItem() {
        ItemRequisicao item = tabelaItensNova.getSelectionModel().getSelectedItem();
        if (item != null) {
            itensNovaRequisicao.remove(item);
        }
    }

    @FXML
    private void guardarRequisicao() {
        if (serviceRequisicao == null) {
            showErroAlert("Servico de requisicoes indisponivel!");
            return;
        }
        LocalDate dataNecessidade = dataNecessidadePicker.getValue();
        try {
            serviceRequisicao.registaRequisicao(dataNecessidade != null ? dataNecessidade.toString() : null,
                    txtMotivo.getText(), FXCollections.observableArrayList(itensNovaRequisicao));
            if (serviceRequisicao.isOpsSuccess()) {
                showSuccessAlert("Requisicao registada com sucesso!");
                limparFormulario();
                atualizarDados();
                tabPaneRequisicoes.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void limparFormulario() {
        txtMotivo.clear();
        txtQuantidade.clear();
        dataNecessidadePicker.setValue(LocalDate.now().plusDays(1));
        combProduto.getSelectionModel().clearSelection();
        combArmazem.getSelectionModel().clearSelection();
        itensNovaRequisicao.clear();
    }

    @FXML
    private void atualizarDados() {
        if (serviceRequisicao == null) {
            return;
        }
        String estado = combFiltroEstado != null ? combFiltroEstado.getSelectionModel().getSelectedItem() : null;
        listaRequisicoes = (estado == null || "Todos".equals(estado))
                ? serviceRequisicao.listaRequisicoes()
                : serviceRequisicao.listaRequisicoesPorEstado(estado);
        aplicarFiltroPesquisa();
    }

    @FXML
    private void selecionarRequisicao(MouseEvent event) {
        Requisicao requisicao = tabelaRequisicoes.getSelectionModel().getSelectedItem();
        if (requisicao != null) {
            setRequisicaoSelecionada(serviceRequisicao.consultaRequisicaoByID(requisicao.getId()));
        }
    }

    @FXML
    private void carregarDetalhesSelecionados() {
        if (!hasRequisicaoSelecionada()) {
            return;
        }
        setRequisicaoSelecionada(serviceRequisicao.consultaRequisicaoByID(requisicaoSelecionada.getId()));
    }

    @FXML
    private void submeterRequisicao() {
        if (hasRequisicaoSelecionada()) {
            try {
                if (serviceRequisicao.submeterRequisicao(requisicaoSelecionada.getId())) {
                    showSuccessAlert("Requisicao submetida com sucesso!");
                    atualizarDados();
                    recarregarSelecionada();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void aprovarRequisicao() {
        if (hasRequisicaoSelecionada()) {
            try {
                if (serviceRequisicao.aprovarRequisicao(requisicaoSelecionada.getId())) {
                    showSuccessAlert("Requisicao aprovada e stock reservado com sucesso!");
                    atualizarDados();
                    recarregarSelecionada();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void iniciarPicking() {
        if (!hasRequisicaoSelecionada()) {
            return;
        }
        if (userData == null) {
            showErroAlert("Utilizador nao encontrado na sessao!");
            return;
        }
        try {
            servicePicking.iniciaPickingRequisicao(requisicaoSelecionada.getId(), userData);
            if (servicePicking.isOpsSuccess()) {
                showSuccessAlert("Picking iniciado e requisicao marcada como EmPicking!");
                atualizarDados();
                recarregarSelecionada();
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void confirmarPicking() {
        if (hasRequisicaoSelecionada()) {
            try {
                if (serviceRequisicao.confirmarPicking(requisicaoSelecionada.getId())) {
                    showSuccessAlert("Picking confirmado e movimentos registados com sucesso!");
                    atualizarDados();
                    recarregarSelecionada();
                }
            } catch (ServiceException ex) {
                showErroAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void cancelarRequisicao() {
        if (hasRequisicaoSelecionada()) {
            try {
                if (serviceRequisicao.cancelarRequisicao(requisicaoSelecionada.getId())) {
                    showSuccessAlert("Requisicao cancelada e reservas libertadas com sucesso!");
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
            serviceRequisicao = new ServiceRequisicao();
            serviceProduto = new ServiceProduto();
            serviceArmazem = new ServiceArmazem();
            serviceReservaStock = new ServiceReservaStock();
            servicePicking = new ServicePicking();
            serviceMovimento = new ServiceMovimento();
        } catch (SQLException ex) {
            Logger.getLogger(RequisicaoViewController.class.getName()).log(Level.SEVERE, null, ex);
            showErroAlert("Erro ao iniciar servicos do modulo de requisicoes!");
        }
    }

    private void configurarCombos() {
        combFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "Pendente", "Submetida", "AguardandoAprovacao", "Aprovada",
                "EmPicking", "Finalizada", "Cancelada"));
        combFiltroEstado.getSelectionModel().select("Todos");
        combFiltroEstado.setOnAction(e -> atualizarDados());

        txtPesquisa.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroPesquisa());

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
        colItemQuantidade.setCellValueFactory(new PropertyValueFactory<ItemRequisicao, Integer>("quantidade"));

        colReqId.setCellValueFactory(new PropertyValueFactory<Requisicao, Integer>("id"));
        colReqData.setCellValueFactory(new PropertyValueFactory<Requisicao, String>("dataRequisicao"));
        colReqNecessidade.setCellValueFactory(new PropertyValueFactory<Requisicao, String>("dataNecessidade"));
        colReqEstado.setCellValueFactory(new PropertyValueFactory<Requisicao, String>("estado"));
        colReqMotivo.setCellValueFactory(new PropertyValueFactory<Requisicao, String>("motivo"));

        colDetalheProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colDetalheArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colDetalheQuantidade.setCellValueFactory(new PropertyValueFactory<ItemRequisicao, Integer>("quantidade"));

        colReservaProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colReservaArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colReservaQuantidade.setCellValueFactory(new PropertyValueFactory<ReservaStock, Integer>("quantidade"));
        colReservaEstado.setCellValueFactory(new PropertyValueFactory<ReservaStock, String>("estado"));

        colMovData.setCellValueFactory(new PropertyValueFactory<Movimento, String>("dataMovimento"));
        colMovOrigem.setCellValueFactory(new PropertyValueFactory<Movimento, String>("tipoOrigem"));
        colMovTipo.setCellValueFactory(new PropertyValueFactory<Movimento, String>("tipoMovimento"));
        colMovProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colMovArmazem.setCellValueFactory(cell -> new SimpleStringProperty(nomeArmazem(cell.getValue())));
        colMovQuantidade.setCellValueFactory(new PropertyValueFactory<Movimento, Integer>("quantidade"));
    }

    private void aplicarFiltroPesquisa() {
        if (tabelaRequisicoes == null || listaRequisicoes == null) {
            return;
        }
        FilteredList<Requisicao> dadosFiltrados = new FilteredList<Requisicao>(listaRequisicoes, req -> true);
        String filtro = txtPesquisa != null ? txtPesquisa.getText() : null;
        if (filtro != null && !filtro.trim().isEmpty()) {
            final String termo = filtro.trim().toLowerCase();
            dadosFiltrados.setPredicate(req -> requisicaoContem(req, termo));
        }
        SortedList<Requisicao> sortedData = new SortedList<Requisicao>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaRequisicoes.comparatorProperty());
        tabelaRequisicoes.setItems(sortedData);
    }

    private boolean requisicaoContem(Requisicao req, String termo) {
        return String.valueOf(req.getId()).contains(termo)
                || contains(req.getEstado(), termo)
                || contains(req.getMotivo(), termo)
                || contains(req.getDataRequisicao(), termo)
                || contains(req.getDataNecessidade(), termo);
    }

    private void setRequisicaoSelecionada(Requisicao requisicao) {
        requisicaoSelecionada = requisicao;
        if (requisicao == null) {
            limparDetalhes();
            return;
        }
        labReqId.setText(String.valueOf(requisicao.getId()));
        labReqEstado.setText(safe(requisicao.getEstado()));
        labReqData.setText(safe(requisicao.getDataRequisicao()));
        labReqNecessidade.setText(safe(requisicao.getDataNecessidade()));
        labReqMotivo.setText(safe(requisicao.getMotivo()));
        labReqSelecionadaAux.setText("#" + requisicao.getId() + " - " + safe(requisicao.getEstado()));
        labResumoCabecalho.setText("Requisicao #" + requisicao.getId() + " seleccionada");

        tabelaItensDetalhe.setItems(requisicao.getItens() != null
                ? FXCollections.observableArrayList(requisicao.getItens())
                : FXCollections.<ItemRequisicao>observableArrayList());

        tabelaReservas.setItems(serviceReservaStock != null
                ? serviceReservaStock.consultaReservasRequisicao(requisicao.getId())
                : FXCollections.<ReservaStock>observableArrayList());

        tabelaMovimentos.setItems(serviceMovimento != null
                ? serviceMovimento.consultaMovimentosRequisicao(requisicao.getId())
                : FXCollections.<Movimento>observableArrayList());

        atualizarInfoPicking(requisicao.getId());
    }

    private void atualizarInfoPicking(int idRequisicao) {
        if (servicePicking == null) {
            labPickingInfo.setText("Picking indisponivel");
            return;
        }
        Picking picking = servicePicking.consultaPickingRequisicao(idRequisicao);
        if (picking == null) {
            labPickingInfo.setText("Sem picking registado");
            return;
        }
        labPickingInfo.setText("Picking #" + picking.getId() + " | " + safe(picking.getEstado())
                + " | Inicio: " + safe(picking.getDataInicio()));
    }

    private void recarregarSelecionada() {
        if (requisicaoSelecionada != null) {
            setRequisicaoSelecionada(serviceRequisicao.consultaRequisicaoByID(requisicaoSelecionada.getId()));
        }
    }

    private boolean hasRequisicaoSelecionada() {
        if (requisicaoSelecionada == null || requisicaoSelecionada.getId() <= 0) {
            showErroAlert("Seleccione uma requisicao!");
            return false;
        }
        return true;
    }

    private void limparDetalhes() {
        labReqId.setText("-");
        labReqEstado.setText("-");
        labReqData.setText("-");
        labReqNecessidade.setText("-");
        labReqMotivo.setText("-");
        labReqSelecionadaAux.setText("-");
        labPickingInfo.setText("Sem picking carregado");
        tabelaItensDetalhe.setItems(FXCollections.<ItemRequisicao>observableArrayList());
        tabelaReservas.setItems(FXCollections.<ReservaStock>observableArrayList());
        tabelaMovimentos.setItems(FXCollections.<Movimento>observableArrayList());
    }

    private Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean contains(String value, String termo) {
        return value != null && value.toLowerCase().contains(termo);
    }

    private String nomeProduto(ItemRequisicao item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    private String nomeProduto(ReservaStock reserva) {
        return reserva != null && reserva.getProduto() != null ? safe(reserva.getProduto().getNome()) : "";
    }

    private String nomeProduto(Movimento movimento) {
        return movimento != null && movimento.getProduto() != null ? safe(movimento.getProduto().getNome()) : "";
    }

    private String nomeArmazem(ItemRequisicao item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    private String nomeArmazem(ReservaStock reserva) {
        return reserva != null && reserva.getArmazem() != null ? safe(reserva.getArmazem().getNome_arm()) : "";
    }

    private String nomeArmazem(Movimento movimento) {
        return movimento != null && movimento.getArmazem() != null ? safe(movimento.getArmazem().getNome_arm()) : "";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

