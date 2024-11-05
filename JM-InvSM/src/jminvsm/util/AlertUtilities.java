/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author JM-Tecnologias
 */
public class AlertUtilities {

    public static void showDialog2(String imagePath, String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Carrega a imagem a partir do caminho fornecido
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        // Ajusta o tamanho da imagem
        imageView.setFitWidth(50); // Largura da imagem
        imageView.setFitHeight(50); // Altura da imagem
        // Define a imagem como o ícone gráfico do alerta
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

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

    public static void showErroNotification(String titulo, String mensagem) {
        // Carrega a imagem a partir do caminho fornecido
        Image image = new Image("/jminvsm/image/icons/red-erro.png");
        ImageView imageView = new ImageView(image);

        // Ajusta o tamanho da imagem
//        imageView.setFitWidth(50); // Largura da imagem
//        imageView.setFitHeight(50); // Altura da imagem
        Notifications not = Notifications.create();
        not.graphic(imageView);
        not.title(titulo);
        not.text(mensagem);
        not.hideAfter(Duration.seconds(10));
        not.darkStyle();
        not.position(Pos.BOTTOM_RIGHT);
        not.show();
    }

    public static void showSuccessNotification(String titulo, String mensagem) {
//        Image image = new Image("/image/danger.png");
        Notifications not = Notifications.create();
//        not.graphic(new ImageView(image));
        not.title(titulo);
        not.text(mensagem);
        not.hideAfter(Duration.seconds(10));
//        not.darkStyle();
        not.position(Pos.BOTTOM_RIGHT);
        not.show();
    }
}
