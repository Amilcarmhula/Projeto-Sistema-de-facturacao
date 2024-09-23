/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_venda.vendaView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.SysFact;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.desconto.DescontoDAO;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.desconto.Desconto;
import sysfact.model.empresa.Empresa;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.model.vendas.factura.itens.TableModelItems;
import sysfact.util.MetodosUteis;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class VendasController implements Initializable {

    private Empresa empresaData;
    private Usuario userData;

    @FXML
    private AnchorPane anchorVendas;

    @FXML
    private Button btnAddCliente;

    @FXML
    private Button btnAddItem;

    @FXML
    private Button btnCancelaFactura;

    @FXML
    private Button btnFinalizaFactura;

    @FXML
    private Button btnRemoveItem;

    @FXML
    private Button contaItensCarrinho;

    @FXML
    private ComboBox<?> comboMetodoPagamento;

    @FXML
    private ComboBox<?> comboPagemento;

    @FXML
    private ComboBox<String> combDesconto;

    @FXML
    private Label labDesconto;

    @FXML
    private Label labDivida;

    @FXML
    private Label labNumeroFactura;

    @FXML
    private Label labPagamento;

    @FXML
    private Label labSubtotal;

    @FXML
    private Label labTotalFactura;

    @FXML
    private Label labTotalGeral;

    @FXML
    private TableView<TableModelItems> tabelaItens;

    @FXML
    private TableColumn<TableModelItems, String> nometabelaItens;

    @FXML
    private TableColumn<TableModelItems, Double> precotabelaItens;

    @FXML
    private TableColumn<TableModelItems, Integer> quantidadetabelaItens;

    @FXML
    private TableColumn<TableModelItems, Double> subTotaltabelaItens;

    @FXML
    private TableColumn<TableModelItems, Double> descontotabelaItens;

    @FXML
    private TableColumn<TableModelItems, Integer> idtabelaItens;

    @FXML
    private TableColumn<TableModelItems, Double> taxatabelaItens;

    @FXML
    private TableColumn<TableModelItems, String> unidadetabelaItens;

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
    private TextField txtValor;
    @FXML
    private TextField txtTrocos;
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
    private ComboBox<String> combTipoFactura;

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 2) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("searchCliente/clienteSearch.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getSearchProduto(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("searchProduto/produtoSearch.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getSearchClienteKEY(KeyEvent keyEvt) throws SQLException {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {
            ClienteDAO xDAO = new ClienteDAO();
            Cliente x = xDAO.getClieneByID(Integer.parseInt(txtIDCliente.getText()));
            if (x != null) {
                txtIDCliente.setText(x.getId() + "");
                txtEnderecoCliente.setText(x.printEnderecos());
                txtNomeCLiente.setText(x.getApelido());
                txtNuitCLiente.setText(x.getNuit() + "");
                txtContactoCLiente.setText(x.printContatos());
                txtEmailCLiente.setText(x.getContactoCliente().getEmail());
            }

        }
    }

    public void selecionaFactura() {
        labNumeroFactura.setText(MetodosUteis.invoiceNumber(combTipoFactura.getSelectionModel().getSelectedItem()));
        if (combTipoFactura.getValue().equals("Factura Recibo")) {
            labValidade.setText(MetodosUteis.datePlusXDays(0));
        } else if (combTipoFactura.getValue().equals("Factura Proforma")) {
            labValidade.setText(MetodosUteis.datePlusXDays(7));
        }
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
                                txtNomeCLiente.setText(x.getApelido());
                                txtNuitCLiente.setText(x.getNuit() + "");
                                txtContactoCLiente.setText(x.printContatos());
                                txtEmailCLiente.setText(x.getContactoCliente().getEmail());
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
                    if (SysFact.getData() instanceof Produto) {
                        try {
                            mapeaProduto();
                            Produto x = (Produto) SysFact.getData();
                            if (x != null) {
                                txtID.setText(String.valueOf(x.getId()));
                                txtStock.setText(String.valueOf(x.getStock().getSaldo()));
                                txtPrecovenda.setText(String.valueOf(x.getPrecovenda().getPreco()));
                                txtNome.setText(x.getNome());
                                txtDescricao.setText(x.getDescricao());
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void initTableItems() {
        idtabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Integer>("id"));
        nometabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, String>("nome"));
        precotabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Double>("preco"));
        unidadetabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, String>("sigla"));
        quantidadetabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Integer>("qtd"));
        descontotabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Double>("desconto"));
        taxatabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Double>("taxa"));
        subTotaltabelaItens.setCellValueFactory(new PropertyValueFactory<TableModelItems, Double>("subtotal"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableItems();
        this.userData = SysFact.getUserData();
        this.empresaData = SysFact.getEmpresaData();
        try {
            labDataInicial.setText("De: " + MetodosUteis.actualDate());
            combTipoFactura.setItems(FXCollections.observableArrayList(
                    "Factura Recibo", "Factura Proforma"
            ));
            populateComboDesconto();
            mapeaProduto();
            getDataOnFocus();
        } catch (SQLException ex) {
            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList<Desconto> listaDescontos;
    private Map<String, Desconto> mapaDesconto;

    public void populateComboDesconto() throws SQLException {
        DescontoDAO xDAO = new DescontoDAO();
        mapaDesconto = new HashMap<>();
        listaDescontos = xDAO.listAllEntities();
        List<String> lista = new ArrayList<>();
        for (Desconto d : listaDescontos) {
            mapaDesconto.put(d.getNome(), d);
            lista.add(d.getNome());
        }
        combDesconto.setItems(FXCollections.observableArrayList(lista));
    }

    private ObservableList<Produto> listaProduto;
    private Map<Integer, Produto> mapaProdutos;

    public void mapeaProduto() throws SQLException {
        ProdutoDAO aDAO = new ProdutoDAO();
        listaProduto = aDAO.getCombinedEntities();
        mapaProdutos = new HashMap<>();
        for (Produto p : listaProduto) {
            mapaProdutos.put(p.getId(), p);
        }
    }

    public void populateTableItems() {
        int id = Integer.parseInt(txtID.getText());
        Produto x = mapaProdutos.get(id);
        int qtd = Integer.parseInt(txtQuantidade.getText());
        Desconto desconto = mapaDesconto.get(combDesconto.getValue() == null ? "Isento" : combDesconto.getValue());
        TableModelItems data = new TableModelItems();
        data.setId(x.getId());
        data.setNome(x.getNome());
        data.setPreco(x.getPrecovenda().getPreco());
        data.setSigla(x.getUnidade_medida().getSigla());
        data.setDesconto(desconto.getNome());
        data.setQtd(qtd);
        data.setTaxa(x.getTaxa_imposto().getIdentificador());
        double subtotal = qtd * (x.getPrecovenda().getPreco() + x.getPrecovenda().getPreco() * x.getTaxa_imposto().getPercentagem()) * (1 - desconto.getPercentagem());
        data.setSubtotal(subtotal);

        tabelaItens.getItems().add(data);
        resetProduto();
    }

    public void criarFactura() throws SQLException {
        Factura f = new Factura();
        f.setNumero(labNumeroFactura.getText());
        f.setData_vencimento(labValidade.getText());
        f.setEmpresa(empresaData);
        f.setUsuario(userData);
        ClienteDAO xDAO = new ClienteDAO();
        Cliente x = xDAO.getClieneByID(Integer.parseInt(txtIDCliente.getText()));
        f.setCliente(x);
        FacturaDAO fDAO = new FacturaDAO();
        fDAO.addEntity(f);
        registraItens();
    }
    
    public void registraItens() throws SQLException{
        Item i = null;
        ObservableList<TableModelItems> itens = tabelaItens.getItems();
        List<Item> listaItems = new ArrayList<>();
        for (TableModelItems item : itens) {
            i = new Item();
            Produto p = mapaProdutos.get(item.getId());
            i.setProduto(p);
            i.setQuantidade(item.getQtd());
            i.setSubtotal(item.getSubtotal());
            Desconto desconto = mapaDesconto.get(item.getDesconto());
            i.setDesconto(desconto);
            i.setArmazem(p.getArmazem());
            i.setUsuario(userData);
            Factura f = new Factura();
            f.setNumero(labNumeroFactura.getText());
            i.setFactura(f);
            listaItems.add(i);
        }
        ItemDAO iDAO = new ItemDAO();
        iDAO.addEntity(listaItems);
    }

    public void resetProduto() {
        txtID.setText("");
        txtStock.setText("");
        txtPrecovenda.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        txtQuantidade.setText("");
    }

}
