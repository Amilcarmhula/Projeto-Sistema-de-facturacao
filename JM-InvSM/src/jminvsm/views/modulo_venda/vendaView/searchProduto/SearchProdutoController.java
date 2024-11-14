/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView.searchProduto;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jminvsm.SysFact;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;
import jminvsm.model.taxa_imposto.TaxaImposto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.precovenda.ServicePrecoVenda;
import jminvsm.service.produto.ServiceProduto;
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
public class SearchProdutoController implements Initializable {

    private Usuario userData;
    private ServicePrecoVenda servicePrecos;
    private ServicoCategoria serCategoria;
    private ServiceTaxaImpostos serTaxa;
    private ServiceUnidadeMedida serUnidade;
    private ServiceProduto serProduto;
    private ServicePrecoVenda servPreco;

    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TableColumn<Produto, Integer> ID;
    @FXML
    private TableColumn<Produto, String> armazem;
    @FXML
    private TableColumn<Produto, String> artigo;
    @FXML
    private TableColumn<Produto, String> codigobarras;
    @FXML
    private TableColumn<Produto, String> descricao;
    @FXML
    private TableColumn<Produto, String> unidade;
    @FXML
    private TableColumn<Produto, String> categoria;
    @FXML
    private TableColumn<Produto, String> taxa;
    @FXML
    private TableColumn<Produto, String> stock;
    @FXML
    private TableColumn<Produto, Integer> nivelStock;
    @FXML
    private TableColumn<Produto, String> quantidade;
    @FXML
    private TableColumn<Produto, String> btntabelaProduto;

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
    
        public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
        if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void stockControlChecked() {
        if (checkControle.isSelected()) {
            txtNivelStock.setDisable(false);
        } else {
            txtNivelStock.setDisable(true);
        }
    }

    public void addORupdate(ActionEvent e) throws SQLException {
        if ("".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            addProduto(e);
        } else if (!"".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            updateProduto(e);
            addPrecoVendaONSelected(e);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        priceFormaterProduto();

        this.userData = SysFact.getUserData();
        try {
            servicePrecos = new ServicePrecoVenda();
            serCategoria = new ServicoCategoria();
            serTaxa = new ServiceTaxaImpostos();
            serUnidade = new ServiceUnidadeMedida();
            serProduto = new ServiceProduto();
            servPreco = new ServicePrecoVenda();

            getData();
            showProduto();
        } catch (SQLException ex) {
            Logger.getLogger(SearchProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateCombos();
        precoListener();
        ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
    }

    private ObservableList<Produto> listaProduto;
    private Map<Integer, Produto> mapaProdutos;

    public void showProduto() {
        listaProduto = serProduto.getProdutos();
        mapaProdutos = new HashMap<>();
        for (Produto p : listaProduto) {
            mapaProdutos.put(p.getId(), p);
        }

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        artigo.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        armazem.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getArmazem().getNome_arm()));
        codigobarras.setCellValueFactory(new PropertyValueFactory<>("codigo_barra"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        unidade.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getUnidade_medida().getSigla()));
        categoria.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getCategoria().getFamilia()));
        taxa.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getTaxa_imposto().getNomeImposto()));
        quantidade.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStock().getSaldo())));
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
                        Produto produto = getTableView().getItems().get(getIndex());
                        check.setSelected(produto.isContole_stock());

                        setGraphic(check);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        stock.setCellFactory(cellFoctory2);

        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);
        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNomeProduto().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
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

    public void getStockView(MouseEvent evt) {
        LoadAndMoveUtilities.showStage(null, evt);
//        LoadAndMoveUtilities.showAsPopUP(null, evt);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "../views/modulo_inventario/stockView/addStock.fxml");
    }

    /*
        Codigos corregidso. Nova versao
     */
