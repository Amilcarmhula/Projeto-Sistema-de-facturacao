/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.clienteView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.listagem.ServicePesquisaListagem;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ClienteViewController implements Initializable {

    private Usuario userData;
    private ServiceCliente serviceCliente;
    private ServicePesquisaListagem servicePesquisa;

    @FXML
    private TableColumn<Cliente, Integer> IDtabelaCliente;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Cliente, String> btntabelaCliente;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TableColumn<Cliente, String> contactotabelaCliente;

    @FXML
    private TableColumn<Cliente, String> enderecotabelaCliente;

    @FXML
    private TableColumn<Cliente, String> nometabelaCliente;

    @FXML
    private TableColumn<Cliente, Integer> nuittabelaCliente;

    @FXML
    private TableColumn<Cliente, String> razaotabelaCliente;

    @FXML
    private TableView<Cliente> tabelaCliente;

    @FXML
    private TableColumn<Cliente, String> tipotabelaCliente;

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
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/clienteView/clienteAdd/cliente_unificado/addCliente.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.userData = SessaoAplicacao.getUserData();
        try {
            serviceCliente = new ServiceCliente();
            servicePesquisa = new ServicePesquisaListagem();
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
    private boolean pesquisaConfigurada;

    public void showClientes() throws SQLException {
        listaClientes = serviceCliente.listaClienteView();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome_cli"));
        razaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("razao_cli"));
        nuittabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nuit_cli"));
        contactotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(servicePesquisa.contactosCliente(cellData.getValue())));
        enderecotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(servicePesquisa.enderecosCliente(cellData.getValue())));

        configurarPesquisa();
        aplicarPesquisa();
    }

    private void configurarPesquisa() {
        if (!pesquisaConfigurada) {
            txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> aplicarPesquisa());
            pesquisaConfigurada = true;
        }
    }

    private void aplicarPesquisa() {
        SortedList<Cliente> sortedData = new SortedList<>(
                servicePesquisa.filtrarClientes(listaClientes, txtPesquisa.getText()));
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

        tabelaCliente.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCliente(MouseEvent event) throws SQLException {
        Cliente c = tabelaCliente.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtIDCliente.setText(String.valueOf(c.getId()));
            deleteItem(event, c);
            Cliente cc = serviceCliente.consultaClienteByID(Integer.valueOf(txtIDCliente.getText()));
            SessaoAplicacao.setData(cc);
            ButtonUtilities.buttonChangeText(btnAdd, txtIDCliente);
//            btnAdd.setText("Actualizar");
        }
    }

    public void deleteItem(MouseEvent event, Cliente x) throws SQLException {
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
                try {
                    serviceCliente.excluir(x.getId());
                    if (serviceCliente.isOpsSuccess()) {
                        showSuccessAlert("Exclusao de registo com sucesso.");
                        showClientes();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
                }
            } else if (result.get() == btnCancel) {
            }
        }
    }
}

