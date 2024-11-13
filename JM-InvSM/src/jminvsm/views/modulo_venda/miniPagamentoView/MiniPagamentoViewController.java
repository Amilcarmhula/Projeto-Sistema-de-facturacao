/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.miniPagamentoView;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.metodoPagamento.ServiceMetodoPagamento;
import jminvsm.service.transacoes.ServiceTransacoes;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;

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
     * @param event
     * @throws java.sql.SQLException
     */
    public void processaPagamento(ActionEvent event) throws SQLException {
        Factura f = service.consultaFactura(labNumeroFactura.getText());
        if (!f.getEstado().equals("Paga") || !f.getEstado().equals("Cancelada")) {
            addTransacao(event);
            f = service.consultaFactura(labNumeroFactura.getText());
        }
        if (f.getEstado().equals("Paga") || f.getEstado().equals("Cancelada")) {
            
            if (LoadAndMoveUtilities.returnToStage()) {
                SysFact.setData(null);
                LoadAndMoveUtilities.setEstadoStage(false);
                LoadAndMoveUtilities.showStage(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
            if (LoadAndMoveUtilities.returnToBaseAnchor()) {
                SysFact.setData(null);
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
            mapaMetodos.put(metodoPagamento.getNomePagamento(), metodoPagamento);
            lista.add(metodoPagamento.getNomePagamento());
        }
        combmetodoPagamento.setItems(FXCollections.observableList(lista));

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
    private double troco;

    public double getTroco() {
        return troco;
    }
    public double valorRecebidoListener() {
        txtValorRecebido.textProperty().addListener(((observable, oldVal, newVal) -> {
            MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());

            double total = Double.parseDouble(labTotal.getText());
            double valorRecebido = 0;
            if (!newVal.isEmpty()) {
                valorRecebido = Double.parseDouble(newVal);
            }

            if (metodo != null && metodo.getId() == 1 && total < valorRecebido) {
                troco = valorRecebido - total;
                labTrocos.setText(troco+" Mt");
            } else {
                troco = 0;
                labTrocos.setText(troco+" Mt");
            }
        }));
        return troco;
    }

    public void addTransacao(ActionEvent e) throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        if (combmetodoPagamento.getValue() == null) {
            showErroAlert( "Metodo de pagamento nao selecionado!");
            return;
        }
        MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
        String dataPagamento = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        double total = Double.parseDouble(labTotal.getText());
        double valorRecebido = Double.parseDouble(txtValorRecebido.getText().equals("") ? "0" : txtValorRecebido.getText());
        double valorPago;
        if (metodo.getId() == 1 && total < valorRecebido) {
            valorPago = valorRecebido - getTroco();
        }else{
            valorPago = valorRecebido;
        }
        st.registapagamentoFacturaAvista(dataPagamento, valorRecebido, valorPago, getTroco(),
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
        double total = f.getTotal() - f.getTotalPago();
        labTotal.setText(total + "");

    }

}
