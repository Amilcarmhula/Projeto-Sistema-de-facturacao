/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.unidademedida;

import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface UnidadeMedidaDAOImpl<T> {
    boolean  addEntity(T t);
    boolean updateEntityByID(T t, int x);
    ObservableList<T> listAllEntities();
    T getEntityByID(int x);
    boolean deleteEntity(int x);
}
