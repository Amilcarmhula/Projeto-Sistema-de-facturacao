/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.cliente.contacto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.cliente.contacto.ContactoClienteDAO;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceContacto {

    private final ContactoClienteDAO contactoDao;
    private boolean opsSuccess;

    public ServiceContacto() throws SQLException {
        this.contactoDao = new ContactoClienteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String email, String numero, String website, String responsavel, Cliente cli, Usuario user) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            email = "email.padrao@mail.com";
        }
        if (responsavel == null || responsavel.isEmpty()) {
            responsavel = cli.getNome_cli();
        }
        if (numero == null || numero.isEmpty()) {
            showErroAlert( "Numero nao pode ser nulo!");
            return;
        }
        ContactoCliente contacto = new ContactoCliente();
        contacto.setEmail_cli(email);
        contacto.setWebsite_cli(website);
        contacto.setContacto_cli(numero);
        contacto.setResponsavel(responsavel);
        contacto.setUsuario(user);
        contacto.setCliente(cli);

        if (contactoDao.addEntity(contacto)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String email, String numero, String website, String responsavel, Usuario user) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            email = "email.padrao@mail.com";
        }

        if (numero == null || numero.isEmpty()) {
            showErroAlert( "Numero nao pode ser nulo!");
            return;
        }
        ContactoCliente contacto = new ContactoCliente();
        contacto.setEmail_cli(email);
        contacto.setWebsite_cli(website);
        contacto.setContacto_cli(numero);
        contacto.setResponsavel(responsavel);
        contacto.setUsuario(user);

        if (contactoDao.updateEntityByID(contacto, id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo actualizado com sucesso.");
        }
    }
    public ObservableList<ContactoCliente> listaContactos(){
        return contactoDao.listAllEntities();
    }
    
    public ObservableList<ContactoCliente> consultaContactoPorCLiente(int idCliente){
        return contactoDao.searchEntityByCLiente(idCliente);
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (contactoDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo excluido com sucesso.");
        }
    }
}
