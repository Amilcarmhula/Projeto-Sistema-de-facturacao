/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.dao.categoria.CategoriaDAO;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.fornecedor.FornecedorDAO;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.dao.produto.fornecido.ProdutoFornecidoDAO;
import sysfact.dao.stock.StockDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.categoria.Categoria;
import sysfact.model.stock.Stock;
import sysfact.model.vendas.factura.Factura;
import sysfact.service.vendas.factura.ServiceFactura;
import sysfact.util.AlertUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class DashboardController implements Initializable {

    @FXML
    private TableView<Factura> tabelaFactura;

    @FXML
    private TableColumn<Factura, String> cliente;

    @FXML
    private TableColumn<Factura, String> dataemissao;

    @FXML
    private TableColumn<Factura, String> datavencimento;

    @FXML
    private TableColumn<Factura, Boolean> estado;

    @FXML
    private TableColumn<Factura, String> numero;

    @FXML
    private TableColumn<Factura, String> tipo;

    @FXML
    private TableColumn<Factura, String> valor;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            showStock();
            showMovimentos();
            showCountCliente();
            showCountFornecedor();
            mapCategorias();
            mapArmazens();
            showFacturas();
            showCountProdutos();
            showCountStock();
            labArmazem.setText(combArmazem.getValue() == null ? "Todos..." : combArmazem.getValue());
            labCategoria.setText(combCategoria.getValue() == null ? "Todas..." : combArmazem.getValue());
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        autoUpdade();
    }
    private ObservableList<Categoria> listaFamilias;
    private Map<String, Categoria> mapaCategorias;

    public void mapCategorias() throws SQLException {
        CategoriaDAO x = new CategoriaDAO();
        listaFamilias = x.listAllEntities();
        mapaCategorias = new HashMap<>();
        List<String> lista = new ArrayList<>();
        lista.add("Todas...");
        for (Categoria categoria : listaFamilias) {
            mapaCategorias.put(categoria.getFamilia(), categoria);
            if (!"Serviço".equals(categoria.getTipo())) {
                lista.add(categoria.getFamilia());
            }
        }
        combCategoria.setItems(FXCollections.observableArrayList(lista));
    }

    private ObservableList<Armazem> listaArmazens;
    private Map<String, Armazem> mapaArmazens;

    public void mapArmazens() throws SQLException {
        ArmazemDAO x = new ArmazemDAO();
        listaArmazens = x.listAllEntities();
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

    public void showFacturas() throws SQLException {
        ServiceFactura sf = new ServiceFactura();
        listaFacturas = sf.exibeFactura();

        numero.setCellValueFactory(new PropertyValueFactory<>("numero_fac"));
        tipo.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDocComerciais().getNome_doc()));
        cliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getCliente().getRazao_cli()));
        dataemissao.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
        datavencimento.setCellValueFactory(new PropertyValueFactory<>("data_vencimento"));
        valor.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getItem().getSubtotal())));
        estado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabelaFactura.setItems(listaFacturas);
    }

    private ObservableList<Stock> listaStock;

    public void showStock() throws SQLException {
        StockDAO x = new StockDAO();
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
        listaStock = x.getStock(cat, arm);

        // Limpar dados antigos do gráfico e da tabela
        pieChart.getData().clear();
        tabelaStock.getItems().clear();

        // Verificar se a lista de estoque está vazia
        if (listaStock == null || listaStock.isEmpty()) {
            AlertUtilities.showDialog(FontAwesomeIcon.INFO, "Aviso", "Nenhum produto encontrado.");
            return;
        }
        ObservableList<PieChart.Data> lista = FXCollections.observableArrayList();
        for (Stock s : listaStock) {
            PieChart.Data dados = new PieChart.Data(s.getProduto().getNomeProduto(), s.getSaldo());
            lista.add(dados);
        }
        // Adicionando dados ao gráfico de pizza
        pieChart.getData().addAll(lista);

        //Adiciona os dados na tabela
        produto.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getNomeProduto())));
        saldo.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("saldo"));
        tabelaStock.setItems(listaStock);

        // Aplicando efeito de hover para destacar fatias
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                data.getNode().setStyle("-fx-scale-x: 1.1; -fx-scale-y: 1.1;");
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                data.getNode().setStyle("-fx-scale-x: 1; -fx-scale-y: 1;");
            });
        }
    }
    private Map<String, Integer> mapaMovimentos;

    public void showMovimentos() throws SQLException {
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
        mapaMovimentos = new ProdutoFornecidoDAO().getMovimentos(cat, arm);
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

    public void showCountCliente() throws SQLException {
        labCliente.setText(new ClienteDAO().CountClientes() + "");
    }

    public void showCountFornecedor() throws SQLException {
        labFornecedor.setText(new FornecedorDAO().CountFornecedor() + "");
    }

    public void showCountProdutos() throws SQLException {
        Integer cat = null;
        if (combCategoria.getValue() == null || combCategoria.getValue().equals("Todas...")) {
            labCategoria.setText("Todas...");
            labDescricao.setText("A categoria inclui produtos de todas categorias existentes!");
            labTotalProdutos.setText("Existem "+new ProdutoDAO().countProdutos(cat) + " produtos em todas categorias.");
        }
        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
            labDescricao.setText(c.getDescricao());
            labCategoria.setText(combCategoria.getValue());
            labTotalProdutos.setText("Existem "+new ProdutoDAO().countProdutos(cat) + " produtos na categoria "+combCategoria.getValue()+".");
        }
    }

    public void showCountStock() throws SQLException {
        Integer cat = null;
        Integer arm = null;
        if (combCategoria.getValue() == null || combCategoria.getValue().equals("Todas..."))  {
            labCategoria.setText("Todas...");
            labTotalStock.setText("Com "+new StockDAO().countStock(cat, arm) + " itens em stock no armazem selecionado");
        }
        if (combArmazem.getValue() == null || combArmazem.getValue().equals("Todos...")) {
            labArmazem.setText("Todos...");
            labTotalStock.setText("Com "+new StockDAO().countStock(cat, arm) + " itens em stock no em todos armazens.");
        }
        if (combCategoria.getValue() != null && !combCategoria.getValue().isEmpty() && !combCategoria.getValue().equals("Todas...")) {
            Categoria c = mapaCategorias.get(combCategoria.getValue());
            cat = c.getId();
            labCategoria.setText(combCategoria.getValue());
            labTotalStock.setText("Com "+new StockDAO().countStock(cat, arm) + " itens em stock na categoria "+combCategoria.getValue()+" em todos armazens.");
        }

        if (combArmazem.getValue() != null && !combArmazem.getValue().isEmpty() && !combArmazem.getValue().equals("Todos...")) {
            Armazem a = mapaArmazens.get(combArmazem.getValue());
            arm = a.getId();
            labArmazem.setText(combArmazem.getValue());
            labTotalStock.setText("Com "+new StockDAO().countStock(cat, arm) + " itens em stock no "+combArmazem.getValue()+".");
        }
        
    }

    public void selectedComboBox() throws SQLException {
        showMovimentos();
        showStock();
        showCountProdutos();
        showCountStock();
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
                try {
                    // Actualiza os dados a cada 10 segundos
                    Platform.runLater(() -> { //To update UI elements from a background thread
                        try {
                            showStock(); // This as an UI component
                            showMovimentos();
                            showCountCliente();
                            showCountFornecedor();
                            mapCategorias();
                            mapArmazens();
                        } catch (SQLException ex) {
                            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    // A thread dorme por 10 segundos
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    // Trata uma possível interrupção da thread
                    System.out.println("Erro: " + ex);
                }
//                }
                autoUpdade();
                // Imprime a mensagem "Fim" após o término do loop
                System.out.println("\nFim");
            }
        });

        // Inicia a execução da thread
        x.start();
    }

}
