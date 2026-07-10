/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.parcelas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import jminvsm.model.enums.EstadoParcela;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import jminvsm.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class Parcela {

    private int id_parcela;
    private int parcelaNumero;
    private BigDecimal taxaJuros = BigDecimal.ZERO;
    private BigDecimal taxaDeAtraso = BigDecimal.ZERO;
    private BigDecimal valorParcela = BigDecimal.ZERO;
    private BigDecimal valorPagoParcela = BigDecimal.ZERO;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private EstadoParcela estado;
    private Factura factura;
    private Usuario usuario;

    public Parcela() {
    }

    public Parcela(int parcelaNumero, double taxaJuros, double taxaDeAtraso, double valorParcela, double valorPagoParcela, String dataVencimento, String dataPagamento, String estado, Factura factura, Usuario usuario) {
        this.parcelaNumero = parcelaNumero;
        this.taxaJuros = toBigDecimal(taxaJuros);
        this.taxaDeAtraso = toBigDecimal(taxaDeAtraso);
        this.valorParcela = toBigDecimal(valorParcela);
        this.valorPagoParcela = toBigDecimal(valorPagoParcela);
        this.dataVencimento = toLocalDate(dataVencimento);
        this.dataPagamento = toLocalDate(dataPagamento);
        this.estado = EstadoParcela.fromValue(estado);
        this.factura = factura;
        this.usuario = usuario;
    }
    

    public int getId_parcela() {
        return id_parcela;
    }

    public void setId_parcela(int id_parcela) {
        this.id_parcela = id_parcela;
    }

    public int getIdParcela() {
        return id_parcela;
    }

    public void setIdParcela(int idParcela) {
        this.id_parcela = idParcela;
    }

    public int getParcelaNumero() {
        return parcelaNumero;
    }

    public void setParcelaNumero(int parcelaNumero) {
        this.parcelaNumero = parcelaNumero;
    }

    public double getTaxaJuros() {
        return toDouble(taxaJuros);
    }

    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = toBigDecimal(taxaJuros);
    }

    public BigDecimal getTaxaJurosDecimal() {
        return taxaJuros;
    }

    public void setTaxaJuros(BigDecimal taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public double getTaxaDeAtraso() {
        return toDouble(taxaDeAtraso);
    }

    public void setTaxaDeAtraso(double taxaDeAtraso) {
        this.taxaDeAtraso = toBigDecimal(taxaDeAtraso);
    }

    public BigDecimal getTaxaDeAtrasoDecimal() {
        return taxaDeAtraso;
    }

    public void setTaxaDeAtraso(BigDecimal taxaDeAtraso) {
        this.taxaDeAtraso = taxaDeAtraso;
    }

    public double getValorParcela() {
        return toDouble(valorParcela.setScale(2, RoundingMode.HALF_UP));
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = toBigDecimal(valorParcela);
    }

    public BigDecimal getValorParcelaDecimal() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getDataVencimento() {
        return toDateString(dataVencimento);
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = toLocalDate(dataVencimento);
    }

    public LocalDate getDataVencimentoDate() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoParcela.fromValue(estado);
    }

    public EstadoParcela getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoParcela estado) {
        this.estado = estado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getValorPagoParcela() {
        return toDouble(valorPagoParcela);
    }

    public void setValorPagoParcela(double valorPagoParcela) {
        this.valorPagoParcela = toBigDecimal(valorPagoParcela);
    }

    public BigDecimal getValorPagoParcelaDecimal() {
        return valorPagoParcela;
    }

    public void setValorPagoParcela(BigDecimal valorPagoParcela) {
        this.valorPagoParcela = valorPagoParcela;
    }
    
    

}
