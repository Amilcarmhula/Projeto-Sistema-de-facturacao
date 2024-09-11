/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.vendas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private TextArea txtDescricaoProdutoServico;

    @FXML
    private TextField txtEnderecoCliente;

    @FXML
    private TextField txtNomeCLiente;

    @FXML
    private TextField txtPesquisaProdutoServico;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtTrocos;

    @FXML
    private TextField txtValor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
