package jminvsm.dao;

import javafx.collections.ObservableList;

public interface SearchBySupplierDAO<T> {

    ObservableList<T> searchEntityByFornecedor(int fornecedorId);
}
