/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.contacorrente;

import java.sql.SQLException;
import jminvsm.dao.contacorrente.ContaCorrenteDAO;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceContaCorrente {
    private final ContaCorrenteDAO contaCorrenteDao;
    private boolean opsSuccess;

    public ServiceContaCorrente() throws SQLException {
        this.contaCorrenteDao = new ContaCorrenteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }
    public double consultaContaCorrente(){
        if(contaCorrenteDao.getLastEntity() != null){
           return contaCorrenteDao.getLastEntity().getSaldo_actual();
        }
        return 0;
    }
}
