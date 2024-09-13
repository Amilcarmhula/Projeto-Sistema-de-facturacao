/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private Button btn_cliente;

    @FXML
    private Button btn_fornecedor;

    @FXML
    private Button btn_config;

    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_facturas;

    @FXML
    private Button btn_produtos;

    @FXML
    private Button btn_vendas;

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) anchorMain.getScene().getWindow();
        stage.setIconified(true);
    }

    // Carregar a primeira tela no centro
    @FXML
    public void switchForm(ActionEvent e) throws IOException {
        AnchorPane pane;
        if (e.getSource() == btn_dashboard) {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_vendas) {
            pane = FXMLLoader.load(getClass().getResource("../vendas/vendas.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_produtos) {
            pane = FXMLLoader.load(getClass().getResource("../produtos/produto.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        } else if (e.getSource() == btn_cliente) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_venda/clienteView/clienteView.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_fornecedor) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_compra/fornecedorView/fornecedorView.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_facturas) {
            pane = FXMLLoader.load(getClass().getResource("../factura/factura.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: transparent");
            btn_facturas.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_produtos.setStyle("-fx-background-color: transparent");
        } else if (e.getSource() == btn_config) {
            pane = FXMLLoader.load(getClass().getResource("../config/config.fxml"));
            anchorMain.getChildren().setAll(pane);
            btn_dashboard.setStyle("-fx-background-color: transparent");
            btn_vendas.setStyle("-fx-background-color: transparent");
            btn_cliente.setStyle("-fx-background-color: transparent");
            btn_config.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
            btn_facturas.setStyle("-fx-background-color: transparent");
            btn_produtos.setStyle("-fx-background-color: transparent");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
