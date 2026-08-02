package jminvsm.model.enums;

public enum ModoOperacaoSistema {
    FACTURACAO("FACTURACAO"),
    FACTURACAO_STOCK("FACTURACAO_STOCK");

    private final String valor;

    ModoOperacaoSistema(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static ModoOperacaoSistema fromValue(String valor) {
        if (valor == null) {
            return FACTURACAO_STOCK;
        }
        for (ModoOperacaoSistema modo : values()) {
            if (modo.valor.equalsIgnoreCase(valor.trim())) {
                return modo;
            }
        }
        return FACTURACAO_STOCK;
    }
}
