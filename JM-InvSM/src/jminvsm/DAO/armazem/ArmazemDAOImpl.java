/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.armazem;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ArmazemDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllEntitiesTypeArmazem();
    T getLastEntity();
    T getEntityByID(int id);
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
}
