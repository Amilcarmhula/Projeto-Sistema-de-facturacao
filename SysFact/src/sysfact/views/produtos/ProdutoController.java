/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.produtos;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.dao.preco.PrecoVendaDAO;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.dao.produto.fornecido.ProdutoFornecidoDAO;
import sysfact.dao.taxaimposto.TaxaImpostoDAO;
import sysfact.dao.unidademedida.UnidadeMedidaDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.categoria.Categoria;
import sysfact.model.cliente.Cliente;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.produto.fornecido.ProdutoFornecido;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.model.usuario.Usuario;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ProdutoController implements Initializable {

    private Usuario userData;

    @FXML
    private AnchorPane anchorProdutos;

    @FXML
    private Label labID;

    @FXML
    private TextField ps_txtpesquisar;

    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TableColumn<Produto, Integer> IDtabelaProduto;
    @FXML
    private TableColumn<Produto, String> IDCatabelaProduto;
    @FXML
    private TableColumn<Produto, String> produtotabelaProduto;
    @FXML
    private TableColumn<Produto, String> tipotabelaProduto;
    @FXML
    private TableColumn<Produto, Double> precotabelaProduto;
    @FXML
    private TableColumn<Produto, String> codigobarrastabelaProduto;
    @FXML
    private TableColumn<Produto, String> taxatabelaProduto;
    @FXML
    private TableColumn<Produto, String> unidademedidatabelaProduto;
    @FXML
    private TableColumn<Produto, String> descricaotabelaProduto;
    @FXML
    private TableColumn<Produto, String> btntabelaProduto;

    @FXML
    private TextField ps_txtpesquisaProduto;
    @FXML
    private TextField ps_txtIDProduto;
    @FXML
    private TextField ps_txtNomeProduto;
    @FXML
    private TextField ps_txtprecoProduto;
    @FXML
    private TextField ps_txtcodigobarrasProduto;
    @FXML
    private ComboBox<String> ps_combunidademedidaProduto;
    @FXML
    private TextArea ps_txtdescricaoProduto;
    @FXML
    private Button btnAddProduto;
    @FXML
    private Button btnUpdateProduto;

    @FXML
    private TableView<Cliente> tabelaCliente;

    @FXML
    private TableColumn<Cliente, Integer> IDtabelaCliente;

    @FXML
    private TableColumn<Cliente, String> nometabelaCliente;

    @FXML
    private TableColumn<Cliente, String> apelidotabelaCliente;

    @FXML
    private TableColumn<Cliente, Integer> nuittabelaCliente;

    @FXML
    private TextField ps_txtPesquisafornecedor;

    @FXML
    private TextField ps_txtIDFornecedor;

    @FXML
    private DatePicker ps_dataPicker;

    @FXML
    private TableView<Armazem> tabelaArmazem;

    @FXML
    private TableColumn<Armazem, Integer> IDtabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> armazemtabelaArmazem;

    @FXML
    private TableColumn<Armazem, String> cidadetabelaArmazem;

    @FXML
    private TableColumn<Armazem, Integer> referenciatabelaArmazem;

    @FXML
    private TextField ps_txtPesquisaArmazem;
    @FXML
    private TextField ps_txtIDArmazem;

    @FXML
    private TextField ps_txtIDCategoria;
    @FXML
    private TextField ps_txtgrupoCategoria;
    @FXML
    private TextField ps_txtsubgrupoCategoria;
    @FXML
    private TextField ps_txtfamiliaCategoria;

    @FXML
    private DatePicker date_inicio;
    @FXML
    private DatePicker date_fim;
    @FXML
    private TextField ps_txtIDPreco;

    @FXML
    private Button btnEntradaProdutos;
    @FXML
    private DatePicker ps_dataAquisicao;
    @FXML
    private TextField ps_txtCustoAquisicao;
    @FXML
    private TextField ps_txtQuantidade;
    @FXML
    private TableView<ProdutoFornecido> tabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, Integer> IDtabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, String> produtotabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, String> fornecedortabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, String> armazemtabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, Integer> quantidadetabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, Double> custotabelaProdutoFornecido;
    @FXML
    private TableColumn<ProdutoFornecido, String> datatabelaProdutoFornecido;

    public void getCategoriaView() {
        try {
            Parent parente = FXMLLoader.load(getClass().getResource("../produtos/categoria/categoria.fxml"));
            Scene scene = new Scene(parente);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getArmazemView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("../armazem/armazem.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void getData() {
        ps_txtgrupoCategoria.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    Categoria x = (Categoria) SysFact.getData();
                    if (x != null) {
                        ps_txtIDCategoria.setText(String.valueOf(x.getId()));
                        ps_txtgrupoCategoria.setText(String.valueOf(x.getGrupo()));
                        ps_txtsubgrupoCategoria.setText(String.valueOf(x.getSubgrupo()));
                        ps_txtfamiliaCategoria.setText(String.valueOf(x.getFamilia()));
                    }

                }
            }
        });

        ps_txtsubgrupoCategoria.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    Categoria x = (Categoria) SysFact.getData();
                    if (x != null) {
                        ps_txtIDCategoria.setText(String.valueOf(x.getId()));
                        ps_txtgrupoCategoria.setText(String.valueOf(x.getGrupo()));
                        ps_txtsubgrupoCategoria.setText(String.valueOf(x.getSubgrupo()));
                        ps_txtfamiliaCategoria.setText(String.valueOf(x.getFamilia()));
                    }
                }
            }
        });

        ps_txtfamiliaCategoria.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    Categoria x = (Categoria) SysFact.getData();
                    if (x != null) {
                        ps_txtIDCategoria.setText(String.valueOf(x.getId()));
                        ps_txtgrupoCategoria.setText(String.valueOf(x.getGrupo()));
                        ps_txtsubgrupoCategoria.setText(String.valueOf(x.getSubgrupo()));
                        ps_txtfamiliaCategoria.setText(String.valueOf(x.getFamilia()));
                    }
                }
            }
        });
    }

    public void priceFormaterProduto() {
        // Define the Locale for currency formatting (Mozambique)
        Locale locale = new Locale("pt", "MZ");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Create a TextFormatter to format the input as currency
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            // Remove all non-numeric characters
            String newText = change.getControlNewText().replaceAll("[^\\d]", "");

            if (newText.isEmpty()) {
                newText = "0";
            }

            try {
                // Convert to a number and format as currency
                double value = Double.parseDouble(newText) / 100.0;
                change.setText(currencyFormat.format(value));
                change.setRange(0, change.getControlText().length()); // Replace the whole text
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }

            return change;
        });

        // Attach the TextFormatter to the TextField
        ps_txtprecoProduto.setTextFormatter(textFormatter);
        ps_txtprecoProduto.setText(currencyFormat.format(0)); // Set initial value

    }

    public void priceFormaterProdutoFornecido() {
        // Define the Locale for currency formatting (Mozambique)
        Locale locale = new Locale("pt", "MZ");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Create a TextFormatter to format the input as currency
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            // Remove all non-numeric characters
            String newText = change.getControlNewText().replaceAll("[^\\d]", "");

            if (newText.isEmpty()) {
                newText = "0";
            }

            try {
                // Convert to a number and format as currency
                double value = Double.parseDouble(newText) / 100.0;
                change.setText(currencyFormat.format(value));
                change.setRange(0, change.getControlText().length()); // Replace the whole text
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }

            return change;
        });

        // Attach the TextFormatter to the TextField
        ps_txtCustoAquisicao.setTextFormatter(textFormatter);
        ps_txtCustoAquisicao.setText(currencyFormat.format(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        getData();
        priceFormaterProduto();
        priceFormaterProdutoFornecido();

        // Configura o TextArea para quebra automática de linha
        ps_txtdescricaoProduto.setWrapText(true);

        // Opcional: configurar a largura máxima do texto
        //ps_txtdescricaoProduto.setPrefColumnCount(30); // Ajuste conforme necessário
        try {
            showProduto();
            showFornecedores();
            showArmazem();
            showProdutoFornecido();

            mapearUnidadeMedidas();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mapearUnidadeMedidas() throws SQLException {
        UnidadeMedidaDAO umd = new UnidadeMedidaDAO();
        Map<String, Integer> mapa_UnidadeMedida = umd.mapUnidadeMedida();
        List<String> lista = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mapa_UnidadeMedida.entrySet()) {
//            Integer key = entry.getKey();
//            String val = entry.getValue();
            lista.add(entry.getKey());
//            System.out.println("Chave:" + key + " Valor:" + val);
            ps_combunidademedidaProduto.setItems(FXCollections.observableArrayList(lista));
        }
    }

    /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Produto> listaProduto;

    public void showProduto() throws SQLException {
        ProdutoDAO aDAO = new ProdutoDAO();
        listaProduto = aDAO.listAllEntities2();

        IDtabelaProduto.setCellValueFactory(new PropertyValueFactory<>("id"));
        IDCatabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCategoria().getId())));
        produtotabelaProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigobarrastabelaProduto.setCellValueFactory(new PropertyValueFactory<>("codigo_barra"));
        descricaotabelaProduto.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tipotabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getCategoria().getTipo()));

        unidademedidatabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getUnidade_medida().getSigla()));

        //add cell of button edit 
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFoctory = (TableColumn<Produto, String> param) -> {
            // make cell containing buttons
            final TableCell<Produto, String> cell = new TableCell<Produto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:linear-gradient(to bottom right, #00ff004d 0%, #0000ff80 50%, #ff00004d 100%);"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Produto a = tabelaProduto.getSelectionModel().getSelectedItem();
                            try {
                                ProdutoDAO xDAO = new ProdutoDAO();
                                xDAO.deleteEntityByUSER(a);
                                resetProduto();
                                showProduto();
                            } catch (SQLException ex) {
                                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaProduto.setCellFactory(cellFoctory);

        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);
        ps_txtpesquisaProduto.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getCodigo_barra().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getUnidade_medida()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getDescricao()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Produto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());

        tabelaProduto.setItems(sortedData);
    }

    private ObservableList<Cliente> listaAgentes;

    public void showFornecedores() throws SQLException {

        ClienteDAO aDAO = new ClienteDAO();
//        listaAgentes = aDAO.listAllEntitiesTypeFornecedor();

        IDtabelaCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        nometabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        apelidotabelaCliente.setCellValueFactory(new PropertyValueFactory<>("apelido"));
        nuittabelaCliente.setCellValueFactory(new PropertyValueFactory<>("nuit"));

        FilteredList<Cliente> dadosFiltrados = new FilteredList<>(listaAgentes, b -> true);
        ps_txtPesquisafornecedor.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(agente -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (agente.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (agente.getApelido().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(agente.getNuit()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Cliente> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCliente.comparatorProperty());

//        tabelaCliente.setItems(listaAgentes);
        tabelaCliente.setItems(sortedData);
    }

    private ObservableList<Armazem> listaArmazens;

    public void showArmazem() throws SQLException {

        ArmazemDAO aDAO = new ArmazemDAO();
        listaArmazens = aDAO.listAllEntities();

        IDtabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("id"));
        armazemtabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cidadetabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        referenciatabelaArmazem.setCellValueFactory(new PropertyValueFactory<>("numero"));

        FilteredList<Armazem> dadosFiltrados = new FilteredList<>(listaArmazens, b -> true);
        ps_txtPesquisaArmazem.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getDescricao().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getDescricao()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Armazem> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaArmazem.comparatorProperty());

//        tabelaCliente.setItems(listaAgentes);
        tabelaArmazem.setItems(sortedData);
    }

    public void showPreco() throws SQLException {
        PrecoVendaDAO xDAO = new PrecoVendaDAO();
        PrecoVenda pv = new PrecoVenda();
        Produto p = new Produto();
        p.setId(Integer.parseInt(ps_txtIDProduto.getText()));
        pv.setProduto(p);

        PrecoVenda returnPrecoVenda = xDAO.getEntityByProdutoID(pv);
        if (returnPrecoVenda != null) {
            date_inicio.setValue(LocalDate.parse(returnPrecoVenda.getData_inicio()));
            date_fim.setValue(LocalDate.parse(returnPrecoVenda.getData_fim()));
            ps_txtprecoProduto.setText(String.valueOf(returnPrecoVenda.getPreco() + "0"));
            ps_txtIDPreco.setText(String.valueOf(returnPrecoVenda.getId()));
        } else {
            date_inicio.setValue(LocalDate.now());
            date_fim.setValue(LocalDate.now());
            ps_txtprecoProduto.setText("");
            ps_txtIDPreco.setText("");

        }

    }

    private ObservableList<ProdutoFornecido> listaProdutosFornecidos;

    public void showProdutoFornecido() throws SQLException {

        ProdutoFornecidoDAO aDAO = new ProdutoFornecidoDAO();
        listaProdutosFornecidos = aDAO.listAllEntities();

        IDtabelaProdutoFornecido.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Binding complex fields using lambda expressions
        produtotabelaProdutoFornecido.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));

