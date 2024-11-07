/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.caixa;

import java.sql.SQLException;
import jminvsm.dao.caixa.CaixaDAO;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceCaixa {
    private final CaixaDAO caixaDao;
    private boolean opsSuccess;

    public ServiceCaixa() throws SQLException {
        this.caixaDao = new CaixaDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }
    public double consultaCaixa(){
        if(caixaDao.getLastEntity() != null){
            return caixaDao.getLastEntity().getSaldo_actual();
        }
        return 0;
    }
}
