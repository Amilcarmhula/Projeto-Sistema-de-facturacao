/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package sysfact;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.dao.empresa.EmpresaDAO;
import sysfact.dao.usuario.UsuarioDAO;
import sysfact.model.usuario.Usuario;
import sysfact.service.usuario.ServiceUsuario;
import sysfact.util.Conexao;
import sysfact.util.MetodosUteis;

/**
 *
 * @author JM-Tecnologias
 */
public class MainController implements Initializable {
    private double x = 0;
    private double y = 0;

    @FXML
    private Button btnlogin;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtusarname;

    public void close() {
        System.exit(0);
    }

    public void log() throws SQLException, IOException {
        Alert alert;
        Usuario user = null;
//        if (txtusarname.getText().isEmpty() || txtpassword.getText().isEmpty()) {
//            MetodosUteis.showErro("Mensagem de Erro.", "Campos Usuario, cogido ou senha nao preenchidos!");
//        } else {
            try {
                UsuarioDAO z = new UsuarioDAO();
                ServiceUsuario service = new ServiceUsuario(z);
                user = service.authLogin(txtusarname.getText(), txtpassword.getText());
                
//                user = z.login(txtusarname.getText(), txtpassword.getText());
                if (user != null) {
                    /*Dados de login a serem armazenados*/
                    SysFact.setUserData(user);
                    EmpresaDAO empresaData = new EmpresaDAO();
                    SysFact.setEmpresaData(empresaData.getEmpresaData());
                    btnlogin.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("../sysfact/views/main/main.fxml"));
                    Stage stage = new Stage();
                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);

                        stage.setOpacity(.5);
                    });

                    root.setOnMouseReleased((MouseEvent event) -> {
                        stage.setOpacity(1);
                    });
                    Scene scene = new Scene(root);

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
//                } else {
//                    MetodosUteis.showInfo("Mensagem de Erro.", "User nao existe! ");
                }
            } catch (SQLException ex) {
                MetodosUteis.showErro(FontAwesomeIcon.EXCLAMATION_TRIANGLE,"Mensagem de Erro.", "Erro ao se logar-se: " + ex);
            }
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
