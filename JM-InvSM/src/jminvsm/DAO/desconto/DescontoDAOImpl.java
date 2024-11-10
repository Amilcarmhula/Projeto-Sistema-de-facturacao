/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.desconto;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 * @param <T>
 */
public interface DescontoDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntity(T t, int x);
    ObservableList<T> listAllEntities();
    boolean deleteEntity(int x);
}
