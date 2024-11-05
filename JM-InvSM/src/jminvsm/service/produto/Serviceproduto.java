/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.produto;

import java.sql.SQLException;
import jminvsm.dao.produto.ProdutoDAO;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.produto.Produto;
import jminvsm.model.taxa_imposto.TaxaImposto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import static jminvsm.util.AlertUtilities.showDialog;
import static jminvsm.util.AlertUtilities.showErroNotification;
import static jminvsm.util.AlertUtilities.showSuccessNotification;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProduto {
    private ProdutoDAO produtoDao;

    public ServiceProduto() throws SQLException {
        this.produtoDao = new ProdutoDAO();
    }
    
    public void registraProduto(Integer id_cat, Integer id_uni, Integer id_tax,
            String nome,String codBarras, String descricao, Boolean  controleStock, Integer nivelstock){
        
        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            showErroNotification("Erro", "Categoria não definida!");
            return;
        }

        if (id_uni == null || id_uni <= 0) {
            showErroNotification("Erro", "Unidade de medida não definida!");
            return;
        }
        
        if (id_tax == null || id_tax <= 0) {
            showErroNotification("Erro", "Taxa/Imposto não definida!");
            return;
        }
        
        if (nome == null || nome.isEmpty()) {
            showErroNotification("Erro", "Nome do produto não pode ser null!");
            return;
        }
        
        if (codBarras == null || codBarras.isEmpty()) {
            showErroNotification("Erro", "Codigo de Barras nao definido. Sera definido codigo padrao");
            codBarras = "1000000000001";
        }
        
        if (controleStock == true && nivelstock < 0) {
            showErroNotification("Erro", "Nivel de stock minimo nao informado!");
            return;
        }

        
        
        Produto p = new Produto();
        p.setNomeProduto(nome);
        p.setCodigo_barra(codBarras);
        p.setContole_stock(controleStock);
        p.setNivelstock(nivelstock);
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
        
        
        try {
            produtoDao.addEntity(p);
            showSuccessNotification("Sucesso", "Produto registado com sucesso!");
        } catch (Exception e) {
            showDialog( "Erro", "Detalhes: " + e.getMessage());
        }
    }
    
    public void actualizaProduto(int id,Integer id_cat, Integer id_uni, Integer id_tax,
            String nome,String codBarras, String descricao, Boolean  controleStock, Integer nivelstock){
        
        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            showErroNotification("Erro", "Categoria não definida!");
            return;
        }

        if (id_uni == null || id_uni <= 0) {
            showErroNotification("Erro", "Unidade de medida não definida!");
            return;
        }
        
        if (id_tax == null || id_tax <= 0) {
            showErroNotification("Erro", "Taxa/Imposto não definida!");
            return;
        }
        
        if (nome == null || nome.isEmpty()) {
            showErroNotification("Erro", "Nome do produto não pode ser null!");
            return;
        }
        
        if (codBarras == null || codBarras.isEmpty()) {
            showErroNotification("Erro", "Codigo de Barras nao definido. Sera definido codigo padrao");
            codBarras = "1000000000001";
        }
        
        if (controleStock == true && nivelstock < 0) {
            showErroNotification("Erro", "Nivel de stock minimo nao informado!");
            return;
        }

        
        
        Produto p = new Produto();
        p.setNomeProduto(nome);
        p.setCodigo_barra(codBarras);
        p.setContole_stock(controleStock);
        p.setNivelstock(nivelstock);
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
        
        
        try {
            produtoDao.updateEntityByID(p, id);
            showSuccessNotification("Sucesso", "Produto actualizado com sucesso!");
        } catch (Exception e) {
            showDialog( "Erro", "Detalhes: " + e.getMessage());
        }
    }
    
    public Produto consultaUltimo(){
        return produtoDao.getLastEntity();
    }
    
    
}
