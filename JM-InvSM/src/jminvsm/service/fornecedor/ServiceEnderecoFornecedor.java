package jminvsm.service.fornecedor;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.fornecedor.endereco.ServiceEndereco;

public class ServiceEnderecoFornecedor {

    private final ServiceEndereco serviceEndereco;

    public ServiceEnderecoFornecedor() throws SQLException {
        this.serviceEndereco = new ServiceEndereco();
    }

    public boolean isOpsSuccess() {
        return serviceEndereco.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceEndereco.setOpsSuccess(opsSuccess);
    }

    public void registar(String avenida, String bairro, String cidade, String codigoPostal,
            String numero, String provincia, String rua, Fornecedor fornecedor, Usuario usuario) {
        validarFornecedor(fornecedor);
        serviceEndereco.registar(trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal, "O codigo postal deve conter apenas numeros."),
                inteiroOuZero(numero, "O numero do endereco deve conter apenas numeros."),
                trim(provincia), trim(rua), fornecedor.getId(), usuario);
    }

    public void actualizar(String id, String avenida, String bairro, String cidade,
            String codigoPostal, String numero, String provincia, String rua,
            Fornecedor fornecedor, Usuario usuario) {
        validarFornecedor(fornecedor);
        serviceEndereco.actualizar(inteiroObrigatorio(id, "Endereco nao selecionado!"),
                trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal, "O codigo postal deve conter apenas numeros."),
                inteiroOuZero(numero, "O numero do endereco deve conter apenas numeros."),
                trim(provincia), trim(rua), fornecedor.getId(), usuario);
    }

    public void excluir(String id) {
        serviceEndereco.excluir(inteiroObrigatorio(id, "Endereco nao selecionado!"));
    }

    public void excluir(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Endereco nao selecionado!");
        }
        serviceEndereco.excluir(id);
    }

    public ObservableList<EnderecoFornecedor> consultaPorFornecedor(Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        return serviceEndereco.consultaEnderecosPorFornecedor(fornecedor.getId());
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null || fornecedor.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados gerais do fornecedor.");
        }
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
