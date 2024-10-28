/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.vendas.documetos;

/**
 *
 * @author JM-Tecnologias
 */
public class DocumentosComerciais {
    private int id;
    private String sigla_doc;
    private String nome_doc;
    private String descricao;
    private int diasUteis;

    public DocumentosComerciais() {
    }

    public DocumentosComerciais(String sigla,String nome, String descricao, int diasUteis) {
        this.sigla_doc = sigla;
        this.nome_doc = nome;
        this.descricao = descricao;
        this.diasUteis = diasUteis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigla_doc() {
        return sigla_doc;
    }

    public void setSigla_doc(String sigla_doc) {
        this.sigla_doc = sigla_doc;
    }

    public String getNome_doc() {
        return nome_doc;
    }

    public void setNome_doc(String nome_doc) {
        this.nome_doc = nome_doc;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDiasUteis() {
        return diasUteis;
    }

    public void setDiasUteis(int diasUteis) {
        this.diasUteis = diasUteis;
    }
    
    
}
