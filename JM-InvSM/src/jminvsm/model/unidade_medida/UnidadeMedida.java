/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.unidade_medida;


/**
 *
 * @author JM-Tecnologias
 */
public class UnidadeMedida {
    private int id;
    private String nome;
    private String sigla;
    private String data_criacao;
    private String descricao;
    
    public UnidadeMedida(){}

    public UnidadeMedida(String identificador, String sigla, String descricao) {
        this.nome = identificador;
        this.sigla = sigla;
        this.descricao = descricao;
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
}
