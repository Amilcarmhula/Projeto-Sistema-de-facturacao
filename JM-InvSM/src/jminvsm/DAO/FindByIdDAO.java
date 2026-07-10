package jminvsm.dao;

public interface FindByIdDAO<T> {

    T getEntityByID(int id);
}
