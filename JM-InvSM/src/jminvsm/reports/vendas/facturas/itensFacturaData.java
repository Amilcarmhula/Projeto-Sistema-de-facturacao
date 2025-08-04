/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.reports.vendas.facturas;

/**
 *
 * @author JM-Tecnologias
 */
public class itensFacturaData {
    private String nomeProduto;
    private String descricaoProduto;
    private int quantidadeProduto;
    private double precoProduto;
    private double taxaProduto;
    private double descontoProduto;
    private double totaProduto;
    private double subtotal;
    private double desconto;
    private double taxas;
    private double total_geral;
    private String unidade_vendida;
    private int qtd_por_unidade;


    public itensFacturaData() {
    }

    public itensFacturaData(String nomeProduto, String descricaoProduto, int quantidadeProduto, int qtd_por_unidade, double precoProduto, double taxaProduto, double descontoProduto, double totaProduto, double subtotal, double desconto, double taxas, double total_geral) {
        this.nomeProduto = nomeProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidadeProduto = quantidadeProduto;
        this.precoProduto = precoProduto;
        this.taxaProduto = taxaProduto;
        this.descontoProduto = descontoProduto;
        this.totaProduto = totaProduto;
        this.subtotal = subtotal;
        this.desconto = desconto;
        this.taxas = taxas;
        this.total_geral = total_geral;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public int getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(int quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public int getQtd_por_unidade() {
        return qtd_por_unidade;
    }

    public void setQtd_por_unidade(int qtd_por_unidade) {
        this.qtd_por_unidade = qtd_por_unidade;
    }

    public double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public double getTaxaProduto() {
        return taxaProduto;
    }

    public void setTaxaProduto(double taxaProduto) {
        this.taxaProduto = taxaProduto;
    }

    public double getDescontoProduto() {
        return descontoProduto;
    }

    public void setDescontoProduto(double descontoProduto) {
        this.descontoProduto = descontoProduto;
    }

    public double getTotaProduto() {
        return totaProduto;
    }

    public void setTotaProduto(double totaProduto) {
        this.totaProduto = totaProduto;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getTaxas() {
        return taxas;
    }

    public void setTaxas(double taxas) {
        this.taxas = taxas;
    }

    public double getTotal_geral() {
        return total_geral;
    }

    public void setTotal_geral(double total_geral) {
        this.total_geral = total_geral;
    }

    
}
