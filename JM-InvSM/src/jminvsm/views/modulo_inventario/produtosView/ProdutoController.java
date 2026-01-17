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
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.stock.Stock;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.desconto.ServiceProdutoDesconto;
import jminvsm.service.precoProdutoArmazem.ServicePrecoProdutoArmazem;
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
    private ServicoCategoria serCategoria;
    private ServiceTaxaImpostos serTaxa;
    private ServiceUnidadeMedida serUnidade;
    private ServiceProduto productService;
    private ServiceArmazem serviceArmazem;
    private ServiceStock serviceStock;
    private ServiceDesconto serDesconto;
    private ServiceProdutoDesconto serProDesconto;

    private int idArmazem;
    private int idArmazemDesconto;
    private int idDescontoComb;

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
    private TableColumn<ProdutoDesconto, String> pdPercentagem;

    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TableColumn<Produto, String> ID;
    @FXML
    private TableColumn<Produto, String> artigo;
    @FXML
    private TableColumn<Produto, String> codigobarras;
    @FXML
    private TableColumn<Produto, String> descricao;
    @FXML
    private TableColumn<Produto, String> nivelStock;
    @FXML
    private TableColumn<Produto, String> tipoProduto;
    @FXML
    private TableColumn<Produto, String> unidadesPorTipo;
    @FXML
    private TableColumn<Produto, String> controloStock;

    @FXML
    private TableView<Stock> tableResumoStock;
    @FXML
    private TableColumn<Stock, String> armazemResumoStock;
    @FXML
    private TableColumn<Stock, Integer> saldoResumoStock;

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
    private TextField txtdescricaoProduto;
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
    private ComboBox<String> cBoxArmazem;
    @FXML
    private ComboBox<String> cBoxDesconto;
    @FXML
    private ComboBox<String> cBoxArmazemDesconto;

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
    private Label labDescricaoDesconto;
    @FXML
    private Label labCategoria;
    @FXML
    private Label labFamilia;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtUnidadesPorTipo;
    @FXML
    private TextField txtPesqDesconto;
    @FXML
    private TableView<PrecoProdutoArmazem> tabelaPrecos;
    @FXML
    private TableColumn<PrecoProdutoArmazem, Integer> idPreco;
    @FXML
    private TableColumn<PrecoProdutoArmazem, Double> preco;
    @FXML
    private TableColumn<PrecoProdutoArmazem, Double> precoVenda;
    @FXML
    private TableColumn<PrecoProdutoArmazem, Double> precoFinal;
    @FXML
    private TableColumn<PrecoProdutoArmazem, String> dataValidade;
    @FXML
    private TableColumn<PrecoProdutoArmazem, String> estadoPreco;
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

//    public void getData() {
//        txtPreco.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!oldValue && newValue) {
//                    if (SysFact.getData() instanceof Double) {
//                        txtPreco.setText(SysFact.getData() + "");
//                    }
//                }
//            }
//        });
//    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        try {
            serPrecosProduArm = new ServicePrecoProdutoArmazem();
            serCategoria = new ServicoCategoria();
            serTaxa = new ServiceTaxaImpostos();
            serUnidade = new ServiceUnidadeMedida();
            productService = new ServiceProduto();
            serviceArmazem = new ServiceArmazem();
            serviceStock = new ServiceStock();
            serDesconto = new ServiceDesconto();
            serProDesconto = new ServiceProdutoDesconto();

//            getData();
            showProdutos();
//            showDescontos();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        populateCombos();
        cBoxArmazem.setOnAction(evt -> {
            String data = cBoxArmazem.getSelectionModel().getSelectedItem();
            idArmazem = mapaArmazens.get(data).getId();
            showProdutoDesconto();
        });
        cBoxArmazemDesconto.setOnAction(evt -> {
            String data = cBoxArmazemDesconto.getSelectionModel().getSelectedItem();
            idArmazemDesconto = mapaArmazens.get(data).getId();
            showProdutoDesconto();
        });
        cBoxDesconto.setOnAction(evt -> {
            String data = cBoxDesconto.getSelectionModel().getSelectedItem();
            idDescontoComb = mapaDesconto.get(data).getId();
            labDescricaoDesconto.setText(mapaDesconto.get(data).getDescricao());
        });
//        precoListener();
        ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);

    }

