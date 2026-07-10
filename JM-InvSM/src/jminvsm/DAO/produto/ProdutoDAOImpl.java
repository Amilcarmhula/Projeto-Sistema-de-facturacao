/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.produto;

import jminvsm.dao.AddDAO;
import jminvsm.dao.DeleteDAO;
import jminvsm.dao.LastEntityDAO;
import jminvsm.dao.UpdateByIdDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ProdutoDAOImpl<T> extends AddDAO<T>, UpdateByIdDAO<T>, LastEntityDAO<T>, DeleteDAO {

    ObservableList<T> getCombinedEntities();
    ObservableList<T> listAllProducts();
    int countProdutos(Integer cat);
}
