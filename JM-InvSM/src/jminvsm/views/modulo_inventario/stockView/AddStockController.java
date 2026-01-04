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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
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
    private ServiceProdutoFornecido servicoprodFornecido;

    private Usuario userData;
    private Stock s;
    @FXML
    private Button btnAdd;

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

    @FXML
    private TextField txtQtd_por_unidade;

    @FXML
    private RadioButton radSimples;

    @FXML
    private RadioButton radComposto;

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
        s = (Stock) SysFact.getData();
        txtIDProduto.setText(String.valueOf(s.getProduto().getId()));
        try {
            service = new ServiceProdutoFornecido();
            serviceArmazem = new ServiceArmazem();
            servicoFornecedor = new ServiceFornecedor();
            servicoprodFornecido = new ServiceProdutoFornecido();
            this.userData = SysFact.getUserData();
            showLotes(s.getProduto().getId());
            ButtonUtilities.buttonChangeText(btnAdd, txtIDProduto);
        } catch (SQLException ex) {
            Logger.getLogger(AddStockController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void addProdutoFornecido(ActionEvent e) throws SQLException {
        String tipo = "";
        if (radComposto.isSelected()) {
            tipo = "Composto";
        }
        if (radSimples.isSelected()) {
            tipo = "Simples";
        }
        service.registraProdutoFornecido(s.getArmazem().getId(),
                Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText()),
                Integer.valueOf(txtIDFornecedor.getText().equals("") ? "0" : txtIDFornecedor.getText()), userData,
                Integer.valueOf(txtQuantidade.getText().equals("") ? "0" : txtQuantidade.getText()),
                String.valueOf(dataAquisicao.getValue() == null ? "" : dataAquisicao.getValue()),
                Double.valueOf(txtCustoAquisicao.getText().equals("") ? "0" : txtCustoAquisicao.getText()),
                tipo,
                Integer.valueOf(txtQtd_por_unidade.getText().equals("") ? "1" : txtQtd_por_unidade.getText()));
    }
}
