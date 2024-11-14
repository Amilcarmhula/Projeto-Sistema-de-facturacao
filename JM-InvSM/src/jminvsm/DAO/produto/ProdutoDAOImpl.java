/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.produto;

import javafx.collections.ObservableList;
import jminvsm.model.produto.Produto;

/**
 *
 * @author JM-Tecnologias
 */
public interface ProdutoDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t, int id);
    ObservableList<T> getCombinedEntities();
    T getLastEntity();
    int countProdutos(Integer cat);
    
    
    
//    ObservableList<T> listAllEntities();
//    ObservableList<T> listAllEntities2();
//    ObservableList<T> searchEntityByCategoria(T x);
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
}
