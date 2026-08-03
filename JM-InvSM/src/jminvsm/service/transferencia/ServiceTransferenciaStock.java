package jminvsm.service.transferencia;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import jminvsm.dao.transferencia.TransferenciaStockDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.transferencia.ItemTransferencia;
import jminvsm.model.transferencia.TransferenciaStock;
import jminvsm.service.ServiceException;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

public class ServiceTransferenciaStock {

    private final TransferenciaStockDAO transferenciaDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;
    private boolean opsSuccess;

    public ServiceTransferenciaStock() throws SQLException {
        this.transferenciaDao = new TransferenciaStockDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void criarRequisicaoTransferencia(Armazem origem, Armazem destino, List<ItemTransferencia> itens) {
        opsSuccess = false;
        serviceConfiguracao.exigirModuloStockActivo("A requisicao de transferencia");
        if (!validarOrigemDestino(origem, destino)) {
            return;
        }
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A requisicao de transferencia deve possuir pelo menos um item!");
        }
        for (ItemTransferencia item : itens) {
            if (!validarItem(item)) {
                return;
            }
        }

        TransferenciaStock transferencia = new TransferenciaStock();
        transferencia.setArmazemOrigem(origem);
        transferencia.setArmazemDestino(destino);
        transferencia.setItens(itens);
        if (transferenciaDao.criarRequisicaoTransferencia(transferencia)) {
            opsSuccess = true;
        }
    }

    public ItemTransferencia novoItem(Produto produto, int quantidade) {
        if (produto == null || produto.getId() <= 0 || quantidade <= 0) {
            return null;
        }
        ItemTransferencia item = new ItemTransferencia();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setQuantidadeRecebida(0);
        return item;
    }

    public boolean aprovarTransferencia(int idTransferencia) {
        return executarFluxo(idTransferencia, "aprovar");
    }

    public boolean enviarTransferencia(int idTransferencia) {
        return executarFluxo(idTransferencia, "enviar");
    }

    public boolean receberTransferencia(int idTransferencia) {
        return executarFluxo(idTransferencia, "receber");
    }

    public boolean cancelarTransferencia(int idTransferencia) {
        return executarFluxo(idTransferencia, "cancelar");
    }

    public ObservableList<TransferenciaStock> listarTransferencias(String estado) {
        return transferenciaDao.listarTransferencias(estado);
    }

    public TransferenciaStock consultarTransferencia(int idTransferencia) {
        return transferenciaDao.getTransferencia(idTransferencia);
    }

    public ObservableList<ItemTransferencia> consultarItens(int idTransferencia) {
        return transferenciaDao.getItensTransferencia(idTransferencia);
    }

    public void transferirProduto(int idProduto, int idArmazemOrigem, int idArmazemDestino, int quantidade) {
        opsSuccess = false;
        serviceConfiguracao.exigirModuloStockActivo("A transferencia directa de stock");
        if (idProduto <= 0) {
            throw new ServiceException("Produto nao selecionado!");
        }
        if (idArmazemOrigem <= 0) {
            throw new ServiceException("Armazem de origem nao selecionado!");
        }
        if (idArmazemDestino <= 0) {
            throw new ServiceException("Armazem de destino nao selecionado!");
        }
        if (idArmazemOrigem == idArmazemDestino) {
            throw new ServiceException("O armazem de origem deve ser diferente do destino!");
        }
        if (quantidade <= 0) {
            throw new ServiceException("Quantidade de transferencia invalida!");
        }

        if (transferenciaDao.transferirProduto(idProduto, idArmazemOrigem, idArmazemDestino, quantidade)) {
            opsSuccess = true;
        }
    }

    public void transferirProduto(Produto produto, Armazem origem, Armazem destino, String quantidade) {
        int qtd = quantidadeInteira(quantidade);
        transferirProduto(
                produto == null ? 0 : produto.getId(),
                origem == null ? 0 : origem.getId(),
                destino == null ? 0 : destino.getId(),
                qtd);
    }

    private boolean executarFluxo(int idTransferencia, String acao) {
        opsSuccess = false;
        serviceConfiguracao.exigirModuloStockActivo("O fluxo de transferencia");
        if (idTransferencia <= 0) {
            throw new ServiceException("Requisicao de transferencia nao selecionada!");
        }
        boolean result;
        if ("aprovar".equals(acao)) {
            result = transferenciaDao.aprovarTransferencia(idTransferencia);
        } else if ("enviar".equals(acao)) {
            result = transferenciaDao.enviarTransferencia(idTransferencia);
        } else if ("receber".equals(acao)) {
            result = transferenciaDao.receberTransferencia(idTransferencia);
        } else if ("cancelar".equals(acao)) {
            result = transferenciaDao.cancelarTransferencia(idTransferencia);
        } else {
            result = false;
        }
        if (result) {
            opsSuccess = true;
        }
        return result;
    }

    private boolean validarOrigemDestino(Armazem origem, Armazem destino) {
        if (origem == null || origem.getId() <= 0) {
            throw new ServiceException("Armazem de origem nao selecionado!");
        }
        if (destino == null || destino.getId() <= 0) {
            throw new ServiceException("Armazem de destino nao selecionado!");
        }
        if (origem.getId() == destino.getId()) {
            throw new ServiceException("O armazem de origem deve ser diferente do destino!");
        }
        return true;
    }

    private boolean validarItem(ItemTransferencia item) {
        if (item == null || item.getProduto() == null || item.getProduto().getId() <= 0) {
            throw new ServiceException("Produto do item nao informado!");
        }
        if (item.getQuantidade() <= 0) {
            throw new ServiceException("Quantidade do item deve ser maior que zero!");
        }
        return true;
    }

    private int quantidadeInteira(String quantidade) {
        if (quantidade == null || quantidade.trim().isEmpty()) {
            throw new ServiceException("Informe uma quantidade inteira maior que zero.");
        }
        try {
            int result = Integer.parseInt(quantidade.trim());
            if (result <= 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException ex) {
            throw new ServiceException("Informe uma quantidade inteira maior que zero.", ex);
        }
    }
}
