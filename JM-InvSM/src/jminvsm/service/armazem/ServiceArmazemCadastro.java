package jminvsm.service.armazem;

import java.sql.SQLException;
import jminvsm.model.armazem.Armazem;
import jminvsm.service.ServiceException;

public class ServiceArmazemCadastro {

    private final ServiceArmazem serviceArmazem;

    public ServiceArmazemCadastro() throws SQLException {
        this.serviceArmazem = new ServiceArmazem();
    }

    public boolean isOpsSuccess() {
        return serviceArmazem.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceArmazem.setOpsSuccess(opsSuccess);
    }

    public Armazem registar(String tipo, String nome, String descricao) {
        serviceArmazem.registar(trim(tipo), trim(nome), trim(descricao));
        return serviceArmazem.isOpsSuccess() ? serviceArmazem.getUltimoArmazem() : null;
    }

    public Armazem actualizar(String id, String tipo, String nome, String descricao) {
        Integer idArmazem = inteiroObrigatorio(id, "Armazem nao selecionado!");
        serviceArmazem.actualizar(idArmazem, trim(tipo), trim(nome), trim(descricao));
        return serviceArmazem.isOpsSuccess() ? serviceArmazem.consultaArmazem(idArmazem) : null;
    }

    public Armazem consultaPorId(String id) {
        return serviceArmazem.consultaArmazem(inteiroObrigatorio(id, "Armazem nao selecionado!"));
    }

    public Armazem consultaPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Armazem nao selecionado!");
        }
        return serviceArmazem.consultaArmazem(id);
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
