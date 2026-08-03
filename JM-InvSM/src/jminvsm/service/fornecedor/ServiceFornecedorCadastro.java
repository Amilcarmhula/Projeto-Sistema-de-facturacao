package jminvsm.service.fornecedor;

import java.sql.SQLException;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.service.ServiceException;

public class ServiceFornecedorCadastro {

    private final ServiceFornecedor serviceFornecedor;

    public ServiceFornecedorCadastro() throws SQLException {
        this.serviceFornecedor = new ServiceFornecedor();
    }

    public boolean isOpsSuccess() {
        return serviceFornecedor.isOpsSuccess();
    }

    public Fornecedor registar(String razaoSocial, String tipo, String nuit) {
        serviceFornecedor.registar(trim(razaoSocial), trim(tipo), inteiroOuZero(nuit, "O NUIT deve conter apenas numeros."));
        return serviceFornecedor.isOpsSuccess() ? serviceFornecedor.ultimoFornecedor() : null;
    }

    public Fornecedor actualizar(String id, String razaoSocial, String tipo, String nuit) {
        Integer idFornecedor = inteiroObrigatorio(id, "Fornecedor nao selecionado!");
        serviceFornecedor.actualizar(idFornecedor, trim(razaoSocial), trim(tipo),
                inteiroOuZero(nuit, "O NUIT deve conter apenas numeros."));
        return serviceFornecedor.isOpsSuccess() ? serviceFornecedor.consultaFornecedor(idFornecedor) : null;
    }

    public Fornecedor consultaPorId(String id) {
        return serviceFornecedor.consultaFornecedor(inteiroObrigatorio(id, "Fornecedor nao selecionado!"));
    }

    public Fornecedor consultaPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Fornecedor nao selecionado!");
        }
        return serviceFornecedor.consultaFornecedor(id);
    }

    private Integer inteiroOuZero(String value, String mensagem) {
        if (isBlank(value)) {
            return 0;
        }
        return inteiroObrigatorio(value, mensagem);
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
