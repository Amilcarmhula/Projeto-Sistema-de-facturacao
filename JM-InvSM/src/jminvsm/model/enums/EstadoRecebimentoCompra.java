package jminvsm.model.enums;

public enum EstadoRecebimentoCompra implements DatabaseEnum {
    RASCUNHO("Rascunho"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoRecebimentoCompra(String valor) {
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

    public static EstadoRecebimentoCompra fromValue(String value) {
        return EnumUtils.fromValue(EstadoRecebimentoCompra.class, value);
    }
}
