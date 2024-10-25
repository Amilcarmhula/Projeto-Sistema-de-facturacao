/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.vendas.factura;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.empresa.Empresa;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.documetos.DocumentosComerciais;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.service.cliente.ServiceCliente;
import sysfact.service.vendas.factura.itens.ServiceItem;
import static sysfact.util.AlertUtilities.showDialog;
import static sysfact.util.AlertUtilities.showErroNotification;

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

    public void criaFactura(String numero, String dataCriacao, String dataVencimento, Double total,
            DocumentosComerciais doc, Integer cli, Empresa emp, Usuario user, List<Item> itens) throws SQLException {
        Cliente cliente;
        if (numero == null || numero.isEmpty()) {
            showErroNotification("Erro", "Tipo de factura nao selecionada!");
            return;
        }

        if (cli == null) {
            showErroNotification("Erro", "Cliente nao pode ser nulo!");
            return;
        } else {
            ServiceCliente serviceCliente = new ServiceCliente(clienteDao);
            cliente = serviceCliente.getClienteByID(cli);
        }

        if (itens == null || itens.isEmpty()) {
            showErroNotification("Erro", "Nao existem itens na factura!");
            return;
        }

        Factura f = new Factura();
        f.setNumero_fac(numero);
        f.setData_criacao(dataCriacao);
        f.setData_vencimento(dataVencimento);
        f.setTotal(total);
        f.setDocComerciais(doc);
        f.setCliente(cliente);
        f.setEmpresa(emp);
        f.setUsuario(user);
        ServiceItem serviceItem = new ServiceItem(itemDao);
        
            facturaDao.addEntity(f);
            serviceItem.registaItens(itens);
            
            setEstado(true);
    }
    
    public void geraRelatorio(String numero) throws SQLException{
        factura.criaRelatorio(numero);
    }
    
    public Factura consultaFactura(String numero) {
        return facturaDao.getFactura(numero);
    }
    
    public ObservableList<Factura> exibeFactura() {
        return facturaDao.getFacturaToDashboard();
    }
    
    public ObservableList<Factura> exibeFacturaPorCliente(int id) {
        return facturaDao.getFacturaToPagamentos(id);
    }
    
    public int contaFacturas(){
        int total = facturaDao.countFactura();
        return total;
    }
    
//    public double somaTotalFacturas(){
//        double total = facturaDao.totalPaidInvoice();
//        return total;
//    }
}
