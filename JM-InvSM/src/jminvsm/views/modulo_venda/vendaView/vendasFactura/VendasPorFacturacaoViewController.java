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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.util.converter.IntegerStringConverter;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.Documentos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.documentosComerciais.ServiceDocumentos;
import jminvsm.service.ServiceException;
import jminvsm.service.stock.ServiceStock;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import jminvsm.util.AlertUtilities;
import jminvsm.util.InvoiceUtilities;
import jminvsm.util.LoadAndMoveUtilities;

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
    private Button btnConverterCotacao;
    @FXML
    private Button btnCarregarCotacao;
    @FXML
    private Button btnGuardarCotacao;

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
    private ComboBox<String> combTipoDoc;
    @FXML
    private ComboBox<String> combCondPagamento;
    @FXML
    private TextField txtNumeroCotacao;
    private boolean cotacaoEmEdicao;

    private Map<Integer, Stock> mapaProdutos;

//    private Random randNum;
    public void getTaxasView(ActionEvent e) {
        SessaoAplicacao.setData(null);
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_inventario/taxasView/taxaView.fxml");
    }

    public void getDescontoView(ActionEvent e) {
        SessaoAplicacao.setData(null);
        LoadAndMoveUtilities.showAsPopUP(e, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/descontoView/descontoView.fxml");
    }

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 2) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/searchCliente/clienteSearch.fxml");
        }
    }
    
    public void getSearchFactura(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 2) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/vendasFactura/searchFactura/searchFacturaView.fxml");
        }
    }

    public void getSearchProduto(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/searchProduto/searchProduto.fxml");
        }
    }

    public void getSearchClienteKEY(KeyEvent keyEvt) throws SQLException {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {

            Cliente c = serviceCliente.getClienteCompletoByID(Integer.valueOf(txtIDCliente.getText()));
            if (c != null) {
                preencherCliente(c);
            }

        }
    }

    private ObservableList<DocumentosComerciais> listaDocComercial;
    private Map<String, DocumentosComerciais> mapaDocComercial;

    public void populateComboFactura() {
        listaDocComercial = serDocs.getDocumentos();
        mapaDocComercial = new HashMap<>();
        List<String> lista = new ArrayList<>();
        for (DocumentosComerciais doc : listaDocComercial) {
            mapaDocComercial.put(doc.getNome_doc(), doc);
            if (doc.getNome_doc().startsWith("Fatura")) {
                lista.add(doc.getNome_doc());
                System.out.println("****"+doc.getNome_doc());
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

    public void getDataOnFocus() throws SQLException {
        txtIDCliente.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SessaoAplicacao.getData() instanceof Cliente) {
                            Cliente c = (Cliente) SessaoAplicacao.getData();
//                        Cliente x = serviceCliente.getClienteCompletoByID(c.getId());
                        if (c != null) {
                            preencherCliente(c);
                        }
                    }
                }
            }
        });

        txtNumeroCotacao.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SessaoAplicacao.getData() instanceof Factura && SessaoAplicacao.getData() != null) {
                        Factura x = (Factura) SessaoAplicacao.getData();
                        if (x.getNumero_fac() != null) {
                            txtNumeroCotacao.setText(x.getNumero_fac());
                        }
                    }
                }
            }
        });
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
                Item item = event.getRowValue(); // ObtÃ©m o objeto da linha
                item.setQuantidade(event.getNewValue()); // Atualiza a propriedade
                item.calcularSubtotal();
                tabelaItens.refresh();
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
        tabelaItens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initTableItems();
        randNum = new Random();
