package jminvsm.model.enums;

public enum EstadoTransferencia implements DatabaseEnum {
    PENDENTE("Pendente"),
    RESERVADO("Reservado"),
    EM_TRANSITO("Em Transito"),
    RECEBIDO("Recebido"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoTransferencia(String valor) {
        this.valor = valor;
    }

    @Override
    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    public static EstadoTransferencia fromValue(String value) {
        return EnumUtils.fromValue(EstadoTransferencia.class, value);
    }
}
