/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.armazem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.categoria.Categoria;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ArmazemController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdade;

    @FXML
    private TableView<Armazem> tabelaArmazem;

    @FXML
    private TableColumn<Armazem, Integer> IDtabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> avenidatabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> bairrotabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> btntabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> cidadetabelaArmazem;

    @FXML
    private TableColumn<Armazem, Integer> codigotabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> nometabelaArmazem;

    @FXML
    private TableColumn<Armazem, Integer> numerotabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> ruatabelaArmazem;

    @FXML
    private TextField txtAvenida;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtCodigopostal;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtRua;

    @FXML
    private TextField txtpesquisar;

    public void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showArmazem();
        } catch (SQLException ex) {
            Logger.getLogger(ArmazemController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addArmazem() throws SQLException {
        Armazem x = new Armazem();
        x.setNome(txtNome.getText());
        x.setCidade(txtCidade.getText());
        x.setBairro(txtBairro.getText());
        x.setAvenida(txtAvenida.getText());
        x.setRua(txtRua.getText());
        x.setNumero(Integer.parseInt(txtNumero.getText()));
        x.setCodigoPostal(Integer.parseInt(txtCodigopostal.getText()));
        ArmazemDAO z = new ArmazemDAO();
        z.addEntity(x);
        showArmazem();
        resetCampos();

    }

    public void updateArmazem() throws SQLException {
        Armazem x = new Armazem();
        x.setId(Integer.parseInt(txtID.getText()));
        x.setNome(txtNome.getText());
        x.setCidade(txtCidade.getText());
        x.setBairro(txtBairro.getText());
        x.setAvenida(txtAvenida.getText());
        x.setRua(txtRua.getText());
        x.setNumero(Integer.parseInt(txtNumero.getText()));
        x.setCodigoPostal(Integer.parseInt(txtCodigopostal.getText()));
        ArmazemDAO z = new ArmazemDAO();
        z.updateEntityByID(x);
        showArmazem();
        resetCampos();
    }

    public void resetCampos() {
        txtID.setText("");
        txtNome.setText("");
        txtCidade.setText("");
        txtBairro.setText("");
        txtAvenida.setText("");
        txtRua.setText("");
        txtNumero.setText("");
        txtCodigopostal.setText("");
    }

    public void selectedRow() {
        Armazem x = tabelaArmazem.getSelectionModel().getSelectedItem();

        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtNome.setText(x.getNome());
            txtCidade.setText(x.getCidade());
            txtBairro.setText(x.getBairro());
            txtAvenida.setText(x.getAvenida());
            txtRua.setText(x.getRua());
            txtNumero.setText(String.valueOf(x.getNumero()));
            txtCodigopostal.setText(String.valueOf(x.getCodigoPostal()));
        } else {
            resetCampos();
        }
    }
    private ObservableList<Armazem> listaAmazens;

    public void showArmazem() throws SQLException {

        ArmazemDAO xDAO = new ArmazemDAO();
        listaAmazens = xDAO.listAllEntities();

        IDtabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("id"));
        nometabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cidadetabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        bairrotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        avenidatabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("avenida"));
        ruatabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("rua"));
        numerotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("numero"));
        codigotabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));

        //add cell of button edit 
        Callback<TableColumn<Armazem, String>, TableCell<Armazem, String>> cellFoctory = (TableColumn<Armazem, String> param) -> {
            // make cell containing buttons
            final TableCell<Armazem, String> cell = new TableCell<Armazem, String>() {
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
                            try {
                                Armazem x = tabelaArmazem.getSelectionModel().getSelectedItem();
                                
                                ArmazemDAO xdao = new ArmazemDAO();
                                xdao.deleteEntityByUSER(x);
                                showArmazem();
                                resetCampos();
                            } catch (SQLException ex) {
                                Logger.getLogger(ArmazemController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaArmazem.setCellFactory(cellFoctory);

        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaAmazens, b -> true);
        txtpesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getBairro().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getNumero()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getRua().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getCidade().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Armazem> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaArmazem.comparatorProperty());
        tabelaArmazem.setItems(sortedData);
    }

}
