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

/**
 *
 * @author JM-Tecnologias
 */
public class SysFact extends Application {

    private static Usuario userData;
    private static Object data;
    private static Empresa empresaData;

    public static Empresa getEmpresaData() {
        return empresaData;
    }

    public static void setEmpresaData(Empresa empresaData) {
        SysFact.empresaData = empresaData;
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
        return data;
    }

    public static void setData(Object data) {
        SysFact.data = data;
    }

    public static Usuario getUserData() {
        return userData;
    }

    public static void setUserData(Usuario userData) {
        SysFact.userData = userData;
    }

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
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
