/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.main;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.SysFact;
import sysfact.model.usuario.Usuario;
import sysfact.util.MetodosUteis;

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
        if (e.getSource() == btn_dashboard) {
            pane = FXMLLoader.load(getClass().getResource("../dashboard/dashboard.fxml"));
            anchorMain.getChildren().setAll(pane);
        } else if (e.getSource() == btn_artigos) {
            pane = FXMLLoader.load(getClass().getResource("../modulo_inventario/produtosView/produtoAdd.fxml"));
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
            // Criando o diálogo personalizado
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Caixa de seleção");
            dialog.setHeaderText("Por favor, selecione o tipo de factura:");

            // Configurando o botão OK/Cancelar
            ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            // Criando o ComboBox e adicionando opções
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Factura Recibo", "Factura Proforma");
            comboBox.setPromptText("Selecione uma factura");

            // Adicionando o ComboBox ao layout do diálogo
            VBox content = new VBox();
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Opções:"), comboBox);
            dialog.getDialogPane().setContent(content);

            // Habilitando o botão OK somente se uma opção for selecionada
            dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(newValue == null);
            });

            // Processando a resposta do usuário
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return comboBox.getValue();
                }
                return null;
            });

            // Exibindo o diálogo e capturando o resultado
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(selectedOption -> {
                SysFact.setData(MetodosUteis.invoiceNumber(selectedOption));
//            System.out.println("Opção selecionada: " + selectedOption);
            });
            pane = FXMLLoader.load(getClass().getResource("../modulo_venda/vendaView/vendasView.fxml"));
            anchorMain.getChildren().setAll(pane);

//             // Criando um diálogo de entrada de texto
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Input Dialog");
//        dialog.setHeaderText("Por favor, insira seu nome:");
//        dialog.setContentText("Nome:");
//
//        // Mostrando o diálogo e aguardando a resposta do usuário
//        Optional<String> result = dialog.showAndWait();
//
//        // Processando o valor inserido pelo usuário
//        result.ifPresent(name -> System.out.println("Nome inserido: " + name));
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
