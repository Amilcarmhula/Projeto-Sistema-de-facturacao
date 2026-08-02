package jminvsm.dao.requisicao;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;

public interface RequisicaoDAOImpl extends CrudDAO<Requisicao> {

    boolean addEntityWithItems(Requisicao requisicao);

    boolean updateEstado(int id, String estado);

    boolean aprovarRequisicao(int id);

    boolean cancelarRequisicao(int id);

    boolean confirmarPicking(int idRequisicao);

    ObservableList<Requisicao> getEntitiesByEstado(String estado);

    ObservableList<ItemRequisicao> getItens(int idRequisicao);
}
