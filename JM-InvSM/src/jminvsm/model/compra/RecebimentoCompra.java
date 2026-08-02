package jminvsm.model.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jminvsm.model.enums.EstadoRecebimentoCompra;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class RecebimentoCompra {

    private int id;
    private String numero;
    private OrdemCompra ordemCompra;
    private Fornecedor fornecedor;
    private LocalDateTime dataRecebimento;
    private String documentoFornecedor;
    private EstadoRecebimentoCompra estado;
    private BigDecimal totalRecebido = BigDecimal.ZERO;
    private String observacao;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataActualizacao;
    private List<ItemRecebimentoCompra> itens;

    public RecebimentoCompra() {
    }

    public RecebimentoCompra(String numero, OrdemCompra ordemCompra, Fornecedor fornecedor,
            LocalDateTime dataRecebimento, String estado) {
        this.numero = numero;
        this.ordemCompra = ordemCompra;
        this.fornecedor = fornecedor;
        this.dataRecebimento = dataRecebimento;
        this.estado = EstadoRecebimentoCompra.fromValue(estado);
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

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDataRecebimento() {
        return toDateTimeString(dataRecebimento);
    }

    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = toLocalDateTime(dataRecebimento);
    }

    public LocalDateTime getDataRecebimentoDateTime() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDateTime dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getDocumentoFornecedor() {
        return documentoFornecedor;
    }

    public void setDocumentoFornecedor(String documentoFornecedor) {
        this.documentoFornecedor = documentoFornecedor;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoRecebimentoCompra.fromValue(estado);
    }

    public EstadoRecebimentoCompra getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoRecebimentoCompra estado) {
        this.estado = estado;
    }

    public double getTotalRecebido() {
        return toDouble(totalRecebido);
    }

    public void setTotalRecebido(double totalRecebido) {
        this.totalRecebido = toBigDecimal(totalRecebido);
    }

    public BigDecimal getTotalRecebidoDecimal() {
        return totalRecebido;
    }

    public void setTotalRecebido(BigDecimal totalRecebido) {
        this.totalRecebido = totalRecebido != null ? totalRecebido : BigDecimal.ZERO;
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

    public List<ItemRecebimentoCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemRecebimentoCompra> itens) {
        this.itens = itens;
    }
}
