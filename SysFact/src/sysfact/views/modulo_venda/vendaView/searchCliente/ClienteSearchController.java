/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_venda.vendaView.searchCliente;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.SysFact;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.usuario.Usuario;
import sysfact.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ClienteSearchController implements Initializable {

    private Usuario userData;

    @FXML
    private TableColumn<Cliente, Integer> IDtabelaCliente;

    @FXML
    private Button btnAdd;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TableColumn<Cliente, String> contactotabelaCliente;

    @FXML
    private TableColumn<Cliente, String> enderecotabelaCliente;

    @FXML
    private TableColumn<Cliente, Integer> nuittabelaCliente;

    @FXML
    private TableColumn<Cliente, String> razaotabelaCliente;

    @FXML
    private TableView<Cliente> tabelaCliente;

    @FXML
    private TableColumn<Cliente, String> emailtabelaCliente;

    @FXML
    private TextField txtPesquisa;

    public void close(ActionEvent event) {
        LoadAndMoveUtilities.returnToBaseAnchor();
        LoadAndMoveUtilities.setEstadoPopUP(false);
        LoadAndMoveUtilities.showAsPopUP(null, null);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void getViewAddCliente(ActionEvent event) {
        LoadAndMoveUtilities.showStage(event,null);
        LoadAndMoveUtilities.loadFXML(null, "../views/modulo_venda/clienteView/clienteAdd/addCliente.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            showClientes();
        } catch (SQLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ----------------------- Clientes e Fornecedores -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Cliente> listaClientes;

    public void showClientes() throws SQLException {

        ClienteDAO aDAO = new ClienteDAO();
        listaClientes = aDAO.listAllForSearch();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
//        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
//        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        razaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("apelido"));
        nuittabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nuit"));
        contactotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));
        emailtabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getContactoCliente().getEmail())));
        enderecotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printEnderecos())));

        FilteredList<Cliente> dadosFiltrados = new FilteredList<>(listaClientes, b -> true);
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getApelido().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getNuit()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Cliente> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

        tabelaCliente.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCliente() throws SQLException {
        Cliente c = tabelaCliente.getSelectionModel().getSelectedItem();
        if (c != null) {
            SysFact.setData(c);
            txtIDCliente.setText(String.valueOf(c.getId()));
            btnAdd.setText("Actualizar");
        }

    }
}
