/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.fornecedor.contacto;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ContactoFornecedorDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t,int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> searchEntityByCLiente(int t) ;
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
//    ObservableList<T> searchListEntityByName(String name);
}