/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.produto.fornecido;


import java.util.Map;
import jminvsm.dao.AddDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 * @param <T>
 */
public interface ProdutoFornecidoDAOImpl<T> extends AddDAO<T> {

    Map<String, Integer> getMovimentos(Integer cat, Integer arm);
    ObservableList<T> getLotesForStock(Integer idProduto);
}
