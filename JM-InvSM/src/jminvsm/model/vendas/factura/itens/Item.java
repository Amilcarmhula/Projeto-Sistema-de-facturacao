/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.vendas.factura.itens;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;


/**
 *
 * @author JM-Tecnologias
 */
public class Item {

    private int id;
    private int quantidade;
    private double subtotal;
    private Desconto desconto;
    private Produto produto;
    private Factura factura;
    private Usuario usuario;
    private Armazem armazem;
    private String data_criacao;

    public Item() {
        super();
    }

    public Item(int quantidade, double subtotal, Desconto desconto, Produto produto, Factura factura, Usuario usuario,Armazem armazem) {
        super();
        this.quantidade = quantidade;
        this.subtotal = subtotal;
        this.desconto = desconto;
        this.produto = produto;
        this.factura = factura;
        this.usuario = usuario;
        this.armazem = armazem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
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

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }
    
    /*
    Expondo Propriedades BEANS
    */
    
    public String getNomeProduto(){
        if(produto != null){
            return  produto.getNomeProduto();
        }
        return null;
    }
    public String getDescricao(){
        if (produto != null) {
            return produto.getDescricao();
        }
        return null;
    }
    
    public Double getPreco(){
        if(produto != null){
            return produto.getPrecovenda().getPreco();
        }
        return null;
    }
    
    public Double getPercentagemImposto(){
        if(produto != null){
            return produto.getTaxa_imposto().getPercentagemImposto();
        }
        return null;
    }
    public Double getPercentagemDesconto(){
        if(desconto != null){
            return desconto.getPercentagemDesconto();
        }
        return null;
    }
    public String getNumero(){
        if(factura !=null){
            return factura.getNumero_fac();
        }
        return null;
    }
    
    
    
    

}
