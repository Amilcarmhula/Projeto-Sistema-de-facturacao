package jminvsm.model.enums;

public enum EstadoParcela implements DatabaseEnum {
    PENDENTE("Pendente"),
    PAGA("Paga"),
    PAGO("Pago"),
    VENCIDA("Vencida"),
    ATRASADA("Atrasada"),
    CANCELADA("Cancelada"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoParcela(String valor) {
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

    public static EstadoParcela fromValue(String value) {
        return EnumUtils.fromValue(EstadoParcela.class, value);
    }
}
