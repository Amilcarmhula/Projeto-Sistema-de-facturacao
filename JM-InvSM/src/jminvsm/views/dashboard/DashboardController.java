/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.dashboard;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.caixa.ServiceCaixa;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.contacorrente.ServiceContaCorrente;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import jminvsm.service.stock.ServiceStock;
import jminvsm.service.vendas.factura.ServiceFactura;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class DashboardController implements Initializable {
    private ServiceFactura serviceFactura;
    private ServiceCliente serviceCliente;
    private ServiceStock serviceStock;
    private ServicoCategoria serviceCategoria;
    private ServiceArmazem serviceArmazem;
    private ServiceProduto serviceProduto;
    private ServiceFornecedor serviceFornecedor;
    private ServiceProdutoFornecido servProdFornecido;
    private ServiceCaixa serviceCaixa;
    private ServiceContaCorrente serviceContaCorrente;
    

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
            serviceFactura = new ServiceFactura();
            serviceCliente = new ServiceCliente();
            serviceStock = new ServiceStock();
            serviceCategoria = new ServicoCategoria();
            serviceArmazem = new ServiceArmazem();
            serviceFornecedor = new ServiceFornecedor();
            servProdFornecido = new ServiceProdutoFornecido();
            serviceCaixa = new ServiceCaixa();
            serviceContaCorrente = new ServiceContaCorrente();
            serviceProduto = new ServiceProduto();
            
            showStock();
            showMovimentos();
            showCountCliente();
            showCountFornecedor();
            mapCategorias();
            mapArmazens();
            showFacturas();
            showCountProdutos();
            showCountStock();
            loadLowStockBalance();
            labArmazem.setText(combArmazem.getValue() == null ? "Todos..." : combArmazem.getValue());
            labCategoria.setText(combCategoria.getValue() == null ? "Todas..." : combArmazem.getValue());
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updadeLowStock();
        autoUpdade();
    }
    private ObservableList<Stock> listaLowStock;

    public void loadLowStockBalance() {
        listaLowStock = serviceStock.stockBaixo();
    }

    public void updadeLowStock() {
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    for (Stock stock : listaLowStock) {

                        try {
                            Platform.runLater(() -> {

                                labArmazemStock.setText(stock.getArmazem().getNome_arm());
                                labProdutoStock.setText(stock.getProduto().getNome());
                                labSaldo.setText(stock.getSaldo() + "");
                            });
                            // A thread dorme por 5 segundo
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            // Trata uma possível interrupção da thread
                            System.out.println("Erro: " + ex);
                        }
                    }
                }
