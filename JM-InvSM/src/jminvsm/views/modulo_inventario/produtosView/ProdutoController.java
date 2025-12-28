/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.produtosView;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Callback;
import jminvsm.SysFact;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.stock.Stock;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.desconto.ServiceProdutoDesconto;
import jminvsm.service.PrecoProdutoArmazem.ServicePrecoProdutoArmazem;
import jminvsm.service.precoVenda.ServicePrecoVenda;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.stock.ServiceStock;
import jminvsm.service.taxaimposto.ServiceTaxaImpostos;
import jminvsm.service.unidadeMedida.ServiceUnidadeMedida;
import jminvsm.util.AlertUtilities;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.FilePathFinderUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ProdutoController implements Initializable {

    private Usuario userData;
    private ServicePrecoProdutoArmazem serPrecosProduArm;
    private ServicePrecoVenda serPrecoVenda;
    private ServicoCategoria serCategoria;
    private ServiceTaxaImpostos serTaxa;
    private ServiceUnidadeMedida serUnidade;
    private ServiceProduto serProduto;
    private ServicePrecoProdutoArmazem servPreco;
    private ServiceArmazem servArmazem;
    private ServiceStock serStock;
    private ServiceDesconto serDesconto;
    private ServiceProdutoDesconto serProDesconto;

    private int idArmazem;

    @FXML
    private TableView<ProdutoDesconto> tabelaprodutodesconto;
    @FXML
    private TableColumn<ProdutoDesconto, String> pdProduto;
    @FXML
    private TableColumn<ProdutoDesconto, String> pdArmazem;
    @FXML
    private TableColumn<ProdutoDesconto, String> pdDesconto;
    @FXML
    private TableColumn<ProdutoDesconto, String> pdDataInicial;
    @FXML
    private TableColumn<ProdutoDesconto, String> pdDataFinal;
    @FXML
    private TableColumn<ProdutoDesconto, Double> pdPercentagem;

    @FXML
    private TableView<Stock> tabelaProduto;
    @FXML
    private TableColumn<Stock, String> ID;
    @FXML
    private TableColumn<Stock, String> armazem;
    @FXML
    private TableColumn<Stock, String> artigo;
    @FXML
    private TableColumn<Stock, String> codigobarras;
    @FXML
    private TableColumn<Stock, String> descricao;
    @FXML
    private TableColumn<Stock, String> unidade;
    @FXML
    private TableColumn<Stock, String> categoria;
    @FXML
    private TableColumn<Stock, String> taxa;
    @FXML
    private TableColumn<Stock, String> stock;
    @FXML
    private TableColumn<Stock, String> nivelStock;
    @FXML
    private TableColumn<Stock, String> quantidade;
    @FXML
    private TableColumn<Stock, String> tipoProduto;
    @FXML
    private TableColumn<Stock, String> unidadesPorTipo;
    @FXML
    private TableColumn<Stock, String> btntabelaProduto;

    @FXML
    private TableView<Desconto> tableDescontos;
    @FXML
    private TableColumn<Desconto, Integer> idDesconto;
    @FXML
    private TableColumn<Desconto, String> nomeDesconto;
    @FXML
    private TableColumn<Desconto, String> descricaoDesconto;

    @FXML
    private CheckBox checkControle;
    @FXML
    private DatePicker dataFim;
    @FXML
    private DatePicker dataInicio;
    @FXML
    private DatePicker dataInicioDesconto;
    @FXML
    private DatePicker dataFimDesconto;

    @FXML
    private TextField txtIDProduto;
    @FXML
    private TextField txtIDDesconto;
    @FXML
    private TextField txtNomeProduto;
    @FXML
    private TextField txtcodigobarrasProduto;
    @FXML
    private TextArea txtdescricaoProduto;
    @FXML
    private TextField txtPesquisar;
    @FXML
    private TextField txtNivelStock;
    @FXML
    private TextField txtPercentagemDesconto;
    @FXML
    private ComboBox<String> combCategoria;
    @FXML
    private ComboBox<String> combUnidade;
    @FXML
    private ComboBox<String> combTipo;
    @FXML
    private ComboBox<String> combTaxa;

    @FXML
    private ComboBox<String> combCategoriaPesq;
    @FXML
    private ComboBox<String> combUnidadePesq;
    @FXML
    private ComboBox<String> combTaxaPesq;
//    @FXML
//    private ComboBox<String> combTipoArmazemPesq;
//    @FXML
//    private ComboBox<String> combArmazemPesq;

    @FXML
    private Label labIDPreco;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtUnidadesPorTipo;
    @FXML
    private TextField txtPesqDesconto;
    @FXML
    private TableView<PrecoVenda> tabelaPrecos;
    @FXML
    private TableColumn<PrecoVenda, Integer> idPreco;
    @FXML
    private TableColumn<PrecoVenda, Double> precoNormal;
    @FXML
    private TableColumn<PrecoVenda, Double> precoVenda;
    @FXML
    private TableColumn<PrecoVenda, Double> precoFinal;
    @FXML
    private TableColumn<PrecoVenda, String> dataValidade;
    @FXML
    private TableColumn<PrecoVenda, String> estadoPreco;
    @FXML
    private Button btnAddProduto;

    @FXML
    private Button btn_taxas;
    @FXML
    private Button btn_unidades;
    @FXML
    private Button btn_categorias;
    @FXML
    private Button btn_Descontos;

    @FXML
    public void getViews(ActionEvent e) {
        SysFact.setData(null);
        String path = "";
        if (e.getSource() == btn_taxas) {
            path = "/jminvsm/views/modulo_inventario/taxasView/taxaView.fxml";
//            path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "taxaView.fxml");
        } else if (e.getSource() == btn_unidades) {
            path = "/jminvsm/views/modulo_inventario/unidadeView/unidadeView.fxml";
        } else if (e.getSource() == btn_categorias) {
            path = "/jminvsm/views/modulo_inventario/categoriaView/categoriaView.fxml";
        } else if (e.getSource() == btn_Descontos) {
            path = "/jminvsm/views/modulo_venda/descontoView/descontoView.fxml";
        }
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, path);
    }

    public void stockControlChecked() {
        if (checkControle.isSelected()) {
            txtNivelStock.setDisable(false);
        } else {
            txtNivelStock.setDisable(true);
        }
    }

    public void addORupdate(ActionEvent e) {
        if ("".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            addProduto(e);
        } else if (!"".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            updateProduto(e);
        } else if (!"".equals(txtIDProduto.getText()) && !"0".equals(labIDPreco.getText())) {
            updateProduto(e);
            updatePrecoVenda(e);
        }
    }

    public void getFamiliaView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            LoadAndMoveUtilities.showAsPopUP(null, evt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "../views/modulo_configs/categoriaView/categoriaView.fxml");
        }
    }

    public void getUnidadeView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            LoadAndMoveUtilities.showAsPopUP(null, evt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "../views/modulo_configs/unidadeView/unidadeView.fxml");
        }
    }

    public void getTaxaView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            LoadAndMoveUtilities.showAsPopUP(null, evt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "../views/modulo_configs/taxasView/taxaView.fxml");
        }
    }

    public void getCalculaPreco(MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            LoadAndMoveUtilities.showAsPopUP(null, evt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "calculaPreco.fxml"));
        }
    }

    public void getData() {
        txtPreco.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Double) {
                        txtPreco.setText(SysFact.getData() + "");
                    }
                }
            }
        });
    }

    public void getDataOnFocus() {
        tabelaProduto.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    showInvenarioProdutos();
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            serPrecosProduArm = new ServicePrecoProdutoArmazem();
            serPrecoVenda = new ServicePrecoVenda();
            serCategoria = new ServicoCategoria();
            serTaxa = new ServiceTaxaImpostos();
            serUnidade = new ServiceUnidadeMedida();
            serProduto = new ServiceProduto();
            servPreco = new ServicePrecoProdutoArmazem();
            servArmazem = new ServiceArmazem();
            serStock = new ServiceStock();
            serDesconto = new ServiceDesconto();
            serProDesconto = new ServiceProdutoDesconto();

            getData();
            getDataOnFocus();
            showInvenarioProdutos();
            showDescontos();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateCombos();
//        precoListener();
        ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);

    }

    private ObservableList<Desconto> listaDescontos;

    public void showDescontos() {
        listaDescontos = serDesconto.listDescontos();

        idDesconto.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeDesconto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoDesconto.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        FilteredList<Desconto> dadosFiltrados = new FilteredList<>(listaDescontos, b -> true);
        txtPesqDesconto.textProperty().addListener((observable, oldValues, newValues) -> {
            dadosFiltrados.setPredicate(x -> {
                if (newValues == null || newValues.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValues.toLowerCase();
                if (x.getNome().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Desconto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tableDescontos.comparatorProperty());
//        tabelaDesconto.setItems(listaDescontos);
        tableDescontos.setItems(sortedData);
    }

    private ObservableList<Stock> listaProduto;
    private Map<String, Stock> mapaProdutos;

    public void showInvenarioProdutos() {
        listaProduto = serStock.listaInvenarioProdutos();
        mapaProdutos = new HashMap<>();
        for (Stock s : listaProduto) {
            mapaProdutos.put(s.getProduto().getId() + "," + s.getArmazem().getId(), s);
        }

        ID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getId() + "," + cellData.getValue().getArmazem().getId())));
        armazem.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getArmazem().getNome_arm() + ": " + cellData.getValue().getArmazem().getTipo())));
        artigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        codigobarras.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getCodigo_barra()));
        descricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getDescricao()));
        unidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getUnidadeMedida().getSigla()));
        tipoProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getTipoProduto()));
        unidadesPorTipo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getUnidadesPorTipo())));
        categoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getCategoria().getFamilia()));
        taxa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getImposto().getNome()));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        nivelStock.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getNivelstock())));

        //add cell of checkBox edit 
        Callback<TableColumn<Stock, String>, TableCell<Stock, String>> cellFoctory2 = (TableColumn<Stock, String> param) -> {
            // make cell containing buttons
            final TableCell<Stock, String> cell = new TableCell<Stock, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        CheckBox check = new CheckBox();
                        check.setDisable(true);
                        Stock s = getTableView().getItems().get(getIndex());
                        check.setSelected(s.getProduto().isControle_stock());

                        setGraphic(check);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        stock.setCellFactory(cellFoctory2);

        FilteredList<Stock> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getProduto().getNome().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getProduto().getCodigo_barra().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getProduto().getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Stock> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());
        //
        tabelaProduto.setItems(sortedData);
