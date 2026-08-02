package jminvsm.model.enums;

public enum OrigemPagamento implements DatabaseEnum {
    CAIXA("Caixa"),
    BANCO("Banco");

    private final String valor;

    OrigemPagamento(String valor) {
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

    public static OrigemPagamento fromValue(String value) {
        return EnumUtils.fromValue(OrigemPagamento.class, value);
    }
}
