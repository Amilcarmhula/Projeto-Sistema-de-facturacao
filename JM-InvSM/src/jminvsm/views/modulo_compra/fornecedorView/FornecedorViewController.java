/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_compra.fornecedorView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import jminvsm.SysFact;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class FornecedorViewController implements Initializable {
    private ServiceFornecedor serFornecedor;

    private Usuario userData;

    @FXML
    private TableColumn<Fornecedor, Integer> IDtabelaCliente;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Fornecedor, String> btntabelaCliente;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TableColumn<Fornecedor, String> contactotabelaCliente;

    @FXML
    private TableColumn<Fornecedor, String> nometabelaCliente;

    @FXML
    private TableColumn<Fornecedor, Integer> nuittabelaCliente;

    @FXML
    private TableColumn<Fornecedor, String> razaotabelaCliente;

    @FXML
    private TableView<Fornecedor> tabelaCliente;

    @FXML
    private TableColumn<Fornecedor, String> tipotabelaCliente;

    @FXML
    private TextField txtPesquisa;

    public void getViewAddCliente(ActionEvent evt) {
        /*
        Metodo para mostrar a nova tela ocultando a anterior
         */
        LoadAndMoveUtilities.showAsPopUP(evt, null);
        /* O caminho para o FXML a ser carregado deve considerar o pacote 
        onde se encontra o metodo estatico LoadAndMoveUtilities.loadFXML(path)
         */
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_compra/fornecedorView/fornecedorAdd/addFornecedor.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            serFornecedor = new ServiceFornecedor();
            showClientes();
        } catch (SQLException ex) {
            Logger.getLogger(Fornecedor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ----------------------- Clientes e Fornecedores -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Fornecedor> listaClientes;

    public void showClientes() {

        listaClientes = serFornecedor.getFornecedores();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo_forn"));
        razaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("razaosocial_forn"));
        nuittabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nuit_forn"));
        contactotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));

        FilteredList<Fornecedor> dadosFiltrados = new FilteredList<>(listaClientes, b -> true);
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
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
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

        tabelaCliente.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCliente(MouseEvent event) {
        Fornecedor c = tabelaCliente.getSelectionModel().getSelectedItem();
        if (c != null) {
            SysFact.setData(String.valueOf(c.getId()));
            txtIDCliente.setText(String.valueOf(c.getId()));
            deleteItem(event, c);
            ButtonUtilities.buttonChangeText(btnAdd, txtIDCliente);
        }

    }

    public void deleteItem(MouseEvent event, Fornecedor x){
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
                serFornecedor.excluir(x.getId());
                showClientes();
                txtIDCliente.setText("");
                SysFact.setData("");

            } else if (result.get() == btnCancel) {
                txtIDCliente.setText("");
                SysFact.setData("");
            }
        }
    }
}
