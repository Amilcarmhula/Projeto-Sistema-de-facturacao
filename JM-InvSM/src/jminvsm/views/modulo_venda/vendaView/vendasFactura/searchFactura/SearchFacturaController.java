/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.vendaView.vendasFactura.searchFactura;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.caixa.ServiceCaixa;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.contacorrente.ServiceContaCorrente;
import jminvsm.service.listagem.ServicePesquisaListagem;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class SearchFacturaController implements Initializable {

    private ServiceFactura serviceFactura;
    private ServiceCliente serviceCliente;
    private ServiceCaixa serviceCaixa;
    private ServiceContaCorrente serviceContaCorrente;
    private ServicePesquisaListagem servicePesquisa;

    @FXML
    private TableView<Factura> tabelaFactura;

    @FXML
    private TableColumn<Factura, String> cliente;

    @FXML
    private TableColumn<Factura, String> dataemissao;

    @FXML
    private TableColumn<Factura, String> datavencimento;

    @FXML
    private TableColumn<Factura, String> numero;

    @FXML
    private TableColumn<Factura, String> tipo;

    @FXML
    private TableColumn<Factura, Double> total;

    @FXML
    private TableColumn<Factura, Double> totalPago;

    @FXML
    private TableColumn<Factura, Double> saldoPendente;

    @FXML
    private TableColumn<Factura, String> estado;

    @FXML
    private Label labCliente;
    @FXML
    private Label labTotalFacturas;
    @FXML
    private Label labTotalContaCorrente;
    @FXML
    private Label labTotalCaixa;

    @FXML
    private TextField txtFilter;

    public void close(ActionEvent event) {
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(true);
            LoadAndMoveUtilities.showStage(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(true);
            LoadAndMoveUtilities.showAsPopUP(null, null);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 2) {
            SessaoAplicacao.setData(null);
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, "/jminvsm/views/modulo_venda/vendaView/searchCliente/clienteSearch.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaFactura.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            serviceFactura = new ServiceFactura();
            serviceCliente = new ServiceCliente();
            serviceCaixa = new ServiceCaixa();
            serviceContaCorrente = new ServiceContaCorrente();
            servicePesquisa = new ServicePesquisaListagem();

            showCountCliente();

            showFacturas();

            showCountInvoice();
            showTotalInvoicePaid();

        } catch (SQLException ex) {
            Logger.getLogger(SearchFacturaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ObservableList<Factura> listaFacturas;
    private boolean pesquisaConfigurada;

    public void showFacturas() {
        listaFacturas = serviceFactura.exibeFactura();

        numero.setCellValueFactory(new PropertyValueFactory<>("numero_fac"));
        tipo.setCellValueFactory(cellData
                -> new SimpleStringProperty(servicePesquisa.tipoFactura(cellData.getValue())));
        cliente.setCellValueFactory(cellData
                -> new SimpleStringProperty(servicePesquisa.clienteFactura(cellData.getValue())));
        dataemissao.setCellValueFactory(new PropertyValueFactory<>("data_emissao"));
        datavencimento.setCellValueFactory(new PropertyValueFactory<>("data_vencimento"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalPago.setCellValueFactory(new PropertyValueFactory<>("totalPago"));
        saldoPendente.setCellValueFactory(new PropertyValueFactory<>("saldoPendente"));
        estado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        configurarPesquisa();
        aplicarPesquisa();
    }

    private void configurarPesquisa() {
        if (!pesquisaConfigurada) {
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> aplicarPesquisa());
            pesquisaConfigurada = true;
        }
    }

    private void aplicarPesquisa() {
        SortedList<Factura> sortedData = new SortedList<>(
                servicePesquisa.filtrarFacturas(listaFacturas, txtFilter.getText()));
        sortedData.comparatorProperty().bind(tabelaFactura.comparatorProperty());

        tabelaFactura.setItems(sortedData);
    }

    public void showCountCliente() {
        labCliente.setText(serviceCliente.contaClientes() + "");
    }

    public void showCountInvoice() {
        labTotalFacturas.setText(serviceFactura.contaFacturas() + "");
    }

    public void showTotalInvoicePaid() {
        labTotalCaixa.setText(String.format("%.2f", serviceCaixa.consultaCaixa()) + " Mt");
        labTotalContaCorrente.setText(String.format("%.2f", serviceContaCorrente.consultaContaCorrente()) + " Mt");
    }

    public void selecionaFactura(MouseEvent event) {
        Factura x = tabelaFactura.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && x != null) {
            SessaoAplicacao.setData(x);
            if (LoadAndMoveUtilities.returnToStage()) {
                LoadAndMoveUtilities.setEstadoStage(false);
                LoadAndMoveUtilities.showStage(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
                LoadAndMoveUtilities.setEstadoPopUP(false);
                LoadAndMoveUtilities.showAsPopUP(null, null);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
        }
    }

}

