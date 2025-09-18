/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.produtosView;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.stock.Stock;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.precovenda.ServicePrecoVenda;
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
    private ServicePrecoVenda servicePrecos;
    private ServicoCategoria serCategoria;
    private ServiceTaxaImpostos serTaxa;
    private ServiceUnidadeMedida serUnidade;
    private ServiceProduto serProduto;
    private ServicePrecoVenda servPreco;
    private ServiceArmazem servArmazem;
    private ServiceStock serStock;

    @FXML
    private TableView<Stock> tabelaProduto;
    @FXML
    private TableColumn<Stock, String> ID;
    @FXML
    private TableColumn<Stock, String> preco;
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
    private TableColumn<Stock, String> btntabelaProduto;

    @FXML
    private CheckBox checkControle;
    @FXML
    private DatePicker dataFim;
    @FXML
    private DatePicker dataInicio;

    @FXML
    private TextField txtIDProduto;
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
    private ComboBox<String> combCategoria;
    @FXML
    private ComboBox<String> combUnidade;
    @FXML
    private ComboBox<String> combTaxa;
    
    @FXML
    private ComboBox<String> combCategoriaPesq;
    @FXML
    private ComboBox<String> combUnidadePesq;
    @FXML
    private ComboBox<String> combTaxaPesq;
    @FXML
    private ComboBox<String> combArmazemPesq;

    @FXML
    private Label labIDPreco;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtprecoInclTaxa;
    @FXML
    private TextField txtprecoExclTaxa;
    @FXML
    private TableView<PrecoVenda> tabelaPrecos;
    @FXML
    private TableColumn<Produto, Integer> idPreco;
    @FXML
    private TableColumn<Produto, Double> precoNormal;
    @FXML
    private TableColumn<Produto, Double> precoComTaxa;
    @FXML
    private TableColumn<Produto, Double> precoSemTaxa;
    @FXML
    private TableColumn<Produto, String> dataInicial;
    @FXML
    private TableColumn<Produto, String> dataFinal;
    @FXML
    private TableColumn<Produto, String> estadoPreco;
    @FXML
    private Button btnAddProduto;

    @FXML
    private Button btn_taxas;
    @FXML
    private Button btn_unidades;
    @FXML
    private Button btn_categorias;

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

    public void addORupdate(ActionEvent e){
        if ("".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            addProduto(e);
        } else if (!"".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            updateProduto(e);
            addPrecoVendaONSelected();
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

    public void getDataOnFocus(){
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
            servicePrecos = new ServicePrecoVenda();
            serCategoria = new ServicoCategoria();
            serTaxa = new ServiceTaxaImpostos();
            serUnidade = new ServiceUnidadeMedida();
            serProduto = new ServiceProduto();
            servPreco = new ServicePrecoVenda();
            servArmazem = new ServiceArmazem();
            serStock = new ServiceStock();

            getData();
            getDataOnFocus();
            showInvenarioProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateCombos();
        precoListener();
        ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
        
        combArmazemPesq.getSelectionModel().select(1);
        combArmazemPesq.requestFocus();
    }

//    private ObservableList<Produto> listaProduto;  
    private ObservableList<Stock> listaProduto;

    private Map<Integer, Stock> mapaProdutos;

    public void showInvenarioProdutos(){
//        listaProduto = serProduto.getProdutos();
        listaProduto = serStock.listaInvenarioProdutos();

        mapaProdutos = new HashMap<>();
        for (Stock s : listaProduto) {
//            mapaProdutos.put(s.getId(), s);
        }

        ID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getId()))); 
        preco.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getPreco())));
        artigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        codigobarras.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getCodigo_barra()));
        descricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getDescricao()));

        unidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getUnidade_medida().getSigla()));
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

                //        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);
                //        // Método auxiliar para atualizar o filtro com base nas duas ComboBox
                //        Runnable atualizarFiltro = () -> {
                //            dadosFiltrados.setPredicate(x -> {
                //                String armazemSelecionada = combArmazemPesq.getValue();
                //                String categoriaSelecionada = combCategoriaPesq.getValue();
                //                String unidadeSelecionada = combUnidadePesq.getValue();
                //                String taxaSelecionada = combTaxaPesq.getValue();
                //                
                //                if(armazemSelecionada == null || armazemSelecionada.equals("Todos...")){
                //                    armazemSelecionada = "";
                //                }
                //                if(categoriaSelecionada == null || categoriaSelecionada.equals("Todas...")){
                //                    categoriaSelecionada = "";
                //                }
                //                if(unidadeSelecionada == null || unidadeSelecionada.equals("Todas...")){
                //                    unidadeSelecionada = "";
                //                }
                //                if(taxaSelecionada == null || taxaSelecionada.equals("Todas...")){
                //                    taxaSelecionada = "";
                //                }
                //                
                //                // Verifica o filtro de armazem
                //                boolean armazemValida = ( armazemSelecionada.isEmpty())
                //                        || x.getArmazem().getNome_arm().toLowerCase().contains(armazemSelecionada.toLowerCase());
                //
                //                // Verifica o filtro da categoria
                //                boolean categoriaValida = ( categoriaSelecionada.isEmpty())
                //                        || x.getCategoria().getFamilia().toLowerCase().contains(categoriaSelecionada.toLowerCase());
                //
                //                // Verifica o filtro da unidade
                //                boolean unidadeValida = ( unidadeSelecionada.isEmpty())
                //                        || x.getUnidade_medida().getIdentificador().toLowerCase().contains(unidadeSelecionada.toLowerCase());
                //                
                //                // Verifica o filtro da unidade
                //                boolean taxaValida = ( taxaSelecionada.isEmpty())
                //                        || x.getImposto().getNome().toLowerCase().contains(taxaSelecionada.toLowerCase());
                //
                //                // Retorna verdadeiro somente se ambos os filtros forem válidos
                //                return armazemValida && categoriaValida && unidadeValida && taxaValida;
                //            });
                //        };

                    //        // Adiciona listeners para atualizar o filtro sempre que a seleção mudar
                    //        combCategoriaPesq.valueProperty().addListener((observable, oldValue, newValue) -> atualizarFiltro.run());
                    //        combArmazemPesq.valueProperty().addListener((observable, oldValue, newValue) -> atualizarFiltro.run());
                    //        combUnidadePesq.valueProperty().addListener((observable, oldValue, newValue) -> atualizarFiltro.run());
                    //        combTaxaPesq.valueProperty().addListener((observable, oldValue, newValue) -> atualizarFiltro.run());