//        fornecedortabelaProdutoFornecido.setCellValueFactory(cellData
//                -> new SimpleStringProperty(cellData.getValue().getAgente().getNome()));

        armazemtabelaProdutoFornecido.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome()));

        quantidadetabelaProdutoFornecido.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        custotabelaProdutoFornecido.setCellValueFactory(new PropertyValueFactory<>("custo"));
        datatabelaProdutoFornecido.setCellValueFactory(new PropertyValueFactory<>("data_fornecida"));

        tabelaProdutoFornecido.setItems(listaProdutosFornecidos);
    }

    /*
        Funcoes para fazer pesquisas no banco de dados informacoes relacionadas 
    com o agente selecionado
     */
 /*
        Funcoes de linha selecionada
     */
    public void selectedLineProduto() throws SQLException {
        Produto x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {

            ps_txtIDProduto.setText(String.valueOf(x.getId()));
            ps_txtIDCategoria.setText(String.valueOf(x.getCategoria().getId()));
            ps_txtNomeProduto.setText(x.getNome());
            ps_combunidademedidaProduto.setValue(x.getUnidade_medida().getSigla());
            ps_txtcodigobarrasProduto.setText(x.getCodigo_barra());
            ps_txtdescricaoProduto.setText(x.getDescricao());
            if (x.getCategoria().getTipo().equals("Serviço")) {
                btnEntradaProdutos.setDisable(true);
            } else {
                btnEntradaProdutos.setDisable(false);
            }
            showPreco();
        }

    }

    public void selectedLineFornecedor() throws SQLException {
        Cliente a = tabelaCliente.getSelectionModel().getSelectedItem();
        if (a != null) {
            ps_txtIDFornecedor.setText(String.valueOf(a.getId()));
        }
    }

    public void selectedLineArmazem() throws SQLException {
        Armazem a = tabelaArmazem.getSelectionModel().getSelectedItem();
        if (a != null) {
            ps_txtIDArmazem.setText(String.valueOf(a.getId()));
        }
    }

    /*
        Funcoes para Adicionar informacoes ao banco de dados
     */
    public void addPreco() throws SQLException {
        PrecoVenda pv = new PrecoVenda();
        PrecoVendaDAO xDAO = new PrecoVendaDAO();
        // O split() pega a segunda parte do array separando o texto formatado
        // no padrao MZN 00.00
        pv.setPreco(Double.valueOf(ps_txtprecoProduto.getText().split(" ")[1].replace(".", "").replace(",", ".")));
        pv.setData_inicio(String.valueOf(date_inicio.getValue()));
        pv.setData_fim(String.valueOf(date_fim.getValue()));
        Produto p = new Produto();
        p.setId(Integer.parseInt(ps_txtIDProduto.getText()));
        pv.setProduto(p);
        pv.setUsuario(userData);
        xDAO.addEntity(pv);

    }

    public void addProdutoFornecido() throws SQLException {
        ProdutoFornecido pf = new ProdutoFornecido();
        ProdutoFornecidoDAO xDAO = new ProdutoFornecidoDAO();
        // O split() pega a segunda parte do array separando o texto formatado
        // no padrao MZN 00.00
        pf.setCusto(Double.valueOf(ps_txtCustoAquisicao.getText().split(" ")[1].replace(".", "").replace(",", ".")));
        pf.setData_fornecida(String.valueOf(ps_dataAquisicao.getValue()));
        pf.setQuantidade(Integer.parseInt(ps_txtQuantidade.getText()));
        Produto p = new Produto();
        p.setId(Integer.parseInt(ps_txtIDProduto.getText()));
        pf.setProduto(p);
        Armazem a = new Armazem();
        a.setId(Integer.parseInt(ps_txtIDArmazem.getText()));
        pf.setArmazem(a);
        Cliente ag = new Cliente();
        ag.setId(Integer.parseInt(ps_txtIDFornecedor.getText()));
//        pf.setAgente(ag);
        pf.setUsuario(userData);
        xDAO.addEntity(pf);
        showProdutoFornecido();

    }

    public void addProduto() throws SQLException {
        ProdutoDAO xDAO = new ProdutoDAO();
        Produto a = new Produto();
        UnidadeMedida u = new UnidadeMedida();
        UnidadeMedidaDAO umd = new UnidadeMedidaDAO();
        TaxaImpostoDAO tid = new TaxaImpostoDAO();
        TaxaImposto ti = new TaxaImposto();

        a.setNome(ps_txtNomeProduto.getText());
        Categoria cat = new Categoria();
        cat.setId(Integer.parseInt(ps_txtIDCategoria.getText()));
        a.setCategoria(cat);

        Map<String, Integer> mapa_UnidadeMedida = umd.mapUnidadeMedida();
        u = umd.getEntityByID(mapa_UnidadeMedida.get(ps_combunidademedidaProduto.getValue()));
        a.setUnidade_medida(u);

        a.setCodigo_barra(ps_txtcodigobarrasProduto.getText());
        a.setDescricao(ps_txtdescricaoProduto.getText());

        xDAO.addEntity(a);
        resetProduto();
        showProduto();
    }

    /*
        Funcoes para Actualizar informacoes ao banco de dados
     */
    public void updateProduto() throws SQLException {
        ProdutoDAO xDAO = new ProdutoDAO();
        Produto a = new Produto();
        UnidadeMedida u = new UnidadeMedida();
        UnidadeMedidaDAO umd = new UnidadeMedidaDAO();
        TaxaImpostoDAO tid = new TaxaImpostoDAO();
        TaxaImposto ti = new TaxaImposto();

        a.setId(Integer.parseInt(ps_txtIDProduto.getText()));
        a.setNome(ps_txtNomeProduto.getText());
        Categoria cat = new Categoria();
        cat.setId(Integer.parseInt(ps_txtIDCategoria.getText()));
        a.setCategoria(cat);

        Map<String, Integer> mapa_UnidadeMedida = umd.mapUnidadeMedida();
        u = umd.getEntityByID(mapa_UnidadeMedida.get(ps_combunidademedidaProduto.getValue()));
        a.setUnidade_medida(u);

        a.setCodigo_barra(ps_txtcodigobarrasProduto.getText());
        a.setDescricao(ps_txtdescricaoProduto.getText());

//        xDAO.updateEntityByID(a);
        resetProduto();
        showProduto();
    }

    public void updatePreco() throws SQLException {
        PrecoVenda pv = new PrecoVenda();
        PrecoVendaDAO xDAO = new PrecoVendaDAO();
        // O split() pega a segunda parte do array separando o texto formatado
        // no padrao MZN 00.00
        pv.setId(Integer.parseInt(ps_txtIDPreco.getText()));
        pv.setPreco(Double.valueOf(ps_txtprecoProduto.getText().split(" ")[1].replace(".", "").replace(",", ".")));
        pv.setData_inicio(String.valueOf(date_inicio.getValue()));
        pv.setData_fim(String.valueOf(date_fim.getValue()));
        Produto p = new Produto();
        p.setId(Integer.parseInt(ps_txtIDProduto.getText()));
        pv.setProduto(p);
        pv.setUsuario(userData);
//        xDAO.updateEntityByID(pv);

    }

    /*
        Funcoes de resetar as textField
     */
    public void resetProduto() {
        ps_combunidademedidaProduto.setPromptText("Selecione a unidade de medida");
        ps_txtNomeProduto.setText("");
        ps_txtcodigobarrasProduto.setText("");
        ps_txtdescricaoProduto.setText("");
        ps_txtIDCategoria.setText("");
        ps_txtgrupoCategoria.setText("");
        ps_txtsubgrupoCategoria.setText("");
        ps_txtfamiliaCategoria.setText("");

    }

}
