/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.Alert;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodosUteis {

    public static String datePlusXcDays(int x) {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        LocalDate futureDate = currentDate.plusDays(x); // Add the specified number of days
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDate.format(df); // Format the future date
        //        System.out.println("A data final será: " + formattedDate + " após adicionados " + x + " dias");
        return formattedDate;
    }

    public static String invoiceNumber(String tipo) {
        String invNumber;
        Date dataActual = new Date();
        SimpleDateFormat tf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS");
        String[] data = tf.format(dataActual).split(":");

        invNumber = data[0] + "-";
        int number = Integer.parseInt(data[1]) + Integer.parseInt(data[2])
                + Integer.parseInt(data[3]) + Integer.parseInt(data[4])
                + Integer.parseInt(data[5]) + Integer.parseInt(data[6]);
        invNumber += number + "/";
        if (tipo.equals("Factura Recibo")) {
            invNumber += 1;
        } else if (tipo.equals("Factura Proforma")) {
            invNumber += 2;
        }
        return invNumber;
    }

    public static void showErro(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
    }
}
