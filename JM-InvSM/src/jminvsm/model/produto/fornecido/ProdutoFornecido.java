/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.produto.fornecido;

import java.math.BigDecimal;
import java.time.LocalDate;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoLote;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;


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
    private LocalDate dataEntrada;
    private int quantidadeInicial;
    private int quantidadeActual;
    private BigDecimal custoUnitario = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private EstadoLote estado;
    
    public ProdutoFornecido(){}

    public ProdutoFornecido(int id, String codigo, Produto produto, Armazem armazem, Fornecedor fornecedor, String dataEntrada, int quantidadeInicial, int quantidadeActual, double custoUnitario, double valorTotal, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.produto = produto;
        this.armazem = armazem;
        this.fornecedor = fornecedor;
        this.dataEntrada = toLocalDate(dataEntrada);
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeActual = quantidadeActual;
        this.custoUnitario = toBigDecimal(custoUnitario);
        this.valorTotal = toBigDecimal(valorTotal);
        this.estado = EstadoLote.fromValue(estado);
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
        if (codigo == null || codigo.length() <= 1) {
            this.codigo = "L1";
            return;
        }

        try {
            this.codigo = "L" + (Integer.parseInt(codigo.substring(1)) + 1);
        } catch (NumberFormatException ex) {
            this.codigo = "L1";
        }
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
        return toDateString(dataEntrada);
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = toLocalDate(dataEntrada);
    }

    public LocalDate getDataEntradaDate() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
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
        return toDouble(custoUnitario);
    }

    public void setCustoUnitario(double custoUnitario) {
        this.custoUnitario = toBigDecimal(custoUnitario);
    }

    public BigDecimal getCustoUnitarioDecimal() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public double getValorTotal() {
        return toDouble(valorTotal);
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = toBigDecimal(valorTotal);
    }

    public BigDecimal getValorTotalDecimal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoLote.fromValue(estado);
    }

    public EstadoLote getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoLote estado) {
        this.estado = estado;
    }

   
}
