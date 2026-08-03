/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.stockView;

import java.net.URL;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.stock.ServiceEntradaStock;
import jminvsm.service.transferencia.ServiceTransferenciaStock;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddStockController implements Initializable {

    private ServiceEntradaStock serviceEntradaStock;
    private ServiceTransferenciaStock serviceTransferenciaStock;

    private Usuario userData;
    private Produto prod;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView<Stock> tableResumoStock;
    @FXML
    private TableColumn<Stock, String> armazemResumoStock;
    @FXML
    private TableColumn<Stock, Integer> saldoResumoStock;

    @FXML
    private ComboBox<String> cBoxArmazem;

    @FXML
    private ComboBox<String> cBoxFornecedor;

    @FXML
    private TableView<ProdutoFornecido> tableLote;
    @FXML
    private TableColumn<ProdutoFornecido, Integer> IDLote;
    @FXML
    private TableColumn<ProdutoFornecido, String> codeLote;
    @FXML
    private TableColumn<ProdutoFornecido, String> armazemLote;
    @FXML
    private TableColumn<ProdutoFornecido, String> entredaLote;
    @FXML
    private TableColumn<ProdutoFornecido, Integer> qtdInicialLote;
    @FXML
    private TableColumn<ProdutoFornecido, Integer> qtdActualLote;
    @FXML
    private TableColumn<ProdutoFornecido, Double> custoUnitarioLote;
    @FXML
    private TableColumn<ProdutoFornecido, Double> totalLote;
    @FXML
    private TableColumn<ProdutoFornecido, String> estadoLote;

    @FXML
    private DatePicker dataAquisicao;

    @FXML
    private TextField txtCustoUnitario;

    @FXML
    private TextField txtIDArmazem;
    @FXML
    private TextField txtQtInicial;
    @FXML
    private TextField txtLoteCode;

    @FXML
    private TextField txtIDProduto;
    @FXML
    private TextField txtNomeProduto;
    @FXML
    private TextField txtCodigoBarrasProduto;
    @FXML
    private TextField txtCategoriaProduto;
    @FXML
    private Label labGrupoProduto;
    @FXML
    private Label labTipoArmazem;
    @FXML
    private Label labControloStock;
    @FXML
    private TextField txtUnidadeProduto;
    @FXML
    private CheckBox chkControloStockProduto;

    @FXML
    private TextField txtIDFornecedor;

    @FXML
    private TextField txtPesquisaArmazem;

    @FXML
    private TextField txtPesquisafrnecedor;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtQtActual;
    @FXML
    private TextField txtValorTotal;
    @FXML
    private TextField txtIDLote;
    @FXML
    private ComboBox<String> cBoxArmazemOrigemTransferencia;
    @FXML
    private ComboBox<String> cBoxArmazemDestinoTransferencia;
    @FXML
    private TextField txtQuantidadeTransferencia;
    @FXML
    private Button btnTransferirStock;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableLote.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableResumoStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Object data = SessaoAplicacao.getData();
        if (!(data instanceof Produto)) {
            showWarning("Produto nÃ£o seleccionado",
                    "Abra esta janela a partir de um produto vÃ¡lido.");
            return;
        }

        prod = (Produto) data;
        txtIDProduto.setText(String.valueOf(prod.getId()));

        try {
            serviceEntradaStock = new ServiceEntradaStock();
            serviceTransferenciaStock = new ServiceTransferenciaStock();
            this.userData = SessaoAplicacao.getUserData();

            showLotes(prod.getId());
            showResumoStock(prod.getId());
            setDetalhesProduto();
            populateComb_ArmazemFornecedor();

            cBoxArmazem.setOnAction(evt -> {
                String nomeArmazem = cBoxArmazem.getValue();
                Armazem armazem = nomeArmazem == null ? null : mapaArmazens.get(nomeArmazem);
                labTipoArmazem.setText(armazem == null ? "â€”" : armazem.getTipo());
                txtIDArmazem.setText(armazem == null ? "" : String.valueOf(armazem.getId()));
            });

            cBoxFornecedor.setOnAction(evt -> {
                String nomeFornecedor = cBoxFornecedor.getValue();
                Fornecedor fornecedor = nomeFornecedor == null ? null : mapaFornecedor.get(nomeFornecedor);
                txtIDFornecedor.setText(fornecedor == null ? "" : String.valueOf(fornecedor.getId()));
            });

            txtQtActual.textProperty().addListener((obs, oldValue, newValue) -> actualizarValorTotal());
            txtCustoUnitario.textProperty().addListener((obs, oldValue, newValue) -> actualizarValorTotal());

            btnAdd.setText("Registar entrada");
            btnTransferirStock.setText("Transferir stock");
        } catch (SQLException ex) {
            Logger.getLogger(AddStockController.class.getName()).log(Level.SEVERE, null, ex);
            showWarning("Erro de inicializaÃ§Ã£o",
                    "NÃ£o foi possÃ­vel carregar os dados necessÃ¡rios para a entrada de stock.");
        }
    }

    private ObservableList<ProdutoFornecido> listaFornecedoress;

    public void showLotes(int id) throws SQLException {
        listaFornecedoress = serviceEntradaStock.listaLotes(prod);

        IDLote.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeLote.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        armazemLote.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        entredaLote.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
        qtdInicialLote.setCellValueFactory(new PropertyValueFactory<>("quantidadeInicial"));
        qtdActualLote.setCellValueFactory(new PropertyValueFactory<>("quantidadeActual"));
        custoUnitarioLote.setCellValueFactory(new PropertyValueFactory<>("custoUnitario"));
        totalLote.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        estadoLote.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableLote.setItems(listaFornecedoress);
    }

    public void setDetalhesProduto() {
        // Carega detalhes do produto

        Stock s = serviceEntradaStock.consultaDetalhesProduto(prod);
        if (s == null || s.getProduto() == null) {
            showWarning("Stock indisponivel",
                    "Nao foi possivel carregar os detalhes de stock deste produto.");
            return;
        }
        txtIDProduto.setText(String.valueOf(s.getProduto().getId()));
        txtNomeProduto.setText(s.getProduto().getNome());
        txtCodigoBarrasProduto.setText(s.getProduto().getCodigo_barra());
        txtCategoriaProduto.setText(s.getProduto().getCategoria().getFamilia());
        labGrupoProduto.setText(s.getProduto().getCategoria().getGrupo());
        txtUnidadeProduto.setText(s.getProduto().getUnidadeMedida().getNome());
        txtQtInicial.setText(String.valueOf(s.getSaldo()));
        if (s.getProduto().isControle_stock()) {
            chkControloStockProduto.setSelected(true);
            labControloStock.setText("SIM");
            labControloStock.setStyle("-fx-text-fill: green;");
        } else {
            chkControloStockProduto.setSelected(false);
            labControloStock.setText("NÃƒO");
            labControloStock.setStyle("-fx-text-fill: red;");
        }

        // Gera codigo do proximo lote
        ProdutoFornecido pf = serviceEntradaStock.proximoCodigoLote();
        txtLoteCode.setText(pf == null ? "" : String.valueOf(pf.getCodigo()));

    }

    private ObservableList<Stock> listaResumoStock;

    public void showResumoStock(int id) throws SQLException {
        listaResumoStock = serviceEntradaStock.resumoStock(prod);

        armazemResumoStock.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        saldoResumoStock.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        tableResumoStock.setItems(listaResumoStock);
    }

    private ObservableList<Armazem> storeList;
    private Map<String, Armazem> mapaArmazens;

    private ObservableList<Fornecedor> fornecedorList;
    private Map<String, Fornecedor> mapaFornecedor;

    public void populateComb_ArmazemFornecedor() {
        storeList = serviceEntradaStock.listaArmazens();
        List<String> listaArm = new ArrayList<>();
        mapaArmazens = new HashMap<>();
        for (Armazem a : storeList) {
            listaArm.add(a.getNome_arm());
            mapaArmazens.put(a.getNome_arm(), a);
        }
        cBoxArmazem.setItems(FXCollections.observableArrayList(listaArm));
        cBoxArmazemOrigemTransferencia.setItems(FXCollections.observableArrayList(listaArm));
        cBoxArmazemDestinoTransferencia.setItems(FXCollections.observableArrayList(listaArm));

        fornecedorList = serviceEntradaStock.listaFornecedores();
        List<String> listaForne = new ArrayList<>();
        mapaFornecedor = new HashMap<>();
        for (Fornecedor a : fornecedorList) {
            listaForne.add(a.getRazaosocial_forn());
            mapaFornecedor.put(a.getRazaosocial_forn(), a);
        }
        cBoxFornecedor.setItems(FXCollections.observableArrayList(listaForne));
    }

    public void transferirStock(ActionEvent e) throws SQLException {
        Armazem origem = mapaArmazens.get(cBoxArmazemOrigemTransferencia.getValue());
        Armazem destino = mapaArmazens.get(cBoxArmazemDestinoTransferencia.getValue());

        try {
            serviceTransferenciaStock.transferirProduto(prod, origem, destino, valor(txtQuantidadeTransferencia));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceTransferenciaStock.isOpsSuccess()) {
            showSuccessAlert("Stock transferido com sucesso!");
            showLotes(prod.getId());
            showResumoStock(prod.getId());
            setDetalhesProduto();
            resetTransferencia();
        }
    }

    public void addProdutoFornecido(ActionEvent e) throws SQLException {
        try {
            serviceEntradaStock.registarEntrada(
                    valor(txtLoteCode),
                    prod,
                    mapaArmazens.get(cBoxArmazem.getValue()),
                    mapaFornecedor.get(cBoxFornecedor.getValue()),
                    valor(txtQtActual),
                    dataEntradaSeleccionada(),
                    valor(txtCustoUnitario));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceEntradaStock.isOpsSuccess()) {
            showSuccessAlert("Stock actualizado com sucesso!");
            showLotes(prod.getId());
            showResumoStock(prod.getId());
            setDetalhesProduto();
            resetCampos(null);
        }
    }

    @FXML
    public void resetCampos(ActionEvent event) {
        cBoxArmazem.getSelectionModel().clearSelection();
        cBoxFornecedor.getSelectionModel().clearSelection();
        dataAquisicao.setValue(null);
        txtQtActual.clear();
        txtCustoUnitario.clear();
        txtValorTotal.clear();
        txtIDArmazem.clear();
        txtIDFornecedor.clear();
        labTipoArmazem.setText("â€”");

        ProdutoFornecido pf = serviceEntradaStock == null
                ? null
                : serviceEntradaStock.proximoCodigoLote();
        txtLoteCode.setText(pf == null ? "" : String.valueOf(pf.getCodigo()));
    }

    private void resetTransferencia() {
        cBoxArmazemOrigemTransferencia.getSelectionModel().clearSelection();
        cBoxArmazemDestinoTransferencia.getSelectionModel().clearSelection();
        txtQuantidadeTransferencia.clear();
    }

    private void actualizarValorTotal() {
        try {
            BigDecimal total = serviceEntradaStock.calcularValorTotal(valor(txtQtActual), valor(txtCustoUnitario));
            txtValorTotal.setText(String.format("%,.2f MZN", total.doubleValue()));
        } catch (Exception ex) {
            txtValorTotal.clear();
        }
    }

    private String dataEntradaSeleccionada() {
        return dataAquisicao == null || dataAquisicao.getValue() == null
                ? null
                : String.valueOf(dataAquisicao.getValue());
    }

    private static String valor(TextField campo) {
        return campo == null || campo.getText() == null ? "" : campo.getText().trim();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

