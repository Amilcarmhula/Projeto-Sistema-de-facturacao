/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.unidadeMedida;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.unidademedida.UnidadeMedidaDAO;
import jminvsm.model.unidade_medida.UnidadeMedida;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceUnidadeMedida {
    private UnidadeMedidaDAO unidadeDao;
    
    public ServiceUnidadeMedida() throws SQLException{
        this.unidadeDao = new UnidadeMedidaDAO();
    }
    
    public ObservableList<UnidadeMedida> getUnidades(){
        return unidadeDao.listAllEntities();
    }
}
