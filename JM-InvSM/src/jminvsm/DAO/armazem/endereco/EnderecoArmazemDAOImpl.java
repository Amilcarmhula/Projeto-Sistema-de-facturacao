/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.armazem.endereco;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface EnderecoArmazemDAOImpl<T> {

    boolean addEntity(T t);
    boolean updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> searchEntityByCLiente(int t);
    boolean deleteEntity(int x);
}
