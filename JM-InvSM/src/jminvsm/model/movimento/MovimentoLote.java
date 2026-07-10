package jminvsm.model.movimento;

import jminvsm.model.lote.Lote;

public class MovimentoLote {

    private int id;
    private Movimento movimento;
    private Lote lote;
    private int quantidadeRetirada;

    public MovimentoLote() {
    }

    public MovimentoLote(Movimento movimento, Lote lote, int quantidadeRetirada) {
        this.movimento = movimento;
        this.lote = lote;
        this.quantidadeRetirada = quantidadeRetirada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public int getQuantidadeRetirada() {
        return quantidadeRetirada;
    }

    public void setQuantidadeRetirada(int quantidadeRetirada) {
        this.quantidadeRetirada = quantidadeRetirada;
    }
}