//        tabelaProduto.setItems(listaProduto);

    }

    public void getStockView(MouseEvent evt) {
//        LoadAndMoveUtilities.showStage(null, evt);
        LoadAndMoveUtilities.showAsPopUP(null, evt);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/stockView/addStock.fxml");
    }

//  ############################################################################
    public void addProduto(ActionEvent e) {
        if (combCategoria.getValue() == null) {
            AlertUtilities.showErroAlert("Categoria nao selecionada!");
            return;
        }
        if (combUnidade.getValue() == null) {
            AlertUtilities.showErroAlert("Unidade de medida nao selecionada!");
            return;
        }
        if (combTaxa.getValue() == null) {
            AlertUtilities.showErroAlert("Taxa/Imposto nao selecionada!");
            return;
        }
        int id_cat = mapaCategorias.get(combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue()).getId();
        String tipo = combTipo.getValue();
        Integer uniPorTipo = Integer.valueOf(txtUnidadesPorTipo.getText().equals("") ? "0" :txtUnidadesPorTipo.getText());

        serProduto.regista(id_cat, id_uni,
                id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(), tipo, uniPorTipo,
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText().equals("") ? "0" : txtNivelStock.getText()));
        showInvenarioProdutos();
        resetCampos();
        stockControlChecked();
    }

    public void addPrecoProdutoArmazem(ActionEvent e) {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        Integer idProd = Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText());

        servPreco.registraPreco(idProd, idArmazem, precoNorm);
        showInvenarioProdutos();
        resetCampos();
    }

    public void addProdutoDesconto(ActionEvent e) {
        Double percentagem = Double.valueOf(txtPercentagemDesconto.getText().equals("") ? "0" : txtPercentagemDesconto.getText());
        Integer idProd = Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText());
        Integer idDesc = Integer.valueOf(txtIDDesconto.getText().equals("") ? "0" : txtIDDesconto.getText());

        serProDesconto.regista(idProd, idDesc, idArmazem,
                String.valueOf(dataInicioDesconto.getValue()),
                String.valueOf(dataFimDesconto.getValue()), percentagem);

        showInvenarioProdutos();
        resetCampos();
    }

   

    public void updateProduto(ActionEvent e) {
        int id_pro = Integer.parseInt(txtIDProduto.getText());
        int id_cat = mapaCategorias.get(combCategoria.getValue().equals("") ? "0" : combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue().equals("") ? "0" : combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue().equals("") ? "0" : combTaxa.getValue()).getId();
        String tipo = combTipo.getValue();
        Integer uniPorTipo = Integer.valueOf(txtUnidadesPorTipo.getText().equals("") ? "0" :txtUnidadesPorTipo.getText());

        serProduto.actualiza(id_pro, id_cat, id_uni, id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(), tipo, uniPorTipo,
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText()));

        showInvenarioProdutos();
