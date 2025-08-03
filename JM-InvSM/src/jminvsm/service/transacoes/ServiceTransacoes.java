/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.transacoes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.ObservableList;
import jminvsm.dao.transacoes.TransacaoDAO;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert( "Data de pagamento nao pode ser nula. Será usada a data atual!");
            data_pagamento = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        
        if (valorRecebido == null || valorRecebido <= 0) {
            showErroAlert( "Valor de pagamento nao informado!");
            return;
        }
        
        if (descricao == null || descricao.isEmpty()) {
            showErroAlert( "Pagamento sem descricao. Sera gerada uma descricao padrao!");
            descricao = "Pagamento de factura";
        }
        
        if (id_metodoPagamento == null) {
            showErroAlert( "Metodo de pagamento nao selecionado. Por favor, selecione um metodo de pagamento!");
            return;
        }
        
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroAlert( "Factura nao selecionada!");
            return;
        }

        Transacao t = new Transacao();
        t.setData_pagamento(data_pagamento);
        t.setValorRecebido(valorRecebido);
        t.setValorPago(valorPago);
        t.setTrocos(trocos);
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

        if (transDao.addTansacaoAvista(t)) {
            setOpsSuccess(true);
            showSuccessAlert( "Pagamento Registado com sucesso!");
        }
    }
    
    public void registapagamentoFacturaParcelada(Integer id_parcela, String data_pagamento, Double valorRecebido,
        Double valorPago,Double trocos, String descricao, Integer id_contabancaria,
        Integer id_metodoPagamento, String numeroFactura, Usuario user) {
        setOpsSuccess(false);
        if (data_pagamento == null || data_pagamento.isEmpty() || "null".equalsIgnoreCase(data_pagamento)) {
            data_pagamento = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        
        if (valorRecebido == null || valorRecebido <= 0) {
            showErroAlert( "Valor de pagamento nao informado!");
            return;
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Pagamento de factura";
        }
        
        if (id_metodoPagamento == null) {
            showErroAlert( "Metodo de pagamento nao selecionado. Por favor, selecione um metodo de pagamento!");
            return;
        }
        
        if (id_parcela == null || id_parcela <= 0) {
            showErroAlert( "Parcela nao selecionada!");
            return;
        }
        
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroAlert( "Factura nao selecionada!");
            return;
        }

        Transacao t = new Transacao();
        Parcela p =new Parcela();
        p.setId_parcela(id_parcela);
        t.setParcela(p);
        t.setData_pagamento(data_pagamento);
        t.setValorRecebido(valorRecebido);
        t.setValorPago(valorPago);
        t.setTrocos(trocos);
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

        if (transDao.addTansacaoParcelada(t)) {
            setOpsSuccess(true);
            showSuccessAlert( "Pagamento Registado com sucesso!");
        }
    }

    public ObservableList<Transacao> consultaTransacoes(String numero) {
        return transDao.getTransacoes(numero);
    }

}
