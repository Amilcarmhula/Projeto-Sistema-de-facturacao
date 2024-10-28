/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author JM-Tecnologias
 */
public class InvoiceUtilities {
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
}
