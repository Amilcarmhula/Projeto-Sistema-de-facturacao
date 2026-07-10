/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.fornecedor.contacto;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.ListDAO;
import jminvsm.dao.SearchBySupplierDAO;
import jminvsm.dao.UpdateByIdDAO;

/**
 *
 * @author JM-Tecnologias
 */
public interface ContactoFornecedorDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>,
        ListDAO<T>, SearchBySupplierDAO<T>, DeleteDAO {
}
