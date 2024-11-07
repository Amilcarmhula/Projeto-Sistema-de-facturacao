/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.taxaimposto;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface TaxaImpostoDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    T getEntityByID(int id);
    boolean deleteEntity(int x);
}
