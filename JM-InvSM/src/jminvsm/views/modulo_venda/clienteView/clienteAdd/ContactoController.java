/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.clienteView.clienteAdd;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import jminvsm.SysFact;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.cliente.contacto.ServiceContacto;
import jminvsm.util.ButtonUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ContactoController implements Initializable {

    private Usuario userData;
    private ServiceContacto serviceContacto;
    private ServiceCliente serviceCliente;

    private Cliente cliente;
    private ContactoCliente contactoCliente;

    @FXML
    private Button btnAddContacto;
    @FXML
    private Button btnDelete;

    @FXML
    private Label labHeadTitle;
    @FXML
    private TextField txtContacto;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtIDContacto;
    @FXML
    private TextField txtResponsavel;
    @FXML
    private TextField txtSite;

    public void addORupdateContacto(ActionEvent e) throws SQLException {
        if ("".equals(txtIDContacto.getText())) {
            addContacto(e);
        } else {
            updateContacto(e);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.userData = SysFact.getUserData();
        if (SysFact.getData() != null) {
            if (SysFact.getData() instanceof Cliente) {
                this.cliente = (Cliente) SysFact.getData();
            }
        }

        try {
            serviceCliente = new ServiceCliente();
            serviceContacto = new ServiceContacto();
            showContactos();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
    }

    public void showContactos() {
        if ((SysFact.getData() instanceof ContactoCliente) && SysFact.getData() != null) {
            contactoCliente = (ContactoCliente) SysFact.getData();
            txtIDContacto.setText(contactoCliente.getId() + "");
            txtResponsavel.setText(contactoCliente.getResponsavel());
            txtContacto.setText(contactoCliente.getContacto_cli());
            txtEmail.setText(contactoCliente.getEmail_cli());
            txtSite.setText(contactoCliente.getWebsite_cli());
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
            SysFact.setData(null);
        }
    }

    public void addContacto(ActionEvent e) throws SQLException {
        serviceContacto.registar(txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), cliente, userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
            for (ContactoCliente cc : serviceContacto.consultaContactoPorCLiente(cliente.getId())) {
                SysFact.setData(cc);
            }
            showContactos();
        }
    }

    public void updateContacto(ActionEvent e) throws SQLException {
        serviceContacto.actualizar(Integer.valueOf(txtIDContacto.getText()), txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
        }
    }

    public void deleteContacto(ActionEvent event) throws SQLException {
        if ((event.getSource() == btnDelete) && (!"".equals(txtIDContacto.getText()) && !txtIDContacto.getText().isEmpty())) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnApagar = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnApagar, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnApagar) {
                serviceContacto.excluir(Integer.parseInt(txtIDContacto.getText()));
                if (serviceContacto.isOpsSuccess()) {
                    serviceContacto.setOpsSuccess(false);
                    /*
                    O que pretende aqui e encontrar a AnchorPane onde esta o botao que disparou esta accao dentro da VBox e remover essa AnchorPane
                     */
                    // botão que disparou a acção
                    Node source = (Node) event.getSource();
                    // AnchorPane que contém este botão
                    AnchorPane contactoPane1 = (AnchorPane) source.getParent();
                    // AnchorPane que contém essa AnchorPane que contém este botão
                    AnchorPane contactoPane2 = (AnchorPane) contactoPane1.getParent();
                    // VBox que contém essa AnchorPane que contém essa AnchorPane que contém este botão
                    VBox vbox = (VBox) contactoPane2.getParent();
                    // remover a view inteira
                    vbox.getChildren().remove(contactoPane2);

                }
            }
        }
    }
}
