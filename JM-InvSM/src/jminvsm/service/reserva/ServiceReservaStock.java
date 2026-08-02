package jminvsm.service.reserva;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.reserva.ReservaStockDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.reserva.ReservaStock;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class ServiceReservaStock {

    private final ReservaStockDAO reservaStockDao;
    private boolean opsSuccess;

    public ServiceReservaStock() throws SQLException {
        this.reservaStockDao = new ReservaStockDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registaReserva(Integer idProduto, Integer idArmazem, Integer idRequisicao, Integer quantidade, String estado) {
        setOpsSuccess(false);
        if (idProduto == null || idProduto <= 0 || idArmazem == null || idArmazem <= 0
                || idRequisicao == null || idRequisicao <= 0 || quantidade == null || quantidade <= 0) {
            showErroAlert("Dados da reserva invalidos!");
            return;
        }

        Produto produto = new Produto();
        produto.setId(idProduto);

        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);

        Requisicao requisicao = new Requisicao();
        requisicao.setId(idRequisicao);

        ReservaStock reserva = new ReservaStock();
        reserva.setProduto(produto);
        reserva.setArmazem(armazem);
        reserva.setRequisicao(requisicao);
        reserva.setQuantidade(quantidade);
        reserva.setEstado(estado == null || estado.trim().isEmpty() ? "Reservado" : estado);

        if (reservaStockDao.addEntity(reserva)) {
            setOpsSuccess(true);
            showSuccessAlert("Reserva registada com sucesso!");
        }
    }

    public ObservableList<ReservaStock> consultaReservasRequisicao(int idRequisicao) {
        return reservaStockDao.getReservasByRequisicao(idRequisicao);
    }

    public ObservableList<ReservaStock> consultaReservasAtivas(Integer idProduto, Integer idArmazem) {
        return reservaStockDao.getReservasAtivas(idProduto, idArmazem);
    }

    public ReservaStock consultaReservaByID(int id) {
        return reservaStockDao.getEntityByID(id);
    }

    public boolean alteraEstadoReservasRequisicao(int idRequisicao, String estado) {
        setOpsSuccess(false);
        if (idRequisicao <= 0 || estado == null || estado.trim().isEmpty()) {
            showErroAlert("Dados para alterar reservas invalidos!");
            return false;
        }
        if (reservaStockDao.updateEstadoByRequisicao(idRequisicao, estado)) {
            setOpsSuccess(true);
            showSuccessAlert("Reservas atualizadas com sucesso!");
        }
        return isOpsSuccess();
    }
}
