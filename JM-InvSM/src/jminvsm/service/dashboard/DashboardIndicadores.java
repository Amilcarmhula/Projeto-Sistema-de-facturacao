package jminvsm.service.dashboard;

public class DashboardIndicadores {

    private final String entradas;
    private final String saidas;
    private final String clientes;
    private final String fornecedores;
    private final String totalProdutos;
    private final String totalStock;
    private final String armazem;
    private final String categoria;
    private final String descricaoCategoria;
    private final String totalFacturas;
    private final String totalContaCorrente;
    private final String totalCaixa;

    public DashboardIndicadores(String entradas, String saidas, String clientes, String fornecedores,
            String totalProdutos, String totalStock, String armazem, String categoria,
            String descricaoCategoria, String totalFacturas, String totalContaCorrente, String totalCaixa) {
        this.entradas = entradas;
        this.saidas = saidas;
        this.clientes = clientes;
        this.fornecedores = fornecedores;
        this.totalProdutos = totalProdutos;
        this.totalStock = totalStock;
        this.armazem = armazem;
        this.categoria = categoria;
        this.descricaoCategoria = descricaoCategoria;
        this.totalFacturas = totalFacturas;
        this.totalContaCorrente = totalContaCorrente;
        this.totalCaixa = totalCaixa;
    }

    public String getEntradas() {
        return entradas;
    }

    public String getSaidas() {
        return saidas;
    }

    public String getClientes() {
        return clientes;
    }

    public String getFornecedores() {
        return fornecedores;
    }

    public String getTotalProdutos() {
        return totalProdutos;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public String getArmazem() {
        return armazem;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public String getTotalFacturas() {
        return totalFacturas;
    }

    public String getTotalContaCorrente() {
        return totalContaCorrente;
    }

    public String getTotalCaixa() {
        return totalCaixa;
    }
}
