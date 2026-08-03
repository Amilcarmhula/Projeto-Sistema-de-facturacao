package jminvsm.service.produto;

import jminvsm.model.produto.Produto;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

public class ServiceProdutoConfiguracao {

    private final ServiceConfiguracaoSistema serviceConfiguracao;

    public ServiceProdutoConfiguracao() {
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }

    public boolean devePermitirAbrirStock(Produto produto) {
        return serviceConfiguracao.deveControlarStock(produto);
    }
}
