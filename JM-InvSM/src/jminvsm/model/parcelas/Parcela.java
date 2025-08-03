/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.parcelas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.parcela.ServiceParcelas;
import jminvsm.service.transacoes.ServiceTransacoes;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import jminvsm.util.AlertUtilities;
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
public class Parcela {

    private int id_parcela;
    private int parcelaNumero;
    private double taxaJuros;
    private double taxaDeAtraso;
    private double valorParcela;
    private double valorPagoParcela;
    private String dataVencimento;
    private String dataPagamento;
    private String estado;
    private Factura factura;
    private Usuario usuario;
    private ServiceFactura serviceFactura;
    private ServiceParcelas serviceParcela;
    private ServiceTransacoes serviceTransacao;

    public Parcela() {
    }

    public Parcela(int parcelaNumero, double taxaJuros, double taxaDeAtraso, double valorParcela, double valorPagoParcela, String dataVencimento, String dataPagamento, String estado, Factura factura, Usuario usuario) {
        this.parcelaNumero = parcelaNumero;
        this.taxaJuros = taxaJuros;
        this.taxaDeAtraso = taxaDeAtraso;
        this.valorParcela = valorParcela;
        this.valorPagoParcela = valorPagoParcela;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.estado = estado;
        this.factura = factura;
        this.usuario = usuario;
    }
    
    public void criarRelatorioDeParcelas(String numeroFactura) throws SQLException  {
        serviceFactura = new ServiceFactura();
        serviceParcela = new ServiceParcelas();
        serviceTransacao = new ServiceTransacoes();
        List<Factura> detalhesFactura = new ArrayList<Factura>();

        Factura detalhes = serviceFactura.consultaDadosFactura(numeroFactura);
        detalhesFactura.add(detalhes);
        JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);

        // Configurar o DataSource para os itens
        JRBeanCollectionDataSource parcelaDATASOURCE = new JRBeanCollectionDataSource(serviceParcela.consultaParcelas(numeroFactura));
        JRBeanCollectionDataSource transacaoDATASOURCE = new JRBeanCollectionDataSource(serviceTransacao.consultaTransacoes(numeroFactura));

        // Passar parâmetros para o relatório
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("TABLE_DATA_SOURCE", parcelaDATASOURCE);
        parametros.put("TABLE_DATA_SOURCE_TRANS", transacaoDATASOURCE);
        parametros.put("NOMEFACTURA", numeroFactura);

        String path = "C:\\JM-InvSM\\compiled\\ExtratoParcelas.jasper";
//        if (numeroFactura.endsWith("/1") || numeroFactura.endsWith("/2")) {
//            path = "C:\\JM-InvSM\\compiled\\Factura.jasper";
//        } else if (numeroFactura.endsWith("/3")) {
//            path = "C:\\JM-InvSM\\compiled\\FacturaRecibo.jasper";
//        } else {
//            path = "C:\\JM-InvSM\\compiled\\FacturaSimplificada.jasper";
//        }

        try {
            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(path);

            // Preencher o relatório
            JasperPrint printableReport = JasperFillManager.fillReport(jReport, parametros, facturaDetalhesDATASOURCE);
            /*
            Configura escolha decisao de escolha de visualizacao e impressao ou salvar documento
             */
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Opcoes de relatorio.");
            dialog.setContentText("Deseja salvar ou visualizar o relatorio de parcelamento?");
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
                String numero = (String) parametros.get("NOMEFACTURA");
                JasperExportManager.exportReportToPdfFile(printableReport, "C:/JM-InvSM/EXTRATO-PARCELAS-" + numero.replace("/", "-") + ".pdf");
                // Exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            } else if (result.get() == btnClose) {
                System.out.println("Aqui nao salva nem visualiza; esta associado ao botao close da tela!");
            }
        } catch (JRException ex) {
            AlertUtilities.showDialog("Erro", "Erro ao gerar relatorio: " + ex);
        }

    }

    public int getId_parcela() {
        return id_parcela;
    }

    public void setId_parcela(int id_parcela) {
        this.id_parcela = id_parcela;
    }

    public int getParcelaNumero() {
        return parcelaNumero;
    }

    public void setParcelaNumero(int parcelaNumero) {
        this.parcelaNumero = parcelaNumero;
    }

    public double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public double getTaxaDeAtraso() {
        return taxaDeAtraso;
    }

    public void setTaxaDeAtraso(double taxaDeAtraso) {
        this.taxaDeAtraso = taxaDeAtraso;
    }

    public double getValorParcela() {
        valorParcela = Double.parseDouble(String.format("%.2f", valorParcela));
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getValorPagoParcela() {
        return valorPagoParcela;
    }

    public void setValorPagoParcela(double valorPagoParcela) {
        this.valorPagoParcela = valorPagoParcela;
    }
    
    

}
