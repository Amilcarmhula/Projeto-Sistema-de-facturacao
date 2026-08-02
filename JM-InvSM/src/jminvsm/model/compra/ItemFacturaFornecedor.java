package jminvsm.model.compra;

import java.math.BigDecimal;
import jminvsm.model.produto.Produto;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDouble;

public class ItemFacturaFornecedor {

    private int id;
    private FacturaFornecedor facturaFornecedor;
    private ItemRecebimentoCompra itemRecebimentoCompra;
    private Produto produto;
    private String descricao;
    private int quantidade;
    private BigDecimal custoUnitario = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal imposto = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal totalLinha = BigDecimal.ZERO;

    public ItemFacturaFornecedor() {
    }

    public ItemFacturaFornecedor(FacturaFornecedor facturaFornecedor,
            ItemRecebimentoCompra itemRecebimentoCompra, Produto produto,
            String descricao, int quantidade, BigDecimal custoUnitario) {
        this.facturaFornecedor = facturaFornecedor;
        this.itemRecebimentoCompra = itemRecebimentoCompra;
        this.produto = produto;
        this.descricao = descricao;
        this.quantidade = quantidade;
        setCustoUnitario(custoUnitario);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FacturaFornecedor getFacturaFornecedor() {
        return facturaFornecedor;
    }

    public void setFacturaFornecedor(FacturaFornecedor facturaFornecedor) {
        this.facturaFornecedor = facturaFornecedor;
    }

    public ItemRecebimentoCompra getItemRecebimentoCompra() {
        return itemRecebimentoCompra;
    }

    public void setItemRecebimentoCompra(ItemRecebimentoCompra itemRecebimentoCompra) {
        this.itemRecebimentoCompra = itemRecebimentoCompra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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
}
