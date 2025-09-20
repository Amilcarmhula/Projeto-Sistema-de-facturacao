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
    private boolean controle_stock;
    private int nivelstock;
    private Categoria categoria;
    private UnidadeMedida unidade_medida;
    private List<PrecoVenda> precoVenda;
    private Imposto imposto;
    private String data_criacao;

    public Produto() {
        super();
    }

    public Produto(String nome, String codigo_barra, String descricao, 
            boolean contole_stock, int nivelstock, Categoria categoria, UnidadeMedida unidade_medida,
            Imposto taxa_imposto) {
        super();
        this.nome = nome;
        this.codigo_barra = codigo_barra;
        this.descricao = descricao;
        this.controle_stock = contole_stock;
        this.categoria = categoria;
        this.unidade_medida = unidade_medida;
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

    public UnidadeMedida getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(UnidadeMedida unidade_medida) {
        this.unidade_medida = unidade_medida;
    }

    public Imposto getImposto() {
        return imposto;
    }

    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }


    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public List<PrecoVenda> getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(List<PrecoVenda> precoVenda) {
        this.precoVenda = precoVenda;
    }
    
    
}
