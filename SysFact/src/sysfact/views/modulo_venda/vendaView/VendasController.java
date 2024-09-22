/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_venda.vendaView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
    private TextField txtIDCliente;

    @FXML
    private TextField txtValor;

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
    
    public void getSearchClienteKEY(KeyEvent keyEvt) {
        if (keyEvt.getCode().equals(KeyCode.ENTER)) {
            System.out.println("YES");
        }
    }
    public void selecionaFactura() {
        labNumeroFactura.setText(MetodosUteis.invoiceNumber(combTipoFactura.getValue()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combTipoFactura.setItems(FXCollections.observableArrayList(
                "Factura Recibo", "Factura Proforma"
        ));
//        System.out.println(SysFact.getData());
//        if (SysFact.getData() == null) {
//            anchorVendas.setDisable(true);
//        } else {
//            labNumeroFactura.setText((String) SysFact.getData());
//        }
    }

}
