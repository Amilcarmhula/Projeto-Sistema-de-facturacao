package jminvsm.service.configuracao;

import jminvsm.dao.configuracao.ConfiguracaoSistemaDAO;
import jminvsm.model.configuracao.ConfiguracaoSistema;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

public class ServiceConfiguracaoSistema {

    private final ConfiguracaoSistemaDAO configuracaoDao;

    public ServiceConfiguracaoSistema() {
        this.configuracaoDao = new ConfiguracaoSistemaDAO();
    }

    public ConfiguracaoSistema getConfiguracaoActual() {
        return configuracaoDao.getConfiguracaoActual();
    }

    public boolean isModoFacturacaoStock() {
        return getConfiguracaoActual().isModoFacturacaoStock();
    }

    public boolean deveControlarStock(Produto produto) {
        if (produto == null) {
            return false;
        }
        ConfiguracaoSistema configuracao = getConfiguracaoActual();
        return configuracao.isModoFacturacaoStock() && produto.isControle_stock();
    }

    public boolean podeVenderSemStock() {
        return getConfiguracaoActual().isPermitirVendaSemStock();
    }

    public boolean isModoFacturacaoSimples() {
        ConfiguracaoSistema configuracao = getConfiguracaoActual();
        return configuracao != null && "FACTURACAO".equalsIgnoreCase(configuracao.getModoOperacao());
    }

    public boolean isModuloStockActivo() {
        ConfiguracaoSistema configuracao = getConfiguracaoActual();
        return configuracao != null && configuracao.isModoFacturacaoStock();
    }

    public boolean validarModuloStockActivo(String nomeOperacao) {
        return isModuloStockActivo();
    }

    public void exigirModuloStockActivo(String nomeOperacao) {
        if (isModuloStockActivo()) {
            return;
        }
        String operacao = nomeOperacao == null || nomeOperacao.trim().isEmpty()
                ? "Esta operacao" : nomeOperacao.trim();
        throw new ServiceException(operacao + " esta disponivel apenas no modo FACTURACAO_STOCK.");
    }

    public boolean actualizarModo(String modoOperacao, boolean controlarStockGlobal,
            boolean permitirVendaSemStock, String observacao, Usuario usuario) {
        if (modoOperacao == null || modoOperacao.trim().isEmpty()) {
            throw new ServiceException("Modo de operacao nao informado!");
        }

        ConfiguracaoSistema configuracao = new ConfiguracaoSistema();
        configuracao.setId(1);
        configuracao.setModoOperacao(modoOperacao);
        configuracao.setControlarStockGlobal(controlarStockGlobal);
        configuracao.setPermitirVendaSemStock(permitirVendaSemStock);
        configuracao.setObservacao(observacao);
        configuracao.setUsuario(usuario);

        return configuracaoDao.updateConfiguracao(configuracao);
    }
}
