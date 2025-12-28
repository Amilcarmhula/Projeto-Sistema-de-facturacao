/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.categoria;


/**
 *
 * @author JM-Tecnologias
 */
public class Categoria {

    private int id;
    private String grupo;
    private String subgrupo;
    private String familia;
    private String descricao;

    public Categoria() {
    }

    public Categoria(int id, String grupo, String subgrupo, String familia, String descricao) {
        this.id = id;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.familia = familia;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
