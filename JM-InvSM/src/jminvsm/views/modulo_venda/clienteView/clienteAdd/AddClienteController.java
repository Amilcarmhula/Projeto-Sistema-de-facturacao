/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.clienteView.clienteAdd;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.cliente.contacto.ServiceContacto;
import jminvsm.service.cliente.endereco.ServiceEndereco;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddClienteController implements Initializable {

    private Usuario userData;
    private ServiceCliente serviceCliente;
    private ServiceContacto serviceContacto;
    private ServiceEndereco serviceEndereco;

    @FXML
    private Button btnAddCliente;
    @FXML
    private Button btnContactoView;
    @FXML
    private Button btnEnderecoView;
    private int countViewContacto = 1;
    private int countViewEndereco = 1;
    private Integer[] vectorDados = new Integer[3];

    @FXML
    private ComboBox<String> combTipoCliente;

    @FXML
    private TextField txtIDCliente;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtNuit;
    @FXML
    private TextField txtRazao;

    @FXML
    private VBox anchorMain;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            SysFact.setData(null);
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SysFact.setData(null);
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void addORupdateCliente(ActionEvent e) throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            addCliente(e);
        } else {
            updateCliente(e);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.userData = SysFact.getUserData();
        combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa Física", "Jurídica"));
        if (SysFact.getData() != null) {
            if (SysFact.getData() instanceof Cliente) {
                Cliente x = (Cliente) SysFact.getData();
                txtIDCliente.setText(x.getId() + "");
            }
        }
        try {
            serviceCliente = new ServiceCliente();
            serviceContacto = new ServiceContacto();
            serviceEndereco = new ServiceEndereco();

            lookContactos();
            lookEnderecos();

        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
    }

    /*
    Funcoes de Consultas
     */
    public void showCliente() throws SQLException {
        Cliente c = serviceCliente.getUtimoCliente();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo());
        txtNome.setText(c.getNome_cli());
        txtRazao.setText(c.getRazao_cli());
        txtNuit.setText(String.valueOf(c.getNuit_cli()));
    }

    public void showClienteByID() throws SQLException {
        if (!"".equals(txtIDCliente.getText())) {
            Cliente c = serviceCliente.consultaClienteByID(Integer.valueOf(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome_cli());
            txtRazao.setText(c.getRazao_cli());
            txtNuit.setText(String.valueOf(c.getNuit_cli()));
        }

    }

    private ObservableList<ContactoCliente> listaContactos;
    public void lookContactos() throws SQLException, IOException {
        listaContactos = serviceContacto.consultaContactoPorCLiente(Integer.parseInt(txtIDCliente.getText()));
        if (listaContactos != null) {
            for (ContactoCliente cc : listaContactos) {
                SysFact.setData(cc);
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/jminvsm/views/modulo_venda/clienteView/clienteAdd/contactoView.fxml"));
                anchorMain.getChildren().add(pane);
            }
            SysFact.setData(null);
        }
    }

    private ObservableList<EnderecoCliente> listaEndereco;
    public void lookEnderecos() throws SQLException, IOException {
        listaEndereco = serviceEndereco.consultaEnderecosPorCLiente(Integer.parseInt(txtIDCliente.getText()));
        if (listaEndereco != null) {
            for (EnderecoCliente ec : listaEndereco) {
                SysFact.setData(ec);
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/jminvsm/views/modulo_venda/clienteView/clienteAdd/enderecoView.fxml"));
                anchorMain.getChildren().add(pane);
            }
            SysFact.setData(null);
        }
    }

    /*
    Funcoes de Adicao de registros
     */
    public void addCliente(ActionEvent e) throws SQLException {
        serviceCliente.registar(txtNome.getText(), txtRazao.getText(),
                combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText().equals("") ? "0" : txtNuit.getText()),
                userData);
        if (serviceCliente.isOpsSuccess()) {
            showCliente();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateCliente(ActionEvent e) throws SQLException {
        serviceCliente.actualizar(Integer.valueOf(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText()), txtNome.getText(), txtRazao.getText(),
                combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText().equals("") ? "0" : txtNuit.getText()),
                userData);
        if (serviceCliente.isOpsSuccess()) {
            showCliente();
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void resetCliente() throws SQLException {
        txtIDCliente.setText("");
        combTipoCliente.setPromptText("Tipo de cliente");
        txtNome.setText("");
        txtRazao.setText("");
        txtNuit.setText("");
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
    }

    @FXML
    public void addConAddView(ActionEvent e) throws IOException {
        SysFact.setData(null);
        String path = "";
        if (e.getSource() == btnContactoView) {
            if (txtIDCliente.getText() != "") {
                vectorDados[0] = Integer.parseInt(txtIDCliente.getText());
                vectorDados[1] = countViewContacto++;
            } else {
                vectorDados[1] = 0;
            }

            path = "/jminvsm/views/modulo_venda/clienteView/clienteAdd/contactoView.fxml";
        } else if (e.getSource() == btnEnderecoView) {
            if (!"".equals(txtIDCliente.getText())) {
                vectorDados[0] = Integer.parseInt(txtIDCliente.getText());
                vectorDados[2] = countViewEndereco++;
            } else {
                vectorDados[2] = 0;
            }
            path = "/jminvsm/views/modulo_venda/clienteView/clienteAdd/enderecoView.fxml";
        }
        SysFact.setData(vectorDados);
        AnchorPane pane = FXMLLoader.load(getClass().getResource(path));
        anchorMain.getChildren().add(pane);

    }

}
