/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.desconto;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.ListDAO;
import jminvsm.dao.UpdateByIdDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 * @param <T>
 */
public interface DescontoDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>, ListDAO<T>, DeleteDAO {

    boolean updateEntity(T entity, int id);

    @Override
    default boolean updateEntityByID(T entity, int id) {
        return updateEntity(entity, id);
    }
}
