/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.enums.TipoTransacao;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.parcelas.Parcela;
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
public class Transacao {
    private int id;
    private LocalDate data_pagamento;
    private BigDecimal valorRecebido = BigDecimal.ZERO;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private BigDecimal trocos = BigDecimal.ZERO;
    private TipoTransacao tipo_transacao;
    private String descricao;
    private Parcela parcela;
    private ContaBancaria contabancaria;
    private Factura factura;
    private Usuario usuario; 
    private LocalDate data_criacao;
    private MetodoPagamento metodo;

    public Transacao() {
    }

    public Transacao(String data_pagamento, double valorRecebido, double valorPago, double trocos, String tipo_transacao, String descricao, ContaBancaria contabancaria, Factura factura, Usuario usuario, String data_criacao, MetodoPagamento metodo, Parcela parcela) {
        this.data_pagamento = toLocalDate(data_pagamento);
        this.valorRecebido = toBigDecimal(valorRecebido);
        this.valorPago = toBigDecimal(valorPago);
        this.trocos = toBigDecimal(trocos);
        this.tipo_transacao = TipoTransacao.fromValue(tipo_transacao);
        this.descricao = descricao;
        this.contabancaria = contabancaria;
        this.factura = factura;
        this.usuario = usuario;
        this.data_criacao = toLocalDate(data_criacao);
        this.metodo = metodo;
        this.parcela = parcela;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData_pagamento() {
        return toDateString(data_pagamento);
    }

    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = toLocalDate(data_pagamento);
    }

    public LocalDate getDataPagamento() {
        return data_pagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.data_pagamento = dataPagamento;
    }

    public double getValorRecebido() {
        return toDouble(valorRecebido);
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = toBigDecimal(valorRecebido);
    }

    public BigDecimal getValorRecebidoDecimal() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public double getTrocos() {
        return toDouble(trocos);
    }

    public void setTrocos(double trocos) {
        this.trocos = toBigDecimal(trocos);
    }

    public BigDecimal getTrocosDecimal() {
        return trocos;
    }

    public void setTrocos(BigDecimal trocos) {
        this.trocos = trocos;
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
        this.valorPago = valorPago;
    }

    public String getTipo_transacao() {
        return tipo_transacao != null ? tipo_transacao.getValor() : null;
    }

    public void setTipo_transacao(String tipo_transacao) {
        this.tipo_transacao = TipoTransacao.fromValue(tipo_transacao);
    }

    public String getTipoTransacao() {
        return getTipo_transacao();
    }

    public void setTipoTransacao(String tipoTransacao) {
        setTipo_transacao(tipoTransacao);
    }

    public TipoTransacao getTipoTransacaoEnum() {
        return tipo_transacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipo_transacao = tipoTransacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ContaBancaria getContabancaria() {
        return contabancaria;
    }

    public void setContabancaria(ContaBancaria contabancaria) {
        this.contabancaria = contabancaria;
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

    public String getData_criacao() {
        return toDateString(data_criacao);
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = toLocalDate(data_criacao);
    }

    public LocalDate getDataCriacao() {
        return data_criacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.data_criacao = dataCriacao;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }
    
    public int getId_parcela() {
        if (parcela != null) {
            return parcela.getId_parcela();
        }
        return -1;
    }
    
    public String getNomePagamento() {
        if (metodo != null) {
            return metodo.getNomePagamento();
        }
        return null;
    }
}
