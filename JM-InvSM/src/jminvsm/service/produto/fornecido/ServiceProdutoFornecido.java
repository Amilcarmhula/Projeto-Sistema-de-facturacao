/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.produto.fornecido;

import java.sql.SQLException;
import java.util.Map;
import jminvsm.dao.produto.fornecido.ProdutoFornecidoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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

    public void registraProdutoFornecido(Integer id_arm, Integer id_pro, Integer id_for,
            Usuario user, Integer qtd, String data_fornecida, Double custo,String unidade_fornecida,Integer qtd_por_unidade) {
        setOpsSuccess(false);

        // Validações básicas antes de registrar a categoria
        if (id_arm <= 0) {
            id_arm = 1;
        }

        if (id_for <= 0) {
            id_for = 1;
        }
        if (qtd <= 0) {
            showErroAlert("Quantidade não definida!");
            return;
        }
        if (custo <= 0) {
            showErroAlert("Preco não pode ser nulo!");
            return;
        }

        if (data_fornecida.isEmpty()) {
            showErroAlert("Data de fornecimento não pode ser nula!");
            return;
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
        pf.setUsuario(user);
        pf.setQuantidade(qtd);
        pf.setData_fornecida(data_fornecida);
        pf.setCusto(custo);
        pf.setUnidade_fornecida(unidade_fornecida);
        pf.setQtd_por_unidade(qtd_por_unidade);

        if (produtoFornecidoDao.addEntity(pf)) {
            setOpsSuccess(true);
            showSuccessAlert("Stock actualizado com sucesso!");
        }
    }
    
    public Map<String, Integer> consultaMovimento(Integer cat, Integer arm){
        return produtoFornecidoDao.getMovimentos(cat, arm);
    }

}
