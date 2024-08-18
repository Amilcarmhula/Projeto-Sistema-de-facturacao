/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package sysfact;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author JM-Tecnologias
 */
public class MainController implements Initializable {
    
//      @FXML
    private Button btnlogin;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtusarname;
    
    public void close(){
        System.exit(0);
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
