/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.desconto;


/**
 *
 * @author JM-Tecnologias
 */
public class Desconto {
    private int id;
    private String nome;
    private String descricao;
    private String data_criacao;
    
    public Desconto(){super();}

    public Desconto(int id, String nome, String descricao, String data_criacao) {
        super();
        this.id = id;
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
