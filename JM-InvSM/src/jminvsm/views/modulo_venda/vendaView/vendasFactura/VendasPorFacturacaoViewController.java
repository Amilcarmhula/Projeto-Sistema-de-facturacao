/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView.vendasFactura;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import jminvsm.SysFact;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.documentosComerciais.ServiceDocumentos;
import jminvsm.service.stock.ServiceStock;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.util.AlertUtilities;
import jminvsm.util.InvoiceUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import jminvsm.util.LeitorSerialListener;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class VendasPorFacturacaoViewController implements Initializable {

    private ServiceCliente serviceCliente;
    private ServiceDesconto serDesconto;
    private ServiceDocumentos serDocs;
    private ServiceStock serviceStock;

    private Empresa empresaData;
    private Usuario userData;

    @FXML
    private Button btnGeraFactura;
    @FXML
    private Button btnNovaFactura;

    @FXML
    private Button btnCancelaFactura;

    @FXML
    private Button btnFinalizaFactura;

    @FXML
    private Button btnRemoveItem;

    @FXML
    private Label labDesconto;

    @FXML
    private Label labTaxas;

    @FXML
    private Label labNumeroFactura;

    @FXML
    private Label labSubtotal;

    @FXML
    private Label labTotalGeral;

    @FXML
    private TableView<Stock> tabelaProduto;
    @FXML
    private TableColumn<Stock, String> IDtabelaProduto;
    @FXML
    private TableColumn<Stock, String> artigotabelaProduto;
    @FXML
    private TableColumn<Stock, String> descricaotabelaProduto;

    @FXML
    private TextField txtPesquisar;
    private ServiceArmazem serviceArmazem;
    private String nomeArmazemFilter;
    private Integer idArmazem = null;
    @FXML
    private Label labSaldoStock;
    @FXML
    private ComboBox<String> cBoxArmazem;

    @FXML
    private TableView<Item> tabelaItens;

    @FXML
    private TableColumn<Item, String> nometabelaItens;

    @FXML
    private TableColumn<Item, String> precotabelaItens;

    @FXML
    private TableColumn<Item, Integer> quantidadetabelaItens;

    @FXML
    private TableColumn<Item, String> lotetabelaItens;

    @FXML
    private TableColumn<Item, Double> subTotaltabelaItens;

    @FXML
    private TableColumn<Item, String> precoFinaltabelaItens;

    @FXML
    private TableColumn<Item, Integer> idtabelaItens;
    @FXML
    private TableColumn<Item, String> reftabelaItens;

    @FXML
    private TableColumn<Item, String> taxatabelaItens;

    @FXML
    private TableColumn<Item, String> unidadetabelaItens;

    @FXML
    private TextArea txtDescricao;
    @FXML
    private TextField txtPrecovenda;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtQtd_por_unidade;
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtUnidadePorTipo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtID;

    @FXML
    private Label labValidade;
    @FXML
    private Label labDataInicial;

    @FXML
    private Label labTipoCliente;
    @FXML
    private Label labVendedor;
    @FXML
    private Label labTempoRelacaoCliente;
    @FXML
    private TextField txtIDCliente;
    @FXML
    private TextField txtEnderecoCliente;
    @FXML
    private TextField txtNomeCLiente;
    @FXML
    private TextField txtRazaoSocialCLiente;
    @FXML
    private TextField txtNuitCLiente;
    @FXML
    private TextField txtContactoCLiente;
    @FXML
    private TextField txtEmailCLiente;
    @FXML
    private RadioButton rdPagamentoVista;
    @FXML
    private RadioButton rdPagamentoParcelado;

    @FXML
    private ComboBox<String> combTipoDoc;
    @FXML
    private ComboBox<String> combCondPagamento;

    private Map<Integer, Stock> mapaProdutos;

//    private Random randNum;
    public void getTaxasView(ActionEvent e) {
        SysFact.setData(null);
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/taxasView/taxaView.fxml");
    }

    public void getDescontoView(ActionEvent e) {
        SysFact.setData(null);
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/descontoView/descontoView.fxml");
    }

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 2) {
            SysFact.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/searchCliente/clienteSearch.fxml");
        }
    }

    public void getSearchProduto(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            SysFact.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/searchProduto/searchProduto.fxml");
        }
    }

    public void getSearchClienteKEY(KeyEvent keyEvt) throws SQLException {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {

            Cliente c = serviceCliente.getClienteCompletoByID(Integer.valueOf(txtIDCliente.getText()));
            if (c != null) {
                txtIDCliente.setText(c.getId() + "");
                txtEnderecoCliente.setText(c.printEnderecos());
                txtNomeCLiente.setText(c.getNome_cli());
                labTipoCliente.setText(c.getTipo());
                txtRazaoSocialCLiente.setText(c.getRazao_cli());
                labTempoRelacaoCliente.setText(c.getTempoRelacao() + " Dias");
                txtNuitCLiente.setText(c.getNuit_cli() + "");
                txtContactoCLiente.setText(c.printContatos());
                txtEmailCLiente.setText(c.getContactoCliente().getEmail_cli());
                labVendedor.setText(userData.getUsuario());
            }

        }
    }

    private ObservableList<DocumentosComerciais> listaDocComercial;
    private Map<String, DocumentosComerciais> mapaDocComercial;

    public void populateComboFactura() throws SQLException {
        listaDocComercial = serDocs.getDocumentos();
        mapaDocComercial = new HashMap<>();
        List<String> lista = new ArrayList<>();
        for (DocumentosComerciais doc : listaDocComercial) {
            mapaDocComercial.put(doc.getNome_doc(), doc);
            if (doc.getNome_doc().startsWith("Fatura")) {
                lista.add(doc.getNome_doc());
            }
        }
        combTipoDoc.setItems(FXCollections.observableArrayList(lista));
    }

    public void geraFactura(ActionEvent evt) {
        String tipoDoc = combTipoDoc.getSelectionModel().getSelectedItem();
        String condPagamento = combCondPagamento.getSelectionModel().getSelectedItem();
        if (tipoDoc == null || condPagamento == null) {
            AlertUtilities.showDialog("Erro", "Tipo de Factura ou Condicao de pagamento nao selecionado");

        } else {
            DocumentosComerciais doc = mapaDocComercial.get(tipoDoc);
            labNumeroFactura.setText(InvoiceUtilities.invoiceNumber(doc.getId()));
            labValidade.setText(InvoiceUtilities.datePlusXDays(doc.getDiasUteis()));
            combTipoDoc.setDisable(true);
            combCondPagamento.setDisable(true);
            btnGeraFactura.setDisable(true);
            btnNovaFactura.setDisable(false);
        }
    }

    public void novaFactura(ActionEvent evt) {
        combTipoDoc.setDisable(false);
        combCondPagamento.setDisable(false);
        btnGeraFactura.setDisable(false);
        btnNovaFactura.setDisable(true);
        combTipoDoc.setPromptText("Selecionar");
        combCondPagamento.setPromptText("Selecionar");
        labNumeroFactura.setText("...");
        labValidade.setText("...");

    }

