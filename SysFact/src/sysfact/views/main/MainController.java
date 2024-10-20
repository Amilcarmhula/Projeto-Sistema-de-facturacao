/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sysfact.SysFact;
import sysfact.model.usuario.Usuario;
import sysfact.util.FilePathFinderUtilities;
import sysfact.util.LoadAndMoveUtilities;

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
    private Button btn_taxas;
    @FXML
    private Button btn_unidades;
    @FXML
    private Button btn_categorias;
    @FXML
    private Button btn_dashboard11;
    @FXML
    private Button btn_cliente11;
    @FXML
    private Button btn_produtos11;
    @FXML
    private Button btn_facturas111;
    @FXML
    private Button btn_produtos112;
    @FXML
    private Button btn_stock;

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
    public void getViews(ActionEvent e) {
        String path = "";
        if (e.getSource() == btn_taxas) {
            path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "taxaView.fxml");
//            path = "../views/modulo_configs/taxasView/taxaView.fxml";
        } else if (e.getSource() == btn_unidades) {
            path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "unidadeView.fxml");
        } else if (e.getSource() == btn_categorias) {
            path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "categoriaView.fxml");
        }
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, path);

    }

    // Carregar a primeira tela no centro
    @FXML
    public void switchForm(ActionEvent e) throws IOException {
        AnchorPane pane;
        if (e.getSource() == btn_dashboard) {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_artigos) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_inventario/produtosView/produtoAdd.fxml"));
//            System.out.println(anchorMain.getChildren().remove(0).getId());
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_cliente) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_venda/clienteView/clienteView.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_fornecedor) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_compra/fornecedorView/fornecedorView.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_armazem) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_inventario/armazemView/armazemView.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_vendas) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_venda/vendaView/vendasView.fxml"));
            anchorMain.getChildren().setAll(pane);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user = SysFact.getUserData();
        labUserName.setText(user.getNome());
        labEmail.setText(user.getEmail());
        labNivel.setText(user.getNivelAcesso().getDescricao());
//        btn_dashboard.setStyle("-fx-background-color: rgba(55, 55, 179, 0.25)");
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
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
