/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_inventario.armazemView.armazemAdd;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.dao.armazem.contacto.ContactoArmazemDAO;
import sysfact.dao.armazem.endereco.EnderecoArmazemDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.armazem.contacto.ContactoArmazem;
import sysfact.model.armazem.endereco.EnderecoArmazem;
import sysfact.model.usuario.Usuario;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddArmazemController implements Initializable {

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
    private TableColumn<ContactoArmazem, String> websitetabelaContacto;

    @FXML
    private TableView<EnderecoArmazem> tabelaEndereco;
    @FXML
    private TableColumn<EnderecoArmazem, String> tipotabelaEndereco;
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
//        txtDescricao.setWrapText(true);
        combTipoCliente.setItems(FXCollections.observableArrayList("Central", "Regional", "Loga", "..."));
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
            Logger.getLogger(AddArmazemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        estadoBTNCliente();
        estadoBTNContacto();
    }

    public void estadoBTNCliente() {
        if (!"".equals(txtIDCliente.getText())) {
            btnAddCliente.setText("Actualizar");
        } else {
            btnAddCliente.setText("Adicionar");
        }
    }

    public void estadoBTNContacto() {
        if (!"".equals(txtIDContacto.getText())) {
            btnAddContacto.setText("Actualizar");
        } else {
            btnAddContacto.setText("Adicionar");
        }
    }

    public void estadoBTNendereco() {
        if (!"".equals(txtIDEndereco.getText())) {
            btnAddEndereco.setText("Actualizar");
        } else {
            btnAddEndereco.setText("Adicionar");
        }
    }

    /*
    Funcoes de Consultas
     */
    public void showCliente() throws SQLException {
        ArmazemDAO xDAO = new ArmazemDAO();
        Armazem c = xDAO.getLastEntity();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo());
        txtNome.setText(c.getNome());
        txtDescricao.setText(c.getDescricao());
        showContactos();
    }

    public void showClienteByID() throws SQLException {
        ArmazemDAO xDAO = new ArmazemDAO();
        if (!"".equals(txtIDCliente.getText())) {
            Armazem c = xDAO.getEntityByID(Integer.parseInt(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome());
            txtDescricao.setText(c.getDescricao());
            showContactos();
        }

    }

    private ObservableList<EnderecoArmazem> listaEndereco;

    public void showEndereco() throws SQLException {
        EnderecoArmazemDAO xDAO = new EnderecoArmazemDAO();
        EnderecoArmazem z = new EnderecoArmazem();
        Armazem c = new Armazem();

        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setArmazem(c);
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
        Callback<TableColumn<EnderecoArmazem, String>, TableCell<EnderecoArmazem, String>> cellFoctory = (TableColumn<EnderecoArmazem, String> param) -> {
            // make cell containing buttons
            final TableCell<EnderecoArmazem, String> cell = new TableCell<EnderecoArmazem, String>() {
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
                            EnderecoArmazem a = tabelaEndereco.getSelectionModel().getSelectedItem();
                            System.out.println(a.getId());
                            try {
                                EnderecoArmazemDAO xDAO = new EnderecoArmazemDAO();
                                xDAO.deleteEntity(a);
                                showEndereco();
                            } catch (SQLException ex) {
                                Logger.getLogger(Armazem.class.getName()).log(Level.SEVERE, null, ex);
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

    private ObservableList<ContactoArmazem> listaContactos;

    public void showContactos() throws SQLException {
        ContactoArmazemDAO xDAO = new ContactoArmazemDAO();
        ContactoArmazem z = new ContactoArmazem();
        Armazem c = new Armazem();

        if ("".equals(txtIDCliente.getText())) {
            listaContactos = xDAO.listAllEntities();
        } else {
            c.setId(Integer.parseInt(txtIDCliente.getText()));
            z.setArmazem(c);
            listaContactos = xDAO.searchEntityByCLiente(z);
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        websitetabelaContacto.setCellValueFactory(new PropertyValueFactory<>("website"));

        //add cell of button edit 
        Callback<TableColumn<ContactoArmazem, String>, TableCell<ContactoArmazem, String>> cellFoctory = (TableColumn<ContactoArmazem, String> param) -> {
            // make cell containing buttons
            final TableCell<ContactoArmazem, String> cell = new TableCell<ContactoArmazem, String>() {
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
                            ContactoArmazem x = tabelaContacto.getSelectionModel().getSelectedItem();
                            System.out.println(x.getId());
                            try {
                                ContactoArmazemDAO xDAO = new ContactoArmazemDAO();
                                xDAO.deleteEntity(x);
                                showContactos();
                            } catch (SQLException ex) {
                                Logger.getLogger(Armazem.class.getName()).log(Level.SEVERE, null, ex);
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
        Armazem x = new Armazem();
        x.setTipo(combTipoCliente.getValue());
        x.setNome(txtNome.getText());
        x.setDescricao(txtDescricao.getText());
        ArmazemDAO xDAO = new ArmazemDAO();
        xDAO.addEntity(x);
        showCliente();
    }

    public void addContacto() throws SQLException {
        ContactoArmazem x = new ContactoArmazem();
        x.setContacto(txtContacto.getText());
        x.setEmail(txtEmail.getText());
        x.setResponsavel(txtResponsavel.getText());
        x.setWebsite(txtSite.getText());
        Armazem c = new Armazem();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setArmazem(c);
        x.setUsuario(userData);
        ContactoArmazemDAO xDAO = new ContactoArmazemDAO();
        xDAO.addEntity(x);
        showContactos();
        restContacto();
    }

    public void addEndereco() {
        Alert alert;
        EnderecoArmazem x = new EnderecoArmazem();
        x.setAvenida(txtAvenida.getText());
        x.setBairro(txtBairro.getText());
        x.setCidade(txtCidade.getText());
        Armazem c = new Armazem();
        c.setId(Integer.parseInt(txtIDCliente.getText()));
        x.setArmazem(c);
        x.setCodigoPostal(Integer.parseInt(txtCodigooPostal.getText()));
        x.setNumero(Integer.parseInt(txtNumeroEndereco.getText()));
        x.setProvincia(combProvincia.getValue());
        x.setRua(txtRua.getText());
        x.setTipo(combTipoEndereco.getValue());
        x.setUsuario(userData);
        try {
            EnderecoArmazemDAO xDAO = new EnderecoArmazemDAO();
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
        Armazem x = new Armazem();
        x.setTipo(combTipoCliente.getValue());
        x.setNome(txtNome.getText());
        x.setDescricao(txtDescricao.getText());
        ArmazemDAO xDAO;
        try {
            xDAO = new ArmazemDAO();
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
        ContactoArmazem x = new ContactoArmazem();
        x.setContacto(txtContacto.getText());
        x.setEmail(txtEmail.getText());
        x.setResponsavel(txtResponsavel.getText());
        x.setWebsite(txtSite.getText());
        x.setUsuario(userData);
        ContactoArmazemDAO xDAO = new ContactoArmazemDAO();
        xDAO.updateEntityByID(x, Integer.parseInt(txtIDContacto.getText()));
        showContactos();
        restContacto();
    }

    public void updateEndereco() {
        Alert alert;
        EnderecoArmazem x = new EnderecoArmazem();
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
            EnderecoArmazemDAO xDAO = new EnderecoArmazemDAO();
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
        ContactoArmazem x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel());
            txtContacto.setText(x.getContacto());
            txtEmail.setText(x.getEmail());
            txtSite.setText(x.getWebsite());
            estadoBTNContacto();
        }
    }

    public void selecionaEndereco() {
        EnderecoArmazem x = tabelaEndereco.getSelectionModel().getSelectedItem();
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
            estadoBTNendereco();
        }
    }

    /*
    Funcoes de Reset Campos
     */
    public void resetCliente() throws SQLException {
        txtIDCliente.setText("");
        combTipoCliente.setPromptText("Tipo de cliente");
        txtDescricao.setText("");
        txtNome.setText("");
        estadoBTNCliente();
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
        estadoBTNContacto();
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
        estadoBTNendereco();
    }

}
