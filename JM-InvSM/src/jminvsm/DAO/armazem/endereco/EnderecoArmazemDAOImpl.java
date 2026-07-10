/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.armazem.endereco;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.ListDAO;
import jminvsm.dao.SearchByOwnerDAO;
import jminvsm.dao.UpdateByIdDAO;

/**
 *
 * @author JM-Tecnologias
 */
public interface EnderecoArmazemDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>,
        ListDAO<T>, SearchByOwnerDAO<T>, DeleteDAO {
}
