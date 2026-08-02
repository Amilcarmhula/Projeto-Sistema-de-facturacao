package jminvsm.model.compra;

import java.math.BigDecimal;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoItemRecebimentoCompra;
import jminvsm.model.lote.Lote;
import jminvsm.model.produto.Produto;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDouble;

public class ItemRecebimentoCompra {

    private int id;
    private RecebimentoCompra recebimentoCompra;
    private ItemOrdemCompra itemOrdemCompra;
    private Produto produto;
    private Armazem armazem;
    private int quantidadeRecebida;
    private BigDecimal custoUnitario = BigDecimal.ZERO;
    private String codigoLote;
    private Lote lote;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private EstadoItemRecebimentoCompra estado;

    public ItemRecebimentoCompra() {
    }

    public ItemRecebimentoCompra(RecebimentoCompra recebimentoCompra,
            ItemOrdemCompra itemOrdemCompra, Produto produto, Armazem armazem,
            int quantidadeRecebida, BigDecimal custoUnitario, String estado) {
        this.recebimentoCompra = recebimentoCompra;
        this.itemOrdemCompra = itemOrdemCompra;
        this.produto = produto;
        this.armazem = armazem;
        this.quantidadeRecebida = quantidadeRecebida;
        setCustoUnitario(custoUnitario);
        this.estado = EstadoItemRecebimentoCompra.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecebimentoCompra getRecebimentoCompra() {
        return recebimentoCompra;
    }

    public void setRecebimentoCompra(RecebimentoCompra recebimentoCompra) {
        this.recebimentoCompra = recebimentoCompra;
    }

    public ItemOrdemCompra getItemOrdemCompra() {
        return itemOrdemCompra;
    }

    public void setItemOrdemCompra(ItemOrdemCompra itemOrdemCompra) {
        this.itemOrdemCompra = itemOrdemCompra;
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

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(int quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
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

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
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

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoItemRecebimentoCompra.fromValue(estado);
    }

    public EstadoItemRecebimentoCompra getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoItemRecebimentoCompra estado) {
        this.estado = estado;
    }
}
