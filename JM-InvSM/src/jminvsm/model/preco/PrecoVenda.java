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
    private double precoNormal;
    private double precoVenda;
    private double precoFinal;
    private String data_inicio;
    private String dataValidade;
    private String estado;
    private Produto produto;
    private Usuario usuario;
    private String data_criacao;

    public PrecoVenda(){
    }
    public PrecoVenda(double precoNormal, double precoVenda, double precoFinal, String dataValidade, 
            Produto produto,Usuario usuario) {
        this.precoVenda = precoVenda;
        this.precoFinal = precoFinal;
        this.precoNormal = precoNormal;
        this.dataValidade = dataValidade;
        this.produto = produto;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecoNormal() {
        return precoNormal;
    }

    public void setPrecoNormal(double precoNormal) {
        this.precoNormal = precoNormal;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(double precoFinal) {
        this.precoFinal = precoFinal;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
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
