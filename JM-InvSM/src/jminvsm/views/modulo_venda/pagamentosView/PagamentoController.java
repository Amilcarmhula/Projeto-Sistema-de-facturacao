/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.pagamentosView;

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
import javafx.event.ActionEvent;
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
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.metodoPagamento.ServiceMetodoPagamento;
import jminvsm.service.parcela.ServiceParcelas;
import jminvsm.service.transacoes.ServiceTransacoes;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.util.AlertUtilities;
import jminvsm.util.FilePathFinderUtilities;
import jminvsm.util.LoadAndMoveUtilities;
import jminvsm.views.modulo_venda.vendaView.VendasController;
import static jminvsm.util.AlertUtilities.showErroAlert;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class PagamentoController implements Initializable {

    private Empresa empresaData;
    private Usuario userData;
    private ServiceParcelas serviceParcela;

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
    private TableColumn<Transacao, Double> valorTransRecebido;
    @FXML
    private TableColumn<Transacao, Double> valorTransPago;
    @FXML
    private TableColumn<Transacao, Double> valorTransTrocos;
    @FXML
    private TableColumn<Transacao, String> metodoPagamentoTrans;
    @FXML
    private TableColumn<Transacao, String> dataPagamentoTrans;
    @FXML
    private TableColumn<Transacao, String> dataRegistoTrans;
    @FXML
    private TableColumn<Transacao, String> descricaoTrans;

    @FXML
    private TableView<Parcela> tabelaParcelas;
    @FXML
    private TableColumn<Parcela, Integer> IDParcela;
    @FXML
    private TableColumn<Parcela, Integer> refParcela;
    @FXML
    private TableColumn<Parcela, Double> jurosParcela;
    @FXML
    private TableColumn<Parcela, Double> jurosAtrasoParcela;
    @FXML
    private TableColumn<Parcela, Double> valorParcela;
    @FXML
    private TableColumn<Parcela, Double> valorParcelaPago;

    @FXML
    private TableColumn<Parcela, String> dataVencimentoParcela;
    @FXML
    private TableColumn<Parcela, String> dataPagamentoParcela;
    @FXML
    private TableColumn<Parcela, String> estadoParcela;

    @FXML
    private TextField txtnumeroDeParcelas;
    @FXML
    private TextField txtjurosParcela;
    @FXML
    private ComboBox<String> combIntervaloParcelas;

    @FXML
    private ComboBox<String> combmetodoPagamento;
    @FXML
    private DatePicker dataPagamento;
    @FXML
    private TextField txtValorRecebido;
    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnParcelas;
    @FXML
    private Button btnSimularParcelas;
    @FXML
    private TextField txtDescricao;
    private int id_parcela = 0;

    private final Map<String, Integer> mapaIntervaloParcelas = new HashMap<>();

    public void initIntervalMap() {
        mapaIntervaloParcelas.put("Cada semana", 7);
        mapaIntervaloParcelas.put("Cada duas semanas", 14);
        mapaIntervaloParcelas.put("Mensal", 30);
        List<String> lista = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mapaIntervaloParcelas.entrySet()) {
            lista.add(entry.getKey());
        }
        combIntervaloParcelas.setItems(FXCollections.observableArrayList(lista));
    }

    public void geraParcelas(ActionEvent e) throws SQLException {
        int intervalo;
        if (combIntervaloParcelas.getValue() == null) {
            intervalo = 30;
        } else {
            intervalo = mapaIntervaloParcelas.get(combIntervaloParcelas.getValue());
        }
        int numParcelas = Integer.parseInt(txtnumeroDeParcelas.getText().equals("") ? "1" : txtnumeroDeParcelas.getText());
        double taxaJuros = Double.parseDouble(txtjurosParcela.getText().equals("") ? "0" : txtjurosParcela.getText());
        double saldo = Double.parseDouble(labValorPorPagar.getText().equals("") ? "0" : labValorPorPagar.getText());
        serviceParcela.regista(numParcelas, intervalo, taxaJuros,
                saldo, labNumeroFactura.getText(), userData);
        if (serviceParcela.isOpsSuccess()) {
            showParcelas();
            showFacturas();

        }
    }

    private ObservableList<Parcela> listaParcelas;

    public void showParcelas() {
//        listaParcelas = serviceParcela.consultaParcelas(labNumeroFactura.getText());
        listaParcelas = FXCollections.observableArrayList();
        for (Parcela p : serviceParcela.consultaParcelas(labNumeroFactura.getText())) {
            p.setId_parcela(p.getId_parcela());
            p.setParcelaNumero(p.getParcelaNumero());
            p.setDataPagamento(p.getDataPagamento());
            p.setDataVencimento(p.getDataVencimento());
            p.setEstado(p.getEstado());
            p.setTaxaDeAtraso(p.getTaxaDeAtraso() * 100);
            p.setTaxaJuros(p.getTaxaJuros() * 100);
            p.setValorParcela(p.getValorParcela());
            listaParcelas.add(p);
        }

        IDParcela.setCellValueFactory(new PropertyValueFactory<>("id_parcela"));
        refParcela.setCellValueFactory(new PropertyValueFactory<>("parcelaNumero"));
        jurosParcela.setCellValueFactory(new PropertyValueFactory<>("taxaJuros"));
        jurosAtrasoParcela.setCellValueFactory(new PropertyValueFactory<>("taxaDeAtraso"));
        valorParcela.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));
        valorParcelaPago.setCellValueFactory(new PropertyValueFactory<>("valorPagoParcela"));
        dataVencimentoParcela.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        dataPagamentoParcela.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        estadoParcela.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabelaParcelas.setItems(listaParcelas);
    }

    public void simulaParcelas(ActionEvent e) throws SQLException {
        int intervalo;
        if (combIntervaloParcelas.getValue() == null) {
            intervalo = 30;
        } else {
            intervalo = mapaIntervaloParcelas.get(combIntervaloParcelas.getValue());
        }
        int numParcelas = Integer.parseInt(txtnumeroDeParcelas.getText().equals("") ? "1" : txtnumeroDeParcelas.getText());
        double taxaJuros = Double.parseDouble(txtjurosParcela.getText().equals("") ? "0" : txtjurosParcela.getText());
        double saldo = Double.parseDouble(labValorPorPagar.getText().equals("") ? "0" : labValorPorPagar.getText());
        listaParcelas = serviceParcela.simulaParcelas(numParcelas, intervalo, taxaJuros,
                saldo, labNumeroFactura.getText(), userData);
        showParcelasSimuladas();

    }

    public void showParcelasSimuladas() {
        ObservableList<Parcela> listaAux = FXCollections.observableArrayList();
        if (listaParcelas != null) {
            for (Parcela p : listaParcelas) {
                p.setParcelaNumero(p.getParcelaNumero());
                p.setDataPagamento(p.getDataPagamento());
                p.setDataVencimento(p.getDataVencimento());
                p.setEstado(p.getEstado());
                p.setTaxaDeAtraso(p.getTaxaDeAtraso() * 100);
                p.setTaxaJuros(p.getTaxaJuros() * 100);
                p.setValorParcela(p.getValorParcela());
                listaAux.add(p);
            }
        }

        IDParcela.setCellValueFactory(new PropertyValueFactory<>("id_parcela"));
        refParcela.setCellValueFactory(new PropertyValueFactory<>("parcelaNumero"));
        jurosParcela.setCellValueFactory(new PropertyValueFactory<>("taxaJuros"));
        jurosAtrasoParcela.setCellValueFactory(new PropertyValueFactory<>("taxaDeAtraso"));
        valorParcela.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));
        valorParcelaPago.setCellValueFactory(new PropertyValueFactory<>("valorPagoParcela"));
        dataVencimentoParcela.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        dataPagamentoParcela.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        estadoParcela.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabelaParcelas.setItems(listaAux);
    }

    public void selecionaParcela() {
        Parcela p = tabelaParcelas.getSelectionModel().getSelectedItem();
        if (p != null) {
            id_parcela = p.getId_parcela();
            if (p.getEstado().equals("Pendente") || p.getEstado().equals("Atrasada")) {
                labValorPorPagar.setText((p.getValorParcela() - p.getValorPagoParcela()) + "");
                labValorPorPagar_Aux.setText("Mt em Saldo");
                btnFinalizar.setDisable(false);
            } else {
                labValorPorPagar.setText("");
                labValorPorPagar_Aux.setText("");
                btnFinalizar.setDisable(true);
            }

        }
    }

    public void getSearchCliente(MouseEvent mouseEvt) {
        if (mouseEvt.getClickCount() == 1) {
            String path = "/jminvsm/views/modulo_venda/vendaView/searchCliente/clienteSearch.fxml";
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
            mapaMetodos.put(metodoPagamento.getNomePagamento(), metodoPagamento);
            lista.add(metodoPagamento.getNomePagamento());
        }
        combmetodoPagamento.setItems(FXCollections.observableList(lista));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userData = SysFact.getUserData();
        this.empresaData = SysFact.getEmpresaData();
        valorRecebidoListener();
        initIntervalMap();
        try {
            this.serviceParcela = new ServiceParcelas();
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

            double total = Double.parseDouble(labValorPorPagar.getText().equals("") ? "0" : labValorPorPagar.getText());
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

    public void addTransacao(ActionEvent event) throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        if (combmetodoPagamento.getValue() == null) {
            AlertUtilities.showErroAlert("Metodo de pagamento nao selecionado!");
            return;
        }
        MetodoPagamento metodo = mapaMetodos.get(combmetodoPagamento.getValue());
        if (dataPagamento.getValue() == null) {
            dataPagamento.setValue(LocalDate.now());
        }
        double total = Double.parseDouble(labValorPorPagar.getText());
        double valorRecebido = Double.parseDouble(txtValorRecebido.getText().equals("") ? "0" : txtValorRecebido.getText());
        double valorPago;
        if (metodo.getId() == 1 && total < valorRecebido) {
            valorPago = valorRecebido - getTroco();
        } else {
            valorPago = valorRecebido;
        }

        st.registapagamentoFacturaParcelada(id_parcela, String.valueOf(dataPagamento.getValue()), valorRecebido, valorPago, getTroco(),
                txtDescricao.getText(), empresaData.getContaBancaria().getId(),
                metodo.getId(), labNumeroFactura.getText(), userData);
        if (st.isOpsSuccess()) {
            showTransacoes();
            showParcelas();
            showFacturas();
//            actualizaLabelSaldo();
            txtDescricao.setText("");
            txtValorRecebido.setText("");
            dataPagamento.setValue(null);
        }
    }

//    public void actualizaLabelSaldo() {
//        ObservableList<Factura> dadosTabela = tabelaFactura.getItems();
//        for (Factura factura : dadosTabela) {
//            if (factura.getNumero_fac().equals(labNumeroFactura.getText())) {
//                labValorPorPagar.setText(factura.getSaldoPendente() + "");
//            }
//        }
//    }
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

    public void genarateReport() throws SQLException {
        Factura f = tabelaFactura.getSelectionModel().getSelectedItem();
        if (f != null) {
            serviceParcela.geraRelatorio(f.getNumero_fac());
            selecionaFactura();
        }else{
            AlertUtilities.showErroAlert("Factura nao selecionada!");
        }
    }

    public void selecionaFactura() throws SQLException {
        Factura f = tabelaFactura.getSelectionModel().getSelectedItem();

        if (f != null) {
            labNumeroFactura.setText(f.getNumero_fac());
            System.out.print(labNumeroFactura.getText());
            labValorPorPagar.setText(f.getSaldoPendente() + "");
            if (f.getEstado().equals("Paga") || f.getEstado().equals("Cancelada") || f.getNumeroParcelas() >= 1) {
                btnFinalizar.setDisable(true);
                btnParcelas.setDisable(true);
                btnSimularParcelas.setDisable(true);
            } else {
                btnFinalizar.setDisable(false);
                btnParcelas.setDisable(false);
                btnSimularParcelas.setDisable(false);
            }

            /*Abaixo vem a logica para pesquisar as transacoes da factura*/
            showTransacoes();
            showParcelas();
        }
    }
    private ObservableList<Transacao> listaTransacoes;

    public void showTransacoes() throws SQLException {
        ServiceTransacoes st = new ServiceTransacoes();
        listaTransacoes = st.consultaTransacoes(labNumeroFactura.getText());
        idTrans.setCellValueFactory(new PropertyValueFactory<>("id"));
        valorTransRecebido.setCellValueFactory(new PropertyValueFactory<>("valorRecebido"));
        valorTransPago.setCellValueFactory(new PropertyValueFactory<>("valorPago"));
        valorTransTrocos.setCellValueFactory(new PropertyValueFactory<>("trocos"));
        metodoPagamentoTrans.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMetodo().getNomePagamento())));
        dataPagamentoTrans.setCellValueFactory(new PropertyValueFactory<>("data_pagamento"));
        dataRegistoTrans.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
        descricaoTrans.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabelaTransacoes.setItems(listaTransacoes);
    }

//    public void selecionaTransacao() {
//        Transacao t = tabelaTransacoes.getSelectionModel().getSelectedItem();
//        if (t != null) {
//            dataPagamento.setValue(LocalDate.parse(t.getData_pagamento()));
//            combmetodoPagamento.setValue(t.getMetodo().getNome());
//            txtValorRecebido.setText(t.getValorRecebido() + "");
//            txtDescricao.setText(t.getDescricao());
//            labTrocos.setText(t.getTrocos() + "");
//        }
//    }
}
