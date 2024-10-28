/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.stock;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface StockDAOImpl<T> {
    ObservableList<T> getStock(Integer cat, Integer arm);
    ObservableList<T> loadLowStock();
    int countStock(Integer cat, Integer arm);
}
