package jminvsm.service.stock;

import java.math.BigDecimal;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.stock.Stock;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.fornecedor.ServiceFornecedor;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;

public class ServiceEntradaStock {

    private final ServiceProdutoFornecido serviceProdutoFornecido;
    private final ServiceStock serviceStock;
    private final ServiceArmazem serviceArmazem;
    private final ServiceFornecedor serviceFornecedor;

    public ServiceEntradaStock() throws SQLException {
        this.serviceProdutoFornecido = new ServiceProdutoFornecido();
        this.serviceStock = new ServiceStock();
        this.serviceArmazem = new ServiceArmazem();
        this.serviceFornecedor = new ServiceFornecedor();
    }

    public boolean isOpsSuccess() {
        return serviceProdutoFornecido.isOpsSuccess();
    }

    public void registarEntrada(String codigoLote, Produto produto, Armazem armazem,
            Fornecedor fornecedor, String quantidade, String dataEntrada, String custoUnitario) {
        validarProduto(produto);
        validarArmazem(armazem);
        validarFornecedor(fornecedor);
        if (isBlank(codigoLote)) {
            throw new ServiceException("Informe ou gere o codigo do lote.");
        }
        if (isBlank(dataEntrada)) {
            throw new ServiceException("Informe a data de entrada do lote.");
        }

        int qtd = inteiroPositivo(quantidade, "Informe uma quantidade inteira maior que zero.");
        BigDecimal custo = decimalNaoNegativo(custoUnitario, "Informe um custo unitario numerico valido.");
        if (custo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Preco nao pode ser nulo!");
        }

        serviceProdutoFornecido.registraProdutoFornecido(
                codigoLote.trim(),
                produto.getId(),
                armazem.getId(),
                fornecedor.getId(),
                qtd,
                dataEntrada.trim(),
                custo.doubleValue());
    }

    public BigDecimal calcularValorTotal(String quantidade, String custoUnitario) {
        int qtd = inteiroPositivo(quantidade, "Quantidade invalida.");
        BigDecimal custo = decimalNaoNegativo(custoUnitario, "Custo unitario invalido.");
        return custo.multiply(BigDecimal.valueOf(qtd));
    }

    public ObservableList<ProdutoFornecido> listaLotes(Produto produto) {
        validarProduto(produto);
        return serviceProdutoFornecido.getLotes(produto.getId());
    }

    public ProdutoFornecido proximoCodigoLote() {
        return serviceProdutoFornecido.getLastCodeLote();
    }

    public Stock consultaDetalhesProduto(Produto produto) {
        validarProduto(produto);
        return serviceStock.consultaDetalhesProduto(produto.getId());
    }

    public ObservableList<Stock> resumoStock(Produto produto) {
        validarProduto(produto);
        return serviceStock.getResumo(produto.getId());
    }

    public ObservableList<Armazem> listaArmazens() {
        return serviceArmazem.listaTodosArmazens();
    }

    public ObservableList<Fornecedor> listaFornecedores() {
        return serviceFornecedor.listaTodosFornecedores();
    }

    private void validarProduto(Produto produto) {
        if (produto == null || produto.getId() <= 0) {
            throw new ServiceException("Produto nao selecionado!");
        }
    }

    private void validarArmazem(Armazem armazem) {
        if (armazem == null || armazem.getId() <= 0) {
            throw new ServiceException("Seleccione o armazem de destino.");
        }
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null || fornecedor.getId() <= 0) {
            throw new ServiceException("Seleccione o fornecedor do lote.");
        }
    }

    private int inteiroPositivo(String value, String mensagem) {
        if (isBlank(value)) {
            throw new ServiceException(mensagem);
        }
        try {
            int result = Integer.parseInt(value.trim());
            if (result <= 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException ex) {
            throw new ServiceException(mensagem, ex);
        }
    }

    private BigDecimal decimalNaoNegativo(String value, String mensagem) {
        if (isBlank(value)) {
            return BigDecimal.ZERO;
        }
        try {
            BigDecimal result = new BigDecimal(value.trim().replace(" ", "").replace(",", "."));
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException ex) {
            throw new ServiceException(mensagem, ex);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
