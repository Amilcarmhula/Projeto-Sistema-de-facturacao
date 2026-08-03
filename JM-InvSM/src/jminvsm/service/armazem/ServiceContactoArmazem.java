package jminvsm.service.armazem;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.contacto.ServiceContacto;

public class ServiceContactoArmazem {

    private final ServiceContacto serviceContacto;

    public ServiceContactoArmazem() throws SQLException {
        this.serviceContacto = new ServiceContacto();
    }

    public boolean isOpsSuccess() {
        return serviceContacto.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceContacto.setOpsSuccess(opsSuccess);
    }

    public void registar(String responsavel, String contacto, String email, Armazem armazem) {
        validarArmazem(armazem);
        serviceContacto.registar(trim(responsavel), trim(contacto), trim(email), armazem.getId());
    }

    public void actualizar(String id, String responsavel, String contacto, String email) {
        serviceContacto.actualizar(trim(responsavel), trim(contacto), trim(email),
                inteiroObrigatorio(id, "Contacto nao selecionado!"));
    }

    public void excluir(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Contacto nao selecionado!");
        }
        serviceContacto.excluir(id);
    }

    public ObservableList<ContactoArmazem> consultaPorArmazem(Armazem armazem) {
        validarArmazem(armazem);
        return serviceContacto.consultaContactoPorArmazem(armazem.getId());
    }

    private void validarArmazem(Armazem armazem) {
        if (armazem == null || armazem.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados do armazem.");
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
