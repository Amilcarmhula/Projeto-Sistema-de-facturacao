package jminvsm.service.produto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.service.ServiceException;
import jminvsm.service.desconto.ServiceProdutoDesconto;
import jminvsm.service.precoProdutoArmazem.ServicePrecoProdutoArmazem;

public class ServiceProdutoPreco {

    private final ServicePrecoProdutoArmazem servicePreco;
    private final ServiceProdutoDesconto serviceProdutoDesconto;

    public ServiceProdutoPreco() throws SQLException {
        this.servicePreco = new ServicePrecoProdutoArmazem();
        this.serviceProdutoDesconto = new ServiceProdutoDesconto();
    }

    public boolean isPrecoOpsSuccess() {
        return servicePreco.isOpsSuccess();
    }

    public boolean isDescontoOpsSuccess() {
        return serviceProdutoDesconto.isOpsSuccess();
    }

    public void registarPreco(Produto produto, Armazem armazem, Double preco) {
        if (produto == null || produto.getId() <= 0) {
            throw new ServiceException("Seleccione primeiro um produto.");
        }
        if (armazem == null || armazem.getId() <= 0) {
            throw new ServiceException("Seleccione o armazem.");
        }
        servicePreco.registraPreco(produto.getId(), armazem.getId(), preco);
    }

    public void registarDesconto(Produto produto, Desconto desconto, Armazem armazem,
            String dataInicio, String dataFim) {
        if (produto == null || produto.getId() <= 0) {
            throw new ServiceException("Seleccione primeiro um produto.");
        }
        if (desconto == null || desconto.getId() <= 0 || armazem == null || armazem.getId() <= 0) {
            throw new ServiceException("Seleccione o desconto e o armazem.");
        }
        serviceProdutoDesconto.regista(produto.getId(), desconto.getId(), armazem.getId(), dataInicio, dataFim);
    }

    public ObservableList<PrecoProdutoArmazem> listaPrecos(Produto produto) {
        if (produto == null || produto.getId() <= 0) {
            return javafx.collections.FXCollections.observableArrayList();
        }
        return servicePreco.listaTodosPrecos(produto.getId());
    }

    public ObservableList<ProdutoDesconto> listaDescontosProduto(Integer idProduto) {
        if (idProduto == null || idProduto <= 0) {
            return javafx.collections.FXCollections.observableArrayList();
        }
        return serviceProdutoDesconto.consultaProdutoDesconto(idProduto);
    }
}
