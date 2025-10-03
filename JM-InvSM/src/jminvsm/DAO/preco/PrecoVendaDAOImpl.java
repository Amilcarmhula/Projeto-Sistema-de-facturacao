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
    
    ObservableList<T> getEntityByID(int x, int w);
    boolean deleteEntityByUSER(int x);

}