/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.fornecedor;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface FornecedorDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> listAllFornecedores();
    T getLastEntity();
    T getEntityByID(int id);
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
    int CountFornecedor();
}
