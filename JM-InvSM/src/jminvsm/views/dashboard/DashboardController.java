package jminvsm.views.dashboard;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.dashboard.DashboardIndicadores;
import jminvsm.service.dashboard.DashboardResumo;
import jminvsm.service.dashboard.ServiceDashboard;

public class DashboardController implements Initializable {

    private static final Logger LOG = Logger.getLogger(DashboardController.class.getName());

    private ServiceDashboard serviceDashboard;
    private DashboardResumo resumo;
    private ObservableList<Stock> listaLowStock;

    @FXML
    private TableView<Factura> tabelaFactura;
    @FXML
    private TableColumn<Factura, String> cliente;
    @FXML
    private TableColumn<Factura, String> dataemissao;
    @FXML
    private TableColumn<Factura, String> datavencimento;
    @FXML
    private TableColumn<Factura, String> numero;
    @FXML
    private TableColumn<Factura, String> tipo;
    @FXML
    private TableColumn<Factura, Double> total;
    @FXML
    private TableColumn<Factura, Double> totalPago;
    @FXML
    private TableColumn<Factura, Double> saldoPendente;
    @FXML
    private TableColumn<Factura, String> estado;
    @FXML
    private PieChart pieChart;
    @FXML
    private ComboBox<String> combArmazem;
    @FXML
    private ComboBox<String> combCategoria;
    @FXML
    private TableColumn<Stock, String> produto;
    @FXML
    private TableColumn<Stock, Integer> saldo;
    @FXML
    private TableView<Stock> tabelaStock;
    @FXML
    private Label labSaida;
    @FXML
    private Label labEntrada;
    @FXML
    private Label labCliente;
    @FXML
    private Label labFornecedor;
    @FXML
    private Label labTotalProdutos;
    @FXML
    private Label labTotalStock;
    @FXML
    private Label labArmazem;
    @FXML
    private Label labCategoria;
    @FXML
    private Label labDescricao;
    @FXML
    private Label labArmazemStock;
    @FXML
    private Label labSaldo;
    @FXML
    private Label labProdutoStock;
    @FXML
    private Label labTotalFacturas;
    @FXML
    private Label labTotalContaCorrente;
    @FXML
    private Label labTotalCaixa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            serviceDashboard = new ServiceDashboard();
            tabelaStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tabelaFactura.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            configurarTabelas();
            carregarDashboard(true);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Nao foi possivel inicializar o dashboard.", ex);
        }
        updadeLowStock();
        autoUpdade();
    }

    private void configurarTabelas() {
        numero.setCellValueFactory(new PropertyValueFactory<>("numero_fac"));
        tipo.setCellValueFactory(cellData -> new SimpleStringProperty(nomeDocumento(cellData.getValue())));
        cliente.setCellValueFactory(cellData -> new SimpleStringProperty(nomeCliente(cellData.getValue())));
        dataemissao.setCellValueFactory(new PropertyValueFactory<>("data_emissao"));
        datavencimento.setCellValueFactory(new PropertyValueFactory<>("data_vencimento"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalPago.setCellValueFactory(new PropertyValueFactory<>("totalPago"));
        saldoPendente.setCellValueFactory(new PropertyValueFactory<>("saldoPendente"));
        estado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        produto.setCellValueFactory(cellData
                -> new SimpleStringProperty(nomeProduto(cellData.getValue())));
        saldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
    }

    private void carregarDashboard(boolean actualizarCombos) {
        if (serviceDashboard == null) {
            return;
        }
        resumo = serviceDashboard.carregarResumo(combCategoria.getValue(), combArmazem.getValue());
        if (actualizarCombos) {
            combCategoria.setItems(resumo.getCategorias());
            combArmazem.setItems(resumo.getArmazens());
        }
        listaLowStock = resumo.getStockBaixo();
        apresentarFacturas(resumo.getFacturas());
        apresentarStock(resumo.getStock());
        apresentarIndicadores(resumo.getIndicadores());
    }

    private void apresentarFacturas(ObservableList<Factura> facturas) {
        tabelaFactura.setItems(facturas == null ? FXCollections.observableArrayList() : facturas);
    }

    private void apresentarStock(ObservableList<Stock> stock) {
        pieChart.getData().clear();
        tabelaStock.getItems().clear();
        if (stock == null || stock.isEmpty()) {
            return;
        }

        ObservableList<PieChart.Data> dadosGrafico = FXCollections.observableArrayList();
        for (Stock item : stock) {
            dadosGrafico.add(new PieChart.Data(nomeProduto(item), item.getSaldo()));
        }
        pieChart.getData().addAll(dadosGrafico);
        tabelaStock.setItems(stock);

        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            });
        }
    }

    private void apresentarIndicadores(DashboardIndicadores indicadores) {
        if (indicadores == null) {
            return;
        }
        labEntrada.setText(indicadores.getEntradas());
        labSaida.setText(indicadores.getSaidas());
        labCliente.setText(indicadores.getClientes());
        labFornecedor.setText(indicadores.getFornecedores());
        labTotalProdutos.setText(indicadores.getTotalProdutos());
        labTotalStock.setText(indicadores.getTotalStock());
        labArmazem.setText(indicadores.getArmazem());
        labCategoria.setText(indicadores.getCategoria());
        labDescricao.setText(indicadores.getDescricaoCategoria());
        labTotalFacturas.setText(indicadores.getTotalFacturas());
        labTotalContaCorrente.setText(indicadores.getTotalContaCorrente());
        labTotalCaixa.setText(indicadores.getTotalCaixa());
    }

    public void updadeLowStock() {
        if (listaLowStock == null || listaLowStock.isEmpty()) {
            labArmazemStock.setText("-");
            labProdutoStock.setText("Nenhum produto com stock baixo");
            labSaldo.setText("0");
            return;
        }

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                for (Stock stockItem : listaLowStock) {
                    Platform.runLater(() -> {
                        labArmazemStock.setText(nomeArmazem(stockItem));
                        labProdutoStock.setText(nomeProduto(stockItem));
                        labSaldo.setText(String.valueOf(stockItem.getSaldo()));
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }, "dashboard-low-stock");

        thread.setDaemon(true);
        thread.start();
    }

    public void selectedComboBox() {
        carregarDashboard(false);
    }

    public void autoUpdade() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Platform.runLater(() -> carregarDashboard(false));
            }
        }, "dashboard-auto-update");

        thread.setDaemon(true);
        thread.start();
    }

    private String nomeDocumento(Factura factura) {
        return factura == null || factura.getDocComerciais() == null
                ? ""
                : texto(factura.getDocComerciais().getNome_doc());
    }

    private String nomeCliente(Factura factura) {
        return factura == null || factura.getCliente() == null
                ? ""
                : texto(factura.getCliente().getRazao_cli());
    }

    private String nomeProduto(Stock stock) {
        return stock == null || stock.getProduto() == null
                ? ""
                : texto(stock.getProduto().getNome());
    }

    private String nomeArmazem(Stock stock) {
        return stock == null || stock.getArmazem() == null
                ? ""
                : texto(stock.getArmazem().getNome_arm());
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }
}
