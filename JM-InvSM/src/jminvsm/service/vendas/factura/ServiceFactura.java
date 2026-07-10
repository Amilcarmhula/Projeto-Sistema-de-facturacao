/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
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
import jminvsm.util.AlertUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceFactura {

    private final FacturaDAO facturaDao;
    private final ItemDAO itemDao;
    private final ClienteDAO clienteDao;
    private boolean estado = false;

    public ServiceFactura() throws SQLException {
        this.facturaDao = new FacturaDAO();
        this.itemDao = new ItemDAO();
        this.clienteDao = new ClienteDAO();
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
        f.setTotal(BigDecimal.valueOf(total));
//        f.setTotalPago(0);
        f.setSaldoPendente(BigDecimal.valueOf(total));
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
        ServiceItem serviceItens = new ServiceItem();
        List<Factura> detalhesFactura = new ArrayList<Factura>();

        Factura detalhes = consultaDadosFactura(numero);
        detalhesFactura.add(detalhes);
        JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);
        JRBeanCollectionDataSource itemDATASOURCE = new JRBeanCollectionDataSource(serviceItens.consultaItensFactura(numero));

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("TABLE_DATA_SOURCE", itemDATASOURCE);
        parametros.put("NOMEFACTURA", numero);

        String path;
        if (numero.endsWith("/1") || numero.endsWith("/2")) {
            path = "C:\\JM-InvSM\\compiled\\Factura.jasper";
        } else if (numero.endsWith("/3")) {
            path = "C:\\JM-InvSM\\compiled\\FacturaRecibo.jasper";
        } else {
            path = "C:\\JM-InvSM\\compiled\\FacturaSimplificada.jasper";
        }

        try {
            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint printableReport = JasperFillManager.fillReport(jReport, parametros, facturaDetalhesDATASOURCE);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Opcoes de relatorio.");
            dialog.setContentText("Deseja salvar ou visualizar a Factura?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnVisualizar = new ButtonType("Visualizar", ButtonType.OK.getButtonData());
            ButtonType btnClose = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
            ButtonType btnSaveView = new ButtonType("Salvar", ButtonType.YES.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnVisualizar, btnSaveView, btnClose);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == btnVisualizar) {
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Relatorio");
                viewer.setVisible(true);
            } else if (result.isPresent() && result.get() == btnSaveView) {
                String nomeFactura = (String) parametros.get("NOMEFACTURA");
                JasperExportManager.exportReportToPdfFile(printableReport, "C:/JM-InvSM/FACTURA-" + nomeFactura.replace("/", "-") + ".pdf");
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            }
        } catch (JRException ex) {
            AlertUtilities.showDialog("Erro", "Erro ao gerar relatorio de factura: " + ex);
        }
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
