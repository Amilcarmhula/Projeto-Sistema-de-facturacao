/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.parcela;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.parcela.ParcelaDAO;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.service.ServiceException;

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
        setOpsSuccess(false);
        if (numeroDeParcelas == null || numeroDeParcelas <= 0) {
            throw new ServiceException("Numero de parcelas nao informado!");
        }
        if (intervaloDePagamentos == null || intervaloDePagamentos <= 0) {
            throw new ServiceException("Intervalo de pagamento das parcelas nao informado!");
        }
        if (juros == null) {
            throw new ServiceException("Taxa de juros nao informada!");
        }
        if (saldoFactura == null) {
            throw new ServiceException("Saldo da factura nao informado!");
        }
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new ServiceException("Factura nao definida!");
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
    }

    public ObservableList<Parcela> simulaParcelas(Integer numeroDeParcelas, Integer intervaloDePagamentos, Double juros, Double saldoFactura,
            String numeroFactura, Usuario user) {
        ObservableList<Parcela> lista = FXCollections.observableArrayList();
        if (numeroDeParcelas == null || numeroDeParcelas <= 0) {
            throw new ServiceException("Numero de parcelas nao informado!");
        }
        if (intervaloDePagamentos == null || intervaloDePagamentos <= 0) {
            throw new ServiceException("Intervalo de pagamento das parcelas nao informado!");
        }

        if (juros == null) {
            throw new ServiceException("Taxa de juros nao informada!");
        }
        if (saldoFactura == null) {
            throw new ServiceException("Saldo da factura nao informado!");
        }
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new ServiceException("Factura nao definida!");
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

}
