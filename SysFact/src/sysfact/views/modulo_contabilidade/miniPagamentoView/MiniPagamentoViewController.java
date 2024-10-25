/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_contabilidade.miniPagamentoView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sysfact.SysFact;
import sysfact.model.empresa.Empresa;
import sysfact.model.metodo_pagamento.MetodoPagamento;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;
import sysfact.service.metodoPagamento.ServiceMetodoPagamento;
import sysfact.service.transacoes.ServiceTransacoes;
import sysfact.service.vendas.factura.ServiceFactura;
import static sysfact.util.AlertUtilities.showErroNotification;
import sysfact.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class MiniPagamentoViewController implements Initializable {

    private Empresa empresaData;
    private Usuario userData;
    private ServiceFactura service;

    @FXML
    private Label labNumeroFactura;
    @FXML
    private Label labTotal;
    @FXML
    private Label labTrocos;
    @FXML
    private TextField txtValorRecebido;
    @FXML
    private TextField txtDescricao;
    @FXML
    private ComboBox<String> combmetodoPagamento;

    /**
     * Initializes the controller class.
     */
    /**
     * Metodo para processar pagamento da factura
     */
    public void processaPagamento(ActionEvent event) throws SQLException {
        Factura f = service.consultaFactura(labNumeroFactura.getText());
        if (!f.isEstado()) {
            addTransacao();
            f = service.consultaFactura(labNumeroFactura.getText());
        }
        if (f.isEstado()) {
            if (LoadAndMoveUtilities.returnToStage()) {
                LoadAndMoveUtilities.setEstadoStage(false);
                LoadAndMoveUtilities.showStage(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
            if (LoadAndMoveUtilities.returnToBaseAnchor()) {
                LoadAndMoveUtilities.setEstadoPopUP(false);
                LoadAndMoveUtilities.showAsPopUP(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
            service.geraRelatorio(labNumeroFactura.getText());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        valorRecebidoListener();
        this.userData = SysFact.getUserData();
        this.empresaData = SysFact.getEmpresaData();
        try {
            service = new ServiceFactura();
            labNumeroFactura.setText((String) SysFact.getData());
            populateComboMetodoPagamento();
            showTotalFactura();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(MiniPagamentoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<MetodoPagamento> listaMetodosPagamento;
    private Map<String, MetodoPagamento> mapaMetodos;

    public void populateComboMetodoPagamento() throws SQLException {
        ServiceMetodoPagamento sm = new ServiceMetodoPagamento();
        listaMetodosPagamento = sm.listaMetodos();
        mapaMetodos = new HashMap<>();
        List<String> lista = new ArrayList<>();
        for (MetodoPagamento metodoPagamento : listaMetodosPagamento) {
            mapaMetodos.put(metodoPagamento.getNome(), metodoPagamento);
            lista.add(metodoPagamento.getNome());
        }
        combmetodoPagamento.setItems(FXCollections.observableList(lista));

    }

    private double troco;

    public double getTroco() {
        return troco;
    }

//    public double valorRecebidoListener() {
//        txtValorRecebido.textProperty().addListener((new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
//
//                double total = Double.parseDouble(labTotal.getText());
//                double valorRecebido = 0;
//                if (!newValue.isEmpty()) {
//                    valorRecebido = Double.parseDouble(newValue);
//                }
//
//                if (metodo != null && metodo.getId() == 1 && total < valorRecebido) {
//                    troco = valorRecebido - total;
//                    labTrocos.setText(troco + " Mt");
//                } else {
//                    troco = 0;
//                    labTrocos.setText(troco + " Mt");
//                }
//            }
//        }));
//        return troco;
//    }
    public double valorRecebidoListener() {
        txtValorRecebido.textProperty().addListener(((observable, oldVal, newVal) -> {
            MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());

            double total = Double.parseDouble(labTotal.getText());
            double valorRecebido = 0;
            if (!newVal.isEmpty()) {
                valorRecebido = Double.parseDouble(oldVal);
            }

            if (metodo != null && metodo.getId() == 1 && total < valorRecebido) {
                troco = valorRecebido - total;
                labTrocos.setText(troco + " Mt");
            } else {
                troco = 0;
                labTrocos.setText(troco + " Mt");
            }
        }));
        return troco;
    }

    public void addTransacao() throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        if (combmetodoPagamento.getValue() == null) {
            showErroNotification("Erro", "Metodo de pagamento nao selecionado!");
            return;
        }
        MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
        String dataPagamento = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        double total = Double.parseDouble(labTotal.getText());
        double valorRecebido = Double.parseDouble(txtValorRecebido.getText().equals("") ? "0" : txtValorRecebido.getText());
        if (metodo.getId() == 1 && total < valorRecebido) {
            txtValorRecebido.setText((valorRecebido - getTroco()) + "");
        }
        st.registapagamentoFactura(dataPagamento, Double.valueOf(txtValorRecebido.getText().equals("") ? "0" : txtValorRecebido.getText()),
                txtDescricao.getText(), empresaData.getContaBancaria().getId(),
                metodo.getId(), labNumeroFactura.getText(), userData);
        if (st.isOpsSuccess()) {
            txtDescricao.setText("");
            txtValorRecebido.setText("");
            showTotalFactura();
        }
    }

    public void showTotalFactura() throws SQLException {
        Factura f = service.consultaFactura(labNumeroFactura.getText());
        double total = f.getTotal() - f.getTransacoes().getValor();
        labTotal.setText(total + "");

    }

}