//                System.out.println("\nFim");
//                updadeLowStock();
            }
        });
        // Inicia a execução da thread
        x.start();
    }
    private ObservableList<Categoria> listaFamilias;
    private Map<String, Categoria> mapaCategorias;

    public void mapCategorias() {
        listaFamilias = serviceCategoria.listaCategorias();
        mapaCategorias = new HashMap<>();
        List<String> lista = new ArrayList<>();
        lista.add("Todas...");
        for (Categoria categoria : listaFamilias) {
            mapaCategorias.put(categoria.getFamilia(), categoria);
            if (!"Serviço".equals(categoria.getFamilia())) {
                lista.add(categoria.getFamilia());
            }
        }
        combCategoria.setItems(FXCollections.observableArrayList(lista));
    }

    private ObservableList<Armazem> listaArmazens;
    private Map<String, Armazem> mapaArmazens;

    public void mapArmazens() {
        listaArmazens = serviceArmazem.listaArmazens();
        mapaArmazens = new HashMap<>();
        List<String> lista = new ArrayList<>();
        lista.add("Todos...");
        for (Armazem armazem : listaArmazens) {
            mapaArmazens.put(armazem.getNome_arm(), armazem);
            lista.add(armazem.getNome_arm());
        }
        combArmazem.setItems(FXCollections.observableArrayList(lista));
    }
    private ObservableList<Factura> listaFacturas;

    public void showFacturas() {
        listaFacturas = serviceFactura.exibeFactura();

        numero.setCellValueFactory(new PropertyValueFactory<>("numero_fac"));
        tipo.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDocComerciais().getNome_doc()));
        cliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getCliente().getRazao_cli()));
        dataemissao.setCellValueFactory(new PropertyValueFactory<>("data_emissao"));
        datavencimento.setCellValueFactory(new PropertyValueFactory<>("data_vencimento"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalPago.setCellValueFactory(new PropertyValueFactory<>("totalPago"));
        saldoPendente.setCellValueFactory(new PropertyValueFactory<>("saldoPendente"));
        estado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabelaFactura.setItems(listaFacturas);
    }

    private ObservableList<Stock> listaStock;

    public void showStock(){
        // Tratando valores nulos ou vazios nos combos
        Integer cat = null;
        Integer arm = null;

        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
        }

        if (combArmazem.getValue() != null && !combArmazem.getValue().isEmpty() && !combArmazem.getValue().equals("Todos...")) {
            Armazem a = mapaArmazens.get(combArmazem.getValue());
            arm = a.getId();
        }
        listaStock = serviceStock.consultaStock(cat, arm);

        // Limpar dados antigos do gráfico e da tabela
        pieChart.getData().clear();
        tabelaStock.getItems().clear();

        // Verificar se a lista de estoque está vazia
        if (listaStock == null || listaStock.isEmpty()) {
//            AlertUtilities.showDialog(FontAwesomeIcon.INFO, "Aviso", "Nenhum produto encontrado.");
            return;
        }
        ObservableList<PieChart.Data> lista = FXCollections.observableArrayList();
        for (Stock s : listaStock) {
            PieChart.Data dados = new PieChart.Data(s.getProduto().getNome(), s.getSaldo());
            lista.add(dados);
        }
        // Adicionando dados ao gráfico de pizza
        pieChart.getData().addAll(lista);

        //Adiciona os dados na tabela
        produto.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getNome())));
        saldo.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("saldo"));
        tabelaStock.setItems(listaStock);

        // Aplicando efeito de hover para destacar fatias
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
//                data.getNode().setStyle("-fx-scale-x: 1.1; -fx-scale-y: 1.1;");
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
//                data.getNode().setStyle("-fx-scale-x: 1; -fx-scale-y: 1;");
            });
        }
    }
    private Map<String, Integer> mapaMovimentos;

    public void showMovimentos(){
        // Tratando valores nulos ou vazios nos combos
        Integer cat = null;
        Integer arm = null;

        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
        }

        if (combArmazem.getValue() != null && !combArmazem.getValue().isEmpty() && !combArmazem.getValue().equals("Todos...")) {
            Armazem a = mapaArmazens.get(combArmazem.getValue());
            arm = a.getId();
        }
        mapaMovimentos = servProdFornecido.consultaMovimento(cat, arm);
        if (!mapaMovimentos.isEmpty()) {
            for (Map.Entry<String, Integer> entry : mapaMovimentos.entrySet()) {
                if (entry.getKey().equals("entradas")) {
                    labEntrada.setText(entry.getValue() + "");
                }
                if (entry.getKey().equals("saidas")) {
                    labSaida.setText(entry.getValue() + "");
                }
            }
        } else {
            labEntrada.setText("0");
            labSaida.setText("0");
        }
    }

    public void showCountCliente(){
        labCliente.setText(serviceCliente.contaClientes() + "");
    }

    public void showCountFornecedor() {
        labFornecedor.setText(serviceFornecedor.contaFornecedores()+ "");
    }

    public void showCountProdutos(){
        Integer cat = null;
        if (combCategoria.getValue() == null || combCategoria.getValue().equals("Todas...")) {
            labCategoria.setText("Todas...");
            labDescricao.setText("A categoria inclui produtos de todas categorias existentes!");
            labTotalProdutos.setText("Existem " + serviceProduto.contaProdutos(cat) + " produtos em todas categorias.");
        }
        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
            labDescricao.setText(c.getDescricao());
            labCategoria.setText(combCategoria.getValue());
            labTotalProdutos.setText("Existem " + serviceProduto.contaProdutos(cat) + " produtos na categoria " + combCategoria.getValue() + ".");
        }
    }

    public void showCountStock(){
        Integer cat = null;
        Integer arm = null;
        if (combCategoria.getValue() == null || combCategoria.getValue().equals("Todas...")) {
            labCategoria.setText("Todas...");
            labTotalStock.setText("Com " + serviceStock.contaSaldoStock(cat, arm) + " itens em stock no armazem selecionado");
        }
        if (combArmazem.getValue() == null || combArmazem.getValue().equals("Todos...")) {
            labArmazem.setText("Todos...");
            labTotalStock.setText("Com " + serviceStock.contaSaldoStock(cat, arm)  + " itens em stock no em todos armazens.");
        }
        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
            labCategoria.setText(combCategoria.getValue());
            labTotalStock.setText("Com " + serviceStock.contaSaldoStock(cat, arm)  + " itens em stock na categoria " + combCategoria.getValue() + " em todos armazens.");
        }

        if (combArmazem.getValue() != null && !combArmazem.getValue().isEmpty() && !combArmazem.getValue().equals("Todos...")) {
            Armazem a = mapaArmazens.get(combArmazem.getValue());
            arm = a.getId();
            labArmazem.setText(combArmazem.getValue());
            labTotalStock.setText("Com " + serviceStock.contaSaldoStock(cat, arm)  + " itens em stock no " + combArmazem.getValue() + ".");
        }

    }

    public void showCountInvoice() {
        labTotalFacturas.setText(serviceFactura.contaFacturas() + "");
    }

    public void showTotalInvoicePaid() {
        
        labTotalCaixa.setText(String.format("%.2f", serviceCaixa.consultaCaixa()) + " Mt");
        labTotalContaCorrente.setText(String.format("%.2f", serviceContaCorrente.consultaContaCorrente()) + " Mt");
    }

    public void selectedComboBox(){
        showMovimentos();
        showStock();
        showCountProdutos();
        showCountStock();
    }

    public void showInvoice(MouseEvent evt) throws SQLException{
        if (evt.getClickCount() == 2) {
            Factura f = tabelaFactura.getSelectionModel().getSelectedItem();
            f.criarFactura(f.getNumero_fac());
        }

    }

