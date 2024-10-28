/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.fornecedor.endereco;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface EnderecoFornecedorDAOImpl<T> {

    void addEntity(T t);
    void updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    ObservableList<T> searchEntityByCLiente(T t);
    void deleteEntity(T x);
}
