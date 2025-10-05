/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.preco;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class PrecoVenda {

    private int id;
    private Double precoNormal;
    private double precoVenda;
    private double precoFinal;
    private String dataValidade;
    private String estado;
    private Produto produto;
    private Armazem armazem;
    private Usuario usuario;
    private String data_criacao;

    public PrecoVenda(){
    }

    public PrecoVenda(int id, double precoNormal, double precoVenda, double precoFinal, String dataValidade, String estado, Produto produto, Armazem armazem, Usuario usuario) {
        this.id = id;
        this.precoNormal = precoNormal;
        this.precoVenda = precoVenda;
        this.precoFinal = precoFinal;
        this.dataValidade = dataValidade;
        this.estado = estado;
        this.produto = produto;
        this.armazem = armazem;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrecoNormal() {
        return precoNormal;
    }

    public void setPrecoNormal(Double precoNormal) {
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

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
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
