package jminvsm.model.enums;

public enum EstadoContaPagar implements DatabaseEnum {
    ABERTA("Aberta"),
    PARCIAL("Parcial"),
    LIQUIDADA("Liquidada"),
    VENCIDA("Vencida"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoContaPagar(String valor) {
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

    public static EstadoContaPagar fromValue(String value) {
        return EnumUtils.fromValue(EstadoContaPagar.class, value);
    }
}