//    public void autoUpdade() {
//        Thread x = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 5; i++) {
//                    try {
//                        // Actualiza os dados a cada 10 segundos
//                        showStock();
//                        showMovimentos();
//                        showCountCliente();
//                        showCountFornecedor();
//                        mapCategorias();
//                        mapArmazens();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    try {
//                        // A thread dorme por 1 segundo
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        // Trata uma possível interrupção da thread
//                        System.out.println("Erro: " + ex);
//                    }
//                }
//                autoUpdade();
//                // Imprime a mensagem "Fim" após o término do loop
//                System.out.println("\nFim");
//            }
//        });
//
//        // Inicia a execução da thread
//        x.start();
//    }
    public void autoUpdade() {
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                try {
                    // Actualiza os dados a cada 10 segundos
                    Platform.runLater(() -> { //To update UI elements from a background thread
                            showStock(); // This as an UI component
                            showMovimentos();
                            showCountCliente();
                            showCountFornecedor();
                            showCountInvoice();
                            showTotalInvoicePaid();
                            mapCategorias();
                            mapArmazens();
                    });

                    // A thread dorme por 1 minuto
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    // Trata uma possível interrupção da thread
                    System.out.println("Erro: " + ex);
                }
                }
//                autoUpdade();
                // Imprime a mensagem "Fim" após o término do loop
//                System.out.println("\nFim");
            }
        });

        // Inicia a execução da thread
        x.start();
    }

}
