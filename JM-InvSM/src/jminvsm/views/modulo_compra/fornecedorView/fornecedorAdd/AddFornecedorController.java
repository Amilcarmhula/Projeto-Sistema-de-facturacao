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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.fornecedor.ServiceContactoFornecedor;
import jminvsm.service.fornecedor.ServiceEnderecoFornecedor;
import jminvsm.service.fornecedor.ServiceFornecedorCadastro;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddFornecedorController implements Initializable {

    private Usuario userData;
    private Fornecedor fornecedor;
    private ServiceFornecedorCadastro serviceFornecedor;
    private ServiceContactoFornecedor serviceContacto;
    private ServiceEnderecoFornecedor serviceEndereco;

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
            SessaoAplicacao.setData(null);
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
        this.userData = SessaoAplicacao.getUserData();
        // Aplicado no controlador para compatibilidade com Scene Builder/JavaFX.
        tabelaContacto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaEndereco.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa FÃ­sica", "JurÃ­dica"));
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
        if (SessaoAplicacao.getData() != null) {
            if (SessaoAplicacao.getData() instanceof String) {
                txtIDCliente.setText((String) SessaoAplicacao.getData());
            }
        }

        try {
            serviceFornecedor = new ServiceFornecedorCadastro();
            serviceContacto = new ServiceContactoFornecedor();
            serviceEndereco = new ServiceEnderecoFornecedor();
            showContactos();
            showClienteByID();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddFornecedorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);

    }

    /*
    Funcoes de Consultas
     */
    public void showCliente() {
        Fornecedor c = fornecedor;
        if (c == null && fornecedorGuardado()) {
            c = serviceFornecedor.consultaPorId(valor(txtIDCliente));
        }
        if (c == null) {
            return;
        }
        fornecedor = c;
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo_forn());
        txtRazao.setText(c.getRazaosocial_forn());
        txtNuit.setText(String.valueOf(c.getNuit_forn()));
        showContactos();
        showEndereco();
    }

    public void showClienteByID() {
        if (fornecedorGuardado()) {
            Fornecedor c = serviceFornecedor.consultaPorId(valor(txtIDCliente));
            fornecedor = c;
            combTipoCliente.setValue(c.getTipo_forn());
            txtRazao.setText(c.getRazaosocial_forn());
            txtNuit.setText(String.valueOf(c.getNuit_forn()));
            showContactos();
            showEndereco();
        }

    }

    private ObservableList<EnderecoFornecedor> listaEndereco;

    public void showEndereco() {
        if (fornecedor == null && fornecedorGuardado()) {
            fornecedor = serviceFornecedor.consultaPorId(valor(txtIDCliente));
        }
        if (fornecedor == null) {
            listaEndereco = FXCollections.observableArrayList();
        } else {
            listaEndereco = serviceEndereco.consultaPorFornecedor(fornecedor);
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
        if (fornecedor == null && fornecedorGuardado()) {
            fornecedor = serviceFornecedor.consultaPorId(valor(txtIDCliente));
        }
        if (fornecedor == null) {
            listaContactos = FXCollections.observableArrayList();
        } else {
            listaContactos = serviceContacto.consultaPorFornecedor(fornecedor);
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
        try {
            fornecedor = serviceFornecedor.registar(valor(txtRazao), valor(combTipoCliente), valor(txtNuit));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceFornecedor.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            showCliente();
            ButtonUtilities.buttonChangeText(btnAddCliente, txtIDCliente);
        }
    }

    public void addContacto() {
        if (!fornecedorGuardado()) {
            mostrarAviso("Guarde primeiro os dados gerais do fornecedor.");
            return;
        }
        try {
            fornecedor = serviceFornecedor.consultaPorId(valor(txtIDCliente));
            serviceContacto.registar(valor(txtEmail), valor(txtContacto), valor(txtSite), valor(txtResponsavel), fornecedor);
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            showContactos();
            restContacto();
        }
    }

    public void addEndereco() {
        if (!fornecedorGuardado()) {
            mostrarAviso("Guarde primeiro os dados gerais do fornecedor.");
            return;
        }
        try {
            fornecedor = serviceFornecedor.consultaPorId(valor(txtIDCliente));
            serviceEndereco.registar(valor(txtAvenida), valor(txtBairro), valor(txtCidade),
                    valor(txtCodigooPostal), valor(txtNumeroEndereco), valor(combProvincia),
                    valor(txtRua), fornecedor, userData);
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateFornecedor() {
        try {
            fornecedor = serviceFornecedor.actualizar(valor(txtIDCliente), valor(txtRazao), valor(combTipoCliente), valor(txtNuit));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceFornecedor.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
            showCliente();
            showContactos();
            showEndereco();
        }
    }

    public void updateContacto(){
        try {
            serviceContacto.actualizar(valor(txtIDContacto), valor(txtEmail), valor(txtContacto), valor(txtSite), valor(txtResponsavel));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco() {
        try {
            fornecedor = serviceFornecedor.consultaPorId(valor(txtIDCliente));
            serviceEndereco.actualizar(valor(txtIDEndereco), valor(txtAvenida), valor(txtBairro),
                    valor(txtCidade), valor(txtCodigooPostal), valor(txtNumeroEndereco),
                    valor(combProvincia), valor(txtRua), fornecedor, userData);
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
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
        if (event.getButton() == MouseButton.SECONDARY) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            if (result.get() == btnDelete) {
                try {
                    serviceContacto.excluir(x.getId());
                    if (serviceContacto.isOpsSuccess()) {
                        showSuccessAlert("Registo excluido com sucesso.");
                        showContactos();
                        restContacto();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
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
        if (event.getButton() == MouseButton.SECONDARY) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            if (result.get() == btnDelete) {
                try {
                    serviceEndereco.excluir(x.getId());
                    if (serviceEndereco.isOpsSuccess()) {
                        showSuccessAlert("Registo excluido com sucesso.");
                        showEndereco();
                        resetEndereco();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
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
        fornecedor = null;
        txtIDCliente.setText("");
        combTipoCliente.setValue(null);
        combTipoCliente.setPromptText("Tipo de fornecedor");
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
        combProvincia.setValue(null);
        combProvincia.setPromptText("ProvÃ­ncia");
        txtRua.setText("");
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);

    }

    private boolean fornecedorGuardado() {
        return txtIDCliente.getText() != null && !txtIDCliente.getText().trim().isEmpty();
    }

    private static String valor(TextField campo) {
        return campo == null || campo.getText() == null ? "" : campo.getText().trim();
    }

    private static String valor(ComboBox<String> combo) {
        return combo == null || combo.getValue() == null ? null : combo.getValue();
    }

    private void mostrarAviso(String mensagem) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Aviso");
        dialog.setContentText(mensagem);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

}

