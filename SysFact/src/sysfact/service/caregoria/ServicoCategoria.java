/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.caregoria;

import javafx.collections.ObservableList;
import sysfact.dao.categoria.CategoriaDAO;
import sysfact.model.categoria.Categoria;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicoCategoria {

    private CategoriaDAO categoriaDAO;

    public ServicoCategoria(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

   

    public void registaCategoria(String tipo, String grupo, String subgrupo,
                                 String familia, String descricao) {
        // Validações básicas antes de registrar a categoria
        if (tipo == null || tipo.isEmpty()) {
            showErrorDialog("Erro de validação", "O tipo não pode ser nulo ou vazio.");
            return;
        }
        if (grupo == null || grupo.isEmpty()) {
            throw new IllegalArgumentException("O grupo não pode ser nulo ou vazio.");
        }
        // Continue validando outros campos conforme necessário

        Categoria c = new Categoria();
        c.setTipo(tipo);
        c.setGrupo(grupo);
        c.setSubgrupo(subgrupo);
        c.setFamilia(familia);
        c.setDescricao(descricao);

        try {
            categoriaDAO.addEntity(c);
             showInfoDialog("Sucesso", "Categoria registrada com sucesso!");
        } catch (Exception e) {
            showErrorDialog("Erro ao registrar categoria", "Detalhes: " + e.getMessage());
        }
    }

    public void actualizarCaregoria(Categoria c) {
        categoriaDAO.updateEntityByID(c);
    }

    public ObservableList<Categoria> consultaCategorias() {
        return categoriaDAO.listAllEntities();
    }
    
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
