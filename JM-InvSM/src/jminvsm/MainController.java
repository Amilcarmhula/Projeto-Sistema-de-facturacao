/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package jminvsm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jminvsm.dao.empresa.EmpresaDAO;
import jminvsm.dao.usuario.UsuarioDAO;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.usuario.ServiceUsuario;
import jminvsm.util.LoadAndMoveUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class MainController implements Initializable {

    private ServiceUsuario service;

    @FXML
    private Button btnlogin;
    @FXML
    private Button btnClose;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtusarname;

    public void close() {
        System.exit(0);
    }

    public void callLog(KeyEvent keyEvt) throws SQLException, IOException {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {
            log();
        }
    }

    public void log() throws SQLException, IOException {
        Usuario user = null;
        user = service.authLogin(txtusarname.getText(), txtpassword.getText());

        if (user != null) {
            /*Dados de login a serem armazenados*/
            SysFact.setUserData(user);
            EmpresaDAO empresaData = new EmpresaDAO();
            SysFact.setEmpresaData(empresaData.getEmpresaData());
            btnlogin.getScene().getWindow().hide();

//                Parent root = FXMLLoader.load(getClass().getResource("../jminvsm/views/main/main.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/jminvsm/views/main/main.fxml"));
            Stage stage = new Stage();
            LoadAndMoveUtilities.moveWindows(root, stage);
            Scene scene = new Scene(root);

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            service = new ServiceUsuario();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
