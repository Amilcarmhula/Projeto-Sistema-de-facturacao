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
            showDialog(icon, "Erro de dados", "Dados invalidos!");
            return "";
        }
        return data;
    }
    public static int validaInteger(FontAwesomeIcon icon, Integer data) {
        if (data == null) {
            showDialog(icon, "Erro de dados", "Dados invalidos!");
            return 0;
        }
        return data;
    }
    
    public static double validaDouble(FontAwesomeIcon icon, Double data) {
        if (data == null) {
            showDialog(icon, "Erro de dados", "Dados invalidos!");
            return 0;
        }
        return Double.parseDouble(String.format("%.2f", data));
    }

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
}
