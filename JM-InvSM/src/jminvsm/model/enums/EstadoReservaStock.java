package jminvsm.model.enums;

public enum EstadoReservaStock implements DatabaseEnum {
    RESERVADO("Reservado"),
    LIBERADO("Liberado"),
    CONSUMIDO("Consumido"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoReservaStock(String valor) {
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

    public static EstadoReservaStock fromValue(String value) {
        return EnumUtils.fromValue(EstadoReservaStock.class, value);
    }
}