//        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
//            dadosFiltrados.setPredicate(x -> {
//
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String filtroCaixaBaixa = newValue.toLowerCase();
//                if (x.getNomeProduto().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else if (x.getCodigo_barra().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else if (x.getUnidade_medida().getIdentificador().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else if (x.getDescricao().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//
//            });
//        });
//
//        // Adiciona um listener à ComboBox para definir o filtro quando um item é selecionado
//        combArmazem.valueProperty().addListener((observable, oldValue, newValue) -> {
//            dadosFiltrados.setPredicate(x -> {
//                // Se o valor da ComboBox for nulo ou vazio, exibe todos os itens
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                // Converte o filtro para caixa baixa para facilitar a busca
//                String filtroCaixaBaixa = newValue.toLowerCase();
//
//                // Aplica o filtro com base na seleção da ComboBox
//                if (x.getArmazem().getNome_arm().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//
//        });
//        
//        combCategoria.valueProperty().addListener((observable, oldValue, newValue) -> {
//            dadosFiltrados.setPredicate(x -> {
//                // Se o valor da ComboBox for nulo ou vazio, exibe todos os itens
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                // Converte o filtro para caixa baixa para facilitar a busca
//                String filtroCaixaBaixa = newValue.toLowerCase();
//
//                // Aplica o filtro com base na seleção da ComboBox
//                if (x.getCategoria().getFamilia().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//
//        });
//
//        // Adiciona um listener à ComboBox para definir o filtro quando um item é selecionado
//        combUnidade.valueProperty().addListener((observable, oldValue, newValue) -> {
//            dadosFiltrados.setPredicate(x -> {
//                // Se o valor da ComboBox for nulo ou vazio, exibe todos os itens
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                // Converte o filtro para caixa baixa para facilitar a busca
//                String filtroCaixaBaixa = newValue.toLowerCase();
//                // Aplica o filtro com base na seleção da ComboBox
//                if (x.getUnidade_medida().getIdentificador().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//        });
//
//        // Adiciona um listener à ComboBox para definir o filtro quando um item é selecionado
//        combTaxa.valueProperty().addListener((observable, oldValue, newValue) -> {
//            dadosFiltrados.setPredicate(x -> {
//                // Se o valor da ComboBox for nulo ou vazio, exibe todos os itens
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                // Converte o filtro para caixa baixa para facilitar a busca
//                String filtroCaixaBaixa = newValue.toLowerCase();
//
//                // Aplica o filtro com base na seleção da ComboBox
//                if (x.getTaxa_imposto().getNomeImposto().toLowerCase().contains(filtroCaixaBaixa)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//        });
//
            //        SortedList<Produto> sortedData = new SortedList<>(dadosFiltrados);
            //        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());
            //
