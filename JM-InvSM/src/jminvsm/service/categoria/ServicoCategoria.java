/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.categoria;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.categoria.CategoriaDAO;
import jminvsm.model.categoria.Categoria;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicoCategoria {

    private final CategoriaDAO categoriaDAO;
    private boolean opsSuccess;

    public ServicoCategoria() throws SQLException {
        this.categoriaDAO = new CategoriaDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String grupo, String subgrupo,
            String familia, String descricao) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (grupo == null || grupo.isEmpty()) {
            throw new ServiceException("Grupo nao pode ser nulo!");
        }
        if (subgrupo == null || subgrupo.isEmpty()) {
            throw new ServiceException("Subgrupo nao pode ser nulo!");
        }
        if (familia == null || familia.isEmpty()) {
            throw new ServiceException("Familia nao pode ser nula!");
        }
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Categoria sem padrao.";
        }

        Categoria c = new Categoria();
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);

        if (categoriaDAO.addEntity(c)) {
            setOpsSuccess(true);
        }
    }

    public void actualizar(int id, String grupo, String subgrupo,
            String familia, String descricao) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (grupo == null || grupo.isEmpty()) {
            throw new ServiceException("Grupo nao pode ser nulo!");
        }
        if (subgrupo == null || subgrupo.isEmpty()) {
            throw new ServiceException("Subgrupo nao pode ser nulo!");
        }
        if (familia == null || familia.isEmpty()) {
            throw new ServiceException("Familia nao pode ser nula!");
        }
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Categoria sem padrao.";
        }
        Categoria c = new Categoria();
        c.setId(id);
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);

        if (categoriaDAO.updateEntityByID(c)) {
            setOpsSuccess(true);
        }
    }
    

    

    public ObservableList<Categoria> listaCategorias() {
        return categoriaDAO.listAllEntities();
    }
    
    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (categoriaDAO.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }
}
