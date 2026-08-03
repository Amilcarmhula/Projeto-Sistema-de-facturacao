package jminvsm.service.compra;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.OrdemCompraDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.service.ServiceException;

public class ServiceOrdemCompra {

    private final OrdemCompraDAO ordemCompraDao;
    private boolean opsSuccess;

    public ServiceOrdemCompra() throws SQLException {
        this.ordemCompraDao = new OrdemCompraDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean criarDeRequisicao(int idRequisicao, int idFornecedor, String dataPrevistaEntrega, int idUsuario) {
        setOpsSuccess(false);
        throw new ServiceException("Use o fluxo com itens da requisicao e custo unitario antes de criar a ordem de compra.");
    }

    public void registaOrdemCompra(OrdemCompra ordemCompra, List<ItemOrdemCompra> itens) {
        setOpsSuccess(false);
        if (!isOrdemValida(ordemCompra)) {
            return;
        }
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A ordem de compra deve possuir pelo menos um item!");
        }
        if (ordemCompra.getRequisicao() != null && ordemCompra.getRequisicao().getId() > 0
                && ordemCompra.getFornecedor() != null && ordemCompra.getFornecedor().getId() > 0
                && ordemCompraDao.existeOrdemActivaParaRequisicaoFornecedor(
                        ordemCompra.getRequisicao().getId(), ordemCompra.getFornecedor().getId())) {
            throw new ServiceException("Ja existe ordem de compra activa para esta requisicao e fornecedor!");
        }
        for (ItemOrdemCompra item : itens) {
            if (!isItemValido(item)) {
                return;
            }
        }
        ordemCompra.setItens(itens);
        if (ordemCompraDao.addEntityWithItems(ordemCompra)) {
            setOpsSuccess(true);
        }
    }

    public ItemOrdemCompra novoItem(Integer idProduto, Integer idArmazem, Integer quantidade,
            Double custoUnitario, Integer idItemRequisicao) {
        if (idProduto == null || idProduto <= 0 || idArmazem == null || idArmazem <= 0
                || quantidade == null || quantidade <= 0 || custoUnitario == null || custoUnitario < 0) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(idProduto);
        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);
        ItemOrdemCompra item = new ItemOrdemCompra();
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setQuantidadeSolicitada(quantidade);
        item.setCustoUnitario(custoUnitario);
        item.setEstado("Pendente");
        actualizarTotaisItem(item);
        if (idItemRequisicao != null && idItemRequisicao > 0) {
            ItemRequisicao itemRequisicao = new ItemRequisicao();
            itemRequisicao.setId(idItemRequisicao);
            item.setItemRequisicao(itemRequisicao);
        }
        return item;
    }

    public void actualizarTotaisItem(ItemOrdemCompra item) {
        if (item == null) {
            return;
        }
        double desconto = item.getDesconto();
        double imposto = item.getImposto();
        double subtotal = item.getQuantidadeSolicitada() * item.getCustoUnitario();
        item.setSubtotal(subtotal);
        item.setTotalLinha(subtotal - desconto + imposto);
    }

    public boolean cancelarOrdemCompra(int id) {
        return alteraEstado(id, "Cancelada", "Ordem de compra cancelada com sucesso!");
    }

    public boolean enviarOrdemCompra(int id) {
        return alteraEstado(id, "Enviada", "Ordem de compra marcada como enviada!");
    }

    public ObservableList<OrdemCompra> listaOrdensCompra() {
        return ordemCompraDao.listAllEntities();
    }

    public ObservableList<OrdemCompra> listaOrdensCompraPorEstado(String estado) {
        return ordemCompraDao.getEntitiesByEstado(estado);
    }

    public OrdemCompra consultaOrdemCompraByID(int id) {
        return ordemCompraDao.getEntityByID(id);
    }

    public ObservableList<ItemOrdemCompra> consultaItens(int idOrdemCompra) {
        return ordemCompraDao.getItens(idOrdemCompra);
    }

    private boolean alteraEstado(int id, String estado, String mensagemSucesso) {
        setOpsSuccess(false);
        if (id <= 0) {
            throw new ServiceException("Ordem de compra nao selecionada!");
        }
        if (ordemCompraDao.updateEstado(id, estado)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    private boolean isOrdemValida(OrdemCompra ordemCompra) {
        if (ordemCompra == null) {
            throw new ServiceException("Ordem de compra invalida!");
        }
        if (ordemCompra.getNumero() == null || ordemCompra.getNumero().trim().isEmpty()) {
            throw new ServiceException("Numero da ordem de compra nao informado!");
        }
        if (ordemCompra.getFornecedor() == null || ordemCompra.getFornecedor().getId() <= 0) {
            throw new ServiceException("Fornecedor da ordem de compra nao informado!");
        }
        return true;
    }

    private boolean isItemValido(ItemOrdemCompra item) {
        if (item == null || item.getProduto() == null || item.getProduto().getId() <= 0) {
            throw new ServiceException("Produto do item nao informado!");
        }
        if (item.getArmazem() == null || item.getArmazem().getId() <= 0) {
            throw new ServiceException("Armazem do item nao informado!");
        }
        if (item.getQuantidadeSolicitada() <= 0) {
            throw new ServiceException("Quantidade solicitada deve ser maior que zero!");
        }
        if (item.getCustoUnitario() < 0) {
            throw new ServiceException("Custo unitario nao pode ser negativo!");
        }
        return true;
    }
}
