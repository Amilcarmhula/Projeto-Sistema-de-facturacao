/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.categoria;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.ListDAO;
import jminvsm.dao.UpdateByIdDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface CategoriaDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>, ListDAO<T>, DeleteDAO {

    boolean updateEntityByID(T entity);

    @Override
    default boolean updateEntityByID(T entity, int id) {
        return updateEntityByID(entity);
    }
}