//        resetCampos();

    }

    public void updatePrecoVenda(ActionEvent e) {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        int id_preco = Integer.parseInt(labIDPreco.getText().equals("") ? "0" : labIDPreco.getText());
//        servPreco.actualizaPrecoVenda(id_preco, precoNorm, precoComTax, precoSemTax,
//                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
//                userData);
        showInvenarioProdutos();
//        showPrecoVenda();
        resetCampos();
    }
//  ############################################################################

//    public void precoListener() {
//        txtPreco.textProperty().addListener(((observable, oldVal, newVal) -> {
//
//            if (!newVal.isEmpty()) {
//                if (combTaxa.getValue() != null) {
//                    Imposto t = mapaTaxas.get(combTaxa.getValue());
//                    double preco = Double.parseDouble(newVal);
//                    double precoComTaxa = preco + preco * t.getPercentagem();
//                    double precoSemTaxa = preco - preco * t.getPercentagem();
//                    txtprecoInclTaxa.setText(precoComTaxa + "");
//                    txtprecoExclTaxa.setText(precoSemTaxa + "");
//                } else {
//                    txtPreco.setText("");
//                    AlertUtilities.showDialog("Erro", "Tipo de Taxa/Imposto nao selecionado!");
//                }
//            }
//        }));
//    }
//  ############################################################################
    private Map<String, Armazem> mapaArmazens;
    private ObservableList<Armazem> listaArmazens;

    private Map<String, Categoria> mapaCategorias;
    private ObservableList<Categoria> listaCategorias;

    private Map<String, UnidadeMedida> mapaUnidades;
    private ObservableList<UnidadeMedida> listaUnidades;

    private Map<String, Imposto> mapaTaxas;
    private ObservableList<Imposto> listaTaxa;

    public void populateCombos() {
        listaCategorias = serCategoria.listaCategorias();
        listaTaxa = serTaxa.getTaxas();
        listaUnidades = serUnidade.getUnidades();
        listaArmazens = servArmazem.listaArmazens();

        List<String> listaArm = new ArrayList<>();
        listaArm.add("Todos...");
        mapaArmazens = new HashMap<>();
        for (Armazem a : listaArmazens) {
            listaArm.add(a.getNome_arm());
            mapaArmazens.put(a.getNome_arm(), a);
        }

        List<String> listaCat = new ArrayList<>();
        listaCat.add("Todas...");
        mapaCategorias = new HashMap<>();
        for (Categoria c : listaCategorias) {
            listaCat.add(c.getFamilia());
            mapaCategorias.put(c.getFamilia(), c);
        }

        List<String> listaUni = new ArrayList<>();
        listaUni.add("Todas...");
        mapaUnidades = new HashMap<>();
        for (UnidadeMedida u : listaUnidades) {
            listaUni.add(u.getNome());
            mapaUnidades.put(u.getNome(), u);
        }

        List<String> listaTax = new ArrayList<>();
        listaTax.add("Todas...");
        mapaTaxas = new HashMap<>();
        for (Imposto t : listaTaxa) {
            listaTax.add(t.getNome());
            mapaTaxas.put(t.getNome(), t);
        }
        combCategoria.setItems(FXCollections.observableArrayList(listaCat));
        combUnidade.setItems(FXCollections.observableArrayList(listaUni));
        combTaxa.setItems(FXCollections.observableArrayList(listaTax));
        combTipo.setItems(FXCollections.observableArrayList("Simples","Composto"));

    }
