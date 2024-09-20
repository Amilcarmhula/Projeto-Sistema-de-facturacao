/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.produto;

import javafx.scene.control.Alert;
import sysfact.dao.produto.ProdutoDAO;
import sysfact.model.categoria.Categoria;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;

/**
 *
 * @author JM-Tecnologias
 */
public class Serviceproduto {
    private ProdutoDAO produtoDao;

    public Serviceproduto(ProdutoDAO produtoDao) {
        this.produtoDao = produtoDao;
    }
    
    public void registraProduto(Integer id_cat, Integer id_uni, Integer id_tax,
            String nome,String codBarras, String descricao, Boolean  controleStock){
        
        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            showErrorDialog("Erro de validação", "Categoria não definida.");
            return;
        }
        if (id_uni == null || id_uni <= 0) {
            showErrorDialog("Erro de validação", "Unidade de medida não definida.");
            return;
        }
        
        if (id_tax == null || id_tax <= 0) {
            showErrorDialog("Erro de validação", "Taxa/Imposto não definida.");
            return;
        }
        
        if (nome == null || nome.isEmpty()) {
            showErrorDialog("Erro de validação", "Nome do produto não definida.");
            return;
        }
        
        if (codBarras == null || codBarras.isEmpty()) {
            showErrorDialog("Erro de validação", "Sera definido Codigo de Barras padrao!");
            codBarras = "1000000000001";
        }

        
        
        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo_barra(codBarras);
        p.setContole_stock(controleStock);
        p.setDescricao(descricao); 
        Categoria c = new Categoria();
        c.setId(id_cat);
        p.setCategoria(c);
        UnidadeMedida u = new UnidadeMedida();
        u.setId(id_uni);
        p.setUnidade_medida(u);
        TaxaImposto t = new TaxaImposto();
        t.setId(id_tax);
        p.setTaxa_imposto(t);
        
        produtoDao.addEntity(p);
    }
    
    public void actualizaProduto(int id, Integer id_cat, Integer id_uni, Integer id_tax,
            String nome,String codBarras, String descricao){
        
        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            showErrorDialog("Erro de validação", "Categoria não definida.");
            return;
        }
        if (id_uni == null || id_uni <= 0) {
            showErrorDialog("Erro de validação", "Unidade de medida não definida.");
            return;
        }
        
        if (id_tax == null || id_tax <= 0) {
            showErrorDialog("Erro de validação", "Taxa/Imposto não definida.");
            return;
        }
        
        if (nome == null || nome.isEmpty()) {
            showErrorDialog("Erro de validação", "Nome do produto não definida.");
            return;
        }
        
        if (codBarras == null || codBarras.isEmpty()) {
            showErrorDialog("Erro de validação", "Sera definido Codigo de Barras padrao!");
            codBarras = "1000000000001";
        }

        
        
        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo_barra(codBarras);
        p.setDescricao(descricao);
        Categoria c = new Categoria();
        c.setId(id_cat);
        p.setCategoria(c);
        UnidadeMedida u = new UnidadeMedida();
        u.setId(id_uni);
        p.setUnidade_medida(u);
        TaxaImposto t = new TaxaImposto();
        t.setId(id_tax);
        p.setTaxa_imposto(t);
        
        produtoDao.updateEntityByID(p, id);
    }
    
    
    
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
