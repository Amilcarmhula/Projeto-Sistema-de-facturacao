/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView.searchProduto;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.listagem.ServicePesquisaListagem;
import jminvsm.service.stock.ServiceStock;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
//SearchProdutoController
//searchProduto
public class SearchProdutoController implements Initializable {

    private Usuario userData;
    private ServiceStock serStock;
    private ServicePesquisaListagem servicePesquisa;
    private int idArmazem;
    

    @FXML
    private TableView<Stock> tabelaProduto;
    @FXML
    private TableColumn<Stock, String> ID;
    @FXML
    private TableColumn<Stock, String> armazem;
    @FXML
    private TableColumn<Stock, String> artigo;
    @FXML
    private TableColumn<Stock, String> descricao;
    @FXML
    private TableColumn<Stock, String> unidade;
    @FXML
    private TableColumn<Stock, String> categoria;
    @FXML
    private TableColumn<Stock, String> quantidade;


    @FXML
    private CheckBox checkArmazemPadrao;
    @FXML
    private TextField txtPesquisar;

    public void stockControlChecked() {
        if (checkArmazemPadrao.isSelected()) {
            txtPesquisar.setDisable(false);
        } else {
            txtPesquisar.setDisable(true);
        }
    }
    
    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
        if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

 
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SessaoAplicacao.getUserData();
        try {

            serStock = new ServiceStock();
            servicePesquisa = new ServicePesquisaListagem();
            showInvenarioProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(SearchProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ObservableList<Stock> listaProduto;
    private boolean pesquisaConfigurada;

    public void showInvenarioProdutos() {
        listaProduto = serStock.listaProdutosParaVenda(1);

        ID.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.chaveStock(cellData.getValue())));
        armazem.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.armazemStock(cellData.getValue())));
        artigo.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.nomeProduto(cellData.getValue())));
        descricao.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.descricaoProduto(cellData.getValue())));
        unidade.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.unidadeProduto(cellData.getValue())));
        categoria.setCellValueFactory(cellData -> new SimpleStringProperty(servicePesquisa.categoriaProduto(cellData.getValue())));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        configurarPesquisa();
        aplicarPesquisa();
    }

    private void configurarPesquisa() {
        if (!pesquisaConfigurada) {
            txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> aplicarPesquisa());
            pesquisaConfigurada = true;
        }
    }

    private void aplicarPesquisa() {
        SortedList<Stock> sortedData = new SortedList<>(
                servicePesquisa.filtrarStock(listaProduto, txtPesquisar.getText()));
        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());
        tabelaProduto.setItems(sortedData);
    }

    public void selecionaProduto(MouseEvent event) {
//        TablePosition pos = tabelaProduto.getSelectionModel().getSelectedCells().get(0);
//        int linha = pos.getRow();
//        TableColumn coluna = tabelaProduto.getColumns().get(0);
//        Object obj = coluna.getCellData(linha);
//        System.out.println("Valor: " + obj);

        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            SessaoAplicacao.setData(x);
        }
    }
}

