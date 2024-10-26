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
import sysfact.SysFact;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.dao.armazem.contacto.ContactoArmazemDAO;
import sysfact.dao.armazem.endereco.EnderecoArmazemDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.armazem.contacto.ContactoArmazem;
import sysfact.model.armazem.endereco.EnderecoArmazem;
import sysfact.model.usuario.Usuario;
import sysfact.service.armazem.ServiceArmazem;
import sysfact.service.armazem.contacto.ServiceContacto;
import sysfact.service.armazem.endereco.ServiceEndereco;
import sysfact.util.AlertUtilities;
import sysfact.util.ButtonUtilities;
import sysfact.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddArmazemController implements Initializable {

    private Usuario userData;
    private ServiceArmazem serviceArmazem;
    private ServiceContacto serviceContacto;
    private ServiceEndereco serviceEndereco;

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
            addArmazem();
        } else {
            updateArmazem();
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
            serviceArmazem = new ServiceArmazem();
            serviceContacto = new ServiceContacto();
            serviceEndereco = new ServiceEndereco();
            showContactos();
            showClienteByID();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddArmazemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddArmazem, txtIDCliente);
        ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
    }

//    

    /*
    Funcoes de Consultas
     */
    public void showLastStoreAdded() throws SQLException {
        Armazem c = serviceArmazem.getUltimoArmazem();
        txtIDCliente.setText(String.valueOf(c.getId()));
        combTipoCliente.setValue(c.getTipo());
        txtNome.setText(c.getNome_arm());
        txtDescricao.setText(c.getDescricao_arm());
        showContactos();
    }

    public void showClienteByID() throws SQLException {
        if (!"".equals(txtIDCliente.getText())) {
            Armazem c = serviceArmazem.consultaArmazem(Integer.parseInt(txtIDCliente.getText()));
            combTipoCliente.setValue(c.getTipo());
            txtNome.setText(c.getNome_arm());
            txtDescricao.setText(c.getDescricao_arm());
            showContactos();
        }

    }

    private ObservableList<EnderecoArmazem> listaEndereco;

    public void showEndereco() throws SQLException {
        if ("".equals(txtIDCliente.getText())) {
            listaEndereco = serviceEndereco.listaEnderecos();
        } else {
            listaEndereco = serviceEndereco.consultaEnderecosPorArmazem(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia_arm"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade_arm"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro_arm"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida_arm"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua_arm"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal_arm"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero_arm"));
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
                                + "-fx-fill:#000000b3;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            EnderecoArmazem a = tabelaEndereco.getSelectionModel().getSelectedItem();
                            try {
                                ServiceEndereco service = new ServiceEndereco();
                                service.excluir(a.getId());
                                if (service.isOpsSuccess()) {
                                    showEndereco();
                                }
                            } catch (SQLException ex) {
                                AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro no processo de exclusao", "Detalhes: "+ex);
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
        if ("".equals(txtIDCliente.getText())) {
            listaContactos = serviceContacto.listaContactos();
        } else {
            listaContactos = serviceContacto.consultaContactoPorArmazem(Integer.parseInt(txtIDCliente.getText()));
        }

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto_arm"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email_arm"));
        responsaveltabelaContacto.setCellValueFactory(new PropertyValueFactory<>("responsavel_arm"));

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
                                + "-fx-fill:#000000b3;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContactoArmazem x = tabelaContacto.getSelectionModel().getSelectedItem();
                            try {
                                ServiceContacto service = new ServiceContacto();
                                service.excluir(x.getId());
                                if (service.isOpsSuccess()) {
                                    showContactos();
                                }
                            } catch (SQLException ex) {
                                AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro no processo de exclusao", "Detalhes: "+ex);
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
    public void addArmazem() throws SQLException {
        ServiceArmazem servicee = new ServiceArmazem();
        servicee.registar(combTipoCliente.getValue(), txtNome.getText(), txtDescricao.getText());
        if (servicee.isOpsSuccess()) {
            showLastStoreAdded();
            servicee.setOpsSuccess(false);
        }

    }

    public void addContacto() throws SQLException {
        ServiceContacto service = new ServiceContacto();
        Integer id = Integer.valueOf(txtIDCliente.getText());
        service.registar(txtResponsavel.getText(), txtContacto.getText(), txtEmail.getText(), id, userData);
        if (service.isOpsSuccess()) {
            showContactos();
            restContacto();
        }
    }

    public void addEndereco() throws SQLException {
        ServiceEndereco service = new ServiceEndereco();
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        Integer id = Integer.valueOf(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        service.registar(txtAvenida.getText(), txtBairro.getText(), 
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(),id, userData);
        if (service.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }

    /*
    Funcoes para Actualizar registros
     */
    public void updateArmazem() throws SQLException {
        ServiceArmazem servicee = new ServiceArmazem();
        Integer id = Integer.valueOf(txtIDCliente.getText());
        servicee.actualizar(id, combTipoCliente.getValue(), txtNome.getText(), txtDescricao.getText());

        if (servicee.isOpsSuccess()) {
            showContactos();
            servicee.setOpsSuccess(false);
        }
    }

    public void updateContacto() throws SQLException {
        ServiceContacto service = new ServiceContacto();
        Integer id = Integer.valueOf(txtIDContacto.getText());
        service.actualizar(txtResponsavel.getText(), txtContacto.getText(), txtEmail.getText(), id, userData);
        if (service.isOpsSuccess()) {
            showContactos();
            restContacto();
        }
    }

    public void updateEndereco() throws SQLException {
        ServiceEndereco service = new ServiceEndereco();
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        Integer id = Integer.valueOf(txtIDEndereco.getText().equals("") ? "0" : txtIDEndereco.getText());
        service.actualizar(txtAvenida.getText(), txtBairro.getText(), 
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(),id, userData);
        if (service.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
        
    }

    /*
    Funcoes de Linha selecionada
     */
    public void selecionaContacto() {
        ContactoArmazem x = tabelaContacto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDContacto.setText(String.valueOf(x.getId()));
            txtResponsavel.setText(x.getResponsavel_arm());
            txtContacto.setText(x.getContacto_arm());
            txtEmail.setText(x.getEmail_arm());
            ButtonUtilities.buttonChangeText(btnAddContacto, txtIDContacto);
        }
    }

    public void selecionaEndereco() {
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
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
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
        combProvincia.setPromptText("Provincia");
        txtRua.setText("");
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
    }

}
