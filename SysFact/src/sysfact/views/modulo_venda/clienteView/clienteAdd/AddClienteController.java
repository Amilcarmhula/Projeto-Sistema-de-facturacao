/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_venda.clienteView.clienteAdd;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import sysfact.SysFact;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.cliente.contacto.ContactoClienteDAO;
import sysfact.dao.cliente.endereco.EnderecoClienteDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.model.cliente.endereco.EnderecoCliente;
import sysfact.model.usuario.Usuario;
import sysfact.util.ButtonUtilities;
import sysfact.util.LoadAndMoveUtilities;
import sysfact.views.modulo_venda.clienteView.ClienteViewController;
import sysfact.views.modulo_venda.vendaView.searchCliente.ClienteSearchController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddClienteController implements Initializable {

    private Usuario userData;

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
            LoadAndMoveUtilities.showStage(null,null);
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
        ClienteDAO xDAO = new ClienteDAO();
        Cliente c = xDAO.getLastEntity();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo());
        txtNome.setText(c.getNome());
        txtRazao.setText(c.getApelido());
        txtNuit.setText(String.valueOf(c.getNuit()));
        showContactos();
    }

    public void showClienteByID() throws SQLException {
        ClienteDAO xDAO = new ClienteDAO();
        if (!"".equals(txtIDCliente.getText())) {
            Cliente c = xDAO.getEntityByID(Integer.parseInt(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome());
            txtRazao.setText(c.getApelido());
            txtNuit.setText(String.valueOf(c.getNuit()));
            showContactos();
        }

    }

    private ObservableList<EnderecoCliente> listaEndereco;

    public void showEndereco() throws SQLException {
        EnderecoClienteDAO xDAO = new EnderecoClienteDAO();
        EnderecoCliente z = new EnderecoCliente();
        Cliente c = new Cliente();

        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setCliente(c);
            listaEndereco = xDAO.searchEntityByCLiente(z);
        }

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero"));
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
                            System.out.println(a.getId());
                            try {
                                EnderecoClienteDAO xDAO = new EnderecoClienteDAO();
                                xDAO.deleteEntity(a);
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
        ContactoClienteDAO xDAO = new ContactoClienteDAO();
        ContactoCliente z = new ContactoCliente();
        Cliente c = new Cliente();

        if ("".equals(txtIDCliente.getText())) {
            listaContactos = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setCliente(c);
            listaContactos = xDAO.searchEntityByCLiente(z);
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        websitetabelaContacto.setCellValueFactory(new PropertyValueFactory<>("website"));

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
                            System.out.println(x.getId());
                            try {
                                ContactoClienteDAO xDAO = new ContactoClienteDAO();
                                xDAO.deleteEntity(x);
                                showContactos();
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
        Cliente x = new Cliente();
        x.setTipo(combTipoCliente.getValue());
        x.setNome(txtNome.getText());
        x.setApelido(txtRazao.getText());
        x.setNuit(Integer.parseInt(txtNuit.getText()));
        x.setUsuario(userData);
        ClienteDAO xDAO = new ClienteDAO();
        xDAO.addEntity(x);
        showCliente();
    }

    public void addContacto() throws SQLException {
        ContactoCliente x = new ContactoCliente();
        x.setContacto(txtContacto.getText());
        x.setEmail(txtEmail.getText());
        x.setResponsavel(txtResponsavel.getText());
        x.setWebsite(txtSite.getText());
        Cliente c = new Cliente();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setCliente(c);
        x.setUsuario(userData);
        ContactoClienteDAO xDAO = new ContactoClienteDAO();
        xDAO.addEntity(x);
        showContactos();
        restContacto();
    }

    public void addEndereco() {
        Alert alert;
        EnderecoCliente x = new EnderecoCliente();
        x.setAvenida(txtAvenida.getText());
        x.setBairro(txtBairro.getText());
        x.setCidade(txtCidade.getText());
        Cliente c = new Cliente();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setCliente(c);
        x.setCodigoPostal(Integer.parseInt(txtCodigooPostal.getText()));
        x.setNumero(Integer.parseInt(txtNumeroEndereco.getText()));
        x.setProvincia(combProvincia.getValue());
        x.setRua(txtRua.getText());
        x.setTipo(combTipoEndereco.getValue());
        x.setUsuario(userData);
        try {
            EnderecoClienteDAO xDAO = new EnderecoClienteDAO();
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
        Cliente x = new Cliente();
        x.setTipo(combTipoCliente.getValue());
        x.setNome(txtNome.getText());
        x.setApelido(txtRazao.getText());
        x.setNuit(Integer.parseInt(txtNuit.getText()));
        x.setUsuario(userData);
        ClienteDAO xDAO;
        try {
            xDAO = new ClienteDAO();
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
        ContactoCliente x = new ContactoCliente();
        x.setContacto(txtContacto.getText());
        x.setEmail(txtEmail.getText());
        x.setResponsavel(txtResponsavel.getText());
        x.setWebsite(txtSite.getText());
        x.setUsuario(userData);
        ContactoClienteDAO xDAO = new ContactoClienteDAO();
        xDAO.updateEntityByID(x, Integer.parseInt(txtIDContacto.getText()));
        showContactos();
        restContacto();
    }

    public void updateEndereco() {
        Alert alert;
        EnderecoCliente x = new EnderecoCliente();
        x.setAvenida(txtAvenida.getText());
        x.setBairro(txtBairro.getText());
        x.setCidade(txtCidade.getText());
        x.setCodigoPostal(Integer.parseInt(txtCodigooPostal.getText()));
        x.setNumero(Integer.parseInt(txtNumeroEndereco.getText()));
        x.setProvincia(combProvincia.getValue());
        x.setRua(txtRua.getText());
        x.setTipo(combTipoEndereco.getValue());
        x.setUsuario(userData);
        try {
            EnderecoClienteDAO xDAO = new EnderecoClienteDAO();
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
    public void selecionaContacto() {
        ContactoCliente x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel());
            txtContacto.setText(x.getContacto());
            txtEmail.setText(x.getEmail());
            txtSite.setText(x.getWebsite());
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

    public void selecionaEndereco() {
        EnderecoCliente x = tabelaEndereco.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDEndereco.setText(String.valueOf(x.getId()));
            txtAvenida.setText(x.getAvenida());
            txtBairro.setText(x.getBairro());
            txtCidade.setText(x.getCidade());
            txtCodigooPostal.setText(String.valueOf(x.getCodigoPostal()));
            txtNumeroEndereco.setText(String.valueOf(x.getNumero()));
            combProvincia.setValue(x.getProvincia());
            txtRua.setText(x.getRua());
            combTipoEndereco.setValue(x.getTipo());
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
