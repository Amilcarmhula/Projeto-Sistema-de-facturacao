/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.contacorrente;

import java.math.BigDecimal;
import jminvsm.model.transacao.Transacao;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDouble;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaCorrente {
    private int id;
    private BigDecimal saldo_inicial = BigDecimal.ZERO;
    private BigDecimal saldo_actual = BigDecimal.ZERO;
    private Transacao transacao;

    public ContaCorrente() {
    }

    public ContaCorrente(double saldo_inicial, double saldo_actual, Transacao transacao) {
        this.saldo_inicial = toBigDecimal(saldo_inicial);
        this.saldo_actual = toBigDecimal(saldo_actual);
        this.transacao = transacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo_inicial() {
        return toDouble(saldo_inicial);
    }

    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = toBigDecimal(saldo_inicial);
    }

    public BigDecimal getSaldoInicialDecimal() {
        return saldo_inicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldo_inicial = saldoInicial;
    }

    public double getSaldo_actual() {
        return toDouble(saldo_actual);
    }

    public void setSaldo_actual(double saldo_actual) {
        this.saldo_actual = toBigDecimal(saldo_actual);
    }

    public BigDecimal getSaldoActualDecimal() {
        return saldo_actual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldo_actual = saldoActual;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }
    
    
    
}
