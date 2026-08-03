package jminvsm.service.transacoes;

import jminvsm.model.metodo_pagamento.MetodoPagamento;

/**
 * Centraliza regras de calculo usadas no registo de pagamentos.
 */
public class ServiceCalculoPagamento {

    private static final int METODO_DINHEIRO_ID = 1;

    public double calcularTroco(double total, double valorRecebido, MetodoPagamento metodoPagamento) {
        if (isPagamentoEmDinheiro(metodoPagamento) && valorRecebido > total) {
            return valorRecebido - total;
        }
        return 0;
    }

    public double calcularValorPago(double total, double valorRecebido, MetodoPagamento metodoPagamento) {
        return valorRecebido - calcularTroco(total, valorRecebido, metodoPagamento);
    }

    private boolean isPagamentoEmDinheiro(MetodoPagamento metodoPagamento) {
        return metodoPagamento != null && metodoPagamento.getId() == METODO_DINHEIRO_ID;
    }
}
