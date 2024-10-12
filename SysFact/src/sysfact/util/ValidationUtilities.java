/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author JM-Tecnologias
 */
public class ValidationUtilities {
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
}
