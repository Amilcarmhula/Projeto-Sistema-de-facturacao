/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.armazem.contacto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.armazem.contacto.ContactoArmazemDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceContacto {
    private final ContactoArmazemDAO contatoArmazemDao;
    private boolean opsSuccess;

    public ServiceContacto() throws SQLException {
        this.contatoArmazemDao = new ContactoArmazemDAO();
    }
    
    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }
    
    public void registar(String responsavel, String contacto, String email, Integer id_arm) {
        setOpsSuccess(false);
        if (responsavel == null || responsavel.isEmpty()) {
            throw new ServiceException("Responsavel nao pode ser nulo!");
        }
        if (contacto == null || contacto.isEmpty()) {
            throw new ServiceException("Numero de contacto nao introduzido!");
        }
        if (email == null || email.isEmpty()) {
            email = "armazem.sem.email@sememail.com";
        }
        if (id_arm == null) {
            throw new ServiceException("Armazem nao selecionado!");
        }

        ContactoArmazem x = new ContactoArmazem();
        Armazem a = new Armazem();
        a.setId(id_arm);
        x.setResponsavel_arm(responsavel);
        x.setContacto_arm(contacto);
        x.setEmail_arm(email);
        x.setArmazem(a);

        if (contatoArmazemDao.addEntity(x)) {
            setOpsSuccess(true);
        }
    }
    
    public void actualizar(String responsavel, String contacto, String email, Integer id_contacto) {
        setOpsSuccess(false);
        if (responsavel == null || responsavel.isEmpty()) {
            throw new ServiceException("Responsavel nao pode ser nulo!");
        }
        if (contacto == null || contacto.isEmpty()) {
            throw new ServiceException("Numero de contacto nao introduzido!");
        }
        if (email == null || email.isEmpty()) {
            email = "armazem.sem.email@sememail.com";
        }
        if (id_contacto == null) {
            throw new ServiceException("Contacto nao selecionado!");
        }

        ContactoArmazem x = new ContactoArmazem();
        x.setResponsavel_arm(responsavel);
        x.setContacto_arm(contacto);
        x.setEmail_arm(email);

        if (contatoArmazemDao.updateEntityByID(x, id_contacto)) {
            setOpsSuccess(true);
        }
    }
    
    public ObservableList<ContactoArmazem> listaContactos(){
        return contatoArmazemDao.listAllEntities();
    }
    
    public ObservableList<ContactoArmazem> consultaContactoPorArmazem(int id){
        return contatoArmazemDao.searchEntityByCLiente(id);
    }
    
    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (contatoArmazemDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }
}
