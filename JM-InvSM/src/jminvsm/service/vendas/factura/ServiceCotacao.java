package jminvsm.service.vendas.factura;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import jminvsm.model.stock.Stock;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.Documentos.DocumentosComerciais;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.ServiceException;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import jminvsm.service.vendas.factura.itens.ServiceItemFactura;

public class ServiceCotacao {

    private final ServiceItem serviceItem;
    private final ServiceItemFactura serviceItemFactura;

    public ServiceCotacao() throws SQLException {
        this.serviceItem = new ServiceItem();
        this.serviceItemFactura = new ServiceItemFactura();
    }

    public boolean isProforma(DocumentosComerciais doc) {
        return doc != null && doc.getNome_doc() != null && doc.getNome_doc().toLowerCase().contains("proforma");
    }

    public void validarConversao(String numeroCotacao, String numeroFactura, String tipoDoc,
            String condicaoPagamento, DocumentosComerciais docDestino) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Informe o numero da cotacao/proforma a converter.");
        }
        if (numeroFactura == null || numeroFactura.trim().isEmpty() || "...".equals(numeroFactura)) {
            throw new ServiceException("Gere primeiro a referencia da factura destino.");
        }
        if (tipoDoc == null || condicaoPagamento == null) {
            throw new ServiceException("Selecione o tipo de factura destino e a condicao de pagamento.");
        }
        if (docDestino == null) {
            throw new ServiceException("Documento comercial destino invalido.");
        }
        if (isProforma(docDestino)) {
            throw new ServiceException("A factura destino nao pode ser Proforma. Selecione Fatura, Fatura Recibo ou Fatura Simplificada.");
        }
    }

    public List<Item> carregarItensEditaveis(String numeroCotacao) {
        List<Item> itens = serviceItem.carregarItensCotacaoEditaveis(numeroCotacao);
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A cotacao nao possui itens para editar.");
        }
        return itens;
    }

    public boolean guardarCotacao(String numeroCotacao, List<Item> itens, Map<String, Stock> mapaStock,
            Usuario usuario) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Informe o numero da cotacao/proforma a guardar.");
        }
        List<Item> itensPreparados = serviceItemFactura.prepararItensCotacao(itens, mapaStock, numeroCotacao, usuario);
        return serviceItem.guardarItensCotacao(numeroCotacao, itensPreparados);
    }
}
