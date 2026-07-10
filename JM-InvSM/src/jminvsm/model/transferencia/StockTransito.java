package jminvsm.model.transferencia;

import jminvsm.model.enums.EstadoStockTransito;
import jminvsm.model.produto.Produto;

public class StockTransito {

    private int id;
    private TransferenciaStock transferencia;
    private Produto produto;
    private int quantidade;
    private EstadoStockTransito estado;

    public StockTransito() {
    }

    public StockTransito(TransferenciaStock transferencia, Produto produto, int quantidade, String estado) {
        this.transferencia = transferencia;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = EstadoStockTransito.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransferenciaStock getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(TransferenciaStock transferencia) {
        this.transferencia = transferencia;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoStockTransito.fromValue(estado);
    }

    public EstadoStockTransito getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoStockTransito estado) {
        this.estado = estado;
    }
}
