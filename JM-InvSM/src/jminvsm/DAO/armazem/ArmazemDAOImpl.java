/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.armazem;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.dao.LastEntityDAO;
import jminvsm.dao.ListDAO;
import jminvsm.dao.UpdateByIdDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ArmazemDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>, ListDAO<T>,
        FindByIdDAO<T>, LastEntityDAO<T>, DeleteDAO {

    ObservableList<T> listAllArmazens();
}
