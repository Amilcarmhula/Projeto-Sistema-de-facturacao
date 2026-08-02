package jminvsm.dao.movimento;

import javafx.collections.ObservableList;
import jminvsm.dao.AddDAO;
import jminvsm.model.movimento.MovimentoLote;

public interface MovimentoLoteDAOImpl extends AddDAO<MovimentoLote> {

    ObservableList<MovimentoLote> getMovimentoLotesByMovimento(int idMovimento);
}
