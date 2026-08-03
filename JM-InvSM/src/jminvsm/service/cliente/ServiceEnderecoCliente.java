package jminvsm.service.cliente;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.cliente.endereco.ServiceEndereco;

public class ServiceEnderecoCliente {

    private final ServiceEndereco serviceEndereco;

    public ServiceEnderecoCliente() throws SQLException {
        this.serviceEndereco = new ServiceEndereco();
    }

    public boolean isOpsSuccess() {
        return serviceEndereco.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceEndereco.setOpsSuccess(opsSuccess);
    }

    public void registar(String tipo, String avenida, String bairro, String cidade,
            String codigoPostal, String numero, String provincia, String rua,
            Cliente cliente, Usuario usuario) {
        validarCliente(cliente);
        validarEndereco(tipo, provincia, cidade, codigoPostal, numero);
        serviceEndereco.registar(trim(tipo), trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal), inteiroOuZero(numero), trim(provincia), trim(rua),
                cliente, usuario);
    }

    public void actualizar(String id, String tipo, String avenida, String bairro, String cidade,
            String codigoPostal, String numero, String provincia, String rua, Usuario usuario) {
        validarEndereco(tipo, provincia, cidade, codigoPostal, numero);
        serviceEndereco.actualizar(inteiroObrigatorio(id, "Endereco nao selecionado!"),
                trim(tipo), trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal), inteiroOuZero(numero), trim(provincia), trim(rua),
                usuario);
    }

    public void excluir(String id) {
        serviceEndereco.excluir(inteiroObrigatorio(id, "Endereco nao selecionado!"));
    }

    public ObservableList<EnderecoCliente> consultaPorCliente(Cliente cliente) {
        validarCliente(cliente);
        return serviceEndereco.consultaEnderecosPorCLiente(cliente.getId());
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados gerais do cliente.");
        }
    }

    private void validarEndereco(String tipo, String provincia, String cidade, String codigoPostal, String numero) {
        if (isBlank(tipo)) {
            throw new ServiceException("Seleccione o tipo ou finalidade do endereco.");
        }
        if (isBlank(provincia)) {
            throw new ServiceException("Seleccione a provincia.");
        }
        if (isBlank(cidade)) {
            throw new ServiceException("Informe a cidade.");
        }
        if (!isBlank(codigoPostal)) {
            inteiroObrigatorio(codigoPostal, "O codigo postal deve conter apenas numeros.");
        }
        if (!isBlank(numero)) {
            inteiroObrigatorio(numero, "O numero do endereco deve conter apenas numeros.");
        }
    }

    private Integer inteiroOuZero(String value) {
        return isBlank(value) ? 0 : inteiroObrigatorio(value, "Valor numerico invalido.");
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
