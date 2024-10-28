/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.metodoPagamento;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.metodoPagamento.MetodoPagamentoDAO;
import jminvsm.model.metodo_pagamento.MetodoPagamento;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceMetodoPagamento {
    private final MetodoPagamentoDAO metodoDao;
    private boolean opsSuccess;

    public ServiceMetodoPagamento() throws SQLException {
        this.metodoDao = new MetodoPagamentoDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }
    
    public ObservableList<MetodoPagamento> listaMetodos(){
        return metodoDao.listAllEntities();
    }
}
