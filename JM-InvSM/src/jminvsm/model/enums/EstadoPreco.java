package jminvsm.model.enums;

public enum EstadoPreco implements DatabaseEnum {
    ATIVO("Ativo"),
    ACTIVO("activo"),
    EXPIRADO("Expirado"),
    INATIVO("Inativo"),
    DESACTIVO("desactivo");

    private final String valor;

    EstadoPreco(String valor) {
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

    public static EstadoPreco fromValue(String value) {
        return EnumUtils.fromValue(EstadoPreco.class, value);
    }
}
