package jminvsm.service.transacoes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.ServiceException;
import jminvsm.service.metodoPagamento.ServiceMetodoPagamento;
import jminvsm.service.vendas.factura.ServiceFactura;

public class ServiceFluxoPagamento {

    private final ServiceFactura serviceFactura;
    private final ServiceMetodoPagamento serviceMetodoPagamento;
    private final ServiceTransacoes serviceTransacoes;
    private final ServiceCalculoPagamento serviceCalculoPagamento;

    public ServiceFluxoPagamento() throws SQLException {
        this.serviceFactura = new ServiceFactura();
        this.serviceMetodoPagamento = new ServiceMetodoPagamento();
        this.serviceTransacoes = new ServiceTransacoes();
        this.serviceCalculoPagamento = new ServiceCalculoPagamento();
    }

    public ObservableList<MetodoPagamento> listarMetodos() {
        return serviceMetodoPagamento.listaMetodos();
    }

    public Map<String, MetodoPagamento> mapearMetodosPorNome(ObservableList<MetodoPagamento> metodos) {
        Map<String, MetodoPagamento> mapa = new HashMap<>();
        if (metodos == null) {
            return mapa;
        }
        for (MetodoPagamento metodo : metodos) {
            if (metodo != null && metodo.getNomePagamento() != null) {
                mapa.put(metodo.getNomePagamento(), metodo);
            }
        }
        return mapa;
    }

    public Factura consultarFactura(String numeroFactura) {
        return serviceFactura.consultaFactura(numeroFactura);
    }

    public ObservableList<Factura> listarFacturasCliente(int idCliente) {
        return serviceFactura.exibeFacturaPorCliente(idCliente);
    }

    public ObservableList<Transacao> listarTransacoes(String numeroFactura) {
        return serviceTransacoes.consultaTransacoes(numeroFactura);
    }

    public double saldoFactura(String numeroFactura) {
        Factura factura = consultarFactura(numeroFactura);
        return factura == null ? 0.0 : factura.getTotal() - factura.getTotalPago();
    }

    public double calcularTroco(double total, double valorRecebido, MetodoPagamento metodo) {
        return serviceCalculoPagamento.calcularTroco(total, valorRecebido, metodo);
    }

    public double calcularValorPago(double total, double valorRecebido, MetodoPagamento metodo) {
        return serviceCalculoPagamento.calcularValorPago(total, valorRecebido, metodo);
    }

    public boolean registarPagamentoAvista(String numeroFactura, String valorRecebidoTexto, double troco,
            String descricao, MetodoPagamento metodo, Empresa empresa, Usuario usuario) {
        if (metodo == null) {
            throw new ServiceException("Metodo de pagamento nao selecionado!");
        }
        double total = saldoFactura(numeroFactura);
        double valorRecebido = parseDouble(valorRecebidoTexto, 0.0);
        double valorPago = calcularValorPago(total, valorRecebido, metodo);
        Integer idContaBancaria = empresa != null && empresa.getContaBancaria() != null
                ? empresa.getContaBancaria().getId() : null;
        serviceTransacoes.registapagamentoFacturaAvista(LocalDate.now().toString(), valorRecebido, valorPago,
                troco, descricao, idContaBancaria, metodo.getId(), numeroFactura, usuario);
        return serviceTransacoes.isOpsSuccess();
    }

    public boolean registarPagamentoParcelado(int idParcela, String numeroFactura, String dataPagamento,
            String valorPorPagarTexto, String valorRecebidoTexto, double troco, String descricao,
            MetodoPagamento metodo, Empresa empresa, Usuario usuario) {
        if (metodo == null) {
            throw new ServiceException("Metodo de pagamento nao selecionado!");
        }
        double total = parseDouble(valorPorPagarTexto, 0.0);
        double valorRecebido = parseDouble(valorRecebidoTexto, 0.0);
        double valorPago = calcularValorPago(total, valorRecebido, metodo);
        Integer idContaBancaria = empresa != null && empresa.getContaBancaria() != null
                ? empresa.getContaBancaria().getId() : null;
        serviceTransacoes.registapagamentoFacturaParcelada(idParcela,
                dataPagamento == null || dataPagamento.trim().isEmpty() ? LocalDate.now().toString() : dataPagamento,
                valorRecebido, valorPago, troco, descricao, idContaBancaria, metodo.getId(), numeroFactura, usuario);
        return serviceTransacoes.isOpsSuccess();
    }

    public double parseDouble(String value, double defaultValue) {
        try {
            return value == null || value.trim().isEmpty() ? defaultValue : Double.valueOf(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
