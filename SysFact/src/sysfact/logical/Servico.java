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
    private Categoria fk_id_categoria;
    private TaxaImposto fk_id_taxa_imposto;
    private Usuario fk_id_usuario;
    private boolean active_or_not;
    
    public Servico(){}

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

    public Categoria getFk_id_categoria() {
        return fk_id_categoria;
    }

    public void setFk_id_categoria(Categoria fk_id_categoria) {
        this.fk_id_categoria = fk_id_categoria;
    }

    public TaxaImposto getFk_id_taxa_imposto() {
        return fk_id_taxa_imposto;
    }

    public void setFk_id_taxa_imposto(TaxaImposto fk_id_taxa_imposto) {
        this.fk_id_taxa_imposto = fk_id_taxa_imposto;
    }

    public Usuario getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(Usuario fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
