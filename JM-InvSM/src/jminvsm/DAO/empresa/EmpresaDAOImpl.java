/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.empresa;

import jminvsm.dao.VoidAddUpdateDAO;

/**
 *
 * @author JM-Tecnologias
 */
public interface EmpresaDAOImpl<T> extends VoidAddUpdateDAO<T> {

    T getEmpresaData();

}
