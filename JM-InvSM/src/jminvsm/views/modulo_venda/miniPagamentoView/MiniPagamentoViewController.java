/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.miniPagamentoView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.ServiceException;
import jminvsm.service.transacoes.ServiceFluxoPagamento;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class MiniPagamentoViewController implements Initializable {

    private Empresa empresaData;
    private Usuario userData;
    private ServiceFluxoPagamento serviceFluxoPagamento;

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
        Factura f = serviceFluxoPagamento.consultarFactura(labNumeroFactura.getText());
        if (!f.getEstado().equals("Paga") || !f.getEstado().equals("Cancelada")) {
            addTransacao(event);
            f = serviceFluxoPagamento.consultarFactura(labNumeroFactura.getText());
        }
        if (f.getEstado().equals("Paga") || f.getEstado().equals("Cancelada")) {
            
            if (LoadAndMoveUtilities.returnToStage()) {
                SessaoAplicacao.setData(null);
                LoadAndMoveUtilities.setEstadoStage(false);
                LoadAndMoveUtilities.showStage(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
            if (LoadAndMoveUtilities.returnToBaseAnchor()) {
                SessaoAplicacao.setData(null);
                LoadAndMoveUtilities.setEstadoPopUP(false);
                LoadAndMoveUtilities.showAsPopUP(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        valorRecebidoListener();
        this.userData = SessaoAplicacao.getUserData();
        this.empresaData = SessaoAplicacao.getEmpresaData();
        try {
            serviceFluxoPagamento = new ServiceFluxoPagamento();
            labNumeroFactura.setText((String) SessaoAplicacao.getData());
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
        listaMetodosPagamento = serviceFluxoPagamento.listarMetodos();
        mapaMetodos = serviceFluxoPagamento.mapearMetodosPorNome(listaMetodosPagamento);
        List<String> lista = new ArrayList<>();
        for (MetodoPagamento metodoPagamento : listaMetodosPagamento) {
            lista.add(metodoPagamento.getNomePagamento());
        }
        combmetodoPagamento.setItems(FXCollections.observableList(lista));

    }

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

            troco = serviceFluxoPagamento.calcularTroco(total, valorRecebido, metodo);
            labTrocos.setText(troco+" Mt");
        }));
        return troco;
    }

    public void addTransacao(ActionEvent e) throws SQLException {
        if (combmetodoPagamento.getValue() == null) {
            showErroAlert( "Metodo de pagamento nao selecionado!");
            return;
        }
        MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
        try {
            boolean registado = serviceFluxoPagamento.registarPagamentoAvista(labNumeroFactura.getText(),
                    txtValorRecebido.getText(), getTroco(), txtDescricao.getText(), metodo, empresaData, userData);
            if (!registado) {
                return;
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        showSuccessAlert("Pagamento registado com sucesso!");
        txtDescricao.setText("");
        txtValorRecebido.setText("");
        showTotalFactura();
    }

    public void showTotalFactura() throws SQLException {
        labTotal.setText(serviceFluxoPagamento.saldoFactura(labNumeroFactura.getText()) + "");

    }

}

