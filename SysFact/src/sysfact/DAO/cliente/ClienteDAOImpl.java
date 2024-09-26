/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.cliente;

import javafx.collections.ObservableList;
import sysfact.model.cliente.Cliente;

/**
 *
 * @author JM-Tecnologias
 */
public interface ClienteDAOImpl<T> {

    void addEntity(T t);

    void updateEntityByID(T t, int id);

    T getLastEntity();

    T getEntityByID(int id);

    ObservableList<T> listAllEntities();
    ObservableList<T> listAllForSearch();
    T getClieneByID(int id);
//     getClinteFacturaData(String x);

//    ObservableList<T> listAllEntitiesTypeFornecedor();
//    void deleteEntityByROOT(int id);
    void deleteEntity(int x);
}
