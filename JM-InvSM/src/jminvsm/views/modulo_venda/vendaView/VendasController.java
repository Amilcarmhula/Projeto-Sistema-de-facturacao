/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
import jminvsm.SysFact;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.produto.Produto;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.documentosComerciais.ServiceDocumentos;
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
public class VendasController implements Initializable {

    private ServiceCliente serviceCliente;
    private ServiceDesconto serDesconto;
    private ServiceDocumentos serDocs;

    private Empresa empresaData;
    private Usuario userData;

    @FXML
    private Button btnAddItem;

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
    private TextField txtIDCliente;
    @FXML
    private TextField txtEnderecoCliente;
    @FXML
    private TextField txtNomeCLiente;
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

    private Map<Integer, Stock> mapaProdutos;

    private Random randNum;

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
        if (mouseEvt.getClickCount() == 1) {
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

            Cliente x = serviceCliente.getClienteCompletoByID(Integer.valueOf(txtIDCliente.getText()));
            if (x != null) {
                txtIDCliente.setText(x.getId() + "");
                txtEnderecoCliente.setText(x.printEnderecos());
                txtNomeCLiente.setText(x.getRazao_cli());
                txtNuitCLiente.setText(x.getNuit_cli() + "");
                txtContactoCLiente.setText(x.printContatos());
                txtEmailCLiente.setText(x.getContactoCliente().getEmail_cli());
            }

        }
    }

    public void selecionaFactura() {
        String tipoDoc = combTipoDoc.getSelectionModel().getSelectedItem();
        DocumentosComerciais doc = mapaDocComercial.get(tipoDoc);
        labNumeroFactura.setText(InvoiceUtilities.invoiceNumber(doc.getId()));
        labValidade.setText(InvoiceUtilities.datePlusXDays(doc.getDiasUteis()));

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
                        Cliente x = serviceCliente.getClienteCompletoByID(c.getId());
                        if (x != null) {
                            txtIDCliente.setText(x.getId() + "");
                            txtEnderecoCliente.setText(x.printEnderecos());
                            txtNomeCLiente.setText(x.getRazao_cli());
                            txtNuitCLiente.setText(x.getNuit_cli() + "");
                            txtContactoCLiente.setText(x.printContatos());
                            txtEmailCLiente.setText(x.getContactoCliente().getEmail_cli());
                        }
                    }
                }
            }
        });

        txtID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Stock && SysFact.getData() != null) {
                        Stock x = (Stock) SysFact.getData();
                        if (x.getPrecoVenda().getPrecoNormal() != null) {
                            int num = randNum.nextInt(50);
                            mapaProdutos.put(num, x);
                            txtID.setText(num + "");
                            txtTipo.setText(String.valueOf(x.getProduto().getTipoProduto()));
                            txtPrecovenda.setText(String.valueOf(x.getPrecoVenda().getPrecoVenda()));
                            txtNome.setText(x.getProduto().getNome());
                            txtDescricao.setText(x.getProduto().getDescricao());
                            txtUnidadePorTipo.setText(String.valueOf(x.getProduto().getUnidadesPorTipo()));
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
        nometabelaItens.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        precotabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecoVenda().getPrecoVenda()) + " MT"));
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
//                i.calculateSubtotal_onTable(item,tabelaItens);
                displayPrice();
            }
        });
        lotetabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getTipoProduto())));
        precoFinaltabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf("x " + cellData.getValue().getPrecoVenda().getPrecoFinal()) + " MT"));
        taxatabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getImposto().getPercentagem()) + "%"));
        subTotaltabelaItens.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        randNum = new Random();
        mapaProdutos = new HashMap<>();

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
        this.empresaData = SysFact.getEmpresaData();
        try {
            serDesconto = new ServiceDesconto();
            serviceCliente = new ServiceCliente();
            serDocs = new ServiceDocumentos();
            labDataInicial.setText(InvoiceUtilities.actualDate());
            populateComboFactura();
            getDataOnFocus();
        } catch (SQLException ex) {
            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void populateTableItems(ActionEvent e) {
        int qtd;
        if (txtID.getText() == null || txtID.getText().isEmpty()) {
            showErroAlert("Produto/Servico nao selecionado!");
            return;
        }

        if (txtQuantidade.getText() == null || txtQuantidade.getText().isEmpty()) {
            qtd = 1;
        } else {
            qtd = Integer.parseInt(txtQuantidade.getText());
        }

        int id = Integer.parseInt(txtID.getText());
        Stock x = mapaProdutos.get(id);
        Item i = new Item();
        i.setId(id);
        i.setQuantidade(qtd);
        double subtotal;
        /*
        Condicao a baixo deve ser revisada em funcao do tipo de factura a ser emitida
         */
        if (combTipoDoc.getValue() != null) {
            if (combTipoDoc.getValue().equals("Fatura Simplificada")) {
                subtotal = qtd * x.getPrecoVenda().getPrecoFinal();
            } else {
                subtotal = qtd * x.getPrecoVenda().getPrecoFinal();
            }
        } else {
            AlertUtilities.showDialog("Erro", "Tipo de documento nao selecionado! Selecione o tipo de factura");
            return;
        }
        i.setSubtotal(subtotal);
        i.setUnidade_vendida(x.getProduto().getTipoProduto());
        i.setQtd_por_unidade(x.getProduto().getUnidadesPorTipo());
        i.setProduto(x.getProduto());
        Factura f = new Factura();
        f.setNumero_fac(labNumeroFactura.getText());
        i.setFactura(f);
        i.setUsuario(userData);
        Armazem a = new Armazem();
        a.setId(x.getArmazem().getId());
        i.setArmazem(a);

//        if (x.isControle_stock() == true) {
//            if (/*x.getStock().getSaldo() >*/ qtd < qtd_por_unidade) {
//                tabelaItens.getItems().add(i);
//                resetProduto();
//                displayPrice();
//            } else {
//                AlertUtilities.showDialog("Erro", "Stock baixo. Produto nao pode ser comercializado!");
//            }
//        } else {
        tabelaItens.getItems().add(i);
        resetProduto();
        displayPrice();
//        }
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
            Stock p = mapaProdutos.get(item.getId());
            
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
            Stock x = mapaProdutos.get(item.getId());
            if (combTipoDoc.getValue().equals("Fatura Simplificada")) {
                subtotal += item.getQuantidade() * x.getPrecoVenda().getPrecoFinal();
//                taxa += subtotal - (item.getQuantidade() * x.getProduto().getPrecoVenda().getPrecoNormal());
                taxa += (item.getQuantidade() * x.getPrecoVenda().getPrecoVenda()) - (item.getQuantidade() * x.getPrecoVenda().getPrecoNormal());
                desc += item.getQuantidade() * x.getPrecoVenda().getPrecoVenda() - (item.getQuantidade() * x.getPrecoVenda().getPrecoFinal());
                total = subtotal;
            } else {
                subtotal += item.getQuantidade() * x.getPrecoVenda().getPrecoNormal();
                taxa += (item.getQuantidade() * x.getPrecoVenda().getPrecoVenda()) - (item.getQuantidade() * x.getPrecoVenda().getPrecoNormal());
                desc += item.getQuantidade() * x.getPrecoVenda().getPrecoVenda() - (item.getQuantidade() * x.getPrecoVenda().getPrecoFinal());
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

}
