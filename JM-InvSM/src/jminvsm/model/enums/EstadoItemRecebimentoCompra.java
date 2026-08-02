package jminvsm.model.enums;

public enum EstadoItemRecebimentoCompra implements DatabaseEnum {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoItemRecebimentoCompra(String valor) {
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

    public static EstadoItemRecebimentoCompra fromValue(String value) {
        return EnumUtils.fromValue(EstadoItemRecebimentoCompra.class, value);
    }
}
