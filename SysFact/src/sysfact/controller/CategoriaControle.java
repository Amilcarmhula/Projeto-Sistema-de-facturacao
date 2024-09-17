/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.controller;

import javafx.collections.ObservableList;
import sysfact.model.categoria.Categoria;
import sysfact.service.caregoria.ServicoCategoria;

/**
 *
 * @author JM-Tecnologias
 */
public class CategoriaControle {

    private ServicoCategoria servicocategoria;

    public CategoriaControle(ServicoCategoria servicocategoria) {
        this.servicocategoria = servicocategoria;
    }
    
    public ObservableList<Categoria> listaTodos(){
        return servicocategoria.consultaCategorias();
    }
    
    public void alteraCategoria(int id, String tipo, String grupo, String subgrupo,
            String familia, String descricao) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setTipo(tipo);
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);
        servicocategoria.actualizarCaregoria(c);
    }
}


