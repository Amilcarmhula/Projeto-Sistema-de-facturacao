/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.produto;

import sysfact.model.categoria.Categoria;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;


/**
 *
 * @author JM-Tecnologias
 */
public class Produto {

    private int id;
    private String nome;
    private String codigo_barra;
    private String descricao;
    private Categoria categoria;
//    private int fk_id_unidade_medida;
    private UnidadeMedida unidade_medida;
//    private int fk_id_taxa_imposto;
    private String data_criacao;
    private boolean active_or_not;
    
    public Produto(){}

    public Produto(String nome, String codigo_barra, String descricao, Categoria categoria, UnidadeMedida unidade_medida) {
        this.nome = nome;
        this.codigo_barra = codigo_barra;
        this.descricao = descricao;
        this.categoria = categoria;
        this.unidade_medida = unidade_medida;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UnidadeMedida getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(UnidadeMedida unidade_medida) {
        this.unidade_medida = unidade_medida;
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
