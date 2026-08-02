package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class MovimentoFinanceiro {

    private int id;
    private LocalDateTime dataMovimento;
    private String tipoMovimento;
    private String origem;
    private BigDecimal valor = BigDecimal.ZERO;
    private String origemPagamento;
    private Integer idContaBancaria;
    private Integer idTransacao;
    private Integer idPagamentoFornecedor;
    private BigDecimal saldoAnterior = BigDecimal.ZERO;
    private BigDecimal saldoPosterior = BigDecimal.ZERO;
    private String descricao;
    private Integer idUsuario;
    private LocalDateTime dataCriacao;
    private String fornecedorNome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataMovimento() {
        return toDateTimeString(dataMovimento);
    }

    public void setDataMovimento(String dataMovimento) {
        this.dataMovimento = toLocalDateTime(dataMovimento);
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public double getValor() {
        return toDouble(valor);
    }

    public void setValor(double valor) {
        this.valor = toBigDecimal(valor);
    }

    public BigDecimal getValorDecimal() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor != null ? valor : BigDecimal.ZERO;
    }

    public String getOrigemPagamento() {
        return origemPagamento;
    }

    public void setOrigemPagamento(String origemPagamento) {
        this.origemPagamento = origemPagamento;
    }

    public Integer getIdContaBancaria() {
        return idContaBancaria;
    }

    public void setIdContaBancaria(Integer idContaBancaria) {
        this.idContaBancaria = idContaBancaria;
    }

    public Integer getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(Integer idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Integer getIdPagamentoFornecedor() {
        return idPagamentoFornecedor;
    }

    public void setIdPagamentoFornecedor(Integer idPagamentoFornecedor) {
        this.idPagamentoFornecedor = idPagamentoFornecedor;
    }

    public double getSaldoAnterior() {
        return toDouble(saldoAnterior);
    }

    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = toBigDecimal(saldoAnterior);
    }

    public BigDecimal getSaldoAnteriorDecimal() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior != null ? saldoAnterior : BigDecimal.ZERO;
    }

    public double getSaldoPosterior() {
        return toDouble(saldoPosterior);
    }

    public void setSaldoPosterior(double saldoPosterior) {
        this.saldoPosterior = toBigDecimal(saldoPosterior);
    }

    public BigDecimal getSaldoPosteriorDecimal() {
        return saldoPosterior;
    }

    public void setSaldoPosterior(BigDecimal saldoPosterior) {
        this.saldoPosterior = saldoPosterior != null ? saldoPosterior : BigDecimal.ZERO;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDataCriacao() {
        return toDateTimeString(dataCriacao);
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = toLocalDateTime(dataCriacao);
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }
}
