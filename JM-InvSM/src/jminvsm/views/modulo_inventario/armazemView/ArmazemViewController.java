/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.armazemView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Callback;
import jminvsm.SysFact;
import jminvsm.dao.armazem.ArmazemDAO;
import jminvsm.dao.armazem.contacto.ContactoArmazemDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.armazem.contacto.ServiceContacto;
import jminvsm.util.AlertUtilities;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

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
    
    public void getViewAddCliente(ActionEvent evt){
        LoadAndMoveUtilities.showAsPopUP(evt, null);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "../views/modulo_inventario/armazemView/armazemAdd/addArmazem.fxml");
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
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome_arm"));
        descricaotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("descricao_arm"));
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
                        Image image = new Image("/jminvsm/image/icons/close_17879231.png");
                        ImageView deleteIcon = new ImageView(image);
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Armazem x = tabelaCliente.getSelectionModel().getSelectedItem();
                            try {
                                ServiceArmazem service = new ServiceArmazem();
                                service.excluir(x.getId());
                                if (service.isOpsSuccess()) {
                                    showClientes();
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
        btntabelaCliente.setCellFactory(cellFoctory);

        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaClientes, b -> true);
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome_arm().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getDescricao_arm().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getTipo().toLowerCase().contains(filtroCaixaBaixa)) {
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
            ButtonUtilities.buttonChangeText(btnAdd, txtIDCliente);
        }

    }
}