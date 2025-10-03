/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.unidadeView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.service.unidadeMedida.ServiceUnidadeMedida;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class UnidadeViewController implements Initializable {

    private ServiceUnidadeMedida serviceUnidade;

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
        SysFact.setData("");
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SysFact.setData("");
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
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
            serviceUnidade = new ServiceUnidadeMedida();
            showUnidades();
        } catch (SQLException ex) {
            Logger.getLogger(UnidadeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList<UnidadeMedida> listaUnidade;

    public void showUnidades() {
        listaUnidade = serviceUnidade.getUnidades();

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        unidade.setCellValueFactory(new PropertyValueFactory<>("nome"));
        sigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        FilteredList<UnidadeMedida> dadosFiltrados = new FilteredList<>(listaUnidade, b -> true);
        txtUnidade.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
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
        serviceUnidade.regista(txtUnidade.getText(), txtSigla.getText(), txtDescricao.getText());
        if (serviceUnidade.isOpsSuccess()) {
            resetCampos();
            showUnidades();
        }

    }

    public void updateUnidade() {
        int id = Integer.parseInt(txtID.getText().equals("") ? "0" : txtID.getText());
        serviceUnidade.actualizar(id, txtUnidade.getText(), txtSigla.getText(), txtDescricao.getText());
        if (serviceUnidade.isOpsSuccess()) {
            resetCampos();
            showUnidades();
        }
    }

    public void linhaSelecionadaUnidade(MouseEvent event) throws SQLException {
        UnidadeMedida x = tabelaUnidade.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtUnidade.setText(x.getNome());
            txtSigla.setText(x.getSigla());
            txtDescricao.setText(x.getDescricao());
            ButtonUtilities.buttonChangeText(btnAdd, txtID);
            deleteItem(event, x);
            SysFact.setData(x);
        }
    }

    public void deleteItem(MouseEvent event, UnidadeMedida x) throws SQLException {
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
                serviceUnidade.excluir(x.getId());
                if (serviceUnidade.isOpsSuccess()) {
                    showUnidades();
                    resetCampos();
                }
            } else if (result.get() == btnCancel) {
                resetCampos();
            }
        }
    }

    public void resetCampos() {
        txtID.setText("");
        txtDescricao.setText("");
        txtSigla.setText("");
        txtUnidade.setText("");
        ButtonUtilities.buttonChangeText(btnAdd, txtID);
        showUnidades();

    }

}
