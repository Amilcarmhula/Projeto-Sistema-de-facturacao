/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.transacoes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javafx.collections.ObservableList;
import sysfact.dao.transacoes.TransacaoDAO;
import sysfact.model.empresa.conta.ContaBancaria;
import sysfact.model.metodo_pagamento.MetodoPagamento;
import sysfact.model.transacao.Transacao;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.factura.Factura;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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

    public void registapagamentoFactura(String data_pagamento, Double valor,
            String descricao, Integer id_contabancaria,
            Integer id_metodoPagamento, String numeroFactura, Usuario user) {
        setOpsSuccess(false);
        if (data_pagamento == null || data_pagamento.isEmpty() || "null".equalsIgnoreCase(data_pagamento)) {
            showErroNotification("Erro", "Data de pagamento nao pode ser nula. Será usada a data atual!");
            data_pagamento = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        
        if (valor == null || valor <= 0) {
            showErroNotification("Erro", "Valor de pagamento nao informado!");
            return;
        }
        
        if (descricao == null || descricao.isEmpty()) {
            showErroNotification("Erro", "Pagamento sem descricao. Sera gerada uma descricao padrao!");
            descricao = "Pagamento de factura";
        }
        
        if (id_metodoPagamento == null) {
            showErroNotification("Erro", "Metodo de pagamento nao selecionado. Por favor, selecione um metodo de pagamento!");
            return;
        }
        
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroNotification("Erro", "Factura nao selecionada!");
            return;
        }

        Transacao t = new Transacao();
        t.setData_pagamento(data_pagamento);
        t.setValor(valor);
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

        if (transDao.addEntity(t)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Pagamento Registado com sucesso!");
        }
    }

    public ObservableList<Transacao> consultaTransacoes(String numero) {
        return transDao.getTransacoes(numero);
    }

}
