/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.armazem.contacto;

import java.sql.SQLException;
import sysfact.dao.armazem.contacto.ContactoArmazemDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.armazem.contacto.ContactoArmazem;
import sysfact.model.usuario.Usuario;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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
    
    public void registaContacto(String responsavel, String contacto, String email, Integer id_arm, Usuario user) {
        setOpsSuccess(false);
        if (responsavel == null || responsavel.isEmpty()) {
            showErroNotification("Erro", "Responsavel nao pode ser nulo!");
            return;
        }
        if (contacto == null || contacto.isEmpty()) {
            showErroNotification("Erro", "Numero de contacto nao introduzido!");
            return;
        }
        if (email == null || email.isEmpty()) {
            showErroNotification("Erro", "E-mail nulo, sera definida um E-mail padrao!");
            email = "armazem.sem.email@sememail.com";
        }
        if (id_arm == null) {
            showErroNotification("Erro", "Armazem nao selecionado!");
            return;
        }

        ContactoArmazem x = new ContactoArmazem();
        Armazem a = new Armazem();
        a.setId(id_arm);
        x.setResponsavel_arm(responsavel);
        x.setContacto_arm(contacto);
        x.setEmail_arm(email);
        x.setUsuario(user);
        x.setArmazem(a);

        if (contatoArmazemDao.addEntity(x)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Contacto adicionado com sucesso!");
        }
    }
    
    public void actualizaContacto(String responsavel, String contacto, String email, Integer id_contacto, Usuario user) {
        setOpsSuccess(false);
        if (responsavel == null || responsavel.isEmpty()) {
            showErroNotification("Erro", "Responsavel nao pode ser nulo!");
            return;
        }
        if (contacto == null || contacto.isEmpty()) {
            showErroNotification("Erro", "Numero de contacto nao introduzido!");
            return;
        }
        if (email == null || email.isEmpty()) {
            showErroNotification("Erro", "E-mail nulo, sera definida um E-mail padrao!");
            email = "armazem.sem.email@sememail.com";
        }
        if (id_contacto == null) {
            showErroNotification("Erro", "Armazem nao selecionado!");
            return;
        }

        ContactoArmazem x = new ContactoArmazem();
        x.setResponsavel_arm(responsavel);
        x.setContacto_arm(contacto);
        x.setEmail_arm(email);
        x.setUsuario(user);

        if (contatoArmazemDao.updateEntityByID(x, id_contacto)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Contacto actualizado com sucesso!");
        }
    }
    
    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (contatoArmazemDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Registo excluido com sucesso!");
        }
    }
}
