/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.factura.itens;

/**
 *
 * @author JM-Tecnologias
 */
public final class TableModelItems {
    private int id;
    private String nome;
    private double preco;
    private String sigla;
    private int qtd;
    private String desconto;
    private String taxa;
    private double subtotal;

    public TableModelItems(){}
    
    public TableModelItems(int id, String nome, double preco, String sigla,
            int qtd, String desconto, String taxa, double subtotal) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.sigla = sigla;
        this.qtd = qtd;
        this.desconto = desconto;
        this.taxa = taxa;
        this.subtotal = subtotal;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getTaxa() {
        return taxa;
    }

    public void setTaxa(String taxa) {
        this.taxa = taxa;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
}
