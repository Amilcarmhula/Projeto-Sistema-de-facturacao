/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodosUteis {

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
        invNumber += number + "/"+tipo;
        return invNumber;
    }
    
    public static String validaString(FontAwesomeIcon icon, String data){
        if(data == null || data.isEmpty()){
            showDialog(icon, "Erro de dados", "Preencha todos campos obrigatorios!");
            return "";
        }
        return data;
    }


    public static void showDialog(FontAwesomeIcon icon,String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        FontAwesomeIconView erroIcon = new FontAwesomeIconView(icon);
        erroIcon.setStyle("-fx-fill: rgba(35,35,35,.3); -fx-font-size: 30px;");
        alert.setGraphic(erroIcon);
        alert.showAndWait();
    }

    public static void main(String[] args) {
    }
}
