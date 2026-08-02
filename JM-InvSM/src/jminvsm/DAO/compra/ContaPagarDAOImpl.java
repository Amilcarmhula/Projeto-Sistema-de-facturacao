package jminvsm.dao.compra;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.compra.ContaPagar;

public interface ContaPagarDAOImpl extends CrudDAO<ContaPagar> {

    boolean updateEstado(int id, String estado);

    ObservableList<ContaPagar> getEntitiesByEstado(String estado);

    ObservableList<ContaPagar> getEntitiesByFornecedor(int idFornecedor);
}
