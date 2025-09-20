/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.stockView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.fornecedor.ServiceFornecedor;
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

    private ServiceProdutoFornecido service;
    private ServiceArmazem serviceArmazem;
    private ServiceFornecedor servicoFornecedor;

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
    private TextField txtUnidade;

    @FXML
    private TextField txtPesquisaArmazem;

    @FXML
    private TextField txtPesquisafrnecedor;

    @FXML
    private TextField txtQuantidade;
    
    @FXML
    private TextField txtQtd_por_unidade;

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
        Stock s = (Stock) SysFact.getData();
        txtIDProduto.setText(String.valueOf(s.getProduto().getId()));
        try {
            service = new ServiceProdutoFornecido();
            serviceArmazem = new ServiceArmazem();
            servicoFornecedor = new ServiceFornecedor();
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
        listaFornecedoress = servicoFornecedor.getFornecedores();

        IDtabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("id"));
        razaotabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("razaosocial_forn"));
//        nuittabelaFornecedor.setCellValueFactory(new PropertyValueFactory<>("nuit_forn"));
//        contactotabelaFornecedor.setCellValueFactory(cellData
//                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));

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

    public void selectedRowFornecedor() {
        Fornecedor c = tabelaFornecedor.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtIDFornecedor.setText(String.valueOf(c.getId()));
        }
    }

    private ObservableList<Armazem> listaArmazens;

    public void showArmazens() {        
        listaArmazens = FXCollections.observableArrayList();
        for (Armazem a : serviceArmazem.listaArmazens()) {
            if (a.isEstado()) {
                listaArmazens.add(a);
            }
        }

        IDtabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("nome_arm"));
        descricaotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("descricao_arm"));

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

    public void addProdutoFornecido(ActionEvent e) throws SQLException {
        service.registraProdutoFornecido(Integer.valueOf(txtIDArmazem.getText().equals("") ? "0" : txtIDArmazem.getText()),
                Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText()),
                Integer.valueOf(txtIDFornecedor.getText().equals("") ? "0" : txtIDFornecedor.getText()), userData,
                Integer.valueOf(txtQuantidade.getText().equals("") ? "1" : txtQuantidade.getText()),
                String.valueOf(dataAquisicao.getValue() == null ? "" : dataAquisicao.getValue()),
                Double.valueOf(txtCustoAquisicao.getText().equals("") ? "0" : txtCustoAquisicao.getText()),
                String.valueOf(txtUnidade.getText().equals("") ? "unidade" : txtUnidade.getText()),
                Integer.valueOf(txtQtd_por_unidade.getText().equals("") ? "1" : txtQtd_por_unidade.getText()));
    }
}
