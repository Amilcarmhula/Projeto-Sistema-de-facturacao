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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jminvsm.SysFact;
import jminvsm.dao.fornecedor.FornecedorDAO;
import jminvsm.dao.fornecedor.contacto.ContactoFornecedorDAO;
import jminvsm.dao.fornecedor.endereco.EnderecoFornecedorDAO;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddFornecedorController implements Initializable {

    private Usuario userData;

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
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void addORupdateContacto() throws SQLException {
        if ("".equals(txtIDContacto.getText())) {
            addContacto();
        } else {
            updateContacto();
        }
    }

    public void addORupdateCliente() throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            addCliente();
        } else {
            updateCliente();
        }
    }

    public void addORupdateEndereco() throws SQLException {
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
    public void showCliente() throws SQLException {
        FornecedorDAO xDAO = new FornecedorDAO();
        Fornecedor c = xDAO.getLastEntity();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo_forn());
        txtRazao.setText(c.getRazaosocial_forn());
        txtNuit.setText(String.valueOf(c.getNuit_forn()));
        showContactos();
    }

    public void showClienteByID() throws SQLException {
        FornecedorDAO xDAO = new FornecedorDAO();
        if (!"".equals(txtIDCliente.getText())) {
            Fornecedor c = xDAO.getEntityByID(Integer.parseInt(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo_forn());
            txtRazao.setText(c.getRazaosocial_forn());
            txtNuit.setText(String.valueOf(c.getNuit_forn()));
            showContactos();
        }

    }

    private ObservableList<EnderecoFornecedor> listaEndereco;

    public void showEndereco() throws SQLException {
        EnderecoFornecedorDAO xDAO = new EnderecoFornecedorDAO();
        EnderecoFornecedor z = new EnderecoFornecedor();
        Fornecedor c = new Fornecedor();

        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setFornecedor(c);
            listaEndereco = xDAO.searchEntityByCLiente(z);
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

    public void showContactos() throws SQLException {
        ContactoFornecedorDAO xDAO = new ContactoFornecedorDAO();
        ContactoFornecedor z = new ContactoFornecedor();
        Fornecedor c = new Fornecedor();

        if ("".equals(txtIDCliente.getText())) {
            listaContactos = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setFornecedor(c);
            listaContactos = xDAO.searchEntityByCLiente(z);
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
    public void addCliente() throws SQLException {
        Fornecedor x = new Fornecedor();
        x.setTipo_forn(combTipoCliente.getValue());
        x.setRazaosocial_forn(txtRazao.getText());
        x.setNuit_forn(Integer.parseInt(txtNuit.getText()));
        x.setUsuario(userData);
        FornecedorDAO xDAO = new FornecedorDAO();
        xDAO.addEntity(x);
        showCliente();
    }

    public void addContacto() throws SQLException {
        ContactoFornecedor x = new ContactoFornecedor();
        x.setContacto_forn(txtContacto.getText());
        x.setEmail_forn(txtEmail.getText());
        x.setResponsavel_forn(txtResponsavel.getText());
        x.setWebsite_forn(txtSite.getText());
        Fornecedor c = new Fornecedor();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setFornecedor(c);
        x.setUsuario(userData);
        ContactoFornecedorDAO xDAO = new ContactoFornecedorDAO();
        xDAO.addEntity(x);
        showContactos();
        restContacto();
    }

    public void addEndereco() {
        Alert alert;
        EnderecoFornecedor x = new EnderecoFornecedor();
        x.setAvenida_forn(txtAvenida.getText());
        x.setBairro_forn(txtBairro.getText());
        x.setCidade_forn(txtCidade.getText());
        Fornecedor c = new Fornecedor();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setFornecedor(c);
        x.setCodigoPostal_forn(Integer.parseInt(txtCodigooPostal.getText()));
        x.setNumero_forn(Integer.parseInt(txtNumeroEndereco.getText()));
        x.setProvincia_forn(combProvincia.getValue());
        x.setRua_forn(txtRua.getText());
        x.setUsuario(userData);
        try {
            EnderecoFornecedorDAO xDAO = new EnderecoFornecedorDAO();
            xDAO.addEntity(x);
            showEndereco();
            resetEndereco();
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao adicionar Endereco" + ex);
            alert.showAndWait();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateCliente() {
        Alert alert;
        Fornecedor x = new Fornecedor();
        x.setTipo_forn(combTipoCliente.getValue());
        x.setRazaosocial_forn(txtRazao.getText());
        x.setNuit_forn(Integer.parseInt(txtNuit.getText()));
        x.setUsuario(userData);
        FornecedorDAO xDAO;
        try {
            xDAO = new FornecedorDAO();
            xDAO.updateEntityByID(x, Integer.parseInt(txtIDCliente.getText()));
            showContactos();
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao actualizar cliente" + ex);
            alert.showAndWait();
        }

    }

    public void updateContacto() throws SQLException {
        ContactoFornecedor x = new ContactoFornecedor();
        x.setContacto_forn(txtContacto.getText());
        x.setEmail_forn(txtEmail.getText());
        x.setResponsavel_forn(txtResponsavel.getText());
        x.setWebsite_forn(txtSite.getText());
        x.setUsuario(userData);
        ContactoFornecedorDAO xDAO = new ContactoFornecedorDAO();
        xDAO.updateEntityByID(x, Integer.parseInt(txtIDContacto.getText()));
        showContactos();
        restContacto();
    }

    public void updateEndereco() {
        Alert alert;
        EnderecoFornecedor x = new EnderecoFornecedor();
        x.setAvenida_forn(txtAvenida.getText());
        x.setBairro_forn(txtBairro.getText());
        x.setCidade_forn(txtCidade.getText());
        x.setCodigoPostal_forn(Integer.parseInt(txtCodigooPostal.getText()));
        x.setNumero_forn(Integer.parseInt(txtNumeroEndereco.getText()));
        x.setProvincia_forn(combProvincia.getValue());
        x.setRua_forn(txtRua.getText());
        x.setUsuario(userData);
        try {
            EnderecoFornecedorDAO xDAO = new EnderecoFornecedorDAO();
            xDAO.updateEntityByID(x, Integer.parseInt(txtIDEndereco.getText()));
            showEndereco();
            resetEndereco();
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao adicionar Endereco" + ex);
            alert.showAndWait();
        }
    }

    /*
    Funcoes de Linha selecionada
     */
    public void selecionaContacto(MouseEvent event) throws SQLException {
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

    public void deleteContacto(MouseEvent event, ContactoFornecedor x) throws SQLException {
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
                ContactoFornecedorDAO xDAO = new ContactoFornecedorDAO();
                xDAO.deleteEntity(x);
                showContactos();
                restContacto();

            } else if (result.get() == btnCancel) {
                restContacto();
            }
        }
    }

    public void selecionaEndereco(MouseEvent event) throws SQLException {
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

    public void deleteEndereco(MouseEvent event, EnderecoFornecedor x) throws SQLException {
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
                EnderecoFornecedorDAO xDAO = new EnderecoFornecedorDAO();
                xDAO.deleteEntity(x);
                showEndereco();
                resetEndereco();
            } else if (result.get() == btnCancel) {
                resetEndereco();
            }
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void resetCliente() throws SQLException {
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
