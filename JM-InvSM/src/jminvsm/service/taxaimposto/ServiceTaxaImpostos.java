/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.taxaimposto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.taxaimposto.TaxaImpostoDAO;
import jminvsm.model.taxa_imposto.TaxaImposto;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceTaxaImpostos {
    private TaxaImpostoDAO taxaDao;
    
    public ServiceTaxaImpostos() throws SQLException{
        this.taxaDao = new TaxaImpostoDAO();
    }
    
    public ObservableList<TaxaImposto> getTaxas(){
        return taxaDao.listAllEntities();
    }
}
