/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.empresa;

import java.util.List;

/**
 *
 * @author JM-Tecnologias
 */
public interface EmpresaDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t, int id);
    T getEmpresaData();

}
