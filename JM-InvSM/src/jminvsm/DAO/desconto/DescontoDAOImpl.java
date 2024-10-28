/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.desconto;

import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface DescontoDAOImpl<T> {
    
//    Map<String,Double> mapDesconto();
    
    
//    void  addEntity(T t);
//    void updateEntityByID(T t, int x);
    ObservableList<T> listAllEntities();
//    
//    T getEntityByID(int x);
//    void deleteEntity(int x);
}
