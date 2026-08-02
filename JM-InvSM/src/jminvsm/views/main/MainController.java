/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.usuario.Usuario;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class MainController implements Initializable {

    private Usuario user;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private Label labUserName;

    @FXML
    private Label labNivel;

    @FXML
    private Label labEmail;

    @FXML
    private Button btn_cliente;
    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_fornecedor;
    @FXML
    private Button btn_requisicoes;
    @FXML
    private Button btn_ordemCompra;
    @FXML
    private Button btn_recebimentoCompra;
    @FXML
    private Button btn_contasPagar;

    @FXML
    private Button btn_armazem;

    @FXML
    private Button btn_artigos;

    @FXML
    private Button btn_vendas;
    @FXML
    private Button btn_vendasPOS;
    @FXML
    private Button btn_vendasFactura;

    @FXML
    private Button btn_setup;
    @FXML
    private Button btn_pagamento;
    @FXML
    private Button btn_stock;
    @FXML
    private Button btn_fiscal;
    @FXML
    private Label labNotification;

    @FXML
    public void close() {
        System.exit(0);
    }

    @FXML
    public void minimize() {
        Stage stage = (Stage) anchorMain.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void maximize() {
        Stage stage = (Stage) anchorMain.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    public void switchForm(ActionEvent event) throws IOException {
        SessaoAplicacao.setData(null);

        String path = resolveViewPath(event.getSource());
        if (path == null || path.trim().isEmpty()) {
            labNotification.setText("Esta funcionalidade ainda nÃ£o possui uma vista configurada.");
            return;
        }

        loadView(path);
        labNotification.setText("");
    }

    private String resolveViewPath(Object source) {
        if (source == btn_dashboard) {
            return "/jminvsm/views/dashboard/dashboard.fxml";
        } else if (source == btn_artigos) {
            return "/jminvsm/views/modulo_inventario/produtosView/produtoAdd.fxml";
        } else if (source == btn_cliente) {
            return "/jminvsm/views/modulo_venda/clienteView/clienteView.fxml";
        } else if (source == btn_fornecedor) {
            return "/jminvsm/views/modulo_compra/fornecedorView/fornecedorView.fxml";
        } else if (source == btn_requisicoes) {
            return "/jminvsm/views/modulo_compra/requisicaoView/requisicaoView.fxml";
        } else if (source == btn_ordemCompra) {
            return "/jminvsm/views/modulo_compra/ordemCompraView/ordemCompraView.fxml";
        } else if (source == btn_recebimentoCompra) {
            return "/jminvsm/views/modulo_compra/recebimentoCompraView/recebimentoCompraView.fxml";
        } else if (source == btn_contasPagar) {
            return "/jminvsm/views/modulo_compra/financeiroFornecedorView/financeiroFornecedorView.fxml";
        } else if (source == btn_armazem) {
            return "/jminvsm/views/modulo_inventario/armazemView/armazemView.fxml";
        } else if (source == btn_stock) {
            return "/jminvsm/views/modulo_inventario/transferenciaView/requisicaoTransferenciaView.fxml";
        } else if (source == btn_vendas) {
            return "/jminvsm/views/modulo_venda/vendaView/vendasView.fxml";
        } else if (source == btn_vendasPOS) {
            return "/jminvsm/views/modulo_venda/vendaView/vendasPOS/vendasPOSView.fxml";
        } else if (source == btn_vendasFactura) {
            return "/jminvsm/views/modulo_venda/vendaView/vendasFactura/vendasPorFacturacaoView_moderno.fxml";
        } else if (source == btn_pagamento) {
            return "/jminvsm/views/modulo_venda/pagamentosView/pagamentoView_moderno.fxml";
        } else if (source == btn_setup) {
            return "/jminvsm/views/configuracaoSistemaView/configuracaoSistemaView.fxml";
        } else if (source == btn_fiscal) {
            return "/jminvsm/views/modulo_fiscal/saft/fiscalExportView.fxml";
        }

        return null;
    }

    private void loadView(String path) throws IOException {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            throw new IOException("FXML nÃ£o encontrado: " + path);
        }

        Parent pane = FXMLLoader.load(resource);

        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);

        if (pane instanceof Region) {
            Region region = (Region) pane;
            region.setMinSize(0.0, 0.0);
            region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            region.prefWidthProperty().bind(anchorMain.widthProperty());
            region.prefHeightProperty().bind(anchorMain.heightProperty());
        }

        anchorMain.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user = SessaoAplicacao.getUserData();
        anchorMain.setMinSize(0.0, 0.0);
        anchorMain.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (user != null) {
            labUserName.setText(user.getNome());
            labEmail.setText(user.getEmail());
            if (user.getNivelAcesso() != null) {
                labNivel.setText(user.getNivelAcesso().getDescricao());
            }
        } else {
            labUserName.setText("Utilizador");
            labEmail.setText("");
            labNivel.setText("");
        }

        try {
            loadView("/jminvsm/views/dashboard/dashboard.fxml");
        } catch (IOException ex) {
            labNotification.setText("NÃ£o foi possÃ­vel carregar o dashboard.");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

