/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sysfact.dao.agente.AgenteDAO;
import sysfact.dao.agente.contacto.ContactoAgenteDAO;
import sysfact.dao.agente.endereco.EnderecoAgenteDAO;
import sysfact.model.agente.Agente;
import sysfact.model.contacto.agente.ContactoAgente;
import sysfact.model.endereco.agente.EnderecoAgente;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class DashboardController implements Initializable {

    private int idUser;

    @FXML
    private TableColumn<Agente, Integer> IDtabelaCliente;

    @FXML
    private TableColumn<ContactoAgente, Integer> IDtabelaContacto;

    @FXML
    private TableColumn<ContactoAgente, String> datatabelaContacto;

    @FXML
    private TableColumn<EnderecoAgente, Integer> IDtabelaEndereco;

    @FXML
    private AnchorPane anchorClientes;

    @FXML
    private AnchorPane anchorDashboar;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private AnchorPane anchorProdutos;

    @FXML
    private AnchorPane anchorVendas;

    @FXML
    private AnchorPane anchorFacturas;

    @FXML
    private AnchorPane anchorConfig;

    @FXML
    private TableColumn<Agente, String> apelidotabelaCliente;

    @FXML
    private TableColumn<EnderecoAgente, String> avenidatabelaEndereco;

    @FXML
    private TableColumn<EnderecoAgente, String> bairrotabelaEndereco;

    @FXML
    private TableColumn<Agente, String> btntabelaCliente;

    @FXML
    private TableColumn<ContactoAgente, String> btntabelaContacto;

    @FXML
    private TableColumn<EnderecoAgente, String> btntabelaEndereco;
    @FXML
    private Button btn_cliente;

    @FXML
    private Button btn_config;

    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_facturas;

    @FXML
    private Button btn_produtos;

    @FXML
    private Button btn_vendas;

    @FXML
    private Button cf_btnAddCliente;

    @FXML
    private Button cf_btnAddContacto;

    @FXML
    private Button cf_btnUpdateCliente;

    @FXML
    private Button cf_btnUpdateContacto;

    @FXML
    private Button cf_btnAddEndereco;

    @FXML
    private Button cf_btnUpdateEndereco;

    @FXML
    private ComboBox<String> cf_combProvincia;

    @FXML
    private ComboBox<String> cf_combTipo;

    @FXML
    private RadioButton cf_radioCliente;

    @FXML
    private RadioButton cf_radioFornecedor;

    @FXML
    private TextField cf_txtApelido;

    @FXML
    private TextField cf_txtAvenida;

    @FXML
    private TextField cf_txtBairro;

    @FXML
    private TextField cf_txtCidade;

    @FXML
    private TextField cf_txtCodigooPostal;

    @FXML
    private TextField cf_txtContacto;

    @FXML
    private TextField cf_txtEmail;

    @FXML
    private TextField cf_txtIDCliente;
    
    @FXML
    private TextField cf_txtIDContacto;

    @FXML
    private TextField cf_txtIDEndereco;

    @FXML
    private TextField cf_txtNome;

    @FXML
    private TextField cf_txtNuit;

    @FXML
    private TextField cf_txtNumeroEndereco;

    @FXML
    private TextField cf_txtPesquisaCliente;

    @FXML
    private TextField cf_txtPesquisaEndereco;

    @FXML
    private TextField cf_txtRua;

    @FXML
    private TableColumn<EnderecoAgente, String> cidadetabelaEndereco;

    @FXML
    private TableColumn<EnderecoAgente, Integer> codigopostaltabelaEndereco;

    @FXML
    private TableColumn<ContactoAgente, String> contactotabelaContacto;

    @FXML
    private TableColumn<Agente, String> datatabelaCliente;

    @FXML
    private TableColumn<EnderecoAgente, String> datatabelaEndereco;

    @FXML
    private TableColumn<ContactoAgente, String> emailtabelaContacto;

    @FXML
    private Label labID;

    @FXML
    private TableColumn<Agente, String> nometabelaCliente;
    @FXML
    private TableColumn<Agente, String> tipotabelaCliente;

    @FXML
    private TableColumn<Agente, Integer> nuittabelaCliente;

    @FXML
    private TableColumn<EnderecoAgente, Integer> numerotabelaEndereco;

    @FXML
    private TableColumn<EnderecoAgente, String> provinciatabelaEndereco;

    @FXML
    private TableColumn<EnderecoAgente, String> ruatabelaEndereco;

    @FXML
    private TableView<Agente> tabelaCliente;

    @FXML
    private TableView<ContactoAgente> tabelaContacto;

    @FXML
    private TableView<EnderecoAgente> tabelaEndereco;

    @FXML
    private TableColumn<EnderecoAgente, String> tipotabelaEndereco;

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) anchorMain.getScene().getWindow();
        stage.setIconified(true);
    }
