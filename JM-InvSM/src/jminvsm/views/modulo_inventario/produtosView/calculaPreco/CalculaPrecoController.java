/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.produtosView.calculaPreco;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.imposto.Imposto;
import jminvsm.service.taxaimposto.ServiceTaxaImpostos;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class CalculaPrecoController implements Initializable {
    private ServiceTaxaImpostos serTaxa;

    @FXML
    private TableView<Imposto> tabelaTaxa;
    @FXML
    private TableColumn<Imposto, Integer> ID;
    @FXML
    private TableColumn<Imposto, String> tipoTaxa;
    @FXML
    private TableColumn<Imposto, Double> percentagem;
    @FXML
    private Button btnAdd;
    @FXML
    private TableColumn<Imposto, String> btnTabela;

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtPesquisaTaxa;
    @FXML
    private TextField txtPreco;
    @FXML
    private Label labPrecoSistema;

    @FXML
    private Button btnClose;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            serTaxa = new ServiceTaxaImpostos();
            showTaxas();
        } catch (SQLException ex) {
            Logger.getLogger(CalculaPrecoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<Imposto> listaTaxas;

    public void showTaxas(){
        listaTaxas = serTaxa.getTaxas();

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoTaxa.setCellValueFactory(new PropertyValueFactory<>("nomeImposto"));
        percentagem.setCellValueFactory(new PropertyValueFactory<>("percentagemImposto"));

        FilteredList<Imposto> dadosFiltrados = new FilteredList<>(listaTaxas, b -> true);
        txtPesquisaTaxa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Imposto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaTaxa.comparatorProperty());

        tabelaTaxa.setItems(sortedData);
    }

    private double percentagemImposto = 0;

    public void linhaSelecionadaTaxa() {
        Imposto x = tabelaTaxa.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            percentagemImposto = x.getPercentagem();
            SysFact.setData(x);
        }
    }

    public void calculaPreco(ActionEvent event) {
        double precoVenda = 0;
        double preco = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        precoVenda = preco - preco * percentagemImposto;
        SysFact.setData(precoVenda);
        labPrecoSistema.setText(precoVenda + " Mt");
        if (precoVenda != 0) {
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
    }
}
