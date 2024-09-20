/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.produto.fornecido;

import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface ProdutoFornecidoDAOImpl<T> {
    void  addEntity(T x);
    void updateEntityByID(T t, int id);
    
    
    ObservableList<T> listAllEntities();
    T searchEntityByName(String name);
    void deleteEntityByUSER(T x);


}