//    E possivel automatizar este metodo para melhorar o seu funcionamento

    public void switchForm(ActionEvent e) {
//        System.out.println("Fonte"+e.getSource());
        if (e.getSource() == btn_dashboard) {
            anchorDashboar.setVisible(true);
            anchorVendas.setVisible(false);
            anchorProdutos.setVisible(false);
            anchorClientes.setVisible(false);
            anchorFacturas.setVisible(false);
            anchorConfig.setVisible(false);

            btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_vendas) {
            anchorDashboar.setVisible(false);
            anchorVendas.setVisible(true);
            anchorProdutos.setVisible(false);
            anchorClientes.setVisible(false);
            anchorFacturas.setVisible(false);
            anchorConfig.setVisible(false);

            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_produtos) {
            anchorDashboar.setVisible(false);
            anchorVendas.setVisible(false);
            anchorProdutos.setVisible(true);
            anchorClientes.setVisible(false);
            anchorFacturas.setVisible(false);
            anchorConfig.setVisible(false);

            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        } else if (e.getSource() == btn_cliente) {
            anchorDashboar.setVisible(false);
            anchorVendas.setVisible(false);
            anchorProdutos.setVisible(false);
            anchorClientes.setVisible(true);
            anchorFacturas.setVisible(false);
            anchorConfig.setVisible(false);

            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_facturas) {
            anchorDashboar.setVisible(false);
            anchorVendas.setVisible(false);
            anchorProdutos.setVisible(false);
            anchorClientes.setVisible(false);
            anchorFacturas.setVisible(true);
            anchorConfig.setVisible(false);

            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_config) {
            anchorDashboar.setVisible(false);
            anchorVendas.setVisible(false);
            anchorProdutos.setVisible(false);
            anchorClientes.setVisible(false);
            anchorFacturas.setVisible(false);
            anchorConfig.setVisible(true);

            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        this.idUser = Integer.parseInt(labID.getText());
        cf_combTipo.setItems(FXCollections.observableArrayList("Facturação", "Envio"));
        cf_combProvincia.setItems(FXCollections.observableArrayList(
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
        try {
            showAgente();
            showContactoAgente();
            showEnderecoAgente();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodos para Manipular Agente economico*/
    public void addAgente() throws SQLException {
        AgenteDAO aDAO = new AgenteDAO();
        Agente a = new Agente();
        Alert alert;

        a.setNome(cf_txtNome.getText());
        a.setApelido(cf_txtApelido.getText());
        a.setNuit(Integer.parseInt(cf_txtNuit.getText()));
        a.setFk_id_usuario(idUser);
        if (cf_radioCliente.isSelected()) {
            cf_radioFornecedor.setSelected(false);
            a.setTipo("Cliente");
            aDAO.addEntity(a);
            resetAgentes();
            showAgente();
        } else if (cf_radioFornecedor.isSelected()) {
            cf_radioCliente.setSelected(false);
            a.setTipo("Fornecedor");
            aDAO.addEntity(a);
            resetAgentes();
            showAgente();
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Tipo de entidade nao definida!");
            alert.showAndWait();
        }
    }

    public void updateAgente() throws SQLException {
        AgenteDAO aDAO = new AgenteDAO();
        Agente a = new Agente();
        Alert alert;

        a.setNome(cf_txtNome.getText());
        a.setApelido(cf_txtApelido.getText());
        a.setNuit(Integer.parseInt(cf_txtNuit.getText()));
        a.setFk_id_usuario(Integer.parseInt(labID.getText()));
        a.setId(Integer.parseInt(cf_txtIDCliente.getText()));
        if (cf_radioCliente.isSelected()) {
            cf_radioFornecedor.setSelected(false);
            a.setTipo("Cliente");
            aDAO.updateEntityByID(a);
            resetAgentes();
            showAgente();
        } else if (cf_radioFornecedor.isSelected()) {
            cf_radioCliente.setSelected(false);
            a.setTipo("Fornecedor");
            aDAO.updateEntityByID(a);
            resetAgentes();
            showAgente();
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Tipo de entidade nao definida!");
            alert.showAndWait();
        }
    }

    private ObservableList<Agente> listaAgentes;

    public void showAgente() throws SQLException {
        AgenteDAO aDAO = new AgenteDAO();
        listaAgentes = aDAO.listAllEntities();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        apelidotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("apelido"));
        nuittabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nuit"));
        datatabelaCliente.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));

        //add cell of button edit 
        Callback<TableColumn<Agente, String>, TableCell<Agente, String>> cellFoctory = (TableColumn<Agente, String> param) -> {
            // make cell containing buttons
            final TableCell<Agente, String> cell = new TableCell<Agente, String>() {
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
                            Agente a = tabelaCliente.getSelectionModel().getSelectedItem();
                            System.out.println(a.getId());
                            /*
                            Definir as accoes do botao deletar
                             */
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaCliente.setCellFactory(cellFoctory);

        FilteredList<Agente> dadosFiltrados = new FilteredList<>(listaAgentes, b -> true);
        cf_txtPesquisaCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(agente -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (agente.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (agente.getApelido().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(agente.getNuit()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Agente> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

//        tabelaCliente.setItems(listaAgentes);
        tabelaCliente.setItems(sortedData);
    }

    public void selectedLineAgentes() throws SQLException {
        Agente a = tabelaCliente.getSelectionModel().getSelectedItem();
        cf_txtIDCliente.setText(String.valueOf(a.getId()));
        cf_txtNome.setText(a.getNome());
        cf_txtApelido.setText(a.getApelido());
        cf_txtNuit.setText(String.valueOf(a.getNuit()));
        showContactoAgente();
        showContactoAgenteSelected();
        showEnderecoAgenteSelected();
        resetContactosAgente();
        if (a.getTipo().equals("Cliente")) {
            cf_radioCliente.setSelected(true);
            cf_radioFornecedor.setSelected(false);
        } else {
            cf_radioFornecedor.setSelected(true);
            cf_radioCliente.setSelected(false);
        }

    }

    public void resetAgentes() {
        cf_txtIDCliente.setText("");
        cf_txtNome.setText("");
        cf_txtApelido.setText("");
        cf_txtNuit.setText("");
    }

    public void setSelectedradioCliente() {
        cf_radioFornecedor.setSelected(false);
        cf_radioCliente.setSelected(true);
    }

    public void setSelectedradioFornecedor() {
        cf_radioFornecedor.setSelected(true);
        cf_radioCliente.setSelected(false);
    }


    /*Metodos para Contactos de Agente economico*/
    public void resetContactosAgente() {
        cf_txtIDContacto.setText("");
        cf_txtContacto.setText("");
        cf_txtEmail.setText("");
    }
    private ObservableList<ContactoAgente> listaContactosAgente;

    public void showContactoAgente() throws SQLException {
        ContactoAgenteDAO x = new ContactoAgenteDAO();
        listaContactosAgente = x.listAllEntities();

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email"));
        datatabelaContacto.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
//        btntabelaContacto.setCellValueFactory(new PropertyValueFactory<>(new Button("X")));
//add cell of button edit 
        Callback<TableColumn<ContactoAgente, String>, TableCell<ContactoAgente, String>> cellFoctory = (TableColumn<ContactoAgente, String> param) -> {
            // make cell containing buttons
            final TableCell<ContactoAgente, String> cell = new TableCell<ContactoAgente, String>() {
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
                            /*
                            Definir as accoes do botao deletar
                             */
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaContacto.setCellFactory(cellFoctory);
        tabelaContacto.setItems(listaContactosAgente);
    }

    public void selectedLineContactos() {
        ContactoAgente x = tabelaContacto.getSelectionModel().getSelectedItem();
        cf_txtIDContacto.setText(String.valueOf(x.getId()));
        cf_txtContacto.setText(x.getContacto());
        cf_txtEmail.setText(x.getEmail());
    }

    public void addContactoAgente() throws SQLException {
        ContactoAgenteDAO xDAO = new ContactoAgenteDAO();
        ContactoAgente x = new ContactoAgente();

        x.setContacto(cf_txtContacto.getText());
        x.setEmail(cf_txtEmail.getText());
        x.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        x.setFk_id_usuario(idUser);
        xDAO.addEntity(x);
        showContactoAgenteSelected();
        resetContactosAgente();
    }

    public void updateContactoAgente() throws SQLException {
        ContactoAgenteDAO xDAO = new ContactoAgenteDAO();
        ContactoAgente x = new ContactoAgente();

        x.setContacto(cf_txtContacto.getText());
        x.setEmail(cf_txtEmail.getText());
        x.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        x.setFk_id_usuario(idUser);
        x.setId(Integer.parseInt(cf_txtIDContacto.getText()));
        xDAO.updateEntityByID(x);
        showContactoAgente();
        resetContactosAgente();
    }
    private ObservableList<ContactoAgente> listaContactos_SeletedAgente;

    public void showContactoAgenteSelected() throws SQLException {
        ContactoAgenteDAO x = new ContactoAgenteDAO();
        ContactoAgente z = new ContactoAgente();
        z.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        listaContactos_SeletedAgente = x.searchEntityByAgente(z);

        IDtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactotabelaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        emailtabelaContacto.setCellValueFactory(new PropertyValueFactory<>("email"));
        datatabelaContacto.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
//        btntabelaContacto.setCellValueFactory(new PropertyValueFactory<>(new Button("X")));

        tabelaContacto.setItems(listaContactos_SeletedAgente);
    }

    /*Metodos para Endereco de Agente economico*/
    public void resetEnderecoAgente() {
        cf_txtIDEndereco.setText("");
        cf_txtCidade.setText("");
        cf_txtCodigooPostal.setText("");
        cf_txtBairro.setText("");
        cf_txtAvenida.setText("");
        cf_txtRua.setText("");
        cf_txtNumeroEndereco.setText("");
        cf_combTipo.setValue("");
        cf_combProvincia.setValue("");
        cf_txtIDEndereco.setText("");
    }
    private ObservableList<EnderecoAgente> listaEnderecosAgente;

    public void showEnderecoAgente() throws SQLException {
        EnderecoAgenteDAO x = new EnderecoAgenteDAO();
        listaEnderecosAgente = x.listAllEntities();

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero"));
        datatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
//        btntabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
//add cell of button edit 
        Callback<TableColumn<EnderecoAgente, String>, TableCell<EnderecoAgente, String>> cellFoctory = (TableColumn<EnderecoAgente, String> param) -> {
            // make cell containing buttons
            final TableCell<EnderecoAgente, String> cell = new TableCell<EnderecoAgente, String>() {
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
                            /*
                            Definir as accoes do botao deletar
                             */
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaEndereco.setCellFactory(cellFoctory);
        tabelaEndereco.setItems(listaEnderecosAgente);
    }

    public void selectedLineEnderecos() {
        EnderecoAgente x = tabelaEndereco.getSelectionModel().getSelectedItem();
        cf_txtIDEndereco.setText(String.valueOf(x.getId()));
        cf_txtCidade.setText(x.getCidade());
        cf_txtCodigooPostal.setText(String.valueOf(x.getCodigoPostal()));
        cf_txtBairro.setText(x.getBairro());
        cf_txtAvenida.setText(x.getAvenida());
        cf_txtRua.setText(x.getRua());
        cf_txtNumeroEndereco.setText(String.valueOf(x.getNumero()));
        cf_combTipo.setValue(x.getTipo());
        cf_combProvincia.setValue(x.getProvincia());
        cf_txtIDEndereco.setText(String.valueOf(x.getId()));
    }

    public void addEnderecoAgente() throws SQLException {
        EnderecoAgenteDAO xDAO = new EnderecoAgenteDAO();
        EnderecoAgente x = new EnderecoAgente();

        x.setProvincia(cf_combProvincia.getValue());
        x.setCidade(cf_txtCidade.getText());
        x.setBairro(cf_txtBairro.getText());
        x.setAvenida(cf_txtAvenida.getText());
        x.setRua(cf_txtRua.getText());
        x.setCodigoPostal(Integer.parseInt(cf_txtCodigooPostal.getText()));
        x.setNumero(Integer.parseInt(cf_txtNumeroEndereco.getText()));
        x.setTipo(cf_combTipo.getValue());
        x.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        x.setFk_id_usuario(idUser);
        xDAO.addEntity(x);
        showEnderecoAgenteSelected();
        resetEnderecoAgente();
    }

    public void updateEnderecoAgente() throws SQLException {
        EnderecoAgenteDAO xDAO = new EnderecoAgenteDAO();
        EnderecoAgente x = new EnderecoAgente();

        x.setProvincia(cf_combProvincia.getValue());
        x.setCidade(cf_txtCidade.getText());
        x.setBairro(cf_txtBairro.getText());
        x.setAvenida(cf_txtAvenida.getText());
        x.setRua(cf_txtRua.getText());
        x.setCodigoPostal(Integer.parseInt(cf_txtCodigooPostal.getText()));
        x.setNumero(Integer.parseInt(cf_txtNumeroEndereco.getText()));
        x.setTipo(cf_combTipo.getValue());
        x.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        x.setFk_id_usuario(idUser);
        x.setId(Integer.parseInt(cf_txtIDEndereco.getText()));
        xDAO.updateEntityByID(x);
        resetEnderecoAgente();
        showEnderecoAgenteSelected();
    }

    private ObservableList<EnderecoAgente> listaEndereco_SeletedAgente;

    public void showEnderecoAgenteSelected() throws SQLException {
        EnderecoAgenteDAO x = new EnderecoAgenteDAO();
        EnderecoAgente z = new EnderecoAgente();
        z.setFk_id_agente(Integer.parseInt(cf_txtIDCliente.getText()));
        listaEndereco_SeletedAgente = x.searchEntityByAgente(z);

        IDtabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        provinciatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        cidadetabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        bairrotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        avenidatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("avenida"));
        ruatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("rua"));
        codigopostaltabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        numerotabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("numero"));
        datatabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
        //add cell of button edit 
        Callback<TableColumn<EnderecoAgente, String>, TableCell<EnderecoAgente, String>> cellFoctory = (TableColumn<EnderecoAgente, String> param) -> {
            // make cell containing buttons
            final TableCell<EnderecoAgente, String> cell = new TableCell<EnderecoAgente, String>() {
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
                            /*
                            Definir as accoes do botao deletar
                             */
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaEndereco.setCellFactory(cellFoctory);
//        btntabelaEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));

        tabelaEndereco.setItems(listaEndereco_SeletedAgente);
    }
}
