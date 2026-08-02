/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.produto.fornecido;

import java.sql.SQLException;
import java.util.Map;
import javafx.collections.ObservableList;
import jminvsm.dao.produto.fornecido.ProdutoFornecidoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProdutoFornecido {

    private final ProdutoFornecidoDAO produtoFornecidoDao;
    private boolean OpsSuccess = false;

    public ServiceProdutoFornecido( ) throws SQLException {
        this.produtoFornecidoDao = new ProdutoFornecidoDAO();
    }

    public boolean isOpsSuccess() {
        return OpsSuccess;
    }

    public void setOpsSuccess(boolean OpsSuccess) {
        this.OpsSuccess = OpsSuccess;
    }

    public void registraProdutoFornecido(String code, Integer id_pro, Integer id_arm, Integer id_for,
             Integer qtdActual, String data_fornecida, Double custo) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (id_arm <= 0) {
            id_arm = 1;
        }

        if (id_for <= 0) {
            id_for = 1;
        }
        if (qtdActual <= 0) {
            throw new ServiceException("Quantidade nao definida!");
        }
        if (custo <= 0) {
            throw new ServiceException("Preco nao pode ser nulo!");
        }

        if (data_fornecida.isEmpty()) {
            throw new ServiceException("Data de fornecimento nao pode ser nula!");
        }
        ProdutoFornecido pf = new ProdutoFornecido();
        Armazem a = new Armazem();
        a.setId(id_arm);
        pf.setArmazem(a);
        Produto p = new Produto();
        p.setId(id_pro);
        pf.setProduto(p);
        Fornecedor f = new Fornecedor();
        f.setId(id_for);
        pf.setFornecedor(f);
        pf.setCodigo(code);
        pf.setQuantidadeActual(qtdActual);
        pf.setDataEntrada(data_fornecida);
        pf.setCustoUnitario(custo);


        if (produtoFornecidoDao.addEntity(pf)) {
            setOpsSuccess(true);
        }
    }
    
    public Map<String, Integer> consultaMovimento(Integer cat, Integer arm){
        return produtoFornecidoDao.getMovimentos(cat, arm);
    }
    
    public ObservableList<ProdutoFornecido> getLotes(Integer idProd) {
        return produtoFornecidoDao.getLotesForStock(idProd);
    }
    
    public ProdutoFornecido getLastCodeLote(){
        return produtoFornecidoDao.getLastCodeLote();
    }
    
}
