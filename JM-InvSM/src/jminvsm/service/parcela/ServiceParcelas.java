/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.parcela;

import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import jminvsm.dao.parcela.ParcelaDAO;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            showErroAlert("Factura nao definida!");
            return;
        }

        Parcela p = new Parcela();
        p.setTaxaJuros(juros / 100);
//        p.setTaxaDeAtraso((juros/100)/2);
        p.setValorParcela((saldoFactura / numeroDeParcelas) * p.getTaxaJuros() + (saldoFactura / numeroDeParcelas));// O valor da parcela ainda deve ser calculada com base na taxa de juros
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
                p.setDataVencimento(dataFinalParcelas.toString());
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
                p.setDataVencimento(dataFinalParcelas.toString());
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
            p.setTaxaJuros(juros / 100);
            p.setEstado("Pendente");
            p.setTaxaDeAtraso((juros / 100) / 2);
            p.setValorParcela((saldoFactura / numeroDeParcelas) * p.getTaxaJuros() + (saldoFactura / numeroDeParcelas));// O valor da parcela ainda deve ser calculada com base na taxa de juros
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
                p.setDataVencimento(dataFinalParcelas.toString());
                p.setDataPagamento(dataFinalParcelas.toString());
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
                p.setDataVencimento(dataFinalParcelas.toString());
                p.setDataPagamento(dataFinalParcelas.toString());
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
        Parcela p = new Parcela();
        p.criarRelatorioDeParcelas(numero);
    }

}
