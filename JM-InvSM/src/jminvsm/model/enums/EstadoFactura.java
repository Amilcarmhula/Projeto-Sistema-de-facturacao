package jminvsm.model.enums;

public enum EstadoFactura implements DatabaseEnum {
    PENDENTE("Pendente"),
    PAGA("Paga"),
    PAGO("Pago"),
    PARCIAL("Parcial"),
    CANCELADA("Cancelada"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoFactura(String valor) {
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

    public static EstadoFactura fromValue(String value) {
        return EnumUtils.fromValue(EstadoFactura.class, value);
    }
}
