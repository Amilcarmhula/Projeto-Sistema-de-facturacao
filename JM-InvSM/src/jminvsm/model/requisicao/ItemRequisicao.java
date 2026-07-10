package jminvsm.model.requisicao;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;

public class ItemRequisicao {

    private int id;
    private int quantidade;
    private Requisicao requisicao;
    private Produto produto;
    private Armazem armazem;

    public ItemRequisicao() {
    }

    public ItemRequisicao(int quantidade, Requisicao requisicao, Produto produto, Armazem armazem) {
        this.quantidade = quantidade;
        this.requisicao = requisicao;
        this.produto = produto;
        this.armazem = armazem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
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
}
