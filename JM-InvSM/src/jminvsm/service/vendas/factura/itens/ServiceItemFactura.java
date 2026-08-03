package jminvsm.service.vendas.factura.itens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.ServiceException;

public class ServiceItemFactura {

    private final Random random = new Random();

    public Item criarItemParaVenda(Stock stock, String numeroFactura, Usuario usuario) {
        if (stock == null || stock.getProduto() == null || stock.getArmazem() == null
                || stock.getPrecoProdutoArmazem() == null) {
            throw new ServiceException("Produto seleccionado nao possui dados suficientes para venda!");
        }

        int quantidade = 1;
        Item item = new Item();
        item.setId(random.nextInt(50));
        item.setRef(stock.getProduto().getId() + "#" + stock.getArmazem().getId());
        item.setQuantidade(quantidade);
        item.setSubtotal(quantidade * stock.getPrecoProdutoArmazem().getPrecoFinal());
        item.setUnidade_vendida(stock.getProduto().getTipoProduto());
        item.setQtd_por_unidade(stock.getProduto().getUnidadesPorTipo());
        item.setProduto(stock.getProduto());
        item.setPrecoProdutoArmazem(stock.getPrecoProdutoArmazem());
        item.setFactura(criarFacturaRef(numeroFactura));
        item.setUsuario(usuario);
        item.setArmazem(stock.getArmazem());
        return item;
    }

    public void actualizarSubtotal(Item item) {
        if (item != null) {
            item.calcularSubtotal();
        }
    }

    public List<Item> prepararItensFactura(List<Item> itens, Map<String, Stock> mapaStock,
            String numeroFactura, Usuario usuario) {
        return prepararItens(itens, mapaStock, numeroFactura, usuario, "Nao existem itens na factura!");
    }

    public List<Item> prepararItensCotacao(List<Item> itens, Map<String, Stock> mapaStock,
            String numeroCotacao, Usuario usuario) {
        return prepararItens(itens, mapaStock, numeroCotacao, usuario, "A cotacao deve possuir pelo menos um item!");
    }

    private List<Item> prepararItens(List<Item> itens, Map<String, Stock> mapaStock, String numeroDocumento,
            Usuario usuario, String mensagemSemItens) {
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException(mensagemSemItens);
        }

        List<Item> itensPreparados = new ArrayList<>();
        for (Item item : itens) {
            Stock stock = mapaStock == null ? null : mapaStock.get(item.getRef());
            if ((stock == null || stock.getProduto() == null || stock.getArmazem() == null)
                    && (item.getProduto() == null || item.getArmazem() == null)) {
                throw new ServiceException("Item sem produto/armazem valido: " + item.getRef());
            }

            Item itemPreparado = new Item();
            itemPreparado.setQuantidade(item.getQuantidade());
            itemPreparado.setSubtotal(item.getSubtotal());
            itemPreparado.setUnidade_vendida(item.getUnidade_vendida());
            itemPreparado.setQtd_por_unidade(item.getQtd_por_unidade());
            itemPreparado.setProduto(stock != null ? stock.getProduto() : item.getProduto());
            itemPreparado.setArmazem(stock != null ? stock.getArmazem() : item.getArmazem());
            itemPreparado.setUsuario(usuario);
            itemPreparado.setFactura(criarFacturaRef(numeroDocumento));
            itensPreparados.add(itemPreparado);
        }
        return itensPreparados;
    }

    private Factura criarFacturaRef(String numeroFactura) {
        Factura factura = new Factura();
        factura.setNumero_fac(numeroFactura);
        return factura;
    }
}
