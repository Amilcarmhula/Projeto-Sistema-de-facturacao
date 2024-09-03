/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.agente.endereco;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface EnderecoAgenteDAOImpl<T> {

    void addEntity(T t);

    void updateEntityByID(T t);

    ObservableList<T> listAllEntities();

    ObservableList<T> searchEntityByAgente(T t);
//    T searchEntityByName(String name);
//    void deleteEntityByROOT(int id);

    void deleteEntityByUSER(T x);
//    ObservableList<T> searchListEntityByName(String name);
}
