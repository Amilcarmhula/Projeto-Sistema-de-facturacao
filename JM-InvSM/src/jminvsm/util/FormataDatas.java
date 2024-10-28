/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author JM-Tecnologias
 */
public class FormataDatas {

    public String returnData() {
        Date dataActual = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        String data = df.format(dataActual);
        return data;
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
        if (tipo.equals("FR")) {
            invNumber += 1;
        } else if (tipo.equals("FP")) {
            invNumber += 2;
        }else if(tipo == null){
            invNumber += 2;
        }
        return invNumber;
    }
}
