/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.transacoes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import jminvsm.dao.transacoes.TransacaoDAO;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceTransacoes {

    private final TransacaoDAO transDao;
    private boolean opsSuccess;

    public ServiceTransacoes() throws SQLException {
        this.transDao = new TransacaoDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registapagamentoFacturaAvista(String data_pagamento, Double valorRecebido,
        Double valorPago,Double trocos, String descricao, Integer id_contabancaria,
        Integer id_metodoPagamento, String numeroFactura, Usuario user) {
        setOpsSuccess(false);
        if (data_pagamento == null || data_pagamento.isEmpty() || "null".equalsIgnoreCase(data_pagamento)) {
            data_pagamento = LocalDate.now().toString();
        }
        
        if (valorRecebido == null || valorRecebido <= 0) {
            throw new ServiceException("Valor de pagamento nao informado!");
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Pagamento de factura";
        }
        
        if (id_metodoPagamento == null) {
            throw new ServiceException("Metodo de pagamento nao selecionado. Por favor, selecione um metodo de pagamento!");
        }
        
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new ServiceException("Factura nao selecionada!");
        }

        Transacao t = new Transacao();
        t.setData_pagamento(data_pagamento);
        t.setValorRecebido(BigDecimal.valueOf(valorRecebido));
        t.setValorPago(BigDecimal.valueOf(valorPago == null ? 0 : valorPago));
        t.setTrocos(BigDecimal.valueOf(trocos == null ? 0 : trocos));
        t.setTipo_transacao("Entrada");
        t.setDescricao(descricao);
        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setId(id_contabancaria);
        t.setContabancaria(contaBancaria);
        MetodoPagamento metodoPagamento = new MetodoPagamento();
        metodoPagamento.setId(id_metodoPagamento);
        t.setMetodo(metodoPagamento);
        Factura factura = new Factura();
        factura.setNumero_fac(numeroFactura);
        t.setFactura(factura);
        t.setUsuario(user);

        if (transDao.addTransacaoAvista(t)) {
            setOpsSuccess(true);
        }
    }
    
    public void registapagamentoFacturaParcelada(Integer id_parcela, String data_pagamento, Double valorRecebido,
        Double valorPago,Double trocos, String descricao, Integer id_contabancaria,
        Integer id_metodoPagamento, String numeroFactura, Usuario user) {
        setOpsSuccess(false);
        if (data_pagamento == null || data_pagamento.isEmpty() || "null".equalsIgnoreCase(data_pagamento)) {
            data_pagamento = LocalDate.now().toString();
        }
        
        if (valorRecebido == null || valorRecebido <= 0) {
            throw new ServiceException("Valor de pagamento nao informado!");
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Pagamento de factura";
        }
        
        if (id_metodoPagamento == null) {
            throw new ServiceException("Metodo de pagamento nao selecionado. Por favor, selecione um metodo de pagamento!");
        }
        
        if (id_parcela == null || id_parcela <= 0) {
            throw new ServiceException("Parcela nao selecionada!");
        }
        
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new ServiceException("Factura nao selecionada!");
        }

        Transacao t = new Transacao();
        Parcela p =new Parcela();
        p.setId_parcela(id_parcela);
        t.setParcela(p);
        t.setData_pagamento(data_pagamento);
        t.setValorRecebido(BigDecimal.valueOf(valorRecebido));
        t.setValorPago(BigDecimal.valueOf(valorPago == null ? 0 : valorPago));
        t.setTrocos(BigDecimal.valueOf(trocos == null ? 0 : trocos));
        t.setTipo_transacao("Entrada");
        t.setDescricao(descricao);
        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setId(id_contabancaria);
        t.setContabancaria(contaBancaria);
        MetodoPagamento metodoPagamento = new MetodoPagamento();
        metodoPagamento.setId(id_metodoPagamento);
        t.setMetodo(metodoPagamento);
        Factura factura = new Factura();
        factura.setNumero_fac(numeroFactura);
        t.setFactura(factura);
        t.setUsuario(user);

        if (transDao.addTransacaoParcelada(t)) {
            setOpsSuccess(true);
        }
    }

    public ObservableList<Transacao> consultaTransacoes(String numero) {
        return transDao.getTransacoes(numero);
    }

}
