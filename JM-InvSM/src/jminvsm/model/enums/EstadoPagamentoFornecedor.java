package jminvsm.model.enums;

public enum EstadoPagamentoFornecedor implements DatabaseEnum {
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoPagamentoFornecedor(String valor) {
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

    public static EstadoPagamentoFornecedor fromValue(String value) {
        return EnumUtils.fromValue(EstadoPagamentoFornecedor.class, value);
    }
}
