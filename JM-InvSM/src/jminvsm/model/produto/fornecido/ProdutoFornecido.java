/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.produto.fornecido;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecido {

    private int id;
    private int quantidade;
    private int qtd_por_unidade;
    private String data_fornecida;
    private String unidade_fornecida;
    private double custo;
    private Armazem armazem;
    private Produto produto;
    private Fornecedor fornecedor;
    private Usuario usuario;
    private String data_criacao;
    
    public ProdutoFornecido(){}

    public ProdutoFornecido(int quantidade, int qtd_por_unidade, String data_fornecida, String unidade_fornecida, double custo, Armazem armazem, Produto produto, Fornecedor fornecedor, Usuario usuario) {
        this.quantidade = quantidade;
        this.qtd_por_unidade = qtd_por_unidade;
        this.data_fornecida = data_fornecida;
        this.unidade_fornecida = unidade_fornecida;
        this.custo = custo;
        this.armazem = armazem;
        this.produto = produto;
        this.fornecedor = fornecedor;
        this.usuario = usuario;
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

    public int getQtd_por_unidade() {
        return qtd_por_unidade;
    }

    public void setQtd_por_unidade(int qtd_por_unidade) {
        this.qtd_por_unidade = qtd_por_unidade;
    }

    public String getData_fornecida() {
        return data_fornecida;
    }

    public void setData_fornecida(String data_fornecida) {
        this.data_fornecida = data_fornecida;
    }

    public String getUnidade_fornecida() {
        return unidade_fornecida;
    }

    public void setUnidade_fornecida(String unidade_fornecida) {
        this.unidade_fornecida = unidade_fornecida;
    }
    
    

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
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
