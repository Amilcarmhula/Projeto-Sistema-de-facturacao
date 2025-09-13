/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.taxaimposto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.imposto.ImpostoDAO;
import jminvsm.model.imposto.Imposto;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert("Sigla da unidade de medida nao pode ser nulo!");
            return;
        }
        
        if(percentagem == null || percentagem <=0){
            showErroAlert("Percentagem nao pode ser nulo!");
            return;
        }
        
        if(descricao == null || descricao.isEmpty()){
            descricao = "Taxa/Imposto sem descricao. Esta e uma descriao padrao!";
        }
        Imposto t = new Imposto();
        t.setNome(identificador);
        t.setPercentagem(percentagem/100);
        t.setDescricao(descricao);
        
        if(taxaDao.addEntity(t)){
            setOpsSuccess(true);
            showSuccessAlert("Registo adicionado com sucesso");
        }
    }
    
    public void actualizar(Integer id, String identificador, Double percentagem, String descricao){
        setOpsSuccess(false);
        if (id == null || id <= 0) {
            showErroAlert("Taxa/Imposto nao selecionada!");
            return;
        }
        
        if(identificador == null || identificador.isEmpty()){
            showErroAlert("Sigla da unidade de medida nao pode ser nulo!");
            return;
        }
        
        if(percentagem == null || percentagem <=0){
            showErroAlert("Percentagem nao pode ser nulo!");
            return;
        }
        
        if(descricao == null || descricao.isEmpty()){
            descricao = "Taxa/Imposto sem descricao. Esta e uma descriao padrao!";
        }
        Imposto t = new Imposto();
        t.setNome(identificador);
        t.setPercentagem(percentagem/100);
        t.setDescricao(descricao);
        
        if(taxaDao.updateEntityByID(t, id)){
            setOpsSuccess(true);
            showSuccessAlert("Registo adicionado com sucesso");
        }
    }
    
    public void excluir(int id){
        setOpsSuccess(false);
        
        if(taxaDao.deleteEntity(id)){
            setOpsSuccess(true);
            showSuccessAlert("Registo excluido com sucesso");
        }
    }
    public ObservableList<Imposto> getTaxas(){
        return taxaDao.listAllEntities();
    }
}