//    private ObservableList<Desconto> listaDescontos;
//
//    public void showDescontos() {
//        listaDescontos = serDesconto.listDescontos();
//
////        idDesconto.setCellValueFactory(new PropertyValueFactory<>("id"));
////        nomeDesconto.setCellValueFactory(new PropertyValueFactory<>("nome"));
////        descricaoDesconto.setCellValueFactory(new PropertyValueFactory<>("descricao"));
//
//        FilteredList<Desconto> dadosFiltrados = new FilteredList<>(listaDescontos, b -> true);
//        txtPesqDesconto.textProperty().addListener((observable, oldValues, newValues) -> {
//            dadosFiltrados.setPredicate(x -> {
//                if (newValues == null || newValues.isEmpty()) {
//                    return true;
//                }
//                String filtroCaixaBaixa = newValues.toLowerCase();
//                if (x.getNome().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else if (x.getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//        });
//        SortedList<Desconto> sortedData = new SortedList<>(dadosFiltrados);
//        sortedData.comparatorProperty().bind(tableDescontos.comparatorProperty());
////        tabelaDesconto.setItems(listaDescontos);
//        tableDescontos.setItems(sortedData);
//    }
    private ObservableList<Produto> listaProduto;
    private Map<Integer, Produto> mapaProdutos;

    public void showProdutos() {
        listaProduto = productService.listaTodosProdutos();
        mapaProdutos = new HashMap<>();
        for (Produto p : listaProduto) {
            mapaProdutos.put(p.getId(), p);
        }

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        artigo.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigobarras.setCellValueFactory(new PropertyValueFactory<>("codigo_barra"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tipoProduto.setCellValueFactory(new PropertyValueFactory<>("tipoProduto"));
        unidadesPorTipo.setCellValueFactory(new PropertyValueFactory<>("unidadesPorTipo"));
        nivelStock.setCellValueFactory(new PropertyValueFactory<>("nivelstock"));

        //add cell of checkBox edit 
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFoctory2 = (TableColumn<Produto, String> param) -> {
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
                        CheckBox check = new CheckBox();
                        check.setDisable(true);
                        Produto s = getTableView().getItems().get(getIndex());
                        check.setSelected(s.isControle_stock());

                        setGraphic(check);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        controloStock.setCellFactory(cellFoctory2);

        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getCodigo_barra().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else if (x.getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Produto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());
        //
        tabelaProduto.setItems(sortedData);
//        tabelaProduto.setItems(listaProduto);

    }

    public void getStockView(MouseEvent evt) {
//        LoadAndMoveUtilities.showStage(null, evt);
        LoadAndMoveUtilities.showAsPopUP(null, evt);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/stockView/addEstoque.fxml");
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
        Integer uniPorTipo = Integer.valueOf(txtUnidadesPorTipo.getText().equals("") ? "0" : txtUnidadesPorTipo.getText());

        productService.regista(id_cat, id_uni,
                id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(), tipo, uniPorTipo,
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText().equals("") ? "0" : txtNivelStock.getText()));
        showProdutos();
        resetCampos();
        stockControlChecked();
    }

    public void addPrecoProdutoArmazem(ActionEvent e) {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        Integer idProd = Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText());

        serPrecosProduArm.registraPreco(idProd, idArmazem, precoNorm);
        showPrecoProdutoArmazem();
        showProdutoDesconto();
        showPrecoProdutoArmazem();
    }

    public void addProdutoDesconto(ActionEvent e) {
        //Double percentagem = Double.valueOf(txtPercentagemDesconto.getText().equals("") ? "0" : txtPercentagemDesconto.getText());
        Integer idProd = Integer.valueOf(txtIDProduto.getText().equals("") ? "0" : txtIDProduto.getText());
        Integer idDesc = idDescontoComb;

        serProDesconto.regista(idProd, idDescontoComb, idArmazemDesconto,
                String.valueOf(dataInicioDesconto.getValue()),
                String.valueOf(dataFimDesconto.getValue()));

        showPrecoProdutoArmazem();
        showProdutoDesconto();
        showProdutoDesconto();
//        resetCampos();
    }

    public void updateProduto(ActionEvent e) {
        int id_pro = Integer.parseInt(txtIDProduto.getText());
        int id_cat = mapaCategorias.get(combCategoria.getValue().equals("") ? "0" : combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue().equals("") ? "0" : combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue().equals("") ? "0" : combTaxa.getValue()).getId();
        String tipo = combTipo.getValue();
        Integer uniPorTipo = Integer.valueOf(txtUnidadesPorTipo.getText().equals("") ? "0" : txtUnidadesPorTipo.getText());

        productService.actualiza(id_pro, id_cat, id_uni, id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(), tipo, uniPorTipo,
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText()));

        showProdutos();
//        resetCampos();

    }

    public void updatePrecoVenda(ActionEvent e) {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        int id_preco = Integer.parseInt(labIDPreco.getText().equals("") ? "0" : labIDPreco.getText());
//        servPreco.actualizaPrecoVenda(id_preco, precoNorm, precoComTax, precoSemTax,
//                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
//                userData);
        showProdutos();
//        showPrecoProdutoArmazem();
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
    private Map<String, Categoria> mapaCategorias;
    private ObservableList<Categoria> listaCategorias;

    private Map<String, UnidadeMedida> mapaUnidades;
    private ObservableList<UnidadeMedida> listaUnidades;

    private Map<String, Imposto> mapaTaxas;
    private ObservableList<Imposto> listaTaxa;

    private ObservableList<Armazem> storeList;
    private Map<String, Armazem> mapaArmazens;

    private ObservableList<Desconto> descontoList;
    private Map<String, Desconto> mapaDesconto;

    public void populateCombos() {
        storeList = serviceArmazem.listaTodosArmazens();
        List<String> listaArm = new ArrayList<>();
        mapaArmazens = new HashMap<>();
        for (Armazem a : storeList) {
            listaArm.add(a.getNome_arm());
            mapaArmazens.put(a.getNome_arm(), a);
        }
        cBoxArmazem.setItems(FXCollections.observableArrayList(listaArm));
        cBoxArmazemDesconto.setItems(FXCollections.observableArrayList(listaArm));

//        descontos
        descontoList = serDesconto.listDescontos();
        List<String> listaDesconto = new ArrayList<>();
        mapaDesconto = new HashMap<>();
        for (Desconto d : descontoList) {
            listaDesconto.add(d.getNome());
            mapaDesconto.put(d.getNome(), d);
        }
        cBoxDesconto.setItems(FXCollections.observableArrayList(listaDesconto));

        listaCategorias = serCategoria.listaCategorias();
        listaTaxa = serTaxa.getTaxas();
        listaUnidades = serUnidade.getUnidades();

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
        combTipo.setItems(FXCollections.observableArrayList("Simples", "Composto"));

    }
//  ############################################################################

    public void selecionaProduto(MouseEvent event) {
        Produto x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            Produto p = mapaProdutos.get(x.getId());

            txtNivelStock.setText(String.valueOf(p.getNivelstock()));
            txtIDProduto.setText(String.valueOf(p.getId()));
            txtNomeProduto.setText(p.getNome());
            txtcodigobarrasProduto.setText(p.getCodigo_barra());
            txtdescricaoProduto.setText(p.getDescricao());
            checkControle.setSelected(p.isControle_stock());
            combCategoria.setValue(p.getCategoria().getFamilia());
            combUnidade.setValue(p.getUnidadeMedida().getNome());
            combTipo.setValue(p.getTipoProduto());
            combTaxa.setValue(p.getImposto().getNome());
            txtUnidadesPorTipo.setText(String.valueOf(p.getUnidadesPorTipo()));
            labCategoria.setText(p.getCategoria().getGrupo() + " > " + p.getCategoria().getSubgrupo() + " > " + p.getCategoria().getFamilia());
            stockControlChecked();
            showResumoStock(x.getId());
            showPrecoProdutoArmazem();
            showProdutoDesconto();

            if (event.getClickCount() == 2 && p.isControle_stock()) {
                SysFact.setData(p);
                getStockView(event);
            }
            deleteProduto(event, x);
//            ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
        }

    }

    private ObservableList<Stock> listaResumoStock;

    public void showResumoStock(int id)  {
        listaResumoStock = serviceStock.getResumo(id);

        armazemResumoStock.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        saldoResumoStock.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        tableResumoStock.setItems(listaResumoStock);
    }

