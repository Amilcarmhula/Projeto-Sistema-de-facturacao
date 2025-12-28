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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jminvsm.SysFact;
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
    private Button btn_armazem;

    @FXML
    private Button btn_artigos;

    @FXML
    private Button btn_vendas;

    /*Butoes do modulo de configuracao*/
    @FXML
    private Button btn_setup;
    @FXML
    private Button btn_pagamento;
    @FXML
    private Button btn_stock;
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
        // Verifica se a janela já está maximizada, para alternar entre maximizar e restaurar. EXPAND
        stage.setMaximized(!stage.isMaximized());
    }

    

    @FXML
    public void switchForm(ActionEvent e) throws IOException {
        SysFact.setData(null);
        String path = "";
        if (e.getSource() == btn_dashboard) {
            
            path = "/jminvsm/views/dashboard/dashboard.fxml";
        } else if (e.getSource() == btn_artigos) {
            path = "/jminvsm/views/modulo_inventario/produtosView/produtoAdd.fxml";
        } else if (e.getSource() == btn_cliente) {
            path = "/jminvsm/views/modulo_venda/clienteView/clienteView.fxml";
        } else if (e.getSource() == btn_fornecedor) {
            path = "/jminvsm/views/modulo_compra/fornecedorView/fornecedorView.fxml";
        } else if (e.getSource() == btn_armazem) {
            path = "/jminvsm/views/modulo_inventario/armazemView/armazemView.fxml";
        } else if (e.getSource() == btn_vendas) {
            path = "/jminvsm/views/modulo_venda/vendaView/vendasView.fxml";
        } else if (e.getSource() == btn_pagamento) {
            path = "/jminvsm/views/modulo_venda/pagamentosView/pagamentoView.fxml";
        }
        AnchorPane pane = FXMLLoader.load(getClass().getResource(path));
        anchorMain.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user = SysFact.getUserData();
        labUserName.setText(user.getNome());
        labEmail.setText(user.getEmail());
        labNivel.setText(user.getNivelAcesso().getDescricao());
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(getClass().getResource("/jminvsm/views/dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        minhaFuncao();
    }

//    public void minhaFuncao() {
//        Thread x = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 5; i++) {
//                    // Imprime o número i no console de erro
//                    System.out.print(i + " ");
////                    pensar como acessar os metodos de consultas pertecentes a 
////                    janela(anchorPane) que estara aberta no memonto para que sejam feitos updates
////                    Os metodos de consulta nao podem chamar os metodos de reset de campos
//                    try {
//                        // A thread dorme por 1 segundo
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        // Trata uma possível interrupção da thread
//                        System.out.println("Erro: " + ex);
//                    }
//                }
//                minhaFuncao();
//
//                // Imprime a mensagem "Fim" após o término do loop
//                System.out.println("\nFim");
//            }
//        });
//
//        // Inicia a execução da thread
//        x.start();
//    }
}
