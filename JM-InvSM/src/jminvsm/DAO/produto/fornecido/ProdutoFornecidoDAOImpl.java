/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.produto.fornecido;


import java.util.Map;

/**
 *
 * @author JM-Tecnologias
 * @param <T>
 */
public interface ProdutoFornecidoDAOImpl<T> {
    boolean  addEntity(T x);
    Map<String, Integer> getMovimentos(Integer cat, Integer arm);
//    boolean updateEntityByID(T t, int id);
    
    
//    ObservableList<T> listAllEntities();
//    T searchEntityByName(String name);
//    void deleteEntityByUSER(T x);


}
