/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.itens;

import sysfact.model.desconto.Desconto;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;


/**
 *
 * @author JM-Tecnologias
 */
public class Item {

    private int id;
    private String tipo;
    private int quantidade;
    private PrecoVenda precovenda;
    private double subtotal;
    private String data_criacao;
    private Desconto desconto;
    private TaxaImposto taxaimposto;
    private Produto produto;
    private Factura factura;
    private Usuario usuario;
    private boolean active_or_not;

    public Item() {
    }

    public Item(String tipo, int quantidade, PrecoVenda precovenda, double subtotal, Desconto desconto, Produto produto, Factura factura, Usuario usuario, TaxaImposto taxaimposto) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.precovenda = precovenda;
        this.subtotal = subtotal;
        this.desconto = desconto;
        this.produto = produto;
        this.factura = factura;
        this.usuario = usuario;
        this.taxaimposto = taxaimposto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public PrecoVenda getPrecovenda() {
        return precovenda;
    }

    public void setPrecovenda(PrecoVenda precovenda) {
        this.precovenda = precovenda;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
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

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    public TaxaImposto getTaxaimposto() {
        return taxaimposto;
    }

    public void setTaxaimposto(TaxaImposto taxaimposto) {
        this.taxaimposto = taxaimposto;
    }

    
}
