/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.preco;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface PrecoVendaDAOImpl<T> {
    void  addEntity(T x);
    void updateEntityByID(T x);
     T getEntityByProdutoID(T x);
    void deleteEntityByUSER(T x);

}