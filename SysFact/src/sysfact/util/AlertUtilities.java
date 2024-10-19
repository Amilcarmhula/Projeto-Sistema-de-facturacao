/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author JM-Tecnologias
 */
public class AlertUtilities {

    public static void showDialog(FontAwesomeIcon icon, String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        FontAwesomeIconView erroIcon = new FontAwesomeIconView(icon);
        erroIcon.setStyle("-fx-fill: #000000b3; -fx-font-size: 25px;");
        alert.setGraphic(erroIcon);
        alert.showAndWait();
    }

    public static void showErroNotification(String titulo, String mensagem) {
//        Image image = new Image("/image/danger.png");
        Notifications not = Notifications.create();
//        not.graphic(new ImageView(image));
        not.title(titulo);
        not.text(mensagem);
        not.hideAfter(Duration.seconds(10));
        not.darkStyle();
        not.position(Pos.BOTTOM_RIGHT);
        not.show();
    }
    
    public static void showSuccessNotification(String titulo, String mensagem) {
//        Image image = new Image("/image/danger.png");
        Notifications not = Notifications.create();
//        not.graphic(new ImageView(image));
        not.title(titulo);
        not.text(mensagem);
        not.hideAfter(Duration.seconds(10));
//        not.darkStyle();
        not.position(Pos.BOTTOM_RIGHT);
        not.show();
    }
}
