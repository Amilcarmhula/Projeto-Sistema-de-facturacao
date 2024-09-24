/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.documetos;

/**
 *
 * @author JM-Tecnologias
 */
public class DocumentosComerciais {
    private int id;
    private String sigla;
    private String descricao;
    private int diasUteis;

    public DocumentosComerciais() {
    }

    public DocumentosComerciais(String sigla, String descricao, int diasUteis) {
        this.sigla = sigla;
        this.descricao = descricao;
        this.diasUteis = diasUteis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
