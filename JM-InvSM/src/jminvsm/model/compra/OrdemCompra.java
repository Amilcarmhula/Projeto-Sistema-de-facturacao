package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jminvsm.model.enums.EstadoOrdemCompra;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class OrdemCompra {

    private int id;
    private String numero;
    private Requisicao requisicao;
    private Fornecedor fornecedor;
    private LocalDate dataEmissao;
    private LocalDate dataPrevistaEntrega;
    private EstadoOrdemCompra estado;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal imposto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private String observacao;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataActualizacao;
    private List<ItemOrdemCompra> itens;

    public OrdemCompra() {
    }

    public OrdemCompra(String numero, Requisicao requisicao, Fornecedor fornecedor,
            LocalDate dataEmissao, LocalDate dataPrevistaEntrega, String estado) {
        this.numero = numero;
        this.requisicao = requisicao;
        this.fornecedor = fornecedor;
        this.dataEmissao = dataEmissao;
        this.dataPrevistaEntrega = dataPrevistaEntrega;
        this.estado = EstadoOrdemCompra.fromValue(estado);
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

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
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

    public String getDataPrevistaEntrega() {
        return toDateString(dataPrevistaEntrega);
    }

    public void setDataPrevistaEntrega(String dataPrevistaEntrega) {
        this.dataPrevistaEntrega = toLocalDate(dataPrevistaEntrega);
    }

    public LocalDate getDataPrevistaEntregaDate() {
        return dataPrevistaEntrega;
    }

    public void setDataPrevistaEntrega(LocalDate dataPrevistaEntrega) {
        this.dataPrevistaEntrega = dataPrevistaEntrega;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoOrdemCompra.fromValue(estado);
    }

    public EstadoOrdemCompra getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoOrdemCompra estado) {
        this.estado = estado;
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

    public List<ItemOrdemCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrdemCompra> itens) {
        this.itens = itens;
    }
}
