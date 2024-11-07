/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_configs.taxasView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import jminvsm.dao.taxaimposto.TaxaImpostoDAO;
import jminvsm.model.taxa_imposto.TaxaImposto;
import jminvsm.service.taxaimposto.ServiceTaxaImpostos;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class TaxaViewController implements Initializable {

    private ServiceTaxaImpostos servicoTaxa;
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
        if (LoadAndMoveUtilities.returnToStage()) {
            SysFact.setData("");
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
            servicoTaxa = new ServiceTaxaImpostos();
            showTaxas();
        } catch (SQLException ex) {
            Logger.getLogger(TaxaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<TaxaImposto> listaTaxas;

    public void showTaxas() {
//        listaTaxas = servicoTaxa.getTaxas();
        List<TaxaImposto> lista = new ArrayList<>();
        for (TaxaImposto t : servicoTaxa.getTaxas()) {
            t.setId(t.getId());
            t.setDescricao(t.getDescricao());
            t.setNomeImposto(t.getNomeImposto());
            t.setPercentagemImposto(t.getPercentagemImposto()*100);
            lista.add(t);
        }
        listaTaxas = FXCollections.observableArrayList(lista);

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoTaxa.setCellValueFactory(new PropertyValueFactory<>("nomeImposto"));
        percentagem.setCellValueFactory(new PropertyValueFactory<>("percentagemImposto"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        FilteredList<TaxaImposto> dadosFiltrados = new FilteredList<>(listaTaxas, b -> true);
        txtTaxa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNomeImposto().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
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
        double taxa = Double.parseDouble(txtPercentagem.getText().equals("") ? "0" : txtPercentagem.getText());
        servicoTaxa.regista(txtTaxa.getText(), taxa, txtDescricao.getText());
        if (servicoTaxa.isOpsSuccess()) {
            resetCampos();
            showTaxas();
        }
    }

    public void updateTaxa() {
        int id = Integer.parseInt(txtID.getText().equals("") ? "0" : txtID.getText());
        double taxa = Double.parseDouble(txtPercentagem.getText().equals("") ? "0" : txtPercentagem.getText());
        servicoTaxa.actualizar(id, txtTaxa.getText(), taxa, txtDescricao.getText());
        if (servicoTaxa.isOpsSuccess()) {
            resetCampos();
            showTaxas();
        }
    }

    public void linhaSelecionadaTaxa(MouseEvent event) throws SQLException {
        TaxaImposto x = tabelaTaxa.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtDescricao.setText(x.getDescricao());
            txtPercentagem.setText(String.valueOf(x.getPercentagemImposto()));
            txtTaxa.setText(x.getNomeImposto());
            ButtonUtilities.buttonChangeText(btnAdd, txtID);
            deleteItem(event, x);
            SysFact.setData(x);
        }
    }

    public void deleteItem(MouseEvent event, TaxaImposto x) {
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
                servicoTaxa.excluir(x.getId());
                if (servicoTaxa.isOpsSuccess()) {
                    showTaxas();
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
        txtPercentagem.setText("");
        txtTaxa.setText("");
        ButtonUtilities.buttonChangeText(btnAdd, txtID);
        showTaxas();

    }

}
