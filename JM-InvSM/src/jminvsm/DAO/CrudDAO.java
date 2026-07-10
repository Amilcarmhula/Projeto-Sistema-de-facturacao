package jminvsm.dao;

public interface CrudDAO<T> extends AddDAO<T>, UpdateByIdDAO<T>, ListDAO<T>, FindByIdDAO<T>, DeleteDAO {
}
