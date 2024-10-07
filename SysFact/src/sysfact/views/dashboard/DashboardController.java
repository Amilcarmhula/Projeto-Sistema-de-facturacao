/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.dashboard;

import java.net.URL;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import sysfact.dao.stock.StockDAO;
import sysfact.model.stock.Stock;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class DashboardController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showStock();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<Stock> listaStock;

    public void showStock() throws SQLException {
        StockDAO x = new StockDAO();
        listaStock = x.getStock();
        XYChart.Series<String, Number> dados = new XYChart.Series<>();
        dados.setName("Contole de Stock");
        for (Stock s : listaStock) {
            dados.getData().add(new XYChart.Data<>(s.getProduto().getNome(), s.getSaldo()));
        }
        barChart.setCategoryGap(20);
        barChart.getData().addAll(dados);
        // Para cada dado, adicionar o valor como rótulo em cima da barra
        for (XYChart.Data<String, Number> data : dados.getData()) {
            // Criar um label com o valor Y
            Text label = new Text(String.valueOf(data.getYValue()));
            
            // Estilizando o rótulo
            label.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-fill: #000;");

            // Criar um StackPane para sobrepor o label na barra
            StackPane node = (StackPane) data.getNode();
            node.getChildren().add(label);

            // Ajustar a posição do label para que fique sobre a barra
            node.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                label.setTranslateY(-10); // Ajuste de acordo com a altura da barra
            });
        }
    }

}
