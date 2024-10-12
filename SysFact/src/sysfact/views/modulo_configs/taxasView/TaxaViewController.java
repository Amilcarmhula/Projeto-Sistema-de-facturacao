/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_configs.taxasView;

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
import sysfact.dao.taxaimposto.TaxaImpostoDAO;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.util.MetodosUteis;
import sysfact.views.modulo_venda.vendaView.searchProduto.ProdutoSearchController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class TaxaViewController implements Initializable {

    @FXML
    private TableColumn<TaxaImposto, Integer> ID;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<TaxaImposto, String> btnTabela;

    @FXML
    private TableColumn<TaxaImposto, String> descricao;

    @FXML
    private TableColumn<TaxaImposto, Double> percentagem;

    @FXML
    private TableView<TaxaImposto> tabelaTaxa;

    @FXML
    private TableColumn<TaxaImposto, String> tipoTaxa;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtPercentagem;

    @FXML
    private TextField txtTaxa;

    @FXML
    private Button btnClose;

    public void close(ActionEvent event) {
        if (ProdutoSearchController.returnToStage()) {
            ProdutoSearchController.setEstadoStage(false);
            ProdutoSearchController.showStage(null,null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
        else if (MetodosUteis.returnToBaseAnchor()) {
            MetodosUteis.setEstadoPopUP(false);
            MetodosUteis.showAsPopUP(null, null);
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
            addTaxa();
        } else {
            updateTaxa();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showTaxas();
        } catch (SQLException ex) {
            Logger.getLogger(TaxaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<TaxaImposto> listaTaxas;

    public void showTaxas() throws SQLException {
        TaxaImpostoDAO xDAO = new TaxaImpostoDAO();
        listaTaxas = xDAO.listAllEntities();

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoTaxa.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        percentagem.setCellValueFactory(new PropertyValueFactory<>("percentagem"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        //add cell of button edit 
        Callback<TableColumn<TaxaImposto, String>, TableCell<TaxaImposto, String>> cellFoctory = (TableColumn<TaxaImposto, String> param) -> {
            // make cell containing buttons
            final TableCell<TaxaImposto, String> cell = new TableCell<TaxaImposto, String>() {
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
                            TaxaImposto a = tabelaTaxa.getSelectionModel().getSelectedItem();
                            try {
                                TaxaImpostoDAO xDAO = new TaxaImpostoDAO();
                                xDAO.deleteEntity(a.getId());
                                resetCampos();
                                showTaxas();
                            } catch (SQLException ex) {
                                Logger.getLogger(TaxaViewController.class.getName()).log(Level.SEVERE, null, ex);
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

        FilteredList<TaxaImposto> dadosFiltrados = new FilteredList<>(listaTaxas, b -> true);
        txtTaxa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getIdentificador().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<TaxaImposto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaTaxa.comparatorProperty());

        tabelaTaxa.setItems(sortedData);
    }

    public void addTaxa() {
        TaxaImposto x = new TaxaImposto();
        x.setDescricao(txtDescricao.getText());
        x.setIdentificador(txtTaxa.getText());
        x.setPercentagem(Double.valueOf(txtPercentagem.getText())/100);
        try {
            TaxaImpostoDAO xDAO = new TaxaImpostoDAO();
            xDAO.addEntity(x);
            resetCampos();
            showTaxas();
        } catch (SQLException ex) {
            Logger.getLogger(TaxaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateTaxa() {
        TaxaImposto x = new TaxaImposto();
        x.setDescricao(txtDescricao.getText());
        x.setIdentificador(txtTaxa.getText());
        x.setPercentagem(Double.valueOf(txtPercentagem.getText())/100);
        try {
            TaxaImpostoDAO xDAO = new TaxaImpostoDAO();
            xDAO.updateEntityByID(x, Integer.parseInt(txtID.getText()));
            resetCampos();
            showTaxas();
        } catch (SQLException ex) {
            Logger.getLogger(TaxaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void linhaSelecionadaTaxa() {
        TaxaImposto x = tabelaTaxa.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtDescricao.setText(x.getDescricao());
            txtPercentagem.setText(String.valueOf(x.getPercentagem()*100));
            txtTaxa.setText(x.getIdentificador());
            estadoBTN();
            SysFact.setData(x);
        }
    }

    public void resetCampos() throws SQLException {
        txtID.setText("");
        txtDescricao.setText("");
        txtPercentagem.setText("");
        txtTaxa.setText("");
        estadoBTN();
        showTaxas();
        
    }

}
