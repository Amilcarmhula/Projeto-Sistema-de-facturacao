package jminvsm.service.compra;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.service.ServiceException;

public class ServiceOrdemCompraFormulario {

    private final ServiceOrdemCompra serviceOrdemCompra;

    public ServiceOrdemCompraFormulario(ServiceOrdemCompra serviceOrdemCompra) {
        this.serviceOrdemCompra = serviceOrdemCompra;
    }

    public ItemOrdemCompra montarItemManual(Produto produto, Armazem armazem, String quantidadeTexto, String custoTexto) {
        Integer quantidade = parseInteger(quantidadeTexto);
        Double custo = parseDouble(custoTexto);
        if (produto == null || armazem == null || quantidade == null || quantidade <= 0 || custo == null || custo < 0) {
            throw new ServiceException("Seleccione produto, armazem, quantidade e custo validos!");
        }
        ItemOrdemCompra item = serviceOrdemCompra.novoItem(produto.getId(), armazem.getId(), quantidade, custo, null);
        if (item == null) {
            throw new ServiceException("Dados do item invalidos!");
        }
        item.setProduto(produto);
        item.setArmazem(armazem);
        return item;
    }

    public ObservableList<ItemOrdemCompra> montarItensDaRequisicao(ObservableList<ItemRequisicao> itensRequisicao) {
        ObservableList<ItemOrdemCompra> itens = FXCollections.observableArrayList();
        if (itensRequisicao == null) {
            return itens;
        }
        for (ItemRequisicao itemRequisicao : itensRequisicao) {
            if (itemRequisicao == null || itemRequisicao.getProduto() == null || itemRequisicao.getArmazem() == null) {
                continue;
            }
            ItemOrdemCompra item = serviceOrdemCompra.novoItem(
                    itemRequisicao.getProduto().getId(),
                    itemRequisicao.getArmazem().getId(),
                    itemRequisicao.getQuantidade(),
                    0.00,
                    itemRequisicao.getId());
            if (item != null) {
                item.setProduto(itemRequisicao.getProduto());
                item.setArmazem(itemRequisicao.getArmazem());
                item.setItemRequisicao(itemRequisicao);
                itens.add(item);
            }
        }
        return itens;
    }

    public void validarItensDaRequisicao(ObservableList<ItemOrdemCompra> itens, Requisicao requisicao) {
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A requisicao seleccionada nao possui itens para ordem de compra!");
        }
        for (ItemOrdemCompra item : itens) {
            if (item.getItemRequisicao() == null || item.getItemRequisicao().getId() <= 0) {
                throw new ServiceException("Os itens da ordem devem estar vinculados aos itens da requisicao!");
            }
            if (item.getItemRequisicao().getRequisicao() == null
                    || requisicao == null
                    || item.getItemRequisicao().getRequisicao().getId() != requisicao.getId()) {
                throw new ServiceException("Existem itens que nao pertencem a requisicao seleccionada!");
            }
            if (item.getCustoUnitario() <= 0) {
                throw new ServiceException("Informe custo unitario maior que zero para todos os itens da requisicao!");
            }
            serviceOrdemCompra.actualizarTotaisItem(item);
        }
    }

    public String numeroOrdem(String numeroInformado) {
        return numeroInformado == null || numeroInformado.trim().isEmpty()
                ? "OC-" + System.currentTimeMillis()
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
