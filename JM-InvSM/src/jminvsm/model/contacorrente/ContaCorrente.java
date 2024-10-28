/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.contacorrente;

import jminvsm.model.transacao.Transacao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaCorrente {
  private int id;
    private double saldo_inicial;
    private double saldo_actual;
    private Transacao transacao;

    public ContaCorrente() {
    }

    public ContaCorrente(double saldo_inicial, double saldo_actual, Transacao transacao) {
        this.saldo_inicial = saldo_inicial;
        this.saldo_actual = saldo_actual;
        this.transacao = transacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public double getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(double saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }
    
    
    
}
