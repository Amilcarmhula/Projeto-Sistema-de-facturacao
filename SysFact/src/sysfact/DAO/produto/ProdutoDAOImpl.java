/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.produto;

import javafx.collections.ObservableList;
import sysfact.model.produto.Produto;

/**
 *
 * @author JM-Tecnologias
 */
public interface ProdutoDAOImpl<T> {
    ObservableList<T> getCombinedEntities();
    
    void  addEntity(T t);
    void updateEntityByID(T t);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllEntities2();
    ObservableList<T> searchEntityByCategoria(T x);
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);
    void deleteEntityByUSER(T x);
}
