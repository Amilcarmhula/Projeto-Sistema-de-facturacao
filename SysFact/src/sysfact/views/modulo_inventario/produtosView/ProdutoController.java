/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_inventario.produtosView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sysfact.SysFact;
import sysfact.model.categoria.Categoria;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.model.usuario.Usuario;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ProdutoController implements Initializable {

    private Usuario userData;

    @FXML
    private Button btnAddProduto;

    @FXML
    private CheckBox checkControle;

    @FXML
    private DatePicker dataFim;

    @FXML
    private DatePicker dataInicio;
    @FXML
    private TableView<?> tabelaProduto;
    @FXML
    private TableColumn<?, ?> ID;
    @FXML
    private TableColumn<?, ?> categoria;
    @FXML
    private TableColumn<?, ?> codigobarras;
    @FXML
    private TableColumn<?, ?> artigo;
    @FXML
    private TableColumn<?, ?> datafim;
    @FXML
    private TableColumn<?, ?> datainicio;
    @FXML
    private TableColumn<?, ?> descricao;
    @FXML
    private TableColumn<?, ?> preco;
    @FXML
    private TableColumn<?, ?> quantidade;
    @FXML
    private TableColumn<?, ?> stock;
    @FXML
    private TableColumn<?, ?> taxa;
    @FXML
    private TableColumn<?, ?> unidade;
    @FXML
    private TableColumn<?, ?> btntabelaProduto;

    @FXML
    private Label labCategoria;

    @FXML
    private Label labTaxa;

    @FXML
    private Label labUnidade;

    @FXML
    private TextField txtIDCategoria;

    @FXML
    private TextField txtIDProduto;

    @FXML
    private TextField txtIDTaxa;

    @FXML
    private TextField txtIDUnidade;

    @FXML
    private TextField txtNomeProduto;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtcodigobarrasProduto;

    @FXML
    private TextField txtdescricaoProduto;
    
    public void getFamiliaView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("../produtosView/categoriaAdd/addCategoria.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void getUnidadeView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("../../modulo_configs/unidadeView/unidadeView.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void getTaxaView(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("../../modulo_configs/taxasView/taxaView.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void getData() {
        txtIDTaxa.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    TaxaImposto x = (TaxaImposto) SysFact.getData();
                    if (x != null) {
                        txtIDTaxa.setText(String.valueOf(x.getId()));
                        labTaxa.setText(String.valueOf(x.getIdentificador()+": "+(x.getPercentagem()*100)+"%"));
                    }
                }
            }
        });
        txtIDUnidade.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    UnidadeMedida x = (UnidadeMedida) SysFact.getData();
                    if (x != null) {
                        txtIDUnidade.setText(String.valueOf(x.getId()));
                        labUnidade.setText(x.getIdentificador()+": "+x.getSigla());
                    }
                }
            }
        });
        txtIDCategoria.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    Categoria x = (Categoria) SysFact.getData();
                    if (x != null) {
                        txtIDCategoria.setText(String.valueOf(x.getId()));
                        labCategoria.setText(x.getFamilia());
                    }
                }
            }
        });
    }

    @FXML
    void addProduto(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        getData() ;

    }

}
