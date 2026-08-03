package jminvsm.service.fornecedor;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.service.ServiceException;
import jminvsm.service.fornecedor.contacto.ServiceContacto;

public class ServiceContactoFornecedor {

    private final ServiceContacto serviceContacto;

    public ServiceContactoFornecedor() throws SQLException {
        this.serviceContacto = new ServiceContacto();
    }

    public boolean isOpsSuccess() {
        return serviceContacto.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceContacto.setOpsSuccess(opsSuccess);
    }

    public void registar(String email, String numero, String website, String responsavel, Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        serviceContacto.registar(trim(email), trim(numero), trim(website), trim(responsavel), fornecedor);
    }

    public void actualizar(String id, String email, String numero, String website, String responsavel) {
        serviceContacto.actualizar(inteiroObrigatorio(id, "Contacto nao selecionado!"),
                trim(email), trim(numero), trim(website), trim(responsavel));
    }

    public void excluir(String id) {
        serviceContacto.excluir(inteiroObrigatorio(id, "Contacto nao selecionado!"));
    }

    public void excluir(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Contacto nao selecionado!");
        }
        serviceContacto.excluir(id);
    }

    public ObservableList<ContactoFornecedor> consultaPorFornecedor(Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        return serviceContacto.consultaContactoPorFornecedor(fornecedor.getId());
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null || fornecedor.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados gerais do fornecedor.");
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