//  ############################################################################
    public void addProduto(ActionEvent e) throws SQLException {
        int id_cat = mapaCategorias.get(combCategoria.getValue().equals("") ? "0" : combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue().equals("") ? "0" : combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue().equals("") ? "0" : combTaxa.getValue()).getId();

        serProduto.regista(id_cat, id_uni,
                id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(),
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText().equals("") ? "0" : txtNivelStock.getText()));

        addPrecoVenda(e);
        resetCampos();
        stockControlChecked();
    }

    public void addPrecoVenda(ActionEvent e) throws SQLException {
        double precoNorm = Double.parseDouble(txtPreco.getText());
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText());
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText());
        servPreco.registraPrecoVenda(precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, serProduto.getUltimo());
        showProduto();
        resetCampos();
    }

    public void addPrecoVendaONSelected(ActionEvent e) throws SQLException {
        double precoNorm = Double.parseDouble(txtPreco.getText());
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText());
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText());
        Produto p = new Produto();
        p.setId(Integer.parseInt(txtIDProduto.getText()));
        servPreco.registraPrecoVenda(precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, p);
        showProduto();
        resetCampos();
        labIDPreco.setText("");
    }

    public void updateProduto(ActionEvent e) throws SQLException {
        int id_pro = Integer.parseInt(txtIDProduto.getText());
        int id_cat = mapaCategorias.get(combCategoria.getValue().equals("") ? "0" : combCategoria.getValue()).getId();
        int id_uni = mapaUnidades.get(combUnidade.getValue().equals("") ? "0" : combUnidade.getValue()).getId();
        int id_tax = mapaTaxas.get(combTaxa.getValue().equals("") ? "0" : combTaxa.getValue()).getId();

        serProduto.actualiza(id_pro, id_cat, id_uni, id_tax, txtNomeProduto.getText(),
                txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(),
                checkControle.isSelected(), Integer.valueOf(txtNivelStock.getText()));

        showProduto();

    }

    public void updatePrecoVenda(ActionEvent e) throws SQLException {
        double precoNorm = Double.parseDouble(txtPreco.getText().equals("")?"0":txtPreco.getText());
        double precoComTax = Double.parseDouble(txtprecoInclTaxa.getText().equals("")?"0":txtprecoInclTaxa.getText());
        double precoSemTax = Double.parseDouble(txtprecoExclTaxa.getText().equals("")?"0":txtprecoExclTaxa.getText());
        int id_preco = Integer.parseInt(labIDPreco.getText().equals("")?"0":labIDPreco.getText());
        servPreco.actualizaPrecoVenda(id_preco, precoNorm, precoComTax, precoSemTax,
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData);
        showProduto();
//        showPrecoVenda();
        resetCampos();
    }
