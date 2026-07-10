package jminvsm.model.reserva;

import java.time.LocalDateTime;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoReservaStock;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.Requisicao;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class ReservaStock {

    private int id;
    private Produto produto;
    private Armazem armazem;
    private Requisicao requisicao;
    private int quantidade;
    private EstadoReservaStock estado;
    private LocalDateTime dataReserva;

    public ReservaStock() {
    }

    public ReservaStock(Produto produto, Armazem armazem, Requisicao requisicao,
            int quantidade, String estado, LocalDateTime dataReserva) {
        this.produto = produto;
        this.armazem = armazem;
        this.requisicao = requisicao;
        this.quantidade = quantidade;
        this.estado = EstadoReservaStock.fromValue(estado);
        this.dataReserva = dataReserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoReservaStock.fromValue(estado);
    }

    public EstadoReservaStock getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoReservaStock estado) {
        this.estado = estado;
    }

    public String getDataReserva() {
        return toDateTimeString(dataReserva);
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = toLocalDateTime(dataReserva);
    }

    public LocalDateTime getDataReservaDateTime() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }
}
