package jminvsm.model.enums;

public enum EstadoStockTransito implements DatabaseEnum {
    EM_TRANSITO("Em Transito"),
    RECEBIDO("Recebido");

    private final String valor;

    EstadoStockTransito(String valor) {
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

    public static EstadoStockTransito fromValue(String value) {
        return EnumUtils.fromValue(EstadoStockTransito.class, value);
    }
}
