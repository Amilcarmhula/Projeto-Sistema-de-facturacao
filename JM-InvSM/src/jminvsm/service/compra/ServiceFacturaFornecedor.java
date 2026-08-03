package jminvsm.service.compra;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.FacturaFornecedorDAO;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.service.ServiceException;

public class ServiceFacturaFornecedor {

    private final FacturaFornecedorDAO facturaFornecedorDao;
    private boolean opsSuccess;

    public ServiceFacturaFornecedor() throws SQLException {
        this.facturaFornecedorDao = new FacturaFornecedorDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean criarDeRecebimento(String numeroFactura, int idRecebimento, String dataEmissao,
            String dataVencimento, int idUsuario) {
        setOpsSuccess(false);
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new ServiceException("Numero da factura do fornecedor nao informado!");
        }
        if (idRecebimento <= 0) {
            throw new ServiceException("Recebimento nao selecionado!");
        }
        if (dataEmissao == null || dataEmissao.trim().isEmpty()) {
            throw new ServiceException("Data de emissao da factura nao informada!");
        }
        if (idUsuario <= 0) {
            throw new ServiceException("Utilizador nao informado!");
        }
        if (facturaFornecedorDao.criarDeRecebimento(numeroFactura, idRecebimento, dataEmissao, dataVencimento, idUsuario)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean criarDeRecebimento(RecebimentoCompra recebimento, String numeroFactura,
            String dataEmissao, String dataVencimento, int idUsuario) {
        if (recebimento == null || recebimento.getId() <= 0) {
            throw new ServiceException("Recebimento nao selecionado!");
        }
        return criarDeRecebimento(numeroFactura, recebimento.getId(), dataEmissao, dataVencimento, idUsuario);
    }

    public boolean cancelarFactura(int id) {
        setOpsSuccess(false);
        if (id <= 0) {
            throw new ServiceException("Factura do fornecedor nao selecionada!");
        }
        if (facturaFornecedorDao.updateEstado(id, "Cancelada")) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public ObservableList<FacturaFornecedor> listaFacturas() {
        return facturaFornecedorDao.listAllEntities();
    }

    public ObservableList<FacturaFornecedor> listaFacturasPorEstado(String estado) {
        return facturaFornecedorDao.getEntitiesByEstado(estado);
    }

    public FacturaFornecedor consultaFacturaByID(int id) {
        return facturaFornecedorDao.getEntityByID(id);
    }

    public ObservableList<ItemFacturaFornecedor> consultaItens(int idFacturaFornecedor) {
        return facturaFornecedorDao.getItens(idFacturaFornecedor);
    }
}
