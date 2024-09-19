/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author JM-Tecnologias
 */
public class FormataDatas {
    
    public String returnData(){
        Date dataActual = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        String data = df.format(dataActual);
        return data;
    }
    
//    public static void main(String[] args) {
//        FormataDatas d = new FormataDatas();
//        System.out.println("A data formatada: "+d.returnData());
//    }
}
