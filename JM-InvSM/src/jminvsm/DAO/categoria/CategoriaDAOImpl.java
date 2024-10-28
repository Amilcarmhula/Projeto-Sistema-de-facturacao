/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.categoria;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface CategoriaDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t);
    ObservableList<T> listAllEntities();
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
}
