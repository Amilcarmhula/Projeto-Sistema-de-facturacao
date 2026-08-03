/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.precoProdutoArmazem;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.precoProdutoArmazem.PrecoProdutoArmazemDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicePrecoProdutoArmazem {

    private boolean OpsSuccess = false;

    private PrecoProdutoArmazemDAO precoDao;

    public ServicePrecoProdutoArmazem() throws SQLException {
        this.precoDao = new PrecoProdutoArmazemDAO();
    }

    public boolean isOpsSuccess() {
        return OpsSuccess;
    }

    public void setOpsSuccess(boolean OpsSuccess) {
        this.OpsSuccess = OpsSuccess;
    }

    public void registraPreco(Integer idProduto, Integer idArmazem, Double preco) {
        setOpsSuccess(false);
        if (preco == null || preco <= 0) {
            throw new ServiceException("Preco nao pode ser nulo!");
        }
        
        PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
        ppa.setPrecoBase(preco);
        Armazem a = new Armazem();
        a.setId(idArmazem);
        ppa.setArmazem(a);
        Produto p = new Produto();
        p.setId(idProduto);
        ppa.setProduto(p);
        if (precoDao.addEntity(ppa)) {
            setOpsSuccess(true);
        }
    }
    public ObservableList<PrecoProdutoArmazem> listaTodosPrecos(int idProduto) {
        return precoDao.listAllPrecos(idProduto);
    }
    
    public ObservableList<PrecoProdutoArmazem> consultaPrecosVendas(int idProduto, int idArmazem) {
        return precoDao.getEntityByID(idProduto,idArmazem);
    }
//
//    public void deletePreco(int id) {
//        setOpsSuccess(false);
//        if (precoDao.deleteEntityByUSER(id)) {
//            setOpsSuccess(true);
//        }
//    }

}
