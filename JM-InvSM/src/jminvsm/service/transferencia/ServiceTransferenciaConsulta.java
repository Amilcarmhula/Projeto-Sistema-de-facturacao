package jminvsm.service.transferencia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.transferencia.ItemTransferencia;
import jminvsm.model.transferencia.TransferenciaStock;
import jminvsm.service.ServiceException;

public class ServiceTransferenciaConsulta {

    public ObservableList<TransferenciaStock> filtrar(ObservableList<TransferenciaStock> transferencias, String termo) {
        ObservableList<TransferenciaStock> resultado = FXCollections.observableArrayList();
        if (transferencias == null) {
            return resultado;
        }
        String filtro = termo == null ? "" : termo.trim().toLowerCase();
        if (filtro.isEmpty()) {
            resultado.addAll(transferencias);
            return resultado;
        }
        for (TransferenciaStock transferencia : transferencias) {
            if (contem(transferencia, filtro)) {
                resultado.add(transferencia);
            }
        }
        return resultado;
    }

    public int quantidadeObrigatoria(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ServiceException("Informe uma quantidade inteira maior que zero.");
        }
        try {
            int quantidade = Integer.parseInt(value.trim());
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
            return quantidade;
        } catch (NumberFormatException ex) {
            throw new ServiceException("Informe uma quantidade inteira maior que zero.", ex);
        }
    }

    public String rotuloArmazem(Armazem armazem) {
        return armazem == null ? "" : armazem.getId() + " - " + safe(armazem.getNome_arm());
    }

    public String rotuloProduto(Produto produto) {
        return produto == null ? "" : produto.getId() + " - " + safe(produto.getNome());
    }

    public String nomeProduto(ItemTransferencia item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    public String nomeArmazemOrigem(TransferenciaStock transferencia) {
        return transferencia != null && transferencia.getArmazemOrigem() != null
                ? safe(transferencia.getArmazemOrigem().getNome_arm()) : "";
    }

    public String nomeArmazemDestino(TransferenciaStock transferencia) {
        return transferencia != null && transferencia.getArmazemDestino() != null
                ? safe(transferencia.getArmazemDestino().getNome_arm()) : "";
    }

    public String rotuloTransferencia(TransferenciaStock transferencia) {
        if (transferencia == null) {
            return "-";
        }
        return "#" + transferencia.getId() + " - " + safe(transferencia.getEstado());
    }

    public String textoResumoSelecionada(TransferenciaStock transferencia) {
        if (transferencia == null) {
            return "Nenhuma transferencia seleccionada";
        }
        return "Transferencia #" + transferencia.getId() + " seleccionada";
    }

    private boolean contem(TransferenciaStock transferencia, String termo) {
        return transferencia != null
                && (String.valueOf(transferencia.getId()).contains(termo)
                || contains(transferencia.getEstado(), termo)
                || contains(nomeArmazemOrigem(transferencia), termo)
                || contains(nomeArmazemDestino(transferencia), termo)
                || contains(transferencia.getDataCriacao(), termo));
    }

    private boolean contains(String value, String termo) {
        return value != null && value.toLowerCase().contains(termo);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
