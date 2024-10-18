/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.fornecedor;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface FornecedorDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllEntitiesTypeFornecedor();
    T getLastEntity();
    T getEntityByID(int id);
//    void deleteEntityByROOT(int id);
    void deleteEntity(int x);
    int CountFornecedor();
}
