package jminvsm.service.listagem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.compra.ContaPagar;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.model.compra.PagamentoFornecedor;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.movimento.Movimento;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.reserva.ReservaStock;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.Factura;

public class ServicePesquisaListagem {

    public ObservableList<Stock> filtrarStock(ObservableList<Stock> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(nomeProduto(item), filtro)
                || contains(armazemStock(item), filtro)
                || contains(descricaoProduto(item), filtro)
                || contains(categoriaProduto(item), filtro)
                || contains(chaveStock(item), filtro));
    }

    public ObservableList<Factura> filtrarFacturas(ObservableList<Factura> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(numeroFactura(item), filtro)
                || contains(clienteFactura(item), filtro)
                || contains(dataEmissao(item), filtro)
                || contains(dataVencimento(item), filtro)
                || contains(estadoFactura(item), filtro)
                || contains(tipoFactura(item), filtro));
    }

    public ObservableList<Cliente> filtrarClientes(ObservableList<Cliente> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(item == null ? null : item.getNome_cli(), filtro)
                || contains(item == null ? null : item.getRazao_cli(), filtro)
                || contains(String.valueOf(item == null ? "" : item.getNuit_cli()), filtro));
    }

    public ObservableList<Fornecedor> filtrarFornecedores(ObservableList<Fornecedor> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(item == null ? null : item.getRazaosocial_forn(), filtro)
                || contains(String.valueOf(item == null ? "" : item.getNuit_forn()), filtro));
    }

    public ObservableList<Armazem> filtrarArmazens(ObservableList<Armazem> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(item == null ? null : item.getNome_arm(), filtro)
                || contains(item == null ? null : item.getDescricao_arm(), filtro)
                || contains(item == null ? null : item.getTipo(), filtro));
    }

    public ObservableList<Categoria> filtrarCategorias(ObservableList<Categoria> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(item == null ? null : item.getGrupo(), filtro)
                || contains(item == null ? null : item.getSubgrupo(), filtro)
                || contains(item == null ? null : item.getFamilia(), filtro)
                || contains(item == null ? null : item.getDescricao(), filtro));
    }

    public ObservableList<Requisicao> filtrarRequisicoes(ObservableList<Requisicao> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(String.valueOf(item == null ? "" : item.getId()), filtro)
                || contains(item == null ? null : item.getEstado(), filtro)
                || contains(item == null ? null : item.getMotivo(), filtro)
                || contains(item == null ? null : item.getDataRequisicao(), filtro)
                || contains(item == null ? null : item.getDataNecessidade(), filtro));
    }

    public ObservableList<OrdemCompra> filtrarOrdensCompra(ObservableList<OrdemCompra> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(String.valueOf(item == null ? "" : item.getId()), filtro)
                || contains(item == null ? null : item.getNumero(), filtro)
                || contains(item == null ? null : item.getEstado(), filtro)
                || contains(nomeFornecedor(item), filtro)
                || contains(item == null ? null : item.getDataEmissao(), filtro)
                || contains(item == null ? null : item.getDataPrevistaEntrega(), filtro));
    }

    public ObservableList<RecebimentoCompra> filtrarRecebimentosCompra(ObservableList<RecebimentoCompra> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(String.valueOf(item == null ? "" : item.getId()), filtro)
                || contains(item == null ? null : item.getNumero(), filtro)
                || contains(item == null ? null : item.getEstado(), filtro)
                || contains(item == null ? null : item.getDocumentoFornecedor(), filtro)
                || contains(numeroOrdem(item), filtro)
                || contains(nomeFornecedor(item), filtro));
    }

    public ObservableList<FacturaFornecedor> filtrarFacturasFornecedor(ObservableList<FacturaFornecedor> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(String.valueOf(item == null ? "" : item.getId()), filtro)
                || contains(item == null ? null : item.getNumero(), filtro)
                || contains(item == null ? null : item.getEstado(), filtro)
                || contains(nomeFornecedor(item), filtro)
                || contains(numeroRecebimento(item), filtro));
    }

    public ObservableList<ContaPagar> filtrarContasPagar(ObservableList<ContaPagar> lista, String termo) {
        return filtrar(lista, termo, (item, filtro) -> contains(String.valueOf(item == null ? "" : item.getId()), filtro)
                || contains(item == null ? null : item.getEstado(), filtro)
                || contains(nomeFornecedor(item), filtro)
                || contains(numeroFactura(item), filtro));
    }

    public String chaveStock(Stock stock) {
        if (stock == null || stock.getProduto() == null || stock.getArmazem() == null) {
            return "";
        }
        return stock.getProduto().getId() + "," + stock.getArmazem().getId();
    }

    public String armazemStock(Stock stock) {
        if (stock == null || stock.getArmazem() == null) {
            return "";
        }
        return safe(stock.getArmazem().getNome_arm()) + ": " + safe(stock.getArmazem().getTipo());
    }

    public String nomeProduto(Stock stock) {
        return stock == null || stock.getProduto() == null ? "" : safe(stock.getProduto().getNome());
    }

    public String descricaoProduto(Stock stock) {
        return stock == null || stock.getProduto() == null ? "" : safe(stock.getProduto().getDescricao());
    }

    public String unidadeProduto(Stock stock) {
        return stock == null || stock.getProduto() == null || stock.getProduto().getUnidadeMedida() == null
                ? "" : safe(stock.getProduto().getUnidadeMedida().getSigla());
    }

    public String categoriaProduto(Stock stock) {
        return stock == null || stock.getProduto() == null || stock.getProduto().getCategoria() == null
                ? "" : safe(stock.getProduto().getCategoria().getFamilia());
    }

    public String numeroFactura(Factura factura) {
        return factura == null ? "" : safe(factura.getNumero_fac());
    }

    public String tipoFactura(Factura factura) {
        return factura == null || factura.getDocComerciais() == null
                ? "" : safe(factura.getDocComerciais().getNome_doc());
    }

    public String clienteFactura(Factura factura) {
        return factura == null || factura.getCliente() == null ? "" : safe(factura.getCliente().getRazao_cli());
    }

    public String dataEmissao(Factura factura) {
        return factura == null ? "" : safe(factura.getData_emissao());
    }

    public String dataVencimento(Factura factura) {
        return factura == null ? "" : safe(factura.getData_vencimento());
    }

    public String estadoFactura(Factura factura) {
        return factura == null ? "" : safe(factura.getEstado());
    }

    public String contactosCliente(Cliente cliente) {
        return cliente == null ? "" : safe(cliente.printContatos());
    }

    public String enderecosCliente(Cliente cliente) {
        return cliente == null ? "" : safe(cliente.printEnderecos());
    }

    public String contactosFornecedor(Fornecedor fornecedor) {
        return fornecedor == null ? "" : safe(fornecedor.printContatos());
    }

    public String enderecosFornecedor(Fornecedor fornecedor) {
        return fornecedor == null ? "" : safe(fornecedor.printEnderecos());
    }

    public String contactosArmazem(Armazem armazem) {
        return armazem == null ? "" : safe(armazem.printContatos());
    }

    public String nomeProduto(ItemRequisicao item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    public String nomeProduto(ReservaStock reserva) {
        return reserva != null && reserva.getProduto() != null ? safe(reserva.getProduto().getNome()) : "";
    }

    public String nomeProduto(Movimento movimento) {
        return movimento != null && movimento.getProduto() != null ? safe(movimento.getProduto().getNome()) : "";
    }

    public String nomeProduto(ItemOrdemCompra item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    public String nomeProduto(ItemRecebimentoCompra item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    public String nomeProduto(ItemFacturaFornecedor item) {
        return item != null && item.getProduto() != null ? safe(item.getProduto().getNome()) : "";
    }

    public String nomeArmazem(ItemRequisicao item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    public String nomeArmazem(ReservaStock reserva) {
        return reserva != null && reserva.getArmazem() != null ? safe(reserva.getArmazem().getNome_arm()) : "";
    }

    public String nomeArmazem(Movimento movimento) {
        return movimento != null && movimento.getArmazem() != null ? safe(movimento.getArmazem().getNome_arm()) : "";
    }

    public String nomeArmazem(ItemOrdemCompra item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    public String nomeArmazem(ItemRecebimentoCompra item) {
        return item != null && item.getArmazem() != null ? safe(item.getArmazem().getNome_arm()) : "";
    }

    public String nomeFornecedor(OrdemCompra ordem) {
        return ordem != null && ordem.getFornecedor() != null ? safe(ordem.getFornecedor().getRazaosocial_forn()) : "";
    }

    public String nomeFornecedor(RecebimentoCompra recebimento) {
        return recebimento != null && recebimento.getFornecedor() != null ? safe(recebimento.getFornecedor().getRazaosocial_forn()) : "";
    }

    public String nomeFornecedor(FacturaFornecedor factura) {
        return factura != null && factura.getFornecedor() != null ? safe(factura.getFornecedor().getRazaosocial_forn()) : "";
    }

    public String nomeFornecedor(ContaPagar conta) {
        return conta != null && conta.getFornecedor() != null ? safe(conta.getFornecedor().getRazaosocial_forn()) : "";
    }

    public String nomeFornecedor(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getFornecedor() != null ? safe(pagamento.getFornecedor().getRazaosocial_forn()) : "";
    }

    public String numeroOrdem(RecebimentoCompra recebimento) {
        return recebimento != null && recebimento.getOrdemCompra() != null ? safe(recebimento.getOrdemCompra().getNumero()) : "";
    }

    public String numeroRecebimento(FacturaFornecedor factura) {
        return factura != null && factura.getRecebimentoCompra() != null
                ? String.valueOf(factura.getRecebimentoCompra().getId()) : "";
    }

    public String numeroFactura(ContaPagar conta) {
        return conta != null && conta.getFacturaFornecedor() != null ? safe(conta.getFacturaFornecedor().getNumero()) : "";
    }

    public String numeroConta(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getContaPagar() != null
                ? String.valueOf(pagamento.getContaPagar().getId()) : "";
    }

    public String nomeMetodo(PagamentoFornecedor pagamento) {
        return pagamento != null && pagamento.getMetodoPagamento() != null
                ? safe(pagamento.getMetodoPagamento().getNomePagamento()) : "";
    }

    public String textoContaBancaria(ContaBancaria conta) {
        if (conta == null) {
            return "";
        }
        StringBuilder texto = new StringBuilder();
        texto.append(safe(conta.getNome_banco()));
        if (conta.getNumero_conta() != null && !conta.getNumero_conta().trim().isEmpty()) {
            texto.append(" - ").append(conta.getNumero_conta());
        }
        if (conta.getDescricaoTipo() != null && !conta.getDescricaoTipo().trim().isEmpty()) {
            texto.append(" - ").append(conta.getDescricaoTipo());
        }
        return texto.toString();
    }

    public String safeText(String value) {
        return safe(value);
    }

    public Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Double parseDouble(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Double.valueOf(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public String estadoSelecionado(String estado) {
        return estado == null || "Todos".equalsIgnoreCase(estado) ? null : estado;
    }

    private <T> ObservableList<T> filtrar(ObservableList<T> lista, String termo, Pesquisa<T> pesquisa) {
        ObservableList<T> resultado = FXCollections.observableArrayList();
        if (lista == null) {
            return resultado;
        }
        String filtro = termo == null ? "" : termo.trim().toLowerCase();
        if (filtro.isEmpty()) {
            resultado.addAll(lista);
            return resultado;
        }
        for (T item : lista) {
            if (pesquisa.matches(item, filtro)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    private boolean contains(String value, String termo) {
        String filtro = termo == null ? "" : termo.trim().toLowerCase();
        return value != null && value.toLowerCase().contains(filtro);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @FunctionalInterface
    private interface Pesquisa<T> {
        boolean matches(T item, String termo);
    }
}
