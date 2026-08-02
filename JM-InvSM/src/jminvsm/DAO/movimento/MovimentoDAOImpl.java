package jminvsm.dao.movimento;

import javafx.collections.ObservableList;
import jminvsm.dao.AddDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.model.movimento.Movimento;

public interface MovimentoDAOImpl extends AddDAO<Movimento>, FindByIdDAO<Movimento> {

    ObservableList<Movimento> getMovimentosByRequisicao(int idRequisicao);

    ObservableList<Movimento> getMovimentosByProduto(Integer idProduto, Integer idArmazem);
}
