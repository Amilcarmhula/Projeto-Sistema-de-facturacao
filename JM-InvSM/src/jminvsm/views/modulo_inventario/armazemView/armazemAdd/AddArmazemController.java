/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.armazemView.armazemAdd;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
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
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import jminvsm.model.armazem.endereco.EnderecoArmazem;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.ServiceArmazemCadastro;
import jminvsm.service.armazem.ServiceContactoArmazem;
import jminvsm.service.armazem.ServiceEnderecoArmazem;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddArmazemController implements Initializable {

    private Usuario userData;
    private Armazem armazem;
    private ServiceArmazemCadastro serviceArmazem;
    private ServiceContactoArmazem serviceContacto;
    private ServiceEnderecoArmazem serviceEndereco;

    @FXML
    private Button btnAddArmazem;
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
    private TableView<ContactoArmazem> tabelaContacto;
    @FXML
    private TableColumn<ContactoArmazem, Integer> IDtabelaContacto;
    @FXML
    private TableColumn<ContactoArmazem, String> responsaveltabelaContacto;
    @FXML
    private TableColumn<ContactoArmazem, String> contactotabelaContacto;
    @FXML
    private TableColumn<ContactoArmazem, String> emailtabelaContacto;
    @FXML
    private TableColumn<ContactoArmazem, String> btntabelaContacto;

    @FXML
    private TableView<EnderecoArmazem> tabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, Integer> numerotabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> provinciatabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> ruatabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> btntabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> cidadetabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, Integer> codigopostaltabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, Integer> IDtabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> avenidatabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> bairrotabelaEndereco;

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
    private TextField txtNome;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtResponsavel;

    @FXML
    private TextField txtRua;

    @FXML
    private TextField txtSite;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void addORupdateContacto(ActionEvent e){
        if ("".equals(txtIDContacto.getText())) {
            addContacto(e);
        } else {
            updateContacto(e);
        }
    }

    public void addORupdateCliente(ActionEvent e){
        if ("".equals(txtIDCliente.getText())) {
            addArmazem(e);
        } else {
            updateArmazem(e);
        }
    }

    public void addORupdateEndereco(ActionEvent e){
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
        this.userData = SessaoAplicacao.getUserData();
//        txtDescricao.setWrapText(true);
        combTipoCliente.setItems(FXCollections.observableArrayList("Central", "Regional", "Loga", "..."));
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
            serviceArmazem = new ServiceArmazemCadastro();
            serviceContacto = new ServiceContactoArmazem();
            serviceEndereco = new ServiceEnderecoArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(AddArmazemController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            showContactos();
            showClienteByID();
            showEndereco();
            
        ButtonUtilities.buttonChangeText(btnAddArmazem, txtIDCliente);
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
        tabelaContacto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaEndereco.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

//    

    /*
    Funcoes de Consultas
     */
    public void showLastStoreAdded(){
        Armazem c = armazem;
        if (c == null && armazemGuardado()) {
            c = serviceArmazem.consultaPorId(valor(txtIDCliente));
        }
        if (c == null) {
            return;
        }
        armazem = c;
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo());
        txtNome.setText(c.getNome_arm());
        txtDescricao.setText(c.getDescricao_arm());
        showContactos();
        showEndereco();
    }

    public void showClienteByID(){
        if (armazemGuardado()) {
            Armazem c = serviceArmazem.consultaPorId(valor(txtIDCliente));
            armazem = c;
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome_arm());
            txtDescricao.setText(c.getDescricao_arm());
            showContactos();
            showEndereco();
        }

    }

    private ObservableList<EnderecoArmazem> listaEndereco;

    public void showEndereco(){
        if (armazem == null && armazemGuardado()) {
            armazem = serviceArmazem.consultaPorId(valor(txtIDCliente));
        }
        if (armazem == null) {
            listaEndereco = FXCollections.observableArrayList();
        } else {
            listaEndereco = serviceEndereco.consultaPorArmazem(armazem);
        }

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia_arm"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade_arm"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro_arm"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida_arm"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua_arm"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal_arm"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero_arm"));

        tabelaEndereco.setItems(listaEndereco);
    }

    private ObservableList<ContactoArmazem> listaContactos;

    public void showContactos() {
        if (armazem == null && armazemGuardado()) {
            armazem = serviceArmazem.consultaPorId(valor(txtIDCliente));
        }
        if (armazem == null) {
            listaContactos = FXCollections.observableArrayList();
        } else {
            listaContactos = serviceContacto.consultaPorArmazem(armazem);
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto_arm"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email_arm"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel_arm"));
        tabelaContacto.setItems(listaContactos);
    }

    /*
    Funcoes de Adicao de registros
     */
    public void addArmazem(ActionEvent e) {
        try {
            armazem = serviceArmazem.registar(valor(combTipoCliente), valor(txtNome), valor(txtDescricao));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceArmazem.isOpsSuccess()) {
            showSuccessAlert("Armazem adicionado com sucesso!");
            ButtonUtilities.buttonChangeText(btnAddArmazem, txtIDCliente);
            showLastStoreAdded();
            serviceArmazem.setOpsSuccess(false);
        }

    }

    public void addContacto(ActionEvent e) {
        if (!armazemGuardado()) {
            mostrarAviso("Guarde primeiro os dados do armazÃ©m antes de adicionar contactos.");
            return;
        }
        try {
            armazem = serviceArmazem.consultaPorId(valor(txtIDCliente));
            serviceContacto.registar(valor(txtResponsavel), valor(txtContacto), valor(txtEmail), armazem);
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Contacto adicionado com sucesso!");
            showContactos();
            restContacto();
        }
    }

    public void addEndereco(ActionEvent e)  {
        if (!armazemGuardado()) {
            mostrarAviso("Guarde primeiro os dados do armazÃ©m antes de adicionar endereÃ§os.");
            return;
        }
        try {
            armazem = serviceArmazem.consultaPorId(valor(txtIDCliente));
            serviceEndereco.registar(valor(txtAvenida), valor(txtBairro), valor(txtCidade),
                    valor(txtCodigooPostal), valor(txtNumeroEndereco), valor(combProvincia),
                    valor(txtRua), armazem);
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Endereco registado com sucesso!");
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateArmazem(ActionEvent e) {
        try {
            armazem = serviceArmazem.actualizar(valor(txtIDCliente), valor(combTipoCliente), valor(txtNome), valor(txtDescricao));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceArmazem.isOpsSuccess()) {
            showSuccessAlert("Armazem actualizado com sucesso!");
            showContactos();
            serviceArmazem.setOpsSuccess(false);
        }
    }

    public void updateContacto(ActionEvent e) {
        try {
            serviceContacto.actualizar(valor(txtIDContacto), valor(txtResponsavel), valor(txtContacto), valor(txtEmail));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Contacto actualizado com sucesso!");
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco(ActionEvent e){
        try {
            serviceEndereco.actualizar(valor(txtIDEndereco), valor(txtAvenida), valor(txtBairro),
                    valor(txtCidade), valor(txtCodigooPostal), valor(txtNumeroEndereco),
                    valor(combProvincia), valor(txtRua));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Endereco actualizado com sucesso!");
            showEndereco();
            resetEndereco();
        }

    }

    /*
    Funcoes de Linha selecionada
     */
    public void selecionaContacto(MouseEvent event){
        ContactoArmazem x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel_arm());
            txtContacto.setText(x.getContacto_arm());
            txtEmail.setText(x.getEmail_arm());
            deleteContacto(event, x);
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

    public void deleteContacto(MouseEvent event, ContactoArmazem x){
        if (event.getButton() == MouseButton.SECONDARY) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == btnDelete) {
                try {
                    serviceContacto.excluir(x.getId());
                    if (serviceContacto.isOpsSuccess()) {
                        showSuccessAlert("Registo excluido com sucesso!");
                        showContactos();
                        restContacto();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
                }
            } else if (result.isPresent() && result.get() == btnCancel) {
                restContacto();
            }
        }
    }

    public void selecionaEndereco(MouseEvent event) {
        EnderecoArmazem x = tabelaEndereco.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDEndereco.setText(String.valueOf(x.getId()));
            txtAvenida.setText(x.getAvenida_arm());
            txtBairro.setText(x.getBairro_arm());
            txtCidade.setText(x.getCidade_arm());
            txtCodigooPostal.setText(String.valueOf(x.getCodigoPostal_arm()));
            txtNumeroEndereco.setText(String.valueOf(x.getNumero_arm()));
            combProvincia.setValue(x.getProvincia_arm());
            txtRua.setText(x.getRua_arm());
            deleteEndereco(event, x);
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
        }
    }

    public void deleteEndereco(MouseEvent event, EnderecoArmazem x){
        if (event.getButton() == MouseButton.SECONDARY) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == btnDelete) {
                try {
                    serviceEndereco.excluir(x.getId());
                    if (serviceEndereco.isOpsSuccess()) {
                        showSuccessAlert("Registo excluido com sucesso!");
                        showEndereco();
                        resetEndereco();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
                }
            } else if (result.isPresent() && result.get() == btnCancel) {
                resetEndereco();
            }
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void resetCliente() {
        armazem = null;
        txtIDCliente.setText("");
        combTipoCliente.setValue(null);
        combTipoCliente.setPromptText("Tipo de armazÃ©m");
        txtDescricao.setText("");
        txtNome.setText("");
        ButtonUtilities.buttonChangeText(btnAddArmazem, txtIDCliente);
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


    private boolean armazemGuardado() {
        return txtIDCliente.getText() != null && !txtIDCliente.getText().trim().isEmpty();
    }

    private static String valor(TextField campo) {
        return campo == null || campo.getText() == null ? "" : campo.getText().trim();
    }

    private static String valor(ComboBox<String> combo) {
        return combo == null || combo.getValue() == null ? null : combo.getValue();
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dados do armazÃ©m");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}

