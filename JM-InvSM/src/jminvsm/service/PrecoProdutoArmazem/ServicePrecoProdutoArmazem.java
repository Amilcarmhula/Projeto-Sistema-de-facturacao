/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.PrecoProdutoArmazem;

import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import jminvsm.dao.preco.PrecoProdutoArmazemDAO;
import jminvsm.dao.preco.PrecoVendaDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert("Preço nao pode ser nulo, Será definido um preço padrão.");
        }
        
        PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
        ppa.setPreco(preco);
        Armazem a = new Armazem();
        a.setId(idArmazem);
        ppa.setArmazem(a);
        Produto p = new Produto();
        p.setId(idProduto);
        ppa.setProduto(p);
        if (precoDao.addEntity(ppa)) {
            showSuccessAlert("Preço registado com sucesso!");
            setOpsSuccess(true);
        }
    }

//    public void actualizaPrecoVenda(int id, Double preco, Double precoComTaxa, Double precoSemTaxa, String data_inicio,
//            String data_fim, Usuario user) {
//        setOpsSuccess(false);
//
//        if (preco == null || preco <= 0) {
//            showErroAlert("Preço nao pode ser nulo, Será definido um preço padrão.");
//        }
//        if (data_inicio == null || data_inicio.isEmpty()) {
//            showErroAlert("Data inicial do preco não pode ser nula. Será usada a data atual!");
//            data_inicio = LocalDate.now().toString();
//        }
//        if (data_fim == null || data_fim.isEmpty()) {
//            showErroAlert("Data final do preco não pode ser nula. Será usada uma data padrão!");
//            data_fim = "2099-12-31";
//        }
//
//        if (user == null) {
//            showErroAlert("Usuário não definido!");
//            return;
//        }
//
//        PrecoVenda pv = new PrecoVenda();
//        pv.setPrecoNormal(preco);
//        pv.setPrecoFinal(precoSemTaxa);
//        pv.setPrecoVenda(precoComTaxa);
//        pv.setData_inicio(data_inicio);
//        pv.setDataValidade(data_fim);
//        pv.setUsuario(user);
//
//        if (precoDao.updateEntityByID(pv, id)) {
//            showSuccessAlert("Preço actualizado com sucesso!");
//            setOpsSuccess(true);
//        }
//    }
//
//    public ObservableList<PrecoVenda> consultaPrecos(int idProduto) {
//        return precoDao.getEntityByProdutoID(idProduto);
//    }
//
//    public void deletePreco(int id) {
//        setOpsSuccess(false);
//        if (precoDao.deleteEntityByUSER(id)) {
//            setOpsSuccess(true);
//        }
//    }

}
