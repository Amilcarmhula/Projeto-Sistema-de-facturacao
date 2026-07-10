package jminvsm.model.enums;

public enum EstadoPicking implements DatabaseEnum {
    AGUARDANDO("Aguardando"),
    SEPARANDO("Separando"),
    SEPARADO("Separado"),
    CONFIRMADO("Confirmado");

    private final String valor;

    EstadoPicking(String valor) {
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

    public static EstadoPicking fromValue(String value) {
        return EnumUtils.fromValue(EstadoPicking.class, value);
    }
}
