package jminvsm.dao.reserva;

import javafx.collections.ObservableList;
import jminvsm.dao.AddDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.dao.UpdateByIdDAO;
import jminvsm.model.reserva.ReservaStock;

public interface ReservaStockDAOImpl extends AddDAO<ReservaStock>, UpdateByIdDAO<ReservaStock>, FindByIdDAO<ReservaStock> {

    ObservableList<ReservaStock> getReservasByRequisicao(int idRequisicao);

    ObservableList<ReservaStock> getReservasAtivas(Integer idProduto, Integer idArmazem);

    boolean updateEstadoByRequisicao(int idRequisicao, String estado);
}
