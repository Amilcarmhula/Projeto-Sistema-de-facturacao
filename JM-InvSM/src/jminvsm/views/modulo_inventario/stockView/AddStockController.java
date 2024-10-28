/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.stockView;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.dao.armazem.ArmazemDAO;
import jminvsm.dao.fornecedor.FornecedorDAO;
import jminvsm.dao.produto.fornecido.ProdutoFornecidoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
//import sysfact.views.modulo_venda.vendaView.searchCliente.ClienteSearchController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddStockController implements Initializable {

    private Usuario userData;
    @FXML
    private Button btnAdd;

    @FXML
    private TableView<Armazem> tabelaArmazem;
    @FXML
    private TableColumn<Armazem, String> tipotabelaArmazem;
    @FXML
    private TableColumn<Armazem, Integer> IDtabelaArmazem;
    @FXML
    private TableColumn<Armazem, String> descricaotabelaArmazem;
    @FXML
    private TableColumn<Armazem, Boolean> estadotabelaArmazem;
    @FXML
    private TableColumn<Armazem, String> nometabelaArmazem;

    @FXML
    private TableView<Fornecedor> tabelaFornecedor;
    @FXML
    private TableColumn<Fornecedor, Integer> IDtabelaFornecedor;
    @FXML
    private TableColumn<Fornecedor, String> contactotabelaFornecedor;
    @FXML
    private TableColumn<Fornecedor, Integer> nuittabelaFornecedor;
    @FXML
    private TableColumn<Fornecedor, String> razaotabelaFornecedor;

    @FXML
    private DatePicker dataAquisicao;

    @FXML
    private TextField txtCustoAquisicao;

    @FXML
    private TextField txtIDArmazem;

    @FXML
    private TextField txtIDFornecedor;

    @FXML
    private TextField txtIDProduto;

    @FXML
    private TextField txtPesquisaArmazem;

    @FXML
    private TextField txtPesquisafrnecedor;

    @FXML
    private TextField txtQuantidade;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null,null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } 
    }
    
    public void priceFormaterProduto() {
        // Define the Locale for currency formatting (Mozambique)
        Locale locale = new Locale("pt", "MZ");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Create a TextFormatter to format the input as currency
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            // Remove all non-numeric characters
            String newText = change.getControlNewText().replaceAll("[^\\d]", "");

            if (newText.isEmpty()) {
                newText = "0";
            }

            try {
                // Convert to a number and format as currency
                double value = Double.parseDouble(newText) / 100.0;
                change.setText(currencyFormat.format(value));
                change.setRange(0, change.getControlText().length()); // Replace the whole text
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }

            return change;
        });

        // Attach the TextFormatter to the TextField
        txtCustoAquisicao.setTextFormatter(textFormatter);
        txtCustoAquisicao.setText(currencyFormat.format(0)); // Set initial value

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        priceFormaterProduto();
        Produto p = (Produto) SysFact.getData();
        txtIDProduto.setText(String.valueOf(p.getId()));
        try {
            this.userData = SysFact.getUserData();
            showFornecedores();
            showArmazens();
            ButtonUtilities.buttonChangeText(btnAdd, txtIDProduto);
        } catch (SQLException ex) {
            Logger.getLogger(AddStockController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList<Fornecedor> listaFornecedoress;

    public void showFornecedores() throws SQLException {

        FornecedorDAO aDAO = new FornecedorDAO();
        listaFornecedoress = aDAO.listAllEntities();

        IDtabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("id"));
        razaotabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("razaosocial_forn"));
        nuittabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("nuit_forn"));
        contactotabelaFornecedor.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));

        FilteredList<Fornecedor> dadosFiltrados = new FilteredList<>(listaFornecedoress, b -> true);
        txtPesquisafrnecedor.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getRazaosocial_forn().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getNuit_forn()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Fornecedor> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaFornecedor.comparatorProperty());

        tabelaFornecedor.setItems(sortedData);
    }
    
    public void selectedRowFornecedor(){
        Fornecedor c = tabelaFornecedor.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtIDFornecedor.setText(String.valueOf(c.getId()));
        }
    }
    
    private ObservableList<Armazem> listaArmazens;

    public void showArmazens() throws SQLException {

        ArmazemDAO aDAO = new ArmazemDAO();
        listaArmazens = aDAO.listAllEntities();

        IDtabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("nome_arm"));
        descricaotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("descricao_arm"));
        estadotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("estado"));


        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaArmazens, b -> true);
        txtPesquisaArmazem.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome_arm().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao_arm().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getTipo().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Armazem> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaArmazem.comparatorProperty());

        tabelaArmazem.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedRowArmazem() throws SQLException {
        Armazem c = tabelaArmazem.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtIDArmazem.setText(String.valueOf(c.getId()));
        }

    }
    public void addProdutoFornecido() throws SQLException{
        ProdutoFornecidoDAO xDAO = new ProdutoFornecidoDAO();
        ServiceProdutoFornecido spf = new ServiceProdutoFornecido(xDAO);
        spf.registraProdutoFornecido(Integer.valueOf(txtIDArmazem.getText().equals("") ? "0" : txtIDArmazem.getText()), 
                Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText()), 
                Integer.valueOf(txtIDFornecedor.getText().equals("") ? "0" : txtIDFornecedor.getText()), userData, 
                Integer.valueOf(txtQuantidade.getText().equals("") ? "0" : txtQuantidade.getText()), 
                String.valueOf(dataAquisicao.getValue() == null ? "" : dataAquisicao.getValue()), 
                Double.valueOf(txtCustoAquisicao.getText().equals("") ? "0" : txtCustoAquisicao.getText().split(" ")[1].replace(".", "").replace(",", ".")));
    }
}
