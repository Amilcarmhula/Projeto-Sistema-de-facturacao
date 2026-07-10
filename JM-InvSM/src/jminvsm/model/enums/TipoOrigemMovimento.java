package jminvsm.model.enums;

public enum TipoOrigemMovimento implements DatabaseEnum {
    COMPRA("Compra"),
    REQUISICAO("Requisicao"),
    TRANSFERENCIA("Transferencia"),
    AJUSTE("Ajuste"),
    DEVOLUCAO("Devolucao"),
    VENDA("Venda");

    private final String valor;

    TipoOrigemMovimento(String valor) {
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

    public static TipoOrigemMovimento fromValue(String value) {
        return EnumUtils.fromValue(TipoOrigemMovimento.class, value);
    }
}
