/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.preco;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface PrecoVendaDAOImpl<T> {
    boolean  addEntity(T x);
    boolean updateEntityByID(T x, int id);
    
    ObservableList<T> getEntityByProdutoID(int x);
    boolean deleteEntityByUSER(int x);

}