/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.parcela;

import jminvsm.dao.AddDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ParcelaDAOImpl<T> extends AddDAO<T> {

    ObservableList<T> getEntities(String fk_numero);
}