//  ############################################################################

    public void selecionaProduto(MouseEvent event) {
//        TablePosition pos = tabelaProduto.getSelectionModel().getSelectedCells().get(0);
//        int linha = pos.getRow();
//        TableColumn coluna = tabelaProduto.getColumns().get(0);
//        Object obj = coluna.getCellData(linha);
//
//        System.out.println("Valor: " + obj);

        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            Stock s = mapaProdutos.get(x.getProduto().getId() + "," + x.getArmazem().getId());

            idArmazem = s.getArmazem().getId();
            txtNivelStock.setText(String.valueOf(s.getProduto().getNivelstock()));
            txtIDProduto.setText(String.valueOf(s.getProduto().getId()));
            txtNomeProduto.setText(s.getProduto().getNome());
            txtcodigobarrasProduto.setText(s.getProduto().getCodigo_barra());
            txtdescricaoProduto.setText(s.getProduto().getDescricao());
            checkControle.setSelected(s.getProduto().isControle_stock());
            combCategoria.setValue(s.getProduto().getCategoria().getFamilia());
            combUnidade.setValue(s.getProduto().getUnidadeMedida().getNome());
            combTipo.setValue(s.getProduto().getTipoProduto());
            combTaxa.setValue(s.getProduto().getImposto().getNome());
            txtUnidadesPorTipo.setText(String.valueOf(s.getProduto().getUnidadesPorTipo()));
            stockControlChecked();
//            showPrecoVenda();

            if (event.getClickCount() == 2 && s.getProduto().isControle_stock()) {
                SysFact.setData(s);
                getStockView(event);
            }
            deleteProduto(event, x);
            ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
        }
        showPrecoVenda();
        showProdutoDesconto();
    }

    public void selecionaDesconto(MouseEvent event) {
        Desconto x = tableDescontos.getSelectionModel().getSelectedItem();
        if (x != null) {
            txtIDDesconto.setText(String.valueOf(x.getId()));
        }
    }

    public void deleteProduto(MouseEvent event, Stock x) {
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
                serProduto.excluir(x.getProduto().getId());
                showInvenarioProdutos();
                resetCampos();
            } else if (result.get() == btnCancel) {
                resetCampos();
            }
        }
    }
