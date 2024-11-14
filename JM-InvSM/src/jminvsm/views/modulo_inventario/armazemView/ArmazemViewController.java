/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.armazemView;

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
import jminvsm.model.armazem.Armazem;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ArmazemViewController implements Initializable {

    private Usuario userData;
    private ServiceArmazem servArmazem;

    @FXML
    private TableColumn<Armazem, Integer> IDtabelaCliente;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Armazem, String> btntabelaCliente;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TableColumn<Armazem, String> contactotabelaCliente;

    @FXML
    private TableColumn<Armazem, String> descricaotabelaCliente;

    @FXML
    private TableColumn<Armazem, String> nometabelaCliente;

    @FXML
    private TableColumn<Armazem, Boolean> estadotabelaCliente;

    @FXML
    private TableView<Armazem> tabelaCliente;

    @FXML
    private TableColumn<Armazem, String> tipotabelaCliente;

    @FXML
    private TextField txtPesquisa;

    public void getViewAddCliente(ActionEvent evt) {
        LoadAndMoveUtilities.showAsPopUP(evt, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/armazemView/armazemAdd/addArmazem.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            servArmazem = new ServiceArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(ArmazemViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        showClientes();
    }

    /*
    ----------------------- Clientes e Fornecedores -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Armazem> listaClientes;

    public void showClientes() {

        listaClientes = servArmazem.listaArmazens();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome_arm"));
        descricaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("descricao_arm"));
        estadotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("estado"));
        contactotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));

        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaClientes, b -> true);
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome_arm().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getDescricao_arm().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getTipo().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Armazem> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

        tabelaCliente.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCliente(MouseEvent event) {
        Armazem c = tabelaCliente.getSelectionModel().getSelectedItem();
        if (c != null) {
            SysFact.setData(String.valueOf(c.getId()));
            txtIDCliente.setText(String.valueOf(c.getId()));
            deleteItem(event, c);
            ButtonUtilities.buttonChangeText(btnAdd, txtIDCliente);
        }
    }

    public void deleteItem(MouseEvent event, Armazem x) {
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
                servArmazem.excluir(x.getId());
                if (servArmazem.isOpsSuccess()) {
                    showClientes();
                }
            } else if (result.get() == btnCancel) {
            }
        }
    }
}
