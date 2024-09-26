/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.reports.facturas;

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

    public itensFacturaData() {
    }

    public itensFacturaData(String nomeProduto, String descricaoProduto, int quantidadeProduto,
            double taxaProduto, double descontoProduto, double totalProduto, double precoProduto) {
        this.nomeProduto = nomeProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidadeProduto = quantidadeProduto;
        this.taxaProduto = taxaProduto;
        this.descontoProduto = descontoProduto;
        this.totaProduto = totalProduto;
        this.precoProduto = precoProduto;
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

    public double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(double precoProduto) {
        this.precoProduto = precoProduto;
    }
    
    
}
