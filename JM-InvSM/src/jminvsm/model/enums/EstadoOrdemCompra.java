package jminvsm.model.enums;

public enum EstadoOrdemCompra implements DatabaseEnum {
    RASCUNHO("Rascunho"),
    SUBMETIDA("Submetida"),
    APROVADA("Aprovada"),
    ENVIADA("Enviada"),
    PARCIALMENTE_RECEBIDA("ParcialmenteRecebida"),
    RECEBIDA("Recebida"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoOrdemCompra(String valor) {
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

    public static EstadoOrdemCompra fromValue(String value) {
        return EnumUtils.fromValue(EstadoOrdemCompra.class, value);
    }
}
