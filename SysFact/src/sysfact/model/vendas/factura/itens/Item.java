/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.factura.itens;

import sysfact.model.desconto.Desconto;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;


/**
 *
 * @author JM-Tecnologias
 */
public class Item {

    private int id;
    private double quantidade;
    private double subtotal;
    private Desconto desconto;
    private Produto produto;
    private Factura factura;
    private Usuario usuario;
    private String data_criacao;

    public Item() {
    }

    public Item(double quantidade, double subtotal, Desconto desconto, Produto produto, Factura factura, Usuario usuario) {
        this.quantidade = quantidade;
        this.subtotal = subtotal;
        this.desconto = desconto;
        this.produto = produto;
        this.factura = factura;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Desconto getDesconto() {
        return desconto;
    }

    public void setDesconto(Desconto desconto) {
        this.desconto = desconto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

}
