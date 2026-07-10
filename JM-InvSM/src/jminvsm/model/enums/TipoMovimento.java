package jminvsm.model.enums;

public enum TipoMovimento implements DatabaseEnum {
    ENTRADA("Entrada"),
    SAIDA("Saida");

    private final String valor;

    TipoMovimento(String valor) {
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

    public static TipoMovimento fromValue(String value) {
        return EnumUtils.fromValue(TipoMovimento.class, value);
    }
}
