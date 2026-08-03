package jminvsm.service.compra;

import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.service.ServiceException;
import jminvsm.service.precoProdutoArmazem.ServicePrecoProdutoArmazem;
import jminvsm.service.produto.fornecido.ServiceProdutoFornecido;

public class ServiceRecebimentoCompraFormulario {

    private final ServiceRecebimentoCompra serviceRecebimentoCompra;
    private final ServicePrecoProdutoArmazem servicePrecoProdutoArmazem;
    private final ServiceProdutoFornecido serviceProdutoFornecido;

    public ServiceRecebimentoCompraFormulario(ServiceRecebimentoCompra serviceRecebimentoCompra,
            ServicePrecoProdutoArmazem servicePrecoProdutoArmazem,
            ServiceProdutoFornecido serviceProdutoFornecido) {
        this.serviceRecebimentoCompra = serviceRecebimentoCompra;
        this.servicePrecoProdutoArmazem = servicePrecoProdutoArmazem;
        this.serviceProdutoFornecido = serviceProdutoFornecido;
    }

    public ItemRecebimentoCompra montarItem(Produto produto, Armazem armazem, ItemOrdemCompra itemOrdem,
            String quantidadeTexto, String custoTexto, String codigoLote) {
        Integer quantidade = parseInteger(quantidadeTexto);
        Double custo = parseDouble(custoTexto);
        if (produto == null || armazem == null || quantidade == null || quantidade <= 0 || custo == null || custo < 0) {
            throw new ServiceException("Seleccione produto, armazem, quantidade e custo validos!");
        }
        ItemRecebimentoCompra item = serviceRecebimentoCompra.novoItem(produto.getId(), armazem.getId(),
                quantidade, custo, codigoLote, itemOrdem != null ? itemOrdem.getId() : null);
        if (item == null) {
            throw new ServiceException("Dados do item invalidos!");
        }
        item.setProduto(produto);
        item.setArmazem(armazem);
        item.setItemOrdemCompra(itemOrdem);
        return item;
    }

    public ItemOrdemCompra localizarItemOrdem(ObservableList<ItemOrdemCompra> itens, Produto produto, Armazem armazem) {
        if (itens == null || produto == null) {
            return null;
        }
        ItemOrdemCompra primeiroProduto = null;
        for (ItemOrdemCompra item : itens) {
            if (item == null || item.getProduto() == null || item.getProduto().getId() != produto.getId()) {
                continue;
            }
            if (primeiroProduto == null && item.getQuantidadePendente() > 0) {
                primeiroProduto = item;
            }
            if (armazem != null && item.getArmazem() != null && item.getArmazem().getId() == armazem.getId()
                    && item.getQuantidadePendente() > 0) {
                return item;
            }
        }
        return primeiroProduto;
    }

    public double custoProdutoArmazem(Produto produto, Armazem armazem) {
        if (produto == null || armazem == null || servicePrecoProdutoArmazem == null) {
            return 0.0;
        }
        ObservableList<PrecoProdutoArmazem> precos = servicePrecoProdutoArmazem.consultaPrecosVendas(produto.getId(), armazem.getId());
        if (precos == null || precos.isEmpty()) {
            return 0.0;
        }
        PrecoProdutoArmazem preco = precos.get(0);
        return preco != null && preco.getPrecoBase() != null ? preco.getPrecoBase() : 0.0;
    }

    public String proximoCodigoLote() {
        if (serviceProdutoFornecido == null) {
            return "";
        }
        ProdutoFornecido produtoFornecido = serviceProdutoFornecido.getLastCodeLote();
        return produtoFornecido == null ? "" : String.valueOf(produtoFornecido.getCodigo());
    }

    public String numeroRecebimento(String numeroInformado) {
        return numeroInformado == null || numeroInformado.trim().isEmpty()
                ? "RC-" + System.currentTimeMillis()
                : numeroInformado.trim();
    }

    private Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Double.valueOf(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
