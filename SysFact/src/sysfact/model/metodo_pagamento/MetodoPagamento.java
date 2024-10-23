/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.metodo_pagamento;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodoPagamento {
    private int id;
    private String nome;
    private String descricao;
    private String data_criacao;

    public MetodoPagamento() {
    }
    
    

    public MetodoPagamento(String nome, String descricao, String data_criacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.data_criacao = data_criacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }
    
    
    
}
