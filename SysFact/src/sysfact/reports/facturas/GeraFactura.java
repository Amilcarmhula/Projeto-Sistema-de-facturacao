/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.reports.facturas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.model.cliente.Cliente;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class GeraFactura {

    public static void main(String[] args) throws SQLException {
//        JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\JM-Tecnologias\\JaspersoftWorkspace\\SysFaceReports\\RelatorioFacturas.jrxml");
//        
//            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
//            ClienteDAO x = new ClienteDAO();
//            ClienteData cliente = x.getClinteFacturaData("2024-262/3");
//            
//            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, Conexao.getConexao());
//            
//            JasperViewer viewer = new JasperViewer(jPrint, false);
//            
//            viewer.setTitle("MarcoMan Report");
//            viewer.show();
        try {

            String jasperPath = "C:\\Users\\JM-Tecnologias\\JaspersoftWorkspace\\SysFaceReports\\RelatorioFacturas.jasper";
            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);

            List<ClienteData> detalhesCliente = new ArrayList<ClienteData>();
            ClienteDAO x = new ClienteDAO();
            ClienteData cliente = x.getClinteFacturaData("2024-262/3");
            detalhesCliente.add(cliente);
            JRBeanCollectionDataSource clienteDataSource = new JRBeanCollectionDataSource(detalhesCliente);
            JRBeanCollectionDataSource clienteDataSource2 = new JRBeanCollectionDataSource(detalhesCliente);

            List<itensFacturaData> itensLista = new ArrayList<itensFacturaData>();
            FacturaDAO fd = new FacturaDAO();
            List<itensFacturaData> lista = fd.getItensFacturaData("2024-262/3");
            for (itensFacturaData facturaData : lista) {
                itensLista.add(facturaData);
            }
            JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(itensLista);

            FacturaDAO fTotais = new FacturaDAO();
            itensFacturaData ifdata = fTotais.getTotaisFacturaData("2024-262/3");
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("TABLE_DATA_SOURCE", tableDataSource);
            
            parametros.put("SUB-TOTAL", ifdata.getSubtotal());
            parametros.put("DESCONTO", ifdata.getDesconto());
            parametros.put("TAXAS", ifdata.getTaxas());
            parametros.put("TOTAL-GERAL", ifdata.getTotal_geral());
            

            JasperPrint jPrint = JasperFillManager.fillReport(jReport, parametros, clienteDataSource);
            
            JasperViewer viewer = new JasperViewer(jPrint, false);
//            
            viewer.setTitle("InvoiceReport");
            viewer.show();

        } catch (JRException ex) {
            Logger.getLogger(GeraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
