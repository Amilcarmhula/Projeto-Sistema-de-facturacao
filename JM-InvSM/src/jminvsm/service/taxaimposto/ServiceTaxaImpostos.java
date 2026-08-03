/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.taxaimposto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.imposto.ImpostoDAO;
import jminvsm.model.imposto.Imposto;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceTaxaImpostos {
    private final ImpostoDAO taxaDao;
    private boolean opsSuccess;
    
    public ServiceTaxaImpostos() throws SQLException{
        this.taxaDao = new ImpostoDAO();
    }
    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }
    
    public void regista(String identificador, Double percentagem, String descricao){
        setOpsSuccess(false);
        if(identificador == null || identificador.isEmpty()){
            throw new ServiceException("Sigla da taxa/imposto nao pode ser nula!");
        }
        
        if(percentagem == null || percentagem <=0){
            throw new ServiceException("Percentagem nao pode ser nula!");
        }
        
        if(descricao == null || descricao.isEmpty()){
            descricao = "Taxa/Imposto sem descricao. Esta e uma descriao padrao!";
        }
        Imposto t = new Imposto();
        t.setNome(identificador);
        t.setPercentagem(percentagem);
        t.setDescricao(descricao);
        
        if(taxaDao.addEntity(t)){
            setOpsSuccess(true);
        }
    }
    
    public void actualizar(Integer id, String identificador, Double percentagem, String descricao){
        setOpsSuccess(false);
        if (id == null || id <= 0) {
            throw new ServiceException("Taxa/Imposto nao selecionada!");
        }
        
        if(identificador == null || identificador.isEmpty()){
            throw new ServiceException("Sigla da taxa/imposto nao pode ser nula!");
        }
        
        if(percentagem == null || percentagem <=0){
            throw new ServiceException("Percentagem nao pode ser nula!");
        }
        
        if(descricao == null || descricao.isEmpty()){
            descricao = "Taxa/Imposto sem descricao. Esta e uma descriao padrao!";
        }
        Imposto t = new Imposto();
        t.setNome(identificador);
        t.setPercentagem(percentagem);
        t.setDescricao(descricao);
        
        if(taxaDao.updateEntityByID(t, id)){
            setOpsSuccess(true);
        }
    }
    
    public void excluir(int id){
        setOpsSuccess(false);
        
        if(taxaDao.deleteEntity(id)){
            setOpsSuccess(true);
        }
    }
    public ObservableList<Imposto> getTaxas(){
        return taxaDao.listAllEntities();
    }
}
