/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.transacao;

import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class Transacao {
    private int id;
    private String data_pagamento;
    private double valorRecebido;
    private double valorPago;
    private double trocos;
    private String tipo_transacao;
    private String descricao;
    private Parcela parcela;
    private ContaBancaria contabancaria;
    private Factura factura;
    private Usuario usuario; 
    private String data_criacao;
    private MetodoPagamento metodo;

    public Transacao() {
    }

    public Transacao(String data_pagamento, double valorRecebido, double valorPago, double trocos, String tipo_transacao, String descricao, ContaBancaria contabancaria, Factura factura, Usuario usuario, String data_criacao, MetodoPagamento metodo, Parcela parcela) {
        this.data_pagamento = data_pagamento;
        this.valorRecebido = valorRecebido;
        this.valorPago = valorPago;
        this.trocos = trocos;
        this.tipo_transacao = tipo_transacao;
        this.descricao = descricao;
        this.contabancaria = contabancaria;
        this.factura = factura;
        this.usuario = usuario;
        this.data_criacao = data_criacao;
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
        return data_pagamento;
    }

    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public double getTrocos() {
        return trocos;
    }

    public void setTrocos(double trocos) {
        this.trocos = trocos;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getTipo_transacao() {
        return tipo_transacao;
    }

    public void setTipo_transacao(String tipo_transacao) {
        this.tipo_transacao = tipo_transacao;
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
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
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
