/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.cliente.ClienteDAO;
import jminvsm.dao.vendas.factura.FacturaDAO;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.Documentos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.ServiceException;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;
import jminvsm.service.vendas.factura.itens.ServiceItem;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceFactura {

    private final FacturaDAO facturaDao;
    private final ItemDAO itemDao;
    private final ClienteDAO clienteDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;
    private boolean estado = false;

    public ServiceFactura() throws SQLException {
        this.facturaDao = new FacturaDAO();
        this.itemDao = new ItemDAO();
        this.clienteDao = new ClienteDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void criaFactura(String numero, String dataEmissao, String dataVencimento, Double total, String condicaoPagamento,
            DocumentosComerciais doc, Integer cli, Empresa emp, Usuario user, List<Item> itens) throws SQLException {
        Cliente cliente;
        setEstado(false);
        if (numero == null || numero.isEmpty()) {
            throw new ServiceException("Tipo de factura nao selecionada!");
        }
        if (condicaoPagamento == null || condicaoPagamento.isEmpty()) {
            throw new ServiceException("Condicao de pagamento nao selecionada!");
        }

        if (cli == null || cli <= 0) {
            throw new ServiceException("Cliente nao pode ser nulo!");
        } else {
            ServiceCliente serviceCliente = new ServiceCliente();
            cliente = serviceCliente.consultaClienteByID(cli);
        }

        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("Nao existem itens na factura!");
        }

        Factura f = new Factura();
        f.setNumero_fac(numero);
        f.setData_emissao(dataEmissao);
        f.setData_vencimento(dataVencimento);
        f.setTotal(BigDecimal.valueOf(total));
//        f.setTotalPago(0);
        f.setSaldoPendente(BigDecimal.valueOf(total));
//        f.setEstado("Pendente");
        f.setCondicaoPagamento(condicaoPagamento);
        f.setDocComerciais(doc);
        f.setCliente(cliente);
        f.setEmpresa(emp);
        f.setUsuario(user);
        // Mantém os itens no agregado antes de persistir o cabeçalho para que
        // os totais e o hash fiscal sejam capturados no momento da emissão.
        f.setItens(itens);
        ServiceItem serviceItem = new ServiceItem();
        boolean modoFacturacaoStock = serviceConfiguracao.isModuloStockActivo();

        if (facturaDao.addEntity(f)) {
            if(isProforma(doc)){
                serviceItem.registaCotacao(itens);
            }else{
                serviceItem.registaItens(itens, modoFacturacaoStock);
            }
            setEstado(true);
        }

    }

    public boolean converterCotacaoEmFactura(String numeroCotacao, String numeroFactura, String dataEmissao,
            String dataVencimento, String condicaoPagamento, DocumentosComerciais docDestino,
            Integer cli, Empresa emp, Usuario user) throws SQLException {
        setEstado(false);
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Cotacao nao informada!");
        }
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new ServiceException("Numero da factura destino nao informado!");
        }
        if (docDestino == null || docDestino.getId() <= 0) {
            throw new ServiceException("Documento comercial da factura destino nao selecionado!");
        }
        if (isProforma(docDestino)) {
            throw new ServiceException("Selecione um documento final. A factura destino nao pode ser Proforma.");
        }
        if (condicaoPagamento == null || condicaoPagamento.trim().isEmpty()) {
            throw new ServiceException("Condicao de pagamento nao selecionada!");
        }

        Factura destino = new Factura();
        destino.setNumero_fac(numeroFactura);
        destino.setData_emissao(dataEmissao);
        destino.setData_vencimento(dataVencimento);
        destino.setCondicaoPagamento(condicaoPagamento);
        destino.setDocComerciais(docDestino);
        destino.setEmpresa(emp);
        destino.setUsuario(user);

        if (cli != null && cli > 0) {
            ServiceCliente serviceCliente = new ServiceCliente();
            Cliente cliente = serviceCliente.consultaClienteByID(cli);
            destino.setCliente(cliente);
        }

        boolean convertido = facturaDao.converterCotacaoEmFactura(numeroCotacao, destino);
        if (convertido) {
            setEstado(true);
        }
        return convertido;
    }

    public Factura consultaFactura(String numero) {
        return facturaDao.getFactura(numero);
    }
    
    public Factura consultaDadosFactura(String numero) {
        return facturaDao.getFacturaData(numero);
    }

    public ObservableList<Factura> exibeFactura() {
        return facturaDao.getFacturaToDashboard();
    }

    public ObservableList<Factura> exibeFacturaPorCliente(int id) {
        return facturaDao.getFacturaToPagamentos(id);
    }

    public int contaFacturas() {
        int total = facturaDao.countFactura();
        return total;
    }

    private boolean isProforma(DocumentosComerciais doc) {
        if (doc == null || doc.getNome_doc() == null) {
            return false;
        }
        return doc.getNome_doc().toLowerCase().contains("proforma");
    }

    public boolean isModoFacturacaoStock() {
        return serviceConfiguracao.isModuloStockActivo();
    }

    public boolean isModoFacturacaoSimples() {
        return serviceConfiguracao.isModoFacturacaoSimples();
    }

//    public double somaTotalFacturas(){
//        double total = facturaDao.totalPaidInvoice();
//        return total;
//    }
}