//                    tabelaProduto.setItems(sortedData);
                    tabelaProduto.setItems(listaProduto);

    }

    public void getStockView(MouseEvent evt) {
//        LoadAndMoveUtilities.showStage(null, evt);
        LoadAndMoveUtilities.showAsPopUP(null, evt);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/stockView/addStock.fxml");
    }

//  ############################################################################
    public void addProduto(ActionEvent e){
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

        serProduto.regista(id_cat, id_uni,
                id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(),
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText().equals("") ? "0" : txtNivelStock.getText()));
        showInvenarioProdutos();
        addPrecoVenda(e);
        resetCampos();
        stockControlChecked();
    }

    public void addPrecoVenda(ActionEvent e){
        double precoNorm = Double.parseDouble(txtPreco.getText());
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText());
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText());
        servPreco.registraPrecoVenda(precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, serProduto.getUltimo());
        showInvenarioProdutos();
        resetCampos();
    }

    public void addPrecoVendaONSelected(){
        if (txtPreco.getText().equals("")) {
            return;
        }
        double precoNorm = Double.parseDouble(txtPreco.getText()/*.equals("")?"0":txtPreco.getText()*/);
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText()/*.equals("")?"0":txtprecoInclTaxa.getText()*/);
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText()/*.equals("")?"0":txtprecoExclTaxa.getText()*/);
        Produto p = new Produto();
        p.setId(Integer.parseInt(txtIDProduto.getText()));
        servPreco.registraPrecoVenda(precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, p);
        showInvenarioProdutos();
//        showPrecoVenda();
        resetCampos();
    }

    public void updateProduto(ActionEvent e) {
        int id_pro = Integer.parseInt(txtIDProduto.getText());
        int id_cat = mapaCategorias.get(combCategoria.getValue().equals("") ? "0" : combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue().equals("") ? "0" : combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue().equals("") ? "0" : combTaxa.getValue()).getId();

        serProduto.actualiza(id_pro, id_cat, id_uni, id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(),
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText()));

        showInvenarioProdutos();
//        resetCampos();

    }

    public void updatePrecoVenda(ActionEvent e) {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("") ? "0" : txtPreco.getText());
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText().equals("") ? "0" : txtprecoInclTaxa.getText());
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText().equals("") ? "0" : txtprecoExclTaxa.getText());
        int id_preco = Integer.parseInt(labIDPreco.getText().equals("") ? "0" : labIDPreco.getText());
        servPreco.actualizaPrecoVenda(id_preco, precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData);
        showInvenarioProdutos();
//        showPrecoVenda();
        resetCampos();
    }
