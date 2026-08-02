package jminvsm.service.requisicao;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.requisicao.ItemRequisicaoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.service.ServiceException;

public class ServiceItemRequisicao {

    private final ItemRequisicaoDAO itemRequisicaoDao;
    private boolean opsSuccess;

    public ServiceItemRequisicao() throws SQLException {
        this.itemRequisicaoDao = new ItemRequisicaoDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void adicionaItem(int idRequisicao, int idProduto, int idArmazem, int quantidade) {
        setOpsSuccess(false);
        ItemRequisicao item = montaItem(idRequisicao, idProduto, idArmazem, quantidade);
        if (item == null) {
            return;
        }
        if (itemRequisicaoDao.addEntity(item)) {
            setOpsSuccess(true);
        }
    }

    public void atualizaItem(int idItem, int idRequisicao, int idProduto, int idArmazem, int quantidade) {
        setOpsSuccess(false);
        if (idItem <= 0) {
            throw new ServiceException("Item nao selecionado!");
        }
        ItemRequisicao item = montaItem(idRequisicao, idProduto, idArmazem, quantidade);
        if (item == null) {
            return;
        }
        if (itemRequisicaoDao.updateEntityByID(item, idItem)) {
            setOpsSuccess(true);
        }
    }

    public boolean removeItem(int idItem) {
        setOpsSuccess(false);
        if (idItem <= 0) {
            throw new ServiceException("Item nao selecionado!");
        }
        if (itemRequisicaoDao.deleteEntity(idItem)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public ItemRequisicao consultaItemByID(int id) {
        return itemRequisicaoDao.getEntityByID(id);
    }

    public ObservableList<ItemRequisicao> consultaItensRequisicao(int idRequisicao) {
        return itemRequisicaoDao.getItensByRequisicao(idRequisicao);
    }

    private ItemRequisicao montaItem(int idRequisicao, int idProduto, int idArmazem, int quantidade) {
        if (idRequisicao <= 0 || idProduto <= 0 || idArmazem <= 0 || quantidade <= 0) {
            throw new ServiceException("Dados do item da requisicao invalidos!");
        }
        Requisicao requisicao = new Requisicao();
        requisicao.setId(idRequisicao);

        Produto produto = new Produto();
        produto.setId(idProduto);

        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);

        ItemRequisicao item = new ItemRequisicao();
        item.setRequisicao(requisicao);
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setQuantidade(quantidade);
        return item;
    }
}
