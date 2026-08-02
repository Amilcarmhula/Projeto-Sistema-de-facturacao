package jminvsm.dao.compra;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.RecebimentoCompra;

public interface RecebimentoCompraDAOImpl extends CrudDAO<RecebimentoCompra> {

    boolean addEntityWithItems(RecebimentoCompra recebimentoCompra);

    boolean confirmarRecebimento(int idRecebimento);

    boolean updateEstado(int id, String estado);

    ObservableList<RecebimentoCompra> getEntitiesByEstado(String estado);

    ObservableList<ItemRecebimentoCompra> getItens(int idRecebimentoCompra);
}
