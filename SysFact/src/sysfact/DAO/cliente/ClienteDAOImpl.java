/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.cliente;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ClienteDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllEntitiesTypeFornecedor();
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    void deleteEntityByUSER(T x);
    void deleteEntityByUSERSimpleTwo(T x);
    void deleteEntityByUSERSimpleThree(T x);
    void deleteEntityByUSERSimpleFour(T x);
//    ObservableList<T> searchListEntityByName(String name);
}
