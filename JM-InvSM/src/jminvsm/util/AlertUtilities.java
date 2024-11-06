/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author JM-Tecnologias
 */
public class AlertUtilities {

    private static Label labNotification;

    public static void showDialog(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Carrega a imagem a partir do caminho fornecido
        Image image = new Image("/jminvsm/image/icons/alert.png");
        ImageView imageView = new ImageView(image);
        // Ajusta o tamanho da imagem
        imageView.setFitWidth(50); // Largura da imagem
        imageView.setFitHeight(50); // Altura da imagem
        // Define a imagem como o ícone gráfico do alerta
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    /*
    ActionEvent aevent, MouseEvent mevent) {
        if (aevent != null) {
            Node source = (Node) aevent.getSource();
            LoadAndMoveUtilities.baseAnchorPane = (AnchorPane) source.getScene().lookup("#anchorMain");
     */
//    public static void showErroAlert(ActionEvent event, String mensagem) {
//        Node source = (Node) event.getSource();
//        AlertUtilities.labNotification = (Label) source.getScene().lookup("#labNotification");
//        labNotification.setText("Erro: "+mensagem);
//    }
    public static void showErroAlert( String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        // Carrega a imagem a partir do caminho fornecido
        Image image = new Image("/jminvsm/image/icons/alert.png");
        ImageView imageView = new ImageView(image);
        // Ajusta o tamanho da imagem
        imageView.setFitWidth(50); // Largura da imagem
        imageView.setFitHeight(50); // Altura da imagem
        // Define a imagem como o ícone gráfico do alerta
        alert.setGraphic(imageView);
        alert.showAndWait();
    }
    public static void showSuccessAlert( String mensagem){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        // Carrega a imagem a partir do caminho fornecido
        Image image = new Image("/jminvsm/image/icons/alert.png");
        ImageView imageView = new ImageView(image);
        // Ajusta o tamanho da imagem
        imageView.setFitWidth(50); // Largura da imagem
        imageView.setFitHeight(50); // Altura da imagem
        // Define a imagem como o ícone gráfico do alerta
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

//    public static void showErroAlert(String titulo, String mensagem) {
//        // Carrega a imagem a partir do caminho fornecido
//        Image image = new Image("/jminvsm/image/icons/red-erro.png");
//        ImageView imageView = new ImageView(image);
//
//        // Ajusta o tamanho da imagem
////        imageView.setFitWidth(50); // Largura da imagem
////        imageView.setFitHeight(50); // Altura da imagem
//        Notifications not = Notifications.create();
//        not.graphic(imageView);
//        not.title(titulo);
//        not.text(mensagem);
//        not.hideAfter(Duration.seconds(10));
//        not.darkStyle();
//        not.position(Pos.BOTTOM_RIGHT);
//        not.show();
//    }

//    public static void showSuccessAlert(String titulo, String mensagem) {
////        Image image = new Image("/image/danger.png");
//        Notifications not = Notifications.create();
////        not.graphic(new ImageView(image));
//        not.title(titulo);
//        not.text(mensagem);
//        not.hideAfter(Duration.seconds(10));
////        not.darkStyle();
//        not.position(Pos.BOTTOM_RIGHT);
//        not.show();
//    }
}
