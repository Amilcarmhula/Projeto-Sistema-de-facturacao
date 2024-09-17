/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.produto;

import java.util.List;
import sysfact.model.categoria.Categoria;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.stock.Stock;
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
    private boolean contole_stock;
    private Categoria categoria;
    private UnidadeMedida unidade_medida;
    private TaxaImposto taxa_imposto;
    private PrecoVenda precovenda;
    private Stock stock;
    private String data_criacao;

    public Produto() {
    }

    public Produto(String nome, String codigo_barra, String descricao, boolean contole_stock, Categoria categoria, UnidadeMedida unidade_medida, TaxaImposto taxa_imposto, PrecoVenda precovenda, Stock stock) {
        this.nome = nome;
        this.codigo_barra = codigo_barra;
        this.descricao = descricao;
        this.contole_stock = contole_stock;
        this.categoria = categoria;
        this.unidade_medida = unidade_medida;
        this.taxa_imposto = taxa_imposto;
        this.precovenda = precovenda;
        this.stock = stock;
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

    public boolean isContole_stock() {
        return contole_stock;
    }

    public void setContole_stock(boolean contole_stock) {
        this.contole_stock = contole_stock;
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

    public TaxaImposto getTaxa_imposto() {
        return taxa_imposto;
    }

    public void setTaxa_imposto(TaxaImposto taxa_imposto) {
        this.taxa_imposto = taxa_imposto;
    }

    public PrecoVenda getPrecovenda() {
        return precovenda;
    }

    public void setPrecovenda(PrecoVenda precovenda) {
        this.precovenda = precovenda;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    
}
