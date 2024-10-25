/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.cliente.contacto;

import java.sql.SQLException;
import sysfact.dao.cliente.contacto.ContactoClienteDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.model.usuario.Usuario;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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
    
    public void registaContacto(String email, String numero, String website, String responsavel, Cliente cli, Usuario user) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            showErroNotification("Erro", "E-mail padrao criado!");
            email = "email.padrao@mail.com";
        }
        if (responsavel == null || responsavel.isEmpty()) {
            showErroNotification("Erro", "Pessoa de contacto padrao definida.");
            responsavel = cli.getNome_cli();
        }
        if (numero == null || numero.isEmpty()) {
            showErroNotification("Erro", "Numero nao pode ser nulo!");
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
            showSuccessNotification("Sucesso", "Registo adicionado com sucesso.");
        }
    }
    
    public void actualizaContacto(Integer id, String email, String numero, String website, String responsavel, Usuario user) {
        setOpsSuccess(false);
        if (email == null || email.isEmpty()) {
            showErroNotification("Erro", "E-mail padrao criado!");
            email = "email.padrao@mail.com";
        }

        if (numero == null || numero.isEmpty()) {
            showErroNotification("Erro", "Numero nao pode ser nulo!");
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
            showSuccessNotification("Sucesso", "Registo actualizado com sucesso.");
        }
    }
}
