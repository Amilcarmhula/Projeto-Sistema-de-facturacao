package jminvsm.model.compra;

import java.math.BigDecimal;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoItemOrdemCompra;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDouble;

public class ItemOrdemCompra {

    private int id;
    private OrdemCompra ordemCompra;
    private ItemRequisicao itemRequisicao;
    private Produto produto;
    private Armazem armazem;
    private int quantidadeSolicitada;
    private int quantidadeRecebida;
    private BigDecimal custoUnitario = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal imposto = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal totalLinha = BigDecimal.ZERO;
    private EstadoItemOrdemCompra estado;

    public ItemOrdemCompra() {
    }

    public ItemOrdemCompra(OrdemCompra ordemCompra, ItemRequisicao itemRequisicao,
            Produto produto, Armazem armazem, int quantidadeSolicitada,
            BigDecimal custoUnitario, String estado) {
        this.ordemCompra = ordemCompra;
        this.itemRequisicao = itemRequisicao;
        this.produto = produto;
        this.armazem = armazem;
        this.quantidadeSolicitada = quantidadeSolicitada;
        setCustoUnitario(custoUnitario);
        this.estado = EstadoItemOrdemCompra.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    public ItemRequisicao getItemRequisicao() {
        return itemRequisicao;
    }

    public void setItemRequisicao(ItemRequisicao itemRequisicao) {
        this.itemRequisicao = itemRequisicao;
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

    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }

    public void setQuantidadeSolicitada(int quantidadeSolicitada) {
        this.quantidadeSolicitada = quantidadeSolicitada;
    }

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(int quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
    }

    public int getQuantidadePendente() {
        return quantidadeSolicitada - quantidadeRecebida;
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
        this.custoUnitario = custoUnitario != null ? custoUnitario : BigDecimal.ZERO;
    }

    public double getDesconto() {
        return toDouble(desconto);
    }

    public void setDesconto(double desconto) {
        this.desconto = toBigDecimal(desconto);
    }

    public BigDecimal getDescontoDecimal() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto != null ? desconto : BigDecimal.ZERO;
    }

    public double getImposto() {
        return toDouble(imposto);
    }

    public void setImposto(double imposto) {
        this.imposto = toBigDecimal(imposto);
    }

    public BigDecimal getImpostoDecimal() {
        return imposto;
    }

    public void setImposto(BigDecimal imposto) {
        this.imposto = imposto != null ? imposto : BigDecimal.ZERO;
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
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    public double getTotalLinha() {
        return toDouble(totalLinha);
    }

    public void setTotalLinha(double totalLinha) {
        this.totalLinha = toBigDecimal(totalLinha);
    }

    public BigDecimal getTotalLinhaDecimal() {
        return totalLinha;
    }

    public void setTotalLinha(BigDecimal totalLinha) {
        this.totalLinha = totalLinha != null ? totalLinha : BigDecimal.ZERO;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoItemOrdemCompra.fromValue(estado);
    }

    public EstadoItemOrdemCompra getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoItemOrdemCompra estado) {
        this.estado = estado;
    }
}
