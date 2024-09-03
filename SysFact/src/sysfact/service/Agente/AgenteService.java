/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.Agente;

import javafx.collections.ObservableList;
import sysfact.dao.agente.AgenteDAO;
import sysfact.model.agente.Agente;

/**
 *
 * @author JM-Tecnologias
 */
public class AgenteService {
    private AgenteDAO agenteDao;
    
    public AgenteService( ) {
    }

    public AgenteService(AgenteDAO agenteDao) {
        this.agenteDao = agenteDao;
    }
    
    public void registaAgente(Agente agente){
        agenteDao.addEntity(agente);
    }
    
    public void actualizaAgente(Agente agente){
        agenteDao.updateEntityByID(agente);
    }
    
    public void deleteAgente(Agente agente){
        agenteDao.deleteEntityByUSER(agente);
    }
    
    public ObservableList<Agente> getAllAgente(){
        return agenteDao.listAllEntities();
    }
}
