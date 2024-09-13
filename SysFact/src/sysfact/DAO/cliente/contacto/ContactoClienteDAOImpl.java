/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.cliente.contacto;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ContactoClienteDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t,int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> searchEntityByCLiente(T t) ;
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    void deleteEntity(T x);
//    ObservableList<T> searchListEntityByName(String name);
}