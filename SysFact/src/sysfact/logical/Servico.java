/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Servico {

    private int id;
    private String nome;
    private String descricao;
    private int fk_id_categoria;
    private int fk_id_taxa_imposto;
    private int fk_id_usuario;
    private String data_criacao;
    private boolean active_or_not;
    
    public Servico(){}

    public Servico( String nome, String descricao, Categoria fk_id_categoria, TaxaImposto fk_id_taxa_imposto, Usuario fk_id_usuario) {
        this.nome = nome;
        this.descricao = descricao;
        this.fk_id_categoria = fk_id_categoria.getId();
        this.fk_id_taxa_imposto = fk_id_taxa_imposto.getId();
        this.fk_id_usuario = fk_id_usuario.getId();
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

    public int getFk_id_categoria() {
        return fk_id_categoria;
    }

    public void setFk_id_categoria(int fk_id_categoria) {
        this.fk_id_categoria = fk_id_categoria;
    }

    public int getFk_id_taxa_imposto() {
        return fk_id_taxa_imposto;
    }

    public void setFk_id_taxa_imposto(int fk_id_taxa_imposto) {
        this.fk_id_taxa_imposto = fk_id_taxa_imposto;
    }

    public int getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(int fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
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