//  ############################################################################

    public void precoListener() {
        txtPreco.textProperty().addListener(((observable, oldVal, newVal) -> {

            if (!newVal.isEmpty()) {
                if (combTaxa.getValue() != null) {
                    TaxaImposto t = mapaTaxas.get(combTaxa.getValue());
                    double preco = Double.parseDouble(newVal);
                    double precoComTaxa = preco + preco * t.getPercentagemImposto();
                    double precoSemTaxa = preco - preco * t.getPercentagemImposto();
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
    private Map<String, Categoria> mapaCategorias;
    private ObservableList<Categoria> listaCategorias;

    private Map<String, UnidadeMedida> mapaUnidades;
    private ObservableList<UnidadeMedida> listaUnidades;

    private Map<String, TaxaImposto> mapaTaxas;
    private ObservableList<TaxaImposto> listaTaxa;

    public void populateCombos() {
        listaCategorias = serCategoria.listaCategorias();
        listaTaxa = serTaxa.getTaxas();
        listaUnidades = serUnidade.getUnidades();

        List<String> listaCat = new ArrayList<>();
        mapaCategorias = new HashMap<>();
        for (Categoria c : listaCategorias) {
            listaCat.add(c.getFamilia());
            mapaCategorias.put(c.getFamilia(), c);
        }

        List<String> listaUni = new ArrayList<>();
        mapaUnidades = new HashMap<>();
        for (UnidadeMedida u : listaUnidades) {
            listaUni.add(u.getIdentificador());
            mapaUnidades.put(u.getIdentificador(), u);
        }

        List<String> listaTax = new ArrayList<>();
        mapaTaxas = new HashMap<>();
        for (TaxaImposto t : listaTaxa) {
            listaTax.add(t.getNomeImposto());
            mapaTaxas.put(t.getNomeImposto(), t);
        }
        combCategoria.setItems(FXCollections.observableArrayList(listaCat));
        combUnidade.setItems(FXCollections.observableArrayList(listaUni));
        combTaxa.setItems(FXCollections.observableArrayList(listaTax));

    }
//  ############################################################################

    public void selecionaProduto(MouseEvent event) {
        Produto x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            Produto p = mapaProdutos.get(x.getId());
            

            txtNivelStock.setText(String.valueOf(p.getNivelstock()));
            txtIDProduto.setText(String.valueOf(p.getId()));
            txtNomeProduto.setText(p.getNomeProduto());
            txtcodigobarrasProduto.setText(p.getCodigo_barra());
            txtdescricaoProduto.setText(p.getDescricao());
            checkControle.setSelected(p.isContole_stock());
            combCategoria.setValue(p.getCategoria().getFamilia());
            combUnidade.setValue(p.getUnidade_medida().getIdentificador());
            combTaxa.setValue(p.getTaxa_imposto().getNomeImposto());
            stockControlChecked();
            showPrecoVenda(p);
            SysFact.setData(p);
            if (event.getClickCount() == 2 && p.isContole_stock()) {
                getStockView(event);
            }
//            System.out.println("Preco normal: "+p.getPrecovenda().getPreco());
            ButtonUtilities.buttonChangeText(btnAddProduto, txtIDProduto);
        }

    }
//  ############################################################################

    public void selecionaPreco() {
        PrecoVenda pv = tabelaPrecos.getSelectionModel().getSelectedItem();
        if (pv != null && !"desactivo".equals(pv.getEstado())) {
            labIDPreco.setText(pv.getId() + "");
            txtPreco.setText(pv.getPreco() + "");
            txtprecoExclTaxa.setText(pv.getPrecoExclTaxa() + "");
            txtprecoInclTaxa.setText(pv.getPrecoInclTaxa() + "");
            dataFim.setValue(LocalDate.parse(pv.getData_fim()));
            dataInicio.setValue(LocalDate.parse(pv.getData_inicio()));
        }
        if (pv != null && "desactivo".equals(pv.getEstado())) {
            labIDPreco.setText("");
        }
    }
//  ############################################################################
    private ObservableList<PrecoVenda> listaPrecos;

    public void showPrecoVenda(Produto p) {
        int id_produto = p.getId();
        listaPrecos = servicePrecos.consultaPrecos(id_produto);
        idPreco.setCellValueFactory(new PropertyValueFactory<>("id"));
        precoNormal.setCellValueFactory(new PropertyValueFactory<>("preco"));
        precoComTaxa.setCellValueFactory(new PropertyValueFactory<>("precoInclTaxa"));
        precoSemTaxa.setCellValueFactory(new PropertyValueFactory<>("precoExclTaxa"));
        dataInicial.setCellValueFactory(new PropertyValueFactory<>("data_inicio"));
        dataFinal.setCellValueFactory(new PropertyValueFactory<>("data_fim"));
        estadoPreco.setCellValueFactory(new PropertyValueFactory<>("estado"));
        for (PrecoVenda preco : listaPrecos) {
            if(!"desactivo".equals(preco.getEstado()) && preco != null){
                p.setPrecovenda(preco);
            }
        }
        
//        
        tabelaPrecos.setItems(listaPrecos);
    }

//  ############################################################################
    public void resetCampos() throws SQLException {
//        labIDPreco.setText("");
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
        showProduto();
    }

}
