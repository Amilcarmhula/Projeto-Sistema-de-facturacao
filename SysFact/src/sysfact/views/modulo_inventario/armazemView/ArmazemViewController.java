/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_inventario.armazemView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.usuario.Usuario;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ArmazemViewController implements Initializable {

    private Usuario userData;

    @FXML
    private TableColumn<Armazem, Integer> IDtabelaCliente;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Armazem, String> btntabelaCliente;

    @FXML
    private TextField txtIDCliente;

    @FXML
    private TableColumn<Armazem, String> contactotabelaCliente;


    @FXML
    private TableColumn<Armazem, String> descricaotabelaCliente;

    @FXML
    private TableColumn<Armazem, String> nometabelaCliente;
    
    @FXML
    private TableColumn<Armazem, Boolean> estadotabelaCliente;
    

    @FXML
    private TableView<Armazem> tabelaCliente;

    @FXML
    private TableColumn<Armazem, String> tipotabelaCliente;

    @FXML
    private TextField txtPesquisa;
    
    public void getViewAddCliente(){
        try {
            Parent parente = FXMLLoader.load(getClass().getResource("../armazemView/armazemAdd/addArmazem.fxml"));
            Scene scene = new Scene(parente);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ArmazemViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            showClientes();
        } catch (SQLException ex) {
            Logger.getLogger(Armazem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ----------------------- Clientes e Fornecedores -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Armazem> listaClientes;

    public void showClientes() throws SQLException {

        ArmazemDAO aDAO = new ArmazemDAO();
        listaClientes = aDAO.listAllEntities();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        estadotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("estado"));
        contactotabelaCliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().printContatos())));

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
                            Armazem x = tabelaCliente.getSelectionModel().getSelectedItem();
                            try {
                                ArmazemDAO xDAO = new ArmazemDAO();
                                xDAO.deleteEntity(x.getId());
                                showClientes();

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
        btntabelaCliente.setCellFactory(cellFoctory);

        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaClientes, b -> true);
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getTipo().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Armazem> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

        tabelaCliente.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCliente() throws SQLException {
        Armazem c = tabelaCliente.getSelectionModel().getSelectedItem();
        if (c != null) {
            SysFact.setData(String.valueOf(c.getId()));
            txtIDCliente.setText(String.valueOf(c.getId()));
            btnAdd.setText("Actualizar");
        }

    }
}