package jminvsm.model.enums;

public enum EstadoLote implements DatabaseEnum {
    ATIVO("Ativo"),
    ESGOTADO("Esgotado");

    private final String valor;

    EstadoLote(String valor) {
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

    public static EstadoLote fromValue(String value) {
        return EnumUtils.fromValue(EstadoLote.class, value);
    }
}
