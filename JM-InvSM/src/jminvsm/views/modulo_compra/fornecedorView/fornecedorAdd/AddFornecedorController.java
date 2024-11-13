/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_compra.fornecedorView.fornecedorAdd;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.fornecedor.contacto.ServiceContacto;
import jminvsm.service.fornecedor.endereco.ServiceEndereco;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddFornecedorController implements Initializable {

    private Usuario userData;
    private ServiceFornecedor serviceFornecedor;
    private ServiceContacto serviceContacto;
    private ServiceEndereco serviceEndereco;

    @FXML
    private Button btnAddCliente;
    @FXML
    private Button btnAddContacto;
    @FXML
    private Button btnAddEndereco;
    @FXML
    private Button btnClose;

    @FXML
    private ComboBox<String> combProvincia;
    @FXML
    private ComboBox<String> combTipoCliente;

    @FXML
    private TableView<ContactoFornecedor> tabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, Integer> IDtabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, String> responsaveltabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, String> contactotabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, String> emailtabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, String> btntabelaContacto;
    @FXML
    private TableColumn<ContactoFornecedor, String> websitetabelaContacto;

    @FXML
    private TableView<EnderecoFornecedor> tabelaEndereco;

    @FXML
    private TableColumn<EnderecoFornecedor, Integer> numerotabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> provinciatabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> ruatabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> btntabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> cidadetabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, Integer> codigopostaltabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, Integer> IDtabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> avenidatabelaEndereco;
    @FXML
    private TableColumn<EnderecoFornecedor, String> bairrotabelaEndereco;

    @FXML
    private TextField txtAvenida;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtCodigooPostal;

    @FXML
    private TextField txtContacto;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TextField txtIDContacto;

    @FXML
    private TextField txtIDEndereco;

    @FXML
    private TextField txtNuit;

    @FXML
    private TextField txtNumeroEndereco;

    @FXML
    private TextField txtRazao;

    @FXML
    private TextField txtResponsavel;

    @FXML
    private TextField txtRua;

    @FXML
    private TextField txtSite;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SysFact.setData(null);
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void addORupdateContacto(){
        if ("".equals(txtIDContacto.getText())) {
            addContacto();
        } else {
            updateContacto();
        }
    }

    public void addORupdateCliente() {
        if ("".equals(txtIDCliente.getText())) {
            addFornecedor();
        } else {
            updateFornecedor();
        }
    }

    public void addORupdateEndereco() {
        if ("".equals(txtIDEndereco.getText())) {
            addEndereco();
        } else {
            updateEndereco();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa Física", "Jurídica"));
        combProvincia.setItems(FXCollections.observableArrayList(
                "Maputo(Cidade)",
                "Maputo(Provincia)",
                "Gaza",
                "Inhambane",
                "Sofala",
                "Manica",
                "Tete",
                "Zambezia",
                "Napula",
                "Niassa",
                "Cabo Delgado"
        )
        );
        if (SysFact.getData() != null) {
            if (SysFact.getData() instanceof String) {
                txtIDCliente.setText((String) SysFact.getData());
            }
        }

        try {
            serviceFornecedor = new ServiceFornecedor();
            serviceContacto = new ServiceContacto();
            serviceEndereco = new ServiceEndereco();
            showContactos();
            showClienteByID();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddFornecedorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);

    }

    /*
    Funcoes de Consultas
     */
    public void showCliente() {
        Fornecedor c = serviceFornecedor.ultimoFornecedor();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo_forn());
        txtRazao.setText(c.getRazaosocial_forn());
        txtNuit.setText(String.valueOf(c.getNuit_forn()));
        showContactos();
        showEndereco();
    }

    public void showClienteByID() {
        if (!"".equals(txtIDCliente.getText())) {
            Fornecedor c = serviceFornecedor.consultaFornecedor(Integer.parseInt(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo_forn());
            txtRazao.setText(c.getRazaosocial_forn());
            txtNuit.setText(String.valueOf(c.getNuit_forn()));
            showContactos();
            showEndereco();
        }

    }

    private ObservableList<EnderecoFornecedor> listaEndereco;

    public void showEndereco() {
        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = serviceEndereco.getEnderecos();
        } else {
            listaEndereco = serviceEndereco.consultaEnderecosPorFornecedor(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia_forn"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade_forn"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro_forn"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida_forn"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua_forn"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal_forn"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero_forn"));

        tabelaEndereco.setItems(listaEndereco);
    }

    private ObservableList<ContactoFornecedor> listaContactos;

    public void showContactos() {
        if ("".equals(txtIDCliente.getText())) {
            listaContactos = serviceContacto.getContactos();
        } else {
            listaContactos = serviceContacto.consultaContactoPorFornecedor(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto_forn"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email_forn"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel_forn"));
        websitetabelaContacto.setCellValueFactory(new PropertyValueFactory<>("website_forn"));

        tabelaContacto.setItems(listaContactos);
    }

    /*
    Funcoes de Adicao de registros
     */
    public void addFornecedor() {
        serviceFornecedor.registar(txtRazao.getText(), combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText().equals("")?"0":txtNuit.getText()), userData);
        if (serviceFornecedor.isOpsSuccess()) {
            showCliente();
            ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        }
    }

    public void addContacto() {
        Fornecedor forn = serviceFornecedor.consultaFornecedor(Integer.parseInt(txtIDCliente.getText().equals("")?"0":txtIDCliente.getText()));
        serviceContacto.registar(txtEmail.getText(), txtContacto.getText(), txtSite.getText(), txtResponsavel.getText(), forn, userData);
        if (serviceContacto.isOpsSuccess()) {
            showContactos();
            restContacto();
        }
    }

    public void addEndereco() {
        serviceEndereco.registar(txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), Integer.valueOf(txtCodigooPostal.getText().equals("")?"0":txtCodigooPostal.getText()),
                Integer.valueOf(txtNumeroEndereco.getText().equals("")?"0":txtNumeroEndereco.getText()),
                combProvincia.getValue(), txtRua.getText(), Integer.valueOf(txtIDCliente.getText().equals("")?"0":txtIDCliente.getText()), userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateFornecedor() {
        serviceFornecedor.actualizar(Integer.parseInt(txtIDCliente.getText()), txtRazao.getText(), combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText()), userData);
        if (serviceFornecedor.isOpsSuccess()) {
            showCliente();
            showContactos();
            showEndereco();
        }
    }

    public void updateContacto(){
        serviceContacto.actualizar(Integer.parseInt(txtIDContacto.getText()), txtEmail.getText(), txtContacto.getText(), txtSite.getText(), txtResponsavel.getText(), userData);
        if (serviceContacto.isOpsSuccess()) {
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco() {
        serviceEndereco.actualizar(Integer.parseInt(txtIDEndereco.getText()), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), Integer.parseInt(txtCodigooPostal.getText()), Integer.parseInt(txtNumeroEndereco.getText()),
                combProvincia.getValue(), txtRua.getText(), Integer.parseInt(txtIDCliente.getText()), userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes de Linha selecionada
     */
    public void selecionaContacto(MouseEvent event) {
        ContactoFornecedor x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel_forn());
            txtContacto.setText(x.getContacto_forn());
            txtEmail.setText(x.getEmail_forn());
            txtSite.setText(x.getWebsite_forn());
            deleteContacto(event, x);
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

    public void deleteContacto(MouseEvent event, ContactoFornecedor x) {
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
                    showContactos();
                    restContacto();
                }

            } else if (result.get() == btnCancel) {
                restContacto();
            }
        }
    }

    public void selecionaEndereco(MouseEvent event){
        EnderecoFornecedor x = tabelaEndereco.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDEndereco.setText(String.valueOf(x.getId()));
            txtAvenida.setText(x.getAvenida_forn());
            txtBairro.setText(x.getBairro_forn());
            txtCidade.setText(x.getCidade_forn());
            txtCodigooPostal.setText(String.valueOf(x.getCodigoPostal_forn()));
            txtNumeroEndereco.setText(String.valueOf(x.getNumero_forn()));
            combProvincia.setValue(x.getProvincia_forn());
            txtRua.setText(x.getRua_forn());
            deleteEndereco(event, x);
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
        }
    }

    public void deleteEndereco(MouseEvent event, EnderecoFornecedor x) {
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
                serviceEndereco.excluir(x.getId());
                if (serviceEndereco.isOpsSuccess()) {
                    showEndereco();
                    resetEndereco();
                }
            } else if (result.get() == btnCancel) {
                resetEndereco();
            }
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void resetCliente() {
        txtIDCliente.setText("");
        combTipoCliente.setPromptText("Tipo de cliente");
        txtRazao.setText("");
        txtNuit.setText("");
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        showContactos();
        showEndereco();
        restContacto();
        resetEndereco();
    }

    public void restContacto() {
        txtIDContacto.setText("");
        txtResponsavel.setText("");
        txtContacto.setText("");
        txtEmail.setText("");
        txtSite.setText("");
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);

    }

    public void resetEndereco() {
        txtIDEndereco.setText("");
        txtAvenida.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtCodigooPostal.setText("");
        txtNumeroEndereco.setText("");
        combProvincia.setPromptText("Provincia");
        txtRua.setText("");
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);

    }

}
