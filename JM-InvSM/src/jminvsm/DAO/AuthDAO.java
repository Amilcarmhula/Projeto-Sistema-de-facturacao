package jminvsm.dao;

public interface AuthDAO<T> {

    T login(String username, String password);
}
