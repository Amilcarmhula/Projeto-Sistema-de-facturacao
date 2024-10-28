/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.taxa_imposto;

/**
 *
 * @author JM-Tecnologias
 */
public class TaxaImposto {

    private int id;
    private String nomeImposto;
    private double percentagemImposto;
    private String descricao;
    private String data_criacao;

    public TaxaImposto() {
    }

    public TaxaImposto(String identificador, double percentagem, String descricao, String data_criacao) {
        this.nomeImposto = identificador;
        this.percentagemImposto = percentagem;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeImposto() {
        return nomeImposto;
    }

    public void setNomeImposto(String nomeImposto) {
        this.nomeImposto = nomeImposto;
    }

    public double getPercentagemImposto() {
        return percentagemImposto;
    }

    public void setPercentagemImposto(double percentagemImposto) {
        this.percentagemImposto = percentagemImposto;
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
