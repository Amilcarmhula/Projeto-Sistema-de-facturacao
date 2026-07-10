package jminvsm.dao;

public interface VoidAddUpdateDAO<T> {

    void addEntity(T entity);

    void updateEntityByID(T entity, int id);
}
