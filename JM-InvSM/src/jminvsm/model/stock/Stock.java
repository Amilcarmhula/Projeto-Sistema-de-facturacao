/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.stock;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;

/**
 *
 * @author JM-Tecnologias
 */
public class Stock {
    private Produto produto;
    private Armazem armazem;
    private PrecoVenda precoVenda;
    private int saldo;

    public Stock() {
    }

    public Stock(Produto produto, int saldo, Armazem armazem) {
        this.produto = produto;
        this.saldo = saldo;
        this.armazem = armazem;
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

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public PrecoVenda getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(PrecoVenda precoVenda) {
        this.precoVenda = precoVenda;
    }

   
    
    
}