//        LeitorSerialListener leitor = new LeitorSerialListener("COM3", codigo -> {
//            System.out.println("CÃ³digo lido via listener: " + codigo);
//        });
//
//        leitor.iniciar();
        // Aqui vocÃª poderia manter a aplicaÃ§Ã£o rodando, ou criar lÃ³gica para parar depois
        // leitor.parar(); // Chame quando quiser fechar a porta

        this.userData = SessaoAplicacao.getUserData();
        this.empresaData = SessaoAplicacao.getEmpresaData();
        try {
            serviceStock = new ServiceStock();
            serviceArmazem = new ServiceArmazem();
            serDesconto = new ServiceDesconto();
            serviceCliente = new ServiceCliente();
            serDocs = new ServiceDocumentos();
//            labDataInicial.setText(InvoiceUtilities.actualDate());
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

    

    public void criarFactura(ActionEvent evt) throws SQLException {
        int cli = Integer.parseInt(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        DocumentosComerciais doc = mapaDocComercial.get(combTipoDoc.getValue());

        ServiceFactura service = new ServiceFactura();
        try {
            service.criaFactura(labNumeroFactura.getText(), InvoiceUtilities.actualDate(),
                    labValidade.getText(), Double.valueOf(labTotalGeral.getText()), combCondPagamento.getValue(), doc, cli, empresaData, userData, collectItens());
        } catch (ServiceException ex) {
            AlertUtilities.showErroAlert(ex.getMessage());
            return;
        }
        //Se a factura for criada com sucesso, sereta os campos
        if (service.isEstado()) {
            AlertUtilities.showSuccessAlert("Factura adicionada com sucesso!");
            if ("Fatura".equals(combTipoDoc.getValue()) || "Fatura Recibo".equals(combTipoDoc.getValue()) || "Fatura Simplificada".equals(combTipoDoc.getValue())) {
                if (combCondPagamento.getValue().equals("A vista")) {
                    SessaoAplicacao.setData(labNumeroFactura.getText());
                    geMiniPagamentoView(evt);
                    resetFactura();
                    novaFactura(evt);
                } else {
                    resetFactura();
                }
            } else if ("Fatura Simplificada".equals(combTipoDoc.getValue())) {
                SessaoAplicacao.setData(labNumeroFactura.getText());
                geMiniPagamentoView(evt);
                novaFactura(evt);
//                resetFactura();
            } else {
                resetFactura();
            }
//            rdPagamentoParcelado.setSelected(false);
//            rdPagamentoVista.setSelected(false);
        }
    }

    public void converterCotacao(ActionEvent evt) throws SQLException {
        String numeroCotacao = txtNumeroCotacao == null ? "" : txtNumeroCotacao.getText().trim();
        String numeroFactura = labNumeroFactura.getText() == null ? "" : labNumeroFactura.getText().trim();
        String tipoDoc = combTipoDoc.getSelectionModel().getSelectedItem();
        String condPagamento = combCondPagamento.getSelectionModel().getSelectedItem();

        if (numeroCotacao.isEmpty()) {
            AlertUtilities.showDialog("Erro", "Informe o numero da cotacao/proforma a converter.");
            return;
        }
        if (numeroFactura.isEmpty() || "...".equals(numeroFactura)) {
            AlertUtilities.showDialog("Erro", "Gere primeiro a referencia da factura destino.");
            return;
        }
        if (tipoDoc == null || condPagamento == null) {
            AlertUtilities.showDialog("Erro", "Selecione o tipo de factura destino e a condicao de pagamento.");
            return;
        }

        DocumentosComerciais docDestino = mapaDocComercial.get(tipoDoc);
        if (docDestino == null) {
            AlertUtilities.showDialog("Erro", "Documento comercial destino invalido.");
            return;
        }
        if (isProforma(docDestino)) {
            AlertUtilities.showDialog("Erro", "A factura destino nao pode ser Proforma. Selecione Fatura, Fatura Recibo ou Fatura Simplificada.");
            return;
        }

        if (cotacaoEmEdicao && !guardarCotacaoEditada(false)) {
            return;
        }

        Integer cli = null;
        if (txtIDCliente.getText() != null && !txtIDCliente.getText().trim().isEmpty()) {
            cli = Integer.parseInt(txtIDCliente.getText().trim());
        }

        ServiceFactura service = new ServiceFactura();
        boolean convertido;
        try {
            convertido = service.converterCotacaoEmFactura(numeroCotacao, numeroFactura, InvoiceUtilities.actualDate(),
                    labValidade.getText(), condPagamento, docDestino, cli, empresaData, userData);
        } catch (ServiceException ex) {
            AlertUtilities.showErroAlert(ex.getMessage());
            return;
        }

        if (convertido) {
            AlertUtilities.showSuccessAlert("Cotacao convertida em factura com sucesso!");
            if ("A vista".equals(condPagamento)) {
                SessaoAplicacao.setData(numeroFactura);
                geMiniPagamentoView(evt);
            }
            resetFactura();
            novaFactura(evt);
            if (txtNumeroCotacao != null) {
                txtNumeroCotacao.clear();
            }
            cotacaoEmEdicao = false;
        }
    }

    public void carregarCotacao(ActionEvent evt) throws SQLException {
        String numeroCotacao = txtNumeroCotacao == null ? "" : txtNumeroCotacao.getText().trim();
        if (numeroCotacao.isEmpty()) {
            AlertUtilities.showDialog("Erro", "Informe o numero da cotacao/proforma a carregar.");
            return;
        }

        ServiceFactura serviceFactura = new ServiceFactura();
        Factura cotacao = serviceFactura.consultaFactura(numeroCotacao);
        if (cotacao == null) {
            AlertUtilities.showDialog("Erro", "Cotacao/proforma nao encontrada.");
            return;
        }

        ServiceItem serviceItem = new ServiceItem();
        List<Item> itens;
        try {
            itens = serviceItem.carregarItensCotacaoEditaveis(numeroCotacao);
        } catch (ServiceException ex) {
            AlertUtilities.showErroAlert(ex.getMessage());
            return;
        }
        if (itens == null || itens.isEmpty()) {
            AlertUtilities.showDialog("Erro", "A cotacao nao possui itens para editar.");
            return;
        }

        for (Item item : itens) {
            item.setUsuario(userData);
            if (mapaStock != null && item.getRef() != null && !mapaStock.containsKey(item.getRef())) {
                Stock stock = new Stock();
                stock.setProduto(item.getProduto());
                stock.setArmazem(item.getArmazem());
                stock.setPrecoProdutoArmazem(item.getPrecoProdutoArmazem());
                mapaStock.put(item.getRef(), stock);
            }
        }

        if (cotacao.getCliente() != null && cotacao.getCliente().getId() > 0) {
            Cliente cliente = serviceCliente.getClienteCompletoByID(cotacao.getCliente().getId());
            if (cliente != null) {
                preencherCliente(cliente);
            } else {
                txtIDCliente.setText(String.valueOf(cotacao.getCliente().getId()));
            }
        }

        tabelaItens.getItems().setAll(itens);
        cotacaoEmEdicao = true;
        displayPrice();
    }

    public void guardarCotacao(ActionEvent evt) throws SQLException {
        if (guardarCotacaoEditada(true)) {
            cotacaoEmEdicao = false;
        }
    }

    public List<Item> collectItens() throws SQLException {
        Item i = null;
        ObservableList<Item> itens = tabelaItens.getItems();
        List<Item> listaItems = new ArrayList<>();
        for (Item item : itens) {
            i = new Item();
            Stock p = mapaStock == null ? null : mapaStock.get(item.getRef());
            if ((p == null || p.getProduto() == null || p.getArmazem() == null)
                    && (item.getProduto() == null || item.getArmazem() == null)) {
                throw new SQLException("Item sem produto/armazem valido: " + item.getRef());
            }

            i.setQuantidade(item.getQuantidade());
            i.setSubtotal(item.getSubtotal());
            i.setUnidade_vendida(item.getUnidade_vendida());
            i.setQtd_por_unidade(item.getQtd_por_unidade());
            i.setProduto(p != null ? p.getProduto() : item.getProduto());
            Factura f = new Factura();
            f.setNumero_fac(labNumeroFactura.getText());
            i.setFactura(f);
            i.setUsuario(userData);
            i.setArmazem(p != null ? p.getArmazem() : item.getArmazem());
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
            Stock x = mapaStock == null ? null : mapaStock.get(item.getRef());
            double precoBase = getPrecoBase(item, x);
            double precoVenda = getPrecoVenda(item, x);
            double precoFinal = getPrecoFinal(item, x);
            if ("Fatura Simplificada".equals(combTipoDoc.getValue())) {
                subtotal += item.getQuantidade() * precoFinal;
                taxa += (item.getQuantidade() * precoVenda) - (item.getQuantidade() * precoBase);
                desc += item.getQuantidade() * precoVenda - (item.getQuantidade() * precoFinal);
                total = subtotal;
            } else {
                subtotal += item.getQuantidade() * precoBase;
                taxa += (item.getQuantidade() * precoVenda) - (item.getQuantidade() * precoBase);
                desc += item.getQuantidade() * precoVenda - (item.getQuantidade() * precoFinal);
                total = subtotal + taxa - desc;
            }
            item.setSubtotal(item.getQuantidade() * precoFinal);

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
        cotacaoEmEdicao = false;
    }

    private boolean isProforma(DocumentosComerciais doc) {
        return doc != null && doc.getNome_doc() != null && doc.getNome_doc().toLowerCase().contains("proforma");
    }

    public void geMiniPagamentoView(ActionEvent evt) {
//        SessaoAplicacao.setData("2024-1022/3");
//        if (evt.getClickCount() == 1) {
        LoadAndMoveUtilities.showAsPopUP(evt, null);
        LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/miniPagamentoView/MiniPagamentoView_moderno.fxml");
//        }
    }

    private ObservableList<Stock> listaStock;
    private Map<String, Stock> mapaStock;

    public void showProdutos() {
        listaStock = serviceStock.listaProdutosParaVenda(idArmazem);
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
            labSaldoStock.setText(s.getSaldo() > 0 ? String.valueOf(s.getSaldo()) : "N/A");
            if (event.getClickCount() == 2) {
                populateTableItems(s);
            }
        }

    }
    
    private Random randNum;
    public void populateTableItems(Stock s) {
        int qtd = 1;
        Item i = new Item();
        int num = randNum.nextInt(50);
        i.setId(num);
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

    private boolean guardarCotacaoEditada(boolean exibirMensagem) throws SQLException {
        String numeroCotacao = txtNumeroCotacao == null ? "" : txtNumeroCotacao.getText().trim();
        if (numeroCotacao.isEmpty()) {
            AlertUtilities.showDialog("Erro", "Informe o numero da cotacao/proforma a guardar.");
            return false;
        }
        List<Item> itens = prepararItensCotacao(numeroCotacao);
        ServiceItem serviceItem = new ServiceItem();
        boolean guardado;
        try {
            guardado = serviceItem.guardarItensCotacao(numeroCotacao, itens);
        } catch (ServiceException ex) {
            AlertUtilities.showErroAlert(ex.getMessage());
            return false;
        }
        if (guardado && exibirMensagem) {
            AlertUtilities.showDialog("Sucesso", "Cotacao actualizada com sucesso.");
        }
        return guardado;
    }

    private List<Item> prepararItensCotacao(String numeroCotacao) throws SQLException {
        ObservableList<Item> itens = tabelaItens.getItems();
        List<Item> listaItems = new ArrayList<>();
        if (itens == null || itens.isEmpty()) {
            throw new SQLException("A cotacao deve possuir pelo menos um item.");
        }

        for (Item item : itens) {
            Stock stock = mapaStock == null ? null : mapaStock.get(item.getRef());
            Item itemCotacao = new Item();
            itemCotacao.setQuantidade(item.getQuantidade());
            itemCotacao.setSubtotal(item.getSubtotal());
            itemCotacao.setUnidade_vendida(item.getUnidade_vendida());
            itemCotacao.setQtd_por_unidade(item.getQtd_por_unidade());
            itemCotacao.setProduto(stock != null ? stock.getProduto() : item.getProduto());
            itemCotacao.setArmazem(stock != null ? stock.getArmazem() : item.getArmazem());
            itemCotacao.setUsuario(userData);
            Factura factura = new Factura();
            factura.setNumero_fac(numeroCotacao);
            itemCotacao.setFactura(factura);
            listaItems.add(itemCotacao);
        }
        return listaItems;
    }

    private void preencherCliente(Cliente c) {
        txtIDCliente.setText(c.getId() + "");
        txtEnderecoCliente.setText(c.printEnderecos());
        txtNomeCLiente.setText(c.getNome_cli());
        labTipoCliente.setText(c.getTipo());
        txtRazaoSocialCLiente.setText(c.getRazao_cli());
        labTempoRelacaoCliente.setText(c.getTempoRelacao() + " Dias");
        txtNuitCLiente.setText(c.getNuit_cli() + "");
        txtContactoCLiente.setText(c.printContatos());
        txtEmailCLiente.setText(c.getContactoCliente() == null ? "" : c.getContactoCliente().getEmail_cli());
        labVendedor.setText(userData.getUsuario());
    }

    private double getPrecoBase(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoBase() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoBase();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoBase() != null) {
            return item.getPrecoProdutoArmazem().getPrecoBase();
        }
        return item.getSubtotal();
    }

    private double getPrecoVenda(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoVenda() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoVenda();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoVenda() != null) {
            return item.getPrecoProdutoArmazem().getPrecoVenda();
        }
        return item.getSubtotal();
    }

    private double getPrecoFinal(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoFinal() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoFinal();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoFinal() != null) {
            return item.getPrecoProdutoArmazem().getPrecoFinal();
        }
        return item.getQuantidade() > 0 ? item.getSubtotal() / item.getQuantidade() : item.getSubtotal();
    }

}

