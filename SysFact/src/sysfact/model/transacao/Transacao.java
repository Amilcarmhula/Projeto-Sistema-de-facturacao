/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.transacao;

import sysfact.model.empresa.conta.ContaBancaria;
import sysfact.model.metodo_pagamento.MetodoPagamento;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class Transacao {
    private int id;
    private String data_pagamento;
    private double valor;
    private String tipo_transacao;
    private String descricao;
    private ContaBancaria contabancaria;
    private Factura factura;
    private Usuario usuario; 
    private String data_criacao;
    private MetodoPagamento metodo;

    public Transacao() {
    }

    public Transacao(double valor, String tipo_transacao , String descricao, ContaBancaria contabancaria, Factura factura, Usuario usuario, String data_criacao, MetodoPagamento metodo) {
        this.valor = valor;
        this.tipo_transacao = tipo_transacao;
        this.descricao = descricao;
        this.contabancaria = contabancaria;
        this.factura = factura;
        this.usuario = usuario;
        this.data_criacao = data_criacao;
        this.metodo = metodo;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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
    
}
