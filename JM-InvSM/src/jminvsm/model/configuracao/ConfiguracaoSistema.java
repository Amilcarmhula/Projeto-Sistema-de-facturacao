package jminvsm.model.configuracao;

import jminvsm.model.enums.ModoOperacaoSistema;
import jminvsm.model.usuario.Usuario;

public class ConfiguracaoSistema {

    private int id;
    private ModoOperacaoSistema modoOperacao = ModoOperacaoSistema.FACTURACAO_STOCK;
    private boolean controlarStockGlobal = true;
    private boolean permitirVendaSemStock;
    private String observacao;
    private Usuario usuario;
    private String dataCriacao;
    private String dataActualizacao;
    private boolean deleteStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModoOperacao() {
        return modoOperacao != null ? modoOperacao.getValor() : null;
    }

    public void setModoOperacao(String modoOperacao) {
        this.modoOperacao = ModoOperacaoSistema.fromValue(modoOperacao);
    }

    public ModoOperacaoSistema getModoOperacaoEnum() {
        return modoOperacao;
    }

    public void setModoOperacao(ModoOperacaoSistema modoOperacao) {
        this.modoOperacao = modoOperacao;
    }

    public boolean isControlarStockGlobal() {
        return controlarStockGlobal;
    }

    public void setControlarStockGlobal(boolean controlarStockGlobal) {
        this.controlarStockGlobal = controlarStockGlobal;
    }

    public boolean isPermitirVendaSemStock() {
        return permitirVendaSemStock;
    }

    public void setPermitirVendaSemStock(boolean permitirVendaSemStock) {
        this.permitirVendaSemStock = permitirVendaSemStock;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataActualizacao() {
        return dataActualizacao;
    }

    public void setDataActualizacao(String dataActualizacao) {
        this.dataActualizacao = dataActualizacao;
    }

    public boolean isDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public boolean isModoFacturacaoStock() {
        return ModoOperacaoSistema.FACTURACAO_STOCK.equals(modoOperacao) && controlarStockGlobal;
    }
}
