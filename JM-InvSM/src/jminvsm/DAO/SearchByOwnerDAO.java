package jminvsm.dao;

import javafx.collections.ObservableList;

public interface SearchByOwnerDAO<T> {

    ObservableList<T> searchEntityByCLiente(int ownerId);
}
