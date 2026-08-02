/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.stockView;

import java.net.URL;
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
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.ServiceException;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import jminvsm.service.stock.ServiceStock;
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

    private ServiceProdutoFornecido service;
    private ServiceArmazem serviceArmazem;
    private ServiceFornecedor servicoFornecedor;
    private ServiceProdutoFornecido servicoprodFornecido;
    private ServiceStock serviceStock;
    private ServiceTransferenciaStock serviceTransferenciaStock;

    private Usuario userData;
    private Stock stock;
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
            service = new ServiceProdutoFornecido();
            serviceArmazem = new ServiceArmazem();
            servicoFornecedor = new ServiceFornecedor();
            servicoprodFornecido = new ServiceProdutoFornecido();
            serviceStock = new ServiceStock();
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
        listaFornecedoress = servicoprodFornecido.getLotes(id);

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

        Stock s = serviceStock.consultaDetalhesProduto(prod.getId());
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
        ProdutoFornecido pf = servicoprodFornecido.getLastCodeLote();
        txtLoteCode.setText(pf == null ? "" : String.valueOf(pf.getCodigo()));

    }

    private ObservableList<Stock> listaResumoStock;

    public void showResumoStock(int id) throws SQLException {
        listaResumoStock = serviceStock.getResumo(id);

        armazemResumoStock.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        saldoResumoStock.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        tableResumoStock.setItems(listaResumoStock);
    }

    private ObservableList<Armazem> storeList;
    private Map<String, Armazem> mapaArmazens;

    private ObservableList<Fornecedor> fornecedorList;
    private Map<String, Fornecedor> mapaFornecedor;

    public void populateComb_ArmazemFornecedor() {
        storeList = serviceArmazem.listaTodosArmazens();
        List<String> listaArm = new ArrayList<>();
        mapaArmazens = new HashMap<>();
        for (Armazem a : storeList) {
            listaArm.add(a.getNome_arm());
            mapaArmazens.put(a.getNome_arm(), a);
        }
        cBoxArmazem.setItems(FXCollections.observableArrayList(listaArm));
        cBoxArmazemOrigemTransferencia.setItems(FXCollections.observableArrayList(listaArm));
        cBoxArmazemDestinoTransferencia.setItems(FXCollections.observableArrayList(listaArm));

        fornecedorList = servicoFornecedor.listaTodosFornecedores();
        List<String> listaForne = new ArrayList<>();
        mapaFornecedor = new HashMap<>();
        for (Fornecedor a : fornecedorList) {
            listaForne.add(a.getRazaosocial_forn());
            mapaFornecedor.put(a.getRazaosocial_forn(), a);
        }
        cBoxFornecedor.setItems(FXCollections.observableArrayList(listaForne));
    }

    public void transferirStock(ActionEvent e) throws SQLException {
        if (!validarTransferencia()) {
            return;
        }

        Armazem origem = mapaArmazens.get(cBoxArmazemOrigemTransferencia.getValue());
        Armazem destino = mapaArmazens.get(cBoxArmazemDestinoTransferencia.getValue());
        int quantidade = Integer.parseInt(txtQuantidadeTransferencia.getText().trim());

        serviceTransferenciaStock.transferirProduto(prod.getId(), origem.getId(), destino.getId(), quantidade);
        if (serviceTransferenciaStock.isOpsSuccess()) {
            showLotes(prod.getId());
            showResumoStock(prod.getId());
            setDetalhesProduto();
            resetTransferencia();
        }
    }

    public void addProdutoFornecido(ActionEvent e) throws SQLException {
        if (!validarEntrada()) {
            return;
        }

        int quantidade = Integer.parseInt(txtQtActual.getText().trim());
        double custoUnitario = parseDecimal(txtCustoUnitario.getText());

        try {
            service.registraProdutoFornecido(
                    txtLoteCode.getText().trim(),
                    Integer.parseInt(txtIDProduto.getText()),
                    mapaArmazens.get(cBoxArmazem.getValue()).getId(),
                    mapaFornecedor.get(cBoxFornecedor.getValue()).getId(),
                    quantidade,
                    String.valueOf(dataAquisicao.getValue()),
                    custoUnitario
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (service.isOpsSuccess()) {
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

        ProdutoFornecido pf = servicoprodFornecido == null
                ? null
                : servicoprodFornecido.getLastCodeLote();
        txtLoteCode.setText(pf == null ? "" : String.valueOf(pf.getCodigo()));
    }

    private boolean validarEntrada() {
        if (txtLoteCode.getText() == null || txtLoteCode.getText().trim().isEmpty()) {
            showWarning("CÃ³digo do lote obrigatÃ³rio",
                    "Informe ou gere o cÃ³digo do lote.");
            txtLoteCode.requestFocus();
            return false;
        }
        if (cBoxArmazem.getValue() == null || mapaArmazens.get(cBoxArmazem.getValue()) == null) {
            showWarning("ArmazÃ©m obrigatÃ³rio",
                    "Seleccione o armazÃ©m de destino.");
            cBoxArmazem.requestFocus();
            return false;
        }
        if (cBoxFornecedor.getValue() == null || mapaFornecedor.get(cBoxFornecedor.getValue()) == null) {
            showWarning("Fornecedor obrigatÃ³rio",
                    "Seleccione o fornecedor do lote.");
            cBoxFornecedor.requestFocus();
            return false;
        }
        if (dataAquisicao.getValue() == null) {
            showWarning("Data obrigatÃ³ria",
                    "Informe a data de entrada do lote.");
            dataAquisicao.requestFocus();
            return false;
        }

        try {
            int quantidade = Integer.parseInt(txtQtActual.getText().trim());
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (Exception ex) {
            showWarning("Quantidade invÃ¡lida",
                    "Informe uma quantidade inteira maior que zero.");
            txtQtActual.requestFocus();
            return false;
        }

        try {
            double custo = parseDecimal(txtCustoUnitario.getText());
            if (custo < 0) {
                throw new NumberFormatException();
            }
        } catch (Exception ex) {
            showWarning("Custo unitÃ¡rio invÃ¡lido",
                    "Informe um custo unitÃ¡rio numÃ©rico vÃ¡lido.");
            txtCustoUnitario.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarTransferencia() {
        if (cBoxArmazemOrigemTransferencia.getValue() == null
                || mapaArmazens.get(cBoxArmazemOrigemTransferencia.getValue()) == null) {
            showWarning("Origem obrigatoria", "Seleccione o armazem de origem da transferencia.");
            cBoxArmazemOrigemTransferencia.requestFocus();
            return false;
        }
        if (cBoxArmazemDestinoTransferencia.getValue() == null
                || mapaArmazens.get(cBoxArmazemDestinoTransferencia.getValue()) == null) {
            showWarning("Destino obrigatorio", "Seleccione o armazem de destino da transferencia.");
            cBoxArmazemDestinoTransferencia.requestFocus();
            return false;
        }
        if (cBoxArmazemOrigemTransferencia.getValue().equals(cBoxArmazemDestinoTransferencia.getValue())) {
            showWarning("Armazens iguais", "A origem e o destino devem ser armazens diferentes.");
            cBoxArmazemDestinoTransferencia.requestFocus();
            return false;
        }
        try {
            int quantidade = Integer.parseInt(txtQuantidadeTransferencia.getText().trim());
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (Exception ex) {
            showWarning("Quantidade invalida", "Informe uma quantidade inteira maior que zero.");
            txtQuantidadeTransferencia.requestFocus();
            return false;
        }
        return true;
    }

    private void resetTransferencia() {
        cBoxArmazemOrigemTransferencia.getSelectionModel().clearSelection();
        cBoxArmazemDestinoTransferencia.getSelectionModel().clearSelection();
        txtQuantidadeTransferencia.clear();
    }

    private void actualizarValorTotal() {
        try {
            int quantidade = Integer.parseInt(txtQtActual.getText().trim());
            double custo = parseDecimal(txtCustoUnitario.getText());
            txtValorTotal.setText(String.format("%,.2f MZN", quantidade * custo));
        } catch (Exception ex) {
            txtValorTotal.clear();
        }
    }

    private double parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value.trim().replace(" ", "").replace(",", "."));
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