//  ############################################################################

    public void precoListener() {
        txtPreco.textProperty().addListener(((observable, oldVal, newVal) -> {

            if (!newVal.isEmpty()) {
                if (combTaxa.getValue() != null) {
                    Imposto t = mapaTaxas.get(combTaxa.getValue());
                    double preco = Double.parseDouble(newVal);
                    double precoComTaxa = preco + preco * t.getPercentagem();
                    double precoSemTaxa = preco - preco * t.getPercentagem();
                    txtprecoInclTaxa.setText(precoComTaxa + "");
                    txtprecoExclTaxa.setText(precoSemTaxa + "");
                } else {
                    txtPreco.setText("");
                    AlertUtilities.showDialog("Erro", "Tipo de Taxa/Imposto nao selecionado!");
                }
            }
        }));
    }

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
        
        combCategoriaPesq.setItems(FXCollections.observableArrayList(listaCat));
        combUnidadePesq.setItems(FXCollections.observableArrayList(listaUni));
        combTaxaPesq.setItems(FXCollections.observableArrayList(listaTax));
        combArmazemPesq.setItems(FXCollections.observableArrayList(listaArm));

    }
//  ############################################################################

    public void selecionaProduto(MouseEvent event) {
        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
//        if (x != null) {
//            Stock p = mapaProdutos.get(x.getId());
//
//            txtNivelStock.setText(String.valueOf(p.getNivelstock()));
//            txtIDProduto.setText(String.valueOf(p.getId()));
//            txtNomeProduto.setText(p.getNome());
//            txtcodigobarrasProduto.setText(p.getCodigo_barra());
//            txtdescricaoProduto.setText(p.getDescricao());
//            checkControle.setSelected(p.isControle_stock());
//            combCategoria.setValue(p.getCategoria().getFamilia());
//            combUnidade.setValue(p.getUnidade_medida().getIdentificador());
//            combTaxa.setValue(p.getImposto().getNome());
//            stockControlChecked();
//            showPrecoVenda();
//            
//            if (event.getClickCount() == 2 && p.isControle_stock()) {
//                SysFact.setData(p);
//                getStockView(event);
//            }
//            deleteProduto(event, x);
//            ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
//        }

    }

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
                serProduto.excluir(x.getId());
                showInvenarioProdutos();
                resetCampos();
            } else if (result.get() == btnCancel) {
                resetCampos();
            }
        }
    }
//  ############################################################################

    public void selecionaPreco(MouseEvent event) {
        PrecoVenda pv = tabelaPrecos.getSelectionModel().getSelectedItem();
        if (pv != null && !"desactivo".equals(pv.getEstado())) {
            labIDPreco.setText(pv.getId() + "");
            txtPreco.setText(pv.getPrecoNormal() + "");
            txtprecoExclTaxa.setText(pv.getPrecoFinal() + "");
            txtprecoInclTaxa.setText(pv.getPrecoVenda() + "");
            dataFim.setValue(LocalDate.parse(pv.getDataValidade()));
            dataInicio.setValue(LocalDate.parse(pv.getData_inicio()));
        }
        deletePreco(event, pv);
        if (pv != null && "desactivo".equals(pv.getEstado())) {
            labIDPreco.setText("");
        }
    }

    public void deletePreco(MouseEvent event, PrecoVenda x){
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
                servicePrecos.deletePreco(x.getId());
                if (servicePrecos.isOpsSuccess()) {
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

//    public void showPrecoVenda() {
//        listaPrecos = servicePrecos.consultaPrecos(Integer.parseInt(txtIDProduto.getText()));
//        idPreco.setCellValueFactory(new PropertyValueFactory<>("id"));
//        precoNormal.setCellValueFactory(new PropertyValueFactory<>("preco"));
//        precoComTaxa.setCellValueFactory(new PropertyValueFactory<>("precoInclTaxa"));
//        precoSemTaxa.setCellValueFactory(new PropertyValueFactory<>("precoExclTaxa"));
//        dataInicial.setCellValueFactory(new PropertyValueFactory<>("data_inicio"));
//        dataFinal.setCellValueFactory(new PropertyValueFactory<>("data_fim"));
//        estadoPreco.setCellValueFactory(new PropertyValueFactory<>("estado"));
//
//        tabelaPrecos.setItems(listaPrecos);
//    }

//  ############################################################################
    public void resetCampos() {
        labIDPreco.setText("");
        txtPreco.setText("");
        txtprecoExclTaxa.setText("");
        txtprecoInclTaxa.setText("");
        dataInicio.setValue(LocalDate.now());
        dataFim.setValue(LocalDate.now());
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
