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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import jminvsm.service.stock.ServiceStock;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
//import sysfact.views.modulo_venda.vendaView.searchCliente.ClienteSearchController;

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
//        priceFormaterProduto();
        prod = (Produto) SysFact.getData();
        txtIDProduto.setText(String.valueOf(prod.getId()));
        try {
            service = new ServiceProdutoFornecido();
            serviceArmazem = new ServiceArmazem();
            servicoFornecedor = new ServiceFornecedor();
            servicoprodFornecido = new ServiceProdutoFornecido();
            serviceStock = new ServiceStock();
            this.userData = SysFact.getUserData();
            showLotes(prod.getId());
            showResumoStock(prod.getId());

            setDetalhesProduto();
            ButtonUtilities.buttonChangeText(btnAdd, txtIDProduto);
        } catch (SQLException ex) {
            Logger.getLogger(AddStockController.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateComb_ArmazemFornecedor();
        cBoxArmazem.setOnAction(evt -> {
            String data = cBoxArmazem.getSelectionModel().getSelectedItem();

            labTipoArmazem.setText(mapaArmazens.get(data).getTipo());
        });
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
            labControloStock.setText("NAO");
            labControloStock.setStyle("-fx-text-fill: red;");
        }

        // Gera codigo do proximo lote
        ProdutoFornecido pf = servicoprodFornecido.getLastCodeLote();
        txtLoteCode.setText("" + pf.getCodigo());

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

        fornecedorList = servicoFornecedor.listaTodosFornecedores();
        List<String> listaForne = new ArrayList<>();
        mapaFornecedor = new HashMap<>();
        for (Fornecedor a : fornecedorList) {
            listaForne.add(a.getRazaosocial_forn());
            mapaFornecedor.put(a.getRazaosocial_forn(), a);
        }
        cBoxFornecedor.setItems(FXCollections.observableArrayList(listaForne));
    }

    public void addProdutoFornecido(ActionEvent e) throws SQLException {
//        INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario)
        service.registraProdutoFornecido(txtLoteCode.getText(),
                Integer.valueOf(txtIDProduto.getText()),
                Integer.valueOf(mapaArmazens.get(cBoxArmazem.getValue()).getId()),
                Integer.valueOf(mapaFornecedor.get(cBoxFornecedor.getValue()).getId()),
                Integer.valueOf(txtQtActual.getText().equals("") ? "1" : txtQtActual.getText()),
                String.valueOf(dataAquisicao.getValue() == null ? "" : dataAquisicao.getValue()),
                Double.valueOf(txtCustoUnitario.getText().equals("") ? "0" : txtCustoUnitario.getText())
        );
        showLotes(stock.getProduto().getId());
        showResumoStock(stock.getProduto().getId());

        // Gera codigo do proximo lote
        ProdutoFornecido pf = servicoprodFornecido.getLastCodeLote();
        txtLoteCode.setText("" + pf.getCodigo());
    }
}
