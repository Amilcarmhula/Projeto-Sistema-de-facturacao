package jminvsm.service.cliente;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.cliente.contacto.ServiceContacto;

public class ServiceContactoCliente {

    private final ServiceContacto serviceContacto;

    public ServiceContactoCliente() throws SQLException {
        this.serviceContacto = new ServiceContacto();
    }

    public boolean isOpsSuccess() {
        return serviceContacto.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceContacto.setOpsSuccess(opsSuccess);
    }

    public void registar(String email, String numero, String website, String responsavel,
            Cliente cliente, Usuario usuario) {
        validarCliente(cliente);
        validarContacto(email, numero);
        serviceContacto.registar(trim(email), trim(numero), trim(website), trim(responsavel), cliente, usuario);
    }

    public void actualizar(String id, String email, String numero, String website,
            String responsavel, Usuario usuario) {
        validarContacto(email, numero);
        serviceContacto.actualizar(inteiroObrigatorio(id, "Contacto nao selecionado!"),
                trim(email), trim(numero), trim(website), trim(responsavel), usuario);
    }

    public void excluir(String id) {
        serviceContacto.excluir(inteiroObrigatorio(id, "Contacto nao selecionado!"));
    }

    public ObservableList<ContactoCliente> consultaPorCliente(Cliente cliente) {
        validarCliente(cliente);
        return serviceContacto.consultaContactoPorCLiente(cliente.getId());
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados gerais do cliente.");
        }
    }

    private void validarContacto(String email, String numero) {
        if (isBlank(email) && isBlank(numero)) {
            throw new ServiceException("Informe pelo menos o telefone ou o e-mail.");
        }
    }

    private Integer inteiroObrigatorio(String value, String mensagem) {
        if (isBlank(value)) {
            throw new ServiceException(mensagem);
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new ServiceException(mensagem, ex);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
