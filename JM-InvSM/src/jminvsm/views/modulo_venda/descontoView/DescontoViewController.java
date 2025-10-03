/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.descontoView;

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
import jminvsm.model.desconto.Desconto;
import jminvsm.model.imposto.Imposto;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class DescontoViewController implements Initializable {

    private ServiceDesconto servicoDesconto;
    @FXML
    private TableColumn<Imposto, Integer> ID;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Desconto, String> btnTabela;

    @FXML
    private TableColumn<Desconto, String> descricao;

    @FXML
    private TableView<Desconto> tabelaDesconto;

    @FXML
    private TableColumn<Desconto, String> nomeDesconto;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtDesconto;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            SysFact.setData(null);
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SysFact.setData(null);
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void addORupdateTaxa() throws SQLException {
        if ("".equals(txtID.getText())) {
            addDesconto();
        } else {
            updateDesconto();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            servicoDesconto = new ServiceDesconto();
            showDesconto();
        } catch (SQLException ex) {
            Logger.getLogger(DescontoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<Desconto> listaDesconto;

    public void showDesconto() {
//        listaTaxas = servicoTaxa.getTaxas();
        List<Desconto> lista = new ArrayList<>();
        for (Desconto t : servicoDesconto.listDescontos()) {
            t.setId(t.getId());
            t.setDescricao(t.getDescricao());
            t.setNome(t.getNome());
//            t.setPercentagem(Double.parseDouble(String.format("%.2f", t.getPercentagem() * 100)));
            lista.add(t);
        }
        listaDesconto = FXCollections.observableArrayList(lista);

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeDesconto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabelaDesconto.setItems(listaDesconto);
    }

    public void addDesconto() {
        servicoDesconto.regista(txtDesconto.getText(), txtDescricao.getText());
        if (servicoDesconto.isOpsSuccess()) {
            resetCampos();
            showDesconto();
        }
    }

    public void updateDesconto() {
        int id = Integer.parseInt(txtID.getText().equals("")?"0":txtID.getText());
        servicoDesconto.actualizar(id, txtDesconto.getText(), txtDescricao.getText());
        if (servicoDesconto.isOpsSuccess()) {
            resetCampos();
            showDesconto();
        }
    }

    public void linhaSelecionadaDesconto(MouseEvent event) throws SQLException {
        Desconto x = tabelaDesconto.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtID.setText(String.valueOf(x.getId()));
            txtDescricao.setText(x.getDescricao());
            txtDesconto.setText(x.getNome());
            ButtonUtilities.buttonChangeText(btnAdd, txtID);
            deleteItem(event, x);
            SysFact.setData(x);
        }
    }

    public void deleteItem(MouseEvent event, Desconto x) {
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
                servicoDesconto.excluir(x.getId());
                if (servicoDesconto.isOpsSuccess()) {
                    showDesconto();
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
        txtDesconto.setText("");
        ButtonUtilities.buttonChangeText(btnAdd, txtID);
        showDesconto();

    }

}
