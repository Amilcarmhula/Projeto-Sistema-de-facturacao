package jminvsm.model.picking;

import java.time.LocalDateTime;
import jminvsm.model.enums.EstadoPicking;
import jminvsm.model.enums.TipoPicking;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toDateTimeString;
import static jminvsm.model.util.ModelConverters.toLocalDateTime;

public class Picking {

    private int id;
    private TipoPicking tipo;
    private EstadoPicking estado;
    private Usuario operador;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Requisicao requisicao;

    public Picking() {
    }

    public Picking(String tipo, String estado, Usuario operador, LocalDateTime dataInicio,
            LocalDateTime dataFim, Requisicao requisicao) {
        this.tipo = TipoPicking.fromValue(tipo);
        this.estado = EstadoPicking.fromValue(estado);
        this.operador = operador;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.requisicao = requisicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo != null ? tipo.getValor() : null;
    }

    public void setTipo(String tipo) {
        this.tipo = TipoPicking.fromValue(tipo);
    }

    public TipoPicking getTipoEnum() {
        return tipo;
    }

    public void setTipo(TipoPicking tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoPicking.fromValue(estado);
    }

    public EstadoPicking getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoPicking estado) {
        this.estado = estado;
    }

    public Usuario getOperador() {
        return operador;
    }

    public void setOperador(Usuario operador) {
        this.operador = operador;
    }

    public String getDataInicio() {
        return toDateTimeString(dataInicio);
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = toLocalDateTime(dataInicio);
    }

    public LocalDateTime getDataInicioDateTime() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return toDateTimeString(dataFim);
    }

    public void setDataFim(String dataFim) {
        this.dataFim = toLocalDateTime(dataFim);
    }

    public LocalDateTime getDataFimDateTime() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
    }
}
