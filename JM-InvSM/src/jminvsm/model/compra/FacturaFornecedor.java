package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jminvsm.model.enums.EstadoFacturaFornecedor;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class FacturaFornecedor {

    private int id;
    private String numero;
    private Fornecedor fornecedor;
    private OrdemCompra ordemCompra;
    private RecebimentoCompra recebimentoCompra;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal imposto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private BigDecimal saldoPendente = BigDecimal.ZERO;
    private EstadoFacturaFornecedor estado;
    private String observacao;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataActualizacao;
    private List<ItemFacturaFornecedor> itens;

    public FacturaFornecedor() {
    }

    public FacturaFornecedor(String numero, Fornecedor fornecedor, LocalDate dataEmissao,
            LocalDate dataVencimento, BigDecimal subtotal, String estado) {
        this.numero = numero;
        this.fornecedor = fornecedor;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        setSubtotal(subtotal);
        this.estado = EstadoFacturaFornecedor.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    public RecebimentoCompra getRecebimentoCompra() {
        return recebimentoCompra;
    }

    public void setRecebimentoCompra(RecebimentoCompra recebimentoCompra) {
        this.recebimentoCompra = recebimentoCompra;
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

    public double getSubtotal() {
        return toDouble(subtotal);
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = toBigDecimal(subtotal);
    }

    public BigDecimal getSubtotalDecimal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    public double getDesconto() {
        return toDouble(desconto);
    }

    public void setDesconto(double desconto) {
        this.desconto = toBigDecimal(desconto);
    }

    public BigDecimal getDescontoDecimal() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto != null ? desconto : BigDecimal.ZERO;
    }

    public double getImposto() {
        return toDouble(imposto);
    }

    public void setImposto(double imposto) {
        this.imposto = toBigDecimal(imposto);
    }

    public BigDecimal getImpostoDecimal() {
        return imposto;
    }

    public void setImposto(BigDecimal imposto) {
        this.imposto = imposto != null ? imposto : BigDecimal.ZERO;
    }

    public double getTotal() {
        return toDouble(total);
    }

    public void setTotal(double total) {
        this.total = toBigDecimal(total);
    }

    public BigDecimal getTotalDecimal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total != null ? total : BigDecimal.ZERO;
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
        this.estado = EstadoFacturaFornecedor.fromValue(estado);
    }

    public EstadoFacturaFornecedor getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoFacturaFornecedor estado) {
        this.estado = estado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public List<ItemFacturaFornecedor> getItens() {
        return itens;
    }

    public void setItens(List<ItemFacturaFornecedor> itens) {
        this.itens = itens;
    }
}
