package jminvsm.service.compra;

import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.PagamentoFornecedorDAO;
import jminvsm.model.compra.ContaPagar;
import jminvsm.model.compra.PagamentoFornecedor;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.service.ServiceException;

public class ServicePagamentoFornecedor {

    private final PagamentoFornecedorDAO pagamentoFornecedorDao;
    private boolean opsSuccess;

    public ServicePagamentoFornecedor() throws SQLException {
        this.pagamentoFornecedorDao = new PagamentoFornecedorDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean registarPagamento(int idContaPagar, String dataPagamento, double valorPago,
            int idMetodoPagamento, String origemPagamento, Integer idContaBancaria,
            String referencia, String descricao, int idUsuario) {
        setOpsSuccess(false);
        if (idContaPagar <= 0) {
            throw new ServiceException("Conta a pagar nao selecionada!");
        }
        if (dataPagamento == null || dataPagamento.trim().isEmpty()) {
            dataPagamento = LocalDate.now().toString();
        }
        if (valorPago <= 0) {
            throw new ServiceException("Valor pago deve ser maior que zero!");
        }
        if (idMetodoPagamento <= 0) {
            throw new ServiceException("Metodo de pagamento nao informado!");
        }
        if (origemPagamento == null || origemPagamento.trim().isEmpty()) {
            throw new ServiceException("Origem do pagamento nao informada!");
        }
        if ("Banco".equalsIgnoreCase(origemPagamento) && (idContaBancaria == null || idContaBancaria <= 0)) {
            throw new ServiceException("Pagamento por banco exige conta bancaria!");
        }
        if (idUsuario <= 0) {
            throw new ServiceException("Utilizador nao informado!");
        }
        if (pagamentoFornecedorDao.registarPagamento(idContaPagar, dataPagamento, valorPago,
                idMetodoPagamento, origemPagamento, idContaBancaria, referencia, descricao, idUsuario)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean registarPagamento(ContaPagar conta, String dataPagamento, Double valorPago,
            MetodoPagamento metodoPagamento, String origemPagamento, ContaBancaria contaBancaria,
            String referencia, String descricao, int idUsuario) {
        if (conta == null || conta.getId() <= 0) {
            throw new ServiceException("Conta a pagar nao selecionada!");
        }
        if (valorPago == null || valorPago <= 0) {
            throw new ServiceException("Valor pago deve ser maior que zero!");
        }
        if (valorPago > conta.getSaldoPendente()) {
            throw new ServiceException("O valor pago nao pode ser maior que o saldo pendente!");
        }
        if (metodoPagamento == null || metodoPagamento.getId() <= 0) {
            throw new ServiceException("Metodo de pagamento nao informado!");
        }

        Integer idContaBancaria = null;
        if ("Banco".equalsIgnoreCase(origemPagamento)) {
            idContaBancaria = contaBancaria != null ? contaBancaria.getId() : null;
        }
        return registarPagamento(conta.getId(), dataPagamento, valorPago,
                metodoPagamento.getId(), origemPagamento, idContaBancaria,
                referencia, descricao, idUsuario);
    }

    public ObservableList<PagamentoFornecedor> listaPagamentos() {
        return pagamentoFornecedorDao.listAllEntities();
    }

    public ObservableList<PagamentoFornecedor> listaPagamentosPorContaPagar(int idContaPagar) {
        return pagamentoFornecedorDao.getEntitiesByContaPagar(idContaPagar);
    }

    public ObservableList<PagamentoFornecedor> listaPagamentosPorFornecedor(int idFornecedor) {
        return pagamentoFornecedorDao.getEntitiesByFornecedor(idFornecedor);
    }

    public PagamentoFornecedor consultaPagamentoByID(int id) {
        return pagamentoFornecedorDao.getEntityByID(id);
    }
}
