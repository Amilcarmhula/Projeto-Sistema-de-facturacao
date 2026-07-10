package jminvsm.dao;

public interface SearchByNameDAO<T> {

    T searchEntityByName(String name);
}
