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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.empresa.EmpresaDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.model.empresa.Empresa;

/**
 *
 * @author JM-Tecnologias
 */
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;

public class GeraFacturaV2 {

    public static void main(String[] args) throws SQLException {
        try {
            String jasperPath = "C:\\Users\\JM-Tecnologias\\JaspersoftWorkspace\\SysFaceReports\\RelatorioFacturas.jrxml";
            JasperReport jReport = JasperCompileManager.compileReport(jasperPath);

            // Recuperar itens da fatura usando o número da fatura
            String numeroFactura = "2024-1010/2"; // Número da fatura

            List<Factura> detalhesFactura = new ArrayList<Factura>();
            Factura detalhes = new FacturaDAO().getFacturaData(numeroFactura);
            detalhesFactura.add(detalhes);
            JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);

            ItemDAO x = new ItemDAO();
            List<Item> detalhesItens = x.getItensData(numeroFactura);

            // Configurar o DataSource para os itens
            JRBeanCollectionDataSource itemDataSource = new JRBeanCollectionDataSource(detalhesItens);

            // Passar parâmetros para o relatório
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("TABLE_DATA_SOURCE", itemDataSource); // Isso pode ser removido se não necessário

            // Preencher o relatório
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, parametros, facturaDetalhesDATASOURCE);
            JasperExportManager.exportReportToPdfFile(jPrint, "C:\\Users\\JM-Tecnologias\\Desktop\\JM-InvSM\\Modelo.pdf");

            // Exibir o relatório
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Factura");
            viewer.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(GeraFacturaV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
