package jminvsm.dao.picking;

import javafx.collections.ObservableList;
import jminvsm.dao.AddDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.dao.UpdateByIdDAO;
import jminvsm.model.picking.Picking;

public interface PickingDAOImpl extends AddDAO<Picking>, UpdateByIdDAO<Picking>, FindByIdDAO<Picking> {

    Picking getPickingByRequisicao(int idRequisicao);

    ObservableList<Picking> getPickingsByEstado(String estado);

    boolean updateEstadoByRequisicao(int idRequisicao, String estado);
}
