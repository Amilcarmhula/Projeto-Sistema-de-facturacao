package jminvsm.service.picking;

import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;
import jminvsm.dao.picking.PickingDAO;
import jminvsm.model.picking.Picking;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

public class ServicePicking {

    private final PickingDAO pickingDao;
    private boolean opsSuccess;

    public ServicePicking() throws SQLException {
        this.pickingDao = new PickingDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void iniciaPickingRequisicao(Integer idRequisicao, Usuario operador) {
        setOpsSuccess(false);
        if (idRequisicao == null || idRequisicao <= 0) {
            throw new ServiceException("Requisicao nao selecionada!");
        }

        Requisicao requisicao = new Requisicao();
        requisicao.setId(idRequisicao);

        Picking picking = new Picking();
        picking.setTipo("Requisicao");
        picking.setEstado("Separando");
        picking.setOperador(operador);
        picking.setDataInicio(LocalDateTime.now());
        picking.setRequisicao(requisicao);

        if (pickingDao.addEntity(picking)) {
            setOpsSuccess(true);
        }
    }

    public boolean atualizaPicking(Picking picking, int idPicking) {
        setOpsSuccess(false);
        if (picking == null || idPicking <= 0) {
            throw new ServiceException("Picking nao selecionado!");
        }
        if (pickingDao.updateEntityByID(picking, idPicking)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean alteraEstadoPickingRequisicao(int idRequisicao, String estado) {
        setOpsSuccess(false);
        if (idRequisicao <= 0 || estado == null || estado.trim().isEmpty()) {
            throw new ServiceException("Dados para alterar picking invalidos!");
        }
        if (pickingDao.updateEstadoByRequisicao(idRequisicao, estado)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public Picking consultaPickingByID(int id) {
        return pickingDao.getEntityByID(id);
    }

    public Picking consultaPickingRequisicao(int idRequisicao) {
        return pickingDao.getPickingByRequisicao(idRequisicao);
    }

    public ObservableList<Picking> consultaPickingsPorEstado(String estado) {
        return pickingDao.getPickingsByEstado(estado);
    }
}
