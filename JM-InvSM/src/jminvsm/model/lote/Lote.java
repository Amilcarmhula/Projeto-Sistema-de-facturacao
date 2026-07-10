package jminvsm.model.lote;

import java.math.BigDecimal;
import java.time.LocalDate;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoLote;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;

public class Lote {

    private int id;
    private String codigo;
    private Produto produto;
    private Armazem armazem;
    private Fornecedor fornecedor;
    private LocalDate dataEntrada;
    private int quantidadeInicial;
    private int quantidadeAtual;
    private BigDecimal custoUnitario = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private EstadoLote estado;

    public Lote() {
    }

    public Lote(String codigo, Produto produto, Armazem armazem, Fornecedor fornecedor,
            LocalDate dataEntrada, int quantidadeInicial, int quantidadeAtual,
            BigDecimal custoUnitario, BigDecimal valorTotal, String estado) {
        this.codigo = codigo;
        this.produto = produto;
        this.armazem = armazem;
        this.fornecedor = fornecedor;
        this.dataEntrada = dataEntrada;
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeAtual = quantidadeAtual;
        this.custoUnitario = custoUnitario;
        this.valorTotal = valorTotal;
        this.estado = EstadoLote.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDataEntrada() {
        return toDateString(dataEntrada);
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = toLocalDate(dataEntrada);
    }

    public LocalDate getDataEntradaDate() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public int getQuantidadeInicial() {
        return quantidadeInicial;
    }

    public void setQuantidadeInicial(int quantidadeInicial) {
        this.quantidadeInicial = quantidadeInicial;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public double getCustoUnitario() {
        return toDouble(custoUnitario);
    }

    public void setCustoUnitario(double custoUnitario) {
        this.custoUnitario = toBigDecimal(custoUnitario);
    }

    public BigDecimal getCustoUnitarioDecimal() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public double getValorTotal() {
        return toDouble(valorTotal);
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = toBigDecimal(valorTotal);
    }

    public BigDecimal getValorTotalDecimal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoLote.fromValue(estado);
    }

    public EstadoLote getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoLote estado) {
        this.estado = estado;
    }
}
