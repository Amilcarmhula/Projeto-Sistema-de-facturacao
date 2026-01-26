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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private ServiceCliente serviceCliente;
    private ServiceContacto serviceContacto;

    @FXML
    private Button btnAddContacto;

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
    private int vectorDados[];

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
//        Object obj = SysFact.getData();
//        this.vectorDados = (int[]) obj;

//        labHeadTitle.setText("Contacto #" + vectorDados[1]);

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

    

    public void showContactos(){
        if (SysFact.getData() != null) {
            ContactoCliente cc = (ContactoCliente)SysFact.getData();
                txtIDContacto.setText(cc.getId()+"");
                txtResponsavel.setText(cc.getResponsavel());
                txtContacto.setText(cc.getContacto_cli());
                txtEmail.setText(cc.getEmail_cli());
                txtSite.setText(cc.getWebsite_cli());
        }
    }

    public void addContacto(ActionEvent e) throws SQLException {
        Cliente c = serviceCliente.consultaClienteByID(Integer.parseInt(txtIDContacto.getText()));
        serviceContacto.registar(txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), c, userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
//            showContactos();
            restContacto();
        }
    }

    public void updateContacto(ActionEvent e) throws SQLException {
        serviceContacto.actualizar(Integer.valueOf(txtIDContacto.getText()), txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
//            showContactos();
            restContacto();
        }
    }

    /*
    Funcoes de Linha selecionada
     */
//    public void selecionaContacto(MouseEvent event) throws SQLException {
//        ContactoCliente x = tabelaContacto.getSelectionModel().getSelectedItem();
//        if (x != null) {
//            txtIDContacto.setText(String.valueOf(x.getId()));
//            txtResponsavel.setText(x.getResponsavel());
//            txtContacto.setText(x.getContacto_cli());
//            txtEmail.setText(x.getEmail_cli());
//            txtSite.setText(x.getWebsite_cli());
//            deleteContacto(event, x);
//            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
//        }
//    }
    public void deleteContacto(MouseEvent event, ContactoCliente x) throws SQLException {
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
                serviceContacto.excluir(x.getId());
                if (serviceContacto.isOpsSuccess()) {
//                    showContactos();
                    restContacto();
                    serviceContacto.setOpsSuccess(false);
                }
            } else if (result.get() == btnCancel) {
                restContacto();
            }
        }
    }

    public void restContacto() {
        txtIDContacto.setText("");
        txtResponsavel.setText("");
        txtContacto.setText("");
        txtEmail.setText("");
        txtSite.setText("");
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
    }

}
