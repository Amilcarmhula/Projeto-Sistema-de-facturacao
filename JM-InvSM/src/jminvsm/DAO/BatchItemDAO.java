package jminvsm.dao;

import java.util.List;

public interface BatchItemDAO<T> {

    void addEntity(List<T> entities);

    void addEntityQuotation(List<T> entities);

    List<T> getItensData(String reference);
}