//    public void selecionaLote() {
//        String tipoLote = combUnidadeVendida.getSelectionModel().getSelectedItem();
//        if ("Unidade".equals(tipoLote)) {
//            txtQuantidade.setText("1");
//            txtQuantidade.setDisable(true);
//        } else {
//            txtQuantidade.setDisable(false);
//            txtQuantidade.clear();
//        }
//    }
    public void getDataOnFocus() throws SQLException {
        txtIDCliente.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Cliente) {
                        Cliente c = (Cliente) SysFact.getData();
//                        Cliente x = serviceCliente.getClienteCompletoByID(c.getId());
                        if (c != null) {
                            txtIDCliente.setText(c.getId() + "");
                            txtEnderecoCliente.setText(c.printEnderecos());
                            txtNomeCLiente.setText(c.getNome_cli());
                            labTipoCliente.setText(c.getTipo());
                            txtRazaoSocialCLiente.setText(c.getRazao_cli());
                            labTempoRelacaoCliente.setText(c.getTempoRelacao() + " Dias");
                            txtNuitCLiente.setText(c.getNuit_cli() + "");
                            txtContactoCLiente.setText(c.printContatos());
                            txtEmailCLiente.setText(c.getContactoCliente().getEmail_cli());
                            labVendedor.setText(userData.getUsuario());
                        }
                    }
                }
            }
        });

