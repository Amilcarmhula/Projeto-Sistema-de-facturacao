/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sysfact.views.modulo_inventario.produtosView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sysfact.SysFact;
import sysfact.dao.preco.PrecoVendaDAO;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.model.categoria.Categoria;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.model.usuario.Usuario;
import sysfact.service.precovenda.ServicePrecoVenda;
import sysfact.service.produto.Serviceproduto;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class ProdutoController implements Initializable {

    private Usuario userData;

    @FXML
    private TableView<Produto> tabelaProduto;
    @FXML
    private TableColumn<Produto, Integer> ID;
    @FXML
    private TableColumn<Produto, String> categoria;
    @FXML
    private TableColumn<Produto, String> codigobarras;
    @FXML
    private TableColumn<Produto, String> artigo;
    @FXML
    private TableColumn<Produto, String> datafim;
    @FXML
    private TableColumn<Produto, String> datainicio;
    @FXML
    private TableColumn<Produto, String> descricao;
    @FXML
    private TableColumn<Produto, String> preco;
    @FXML
    private TableColumn<Produto, String> quantidade;
    @FXML
    private TableColumn<Produto, String> stock;
    @FXML
    private TableColumn<Produto, String> taxa;
    @FXML
    private TableColumn<Produto, String> unidade;
    @FXML
    private TableColumn<Produto, String> btntabelaProduto;

    @FXML
    private Button btnAddProduto;
    
    @FXML
    private Button btn_controleStock;

    @FXML
    private CheckBox checkControle;
    @FXML
    private DatePicker dataFim;
    @FXML
    private DatePicker dataInicio;
    @FXML
    private Label labCategoria;
    @FXML
    private Label labTaxa;
    @FXML
    private Label labUnidade;
    @FXML
    private Label labIDPreco;
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
    @FXML
    private TextField txtPesquisar;

    public void addORupdate() throws SQLException {
        if ("".equals(txtIDProduto.getText()) && "".equals(labIDPreco.getText())) {
            addProduto();
        } else if (!"".equals(txtIDProduto.getText()) && "0".equals(labIDPreco.getText())) {
            updateProduto();
            addPrecoVendaONSelected();
        } else if (!"".equals(txtIDProduto.getText()) && !"0".equals(labIDPreco.getText())) {
            updateProduto();
            updatePrecoVenda();
        }
    }

    public void estadoBTN() {
        if (!"".equals(txtIDProduto.getText()) || !"".equals(labIDPreco.getText())) {
            btnAddProduto.setText("Actualizar");
        } else {
            btnAddProduto.setText("Adicionar");
        }
    }

    public void priceFormaterProduto() {
        // Define the Locale for currency formatting (Mozambique)
        Locale locale = new Locale("pt", "MZ");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Create a TextFormatter to format the input as currency
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            // Remove all non-numeric characters
            String newText = change.getControlNewText().replaceAll("[^\\d]", "");

            if (newText.isEmpty()) {
                newText = "0";
            }

            try {
                // Convert to a number and format as currency
                double value = Double.parseDouble(newText) / 100.0;
                change.setText(currencyFormat.format(value));
                change.setRange(0, change.getControlText().length()); // Replace the whole text
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }

            return change;
        });

        // Attach the TextFormatter to the TextField
        txtPreco.setTextFormatter(textFormatter);
        txtPreco.setText(currencyFormat.format(0)); // Set initial value

    }
    
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

    public void getStockView() {
            try {
                Parent parente = FXMLLoader.load(getClass().getResource("../stockView/addStock.fxml"));
                Scene scene = new Scene(parente);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
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
                    if (SysFact.getData() instanceof TaxaImposto) {
                        TaxaImposto x = (TaxaImposto) SysFact.getData();
                        if (x != null) {
                            txtIDTaxa.setText(String.valueOf(x.getId()));
                            labTaxa.setText(String.valueOf(x.getIdentificador() + ": " + (x.getPercentagem() * 100) + "%"));
                        }
                    }
                }
            }
        });
        txtIDUnidade.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof UnidadeMedida) {
                        UnidadeMedida x = (UnidadeMedida) SysFact.getData();
                        if (x != null) {
                            txtIDUnidade.setText(String.valueOf(x.getId()));
                            labUnidade.setText(x.getIdentificador() + ": " + x.getSigla());

                        }
                    }
                }
            }
        });
        txtIDCategoria.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Categoria) {
                        Categoria x = (Categoria) SysFact.getData();
                        if (x != null) {
                            txtIDCategoria.setText(String.valueOf(x.getId()));
                            labCategoria.setText(x.getFamilia());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        priceFormaterProduto();
        try {
            this.userData = SysFact.getUserData();
            getData();
            showProduto();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        estadoBTN();
    }

    private ObservableList<Produto> listaProduto;
    private Map<Integer, Produto> mapaProdutos;

    public void showProduto() throws SQLException {
        ProdutoDAO aDAO = new ProdutoDAO();
        listaProduto = aDAO.getCombinedEntities();
        mapaProdutos = new HashMap<>();
        for (Produto p : listaProduto) {
            mapaProdutos.put(p.getId(), p);
        }

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        artigo.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigobarras.setCellValueFactory(new PropertyValueFactory<>("codigo_barra"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        unidade.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getUnidade_medida().getSigla()));
        categoria.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getCategoria().getFamilia()));
        taxa.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getTaxa_imposto().getIdentificador()));
        preco.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecovenda().getPreco()) + "MZN"));
        datainicio.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getPrecovenda().getData_inicio()));
        datafim.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getPrecovenda().getData_fim()));
        quantidade.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStock().getSaldo())));

        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFoctory = (TableColumn<Produto, String> param) -> {
            // make cell containing buttons
            final TableCell<Produto, String> cell = new TableCell<Produto, String>() {
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
                                + "-fx-fill:linear-gradient(to bottom right, #00ff004d 0%, #0000ff80 50%, #ff00004d 100%);"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Produto a = tabelaProduto.getSelectionModel().getSelectedItem();
                            try {
                                ProdutoDAO xDAO = new ProdutoDAO();
//                                xDAO.deleteEntityByUSER(a);
//                                resetProduto();
                                showProduto();
                            } catch (SQLException ex) {
                                Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(deleteIcon);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        btntabelaProduto.setCellFactory(cellFoctory);

        //add cell of checkBox edit 
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFoctory2 = (TableColumn<Produto, String> param) -> {
            // make cell containing buttons
            final TableCell<Produto, String> cell = new TableCell<Produto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        CheckBox check = new CheckBox();
                        check.setDisable(true);
                        Produto produto = getTableView().getItems().get(getIndex());
                        check.setSelected(produto.isContole_stock());

                        setGraphic(check);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        stock.setCellFactory(cellFoctory2);

        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProduto, b -> true);
        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(x -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filtroCaixaBaixa = newValue.toLowerCase();
                if (x.getNome().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (x.getCodigo_barra().toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getUnidade_medida()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else if (String.valueOf(x.getDescricao()).toLowerCase().indexOf(filtroCaixaBaixa) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<Produto> sortedData = new SortedList<>(dadosFiltrados);
        sortedData.comparatorProperty().bind(tabelaProduto.comparatorProperty());

        tabelaProduto.setItems(sortedData);
    }

    public void selectedRow() {
        Produto x = tabelaProduto.getSelectionModel().getSelectedItem();
        if (x != null) {
            Produto p = mapaProdutos.get(x.getId());

            labIDPreco.setText(String.valueOf(p.getPrecovenda().getId()));
            txtPreco.setText(String.valueOf(p.getPrecovenda().getPreco() + "0"));

            if (p.getPrecovenda().getData_inicio() == null) {
                dataInicio.setValue(LocalDate.now());
            } else {
                dataInicio.setValue(LocalDate.parse(p.getPrecovenda().getData_inicio()));
            }
            
            if (p.getPrecovenda().getData_fim()== null) {
                dataFim.setValue(LocalDate.now());
            } else {
                dataFim.setValue(LocalDate.parse(p.getPrecovenda().getData_fim()));
            }
            
            
            checkControle.setSelected(p.isContole_stock());

            labCategoria.setText(p.getCategoria().getFamilia());
            labUnidade.setText(p.getUnidade_medida().getIdentificador() + ": " + p.getUnidade_medida().getSigla());
            labTaxa.setText(p.getTaxa_imposto().getIdentificador() + ": " + (p.getTaxa_imposto().getPercentagem() * 100) + "%");

            txtIDCategoria.setText(String.valueOf(p.getCategoria().getId()));
            txtIDUnidade.setText(String.valueOf(p.getUnidade_medida().getId()));
            txtIDTaxa.setText(String.valueOf(p.getTaxa_imposto().getId()));

            txtIDProduto.setText(String.valueOf(p.getId()));
            txtNomeProduto.setText(p.getNome());
            txtcodigobarrasProduto.setText(p.getCodigo_barra());
            txtdescricaoProduto.setText(p.getDescricao());
            SysFact.setData(p);
            btn_controleStock.setDisable(false);
            estadoBTN();
        }
    }

    public void addProduto() throws SQLException {
        ProdutoDAO xDAO = new ProdutoDAO();
        Serviceproduto sp = new Serviceproduto(xDAO);
        sp.registraProduto(Integer.valueOf(txtIDCategoria.getText()), Integer.valueOf(txtIDUnidade.getText()),
                Integer.valueOf(txtIDTaxa.getText()), txtNomeProduto.getText(), txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText(), checkControle.isSelected());

        addPrecoVenda();
        resetCampos();
    }

    public void addPrecoVenda() throws SQLException {
        PrecoVendaDAO pvDAO = new PrecoVendaDAO();
        ServicePrecoVenda spv = new ServicePrecoVenda(pvDAO);
        ProdutoDAO xDAO = new ProdutoDAO();
        spv.registraPrecoVenda(Double.valueOf(txtPreco.getText().split(" ")[1].replace(".", "").replace(",", ".")),
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, xDAO.getLastEntity());
        showProduto();
        resetCampos();
    }
    
    public void addPrecoVendaONSelected() throws SQLException {
        PrecoVendaDAO pvDAO = new PrecoVendaDAO();
        ServicePrecoVenda spv = new ServicePrecoVenda(pvDAO);
        Produto p = new Produto();
        p.setId(Integer.parseInt(txtIDProduto.getText()));
        spv.registraPrecoVenda(Double.valueOf(txtPreco.getText().split(" ")[1].replace(".", "").replace(",", ".")),
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData, p);
        showProduto();
        resetCampos();
    }

    public void updateProduto() throws SQLException {
        ProdutoDAO xDAO = new ProdutoDAO();
        Serviceproduto sp = new Serviceproduto(xDAO);
        sp.actualizaProduto(Integer.parseInt(txtIDProduto.getText()), Integer.valueOf(txtIDCategoria.getText()), Integer.valueOf(txtIDUnidade.getText()),
                Integer.valueOf(txtIDTaxa.getText()), txtNomeProduto.getText(), txtcodigobarrasProduto.getText(), txtdescricaoProduto.getText());

        showProduto();
    }

    public void updatePrecoVenda() throws SQLException {
        PrecoVendaDAO pvDAO = new PrecoVendaDAO();
        ServicePrecoVenda spv = new ServicePrecoVenda(pvDAO);
        ProdutoDAO xDAO = new ProdutoDAO();
        spv.actualizaPrecoVenda(Integer.parseInt(labIDPreco.getText()), Double.valueOf(txtPreco.getText().split(" ")[1].replace(".", "").replace(",", ".")),
                String.valueOf(dataInicio.getValue()), String.valueOf(dataFim.getValue()),
                userData);
        showProduto();
        resetCampos();

    }

    public void resetCampos() throws SQLException {
        labIDPreco.setText("");
        txtPreco.setText("");
        dataInicio.setValue(LocalDate.now());
        dataFim.setValue(LocalDate.now());
        checkControle.setSelected(false);

        labCategoria.setText("");
        labUnidade.setText("");
        labTaxa.setText("");

        txtIDCategoria.setText("");
        txtIDUnidade.setText("");
        txtIDTaxa.setText("");

        txtIDProduto.setText("");
        txtNomeProduto.setText("");
        txtcodigobarrasProduto.setText("");
        txtdescricaoProduto.setText("");
        btn_controleStock.setDisable(true);
        estadoBTN();
        showProduto();
    }

}
