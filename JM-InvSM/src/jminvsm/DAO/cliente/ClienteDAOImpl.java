/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.cliente;

import javafx.collections.ObservableList;
import jminvsm.model.cliente.Cliente;

/**
 *
 * @author JM-Tecnologias
 */
public interface ClienteDAOImpl<T> {

    boolean addEntity(T t);

    boolean updateEntityByID(T t, int id);

    T getLastEntity();

    T getEntityByID(int id);

    ObservableList<T> listAllEntitiesToClienteView();
    ObservableList<T> listClientesForSearch();
    T getFullClieneByID(int id);
//     getClinteFacturaData(String x);

//    ObservableList<T> listAllEntitiesTypeFornecedor();
//    void deleteEntityByROOT(int id);
    boolean deleteEntity(int x);
    int CountClientes();
}
