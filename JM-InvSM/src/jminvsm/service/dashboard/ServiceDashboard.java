package jminvsm.service.dashboard;

import java.sql.SQLException;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.caixa.ServiceCaixa;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.contacorrente.ServiceContaCorrente;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.produto.ServiceProduto;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;
import jminvsm.service.stock.ServiceStock;
import jminvsm.service.vendas.factura.ServiceFactura;

public class ServiceDashboard {

    public static final String TODAS_CATEGORIAS = "Todas...";
    public static final String TODOS_ARMAZENS = "Todos...";

    private final ServiceFactura serviceFactura;
    private final ServiceCliente serviceCliente;
    private final ServiceStock serviceStock;
    private final ServicoCategoria serviceCategoria;
    private final ServiceArmazem serviceArmazem;
    private final ServiceProduto serviceProduto;
    private final ServiceFornecedor serviceFornecedor;
    private final ServiceProdutoFornecido serviceProdutoFornecido;
    private final ServiceCaixa serviceCaixa;
    private final ServiceContaCorrente serviceContaCorrente;

    public ServiceDashboard() throws SQLException {
        this.serviceFactura = new ServiceFactura();
        this.serviceCliente = new ServiceCliente();
        this.serviceStock = new ServiceStock();
        this.serviceCategoria = new ServicoCategoria();
        this.serviceArmazem = new ServiceArmazem();
        this.serviceFornecedor = new ServiceFornecedor();
        this.serviceProdutoFornecido = new ServiceProdutoFornecido();
        this.serviceCaixa = new ServiceCaixa();
        this.serviceContaCorrente = new ServiceContaCorrente();
        this.serviceProduto = new ServiceProduto();
    }

    public DashboardResumo carregarResumo(String categoriaSeleccionada, String armazemSeleccionado) {
        ObservableList<Categoria> categorias = serviceCategoria.listaCategorias();
        ObservableList<Armazem> armazens = serviceArmazem.listaArmazens();
        Categoria categoria = categoriaSeleccionada(categorias, categoriaSeleccionada);
        Armazem armazem = armazemSeleccionado(armazens, armazemSeleccionado);
        Integer idCategoria = categoria == null ? null : categoria.getId();
        Integer idArmazem = armazem == null ? null : armazem.getId();

        ObservableList<Factura> facturas = serviceFactura.exibeFactura();
        ObservableList<Stock> stock = serviceStock.consultaStock(idCategoria, idArmazem);
        ObservableList<Stock> stockBaixo = serviceStock.stockBaixo();
        DashboardIndicadores indicadores = montarIndicadores(categoriaSeleccionada, armazemSeleccionado,
                categoria, armazem, idCategoria, idArmazem);

        return new DashboardResumo(
                opcoesCategorias(categorias),
                opcoesArmazens(armazens),
                facturas,
                stock,
                stockBaixo,
                indicadores);
    }

    private DashboardIndicadores montarIndicadores(String categoriaSeleccionada, String armazemSeleccionado,
            Categoria categoria, Armazem armazem, Integer idCategoria, Integer idArmazem) {
        Map<String, Integer> movimentos = serviceProdutoFornecido.consultaMovimento(idCategoria, idArmazem);
        String entradas = String.valueOf(movimentos.getOrDefault("entradas", 0));
        String saidas = String.valueOf(movimentos.getOrDefault("saidas", 0));
        String categoriaTexto = categoria == null ? TODAS_CATEGORIAS : categoriaSeleccionada;
        String armazemTexto = armazem == null ? TODOS_ARMAZENS : armazemSeleccionado;
        String descricaoCategoria = categoria == null
                ? "A categoria inclui produtos de todas categorias existentes!"
                : categoria.getDescricao();

        int totalProdutos = serviceProduto.contaProdutos(idCategoria);
        int saldoStock = serviceStock.contaSaldoStock(idCategoria, idArmazem);

        return new DashboardIndicadores(
                entradas,
                saidas,
                String.valueOf(serviceCliente.contaClientes()),
                String.valueOf(serviceFornecedor.contaFornecedores()),
                textoTotalProdutos(categoriaTexto, totalProdutos),
                textoTotalStock(categoria, armazem, categoriaTexto, armazemTexto, saldoStock),
                armazemTexto,
                categoriaTexto,
                descricaoCategoria,
                String.valueOf(serviceFactura.contaFacturas()),
                String.format("%.2f Mt", serviceContaCorrente.consultaContaCorrente()),
                String.format("%.2f Mt", serviceCaixa.consultaCaixa()));
    }

    private ObservableList<String> opcoesCategorias(ObservableList<Categoria> categorias) {
        ObservableList<String> opcoes = FXCollections.observableArrayList();
        opcoes.add(TODAS_CATEGORIAS);
        if (categorias == null) {
            return opcoes;
        }
        for (Categoria categoria : categorias) {
            if (categoria != null && categoria.getFamilia() != null
                    && !"ServiÃ§o".equals(categoria.getFamilia())) {
                opcoes.add(categoria.getFamilia());
            }
        }
        return opcoes;
    }

    private ObservableList<String> opcoesArmazens(ObservableList<Armazem> armazens) {
        ObservableList<String> opcoes = FXCollections.observableArrayList();
        opcoes.add(TODOS_ARMAZENS);
        if (armazens == null) {
            return opcoes;
        }
        for (Armazem armazem : armazens) {
            if (armazem != null && armazem.getNome_arm() != null) {
                opcoes.add(armazem.getNome_arm());
            }
        }
        return opcoes;
    }

    private Categoria categoriaSeleccionada(ObservableList<Categoria> categorias, String nome) {
        if (nome == null || nome.trim().isEmpty() || TODAS_CATEGORIAS.equals(nome) || categorias == null) {
            return null;
        }
        for (Categoria categoria : categorias) {
            if (categoria != null && nome.equals(categoria.getFamilia())) {
                return categoria;
            }
        }
        return null;
    }

    private Armazem armazemSeleccionado(ObservableList<Armazem> armazens, String nome) {
        if (nome == null || nome.trim().isEmpty() || TODOS_ARMAZENS.equals(nome) || armazens == null) {
            return null;
        }
        for (Armazem armazem : armazens) {
            if (armazem != null && nome.equals(armazem.getNome_arm())) {
                return armazem;
            }
        }
        return null;
    }

    private String textoTotalProdutos(String categoriaTexto, int totalProdutos) {
        if (TODAS_CATEGORIAS.equals(categoriaTexto)) {
            return "Existem " + totalProdutos + " produtos em todas categorias.";
        }
        return "Existem " + totalProdutos + " produtos na categoria " + categoriaTexto + ".";
    }

    private String textoTotalStock(Categoria categoria, Armazem armazem, String categoriaTexto,
            String armazemTexto, int saldoStock) {
        if (categoria == null && armazem == null) {
            return "Com " + saldoStock + " itens em stock no em todos armazens.";
        }
        if (categoria != null && armazem == null) {
            return "Com " + saldoStock + " itens em stock na categoria " + categoriaTexto + " em todos armazens.";
        }
        if (categoria == null) {
            return "Com " + saldoStock + " itens em stock no " + armazemTexto + ".";
        }
        return "Com " + saldoStock + " itens em stock na categoria " + categoriaTexto
                + " no " + armazemTexto + ".";
    }
}
