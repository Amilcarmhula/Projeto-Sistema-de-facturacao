package jminvsm.model.enums;

public enum TipoPicking implements DatabaseEnum {
    TRANSFERENCIA("Transferencia"),
    VENDA("Venda"),
    REQUISICAO("Requisicao");

    private final String valor;

    TipoPicking(String valor) {
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

    public static TipoPicking fromValue(String value) {
        return EnumUtils.fromValue(TipoPicking.class, value);
    }
}
