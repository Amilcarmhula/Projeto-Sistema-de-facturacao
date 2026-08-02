package jminvsm.model.enums;

public enum EstadoFacturaFornecedor implements DatabaseEnum {
    PENDENTE("Pendente"),
    PARCIALMENTE_PAGA("ParcialmentePaga"),
    PAGA("Paga"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoFacturaFornecedor(String valor) {
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

    public static EstadoFacturaFornecedor fromValue(String value) {
        return EnumUtils.fromValue(EstadoFacturaFornecedor.class, value);
    }
}
