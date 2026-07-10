package jminvsm.dao;

import javafx.collections.ObservableList;

public interface ListDAO<T> {

    ObservableList<T> listAllEntities();
}
