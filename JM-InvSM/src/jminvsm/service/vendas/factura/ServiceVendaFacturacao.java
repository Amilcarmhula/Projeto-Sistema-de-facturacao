package jminvsm.service.vendas.factura;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.Documentos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.vendas.factura.itens.ServiceItemFactura;

public class ServiceVendaFacturacao {

    private final ServiceFactura serviceFactura;
    private final ServiceItemFactura serviceItemFactura;

    public ServiceVendaFacturacao() throws SQLException {
        this.serviceFactura = new ServiceFactura();
        this.serviceItemFactura = new ServiceItemFactura();
    }

    public boolean emitirFactura(String numero, String dataEmissao, String dataVencimento, Double total,
            String condicaoPagamento, DocumentosComerciais doc, Integer idCliente, Empresa empresa,
            Usuario usuario, List<Item> itens, Map<String, Stock> mapaStock) throws SQLException {
        List<Item> itensPreparados = serviceItemFactura.prepararItensFactura(itens, mapaStock, numero, usuario);
        serviceFactura.criaFactura(numero, dataEmissao, dataVencimento, total, condicaoPagamento,
                doc, idCliente, empresa, usuario, itensPreparados);
        return serviceFactura.isEstado();
    }

    public boolean converterCotacaoEmFactura(String numeroCotacao, String numeroFactura, String dataEmissao,
            String dataVencimento, String condicaoPagamento, DocumentosComerciais docDestino,
            Integer idCliente, Empresa empresa, Usuario usuario) throws SQLException {
        return serviceFactura.converterCotacaoEmFactura(numeroCotacao, numeroFactura, dataEmissao,
                dataVencimento, condicaoPagamento, docDestino, idCliente, empresa, usuario);
    }

    public Factura consultaFactura(String numero) {
        return serviceFactura.consultaFactura(numero);
    }

    public Map<String, Stock> mapearStockPorReferencia(List<Stock> listaStock) {
        Map<String, Stock> mapaStock = new HashMap<>();
        if (listaStock == null) {
            return mapaStock;
        }
        for (Stock stock : listaStock) {
            if (stock != null && stock.getProduto() != null && stock.getArmazem() != null) {
                mapaStock.put(stock.getProduto().getId() + "#" + stock.getArmazem().getId(), stock);
            }
        }
        return mapaStock;
    }

    public boolean stockContemTermo(Stock stock, String termo) {
        if (termo == null || termo.isEmpty()) {
            return true;
        }
        if (stock == null || stock.getProduto() == null) {
            return false;
        }
        String filtro = termo.toLowerCase();
        return contains(stock.getProduto().getNome(), filtro)
                || contains(stock.getProduto().getDescricao(), filtro);
    }

    private boolean contains(String value, String termo) {
        return value != null && value.toLowerCase().contains(termo);
    }
}
