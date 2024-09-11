/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.categoria;


/**
 *
 * @author JM-Tecnologias
 */
public class Categoria {

    private int id;
    private String tipo;
    private String grupo;
    private String subgrupo;
    private String familia;
    private String descricao;
    private String data_criacao;
    private boolean active_or_not;

    public Categoria() {
    }

    public Categoria(int id, String tipo, String grupo, String subgrupo, String familia, String descricao, String data_criacao, boolean active_or_not) {
        this.id = id;
        this.tipo = tipo;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.familia = familia;
        this.descricao = descricao;
        this.data_criacao = data_criacao;
        this.active_or_not = active_or_not;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
