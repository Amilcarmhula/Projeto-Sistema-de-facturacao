package jminvsm.model.transferencia;

import java.time.LocalDateTime;
import java.util.List;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoTransferencia;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class TransferenciaStock {

    private int id;
    private Armazem armazemOrigem;
    private Armazem armazemDestino;
    private EstadoTransferencia estado;
    private LocalDateTime dataCriacao;
    private List<ItemTransferencia> itens;

    public TransferenciaStock() {
    }

    public TransferenciaStock(Armazem armazemOrigem, Armazem armazemDestino,
            String estado, LocalDateTime dataCriacao) {
        this.armazemOrigem = armazemOrigem;
        this.armazemDestino = armazemDestino;
        this.estado = EstadoTransferencia.fromValue(estado);
        this.dataCriacao = dataCriacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Armazem getArmazemOrigem() {
        return armazemOrigem;
    }

    public void setArmazemOrigem(Armazem armazemOrigem) {
        this.armazemOrigem = armazemOrigem;
    }

    public Armazem getArmazemDestino() {
        return armazemDestino;
    }

    public void setArmazemDestino(Armazem armazemDestino) {
        this.armazemDestino = armazemDestino;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoTransferencia.fromValue(estado);
    }

    public EstadoTransferencia getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoTransferencia estado) {
        this.estado = estado;
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

    public List<ItemTransferencia> getItens() {
        return itens;
    }

    public void setItens(List<ItemTransferencia> itens) {
        this.itens = itens;
    }
}
