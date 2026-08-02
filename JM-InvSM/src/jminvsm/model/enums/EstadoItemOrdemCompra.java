package jminvsm.model.enums;

public enum EstadoItemOrdemCompra implements DatabaseEnum {
    PENDENTE("Pendente"),
    PARCIALMENTE_RECEBIDO("ParcialmenteRecebido"),
    RECEBIDO("Recebido"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoItemOrdemCompra(String valor) {
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

    public static EstadoItemOrdemCompra fromValue(String value) {
        return EnumUtils.fromValue(EstadoItemOrdemCompra.class, value);
    }
}
