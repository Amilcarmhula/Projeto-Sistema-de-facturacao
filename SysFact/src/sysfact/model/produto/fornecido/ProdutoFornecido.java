/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.produto.fornecido;

import sysfact.model.agente.Agente;
import sysfact.model.armazem.Armazem;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecido {

    private int id;
    private int quantidade;
    private String data_fornecida;
    private double custo;
    private Armazem armazem;
    private Produto produto;
    private Agente agente;
    private Usuario usuario;
    private String data_criacao;
    private boolean active_or_not;
    
    public ProdutoFornecido(){}

    public ProdutoFornecido(int quantidade, String data_fornedida, double custo, Armazem armazem, Produto produto, Agente agente, Usuario usuario) {
        this.quantidade = quantidade;
        this.data_fornecida = data_fornedida;
        this.custo = custo;
        this.armazem = armazem;
        this.produto = produto;
        this.agente = agente;
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

    public String getData_fornecida() {
        return data_fornecida;
    }

    public void setData_fornecida(String data_fornecida) {
        this.data_fornecida = data_fornecida;
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

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
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

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

   
}
