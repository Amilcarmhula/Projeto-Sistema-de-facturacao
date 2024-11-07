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
    private Button btnAddContacto;
    @FXML
    private Button btnAddEndereco;

    @FXML
    private ComboBox<String> combProvincia;
    @FXML
    private ComboBox<String> combTipoCliente;
    @FXML
    private ComboBox<String> combTipoEndereco;

    @FXML
    private TableView<ContactoCliente> tabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, Integer> IDtabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, String> responsaveltabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, String> contactotabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, String> emailtabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, String> btntabelaContacto;
    @FXML
    private TableColumn<ContactoCliente, String> websitetabelaContacto;

    @FXML
    private TableView<EnderecoCliente> tabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> tipotabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> numerotabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> provinciatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> ruatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> btntabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> cidadetabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> codigopostaltabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> IDtabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> avenidatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> bairrotabelaEndereco;

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
    private TextField txtNome;

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

    public void addORupdateContacto(ActionEvent e) throws SQLException {
        if ("".equals(txtIDContacto.getText())) {
            addContacto(e);
        } else {
            updateContacto(e);
        }
    }

    public void addORupdateCliente(ActionEvent e) throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            addCliente(e);
        } else {
            updateCliente(e);
        }
    }

    public void addORupdateEndereco(ActionEvent e) throws SQLException {
        if ("".equals(txtIDEndereco.getText())) {
            addEndereco(e);
        } else {
            updateEndereco(e);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.userData = SysFact.getUserData();
        combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa Física", "Jurídica"));
        combTipoEndereco.setItems(FXCollections.observableArrayList("Facturação", "Envio"));
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
            if (SysFact.getData() instanceof Cliente) {
                Cliente x = (Cliente) SysFact.getData();
                txtIDCliente.setText(x.getId() + "");
            }
        }

        try {
            serviceCliente = new ServiceCliente();
            serviceContacto = new ServiceContacto();
            serviceEndereco = new ServiceEndereco();
            showContactos();
            showClienteByID();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
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
        showContactos();
    }

    public void showClienteByID() throws SQLException {
        if (!"".equals(txtIDCliente.getText())) {
            Cliente c = serviceCliente.consultaClienteByID(Integer.valueOf(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome_cli());
            txtRazao.setText(c.getRazao_cli());
            txtNuit.setText(String.valueOf(c.getNuit_cli()));
            showContactos();
        }

    }

    private ObservableList<EnderecoCliente> listaEndereco;

    public void showEndereco() throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = serviceEndereco.listaEnderecos();
        } else {
            listaEndereco = serviceEndereco.consultaEnderecosPorCLiente(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("tipo_cli"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia_cli"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade_cli"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro_cli"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida_cli"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua_cli"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal_cli"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero_cli"));

        tabelaEndereco.setItems(listaEndereco);
    }

    private ObservableList<ContactoCliente> listaContactos;

    public void showContactos() throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            listaContactos = serviceContacto.listaContactos();
        } else {
            listaContactos = serviceContacto.consultaContactoPorCLiente(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto_cli"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email_cli"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        websitetabelaContacto.setCellValueFactory(new PropertyValueFactory<>("website_cli"));

        tabelaContacto.setItems(listaContactos);
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

    public void addContacto(ActionEvent e) throws SQLException {
        Cliente c = serviceCliente.consultaClienteByID(Integer.parseInt(txtIDCliente.getText()));
        serviceContacto.registar(txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), c, userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
            showContactos();
            restContacto();
        }
    }

    public void addEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        Integer id = Integer.valueOf(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        serviceEndereco.registar(combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), id, userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
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

    public void updateContacto(ActionEvent e) throws SQLException {
        serviceContacto.actualizar(Integer.valueOf(txtIDContacto.getText()), txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        serviceEndereco.actualizar(Integer.valueOf(txtIDEndereco.getText()), combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes de Linha selecionada
     */
    public void selecionaContacto(MouseEvent event) throws SQLException {
        ContactoCliente x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel());
            txtContacto.setText(x.getContacto_cli());
            txtEmail.setText(x.getEmail_cli());
            txtSite.setText(x.getWebsite_cli());
            deleteContacto(event, x);
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

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
                    showContactos();
                    restContacto();
                    serviceContacto.setOpsSuccess(false);
                }
            } else if (result.get() == btnCancel) {
                restContacto();
            }
        }
    }

    public void selecionaEndereco(MouseEvent event) throws SQLException {
        EnderecoCliente x = tabelaEndereco.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDEndereco.setText(String.valueOf(x.getId()));
            txtAvenida.setText(x.getAvenida_cli());
            txtBairro.setText(x.getBairro_cli());
            txtCidade.setText(x.getCidade_cli());
            txtCodigooPostal.setText(String.valueOf(x.getCodigoPostal_cli()));
            txtNumeroEndereco.setText(String.valueOf(x.getNumero_cli()));
            combProvincia.setValue(x.getProvincia_cli());
            txtRua.setText(x.getRua_cli());
            combTipoEndereco.setValue(x.getTipo_cli());
            deleteEndereco(event, x);
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
        }
    }

    public void deleteEndereco(MouseEvent event, EnderecoCliente x) throws SQLException {
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
        txtNome.setText("");
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
        combTipoEndereco.setPromptText("Tipo de endereco");
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
    }

}
