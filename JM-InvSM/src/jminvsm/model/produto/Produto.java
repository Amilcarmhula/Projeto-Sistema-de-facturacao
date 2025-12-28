/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.produto;

import java.util.List;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.stock.Stock;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.unidade_medida.UnidadeMedida;

/**
 *
 * @author JM-Tecnologias
 */
public class Produto {

    private int id;
    private String nome;
    private String codigo_barra;
    private String descricao;
    private String tipoProduto; // Simples ou Composto
    private Integer unidadesPorTipo;
    private boolean controle_stock;
    private int nivelstock;
    private Categoria categoria;
    private UnidadeMedida unidadeMedida;
//    private PrecoVenda precoVenda;
    private Imposto imposto;

    public Produto() {
        super();
    }

    public Produto(String nome, String codigo_barra, String descricao, String tipo, Integer unidadesPorTipo,
            boolean contole_stock, int nivelstock, Categoria categoria, UnidadeMedida unidade_medida,
            Imposto taxa_imposto) {
        super();
        this.nome = nome;
        this.codigo_barra = codigo_barra;
        this.descricao = descricao;
        this.tipoProduto = tipo;
        this.unidadesPorTipo = unidadesPorTipo;
        this.controle_stock = contole_stock;
        this.categoria = categoria;
        this.unidadeMedida = unidade_medida;
        this.imposto = taxa_imposto;

        this.nivelstock = nivelstock;
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

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public Integer getUnidadesPorTipo() {
        return unidadesPorTipo;
    }

    public void setUnidadesPorTipo(Integer unidadesPorTipo) {
        this.unidadesPorTipo = unidadesPorTipo;
    }

    public boolean isControle_stock() {
        return controle_stock;
    }

    public void setControle_stock(boolean controle_stock) {
        this.controle_stock = controle_stock;
    }

    public int getNivelstock() {
        return nivelstock;
    }

    public void setNivelstock(int nivelstock) {
        this.nivelstock = nivelstock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Imposto getImposto() {
        return imposto;
    }

    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }
//
//    public PrecoVenda getPrecoVenda() {
//        return precoVenda;
//    }
//
//    public void setPrecoVenda(PrecoVenda precoVenda) {
//        this.precoVenda = precoVenda;
//    }
    
    
}
