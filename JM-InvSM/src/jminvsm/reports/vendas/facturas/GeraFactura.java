/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.reports.vendas.facturas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import jminvsm.dao.cliente.ClienteDAO;
import jminvsm.dao.empresa.EmpresaDAO;
import jminvsm.dao.vendas.factura.FacturaDAO;
import jminvsm.model.empresa.Empresa;

/**
 *
 * @author JM-Tecnologias
 */

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;

public class GeraFactura {

    public static void main(String[] args) throws SQLException {
//        try {
//        // O resto do código para gerar o relatório...
//
////            String jasperPath = "C:\\Users\\JM-Tecnologias\\JaspersoftWorkspace\\SysFaceReports\\RelatorioFacturas.jasper";
//            String jasperPath = "C:\\Users\\JM-Tecnologias\\Documents\\NetBeansProjects\\Projeto Sistema de facturacao\\SysFaceReports\\RelatorioFacturas.jrxml";
////            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
//            JasperReport jReport = JasperCompileManager.compileReport(jasperPath);
//
//
//            List<ClienteData> detalhesCliente = new ArrayList<ClienteData>();
//            ClienteDAO x = new ClienteDAO();
////            ClienteData cliente = x.getClinteFacturaData("2024-777/2");
////            detalhesCliente.add(cliente);
//            JRBeanCollectionDataSource clienteDataSource = new JRBeanCollectionDataSource(detalhesCliente);
//
//            List<itensFacturaData> itensLista = new ArrayList<itensFacturaData>();
//            FacturaDAO fd = new FacturaDAO();
//            List<itensFacturaData> lista = fd.getItensFacturaData("2024-777/2");
//            for (itensFacturaData facturaData : lista) {
//                itensLista.add(facturaData);
//            }
//            JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(itensLista);
//
//            EmpresaDAO empData = new EmpresaDAO();
//            Empresa e = empData.getEmpresaData();
//
//            FacturaDAO fTotais = new FacturaDAO();
//            itensFacturaData ifdata = fTotais.getTotaisFacturaData("2024-777/2");
//            Map<String, Object> parametros = new HashMap<String, Object>();
////            parametros.put("Berlin Sans FB", "C:\\Users\\JM-Tecnologias\\Documents\\NetBeansProjects\\Projeto Sistema de facturacao\\SysFact\\fonts\\BRLNSR.ttf");
//            parametros.put("TABLE_DATA_SOURCE", tableDataSource);
//
//            parametros.put("SUB-TOTAL", ifdata.getSubtotal());
//            parametros.put("DESCONTO", ifdata.getDesconto());
//            parametros.put("TAXAS", ifdata.getTaxas());
//            parametros.put("TOTAL-GERAL", ifdata.getTotal_geral());
//
//            parametros.put("nomeEmpresa", e.getNome_emp() + "." + e.getRazao_emp());
//            parametros.put("nuitEmpresa", e.getNuit_emp());
//            parametros.put("enderecoEmpresa", e.printEnderecos());
//            parametros.put("contactoEmpresa", e.printContatos());
//            parametros.put("emailEmpresa", e.getContactoEmpresa().getEmail_emp());
//            parametros.put("websiteEmpresa", e.getContactoEmpresa().getWebsite_emp());
//            parametros.put("nomeBanco", e.getContaBancaria().getNome_banco());
//            parametros.put("numeroConta", e.getContaBancaria().getNumero_conta());
//            parametros.put("nibConta", e.getContaBancaria().getNib());
//            parametros.put("nuibConta", e.getContaBancaria().getNuib());
//
////            Tambem ja funciona
//            JasperPrint jPrint = JasperFillManager.fillReport(jReport, parametros, clienteDataSource);
//            JasperExportManager.exportReportToPdfFile(jPrint, "C:\\Users\\JM-Tecnologias\\Desktop\\JM-InvSM\\Modelo.pdf");
//            // Coddigo abaixo funciona
//            JasperViewer viewer = new JasperViewer(jPrint, false);
//            viewer.setTitle("InvoiceReport");
//            viewer.setVisible(true);
//
//        } catch (JRException ex) {
//            Logger.getLogger(GeraFactura.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

}
