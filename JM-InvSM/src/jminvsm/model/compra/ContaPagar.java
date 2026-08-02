package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jminvsm.model.enums.EstadoContaPagar;
import jminvsm.model.fornecedor.Fornecedor;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class ContaPagar {

    private int id;
    private FacturaFornecedor facturaFornecedor;
    private Fornecedor fornecedor;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private BigDecimal valorOriginal = BigDecimal.ZERO;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private BigDecimal saldoPendente = BigDecimal.ZERO;
    private EstadoContaPagar estado;
    private String observacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataActualizacao;

    public ContaPagar() {
    }

    public ContaPagar(FacturaFornecedor facturaFornecedor, Fornecedor fornecedor,
            LocalDate dataEmissao, LocalDate dataVencimento,
            BigDecimal valorOriginal, String estado) {
        this.facturaFornecedor = facturaFornecedor;
        this.fornecedor = fornecedor;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        setValorOriginal(valorOriginal);
        this.estado = EstadoContaPagar.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FacturaFornecedor getFacturaFornecedor() {
        return facturaFornecedor;
    }

    public void setFacturaFornecedor(FacturaFornecedor facturaFornecedor) {
        this.facturaFornecedor = facturaFornecedor;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDataEmissao() {
        return toDateString(dataEmissao);
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = toLocalDate(dataEmissao);
    }

    public LocalDate getDataEmissaoDate() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataVencimento() {
        return toDateString(dataVencimento);
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = toLocalDate(dataVencimento);
    }

    public LocalDate getDataVencimentoDate() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public double getValorOriginal() {
        return toDouble(valorOriginal);
    }

    public void setValorOriginal(double valorOriginal) {
        this.valorOriginal = toBigDecimal(valorOriginal);
    }

    public BigDecimal getValorOriginalDecimal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal != null ? valorOriginal : BigDecimal.ZERO;
    }

    public double getValorPago() {
        return toDouble(valorPago);
    }

    public void setValorPago(double valorPago) {
        this.valorPago = toBigDecimal(valorPago);
    }

    public BigDecimal getValorPagoDecimal() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago != null ? valorPago : BigDecimal.ZERO;
    }

    public double getSaldoPendente() {
        return toDouble(saldoPendente);
    }

    public void setSaldoPendente(double saldoPendente) {
        this.saldoPendente = toBigDecimal(saldoPendente);
    }

    public BigDecimal getSaldoPendenteDecimal() {
        return saldoPendente;
    }

    public void setSaldoPendente(BigDecimal saldoPendente) {
        this.saldoPendente = saldoPendente != null ? saldoPendente : BigDecimal.ZERO;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoContaPagar.fromValue(estado);
    }

    public EstadoContaPagar getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoContaPagar estado) {
        this.estado = estado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataCriacao() {
        return toDateTimeString(dataCriacao);
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = toLocalDateTime(dataCriacao);
    }

    public LocalDateTime getDataCriacaoDateTime() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataActualizacao() {
        return toDateTimeString(dataActualizacao);
    }

    public void setDataActualizacao(String dataActualizacao) {
        this.dataActualizacao = toLocalDateTime(dataActualizacao);
    }

    public LocalDateTime getDataActualizacaoDateTime() {
        return dataActualizacao;
    }

    public void setDataActualizacao(LocalDateTime dataActualizacao) {
        this.dataActualizacao = dataActualizacao;
    }
}
