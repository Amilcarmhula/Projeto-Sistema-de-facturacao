/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_venda.vendaView;

import java.io.File;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import sysfact.SysFact;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.desconto.DescontoDAO;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.dao.vendas.documentos.DocumentosComerciaisDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.desconto.Desconto;
import sysfact.model.empresa.Empresa;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.documetos.DocumentosComerciais;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.service.vendas.factura.ServiceFactura;
import sysfact.util.InvoiceUtilities;
import sysfact.util.LoadAndMoveUtilities;
import static sysfact.util.AlertUtilities.showErroNotification;
import sysfact.util.FilePathFinderUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class VendasController implements Initializable {

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
    private ComboBox<String> combDesconto;

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
    private TableColumn<Item, Double> subTotaltabelaItens;

    @FXML
    private TableColumn<Item, String> descontotabelaItens;

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
    private TextField txtStock;
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
    private ComboBox<String> combTipoDoc;

    private Map<Integer, Produto> mapaProdutos;
    
    private Random randNum;

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            String path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "clienteSearch.fxml");
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, path);
        }
    }

    public void getSearchProduto(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            String path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "produtoSearch.fxml");
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, path);
        }
    }

    public void getSearchClienteKEY(KeyEvent keyEvt) throws SQLException {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {
            ClienteDAO xDAO = new ClienteDAO();
            Cliente x = xDAO.getClieneByID(Integer.parseInt(txtIDCliente.getText()));
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

    public void getDataOnFocus() throws SQLException {
        txtIDCliente.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Cliente) {
                        try {
                            Cliente c = (Cliente) SysFact.getData();
                            ClienteDAO xDAO = new ClienteDAO();
                            Cliente x = xDAO.getClieneByID(c.getId());
                            if (x != null) {
                                txtIDCliente.setText(x.getId() + "");
                                txtEnderecoCliente.setText(x.printEnderecos());
                                txtNomeCLiente.setText(x.getRazao_cli());
                                txtNuitCLiente.setText(x.getNuit_cli() + "");
                                txtContactoCLiente.setText(x.printContatos());
                                txtEmailCLiente.setText(x.getContactoCliente().getEmail_cli());
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        txtID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Produto && SysFact.getData() != null) {
                        Produto x = (Produto) SysFact.getData();
                        int num = randNum.nextInt(50);
                        mapaProdutos.put(num, x);

                        txtID.setText(num + "");
                        txtStock.setText(String.valueOf(x.getStock().getSaldo()));
                        txtPrecovenda.setText(String.valueOf(x.getPrecovenda().getPreco()));
                        txtNome.setText(x.getNomeProduto());
                        txtDescricao.setText(x.getDescricao());
                    }
                }
            }
        });
    }

    private void initTableItems() {
        idtabelaItens.setCellValueFactory(new PropertyValueFactory<>("id"));
        nometabelaItens.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        precotabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getPrecovenda().getPreco()) + " MT"));
        unidadetabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getUnidade_medida().getSigla())));
        quantidadetabelaItens.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        descontotabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDesconto().getPercentagemDesconto())));
        taxatabelaItens.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduto().getTaxa_imposto().getPercentagemImposto())));
        subTotaltabelaItens.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        randNum = new Random();
        mapaProdutos = new HashMap<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableItems();

        this.userData = SysFact.getUserData();
        this.empresaData = SysFact.getEmpresaData();
        try {
            labDataInicial.setText(InvoiceUtilities.actualDate());
            populateComboFactura();
            populateComboDesconto();
            getDataOnFocus();
        } catch (SQLException ex) {
            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private ObservableList<DocumentosComerciais> listaDocComercial;
    private Map<String, DocumentosComerciais> mapaDocComercial;

    public void populateComboFactura() throws SQLException {
        DocumentosComerciaisDAO dc = new DocumentosComerciaisDAO();
        listaDocComercial = dc.listAllEntities();
        mapaDocComercial = new HashMap<>();
        List<String> lista = new ArrayList<>();
        for (DocumentosComerciais doc : listaDocComercial) {
            mapaDocComercial.put(doc.getNome_doc(), doc);
            lista.add(doc.getNome_doc());
        }
        combTipoDoc.setItems(FXCollections.observableArrayList(lista));
    }

    private ObservableList<Desconto> listaDescontos;
    private Map<String, Desconto> mapaDesconto;

    public void populateComboDesconto() throws SQLException {
        DescontoDAO xDAO = new DescontoDAO();
        mapaDesconto = new HashMap<>();
        listaDescontos = xDAO.listAllEntities();
        List<String> lista = new ArrayList<>();
        for (Desconto d : listaDescontos) {
            mapaDesconto.put(d.getNomeDesconto(), d);
            lista.add(d.getNomeDesconto());
        }
        combDesconto.setItems(FXCollections.observableArrayList(lista));
    }

    public void populateTableItems() {
        if (txtID.getText() == null || txtID.getText().isEmpty()) {
            showErroNotification("Erro", "Produto/Servico nao selecionado!");
            return;
        }

        if (txtQuantidade.getText() == null || txtQuantidade.getText().isEmpty()) {
            showErroNotification("Erro", "Quantidade nao informada!");
            return;
        }
        int id = Integer.parseInt(txtID.getText());
        int qtd = Integer.parseInt(txtQuantidade.getText());

        Produto x = mapaProdutos.get(id);
        Desconto desconto = mapaDesconto.get(combDesconto.getValue() == null ? "Isento" : combDesconto.getValue());
        Item i = new Item();
        i.setId(id);
        i.setProduto(x);
        i.setDesconto(desconto);
        i.setQuantidade(qtd);
        double taxa = qtd * x.getPrecovenda().getPreco() * x.getTaxa_imposto().getPercentagemImposto();
        double desc = qtd * x.getPrecovenda().getPreco() * desconto.getPercentagemDesconto();
        double subtotal = qtd * x.getPrecovenda().getPreco() + taxa - desc;
        i.setSubtotal(subtotal);
        if (x.getStock().getSaldo() > qtd) {
            tabelaItens.getItems().add(i);
            resetProduto();
            displayPrice();
        } else {
            showErroNotification("Erro", "Stock baixo. Produto nao pode ser comercializado!");
        }

    }

    public void criarFactura() throws SQLException {
        int cli = Integer.parseInt(txtIDCliente.getText().equals("") ? "0" : txtIDCliente.getText());
        DocumentosComerciais doc = mapaDocComercial.get(combTipoDoc.getValue());

        ServiceFactura service = new ServiceFactura();
        service.criaFactura(labNumeroFactura.getText(), labDataInicial.getText(),
                labValidade.getText(), doc, cli, empresaData, userData, collectItens());
        if (service.isEstado()) {
            resetFactura();
        }
    }

    public List<Item> collectItens() {
        Item i = null;
        ObservableList<Item> itens = tabelaItens.getItems();
        List<Item> listaItems = new ArrayList<>();
        for (Item item : itens) {
            i = new Item();
            Produto p = mapaProdutos.get(item.getId());
            i.setProduto(p);
            i.setQuantidade(item.getQuantidade());
            i.setSubtotal(item.getSubtotal());
            Desconto desconto = mapaDesconto.get(item.getDesconto().getNomeDesconto());
            i.setDesconto(desconto);
            i.setArmazem(p.getArmazem());
            i.setUsuario(userData);
            Factura f = new Factura();
            f.setNumero_fac(labNumeroFactura.getText());
            i.setFactura(f);
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
            Produto x = mapaProdutos.get(item.getId());
            subtotal += item.getQuantidade() * x.getPrecovenda().getPreco();
            taxa += item.getQuantidade() * x.getPrecovenda().getPreco() * x.getTaxa_imposto().getPercentagemImposto();
            Desconto desconto = mapaDesconto.get(item.getDesconto().getNomeDesconto());
            desc += item.getQuantidade() * x.getPrecovenda().getPreco() * desconto.getPercentagemDesconto();
            total = subtotal + taxa - desc;
        }
        labSubtotal.setText(String.format("%.2f", subtotal) + " MZN");
        labDesconto.setText(String.format("%.2f", desc) + " MZN");
        labTaxas.setText(String.format("%.2f", taxa) + " MZN");
        labTotalGeral.setText(String.format("%.2f", total) + " MZN");
    }

    public void removeItens() {
        tabelaItens.getItems().removeAll(tabelaItens.getSelectionModel().getSelectedItem());
        displayPrice();
    }

    public void resetProduto() {
        txtID.setText("");
        txtStock.setText("");
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

}
