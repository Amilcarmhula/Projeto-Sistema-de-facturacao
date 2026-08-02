/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.vendas.factura.itens;

import java.math.BigDecimal;
import java.time.LocalDate;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import jminvsm.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class Item {

    private int id;
    private String ref; // referencia do item. e um valor auxiliar
    private int quantidade;
    private int qtd_por_unidade;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private Produto produto;
    private Factura factura;
    private Usuario usuario;
    private Armazem armazem;
    private LocalDate data_criacao;
    private String unidade_vendida;
    private PrecoProdutoArmazem precoProdutoArmazem;

    public Item() {
        super();
    }

    public Item(int quantidade, int qtd_por_unidade, double subtotal, Produto produto, Factura factura, Usuario usuario, Armazem armazem, String unidade_vendida, PrecoProdutoArmazem precoProdutoArmazem) {
        super();
        this.quantidade = quantidade;
        this.qtd_por_unidade = qtd_por_unidade;
        this.subtotal = toBigDecimal(subtotal);
        this.produto = produto;
        this.factura = factura;
        this.usuario = usuario;
        this.armazem = armazem;
        this.unidade_vendida = unidade_vendida;
        this.precoProdutoArmazem = precoProdutoArmazem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }


    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade_vendida() {
        return unidade_vendida;
    }

    public void setUnidade_vendida(String unidade_vendida) {
        this.unidade_vendida = unidade_vendida;
    }

    public String getUnidadeVendida() {
        return unidade_vendida;
    }

    public void setUnidadeVendida(String unidadeVendida) {
        this.unidade_vendida = unidadeVendida;
    }

    public int getQtd_por_unidade() {
        return qtd_por_unidade;
    }

    public void setQtd_por_unidade(int qtd_por_unidade) {
        this.qtd_por_unidade = qtd_por_unidade;
    }

    public double getSubtotal() {
        return toDouble(subtotal);
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = toBigDecimal(subtotal);
    }

    public BigDecimal getSubtotalDecimal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getData_criacao() {
        return toDateString(data_criacao);
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = toLocalDate(data_criacao);
    }

    public LocalDate getDataCriacao() {
        return data_criacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.data_criacao = dataCriacao;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public PrecoProdutoArmazem getPrecoProdutoArmazem() {
        return precoProdutoArmazem;
    }

    public void setPrecoProdutoArmazem(PrecoProdutoArmazem precoProdutoArmazem) {
        this.precoProdutoArmazem = precoProdutoArmazem;
    }
    

    public void calcularSubtotal() {
        if (precoProdutoArmazem != null && precoProdutoArmazem.getPrecoFinalDecimal() != null) {
            subtotal = precoProdutoArmazem.getPrecoFinalDecimal().multiply(BigDecimal.valueOf(quantidade));
        }
    }
    

    /*
    Expondo Propriedades BEANS
     */
    public String getNomeProduto() {
        if (produto != null) {
            return produto.getNome()+": "+getDescricao();
        }
        return null;
    }

    public String getDescricao() {
        if (produto != null) {
            return produto.getDescricao();
        }
        return null;
    }

    public Double getPercentagemImposto() {
        if (produto != null && produto.getImposto() != null) {
            return produto.getImposto().getPercentagem();
        }
        return null;
    }

//    public Double getPercentagemDesconto() {
//        if (desconto != null) {
//            return desconto.getPercentagem();
//        }
//        return null;
//    }

    public String getNumero() {
        if (factura != null) {
            return factura.getNumero_fac();
        }
        return null;
    }

    public String getNumeroFactura() {
        return getNumero();
    }

}
