package jminvsm.dao.precoProdutoArmazem;

import jminvsm.dao.AddDAO;
import javafx.collections.ObservableList;

public interface PrecoProdutoArmazemDAOImpl<T> extends AddDAO<T> {

    ObservableList<T> getEntityByID(int idProduto, int idArmazem);

    ObservableList<T> listAllPrecos(int idProduto);

    boolean deleteEntityByUSER(int id);
}
