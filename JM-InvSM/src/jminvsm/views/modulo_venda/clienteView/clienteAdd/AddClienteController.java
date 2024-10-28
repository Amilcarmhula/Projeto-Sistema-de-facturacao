/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.clienteView.clienteAdd;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
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
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
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
        //add cell of button edit 
        Callback<TableColumn<EnderecoCliente, String>, TableCell<EnderecoCliente, String>> cellFoctory = (TableColumn<EnderecoCliente, String> param) -> {
            // make cell containing buttons
            final TableCell<EnderecoCliente, String> cell = new TableCell<EnderecoCliente, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#000000b3;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            EnderecoCliente a = tabelaEndereco.getSelectionModel().getSelectedItem();
                            try {
                                serviceEndereco.excluir(a.getId());
                                showEndereco();
                            } catch (SQLException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaEndereco.setCellFactory(cellFoctory);
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

        //add cell of button edit 
        Callback<TableColumn<ContactoCliente, String>, TableCell<ContactoCliente, String>> cellFoctory = (TableColumn<ContactoCliente, String> param) -> {
            // make cell containing buttons
            final TableCell<ContactoCliente, String> cell = new TableCell<ContactoCliente, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#000000b3;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContactoCliente x = tabelaContacto.getSelectionModel().getSelectedItem();
                            try {
                                serviceContacto.excluir(x.getId());
                                if(serviceContacto.isOpsSuccess()){
                                    showContactos();
                                    serviceContacto.setOpsSuccess(false);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaContacto.setCellFactory(cellFoctory);

        tabelaContacto.setItems(listaContactos);
    }

    /*
    Funcoes de Adicao de registros
     */
    public void addCliente() throws SQLException {
        serviceCliente.registar(txtNome.getText(), txtRazao.getText(),
                combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText().equals("") ? "0" : txtNuit.getText()),
                userData);
        if (serviceCliente.isOpsSuccess()) {
            showCliente();
        }

    }

    public void addContacto() throws SQLException {
        Cliente c = serviceCliente.consultaClienteByID(Integer.parseInt(txtIDCliente.getText()));
        serviceContacto.registar(txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), c, userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
            showContactos();
            restContacto();
        }
    }

    public void addEndereco() throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        Integer id = Integer.valueOf(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        serviceEndereco.registar(combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(), 
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(),id, userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateCliente() throws SQLException {
        serviceCliente.actualizar(Integer.valueOf(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText()), txtNome.getText(), txtRazao.getText(),
                combTipoCliente.getValue(), Integer.valueOf(txtNuit.getText().equals("") ? "0" : txtNuit.getText()),
                userData);
        if (serviceCliente.isOpsSuccess()) {
            showCliente();
        }
    }

    public void updateContacto() throws SQLException {
        serviceContacto.actualizar(Integer.valueOf(txtIDContacto.getText()),txtEmail.getText(), txtContacto.getText(),
                txtSite.getText(), txtResponsavel.getText(), userData);

        if (serviceContacto.isOpsSuccess()) {
            serviceContacto.setOpsSuccess(false);
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco() throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        serviceEndereco.actualizar(Integer.valueOf(txtIDEndereco.getText()),combTipoEndereco.getValue(),txtAvenida.getText(), txtBairro.getText(), 
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
    public void selecionaContacto() {
        ContactoCliente x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel());
            txtContacto.setText(x.getContacto_cli());
            txtEmail.setText(x.getEmail_cli());
            txtSite.setText(x.getWebsite_cli());
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

    public void selecionaEndereco() {
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
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
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
