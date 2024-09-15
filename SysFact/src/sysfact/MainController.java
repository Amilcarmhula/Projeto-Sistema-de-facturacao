/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package sysfact;

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
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;

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
        Usuario u = new Usuario();
        if (txtusarname.getText().isEmpty() || txtpassword.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mensagem de Erro.");
            alert.setHeaderText(null);
            alert.setContentText("Campos Usuario, cogido ou senha nao preenchidos!");
            alert.showAndWait();
        } else {
            u.setUsuario(txtusarname.getText());
            u.setSenha(Integer.parseInt(txtpassword.getText()));
            Usuario user = null;
            try {
                String sql = "select * from usuario where usuario=? and senha=?";
                PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
                ps.setString(1, u.getUsuario());
                ps.setInt(2, u.getSenha());

                ResultSet result = ps.executeQuery();
                if (result.next()) {
                    user = new Usuario();
                    user.setId(result.getInt("id"));
                    user.setNome(result.getString("nome"));
                    user.setUsuario(result.getString("usuario"));
//                    user.setSenha(result.getInt("senha"));
                    user.setData_criacao(result.getString("data_criacao"));

                    /*Dados de login a serem armazenados*/
                    SysFact.setUserData(user);
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
//                    alert = new Alert(AlertType.INFORMATION);
//                    alert.setTitle("Mensagem de Erro.");
//                    alert.setHeaderText("Teste");
//                    alert.setContentText("Sucesso!");
//                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Mensagem de Erro.");
                    alert.setHeaderText(null);
                    alert.setContentText("Usuario nao encontrado! ");
                    alert.showAndWait();
                }
                result.close();
                ps.close();
            } catch (SQLException ex) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Mensagem de Erro.");
                alert.setHeaderText(null);
                alert.setContentText("Erro ao se logar-se: " + ex);
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
