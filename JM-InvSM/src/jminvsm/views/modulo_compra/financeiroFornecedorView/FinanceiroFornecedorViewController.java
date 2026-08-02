package jminvsm.views.modulo_compra.financeiroFornecedorView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.compra.ContaPagar;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;
import jminvsm.model.compra.MovimentoFinanceiro;
import jminvsm.model.compra.PagamentoFornecedor;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.compra.ServiceContaPagar;
import jminvsm.service.compra.ServiceFacturaFornecedor;
import jminvsm.service.compra.ServiceMovimentoFinanceiro;
import jminvsm.service.compra.ServicePagamentoFornecedor;
import jminvsm.service.compra.ServiceRecebimentoCompra;
import jminvsm.service.empresa.ServiceContaBancaria;
import jminvsm.service.metodoPagamento.ServiceMetodoPagamento;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class FinanceiroFornecedorViewController implements Initializable {

    private ServiceRecebimentoCompra serviceRecebimentoCompra;
    private ServiceFacturaFornecedor serviceFacturaFornecedor;
    private ServiceContaPagar serviceContaPagar;
    private ServicePagamentoFornecedor servicePagamentoFornecedor;
    private ServiceMovimentoFinanceiro serviceMovimentoFinanceiro;
    private ServiceContaBancaria serviceContaBancaria;
    private ServiceMetodoPagamento serviceMetodoPagamento;
    private Usuario userData;

    private ObservableList<FacturaFornecedor> listaFacturas = FXCollections.observableArrayList();
    private ObservableList<ContaPagar> listaContas = FXCollections.observableArrayList();
    private ObservableList<PagamentoFornecedor> listaPagamentos = FXCollections.observableArrayList();
    private ObservableList<MovimentoFinanceiro> listaMovimentos = FXCollections.observableArrayList();
    private FacturaFornecedor facturaSelecionada;
    private ContaPagar contaSelecionada;

    @FXML
    private TabPane tabPaneFinanceiro;
    @FXML
    private Label labResumoCabecalho;

    @FXML
    private ComboBox<RecebimentoCompra> combRecebimento;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private DatePicker dataEmissaoFactura;
    @FXML
    private DatePicker dataVencimentoFactura;
    @FXML
    private TextField txtPesquisaFactura;
    @FXML
    private ComboBox<String> combFiltroFactura;
    @FXML
    private TableView<FacturaFornecedor> tabelaFacturas;
    @FXML
    private TableColumn<FacturaFornecedor, Integer> colFacturaId;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaNumero;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaRecebimento;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaFornecedor;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaEmissao;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaVencimento;
    @FXML
    private TableColumn<FacturaFornecedor, Double> colFacturaTotal;
    @FXML
    private TableColumn<FacturaFornecedor, Double> colFacturaSaldo;
    @FXML
    private TableColumn<FacturaFornecedor, String> colFacturaEstado;
    @FXML
    private TableView<ItemFacturaFornecedor> tabelaItensFactura;
    @FXML
    private TableColumn<ItemFacturaFornecedor, String> colItemProduto;
    @FXML
    private TableColumn<ItemFacturaFornecedor, Integer> colItemQuantidade;
    @FXML
    private TableColumn<ItemFacturaFornecedor, Double> colItemCusto;
    @FXML
    private TableColumn<ItemFacturaFornecedor, Double> colItemSubtotal;
    @FXML
    private TableColumn<ItemFacturaFornecedor, Double> colItemTotal;

    @FXML
    private TextField txtPesquisaConta;
    @FXML
    private ComboBox<String> combFiltroConta;
    @FXML
    private TableView<ContaPagar> tabelaContas;
    @FXML
    private TableColumn<ContaPagar, Integer> colContaId;
    @FXML
    private TableColumn<ContaPagar, String> colContaFactura;
    @FXML
    private TableColumn<ContaPagar, String> colContaFornecedor;
    @FXML
    private TableColumn<ContaPagar, String> colContaEmissao;
    @FXML
    private TableColumn<ContaPagar, String> colContaVencimento;
    @FXML
    private TableColumn<ContaPagar, Double> colContaOriginal;
    @FXML
    private TableColumn<ContaPagar, Double> colContaPago;
    @FXML
    private TableColumn<ContaPagar, Double> colContaSaldo;
    @FXML
    private TableColumn<ContaPagar, String> colContaEstado;
    @FXML
    private Label labContaSelecionada;
    @FXML
    private Label labFornecedorConta;
    @FXML
    private Label labFacturaConta;
    @FXML
    private Label labEstadoConta;
    @FXML
    private Label labSaldoConta;

    @FXML
    private ComboBox<ContaPagar> combContaPagar;
    @FXML
    private DatePicker dataPagamento;
    @FXML
    private TextField txtValorPago;
    @FXML
    private ComboBox<MetodoPagamento> combMetodoPagamento;
    @FXML
    private ComboBox<String> combOrigemPagamento;
    @FXML
    private ComboBox<ContaBancaria> combContaBancaria;
    @FXML
    private TextField txtReferencia;
    @FXML
    private TextArea txtDescricaoPagamento;
    @FXML
    private TableView<PagamentoFornecedor> tabelaPagamentos;
    @FXML
    private TableColumn<PagamentoFornecedor, Integer> colPagamentoId;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoConta;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoFornecedor;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoData;
    @FXML
    private TableColumn<PagamentoFornecedor, Double> colPagamentoValor;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoMetodo;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoOrigem;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoReferencia;
    @FXML
    private TableColumn<PagamentoFornecedor, String> colPagamentoEstado;

    @FXML
    private ComboBox<String> combFiltroMovimentoOrigem;
    @FXML
    private TableView<MovimentoFinanceiro> tabelaMovimentos;
    @FXML
    private TableColumn<MovimentoFinanceiro, Integer> colMovimentoId;
    @FXML
    private TableColumn<MovimentoFinanceiro, String> colMovimentoData;
    @FXML
    private TableColumn<MovimentoFinanceiro, String> colMovimentoTipo;
    @FXML
    private TableColumn<MovimentoFinanceiro, String> colMovimentoOrigem;
    @FXML
    private TableColumn<MovimentoFinanceiro, Double> colMovimentoValor;
    @FXML
    private TableColumn<MovimentoFinanceiro, String> colMovimentoOrigemPagamento;
    @FXML
    private TableColumn<MovimentoFinanceiro, Integer> colMovimentoPagamento;
    @FXML
    private TableColumn<MovimentoFinanceiro, Double> colMovimentoSaldoAnterior;
    @FXML
    private TableColumn<MovimentoFinanceiro, Double> colMovimentoSaldoPosterior;
    @FXML
    private TableColumn<MovimentoFinanceiro, String> colMovimentoFornecedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaFacturas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItensFactura.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaContas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaPagamentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaMovimentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userData = SessaoAplicacao.getUserData();
        inicializarServices();
        configurarCombos();
        configurarTabelas();
        inicializarDatas();
        atualizarDados();
        atualizarCamposConta(null);
    }

    @FXML
    private void criarFacturaFornecedor() {
        if (serviceFacturaFornecedor == null) {
            showErroAlert("Servico de factura do fornecedor indisponivel!");
            return;
        }
        RecebimentoCompra recebimento = combRecebimento.getSelectionModel().getSelectedItem();
        if (recebimento == null) {
            showErroAlert("Seleccione um recebimento confirmado!");
            return;
        }
        if (txtNumeroFactura.getText() == null || txtNumeroFactura.getText().trim().isEmpty()) {
            showErroAlert("Informe o numero da factura do fornecedor!");
            return;
        }
        if (dataEmissaoFactura.getValue() == null) {
            showErroAlert("Informe a data de emissao da factura!");
            return;
        }
        int idUsuario = userData != null ? userData.getId() : 0;
        try {
            boolean criado = serviceFacturaFornecedor.criarDeRecebimento(txtNumeroFactura.getText().trim(),
                    recebimento.getId(), dataEmissaoFactura.getValue().toString(),
                    dataVencimentoFactura.getValue() != null ? dataVencimentoFactura.getValue().toString() : null,
                    idUsuario);
            if (criado) {
                showSuccessAlert("Factura do fornecedor criada com sucesso!");
                limparFormularioFactura();
                atualizarDados();
                tabPaneFinanceiro.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void registarPagamentoFornecedor() {
        if (servicePagamentoFornecedor == null) {
            showErroAlert("Servico de pagamento ao fornecedor indisponivel!");
            return;
        }
        ContaPagar conta = combContaPagar.getSelectionModel().getSelectedItem();
        if (conta == null) {
            conta = tabelaContas.getSelectionModel().getSelectedItem();
        }
        MetodoPagamento metodo = combMetodoPagamento.getSelectionModel().getSelectedItem();
        String origem = combOrigemPagamento.getSelectionModel().getSelectedItem();
        Double valor = parseDouble(txtValorPago.getText());
        if (conta == null || conta.getId() <= 0) {
            showErroAlert("Seleccione uma conta a pagar!");
            return;
        }
        if (valor == null || valor <= 0) {
            showErroAlert("Informe um valor pago valido!");
            return;
        }
        if (valor > conta.getSaldoPendente()) {
            showErroAlert("O valor pago nao pode ser maior que o saldo pendente!");
            return;
        }
        if (metodo == null || metodo.getId() <= 0) {
            showErroAlert("Seleccione o metodo de pagamento!");
            return;
        }
        if (origem == null || origem.trim().isEmpty()) {
            showErroAlert("Seleccione a origem do pagamento!");
            return;
        }
        ContaBancaria contaBancaria = combContaBancaria.getSelectionModel().getSelectedItem();
        Integer idContaBancaria = "Banco".equalsIgnoreCase(origem) && contaBancaria != null ? contaBancaria.getId() : null;
        if ("Banco".equalsIgnoreCase(origem) && (idContaBancaria == null || idContaBancaria <= 0)) {
            showErroAlert("Seleccione a conta bancaria para pagamento por banco!");
            return;
        }
        int idUsuario = userData != null ? userData.getId() : 0;
        try {
            boolean pago = servicePagamentoFornecedor.registarPagamento(conta.getId(),
                    dataPagamento.getValue() != null ? dataPagamento.getValue().toString() : LocalDate.now().toString(),
                    valor, metodo.getId(), origem, idContaBancaria, txtReferencia.getText(),
                    txtDescricaoPagamento.getText(), idUsuario);
            if (pago) {
                showSuccessAlert("Pagamento ao fornecedor registado com sucesso!");
                limparFormularioPagamento();
                atualizarDados();
                selecionarContaPorId(conta.getId());
                tabPaneFinanceiro.getSelectionModel().select(1);
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void atualizarDados() {
        carregarRecebimentos();
        carregarMetodosPagamento();
        carregarContasBancarias();
        carregarFacturas();
        carregarContas();
        carregarPagamentos(null);
        carregarMovimentos();
        atualizarResumoCabecalho();
    }

    @FXML
    private void selecionarFactura(MouseEvent event) {
        FacturaFornecedor factura = tabelaFacturas.getSelectionModel().getSelectedItem();
        setFacturaSelecionada(factura);
    }

    @FXML
    private void selecionarConta(MouseEvent event) {
        ContaPagar conta = tabelaContas.getSelectionModel().getSelectedItem();
        setContaSelecionada(conta);
    }

    @FXML
    private void limparFormularioFactura() {
        combRecebimento.getSelectionModel().clearSelection();
        txtNumeroFactura.clear();
        dataEmissaoFactura.setValue(LocalDate.now());
        dataVencimentoFactura.setValue(LocalDate.now().plusDays(30));
    }

    @FXML
    private void limparFormularioPagamento() {
        combContaPagar.getSelectionModel().clearSelection();
        dataPagamento.setValue(LocalDate.now());
        txtValorPago.clear();
        combMetodoPagamento.getSelectionModel().clearSelection();
        combOrigemPagamento.getSelectionModel().select("Caixa");
        combContaBancaria.getSelectionModel().clearSelection();
        txtReferencia.clear();
        txtDescricaoPagamento.clear();
    }

    private void inicializarServices() {
        try {
            serviceRecebimentoCompra = new ServiceRecebimentoCompra();
            serviceFacturaFornecedor = new ServiceFacturaFornecedor();
            serviceContaPagar = new ServiceContaPagar();
            servicePagamentoFornecedor = new ServicePagamentoFornecedor();
            serviceMovimentoFinanceiro = new ServiceMovimentoFinanceiro();
            serviceContaBancaria = new ServiceContaBancaria();
            serviceMetodoPagamento = new ServiceMetodoPagamento();
        } catch (SQLException ex) {
            Logger.getLogger(FinanceiroFornecedorViewController.class.getName()).log(Level.SEVERE, null, ex);
            showErroAlert("Erro ao inicializar servicos financeiros de fornecedor: " + ex.getMessage());
        }
    }

    private void configurarCombos() {
        combFiltroFactura.setItems(FXCollections.observableArrayList("Todos", "Pendente", "ParcialmentePaga", "Paga", "Cancelada"));
        combFiltroFactura.getSelectionModel().select("Todos");
        combFiltroFactura.setOnAction(event -> carregarFacturas());
        combFiltroConta.setItems(FXCollections.observableArrayList("Todos", "Aberta", "Parcial", "Liquidada", "Vencida", "Cancelada"));
        combFiltroConta.getSelectionModel().select("Todos");
        combFiltroConta.setOnAction(event -> carregarContas());
        combOrigemPagamento.setItems(FXCollections.observableArrayList("Caixa", "Banco"));
        combOrigemPagamento.getSelectionModel().select("Caixa");
        combOrigemPagamento.setOnAction(event -> controlarContaBancaria());
        combFiltroMovimentoOrigem.setItems(FXCollections.observableArrayList("Todos", "Caixa", "Banco"));
        combFiltroMovimentoOrigem.getSelectionModel().select("Todos");
        combFiltroMovimentoOrigem.setOnAction(event -> carregarMovimentos());

        txtPesquisaFactura.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroFacturas());
        txtPesquisaConta.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroContas());
        combContaPagar.setOnAction(event -> {
            ContaPagar conta = combContaPagar.getSelectionModel().getSelectedItem();
            if (conta != null) {
                setContaSelecionada(conta);
            }
        });

        combRecebimento.setConverter(new StringConverter<RecebimentoCompra>() {
            @Override
            public String toString(RecebimentoCompra rec) {
                return rec == null ? "" : "#" + rec.getId() + " - " + safe(rec.getNumero()) + " - " + nomeFornecedor(rec);
            }

            @Override
            public RecebimentoCompra fromString(String string) {
                return null;
            }
        });
        combContaPagar.setConverter(new StringConverter<ContaPagar>() {
            @Override
            public String toString(ContaPagar conta) {
                return conta == null ? "" : "#" + conta.getId() + " - " + numeroFactura(conta) + " - saldo " + conta.getSaldoPendente();
            }

            @Override
            public ContaPagar fromString(String string) {
                return null;
            }
        });
        combMetodoPagamento.setConverter(new StringConverter<MetodoPagamento>() {
            @Override
            public String toString(MetodoPagamento metodo) {
                return metodo == null ? "" : safe(metodo.getNomePagamento());
            }

            @Override
            public MetodoPagamento fromString(String string) {
                return null;
            }
        });
        combContaBancaria.setConverter(new StringConverter<ContaBancaria>() {
            @Override
            public String toString(ContaBancaria conta) {
                return textoContaBancaria(conta);
            }

            @Override
            public ContaBancaria fromString(String string) {
                return null;
            }
        });
    }

    private void configurarTabelas() {
        colFacturaId.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, Integer>("id"));
        colFacturaNumero.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, String>("numero"));
        colFacturaRecebimento.setCellValueFactory(cell -> new SimpleStringProperty(numeroRecebimento(cell.getValue())));
        colFacturaFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(nomeFornecedor(cell.getValue())));
        colFacturaEmissao.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, String>("dataEmissao"));
        colFacturaVencimento.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, String>("dataVencimento"));
        colFacturaTotal.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, Double>("total"));
        colFacturaSaldo.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, Double>("saldoPendente"));
        colFacturaEstado.setCellValueFactory(new PropertyValueFactory<FacturaFornecedor, String>("estado"));

        colItemProduto.setCellValueFactory(cell -> new SimpleStringProperty(nomeProduto(cell.getValue())));
        colItemQuantidade.setCellValueFactory(new PropertyValueFactory<ItemFacturaFornecedor, Integer>("quantidade"));
        colItemCusto.setCellValueFactory(new PropertyValueFactory<ItemFacturaFornecedor, Double>("custoUnitario"));
        colItemSubtotal.setCellValueFactory(new PropertyValueFactory<ItemFacturaFornecedor, Double>("subtotal"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<ItemFacturaFornecedor, Double>("totalLinha"));

        colContaId.setCellValueFactory(new PropertyValueFactory<ContaPagar, Integer>("id"));
        colContaFactura.setCellValueFactory(cell -> new SimpleStringProperty(numeroFactura(cell.getValue())));
        colContaFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(nomeFornecedor(cell.getValue())));
        colContaEmissao.setCellValueFactory(new PropertyValueFactory<ContaPagar, String>("dataEmissao"));
        colContaVencimento.setCellValueFactory(new PropertyValueFactory<ContaPagar, String>("dataVencimento"));
        colContaOriginal.setCellValueFactory(new PropertyValueFactory<ContaPagar, Double>("valorOriginal"));
        colContaPago.setCellValueFactory(new PropertyValueFactory<ContaPagar, Double>("valorPago"));
        colContaSaldo.setCellValueFactory(new PropertyValueFactory<ContaPagar, Double>("saldoPendente"));
        colContaEstado.setCellValueFactory(new PropertyValueFactory<ContaPagar, String>("estado"));

        colPagamentoId.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, Integer>("id"));
        colPagamentoConta.setCellValueFactory(cell -> new SimpleStringProperty(numeroConta(cell.getValue())));
        colPagamentoFornecedor.setCellValueFactory(cell -> new SimpleStringProperty(nomeFornecedor(cell.getValue())));
        colPagamentoData.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, String>("dataPagamento"));
        colPagamentoValor.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, Double>("valorPago"));
        colPagamentoMetodo.setCellValueFactory(cell -> new SimpleStringProperty(nomeMetodo(cell.getValue())));
        colPagamentoOrigem.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, String>("origemPagamento"));
        colPagamentoReferencia.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, String>("referencia"));
        colPagamentoEstado.setCellValueFactory(new PropertyValueFactory<PagamentoFornecedor, String>("estado"));

        colMovimentoId.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, Integer>("id"));
        colMovimentoData.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, String>("dataMovimento"));
        colMovimentoTipo.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, String>("tipoMovimento"));
        colMovimentoOrigem.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, String>("origem"));
        colMovimentoValor.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, Double>("valor"));
        colMovimentoOrigemPagamento.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, String>("origemPagamento"));
        colMovimentoPagamento.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, Integer>("idPagamentoFornecedor"));
        colMovimentoSaldoAnterior.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, Double>("saldoAnterior"));
        colMovimentoSaldoPosterior.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, Double>("saldoPosterior"));
        colMovimentoFornecedor.setCellValueFactory(new PropertyValueFactory<MovimentoFinanceiro, String>("fornecedorNome"));
    }

    private void inicializarDatas() {
        dataEmissaoFactura.setValue(LocalDate.now());
        dataVencimentoFactura.setValue(LocalDate.now().plusDays(30));
        dataPagamento.setValue(LocalDate.now());
    }

    private void carregarRecebimentos() {
        if (serviceRecebimentoCompra == null) {
            return;
        }
        combRecebimento.setItems(serviceRecebimentoCompra.listaRecebimentosPorEstado("Confirmado"));
    }

    private void carregarMetodosPagamento() {
        if (serviceMetodoPagamento == null) {
            return;
        }
        combMetodoPagamento.setItems(serviceMetodoPagamento.listaMetodos());
    }

    private void carregarContasBancarias() {
        if (serviceContaBancaria == null) {
            return;
        }
        combContaBancaria.setItems(serviceContaBancaria.listaContasBancarias());
    }

    private void carregarFacturas() {
        if (serviceFacturaFornecedor == null) {
            return;
        }
        String estado = estadoSelecionado(combFiltroFactura);
        listaFacturas = estado == null ? serviceFacturaFornecedor.listaFacturas() : serviceFacturaFornecedor.listaFacturasPorEstado(estado);
        aplicarFiltroFacturas();
        setFacturaSelecionada(null);
    }

    private void carregarContas() {
        if (serviceContaPagar == null) {
            return;
        }
        String estado = estadoSelecionado(combFiltroConta);
        listaContas = estado == null ? serviceContaPagar.listaContasPagar() : serviceContaPagar.listaContasPagarPorEstado(estado);
        aplicarFiltroContas();
        combContaPagar.setItems(contasPagaveis(listaContas));
        setContaSelecionada(null);
    }

    private void carregarPagamentos(Integer idContaPagar) {
        if (servicePagamentoFornecedor == null) {
            return;
        }
        listaPagamentos = idContaPagar != null && idContaPagar > 0
                ? servicePagamentoFornecedor.listaPagamentosPorContaPagar(idContaPagar)
                : servicePagamentoFornecedor.listaPagamentos();
        tabelaPagamentos.setItems(listaPagamentos);
    }

    private void carregarMovimentos() {
        if (serviceMovimentoFinanceiro == null) {
            return;
        }
        String origem = estadoSelecionado(combFiltroMovimentoOrigem);
        listaMovimentos = origem == null
                ? serviceMovimentoFinanceiro.listaMovimentos()
                : serviceMovimentoFinanceiro.listaMovimentosPorOrigemPagamento(origem);
        tabelaMovimentos.setItems(listaMovimentos);
    }

    private void aplicarFiltroFacturas() {
        FilteredList<FacturaFornecedor> dados = new FilteredList<FacturaFornecedor>(listaFacturas, factura -> true);
        String termo = txtPesquisaFactura != null ? txtPesquisaFactura.getText() : null;
        if (termo != null && !termo.trim().isEmpty()) {
            final String filtro = termo.trim().toLowerCase();
            dados.setPredicate(factura -> facturaContem(factura, filtro));
        }
        SortedList<FacturaFornecedor> ordenado = new SortedList<FacturaFornecedor>(dados);
        ordenado.comparatorProperty().bind(tabelaFacturas.comparatorProperty());
        tabelaFacturas.setItems(ordenado);
    }

    private void aplicarFiltroContas() {
        FilteredList<ContaPagar> dados = new FilteredList<ContaPagar>(listaContas, conta -> true);
        String termo = txtPesquisaConta != null ? txtPesquisaConta.getText() : null;
        if (termo != null && !termo.trim().isEmpty()) {
            final String filtro = termo.trim().toLowerCase();
            dados.setPredicate(conta -> contaContem(conta, filtro));
        }
        SortedList<ContaPagar> ordenado = new SortedList<ContaPagar>(dados);
        ordenado.comparatorProperty().bind(tabelaContas.comparatorProperty());
        tabelaContas.setItems(ordenado);
    }

    private void setFacturaSelecionada(FacturaFornecedor factura) {
        facturaSelecionada = factura;
        if (factura == null || serviceFacturaFornecedor == null) {
            tabelaItensFactura.setItems(FXCollections.<ItemFacturaFornecedor>observableArrayList());
            return;
        }
        tabelaItensFactura.setItems(serviceFacturaFornecedor.consultaItens(factura.getId()));
        labResumoCabecalho.setText("Factura #" + factura.getId() + " - " + safe(factura.getNumero()));
    }

    private void setContaSelecionada(ContaPagar conta) {
        contaSelecionada = conta;
        atualizarCamposConta(conta);
        if (conta != null) {
            selecionarComboConta(conta);
            carregarPagamentos(conta.getId());
            labResumoCabecalho.setText("Conta a pagar #" + conta.getId() + " - saldo " + conta.getSaldoPendente());
        } else {
            carregarPagamentos(null);
        }
    }

    private void atualizarCamposConta(ContaPagar conta) {
        labContaSelecionada.setText(conta != null ? "#" + conta.getId() : "-");
        labFornecedorConta.setText(conta != null ? nomeFornecedor(conta) : "-");
        labFacturaConta.setText(conta != null ? numeroFactura(conta) : "-");
        labEstadoConta.setText(conta != null ? safe(conta.getEstado()) : "-");
        labSaldoConta.setText(conta != null ? String.valueOf(conta.getSaldoPendente()) : "-");
    }

    private void selecionarContaPorId(int idConta) {
        for (ContaPagar conta : listaContas) {
            if (conta.getId() == idConta) {
                setContaSelecionada(conta);
                tabelaContas.getSelectionModel().select(conta);
                return;
            }
        }
    }

    private void selecionarComboConta(ContaPagar conta) {
        if (conta == null) {
            return;
        }
        for (ContaPagar item : combContaPagar.getItems()) {
            if (item.getId() == conta.getId()) {
                combContaPagar.getSelectionModel().select(item);
                return;
            }
        }
    }

    private void controlarContaBancaria() {
        boolean banco = "Banco".equalsIgnoreCase(combOrigemPagamento.getSelectionModel().getSelectedItem());
        combContaBancaria.setDisable(!banco);
        if (!banco) {
            combContaBancaria.getSelectionModel().clearSelection();
        }
    }

    private ObservableList<ContaPagar> contasPagaveis(ObservableList<ContaPagar> contas) {
        ObservableList<ContaPagar> pagaveis = FXCollections.observableArrayList();
        if (contas == null) {
            return pagaveis;
        }
        for (ContaPagar conta : contas) {
            if (conta != null && conta.getSaldoPendente() > 0
                    && !"Liquidada".equalsIgnoreCase(conta.getEstado())
                    && !"Cancelada".equalsIgnoreCase(conta.getEstado())) {
                pagaveis.add(conta);
            }
        }
        return pagaveis;
    }

    private void atualizarResumoCabecalho() {
        double saldo = 0.0;
        for (ContaPagar conta : listaContas) {
            if (conta != null && conta.getSaldoPendente() > 0) {
                saldo += conta.getSaldoPendente();
            }
        }
        labResumoCabecalho.setText("Facturas, contas a pagar, pagamentos e movimento financeiro | Saldo aberto: " + saldo);
    }

    private boolean facturaContem(FacturaFornecedor factura, String termo) {
        return String.valueOf(factura.getId()).contains(termo)
                || contains(factura.getNumero(), termo)
                || contains(factura.getEstado(), termo)
                || contains(nomeFornecedor(factura), termo)
                || contains(numeroRecebimento(factura), termo);
    }

    private boolean contaContem(ContaPagar conta, String termo) {
        return String.valueOf(conta.getId()).contains(termo)
                || contains(conta.getEstado(), termo)
                || contains(nomeFornecedor(conta), termo)
                || contains(numeroFactura(conta), termo);
    }

    private String estadoSelecionado(ComboBox<String> combo) {
        String estado = combo.getSelectionModel().getSelectedItem();
        return estado == null || "Todos".equalsIgnoreCase(estado) ? null : estado;
    }

    private Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Double.valueOf(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean contains(String value, String termo) {
        return value != null && value.toLowerCase().contains(termo);
    }

    private String numeroRecebimento(FacturaFornecedor factura) {
        return factura != null && factura.getRecebimentoCompra() != null
                ? String.valueOf(factura.getRecebimentoCompra().getId()) : "";
    }

    private String nomeFornecedor(RecebimentoCompra recebimento) {
        return recebimento != null && recebimento.getFornecedor() != null
                ? safe(recebimento.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeFornecedor(FacturaFornecedor factura) {
        return factura != null && factura.getFornecedor() != null
                ? safe(factura.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeFornecedor(ContaPagar conta) {
        return conta != null && conta.getFornecedor() != null
                ? safe(conta.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String nomeFornecedor(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getFornecedor() != null
                ? safe(pagamento.getFornecedor().getRazaosocial_forn()) : "";
    }

    private String numeroFactura(ContaPagar conta) {
        return conta != null && conta.getFacturaFornecedor() != null ? safe(conta.getFacturaFornecedor().getNumero()) : "";
    }

    private String numeroConta(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getContaPagar() != null
                ? String.valueOf(pagamento.getContaPagar().getId()) : "";
    }

    private String nomeProduto(ItemFacturaFornecedor item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    private String nomeMetodo(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getMetodoPagamento() != null
                ? safe(pagamento.getMetodoPagamento().getNomePagamento()) : "";
    }

    private String textoContaBancaria(ContaBancaria conta) {
        if (conta == null) {
            return "";
        }
        StringBuilder texto = new StringBuilder();
        texto.append(safe(conta.getNome_banco()));
        if (conta.getNumero_conta() != null && !conta.getNumero_conta().trim().isEmpty()) {
            texto.append(" - ").append(conta.getNumero_conta());
        }
        if (conta.getDescricaoTipo() != null && !conta.getDescricaoTipo().trim().isEmpty()) {
            texto.append(" - ").append(conta.getDescricaoTipo());
        }
        return texto.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

