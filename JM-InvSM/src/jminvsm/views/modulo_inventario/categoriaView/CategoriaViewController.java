/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_inventario.categoriaView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.categoria.Categoria;
import jminvsm.service.ServiceException;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.listagem.ServicePesquisaListagem;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class CategoriaViewController implements Initializable {
    private ServicoCategoria serviceCategoria;
    private ServicePesquisaListagem servicePesquisa;
    
    @FXML
    private Button btnClose;
    
    @FXML
    private Button btnAddCategoria;
    
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
    private TextArea ps_txtdescricaoCategoria;
    
    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            SessaoAplicacao.setData("");
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            SessaoAplicacao.setData("");
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }
    
    public void addORupdateCategoria(ActionEvent e) throws SQLException {
        if ("".equals(ps_txtIDCategoria.getText())) {
            addCategoria(e);
        } else {
            updateCategoria(e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura o TextArea para quebra automÃ¡tica de linha
        ps_txtdescricaoCategoria.setWrapText(true);

        try {
            serviceCategoria = new ServicoCategoria();
            servicePesquisa = new ServicePesquisaListagem();
            showCategoria();
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ----------------------- Produtos e Estoque -------------------------------
    
     */

 /*
        Funcoes para fazer pesquisas no banco de dados buscando todos dados
     */
    private ObservableList<Categoria> listaCategorias;
    private boolean pesquisaConfigurada;
    
    public void showCategoria() throws SQLException {
        listaCategorias = serviceCategoria.listaCategorias();
        
        IDtabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        grupotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        subgrupotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("subgrupo"));
        familiatabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("familia"));
        descricaotabelaCategoria.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        configurarPesquisa();
        aplicarPesquisa();
    }

    private void configurarPesquisa() {
        if (!pesquisaConfigurada) {
            ps_txtpesquisar.textProperty().addListener((observable, oldValue, newValue) -> aplicarPesquisa());
            pesquisaConfigurada = true;
        }
    }

    private void aplicarPesquisa() {
        SortedList<Categoria> sortedData = new SortedList<>(
                servicePesquisa.filtrarCategorias(listaCategorias, ps_txtpesquisar.getText()));
        sortedData.comparatorProperty().bind(tabelaCategoria.comparatorProperty());

//        tabelaCliente.setItems(listaAgentes);
        tabelaCategoria.setItems(sortedData);
    }

    /*
        Funcoes de linha selecionada
     */
    public void selectedLineCategoria(MouseEvent event) throws SQLException {
        Categoria x = tabelaCategoria.getSelectionModel().getSelectedItem();
        
        SessaoAplicacao.setData(x);
        if (x != null) {
            ps_txtIDCategoria.setText(String.valueOf(x.getId()));
            ps_txtgrupoCategoria.setText(x.getGrupo());
            ps_txtsubgrupoCategoria.setText(x.getSubgrupo());
            ps_txtfamiliaCategoria.setText(x.getFamilia());
            ps_txtdescricaoCategoria.setText(x.getDescricao());
            ButtonUtilities.buttonChangeText(btnAddCategoria, ps_txtIDCategoria);
            deleteItem(event, x);
        } else {
            resetCategorias();
        }
    }
    
    public void deleteItem(MouseEvent event, Categoria x) throws SQLException {
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            
            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
                try {
                    serviceCategoria.excluir(x.getId());
                    if (serviceCategoria.isOpsSuccess()) {
                        showSuccessAlert("Registo excluido com sucesso!");
                        showCategoria();
                        resetCategorias();
                    }
                } catch (ServiceException ex) {
                    showErroAlert(ex.getMessage());
                }
                
            } else if (result.get() == btnCancel) {
                resetCategorias();
            }
        }
    }

    /*
        Funcoes para Adicionar informacoes ao banco de dados
     */
    public void addCategoria(ActionEvent e) throws SQLException {
        try {
            serviceCategoria.registar(ps_txtgrupoCategoria.getText(),
                    ps_txtsubgrupoCategoria.getText(), ps_txtfamiliaCategoria.getText(),
                    ps_txtdescricaoCategoria.getText());
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceCategoria.isOpsSuccess()) {
            showSuccessAlert("Categoria registada com sucesso!");
            resetCategorias();
            showCategoria();
        }
        
    }

    /*
        Funcoes para Actualizar informacoes ao banco de dados
     */
    public void updateCategoria(ActionEvent e) throws SQLException {
        try {
            serviceCategoria.actualizar(Integer.parseInt(ps_txtIDCategoria.getText()), ps_txtgrupoCategoria.getText(),
                    ps_txtsubgrupoCategoria.getText(), ps_txtfamiliaCategoria.getText(),
                    ps_txtdescricaoCategoria.getText());
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        
        if (serviceCategoria.isOpsSuccess()) {
            showSuccessAlert("Categoria actualizada com sucesso!");
            resetCategorias();
            showCategoria();
        }
    }

    /*
        Funcoes de resetar as textField
     */
    public void resetCategorias() {
//        ps_combTipoCategoria.getValue();
        ps_txtIDCategoria.setText("");
        ps_txtgrupoCategoria.setText("");
        ps_txtsubgrupoCategoria.setText("");
        ps_txtfamiliaCategoria.setText("");
        ps_txtdescricaoCategoria.setText("");
        ButtonUtilities.buttonChangeText(btnAddCategoria, ps_txtIDCategoria);
        
    }
    
}

