/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.categoria;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import jminvsm.dao.categoria.CategoriaDAO;
import jminvsm.model.categoria.Categoria;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert( "Grupo nao pode ser nulo!");
            return;
        }
        if (subgrupo == null || subgrupo.isEmpty()) {
            showErroAlert( "Subgrupo nao pode ser nulo!");
            return;
        }
        if (familia == null || familia.isEmpty()) {
            showErroAlert( "Familia nao pode ser nula!");
            return;
        }
        if (descricao == null || descricao.isEmpty()) {
            showErroAlert( "Descricao nao pode ser nula. Sere criada uma descricao padrao.");
            descricao = "Categoria sem padrao.";
        }

        Categoria c = new Categoria();
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);

        if (categoriaDAO.addEntity(c)) {
            setOpsSuccess(true);
            showSuccessAlert( "Categoria registada com sucesso!");
        }
    }

    public void actualizar(int id, String grupo, String subgrupo,
            String familia, String descricao) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (grupo == null || grupo.isEmpty()) {
            showErroAlert( "Grupo nao pode ser nulo!");
            return;
        }
        if (subgrupo == null || subgrupo.isEmpty()) {
            showErroAlert( "Subgrupo nao pode ser nulo!");
            return;
        }
        if (familia == null || familia.isEmpty()) {
            showErroAlert( "Familia nao pode ser nula!");
            return;
        }
        if (descricao == null || descricao.isEmpty()) {
            showErroAlert( "Descricao nao pode ser nula. Sere criada uma descricao padrao.");
            descricao = "Categoria sem padrao.";
        }
        Categoria c = new Categoria();
        c.setId(id);
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);

        if (categoriaDAO.updateEntityByID(c)) {
            showSuccessAlert( "Categoria actualizada com sucesso!");
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
            showSuccessAlert( "Registo excluido com sucesso!");
        }
    }
}
