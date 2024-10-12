/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_inventario.produtosView.categoriaAdd;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.controller.CategoriaControle;
import sysfact.dao.categoria.CategoriaDAO;
import sysfact.model.categoria.Categoria;
import sysfact.service.caregoria.ServicoCategoria;
import sysfact.util.MetodosUteis;
import sysfact.views.modulo_venda.vendaView.searchProduto.ProdutoSearchController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class AddCategoriaController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private TableView<Categoria> tabelaCategoria;
    @FXML
    private TableColumn<Categoria, Integer> IDtabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> tipotabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> grupotabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> subgrupotabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> familiatabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> descricaotabelaCategoria;
    @FXML
    private TableColumn<Categoria, String> btntabelaCategoria;

    @FXML
    private TextField ps_txtpesquisar;
    @FXML
    private TextField ps_txtIDCategoria;
    @FXML
    private TextField ps_txtgrupoCategoria;
    @FXML
    private TextField ps_txtsubgrupoCategoria;
    @FXML
    private TextField ps_txtfamiliaCategoria;
    @FXML
    private ComboBox<String> ps_combTipoCategoria;
    @FXML
    private TextArea ps_txtdescricaoCategoria;

   public void close(ActionEvent event) {
        if (ProdutoSearchController.returnToStage()) {
            ProdutoSearchController.setEstadoStage(false);
            ProdutoSearchController.showStage(null,null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (MetodosUteis.returnToBaseAnchor()) {
            MetodosUteis.setEstadoPopUP(false);
            MetodosUteis.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura o TextArea para quebra automática de linha
        ps_txtdescricaoCategoria.setWrapText(true);

        // Opcional: configurar a largura máxima do texto
        //ps_txtdescricaoProduto.setPrefColumnCount(30); // Ajuste conforme necessário
        ps_combTipoCategoria.setItems(FXCollections.observableArrayList("Produto", "Serviço"));
        try {
            showCategoria();
        } catch (SQLException ex) {
            Logger.getLogger(AddCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ----------------------- Produtos e Estoque -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Categoria> listaCategorias;

    public void showCategoria() throws SQLException {

        CategoriaDAO xDAO = new CategoriaDAO();
        ServicoCategoria sc = new ServicoCategoria(xDAO);
        CategoriaControle catController = new CategoriaControle(sc);
        catController.listaTodos();
        listaCategorias = catController.listaTodos();

        IDtabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        grupotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        subgrupotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("subgrupo"));
        familiatabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("familia"));
        descricaotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        //add cell of button edit 
        Callback<TableColumn<Categoria, String>, TableCell<Categoria, String>> cellFoctory = (TableColumn<Categoria, String> param) -> {
            // make cell containing buttons
            final TableCell<Categoria, String> cell = new TableCell<Categoria, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#000000b3;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Categoria a = tabelaCategoria.getSelectionModel().getSelectedItem();
                            System.out.println(a.getId());
                            try {
                                CategoriaDAO xDAO = new CategoriaDAO();
                                xDAO.deleteEntityByUSER(a);
                                showCategoria();
                                resetCategorias();
                            } catch (SQLException ex) {
                                Logger.getLogger(AddCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaCategoria.setCellFactory(cellFoctory);

        FilteredList<Categoria> dadosFiltrados = new FilteredList<>(listaCategorias, b -> true);
        ps_txtpesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getTipo().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getGrupo().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getSubgrupo()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getFamilia()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getDescricao()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Categoria> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaCategoria.comparatorProperty());

//        tabelaCliente.setItems(listaAgentes);
        tabelaCategoria.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCategoria() throws SQLException {
        Categoria x = tabelaCategoria.getSelectionModel().getSelectedItem();
        SysFact.setData(x);
        if (x != null) {
            ps_txtIDCategoria.setText(String.valueOf(x.getId()));
            ps_combTipoCategoria.setValue(x.getTipo());
            ps_txtgrupoCategoria.setText(x.getGrupo());
            ps_txtsubgrupoCategoria.setText(x.getSubgrupo());
            ps_txtfamiliaCategoria.setText(x.getFamilia());
            ps_txtdescricaoCategoria.setText(x.getDescricao());
//            resetCategorias();
        } else {
            resetCategorias();
        }

    }

    /*
        Funcoes para Adicionar informacoes ao banco de dados
     */
    public void addCategoria() throws SQLException {
        CategoriaDAO xDAO = new CategoriaDAO();
        ServicoCategoria sc = new ServicoCategoria(xDAO);
//        CategoriaControle catController = new CategoriaControle(sc);
        sc.registaCategoria(ps_combTipoCategoria.getValue(), ps_txtgrupoCategoria.getText(),
                ps_txtsubgrupoCategoria.getText(), ps_txtfamiliaCategoria.getText(),
                ps_txtdescricaoCategoria.getText());
        
        
//        Categoria a = new Categoria();
//        a.setTipo(ps_combTipoCategoria.getValue());
//        a.setGrupo(ps_txtgrupoCategoria.getText());
//        a.setSubgrupo(ps_txtsubgrupoCategoria.getText());
//        a.setFamilia(ps_txtfamiliaCategoria.getText());
//        a.setDescricao(ps_txtdescricaoCategoria.getText());
//
//        xDAO.addEntity(a);
        resetCategorias();
        showCategoria();
    }

    /*
        Funcoes para Actualizar informacoes ao banco de dados
     */
    public void updateCategoria() throws SQLException {

        CategoriaDAO xDAO = new CategoriaDAO();
        ServicoCategoria sc = new ServicoCategoria(xDAO);
        CategoriaControle catController = new CategoriaControle(sc);
        catController.alteraCategoria(Integer.parseInt(ps_txtIDCategoria.getText()),
                ps_combTipoCategoria.getValue(), ps_txtgrupoCategoria.getText(),
                ps_txtsubgrupoCategoria.getText(), ps_txtfamiliaCategoria.getText(),
                ps_txtdescricaoCategoria.getText());

        resetCategorias();
        showCategoria();
    }

    /*
        Funcoes de resetar as textField
     */
    public void resetCategorias() {
//        ps_combTipoCategoria.getValue();
        ps_txtgrupoCategoria.setText("");
        ps_txtsubgrupoCategoria.setText("");
        ps_txtfamiliaCategoria.setText("");
        ps_txtdescricaoCategoria.setText("");
    }

}
