/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.empresa.util;

import java.util.List;

/**
 *
 * @author JM-Tecnologias
 */
public interface DaoImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t, int id);
    List<T> listAllEntities();
    T searchEntityByName(String name);
    void deleteEntityByROOT(int id);
    void deleteEntityByUSER(int id);

//    List<T> searchListEntityByName(String name);

}
