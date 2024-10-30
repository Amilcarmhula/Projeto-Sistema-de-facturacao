/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.parcelas;

import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class Parcela {
    private int id;
    private int parcelaNumero;
    private double taxaJuros;
    private double taxaDeAtraso;
    private double valorParcela;
    private double valorPagoParcela;
    private String dataVencimento;
    private String dataPagamento;
    private String estado;
    private Factura factura;
    private Usuario usuario;

    public Parcela() {
    }

    public Parcela(int parcelaNumero, double taxaJuros, double taxaDeAtraso, double valorParcela, double valorPagoParcela, String dataVencimento, String dataPagamento, String estado, Factura factura, Usuario usuario) {
        this.parcelaNumero = parcelaNumero;
        this.taxaJuros = taxaJuros;
        this.taxaDeAtraso = taxaDeAtraso;
        this.valorParcela = valorParcela;
        this.valorPagoParcela = valorPagoParcela;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.estado = estado;
        this.factura = factura;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParcelaNumero() {
        return parcelaNumero;
    }

    public void setParcelaNumero(int parcelaNumero) {
        this.parcelaNumero = parcelaNumero;
    }

    public double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public double getTaxaDeAtraso() {
        return taxaDeAtraso;
    }

    public void setTaxaDeAtraso(double taxaDeAtraso) {
        this.taxaDeAtraso = taxaDeAtraso;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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
        return valorPagoParcela;
    }

    public void setValorPagoParcela(double valorPagoParcela) {
        this.valorPagoParcela = valorPagoParcela;
    }
    
    
}
