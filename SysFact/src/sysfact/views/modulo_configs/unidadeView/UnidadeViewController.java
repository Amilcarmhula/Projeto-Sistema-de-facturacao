/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_configs.unidadeView;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.dao.unidademedida.UnidadeMedidaDAO;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.views.modulo_venda.vendaView.searchProduto.ProdutoSearchController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class UnidadeViewController implements Initializable {

    @FXML
    private TableColumn<UnidadeMedida, Integer> ID;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<UnidadeMedida, String> btnTabela;

    @FXML
    private TableColumn<UnidadeMedida, String> descricao;

    @FXML
    private TableColumn<UnidadeMedida, String> sigla;

    @FXML
    private TableView<UnidadeMedida> tabelaUnidade;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtSigla;

    @FXML
    private TextField txtUnidade;

    @FXML
    private TableColumn<UnidadeMedida, String> unidade;

    public void close(ActionEvent event) {
        if (ProdutoSearchController.returnToMain()) {
            ProdutoSearchController.setX(false);
            ProdutoSearchController.setMainStage(null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            
        } else {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void estadoBTN() {
        if (!"".equals(txtID.getText())) {
            btnAdd.setText("Actualizar");
        } else {
            btnAdd.setText("Adicionar");
        }
    }

    public void addORupdateTaxa() throws SQLException {
        if ("".equals(txtID.getText())) {
            addUnidade();
        } else {
            updateUnidade();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showUnidades();
        } catch (SQLException ex) {
            Logger.getLogger(UnidadeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList<UnidadeMedida> listaUnidade;

    public void showUnidades() throws SQLException {
        UnidadeMedidaDAO xDAO = new UnidadeMedidaDAO();
        listaUnidade = xDAO.listAllEntities();

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        unidade.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        sigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        //add cell of button edit 
        Callback<TableColumn<UnidadeMedida, String>, TableCell<UnidadeMedida, String>> cellFoctory = (TableColumn<UnidadeMedida, String> param) -> {
            // make cell containing buttons
            final TableCell<UnidadeMedida, String> cell = new TableCell<UnidadeMedida, String>() {
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
                            UnidadeMedida a = tabelaUnidade.getSelectionModel().getSelectedItem();
                            try {
                                UnidadeMedidaDAO xDAO = new UnidadeMedidaDAO();
                                xDAO.deleteEntity(a.getId());
                                resetCampos();
                                showUnidades();
                            } catch (SQLException ex) {
                                Logger.getLogger(UnidadeViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btnTabela.setCellFactory(cellFoctory);

        FilteredList<UnidadeMedida> dadosFiltrados = new FilteredList<>(listaUnidade, b -> true);
        txtUnidade.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getIdentificador().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getSigla().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<UnidadeMedida> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaUnidade.comparatorProperty());

        tabelaUnidade.setItems(sortedData);
    }

    public void addUnidade() {
        UnidadeMedida x = new UnidadeMedida();
        x.setDescricao(txtDescricao.getText());
        x.setIdentificador(txtUnidade.getText());
        x.setSigla(txtSigla.getText());
        try {
            UnidadeMedidaDAO xDAO = new UnidadeMedidaDAO();
            xDAO.addEntity(x);
            resetCampos();
            showUnidades();
        } catch (SQLException ex) {
            Logger.getLogger(UnidadeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateUnidade() {
        UnidadeMedida x = new UnidadeMedida();
        x.setDescricao(txtDescricao.getText());
        x.setIdentificador(txtUnidade.getText());
        x.setSigla(txtSigla.getText());
        try {
            UnidadeMedidaDAO xDAO = new UnidadeMedidaDAO();
            xDAO.updateEntityByID(x, Integer.parseInt(txtID.getText()));
            resetCampos();
            showUnidades();
        } catch (SQLException ex) {
            Logger.getLogger(UnidadeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void linhaSelecionadaUnidade() {
        UnidadeMedida x = tabelaUnidade.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtUnidade.setText(x.getIdentificador());
            txtSigla.setText(x.getSigla());
            txtDescricao.setText(x.getDescricao());
            estadoBTN();
            SysFact.setData(x);
        }
    }

    public void resetCampos() throws SQLException {
        txtID.setText("");
        txtDescricao.setText("");
        txtSigla.setText("");
        txtUnidade.setText("");
        estadoBTN();
        showUnidades();

    }

}
