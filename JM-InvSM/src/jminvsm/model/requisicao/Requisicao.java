package jminvsm.model.requisicao;

import java.time.LocalDate;
import java.util.List;
import jminvsm.model.enums.EstadoRequisicao;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toLocalDate;

public class Requisicao {

    private int id;
    private LocalDate dataRequisicao;
    private LocalDate dataNecessidade;
    private EstadoRequisicao estado;
    private String motivo;
    private List<ItemRequisicao> itens;

    public Requisicao() {
    }

    public Requisicao(LocalDate dataRequisicao, LocalDate dataNecessidade, String estado, String motivo) {
        this.dataRequisicao = dataRequisicao;
        this.dataNecessidade = dataNecessidade;
        this.estado = EstadoRequisicao.fromValue(estado);
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataRequisicao() {
        return toDateString(dataRequisicao);
    }

    public void setDataRequisicao(String dataRequisicao) {
        this.dataRequisicao = toLocalDate(dataRequisicao);
    }

    public LocalDate getDataRequisicaoDate() {
        return dataRequisicao;
    }

    public void setDataRequisicao(LocalDate dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public String getDataNecessidade() {
        return toDateString(dataNecessidade);
    }

    public void setDataNecessidade(String dataNecessidade) {
        this.dataNecessidade = toLocalDate(dataNecessidade);
    }

    public LocalDate getDataNecessidadeDate() {
        return dataNecessidade;
    }

    public void setDataNecessidade(LocalDate dataNecessidade) {
        this.dataNecessidade = dataNecessidade;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoRequisicao.fromValue(estado);
    }

    public EstadoRequisicao getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoRequisicao estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<ItemRequisicao> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequisicao> itens) {
        this.itens = itens;
    }
}
