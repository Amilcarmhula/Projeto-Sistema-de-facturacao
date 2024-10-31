/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.preco;

import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class PrecoVenda {

    private int id;
    private double preco;
    private double precoInclTaxa;
    private double precoExclTaxa;
    private String data_inicio;
    private String data_fim;
    private String estado;
    private Produto produto;
    private Usuario usuario;
    private String data_criacao;

    public PrecoVenda(){
    }
    public PrecoVenda(double preco, double precoInclTaxa, double precoExclTaxa, String data_inicio, String data_fim, 
            Produto produto,Usuario usuario) {
        this.precoInclTaxa = precoInclTaxa;
        this.precoExclTaxa = precoExclTaxa;
        this.preco = preco;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.produto = produto;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPrecoInclTaxa() {
        return precoInclTaxa;
    }

    public void setPrecoInclTaxa(double precoInclTaxa) {
        this.precoInclTaxa = precoInclTaxa;
    }

    public double getPrecoExclTaxa() {
        return precoExclTaxa;
    }

    public void setPrecoExclTaxa(double precoExclTaxa) {
        this.precoExclTaxa = precoExclTaxa;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
