/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.cliente;

import jminvsm.dao.AddDAO;
import jminvsm.dao.CountDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.FindByIdDAO;
import jminvsm.dao.LastEntityDAO;
import jminvsm.dao.UpdateByIdDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ClienteDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>, FindByIdDAO<T>,
        LastEntityDAO<T>, DeleteDAO, CountDAO {

    ObservableList<T> listAllEntitiesToClienteView();
    ObservableList<T> listClientesForSearch();
    T getFullClieneByID(int id);
    int CountClientes();

    default int count() {
        return CountClientes();
    }
}
