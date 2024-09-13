/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.armazem;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ArmazemDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllEntitiesTypeArmazem();
    T getLastEntity();
    T getEntityByID(int id);
//    void deleteEntityByROOT(int id);
    void deleteEntity(int x);
}
