package jminvsm.service.vendas.factura;

import java.util.List;
import java.util.Map;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.itens.Item;

public class ServiceCalculoFactura {

    public TotaisFactura calcularTotais(List<Item> itens, Map<String, Stock> mapaStock, boolean facturaSimplificada) {
        double subtotal = 0;
        double desconto = 0;
        double imposto = 0;
        double total = 0;

        if (itens == null) {
            return new TotaisFactura(subtotal, desconto, imposto, total);
        }

        for (Item item : itens) {
            Stock stock = mapaStock == null ? null : mapaStock.get(item.getRef());
            double precoBase = getPrecoBase(item, stock);
            double precoVenda = getPrecoVenda(item, stock);
            double precoFinal = getPrecoFinal(item, stock);

            if (facturaSimplificada) {
                subtotal += item.getQuantidade() * precoFinal;
                imposto += (item.getQuantidade() * precoVenda) - (item.getQuantidade() * precoBase);
                desconto += item.getQuantidade() * precoVenda - (item.getQuantidade() * precoFinal);
                total = subtotal;
            } else {
                subtotal += item.getQuantidade() * precoBase;
                imposto += (item.getQuantidade() * precoVenda) - (item.getQuantidade() * precoBase);
                desconto += item.getQuantidade() * precoVenda - (item.getQuantidade() * precoFinal);
                total = subtotal + imposto - desconto;
            }

            item.setSubtotal(item.getQuantidade() * precoFinal);
        }

        return new TotaisFactura(subtotal, desconto, imposto, total);
    }

    private double getPrecoBase(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoBase() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoBase();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoBase() != null) {
            return item.getPrecoProdutoArmazem().getPrecoBase();
        }
        return item.getSubtotal();
    }

    private double getPrecoVenda(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoVenda() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoVenda();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoVenda() != null) {
            return item.getPrecoProdutoArmazem().getPrecoVenda();
        }
        return item.getSubtotal();
    }

    private double getPrecoFinal(Item item, Stock stock) {
        if (stock != null && stock.getPrecoProdutoArmazem() != null && stock.getPrecoProdutoArmazem().getPrecoFinal() != null) {
            return stock.getPrecoProdutoArmazem().getPrecoFinal();
        }
        if (item.getPrecoProdutoArmazem() != null && item.getPrecoProdutoArmazem().getPrecoFinal() != null) {
            return item.getPrecoProdutoArmazem().getPrecoFinal();
        }
        return item.getQuantidade() > 0 ? item.getSubtotal() / item.getQuantidade() : item.getSubtotal();
    }

    public static class TotaisFactura {

        private final double subtotal;
        private final double desconto;
        private final double imposto;
        private final double total;

        public TotaisFactura(double subtotal, double desconto, double imposto, double total) {
            this.subtotal = subtotal;
            this.desconto = desconto;
            this.imposto = imposto;
            this.total = total;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getDesconto() {
            return desconto;
        }

        public double getImposto() {
            return imposto;
        }

        public double getTotal() {
            return total;
        }
    }
}
