package jminvsm.dao.compra;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.compra.PagamentoFornecedor;

public interface PagamentoFornecedorDAOImpl extends CrudDAO<PagamentoFornecedor> {

    boolean registarPagamento(int idContaPagar, String dataPagamento, double valorPago,
            int idMetodoPagamento, String origemPagamento, Integer idContaBancaria,
            String referencia, String descricao, int idUsuario);

    ObservableList<PagamentoFornecedor> getEntitiesByContaPagar(int idContaPagar);

    ObservableList<PagamentoFornecedor> getEntitiesByFornecedor(int idFornecedor);
}
