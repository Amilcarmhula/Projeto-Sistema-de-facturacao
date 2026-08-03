package jminvsm.service.produto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jminvsm.model.produto.Produto;

public class ServicePesquisaProduto {

    public Map<Integer, Produto> mapearProdutosPorId(List<Produto> produtos) {
        Map<Integer, Produto> mapaProdutos = new HashMap<>();
        if (produtos == null) {
            return mapaProdutos;
        }
        for (Produto produto : produtos) {
            if (produto != null) {
                mapaProdutos.put(produto.getId(), produto);
            }
        }
        return mapaProdutos;
    }

    public boolean produtoContemTermo(Produto produto, String termo) {
        if (termo == null || termo.isEmpty()) {
            return true;
        }
        if (produto == null) {
            return false;
        }
        String filtro = termo.toLowerCase();
        return contains(produto.getNome(), filtro)
                || contains(produto.getCodigo_barra(), filtro)
                || contains(produto.getDescricao(), filtro);
    }

    private boolean contains(String value, String filtro) {
        return value != null && value.toLowerCase().contains(filtro);
    }
}
