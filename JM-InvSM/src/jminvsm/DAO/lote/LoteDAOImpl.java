package jminvsm.dao.lote;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.dao.LastEntityDAO;
import jminvsm.model.lote.Lote;

public interface LoteDAOImpl extends CrudDAO<Lote>, LastEntityDAO<Lote> {

    ObservableList<Lote> getLotesDisponiveis(Integer idProduto, Integer idArmazem);

    boolean updateQuantidadeAtual(int idLote, int quantidadeAtual);
}
