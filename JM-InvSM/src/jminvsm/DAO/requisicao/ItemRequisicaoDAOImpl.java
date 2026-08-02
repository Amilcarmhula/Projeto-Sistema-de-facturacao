package jminvsm.dao.requisicao;

import javafx.collections.ObservableList;
import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.dao.UpdateByIdDAO;
import jminvsm.model.requisicao.ItemRequisicao;

public interface ItemRequisicaoDAOImpl extends AddDAO<ItemRequisicao>,
        UpdateByIdDAO<ItemRequisicao>, FindByIdDAO<ItemRequisicao>, DeleteDAO {

    ObservableList<ItemRequisicao> getItensByRequisicao(int idRequisicao);
}