//        txtID.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!oldValue && newValue) {
//                    if (SysFact.getData() instanceof Stock && SysFact.getData() != null) {
//                        Stock x = (Stock) SysFact.getData();
//                        if (x.getPrecoProdutoArmazem().getPrecoBase() != null) {
//                            int num = randNum.nextInt(50);
//                            mapaProdutos.put(num, x);
//                            txtID.setText(num + "");
//                            txtTipo.setText(String.valueOf(x.getProduto().getTipoProduto()));
//                            txtPrecovenda.setText(String.valueOf(x.getPrecoProdutoArmazem().getPrecoVenda()));
//                            txtNome.setText(x.getProduto().getNome());
//                            txtDescricao.setText(x.getProduto().getDescricao());
//                            txtUnidadePorTipo.setText(String.valueOf(x.getProduto().getUnidadesPorTipo()));
//                        }
//                    }
//                }
//            }
//        });
    }

    private void initTableItems() {
        // habilita a tabela a ser editavel
        tabelaItens.setEditable(true);

        idtabelaItens.setCellValueFactory(new PropertyValueFactory<>("id"));
        reftabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduto().getId() + "#" + cellData.getValue().getArmazem().getId()));
        nometabelaItens.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        unidadetabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getUnidadeMedida().getSigla())));

        quantidadetabelaItens.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        quantidadetabelaItens.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantidadetabelaItens.setOnEditCommit(new EventHandler<CellEditEvent<Item, Integer>>() {
            @Override
            public void handle(CellEditEvent<Item, Integer> event) {
                Item item = event.getRowValue(); // Obtém o objeto da linha
                item.setQuantidade(event.getNewValue()); // Atualiza a propriedade
                Item i = new Item();
                i.calculateSubtotal_onTable(item, tabelaItens);
                displayPrice();
            }
        });
        lotetabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getUnidadesPorTipo())));
        precoFinaltabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf("x " + cellData.getValue().getPrecoProdutoArmazem().getPrecoFinal()) + " MT"));
        taxatabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getImposto().getPercentagem()) + "%"));
        subTotaltabelaItens.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableItems();
        LeitorSerialListener leitor = new LeitorSerialListener("COM3", codigo -> {
            System.out.println("Código lido via listener: " + codigo);
        });

        leitor.iniciar();
        // Aqui você poderia manter a aplicação rodando, ou criar lógica para parar depois
        // leitor.parar(); // Chame quando quiser fechar a porta

        this.userData = SysFact.getUserData();
        System.out.println("User: " + userData.getUsuario());
        this.empresaData = SysFact.getEmpresaData();
        try {
            serviceStock = new ServiceStock();
            serviceArmazem = new ServiceArmazem();
            serDesconto = new ServiceDesconto();
            serviceCliente = new ServiceCliente();
            serDocs = new ServiceDocumentos();
            labDataInicial.setText(InvoiceUtilities.actualDate());
            populateComboFactura();
            getDataOnFocus();

            showProdutos();
            populateComb_Armazem();
        } catch (SQLException ex) {
            Logger.getLogger(VendasPorFacturacaoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cBoxArmazem.setOnAction(evt -> {
            String data = cBoxArmazem.getSelectionModel().getSelectedItem();
            idArmazem = mapaArmazens.get(data).getId();
            showProdutos();
        });
        combCondPagamento.setItems(FXCollections.observableArrayList("A vista", "Parcelado"));
    }

    

    public void radioParcelado() {
        String condicao;
        rdPagamentoParcelado.setSelected(true);
        rdPagamentoVista.setSelected(false);
    }

    public void radioVista() {
        String condicao;
        rdPagamentoVista.setSelected(true);
        rdPagamentoParcelado.setSelected(false);
    }

    public String radioCondicaoPagamento() {
        String condicao = "Aberta";
        if (rdPagamentoParcelado.isSelected()) {
            condicao = "Parcelado";
        }
        if (rdPagamentoVista.isSelected()) {
            if (combTipoDoc.getValue().equals("Fatura Proforma")) {
                condicao = "Parcelado";
            } else {
                condicao = "A vista";
            }
        }
        if (combTipoDoc.getValue().equals("Fatura Simplificada") && (!rdPagamentoVista.isSelected() || !rdPagamentoParcelado.isSelected())) {
            condicao = "A vista";
        }
        return condicao;
    }

    public void criarFactura(ActionEvent evt) throws SQLException {
        int cli = Integer.parseInt(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        DocumentosComerciais doc = mapaDocComercial.get(combTipoDoc.getValue());

        ServiceFactura service = new ServiceFactura();
        service.criaFactura(labNumeroFactura.getText(), labDataInicial.getText(),
                labValidade.getText(), Double.valueOf(labTotalGeral.getText()), radioCondicaoPagamento(), doc, cli, empresaData, userData, collectItens());
        //Se a factura for criada com sucesso, sereta os campos
        if (service.isEstado()) {
            if ("Fatura".equals(combTipoDoc.getValue()) || "Fatura Recibo".equals(combTipoDoc.getValue())) {
                if (rdPagamentoVista.isSelected()) {
                    SysFact.setData(labNumeroFactura.getText());
                    geMiniPagamentoView(evt);
                    resetFactura();
                } else {
                    service.geraRelatorio(labNumeroFactura.getText());
                    resetFactura();
                }
            } else if ("Fatura Simplificada".equals(combTipoDoc.getValue())) {
                SysFact.setData(labNumeroFactura.getText());
                geMiniPagamentoView(evt);
                resetFactura();
            } else {
                service.geraRelatorio(labNumeroFactura.getText());
                resetFactura();
            }
            rdPagamentoParcelado.setSelected(false);
            rdPagamentoVista.setSelected(false);
        }
    }

    public List<Item> collectItens() throws SQLException {
        Item i = null;
        ObservableList<Item> itens = tabelaItens.getItems();
        List<Item> listaItems = new ArrayList<>();
        for (Item item : itens) {
            i = new Item();
            Stock p = mapaStock.get(item.getRef());

            i.setQuantidade(item.getQuantidade());
            i.setSubtotal(item.getSubtotal());
            i.setUnidade_vendida(item.getUnidade_vendida());
            i.setQtd_por_unidade(item.getQtd_por_unidade());
            i.setProduto(p.getProduto());
            Factura f = new Factura();
            f.setNumero_fac(labNumeroFactura.getText());
            i.setFactura(f);
            i.setUsuario(userData);
            i.setArmazem(p.getArmazem());
            listaItems.add(i);
        }
        return listaItems;
    }

    public void displayPrice() {
        ObservableList<Item> itens = tabelaItens.getItems();
        double subtotal = 0;
        double desc = 0;
        double taxa = 0;
        double total = 0;
        for (Item item : itens) {
            Stock x = mapaStock.get(item.getRef());
            if (combTipoDoc.getValue().equals("Fatura Simplificada")) {
                subtotal += item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoFinal();
                taxa += (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoVenda()) - (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoBase());
                desc += item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoVenda() - (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoFinal());
                total = subtotal;
            } else {
                subtotal += item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoBase();
                taxa += (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoVenda()) - (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoBase());
                desc += item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoVenda() - (item.getQuantidade() * x.getPrecoProdutoArmazem().getPrecoFinal());
                total = subtotal + taxa - desc;
            }

        }
        labSubtotal.setText(String.format("%.2f", subtotal));
        labDesconto.setText(String.format("%.2f", desc));
        labTaxas.setText(String.format("%.2f", taxa));
        labTotalGeral.setText(String.format("%.2f", total));
    }

    public void removeItens() {
        tabelaItens.getItems().removeAll(tabelaItens.getSelectionModel().getSelectedItem());
        displayPrice();
    }

    public void resetProduto() {
        txtID.setText("");
        txtTipo.setText("");
        txtPrecovenda.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        txtQuantidade.setText("");
    }

    public void resetFactura() {
        tabelaItens.getItems().clear();
        txtIDCliente.setText("");
        txtEnderecoCliente.setText("");
        txtNomeCLiente.setText("");
        txtNuitCLiente.setText("");
        txtContactoCLiente.setText("");
        txtEmailCLiente.setText("");

        labSubtotal.setText("");
        labDesconto.setText("");
        labTaxas.setText("");
        labTotalGeral.setText("");
        labNumeroFactura.setText("");
        labValidade.setText("");
    }

    public void geMiniPagamentoView(ActionEvent evt) {
//        SysFact.setData("2024-1022/3");
//        if (evt.getClickCount() == 1) {
        LoadAndMoveUtilities.showAsPopUP(evt, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/miniPagamentoView/MiniPagamentoView.fxml");
//        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private ObservableList<Stock> listaStock;
    private Map<String, Stock> mapaStock;

    public void showProdutos() {
        listaStock = serviceStock.listaStockProdutos(idArmazem);
        mapaStock = new HashMap<>();
        for (Stock s : listaStock) {
            mapaStock.put(s.getProduto().getId() + "#" + s.getArmazem().getId(), s);
        }

        IDtabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduto().getId() + "#" + cellData.getValue().getArmazem().getId()));
        artigotabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        descricaotabelaProduto.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduto().getDescricao()));

        FilteredList<Stock> dadosFiltrados = new FilteredList<>(listaStock, b -> true);

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getProduto().getNome().toLowerCase().contains(filtroCaixaBaixa)) {
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
    }

    public void selecionaProduto(MouseEvent event) {
        Stock x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            Stock s = mapaStock.get(x.getProduto().getId() + "#" + x.getArmazem().getId());
            labSaldoStock.setText(s.getSaldo() + "");
            if (event.getClickCount() == 2) {
                populateTableItems(s);
            }
        }

    }
    public void populateTableItems(Stock s) {
        int qtd = 1;
        Item i = new Item();
        i.setQuantidade(qtd);
//        popular tabela itens

        i.setRef(s.getProduto().getId() + "#" + s.getArmazem().getId());
        i.setQuantidade(1);
        double subtotal;
        /*
        Condicao a baixo deve ser revisada em funcao do tipo de factura a ser emitida
         */
        if (combTipoDoc.getValue() != null) {
            if (combTipoDoc.getValue().equals("Fatura Simplificada")) {
                subtotal = qtd * s.getPrecoProdutoArmazem().getPrecoFinal();
            } else {
                subtotal = qtd * s.getPrecoProdutoArmazem().getPrecoFinal();
            }
        } else {
            AlertUtilities.showDialog("Erro", "Tipo de documento nao selecionado! Selecione o tipo de factura");
            return;
        }
        i.setSubtotal(subtotal);
        i.setUnidade_vendida(s.getProduto().getTipoProduto());
        i.setQtd_por_unidade(s.getProduto().getUnidadesPorTipo());
        i.setProduto(s.getProduto());
        i.setPrecoProdutoArmazem(s.getPrecoProdutoArmazem());
        Factura f = new Factura();
        f.setNumero_fac(labNumeroFactura.getText());
        i.setFactura(f);
        i.setUsuario(userData);
        Armazem a = new Armazem();
        a.setId(s.getArmazem().getId());
        i.setArmazem(a);

        tabelaItens.getItems().add(i);
//        resetProduto();
        displayPrice();
//        }
    }

    private ObservableList<Armazem> storeList;
    private Map<String, Armazem> mapaArmazens;

    public void populateComb_Armazem() {
        storeList = serviceArmazem.listaTodosArmazens();
        List<String> listaArm = new ArrayList<>();
        mapaArmazens = new HashMap<>();
        for (Armazem a : storeList) {
            listaArm.add(a.getNome_arm());
            mapaArmazens.put(a.getNome_arm(), a);
        }
        cBoxArmazem.setItems(FXCollections.observableArrayList(listaArm));
    }

}
