/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView.searchProduto;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
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
        this.userData = SysFact.getUserData();
        try {

            serStock = new ServiceStock();
            showInvenarioProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(SearchProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ObservableList<Stock> listaProduto;
    private Map<String, Stock> mapaProdutos;

    public void showInvenarioProdutos() {
        listaProduto = serStock.listaStockProdutos(1);
        mapaProdutos = new HashMap<>();
        for (Stock s : listaProduto) {
            mapaProdutos.put(s.getProduto().getId() + "," + s.getArmazem().getId(), s);
        }

        ID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getId() + "," + cellData.getValue().getArmazem().getId())));
        armazem.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getArmazem().getNome_arm() + ": " + cellData.getValue().getArmazem().getTipo())));
        artigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        descricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getDescricao()));
        unidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getUnidadeMedida().getSigla()));
        categoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getCategoria().getFamilia()));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        FilteredList<Stock> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getProduto().getNome().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getArmazem().getNome_arm().concat(": "+x.getArmazem().getTipo()).toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getProduto().getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                }else if (x.getProduto().getCategoria().getFamilia().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Stock> sortedData = new SortedList<>(dadosFiltrados);
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
            Stock s = mapaProdutos.get(x.getProduto().getId() + "," + x.getArmazem().getId());
            System.out.println("ID: "+s.getProduto().getId() + "," + s.getArmazem().getId()+" - PV: "+s.getPrecoProdutoArmazem().getPrecoVenda());
            SysFact.setData(s);
        }
    }
}
