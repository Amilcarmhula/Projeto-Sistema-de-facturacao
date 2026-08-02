package jminvsm.service.compra;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.ContaPagarDAO;
import jminvsm.model.compra.ContaPagar;
import jminvsm.service.ServiceException;

public class ServiceContaPagar {

    private final ContaPagarDAO contaPagarDao;
    private boolean opsSuccess;

    public ServiceContaPagar() throws SQLException {
        this.contaPagarDao = new ContaPagarDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean cancelarContaPagar(int id) {
        setOpsSuccess(false);
        if (id <= 0) {
            throw new ServiceException("Conta a pagar nao selecionada!");
        }
        if (contaPagarDao.updateEstado(id, "Cancelada")) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public ObservableList<ContaPagar> listaContasPagar() {
        return contaPagarDao.listAllEntities();
    }

    public ObservableList<ContaPagar> listaContasPagarPorEstado(String estado) {
        return contaPagarDao.getEntitiesByEstado(estado);
    }

    public ObservableList<ContaPagar> listaContasPagarPorFornecedor(int idFornecedor) {
        return contaPagarDao.getEntitiesByFornecedor(idFornecedor);
    }

    public ContaPagar consultaContaPagarByID(int id) {
        return contaPagarDao.getEntityByID(id);
    }
}
