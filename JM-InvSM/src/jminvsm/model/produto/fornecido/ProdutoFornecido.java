/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.produto.fornecido;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;


/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecido {

    private int id;
    private String codigo;
    private Produto produto;
    private Armazem armazem;
    private Fornecedor fornecedor;
    private String dataEntrada;
    private int quantidadeInicial;
    private int quantidadeActual;
    private double custoUnitario;
    private double valorTotal;
    private String estado;
    
    public ProdutoFornecido(){}

    public ProdutoFornecido(int id, String codigo, Produto produto, Armazem armazem, Fornecedor fornecedor, String dataEntrada, int quantidadeInicial, int quantidadeActual, double custoUnitario, double valorTotal, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.produto = produto;
        this.armazem = armazem;
        this.fornecedor = fornecedor;
        this.dataEntrada = dataEntrada;
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeActual = quantidadeActual;
        this.custoUnitario = custoUnitario;
        this.valorTotal = valorTotal;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCodigoForSave(String codigo) {
        codigo =  codigo.substring(1);
        this.codigo = "L"+String.valueOf(Integer.parseInt(codigo) + 1);
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public int getQuantidadeInicial() {
        return quantidadeInicial;
    }

    public void setQuantidadeInicial(int quantidadeInicial) {
        this.quantidadeInicial = quantidadeInicial;
    }

    public int getQuantidadeActual() {
        return quantidadeActual;
    }

    public void setQuantidadeActual(int quantidadeActual) {
        this.quantidadeActual = quantidadeActual;
    }

    public double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   
}
