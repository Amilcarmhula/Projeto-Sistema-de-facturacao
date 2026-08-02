package jminvsm.model.auditoria;

public class AuditoriaRegisto {

    private long id;
    private Integer idUsuario;
    private String usuarioNome;
    private String tabela;
    private String idRegisto;
    private String operacao;
    private String colunasAlteradas;
    private String valoresAnteriores;
    private String valoresNovos;
    private String dataOperacao;
    private String origem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public String getIdRegisto() {
        return idRegisto;
    }

    public void setIdRegisto(String idRegisto) {
        this.idRegisto = idRegisto;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getColunasAlteradas() {
        return colunasAlteradas;
    }

    public void setColunasAlteradas(String colunasAlteradas) {
        this.colunasAlteradas = colunasAlteradas;
    }

    public String getValoresAnteriores() {
        return valoresAnteriores;
    }

    public void setValoresAnteriores(String valoresAnteriores) {
        this.valoresAnteriores = valoresAnteriores;
    }

    public String getValoresNovos() {
        return valoresNovos;
    }

    public void setValoresNovos(String valoresNovos) {
        this.valoresNovos = valoresNovos;
    }

    public String getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(String dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
