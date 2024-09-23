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
import sysfact.model.cliente.Cliente;
import sysfact.model.desconto.Desconto;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.util.MetodosUteis;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class VendasController implements Initializable {

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
    private TableView<?> tabelaItens;

    @FXML
    private TableColumn<?, ?> nometabelaItens;

    @FXML
    private TableColumn<?, ?> precotabelaItens;

    @FXML
    private TableColumn<?, ?> quantidadetabelaItens;

    @FXML
    private TableColumn<?, ?> subTotaltabelaItens;

    @FXML
    private TableColumn<?, ?> descontotabelaItens;

    @FXML
    private TableColumn<?, ?> idtabelaItens;

    @FXML
    private TableColumn<?, ?> taxatabelaItens;
    
    @FXML
    private TableColumn<?, ?> unidadetabelaItens;

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
    public void getDataForProduto() throws SQLException {
        populateComboDesconto();
        txtID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Produto) {
                        Produto x = (Produto) SysFact.getData();
                        if (x != null) {
                            txtID.setText(String.valueOf(x.getId()));
                            txtStock.setText(String.valueOf(x.getStock().getSaldo()));
                            txtPrecovenda.setText(String.valueOf(x.getPrecovenda().getPreco()));
                            txtNome.setText(x.getNome());
                            txtDescricao.setText(x.getDescricao());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            labDataInicial.setText("De: " + MetodosUteis.actualDate());
            combTipoFactura.setItems(FXCollections.observableArrayList(
                    "Factura Recibo", "Factura Proforma"
            ));
            populateComboDesconto();
            mapeaProduto();
            getDataForProduto();
        } catch (SQLException ex) {
            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ObservableList<Desconto> listaDescontos;
    private Map<String, Integer> mapaDesconto;
    public void populateComboDesconto() throws SQLException{
        DescontoDAO xDAO = new DescontoDAO();
        mapaDesconto = new HashMap<>();
        listaDescontos = xDAO.listAllEntities();
        List<String> lista = new ArrayList<>();
        for (Desconto d : listaDescontos) {
            mapaDesconto.put(d.getNome(), d.getId());
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

}
