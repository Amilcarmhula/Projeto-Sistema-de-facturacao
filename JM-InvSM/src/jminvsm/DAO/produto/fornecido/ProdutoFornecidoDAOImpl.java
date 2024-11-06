/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.produto.fornecido;


import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ProdutoFornecidoDAOImpl<T> {
    boolean  addEntity(T x);
    boolean updateEntityByID(T t, int id);
    
    
    ObservableList<T> listAllEntities();
    T searchEntityByName(String name);
    void deleteEntityByUSER(T x);


}
