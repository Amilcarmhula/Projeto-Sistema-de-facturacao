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
    private String nomeDesconto;
    private double percentagemDesconto;
    private String descricao;
    private String data_criacao;
    
    public Desconto(){super();}

    public Desconto(int id, String nome, double percentagem, String descricao, String data_criacao) {
        super();
        this.id = id;
        this.nomeDesconto = nome;
        this.percentagemDesconto = percentagem;
        this.descricao = descricao;
        this.data_criacao = data_criacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeDesconto() {
        return nomeDesconto;
    }

    public void setNomeDesconto(String nomeDesconto) {
        this.nomeDesconto = nomeDesconto;
    }

    public double getPercentagemDesconto() {
        return percentagemDesconto;
    }

    public void setPercentagemDesconto(double percentagemDesconto) {
        this.percentagemDesconto = percentagemDesconto;
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
