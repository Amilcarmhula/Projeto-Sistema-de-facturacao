package jminvsm.dao;

public interface UpdateByIdDAO<T> {

    boolean updateEntityByID(T entity, int id);
}