//    public void selecionaArmazemNaTabelaPreco(MouseEvent event) {
//        PrecoProdutoArmazem x = tabelaPrecos.getSelectionModel().getSelectedItem();
//        if (x != null) {
//            idArmazem = mapaPrecoProdutoArmazem.get(x.getId()).getArmazem().getId();
//            
//        }
//    }
    public void deleteProduto(MouseEvent event, Produto x) {
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
                productService.excluir(x.getId());
                showProdutos();
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
    public void deletePreco(MouseEvent event, PrecoProdutoArmazem x) {
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
                    showProdutos();
//                    showPrecoProdutoArmazem();
                    resetCampos();
                }
            } else if (result.get() == btnCancel) {
                resetCampos();
            }
        }
    }
//  ############################################################################
    private ObservableList<PrecoProdutoArmazem> listaPrecos;
    private Map<Integer, PrecoProdutoArmazem> mapaPrecoProdutoArmazem;

    public void showPrecoProdutoArmazem() {
        Produto x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {

            listaPrecos = serPrecosProduArm.listaTodosPrecos(x.getId());
            mapaPrecoProdutoArmazem = new HashMap<>();
            for (PrecoProdutoArmazem ppa : listaPrecos) {
                mapaPrecoProdutoArmazem.put(ppa.getId(), ppa);
            }

            idPreco.setCellValueFactory(new PropertyValueFactory<>("id"));
            preco.setCellValueFactory(new PropertyValueFactory<>("precoBase"));
            precoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
            precoFinal.setCellValueFactory(new PropertyValueFactory<>("precoFinal"));
            dataValidade.setCellValueFactory(new PropertyValueFactory<>("dataFimVigencia"));
            estadoPreco.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tabelaPrecos.setItems(listaPrecos);
//            showProdutoDesconto();
        }
    }

    private ObservableList<ProdutoDesconto> listaprodutodesconto;

    public void showProdutoDesconto() {
        listaprodutodesconto = serProDesconto.consultaProdutoDesconto(Integer.parseInt(txtIDProduto.getText()));

        pdProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        pdArmazem.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        pdDesconto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesconto().getNome()));
        pdDataInicial.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        pdDataFinal.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        pdPercentagem.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesconto().getPercentagem() + " %"));

        tabelaprodutodesconto.setItems(listaprodutodesconto);

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
        showProdutos();
//        populateCombos();
    }

}
