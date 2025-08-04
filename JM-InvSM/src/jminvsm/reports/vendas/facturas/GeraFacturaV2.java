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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import jminvsm.dao.vendas.factura.FacturaDAO;

/**
 *
 * @author JM-Tecnologias
 */
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;

public class GeraFacturaV2 {
    

    public void genarateJasperReport(String jasperPath, Map<String, Object> mapParams, JRBeanCollectionDataSource dataSOURCE) {
        try {
//            String jasperPath = "C:\\Users\\JM-Tecnologias\\JaspersoftWorkspace\\SysFaceReports\\RelatorioFacturas.jrxml";
            JasperReport compiledReport = JasperCompileManager.compileReport(jasperPath);

            // Preencher o relatório
            JasperPrint printableReport = JasperFillManager.fillReport(compiledReport, mapParams, dataSOURCE);
            /*
            Configura escolha decisao de escolha de visualizacao e impressao ou salvar documento
             */
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Opcoes de relatorio.");
            dialog.setContentText("Deseja salvar ou visualizar a Factura?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Configurando o botão View/Save
            ButtonType btnVisualizar = new ButtonType("Visualizar", ButtonType.OK.getButtonData());
            ButtonType btnClose = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
            ButtonType btnSaveView = new ButtonType("Salvar&Visualizar", ButtonType.YES.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnVisualizar, btnSaveView, btnClose);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnVisualizar) {
                // Apenas exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            } else if (result.get() == btnSaveView) {
                //Salva o relatório
                JasperExportManager.exportReportToPdfFile(printableReport, "C:\\JM-InvSM\\Modelo.pdf");
                // Exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            } else if (result.get() == btnClose) {
                System.out.println("Aqui nao salva nem visualiza; esta associado ao botao close da tela!");
            }
        } catch (JRException ex) {
            Logger.getLogger(GeraFacturaV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
