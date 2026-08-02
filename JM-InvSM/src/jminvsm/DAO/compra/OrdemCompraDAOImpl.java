package jminvsm.dao.compra;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.OrdemCompra;

public interface OrdemCompraDAOImpl extends CrudDAO<OrdemCompra> {

    boolean addEntityWithItems(OrdemCompra ordemCompra);

    boolean criarDeRequisicao(int idRequisicao, int idFornecedor, String dataPrevistaEntrega, int idUsuario);

    boolean existeOrdemActivaParaRequisicaoFornecedor(int idRequisicao, int idFornecedor);

    boolean updateEstado(int id, String estado);

    ObservableList<OrdemCompra> getEntitiesByEstado(String estado);

    ObservableList<ItemOrdemCompra> getItens(int idOrdemCompra);
}
