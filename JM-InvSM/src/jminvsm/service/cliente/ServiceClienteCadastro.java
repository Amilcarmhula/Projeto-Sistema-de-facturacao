package jminvsm.service.cliente;

import java.sql.SQLException;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

public class ServiceClienteCadastro {

    private final ServiceCliente serviceCliente;

    public ServiceClienteCadastro() throws SQLException {
        this.serviceCliente = new ServiceCliente();
    }

    public boolean isOpsSuccess() {
        return serviceCliente.isOpsSuccess();
    }

    public Cliente registar(String nome, String razaoSocial, String tipo, String nuit, Usuario usuario) {
        serviceCliente.registar(trim(nome), trim(razaoSocial), trim(tipo), inteiroOuZero(nuit), usuario);
        return serviceCliente.isOpsSuccess() ? serviceCliente.getUtimoCliente() : null;
    }

    public Cliente actualizar(String id, String nome, String razaoSocial, String tipo, String nuit, Usuario usuario) {
        Integer idCliente = inteiroObrigatorio(id, "Cliente nao selecionado!");
        serviceCliente.actualizar(idCliente, trim(nome), trim(razaoSocial), trim(tipo), inteiroOuZero(nuit), usuario);
        return serviceCliente.isOpsSuccess() ? serviceCliente.consultaClienteByID(idCliente) : null;
    }

    public Cliente consultaPorId(String id) {
        return serviceCliente.consultaClienteByID(inteiroObrigatorio(id, "Cliente nao selecionado!"));
    }

    public Cliente consultaPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Cliente nao selecionado!");
        }
        return serviceCliente.consultaClienteByID(id);
    }

    private Integer inteiroOuZero(String value) {
        if (isBlank(value)) {
            return 0;
        }
        return inteiroObrigatorio(value, "O NUIT deve conter apenas numeros.");
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
