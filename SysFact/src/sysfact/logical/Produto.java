/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Produto {

    private int id;
    private String nome;
    private String codigo_barra;
    private double preco;
    private String descricao;
    private Categoria fk_id_categoria;
    private UnidadeMedida fk_id_unidade_medida;
    private TaxaImposto fk_id_taxa_imposto;
    private boolean active_or_not;
    
    public Produto(){}

    public Produto(int id, String nome, String codigo_barra, double preco, String descricao, Categoria fk_id_categoria, UnidadeMedida fk_id_unidade_medida, TaxaImposto fk_id_taxa_imposto, boolean active_or_not) {
        this.id = id;
        this.nome = nome;
        this.codigo_barra = codigo_barra;
        this.preco = preco;
        this.descricao = descricao;
        this.fk_id_categoria = fk_id_categoria;
        this.fk_id_unidade_medida = fk_id_unidade_medida;
        this.fk_id_taxa_imposto = fk_id_taxa_imposto;
        this.active_or_not = active_or_not;
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

    public String getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(String codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
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

    public UnidadeMedida getFk_id_unidade_medida() {
        return fk_id_unidade_medida;
    }

    public void setFk_id_unidade_medida(UnidadeMedida fk_id_unidade_medida) {
        this.fk_id_unidade_medida = fk_id_unidade_medida;
    }

    public TaxaImposto getFk_id_taxa_imposto() {
        return fk_id_taxa_imposto;
    }

    public void setFk_id_taxa_imposto(TaxaImposto fk_id_taxa_imposto) {
        this.fk_id_taxa_imposto = fk_id_taxa_imposto;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

}
