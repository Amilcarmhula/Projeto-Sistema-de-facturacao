package jminvsm.model.enums;

public enum TipoTransacao implements DatabaseEnum {
    ENTRADA("Entrada"),
    SAIDA("Saida");

    private final String valor;

    TipoTransacao(String valor) {
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

    public static TipoTransacao fromValue(String value) {
        return EnumUtils.fromValue(TipoTransacao.class, value);
    }
}
