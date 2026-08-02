/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package jminvsm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.usuario.Usuario;
import jminvsm.util.SessaoAplicacao;

/**
 *
 * @author JM-Tecnologias
 */
public class SysFact extends Application {

    public static Empresa getEmpresaData() {
        return SessaoAplicacao.getEmpresaData();
    }

    public static void setEmpresaData(Empresa empresaData) {
        SessaoAplicacao.setEmpresaData(empresaData);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static Object getData() {
        return SessaoAplicacao.getData();
    }

    public static void setData(Object data) {
        SessaoAplicacao.setData(data);
    }

    public static Usuario getUserData() {
        return SessaoAplicacao.getUserData();
    }

    public static void setUserData(Usuario userData) {
        SessaoAplicacao.setUserData(userData);
    }

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/jminvsm/views/login/login.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../sysfact/views/modulo_venda/clienteView/clienteView.fxml"));

        Scene scene = new Scene(root);

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
//      Remover os botoes de close, maximize e minimize
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
