/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_contabilidade.pagamentosView;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import jminvsm.SysFact;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.metodoPagamento.ServiceMetodoPagamento;
import jminvsm.service.transacoes.ServiceTransacoes;
import jminvsm.service.vendas.factura.ServiceFactura;
import static jminvsm.util.AlertUtilities.showErroNotification;
import jminvsm.util.FilePathFinderUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import jminvsm.views.modulo_venda.vendaView.VendasController;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class PagamentoController implements Initializable {
    private Empresa empresaData;
    private Usuario userData;

    @FXML
    private TextField txtIDCliente;
    @FXML
    private Label labRazaoCliente;
    @FXML
    private Label labNumeroFactura;
    @FXML
    private Label labTrocos;
    @FXML
    private Label labValorPorPagar;
    @FXML
    private Label labValorPorPagar_Aux;

    @FXML
    private TableView<Factura> tabelaFactura;
    @FXML
    private TableColumn<Factura, String> numero;
    @FXML
    private TableColumn<Factura, String> tipo;
    @FXML
    private TableColumn<Factura, Integer> numeroParcelas;
    @FXML
    private TableColumn<Factura, String> dataemissao;
    @FXML
    private TableColumn<Factura, String> datavencimento;
    @FXML
    private TableColumn<Factura, Double> total;
    @FXML
    private TableColumn<Factura, Double> totalPago;
    @FXML
    private TableColumn<Factura, String> saldoPendente;
    @FXML
    private TableColumn<Factura, String> estado;

    @FXML
    private TableView<Transacao> tabelaTransacoes;
    @FXML
    private TableColumn<Transacao, Integer> idTrans;
    @FXML
    private TableColumn<Transacao, Double> valorTrans;
    @FXML
    private TableColumn<Transacao, String> metodoPagamentoTrans;
    @FXML
    private TableColumn<Transacao, String> dataPagamentoTrans;
    @FXML
    private TableColumn<Transacao, String> dataRegistoTrans;
    @FXML
    private TableColumn<Transacao, String> descricaoTrans;

    @FXML
    private ComboBox<String> combmetodoPagamento;
    @FXML
    private DatePicker dataPagamento;
    @FXML
    private TextField txtValorRecebido;
    @FXML
    private Button btnFinalizar;
    @FXML
    private TextField txtDescricao;

    

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            String path = FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "clienteSearch.fxml");
            LoadAndMoveUtilities.showAsPopUP(null, mouseEvt);
            LoadAndMoveUtilities.loadFXML(Modality.APPLICATION_MODAL, path);
        }
    }

    public void getDataOnFocus() throws SQLException {
        txtIDCliente.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    if (SysFact.getData() instanceof Cliente && SysFact.getData() != null) {
                        try {
                            Cliente c = (Cliente) SysFact.getData();
                            txtIDCliente.setText(c.getId() + "");
                            labRazaoCliente.setText(c.getRazao_cli());
                            showFacturas();
                        } catch (SQLException ex) {
                            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }
    private ObservableList<MetodoPagamento> listaMetodosPagamento;
    private Map<String, MetodoPagamento> mapaMetodos;

    public void populateComboMetodoPagamento() throws SQLException {
        ServiceMetodoPagamento sm = new ServiceMetodoPagamento();
        listaMetodosPagamento = sm.listaMetodos();
        mapaMetodos = new HashMap<>();
        List<String> lista = new ArrayList<>();
        for (MetodoPagamento metodoPagamento : listaMetodosPagamento) {
            mapaMetodos.put(metodoPagamento.getNome(), metodoPagamento);
            lista.add(metodoPagamento.getNome());
        }
        combmetodoPagamento.setItems(FXCollections.observableList(lista));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        this.empresaData = SysFact.getEmpresaData();
        valorRecebidoListener();
        try {
            getDataOnFocus();
            populateComboMetodoPagamento();
        } catch (SQLException ex) {
            Logger.getLogger(PagamentoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private double troco;

    public double getTroco() {
        return troco;
    }
    public double valorRecebidoListener() {
        txtValorRecebido.textProperty().addListener(((observable, oldVal, newVal) -> {
            MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());

            double total = Double.parseDouble(labValorPorPagar.getText());
            double valorRecebido = 0;
            if (!newVal.isEmpty()) {
                valorRecebido = Double.parseDouble(newVal);
            }

            if (metodo != null && metodo.getId() == 1 && total < valorRecebido) {
                troco = valorRecebido - total;
                labTrocos.setText(troco + " Mt");
            } else {
                troco = 0;
                labTrocos.setText(troco + " Mt");
            }
        }));
        return troco;
    }
    
    public void addTransacao() throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        if(combmetodoPagamento.getValue()==null){
            showErroNotification("Erro", "Metodo de pagamento nao selecionado!");
            return;
        }
        MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
        if(dataPagamento.getValue() == null){
            dataPagamento.setValue(LocalDate.now());
        }
        double total = Double.parseDouble(labValorPorPagar.getText());
        double valorRecebido = Double.parseDouble(txtValorRecebido.getText().equals("") ? "0" : txtValorRecebido.getText());
        double valorPago;
        if (metodo.getId() == 1 && total < valorRecebido) {
            valorPago = valorRecebido - getTroco();
        }else{
            valorPago = valorRecebido;
        }

        st.registapagamentoFactura(String.valueOf(dataPagamento.getValue()), valorRecebido, valorPago, getTroco(),
                txtDescricao.getText(), empresaData.getContaBancaria().getId(),
                metodo.getId(), labNumeroFactura.getText(), userData);
        if(st.isOpsSuccess()){
            showTransacoes();
            showFacturas();
            actualizaLabelSaldo();
            txtDescricao.setText("");
            txtValorRecebido.setText("");
            dataPagamento.setValue(null);
        }
    }
    public void actualizaLabelSaldo(){
        ObservableList<Factura> dadosTabela = tabelaFactura.getItems();
        for (Factura factura : dadosTabela) {
            if(factura.getNumero_fac().equals(labNumeroFactura.getText())){
                labValorPorPagar.setText(factura.getSaldoPendente()+"");
            }
        }
    }

    private ObservableList<Factura> listaFacturasCliente;

    public void showFacturas() throws SQLException {
        ServiceFactura sf = new ServiceFactura();
        listaFacturasCliente = sf.exibeFacturaPorCliente(Integer.parseInt(txtIDCliente.getText()));

        numero.setCellValueFactory(new PropertyValueFactory<>("numero_fac"));
        tipo.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDocComerciais().getNome_doc()));
        numeroParcelas.setCellValueFactory(new PropertyValueFactory<>("numeroParcelas"));
        dataemissao.setCellValueFactory(new PropertyValueFactory<>("data_emissao"));
        datavencimento.setCellValueFactory(new PropertyValueFactory<>("data_vencimento"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalPago.setCellValueFactory(new PropertyValueFactory<>("totalPago"));
        saldoPendente.setCellValueFactory(new PropertyValueFactory<>("saldoPendente"));
                
        estado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabelaFactura.setItems(listaFacturasCliente);
    }

    public void showInvoice(MouseEvent evt) throws SQLException {
        if (evt.getClickCount() == 2) {
            Factura f = tabelaFactura.getSelectionModel().getSelectedItem();
            f.criaRelatorio(f.getNumero_fac());
        }
        selecionaFactura();
    }

    public void selecionaFactura() throws SQLException {
        Factura f = tabelaFactura.getSelectionModel().getSelectedItem();
        labValorPorPagar.setText(f.getSaldoPendente()+"");
        labValorPorPagar_Aux.setText("Mt de Saldo");
        if (f != null) {
            labNumeroFactura.setText(f.getNumero_fac());
            if(f.getEstado().equals("Paga") || f.getEstado().equals("Cancelada")){
                dataPagamento.setDisable(true);
                combmetodoPagamento.setDisable(true);
                txtValorRecebido.setDisable(true);
                txtDescricao.setDisable(true);
                btnFinalizar.setDisable(true);
            }else{
                dataPagamento.setDisable(false);
                combmetodoPagamento.setDisable(false);
                txtValorRecebido.setDisable(false);
                txtDescricao.setDisable(false);
                btnFinalizar.setDisable(false);
            }
            /*Abaixo vem a logica para pesquisar as transacoes da factura*/
            showTransacoes();
        }
    }
    private ObservableList<Transacao> listaTransacoes;

    public void showTransacoes() throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        listaTransacoes = st.consultaTransacoes(labNumeroFactura.getText());
        idTrans.setCellValueFactory(new PropertyValueFactory<>("id"));
        valorTrans.setCellValueFactory(new PropertyValueFactory<>("valorPago"));
        metodoPagamentoTrans.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMetodo().getNome())));
        dataPagamentoTrans.setCellValueFactory(new PropertyValueFactory<>("data_pagamento"));
        dataRegistoTrans.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
        descricaoTrans.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabelaTransacoes.setItems(listaTransacoes);
    }

}
