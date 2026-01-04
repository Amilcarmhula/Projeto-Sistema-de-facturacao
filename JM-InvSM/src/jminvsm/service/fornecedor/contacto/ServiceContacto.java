/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.fornecedor.contacto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.fornecedor.contacto.ContactoFornecedorDAO;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceContacto {

    private final ContactoFornecedorDAO contactoDao;
    private boolean opsSuccess;

    public ServiceContacto() throws SQLException {
        this.contactoDao = new ContactoFornecedorDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String email, String numero, String website, String responsavel, Fornecedor forn) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            email = "email.padrao@mail.com";
        }
        if (responsavel == null || responsavel.isEmpty()) {
            responsavel = forn.getRazaosocial_forn();
        }
        if (numero == null || numero.isEmpty()) {
            showErroAlert( "Numero nao pode ser nulo!");
            return;
        }
        ContactoFornecedor contacto = new ContactoFornecedor();
        contacto.setEmail_forn(email);
        contacto.setWebsite_forn(website);
        contacto.setContacto_forn(numero);
        contacto.setResponsavel_forn(responsavel);
        contacto.setFornecedor(forn);

        if (contactoDao.addEntity(contacto)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String email, String numero, String website, String responsavel) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            email = "email.padrao@mail.com";
        }

        if (numero == null || numero.isEmpty()) {
            showErroAlert( "Numero nao pode ser nulo!");
            return;
        }
        ContactoFornecedor contacto = new ContactoFornecedor();
        contacto.setEmail_forn(email);
        contacto.setWebsite_forn(website);
        contacto.setContacto_forn(numero);
        contacto.setResponsavel_forn(responsavel);

        if (contactoDao.updateEntityByID(contacto, id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo actualizado com sucesso.");
        }
    }
    public ObservableList<ContactoFornecedor> getContactos(){
        return contactoDao.listAllEntities();
    }
    
    public ObservableList<ContactoFornecedor>  consultaContactoPorFornecedor(int id){
        return contactoDao.searchEntityByCLiente(id);
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (contactoDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo excluido com sucesso.");
        }
    }
}
