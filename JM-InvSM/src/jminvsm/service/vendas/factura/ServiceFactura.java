/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.cliente.ClienteDAO;
import jminvsm.dao.vendas.factura.FacturaDAO;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceFactura {

    private final FacturaDAO facturaDao;
    private final ItemDAO itemDao;
    private final ClienteDAO clienteDao;
    private boolean estado = false;
    private Factura factura;

    public ServiceFactura() throws SQLException {
        this.facturaDao = new FacturaDAO();
        this.itemDao = new ItemDAO();
        this.clienteDao = new ClienteDAO();
        this.factura = new Factura();
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
            showErroAlert( "Tipo de factura nao selecionada!");
            return;
        }
        if (condicaoPagamento == null || condicaoPagamento.isEmpty()) {
            showErroAlert( "Condicao de pagamento nao selecionada!");
            return;
        }

        if (cli == null || cli <= 0) {
            showErroAlert( "Cliente nao pode ser nulo!");
            return;
        } else {
            System.out.println("id: "+cli);
            ServiceCliente serviceCliente = new ServiceCliente();
            cliente = serviceCliente.consultaClienteByID(cli);
        }

        if (itens == null || itens.isEmpty()) {
            showErroAlert( "Nao existem itens na factura!");
            return;
        }

        Factura f = new Factura();
        f.setNumero_fac(numero);
        f.setData_emissao(dataEmissao);
        f.setData_vencimento(dataVencimento);
        f.setTotal(total);
//        f.setTotalPago(0);
        f.setSaldoPendente(total);
//        f.setEstado("Pendente");
        f.setCondicaoPagamento(condicaoPagamento);
        f.setDocComerciais(doc);
        f.setCliente(cliente);
        f.setEmpresa(emp);
        f.setUsuario(user);
        ServiceItem serviceItem = new ServiceItem();

        if (facturaDao.addEntity(f)) {
            if(doc.getNome_doc().equals("Fatura Proforma")){
                serviceItem.registaCotacao(itens);
            }else{
                serviceItem.registaItens(itens);
            }
            setEstado(true);
            showSuccessAlert( "Factura adicionado com sucesso!");
        }

    }

    public void geraRelatorio(String numero) throws SQLException {
        factura.criarFactura(numero);
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

//    public double somaTotalFacturas(){
//        double total = facturaDao.totalPaidInvoice();
//        return total;
//    }
}
