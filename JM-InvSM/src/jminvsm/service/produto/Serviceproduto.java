/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.produto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.produto.ProdutoDAO;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.produto.Produto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProduto {

    private ProdutoDAO produtoDao;
    private boolean OpsSuccess = false;

    public ServiceProduto() throws SQLException {
        this.produtoDao = new ProdutoDAO();
    }

    public boolean isOpsSuccess() {
        return OpsSuccess;
    }

    public void setOpsSuccess(boolean OpsSuccess) {
        this.OpsSuccess = OpsSuccess;
    }

    public void regista(Integer id_cat, Integer id_uni, Integer id_tax,
            String nome, String codBarras, String descricao, String tipo, Integer unidadePorTipo,
            Boolean controleStock, Integer nivelstock) {
        setOpsSuccess(false);
        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            throw new ServiceException("Categoria nao definida!");
        }

        if (id_uni == null || id_uni <= 0) {
            throw new ServiceException("Unidade de medida nao definida!");
        }

        if (id_tax == null || id_tax <= 0) {
            throw new ServiceException("Taxa/Imposto nao definida!");
        }

        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome do produto nao pode ser nulo!");
        }

        if (codBarras == null || codBarras.isEmpty()) {
            codBarras = "1000000000001";
        }

        if (Boolean.TRUE.equals(controleStock) && (nivelstock == null || nivelstock < 0)) {
            throw new ServiceException("Nivel de stock minimo nao informado!");
        }

        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao Padrao!";
        }

        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo_barra(codBarras);
        p.setControle_stock(controleStock);
        p.setNivelstock(nivelstock);
        p.setDescricao(descricao);
        p.setTipoProduto(tipo);
        p.setUnidadesPorTipo(unidadePorTipo);
        Categoria c = new Categoria();
        c.setId(id_cat);
        p.setCategoria(c);
        UnidadeMedida u = new UnidadeMedida();
        u.setId(id_uni);
        p.setUnidadeMedida(u);
        Imposto t = new Imposto();
        t.setId(id_tax);
        p.setImposto(t);

        if (produtoDao.addEntity(p)) {
            setOpsSuccess(true);
        }
    }

    public void actualiza(int id, Integer id_cat, Integer id_uni, Integer id_tax,
            String nome, String codBarras, String descricao,String tipo, Integer unidadePorTipo, Boolean controleStock, Integer nivelstock) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (id_cat == null || id_cat <= 0) {
            throw new ServiceException("Categoria nao definida!");
        }

        if (id_uni == null || id_uni <= 0) {
            throw new ServiceException("Unidade de medida nao definida!");
        }

        if (id_tax == null || id_tax <= 0) {
            throw new ServiceException("Taxa/Imposto nao definida!");
        }

        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome do produto nao pode ser nulo!");
        }

        if (codBarras == null || codBarras.isEmpty()) {
            codBarras = "1000000000001";
        }

        if (Boolean.TRUE.equals(controleStock) && (nivelstock == null || nivelstock < 0)) {
            throw new ServiceException("Nivel de stock minimo nao informado!");
        }

        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo_barra(codBarras);
        p.setControle_stock(controleStock);
        p.setNivelstock(nivelstock);
        p.setDescricao(descricao);
        p.setTipoProduto(tipo);
        p.setUnidadesPorTipo(unidadePorTipo);
        Categoria c = new Categoria();
        c.setId(id_cat);
        p.setCategoria(c);
        UnidadeMedida u = new UnidadeMedida();
        u.setId(id_uni);
        p.setUnidadeMedida(u);
        Imposto t = new Imposto();
        t.setId(id_tax);
        p.setImposto(t);

        if (produtoDao.updateEntityByID(p, id)) {
            setOpsSuccess(true);
        }
    }
    //

    public ObservableList<Produto> getProdutos() {
        return produtoDao.getCombinedEntities();
    }
    public ObservableList<Produto> listaTodosProdutos() {
        return produtoDao.listAllProducts();
    }
    public Produto getUltimo() {
        return produtoDao.getLastEntity();
    }
    
    public int contaProdutos(Integer id_cat){
        return produtoDao.countProdutos(id_cat);
    }
    
    public void excluir(int id){
        setOpsSuccess(false);
        if(produtoDao.deleteEntity(id)){
            setOpsSuccess(true);
        }
    }

}
