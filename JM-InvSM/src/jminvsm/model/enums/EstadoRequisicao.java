package jminvsm.model.enums;

public enum EstadoRequisicao implements DatabaseEnum {
    PENDENTE("Pendente"),
    SUBMETIDA("Submetida"),
    AGUARDANDO_APROVACAO("AguardandoAprovacao"),
    APROVADA("Aprovada"),
    EM_PICKING("EmPicking"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoRequisicao(String valor) {
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

    public static EstadoRequisicao fromValue(String value) {
        return EnumUtils.fromValue(EstadoRequisicao.class, value);
    }
}
