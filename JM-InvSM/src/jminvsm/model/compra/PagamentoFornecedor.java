package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.enums.EstadoPagamentoFornecedor;
import jminvsm.model.enums.OrigemPagamento;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class PagamentoFornecedor {

    private int id;
    private ContaPagar contaPagar;
    private Fornecedor fornecedor;
    private LocalDate dataPagamento;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private MetodoPagamento metodoPagamento;
    private OrigemPagamento origemPagamento;
    private ContaBancaria contaBancaria;
    private String referencia;
    private String descricao;
    private EstadoPagamentoFornecedor estado;
    private Usuario usuario;
    private LocalDateTime dataCriacao;

    public PagamentoFornecedor() {
    }

    public PagamentoFornecedor(ContaPagar contaPagar, Fornecedor fornecedor,
            LocalDate dataPagamento, BigDecimal valorPago,
            MetodoPagamento metodoPagamento, String origemPagamento, String estado) {
        this.contaPagar = contaPagar;
        this.fornecedor = fornecedor;
        this.dataPagamento = dataPagamento;
        setValorPago(valorPago);
        this.metodoPagamento = metodoPagamento;
        this.origemPagamento = OrigemPagamento.fromValue(origemPagamento);
        this.estado = EstadoPagamentoFornecedor.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContaPagar getContaPagar() {
        return contaPagar;
    }

    public void setContaPagar(ContaPagar contaPagar) {
        this.contaPagar = contaPagar;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDataPagamento() {
        return toDateString(dataPagamento);
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = toLocalDate(dataPagamento);
    }

    public LocalDate getDataPagamentoDate() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public double getValorPago() {
        return toDouble(valorPago);
    }

    public void setValorPago(double valorPago) {
        this.valorPago = toBigDecimal(valorPago);
    }

    public BigDecimal getValorPagoDecimal() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago != null ? valorPago : BigDecimal.ZERO;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getOrigemPagamento() {
        return origemPagamento != null ? origemPagamento.getValor() : null;
    }

    public void setOrigemPagamento(String origemPagamento) {
        this.origemPagamento = OrigemPagamento.fromValue(origemPagamento);
    }

    public OrigemPagamento getOrigemPagamentoEnum() {
        return origemPagamento;
    }

    public void setOrigemPagamento(OrigemPagamento origemPagamento) {
        this.origemPagamento = origemPagamento;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoPagamentoFornecedor.fromValue(estado);
    }

    public EstadoPagamentoFornecedor getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoPagamentoFornecedor estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataCriacao() {
        return toDateTimeString(dataCriacao);
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = toLocalDateTime(dataCriacao);
    }

    public LocalDateTime getDataCriacaoDateTime() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
