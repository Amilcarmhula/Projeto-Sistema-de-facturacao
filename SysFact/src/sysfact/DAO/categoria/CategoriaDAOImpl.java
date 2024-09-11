/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.categoria;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface CategoriaDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t);
    ObservableList<T> listAllEntities();
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    void deleteEntityByUSER(T x);
}
