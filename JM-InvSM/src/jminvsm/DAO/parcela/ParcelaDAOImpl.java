/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.parcela;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ParcelaDAOImpl<T> {
    boolean  addEntity(T t);
    ObservableList<T> getEntities(String fk_numero);
}
