package jminvsm.dao.compra;

import javafx.collections.ObservableList;
import jminvsm.dao.CrudDAO;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;

public interface FacturaFornecedorDAOImpl extends CrudDAO<FacturaFornecedor> {

    boolean addEntityWithItems(FacturaFornecedor facturaFornecedor);

    boolean criarDeRecebimento(String numeroFactura, int idRecebimento, String dataEmissao,
            String dataVencimento, int idUsuario);

    boolean updateEstado(int id, String estado);

    ObservableList<FacturaFornecedor> getEntitiesByEstado(String estado);

    ObservableList<ItemFacturaFornecedor> getItens(int idFacturaFornecedor);
}
