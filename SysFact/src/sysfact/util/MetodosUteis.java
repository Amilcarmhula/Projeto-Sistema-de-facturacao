/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.views.modulo_venda.clienteView.ClienteViewController;
import sysfact.views.modulo_venda.vendaView.searchCliente.ClienteSearchController;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodosUteis {

    private static double x = 0;
    private static double y = 0;

    public static String datePlusXDays(int x) {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        LocalDate futureDate = currentDate.plusDays(x); // Add the specified number of days
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDate.format(df); // Format the future date
        //        System.out.println("A data final será: " + formattedDate + " após adicionados " + x + " dias");
        return formattedDate;
    }

    public static String actualDate() {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(df); // Format the future date
        //        System.out.println("A data final será: " + formattedDate + " após adicionados " + x + " dias");
        return formattedDate;
    }

    public static String invoiceNumber(int tipo) {
        String invNumber;
        Date dataActual = new Date();
        SimpleDateFormat tf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS");
        String[] data = tf.format(dataActual).split(":");

        invNumber = data[0] + "-";
        int number = Integer.parseInt(data[1]) + Integer.parseInt(data[2])
                + Integer.parseInt(data[3]) + Integer.parseInt(data[4])
                + Integer.parseInt(data[5]) + Integer.parseInt(data[6]);
        invNumber += number + "/" + tipo;
        return invNumber;
    }

    public static String validaString(FontAwesomeIcon icon, String data) {
        if (data == null || data.isEmpty()) {
            showDialog(icon, "Erro de dados", "Preencha todos campos obrigatorios!");
            return "";
        }
        return data;
    }

    public static void showDialog(FontAwesomeIcon icon, String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        FontAwesomeIconView erroIcon = new FontAwesomeIconView(icon);
        erroIcon.setStyle("-fx-fill: rgba(35,35,35,.3); -fx-font-size: 30px;");
        alert.setGraphic(erroIcon);
        alert.showAndWait();
    }

    public static void moveWindows(Parent root, Stage stage) {
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(.5);
        });

        root.setOnMouseReleased((MouseEvent event) -> {
            stage.setOpacity(1);
        });
    }

    public static void loadFXML(Modality modal, String path) {
        try {
            Parent root = FXMLLoader.load(MetodosUteis.class.getResource(path));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            /*
            Metodo para mover tela
             */
            MetodosUteis.moveWindows(root, stage);

            stage.initStyle(StageStyle.TRANSPARENT);
            /*
            Torna a tela nova como uma Modal(bloqueia a janela anterior ate que
            esta seja fechada)
             */
            if (modal != null) {
                stage.initModality(modal);
                stage.showAndWait();//Bloqueia ate que esta seja fechada
            }else{
                stage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(MetodosUteis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    Variavel que ira armazenar a AnchorPane antes de ser
    removida ou ocultada
     */
    private static AnchorPane baseAnchorPane;
    private static boolean estadoPopUP;

    /*
    Mostra nova tela, ocultando a anterior e bloqueando acesso a controles da tela
    anterior
    */
    public static void showAsPopUP(/*AnchorPane baseAnchorPane,*/ActionEvent aevent, MouseEvent mevent) {
        if (aevent != null) {
            Node source = (Node) aevent.getSource();
            MetodosUteis.baseAnchorPane = (AnchorPane) source.getScene().lookup("#anchorMain");
            baseAnchorPane.setVisible(false);
        }
        if (mevent != null) {
            Node source = (Node) mevent.getSource();
            MetodosUteis.baseAnchorPane = (AnchorPane) source.getScene().lookup("#anchorMain");
            baseAnchorPane.setVisible(false);
        }
    }

//    public static AnchorPane getBaseAnchorPane() {
//        return baseAnchorPane;
//    }
    public static boolean isEstadoPopUP() {
        return estadoPopUP;
    }

    public static void setEstadoPopUP(boolean estadoPopUP) {
        MetodosUteis.estadoPopUP = estadoPopUP;
    }

    public static boolean returnToBaseAnchor() {
        if (baseAnchorPane != null) {
            setEstadoPopUP(true);
            baseAnchorPane.setVisible(true);
        }
        return isEstadoPopUP();
    }
    
    /*
    Variavel que ira armazenar a stage antes de ser
     ocultada
     */
    private static Stage baseStage;
    private static boolean estadoStage;
    /*
    Mostra nova tela, sem ocultando a anterior e  ne bloqueando acesso a controles da tela
    anterior
    */
    public static void showStage(ActionEvent event, MouseEvent evt) {
        if (event != null) {
            MetodosUteis.baseStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            MetodosUteis.baseStage.hide(); // Esconde a janela principal
        }
        if (evt != null) {
            MetodosUteis.baseStage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            MetodosUteis.baseStage.hide(); // Esconde a janela principal
        }
    }

    public static boolean isEstadoStage() {
        return estadoStage;
    }

    public static void setEstadoStage(boolean estadoStage) {
        MetodosUteis.estadoStage = estadoStage;
    }

    public static boolean returnToStage() {
        if (baseStage != null) {
            setEstadoStage(true);
            baseStage.show(); // Mostra a janela principal novamente
        }
        return isEstadoStage();
    }

    public static void main(String[] args) {
    }
}
