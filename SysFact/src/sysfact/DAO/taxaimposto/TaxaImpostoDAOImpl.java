/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.taxaimposto;

import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface TaxaImpostoDAOImpl<T> {
    void  addEntity(T t);
    void updateEntityByID(T t, int id);
    ObservableList<T> listAllEntities();
    T getEntityByID(int id);
    Map<String,Integer> mapTaxaImposto();
    void deleteEntity(int x);
}
