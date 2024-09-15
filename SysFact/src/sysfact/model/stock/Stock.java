/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.stock;

import sysfact.model.produto.Produto;

/**
 *
 * @author JM-Tecnologias
 */
public class Stock {
    private Produto produto;
    private int saldo;

    public Stock() {
    }

    public Stock(Produto produto, int saldo) {
        this.produto = produto;
        this.saldo = saldo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

   
    
    
}
