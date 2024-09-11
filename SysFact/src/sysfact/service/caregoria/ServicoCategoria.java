/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.caregoria;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import sysfact.dao.categoria.CategoriaDAO;
import sysfact.model.categoria.Categoria;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicoCategoria {
    private CategoriaDAO categoriaDAO;

    public ServicoCategoria(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }
    
    public void registaCaregoria(Categoria c){
        categoriaDAO.addEntity(c);
    }
    
    public void actualizarCaregoria(Categoria c){
        categoriaDAO.updateEntityByID(c);
    }
    
    public ObservableList<Categoria> consultaCategorias(){
        return categoriaDAO.listAllEntities();
    }
}
