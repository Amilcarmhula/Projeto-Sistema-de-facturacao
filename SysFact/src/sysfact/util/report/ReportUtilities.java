/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.util.report;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import sysfact.util.AlertUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class ReportUtilities {
    
    public static void genarateJasperReport(String jasperPath, Map<String, Object> mapParams, JRBeanCollectionDataSource dataSOURCE) {
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
            ButtonType btnSaveView = new ButtonType("Salvar", ButtonType.YES.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnVisualizar, btnSaveView, btnClose);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnVisualizar) {
                // Apenas exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Relatorio");
                viewer.setVisible(true);
            } else if (result.get() == btnSaveView) {
                //Salva o relatório NOMEFACTURA
                String numero = (String) mapParams.get("NOMEFACTURA");
                JasperExportManager.exportReportToPdfFile(printableReport, "C:/JM-InvSM/FACTURA-"+numero.replace("/", "-")+".pdf");
                // Exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            } else if (result.get() == btnClose) {
                System.out.println("Aqui nao salva nem visualiza; esta associado ao botao close da tela!");
            }
        } catch (JRException ex) {
            AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Informacao", "Erro ao gerar relatorio de factura: "+ex);
        }
    }
}
