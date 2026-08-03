package jminvsm.views.modulo_inventario.transferenciaView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.transferencia.ItemTransferencia;
import jminvsm.model.transferencia.TransferenciaStock;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.transferencia.ServiceTransferenciaConsulta;
import jminvsm.service.transferencia.ServiceTransferenciaStock;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class RequisicaoTransferenciaController implements Initializable {

    private ServiceTransferenciaStock serviceTransferencia;
    private ServiceTransferenciaConsulta serviceConsulta;
    private ServiceProduto serviceProduto;
    private ServiceArmazem serviceArmazem;

    private final ObservableList<ItemTransferencia> itensNovaTransferencia = FXCollections.observableArrayList();
    private ObservableList<TransferenciaStock> listaTransferencias = FXCollections.observableArrayList();
    private TransferenciaStock transferenciaSelecionada;

    @FXML
    private TabPane tabPaneTransferencias;
    @FXML
    private Label labResumoCabecalho;
    @FXML
    private ComboBox<Armazem> combArmazemOrigem;
    @FXML
    private ComboBox<Armazem> combArmazemDestino;
    @FXML
    private ComboBox<Produto> combProduto;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TableView<ItemTransferencia> tabelaItensNova;
    @FXML
    private TableColumn<ItemTransferencia, String> colNovoProduto;
    @FXML
    private TableColumn<ItemTransferencia, Integer> colNovoQuantidade;

    @FXML
    private TextField txtPesquisa;
    @FXML
    private ComboBox<String> combFiltroEstado;
    @FXML
    private TableView<TransferenciaStock> tabelaTransferencias;
    @FXML
    private TableColumn<TransferenciaStock, Integer> colTransId;
    @FXML
    private TableColumn<TransferenciaStock, String> colTransData;
    @FXML
    private TableColumn<TransferenciaStock, String> colTransOrigem;
    @FXML
    private TableColumn<TransferenciaStock, String> colTransDestino;
    @FXML
    private TableColumn<TransferenciaStock, String> colTransEstado;

    @FXML
    private Label labTransId;
    @FXML
    private Label labTransEstado;
    @FXML
    private Label labTransOrigem;
    @FXML
    private Label labTransDestino;
    @FXML
    private Label labTransData;
    @FXML
    private Label labTransSelecionadaAux;

    @FXML
    private TableView<ItemTransferencia> tabelaItensDetalhe;
    @FXML
    private TableColumn<ItemTransferencia, String> colDetalheProduto;
    @FXML
    private TableColumn<ItemTransferencia, Integer> colDetalheQuantidade;
    @FXML
    private TableColumn<ItemTransferencia, Integer> colDetalheRecebida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarServices();
        configurarCombos();
        configurarTabelas();
        tabelaItensNova.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaTransferencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensDetalhe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensNova.setItems(itensNovaTransferencia);
        atualizarDados();
    }

    @FXML
    private void adicionarItem() {
        Produto produto = combProduto.getSelectionModel().getSelectedItem();
        int quantidade;
        try {
            quantidade = serviceConsulta.quantidadeObrigatoria(txtQuantidade.getText());
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        ItemTransferencia item = serviceTransferencia.novoItem(produto, quantidade);
        if (item == null) {
            showErroAlert("Dados do item invalidos!");
            return;
        }
        itensNovaTransferencia.add(item);
        txtQuantidade.clear();
        combProduto.requestFocus();
    }

    @FXML
    private void removerItem() {
        ItemTransferencia item = tabelaItensNova.getSelectionModel().getSelectedItem();
        if (item != null) {
            itensNovaTransferencia.remove(item);
        }
    }

    @FXML
    private void guardarRequisicao() {
        Armazem origem = combArmazemOrigem.getSelectionModel().getSelectedItem();
        Armazem destino = combArmazemDestino.getSelectionModel().getSelectedItem();
        try {
            serviceTransferencia.criarRequisicaoTransferencia(origem, destino,
                    FXCollections.observableArrayList(itensNovaTransferencia));
            if (serviceTransferencia.isOpsSuccess()) {
                showSuccessAlert("Requisicao de transferencia criada com sucesso!");
                limparFormulario();
                atualizarDados();
                tabPaneTransferencias.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void limparFormulario() {
        combArmazemOrigem.getSelectionModel().clearSelection();
        combArmazemDestino.getSelectionModel().clearSelection();
        combProduto.getSelectionModel().clearSelection();
        txtQuantidade.clear();
        itensNovaTransferencia.clear();
    }

    @FXML
    private void atualizarDados() {
        if (serviceTransferencia == null) {
            return;
        }
        String estado = combFiltroEstado != null ? combFiltroEstado.getSelectionModel().getSelectedItem() : null;
        listaTransferencias = serviceTransferencia.listarTransferencias(estado);
        aplicarFiltroPesquisa();
    }

    @FXML
    private void selecionarTransferencia(MouseEvent event) {
        TransferenciaStock transferencia = tabelaTransferencias.getSelectionModel().getSelectedItem();
        if (transferencia != null) {
            setTransferenciaSelecionada(serviceTransferencia.consultarTransferencia(transferencia.getId()));
        }
    }

    @FXML
    private void aprovarTransferencia() {
        executarAcaoTransferencia("Transferencia aprovada com sucesso!", () ->
                serviceTransferencia.aprovarTransferencia(transferenciaSelecionada.getId()));
    }

    @FunctionalInterface
    private interface AcaoTransferencia {

        boolean executar();
    }

    private void executarAcaoTransferencia(String mensagemSucesso, AcaoTransferencia acao) {
        if (!hasTransferenciaSelecionada()) {
            return;
        }
        try {
            if (acao.executar()) {
                showSuccessAlert(mensagemSucesso);
                atualizarDados();
                recarregarSelecionada();
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void enviarTransferencia() {
        executarAcaoTransferencia("Transferencia enviada com sucesso!", () ->
                serviceTransferencia.enviarTransferencia(transferenciaSelecionada.getId()));
    }

    @FXML
    private void receberTransferencia() {
        executarAcaoTransferencia("Transferencia recebida com sucesso!", () ->
                serviceTransferencia.receberTransferencia(transferenciaSelecionada.getId()));
    }

    @FXML
    private void cancelarTransferencia() {
        executarAcaoTransferencia("Transferencia cancelada com sucesso!", () ->
                serviceTransferencia.cancelarTransferencia(transferenciaSelecionada.getId()));
    }

    @FXML
    private void carregarDetalhesSelecionados() {
        if (hasTransferenciaSelecionada()) {
            recarregarSelecionada();
        }
    }

    private void inicializarServices() {
        try {
            serviceTransferencia = new ServiceTransferenciaStock();
            serviceConsulta = new ServiceTransferenciaConsulta();
            serviceProduto = new ServiceProduto();
            serviceArmazem = new ServiceArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(RequisicaoTransferenciaController.class.getName()).log(Level.SEVERE, null, ex);
            showErroAlert("Erro ao iniciar servicos de transferencia!");
        }
    }

    private void configurarCombos() {
        combFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "Pendente", "Reservado", "Em Transito", "Recebido", "Cancelado"));
        combFiltroEstado.getSelectionModel().select("Todos");
        combFiltroEstado.setOnAction(e -> atualizarDados());

        txtPesquisa.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroPesquisa());

        StringConverter<Armazem> armazemConverter = new StringConverter<Armazem>() {
            @Override
            public String toString(Armazem armazem) {
                return serviceConsulta.rotuloArmazem(armazem);
            }

            @Override
            public Armazem fromString(String string) {
                return null;
            }
        };
        combArmazemOrigem.setConverter(armazemConverter);
        combArmazemDestino.setConverter(armazemConverter);
        combProduto.setConverter(new StringConverter<Produto>() {
            @Override
            public String toString(Produto produto) {
                return serviceConsulta.rotuloProduto(produto);
            }

            @Override
            public Produto fromString(String string) {
                return null;
            }
        });

        if (serviceArmazem != null) {
            ObservableList<Armazem> armazens = serviceArmazem.listaTodosArmazens();
            combArmazemOrigem.setItems(armazens);
            combArmazemDestino.setItems(FXCollections.observableArrayList(armazens));
        }
        if (serviceProduto != null) {
            combProduto.setItems(serviceProduto.listaTodosProdutos());
        }
    }

    private void configurarTabelas() {
        colNovoProduto.setCellValueFactory(cell -> new SimpleStringProperty(serviceConsulta.nomeProduto(cell.getValue())));
        colNovoQuantidade.setCellValueFactory(new PropertyValueFactory<ItemTransferencia, Integer>("quantidade"));

        colTransId.setCellValueFactory(new PropertyValueFactory<TransferenciaStock, Integer>("id"));
        colTransData.setCellValueFactory(new PropertyValueFactory<TransferenciaStock, String>("dataCriacao"));
        colTransOrigem.setCellValueFactory(cell -> new SimpleStringProperty(serviceConsulta.nomeArmazemOrigem(cell.getValue())));
        colTransDestino.setCellValueFactory(cell -> new SimpleStringProperty(serviceConsulta.nomeArmazemDestino(cell.getValue())));
        colTransEstado.setCellValueFactory(new PropertyValueFactory<TransferenciaStock, String>("estado"));

        colDetalheProduto.setCellValueFactory(cell -> new SimpleStringProperty(serviceConsulta.nomeProduto(cell.getValue())));
        colDetalheQuantidade.setCellValueFactory(new PropertyValueFactory<ItemTransferencia, Integer>("quantidade"));
        colDetalheRecebida.setCellValueFactory(new PropertyValueFactory<ItemTransferencia, Integer>("quantidadeRecebida"));
    }

    private void aplicarFiltroPesquisa() {
        if (tabelaTransferencias == null || listaTransferencias == null) {
            return;
        }
        String filtro = txtPesquisa != null ? txtPesquisa.getText() : null;
        ObservableList<TransferenciaStock> filtradas = serviceConsulta.filtrar(listaTransferencias, filtro);
        SortedList<TransferenciaStock> ordenadas = new SortedList<TransferenciaStock>(filtradas);
        ordenadas.comparatorProperty().bind(tabelaTransferencias.comparatorProperty());
        tabelaTransferencias.setItems(ordenadas);
    }

    private void setTransferenciaSelecionada(TransferenciaStock transferencia) {
        transferenciaSelecionada = transferencia;
        if (transferencia == null) {
            limparDetalhes();
            return;
        }
        labTransId.setText(String.valueOf(transferencia.getId()));
        labTransEstado.setText(texto(transferencia.getEstado()));
        labTransOrigem.setText(serviceConsulta.nomeArmazemOrigem(transferencia));
        labTransDestino.setText(serviceConsulta.nomeArmazemDestino(transferencia));
        labTransData.setText(texto(transferencia.getDataCriacao()));
        labTransSelecionadaAux.setText(serviceConsulta.rotuloTransferencia(transferencia));
        labResumoCabecalho.setText(serviceConsulta.textoResumoSelecionada(transferencia));
        tabelaItensDetalhe.setItems(transferencia.getItens() != null
                ? FXCollections.observableArrayList(transferencia.getItens())
                : FXCollections.<ItemTransferencia>observableArrayList());
    }

    private void recarregarSelecionada() {
        if (transferenciaSelecionada != null) {
            setTransferenciaSelecionada(serviceTransferencia.consultarTransferencia(transferenciaSelecionada.getId()));
        }
    }

    private boolean hasTransferenciaSelecionada() {
        if (transferenciaSelecionada == null || transferenciaSelecionada.getId() <= 0) {
            showErroAlert("Seleccione uma requisicao de transferencia!");
            return false;
        }
        return true;
    }

    private void limparDetalhes() {
        labTransId.setText("-");
        labTransEstado.setText("-");
        labTransOrigem.setText("-");
        labTransDestino.setText("-");
        labTransData.setText("-");
        labTransSelecionadaAux.setText("-");
        tabelaItensDetalhe.setItems(FXCollections.<ItemTransferencia>observableArrayList());
    }

    private String texto(String value) {
        return value == null ? "" : value;
    }
}