//  ############################################################################

//    public void selecionaPreco(MouseEvent event) {
//        PrecoVenda pv = tabelaPrecos.getSelectionModel().getSelectedItem();
//        if (pv != null && !"desactivo".equals(pv.getEstado())) {
//            labIDPreco.setText(pv.getId() + "");
//            txtPreco.setText(pv.getPrecoNormal() + "");
//            dataFim.setValue(LocalDate.parse(pv.getDataValidade()));
//        }
//        deletePreco(event, pv);
//        if (pv != null && "desactivo".equals(pv.getEstado())) {
//            labIDPreco.setText("");
//        }
//    }
    public void deletePreco(MouseEvent event, PrecoVenda x) {
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja desactivar o preco?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Desactivar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
//                servicePrecos.deletePreco(x.getId());
                if (serPrecosProduArm.isOpsSuccess()) {
                    showInvenarioProdutos();
//                    showPrecoVenda();
                    resetCampos();
                }
            } else if (result.get() == btnCancel) {
                resetCampos();
            }
        }
    }
//  ############################################################################
    private ObservableList<PrecoVenda> listaPrecos;

    public void showPrecoVenda() {
        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {

            listaPrecos = serPrecoVenda.consultaPrecosVendas(x.getProduto().getId(), x.getArmazem().getId());
            idPreco.setCellValueFactory(new PropertyValueFactory<>("id"));
            precoNormal.setCellValueFactory(new PropertyValueFactory<>("precoNormal"));
            precoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
            precoFinal.setCellValueFactory(new PropertyValueFactory<>("precoFinal"));
            dataValidade.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
            estadoPreco.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tabelaPrecos.setItems(listaPrecos);
        }
    }

    private ObservableList<ProdutoDesconto> listaprodutodesconto;

    public void showProdutoDesconto() {
        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {

            listaprodutodesconto = serProDesconto.consultaProdutoDesconto(x.getProduto().getId(), x.getArmazem().getId());

            pdProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
            pdArmazem.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
            pdDesconto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesconto().getNome()));
            pdDataInicial.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
            pdDataFinal.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
            pdPercentagem.setCellValueFactory(new PropertyValueFactory<>("percentagem"));

            tabelaprodutodesconto.setItems(listaprodutodesconto);
        }
    }
//  ############################################################################

    public void resetCampos() {
        labIDPreco.setText("");
        txtPreco.setText("");
        checkControle.setSelected(false);
        stockControlChecked();
        txtNivelStock.setText("");

        txtIDProduto.setText("");
        txtNomeProduto.setText("");
        txtcodigobarrasProduto.setText("");
        txtdescricaoProduto.setText("");
        ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
        showInvenarioProdutos();
//        populateCombos();
    }

}
