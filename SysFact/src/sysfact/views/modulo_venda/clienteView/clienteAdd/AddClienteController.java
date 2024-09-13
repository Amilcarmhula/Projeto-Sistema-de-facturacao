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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import sysfact.model.cliente.Cliente;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.model.cliente.endereco.EnderecoCliente;
import sysfact.model.usuario.Usuario;

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
    private Button btnClose;

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

    public void close() {
//        System.exit(0);
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa Física", "Jurídica"));
        if (SysFact.getData() != null) {
            txtIDCliente.setText((String) SysFact.getData());
        }

        try {
            showContactos();
            showClienteByID();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        estadoBTNCliente();
        estadoBTNContacto();
    }
    
    public void estadoBTNCliente(){
        if(!"".equals(txtIDCliente.getText())){
            btnAddCliente.setText("Actualizar");
        }else{
            btnAddCliente.setText("Adicionar");
        }
    }
    public void estadoBTNContacto(){
        if(!"".equals(txtIDContacto.getText())){
            btnAddContacto.setText("Actualizar");
        }else{
            btnAddContacto.setText("Adicionar");
        }
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

    private ObservableList<ContactoCliente> listaContactos;

    public void showContactos() throws SQLException {
        ContactoClienteDAO xDAO = new ContactoClienteDAO();
        ContactoCliente cc = new ContactoCliente();
        Cliente c = new Cliente();

        if ("".equals(txtIDCliente.getText())) {
            listaContactos = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            cc.setCliente(c);
            listaContactos = xDAO.searchEntityByCLiente(cc);
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
                                + "-fx-fill:linear-gradient(to bottom right, #00ff004d 0%, #0000ff80 50%, #ff00004d 100%);"
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
            estadoBTNContacto();
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void restContacto() {
        txtIDContacto.setText("");
        txtResponsavel.setText("");
        txtContacto.setText("");
        txtEmail.setText("");
        txtSite.setText("");
        estadoBTNContacto();
    }

    public void resetCliente() throws SQLException {
        txtIDCliente.setText("");
        combTipoCliente.setPromptText("Tipo de cliente");
        txtNome.setText("");
        txtRazao.setText("");
        txtNuit.setText("");
        estadoBTNCliente();
        showContactos();
    }

}
