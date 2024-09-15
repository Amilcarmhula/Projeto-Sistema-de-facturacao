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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.SysFact;
import sysfact.model.usuario.Usuario;

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
    private Button btn_cliente;

    @FXML
    private Button btn_fornecedor;

    @FXML
    private Button btn_armazem;

    @FXML
    private Button btn_artigos;

    /*Butoes do modulo de configuracao*/
    @FXML
    private Button btn_taxas;
    @FXML
    private Button btn_unidades;
    @FXML
    private Button btn_categorias;

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) anchorMain.getScene().getWindow();
        stage.setIconified(true);
    }

    public void getViews(ActionEvent e) {
        String view = "";
        if (e.getSource() == btn_taxas) {
            view = "../modulo_configs/taxasView/taxaView.fxml";
        } else if (e.getSource() == btn_unidades) {
            view = "../modulo_configs/unidadeView/unidadeView.fxml";
        } else if (e.getSource() == btn_categorias) {
            view = "../modulo_inventario/produtosView/categoriaAdd/addCategoria.fxml";
        }
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(view))));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Carregar a primeira tela no centro
    @FXML
    public void switchForm(ActionEvent e) throws IOException {
        AnchorPane pane;
        if (e.getSource() == btn_artigos) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_inventario/produtosView/produtoView.fxml"));
            anchorMain.getChildren().setAll(pane);
//            btn_dashboard.setStyle("-fx-background-color: transparent");
//            btn_vendas.setStyle("-fx-background-color: transparent");
//            btn_cliente.setStyle("-fx-background-color: transparent");
//            btn_config.setStyle("-fx-background-color: transparent");
//            btn_facturas.setStyle("-fx-background-color: transparent");
//            btn_artigos.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        } else if (e.getSource() == btn_cliente) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_venda/clienteView/clienteView.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_fornecedor) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_compra/fornecedorView/fornecedorView.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_armazem) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_inventario/armazemView/armazemView.fxml"));
            anchorMain.getChildren().setAll(pane);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user = SysFact.getUserData();
        labUserName.setText(user.getNome());
//        btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/*
    public void minhaFuncao() {
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    // Imprime o número i no console de erro
                    System.err.print(i + " ");
                    try {
                        // A thread dorme por 1 segundo
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // Trata uma possível interrupção da thread
                        System.out.println("Erro: " + ex);
                    }
                }
                // Imprime a mensagem "Fim" após o término do loop
                System.out.println("\nFim");
            }
        });
        // Inicia a execução da thread
        x.start();
    }
*/
}
