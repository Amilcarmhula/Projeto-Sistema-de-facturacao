/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author JM-Tecnologias
 */
public class ButtonUtilities {
    public static void buttonChangeText(Button btn, TextField txt) {
        if (!"".equals(txt.getText())) {
            btn.setText("Actualizar");
        } else {
            btn.setText("Adicionar");
        }
    }
}
