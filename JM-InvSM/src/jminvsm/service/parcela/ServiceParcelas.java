/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.parcela;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import jminvsm.dao.parcela.ParcelaDAO;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.transacoes.ServiceTransacoes;
import jminvsm.service.vendas.factura.ServiceFactura;
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
public class ServiceParcelas {

    private final ParcelaDAO parcelaDao;
    private boolean opsSuccess;

    public ServiceParcelas() throws SQLException {
        this.parcelaDao = new ParcelaDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void regista(Integer numeroDeParcelas, Integer intervaloDePagamentos, Double juros, Double saldoFactura,
            String numeroFactura, Usuario user) {
        if (numeroDeParcelas == null || numeroDeParcelas <= 0) {
            showErroAlert("Numero de parcelas nao informado!");
            return;
        }
//        if (intervaloDePagamentos == null || intervaloDePagamentos <= 0) {
//            showErroAlert("Erro", "Intervalo de pagamento das parcelas nao informado!");
//            return;
//        }
        if (juros == null) {
            showErroAlert("Taxa de juros nao informado!");
            return;
        }
        if (saldoFactura == null) {
            showErroAlert("Saldo da factura nao informado!");
            return;
        }
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroAlert("Factura nao definida!");
            return;
        }

        Parcela p = new Parcela();
        BigDecimal taxaJuros = BigDecimal.valueOf(juros).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal valorBaseParcela = BigDecimal.valueOf(saldoFactura).divide(BigDecimal.valueOf(numeroDeParcelas), 10, RoundingMode.HALF_UP);
        BigDecimal valorParcela = valorBaseParcela.add(valorBaseParcela.multiply(taxaJuros));
        p.setTaxaJuros(taxaJuros);
//        p.setTaxaDeAtraso((juros/100)/2);
        p.setValorParcela(valorParcela);
        Factura f = new Factura();
        f.setNumero_fac(numeroFactura);
        p.setFactura(f);
        p.setUsuario(user);

        LocalDate dataActual = LocalDate.now();

        LocalDate dataFinalParcelas = null;
        for (int i = 1; i <= numeroDeParcelas; i++) {
            setOpsSuccess(false);
            p.setParcelaNumero(i);

            if (i == 1) {
                dataFinalParcelas = dataActual.plusDays(i * intervaloDePagamentos);
                // actualiza a data actual para a data de vencimento da primeira paracela para iniciar a contagem em funcao dos intervalos
                dataActual = dataActual.plusDays(i * intervaloDePagamentos);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataActual.plusDays(2);
                }
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataActual.plusDays(1);
                }
                p.setDataVencimento(dataFinalParcelas);
            } else {
                //adiciona o intervalo de dias de parcelamento para as parcelas subsequentes
                dataFinalParcelas = dataActual.plusDays((i - 1) * intervaloDePagamentos);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(2);
                }
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(1);
                }
                p.setDataVencimento(dataFinalParcelas);
            }
            if (parcelaDao.addEntity(p)) {
                setOpsSuccess(true);
            }
        }
        if (isOpsSuccess()) {
            showSuccessAlert("Parcelas Geradas com sucesso!");
        }
    }

    public ObservableList<Parcela> simulaParcelas(Integer numeroDeParcelas, Integer intervaloDePagamentos, Double juros, Double saldoFactura,
            String numeroFactura, Usuario user) {
        ObservableList<Parcela> lista = FXCollections.observableArrayList();
        if (numeroDeParcelas == null || numeroDeParcelas <= 0) {
            showErroAlert("Numero de parcelas nao informado!");
            return null;
        }

        if (juros == null) {
            showErroAlert("Taxa de juros nao informado!");
            return null;
        }
        if (saldoFactura == null) {
            showErroAlert("Saldo da factura nao informado!");
            return null;
        }
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroAlert("Factura nao definida!");
            return null;
        }

        Parcela p = null;

        LocalDate dataActual = LocalDate.now();

        LocalDate dataFinalParcelas = null;
        for (int i = 1; i <= numeroDeParcelas; i++) {
            setOpsSuccess(false);
            p = new Parcela();
            BigDecimal taxaJuros = BigDecimal.valueOf(juros).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            BigDecimal valorBaseParcela = BigDecimal.valueOf(saldoFactura).divide(BigDecimal.valueOf(numeroDeParcelas), 10, RoundingMode.HALF_UP);
            BigDecimal valorParcela = valorBaseParcela.add(valorBaseParcela.multiply(taxaJuros));
            p.setTaxaJuros(taxaJuros);
            p.setEstado("Pendente");
            p.setTaxaDeAtraso(taxaJuros.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP));
            p.setValorParcela(valorParcela);
            Factura f = new Factura();
            f.setNumero_fac(numeroFactura);
            p.setFactura(f);
            p.setUsuario(user);
            p.setParcelaNumero(i);

            if (i == 1) {
                dataFinalParcelas = dataActual.plusDays(i * intervaloDePagamentos);
                // actualiza a data actual para a data de vencimento da primeira paracela para iniciar a contagem em funcao dos intervalos
                dataActual = dataActual.plusDays(i * intervaloDePagamentos);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataActual.plusDays(2);
                }
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataActual.plusDays(1);
                }
                p.setDataVencimento(dataFinalParcelas);
                p.setDataPagamento(dataFinalParcelas);
            } else {
                //adiciona o intervalo de dias de parcelamento para as parcelas subsequentes
                dataFinalParcelas = dataActual.plusDays((i - 1) * intervaloDePagamentos);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(2);
                }
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(1);
                }
                p.setDataVencimento(dataFinalParcelas);
                p.setDataPagamento(dataFinalParcelas);
            }
            lista.add(p);

        }
        setOpsSuccess(true);
        return lista;
    }

    public ObservableList<Parcela> consultaParcelas(String fk_numero) {
        return parcelaDao.getEntities(fk_numero);
    }
    public void geraRelatorio(String numero) throws SQLException{
        ServiceFactura serviceFactura = new ServiceFactura();
        ServiceTransacoes serviceTransacao = new ServiceTransacoes();
        List<Factura> detalhesFactura = new ArrayList<Factura>();

        Factura detalhes = serviceFactura.consultaDadosFactura(numero);
        detalhesFactura.add(detalhes);
        JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);
        JRBeanCollectionDataSource parcelaDATASOURCE = new JRBeanCollectionDataSource(consultaParcelas(numero));
        JRBeanCollectionDataSource transacaoDATASOURCE = new JRBeanCollectionDataSource(serviceTransacao.consultaTransacoes(numero));

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("TABLE_DATA_SOURCE", parcelaDATASOURCE);
        parametros.put("TABLE_DATA_SOURCE_TRANS", transacaoDATASOURCE);
        parametros.put("NOMEFACTURA", numero);

        String path = "C:\\JM-InvSM\\compiled\\ExtratoParcelas.jasper";

        try {
            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint printableReport = JasperFillManager.fillReport(jReport, parametros, facturaDetalhesDATASOURCE);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Opcoes de relatorio.");
            dialog.setContentText("Deseja salvar ou visualizar o relatorio de parcelamento?");
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
                JasperExportManager.exportReportToPdfFile(printableReport, "C:/JM-InvSM/EXTRATO-PARCELAS-" + nomeFactura.replace("/", "-") + ".pdf");
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            }
        } catch (JRException ex) {
            AlertUtilities.showDialog("Erro", "Erro ao gerar relatorio: " + ex);
        }
    }

}
