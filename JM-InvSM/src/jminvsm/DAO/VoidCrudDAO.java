package jminvsm.dao;

import java.util.List;

public interface VoidCrudDAO<T> extends SearchByNameDAO<T> {

    void addEntity(T entity);

    void updateEntityByID(T entity, int id);

    List<T> listAllEntities();

    void deleteEntityByROOT(int id);

    void deleteEntityByUSER(int id);
}
