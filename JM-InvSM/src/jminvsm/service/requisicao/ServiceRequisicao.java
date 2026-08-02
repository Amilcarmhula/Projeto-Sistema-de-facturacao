package jminvsm.service.requisicao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.requisicao.RequisicaoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.service.ServiceException;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

public class ServiceRequisicao {

    private final RequisicaoDAO requisicaoDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;
    private boolean opsSuccess;

    public ServiceRequisicao() throws SQLException {
        this.requisicaoDao = new RequisicaoDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registaRequisicao(String dataNecessidade, String motivo, List<ItemRequisicao> itens) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("O modulo de requisicoes");
        if (dataNecessidade == null || dataNecessidade.trim().isEmpty()) {
            throw new ServiceException("Data de necessidade nao informada!");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new ServiceException("Motivo da requisicao nao informado!");
        }
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A requisicao deve possuir pelo menos um item!");
        }
        for (ItemRequisicao item : itens) {
            if (!isItemValido(item)) {
                return;
            }
        }

        Requisicao requisicao = new Requisicao();
        requisicao.setDataRequisicao(LocalDate.now());
        requisicao.setDataNecessidade(dataNecessidade);
        requisicao.setEstado("Pendente");
        requisicao.setMotivo(motivo);
        requisicao.setItens(itens);

        if (requisicaoDao.addEntityWithItems(requisicao)) {
            setOpsSuccess(true);
        }
    }

    public ItemRequisicao novoItem(Integer idProduto, Integer idArmazem, Integer quantidade) {
        if (idProduto == null || idProduto <= 0 || idArmazem == null || idArmazem <= 0 || quantidade == null || quantidade <= 0) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(idProduto);

        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);

        ItemRequisicao item = new ItemRequisicao();
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setQuantidade(quantidade);
        return item;
    }

    public boolean submeterRequisicao(int id) {
        return alteraEstado(id, "Submetida", "Requisicao submetida com sucesso!");
    }

    public boolean cancelarRequisicao(int id) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("A aprovacao de requisicoes");
        if (id <= 0) {
            throw new ServiceException("Requisicao nao selecionada!");
        }
        if (requisicaoDao.cancelarRequisicao(id)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean aprovarRequisicao(int id) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("A confirmacao de picking");
        if (id <= 0) {
            throw new ServiceException("Requisicao nao selecionada!");
        }
        if (requisicaoDao.aprovarRequisicao(id)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean confirmarPicking(int idRequisicao) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("A alteracao de estado da requisicao");
        if (idRequisicao <= 0) {
            throw new ServiceException("Requisicao nao selecionada!");
        }
        if (requisicaoDao.confirmarPicking(idRequisicao)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public ObservableList<Requisicao> listaRequisicoes() {
        return requisicaoDao.listAllEntities();
    }

    public ObservableList<Requisicao> listaRequisicoesPorEstado(String estado) {
        return requisicaoDao.getEntitiesByEstado(estado);
    }

    public Requisicao consultaRequisicaoByID(int id) {
        return requisicaoDao.getEntityByID(id);
    }

    public ObservableList<ItemRequisicao> consultaItens(int idRequisicao) {
        return requisicaoDao.getItens(idRequisicao);
    }

    private boolean alteraEstado(int id, String estado, String mensagemSucesso) {
        setOpsSuccess(false);
        if (id <= 0) {
            throw new ServiceException("Requisicao nao selecionada!");
        }
        if (requisicaoDao.updateEstado(id, estado)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    private boolean isItemValido(ItemRequisicao item) {
        if (item == null) {
            throw new ServiceException("Item da requisicao invalido!");
        }
        if (item.getProduto() == null || item.getProduto().getId() <= 0) {
            throw new ServiceException("Produto do item nao informado!");
        }
        if (item.getArmazem() == null || item.getArmazem().getId() <= 0) {
            throw new ServiceException("Armazem do item nao informado!");
        }
        if (item.getQuantidade() <= 0) {
            throw new ServiceException("Quantidade do item deve ser maior que zero!");
        }
        return true;
    }
}
