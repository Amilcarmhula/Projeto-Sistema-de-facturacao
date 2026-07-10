package jminvsm.model.transferencia;

import jminvsm.model.produto.Produto;

public class ItemTransferencia {

    private int id;
    private TransferenciaStock transferencia;
    private Produto produto;
    private int quantidade;
    private int quantidadeRecebida;

    public ItemTransferencia() {
    }

    public ItemTransferencia(TransferenciaStock transferencia, Produto produto,
            int quantidade, int quantidadeRecebida) {
        this.transferencia = transferencia;
        this.produto = produto;
        this.quantidade = quantidade;
        this.quantidadeRecebida = quantidadeRecebida;
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

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(int quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
    }
}
