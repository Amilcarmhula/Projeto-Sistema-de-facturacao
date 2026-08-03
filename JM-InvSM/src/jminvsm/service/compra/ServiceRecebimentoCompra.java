package jminvsm.service.compra;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.RecebimentoCompraDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.model.produto.Produto;
import jminvsm.service.ServiceException;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

public class ServiceRecebimentoCompra {

    private final RecebimentoCompraDAO recebimentoCompraDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;
    private boolean opsSuccess;

    public ServiceRecebimentoCompra() throws SQLException {
        this.recebimentoCompraDao = new RecebimentoCompraDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registaRecebimento(RecebimentoCompra recebimento, List<ItemRecebimentoCompra> itens) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("O recebimento de compras");
        if (!isRecebimentoValido(recebimento)) {
            return;
        }
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("O recebimento deve possuir pelo menos um item!");
        }
        for (ItemRecebimentoCompra item : itens) {
            if (!isItemValido(item)) {
                return;
            }
        }
        recebimento.setItens(itens);
        if (recebimentoCompraDao.addEntityWithItems(recebimento)) {
            setOpsSuccess(true);
        }
    }

    public ItemRecebimentoCompra novoItem(Integer idProduto, Integer idArmazem, Integer quantidade,
            Double custoUnitario, String codigoLote, Integer idItemOrdemCompra) {
        if (idProduto == null || idProduto <= 0 || idArmazem == null || idArmazem <= 0
                || quantidade == null || quantidade <= 0 || custoUnitario == null || custoUnitario < 0) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(idProduto);
        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);
        ItemRecebimentoCompra item = new ItemRecebimentoCompra();
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setQuantidadeRecebida(quantidade);
        item.setCustoUnitario(custoUnitario);
        item.setCodigoLote(codigoLote);
        item.setEstado("Pendente");
        actualizarSubtotalItem(item);
        if (idItemOrdemCompra != null && idItemOrdemCompra > 0) {
            ItemOrdemCompra itemOrdem = new ItemOrdemCompra();
            itemOrdem.setId(idItemOrdemCompra);
            item.setItemOrdemCompra(itemOrdem);
        }
        return item;
    }

    public void actualizarSubtotalItem(ItemRecebimentoCompra item) {
        if (item == null) {
            return;
        }
        item.setSubtotal(item.getQuantidadeRecebida() * item.getCustoUnitario());
    }

    public boolean confirmarRecebimento(int idRecebimento) {
        setOpsSuccess(false);
        serviceConfiguracao.exigirModuloStockActivo("A confirmacao de recebimento");
        if (idRecebimento <= 0) {
            throw new ServiceException("Recebimento nao selecionado!");
        }
        if (recebimentoCompraDao.confirmarRecebimento(idRecebimento)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public boolean cancelarRecebimento(int id) {
        return alteraEstado(id, "Cancelado", "Recebimento cancelado com sucesso!");
    }

    public ObservableList<RecebimentoCompra> listaRecebimentos() {
        return recebimentoCompraDao.listAllEntities();
    }

    public ObservableList<RecebimentoCompra> listaRecebimentosPorEstado(String estado) {
        return recebimentoCompraDao.getEntitiesByEstado(estado);
    }

    public RecebimentoCompra consultaRecebimentoByID(int id) {
        return recebimentoCompraDao.getEntityByID(id);
    }

    public ObservableList<ItemRecebimentoCompra> consultaItens(int idRecebimentoCompra) {
        return recebimentoCompraDao.getItens(idRecebimentoCompra);
    }

    private boolean alteraEstado(int id, String estado, String mensagemSucesso) {
        setOpsSuccess(false);
        if (id <= 0) {
            throw new ServiceException("Recebimento nao selecionado!");
        }
        if (recebimentoCompraDao.updateEstado(id, estado)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    private boolean isRecebimentoValido(RecebimentoCompra recebimento) {
        if (recebimento == null) {
            throw new ServiceException("Recebimento invalido!");
        }
        if (recebimento.getNumero() == null || recebimento.getNumero().trim().isEmpty()) {
            throw new ServiceException("Numero do recebimento nao informado!");
        }
        if (recebimento.getOrdemCompra() == null || recebimento.getOrdemCompra().getId() <= 0) {
            throw new ServiceException("Ordem de compra do recebimento nao informada!");
        }
        if (recebimento.getFornecedor() == null || recebimento.getFornecedor().getId() <= 0) {
            throw new ServiceException("Fornecedor do recebimento nao informado!");
        }
        return true;
    }

    private boolean isItemValido(ItemRecebimentoCompra item) {
        if (item == null || item.getProduto() == null || item.getProduto().getId() <= 0) {
            throw new ServiceException("Produto do item recebido nao informado!");
        }
        if (item.getArmazem() == null || item.getArmazem().getId() <= 0) {
            throw new ServiceException("Armazem do item recebido nao informado!");
        }
        if (item.getQuantidadeRecebida() <= 0) {
            throw new ServiceException("Quantidade recebida deve ser maior que zero!");
        }
        if (item.getCustoUnitario() < 0) {
            throw new ServiceException("Custo unitario nao pode ser negativo!");
        }
        return true;
    }
}
