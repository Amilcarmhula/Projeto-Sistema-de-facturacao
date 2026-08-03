package jminvsm.service.armazem;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.endereco.EnderecoArmazem;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.endereco.ServiceEndereco;

public class ServiceEnderecoArmazem {

    private final ServiceEndereco serviceEndereco;

    public ServiceEnderecoArmazem() throws SQLException {
        this.serviceEndereco = new ServiceEndereco();
    }

    public boolean isOpsSuccess() {
        return serviceEndereco.isOpsSuccess();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        serviceEndereco.setOpsSuccess(opsSuccess);
    }

    public void registar(String avenida, String bairro, String cidade, String codigoPostal,
            String numero, String provincia, String rua, Armazem armazem) {
        validarArmazem(armazem);
        serviceEndereco.registar(trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal, "O codigo postal deve conter apenas numeros."),
                inteiroOuZero(numero, "O numero do endereco deve conter apenas numeros."),
                trim(provincia), trim(rua), armazem.getId());
    }

    public void actualizar(String id, String avenida, String bairro, String cidade,
            String codigoPostal, String numero, String provincia, String rua) {
        serviceEndereco.actualizar(trim(avenida), trim(bairro), trim(cidade),
                inteiroOuZero(codigoPostal, "O codigo postal deve conter apenas numeros."),
                inteiroOuZero(numero, "O numero do endereco deve conter apenas numeros."),
                trim(provincia), trim(rua), inteiroObrigatorio(id, "Endereco nao selecionado!"));
    }

    public void excluir(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceException("Endereco nao selecionado!");
        }
        serviceEndereco.excluir(id);
    }

    public ObservableList<EnderecoArmazem> consultaPorArmazem(Armazem armazem) {
        validarArmazem(armazem);
        return serviceEndereco.consultaEnderecosPorArmazem(armazem.getId());
    }

    private void validarArmazem(Armazem armazem) {
        if (armazem == null || armazem.getId() <= 0) {
            throw new ServiceException("Guarde primeiro os dados do armazem.");
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
