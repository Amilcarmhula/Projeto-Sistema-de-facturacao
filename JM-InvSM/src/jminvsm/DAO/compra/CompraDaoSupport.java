package jminvsm.dao.compra;

import java.sql.ResultSet;
import java.sql.SQLException;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.compra.ContaPagar;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.MovimentoFinanceiro;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.model.compra.PagamentoFornecedor;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.lote.Lote;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;

final class CompraDaoSupport {

    private CompraDaoSupport() {
    }

    static OrdemCompra mapOrdem(ResultSet rs) throws SQLException {
        OrdemCompra ordem = new OrdemCompra();
        ordem.setId(rs.getInt("id"));
        ordem.setNumero(rs.getString("numero"));
        ordem.setRequisicao(refRequisicao(getNullableInt(rs, "idRequisicao")));
        ordem.setFornecedor(refFornecedor(getNullableInt(rs, "idFornecedor"), rs.getString("fornecedorNome")));
        ordem.setDataEmissao(rs.getString("dataEmissao"));
        ordem.setDataPrevistaEntrega(rs.getString("dataPrevistaEntrega"));
        ordem.setEstado(rs.getString("estado"));
        ordem.setSubtotal(rs.getDouble("subtotal"));
        ordem.setDesconto(rs.getDouble("desconto"));
        ordem.setImposto(rs.getDouble("imposto"));
        ordem.setObservacao(rs.getString("observacao"));
        ordem.setUsuario(refUsuario(getNullableInt(rs, "idUsuario")));
        ordem.setDataCriacao(rs.getString("dataCriacao"));
        ordem.setDataActualizacao(rs.getString("dataActualizacao"));
        return ordem;
    }

    static ItemOrdemCompra mapItemOrdem(ResultSet rs) throws SQLException {
        ItemOrdemCompra item = new ItemOrdemCompra();
        item.setId(rs.getInt("id"));
        item.setOrdemCompra(refOrdem(getNullableInt(rs, "idOrdemCompra"), null));
        item.setItemRequisicao(refItemRequisicao(getNullableInt(rs, "idItemRequisicao")));
        item.setProduto(refProduto(getNullableInt(rs, "idProduto"), rs.getString("produtoNome")));
        item.setArmazem(refArmazem(getNullableInt(rs, "idArmazem"), rs.getString("armazemNome")));
        item.setQuantidadeSolicitada(rs.getInt("quantidadeSolicitada"));
        item.setQuantidadeRecebida(rs.getInt("quantidadeRecebida"));
        item.setCustoUnitario(rs.getDouble("custoUnitario"));
        item.setDesconto(rs.getDouble("desconto"));
        item.setImposto(rs.getDouble("imposto"));
        item.setSubtotal(rs.getDouble("subtotal"));
        item.setTotalLinha(rs.getDouble("totalLinha"));
        item.setEstado(rs.getString("estado"));
        return item;
    }

    static RecebimentoCompra mapRecebimento(ResultSet rs) throws SQLException {
        RecebimentoCompra recebimento = new RecebimentoCompra();
        recebimento.setId(rs.getInt("id"));
        recebimento.setNumero(rs.getString("numero"));
        recebimento.setOrdemCompra(refOrdem(getNullableInt(rs, "idOrdemCompra"), rs.getString("ordemNumero")));
        recebimento.setFornecedor(refFornecedor(getNullableInt(rs, "idFornecedor"), rs.getString("fornecedorNome")));
        recebimento.setDataRecebimento(rs.getString("dataRecebimento"));
        recebimento.setDocumentoFornecedor(rs.getString("documentoFornecedor"));
        recebimento.setEstado(rs.getString("estado"));
        recebimento.setTotalRecebido(rs.getDouble("totalRecebido"));
        recebimento.setObservacao(rs.getString("observacao"));
        recebimento.setUsuario(refUsuario(getNullableInt(rs, "idUsuario")));
        recebimento.setDataCriacao(rs.getString("dataCriacao"));
        recebimento.setDataActualizacao(rs.getString("dataActualizacao"));
        return recebimento;
    }

    static ItemRecebimentoCompra mapItemRecebimento(ResultSet rs) throws SQLException {
        ItemRecebimentoCompra item = new ItemRecebimentoCompra();
        item.setId(rs.getInt("id"));
        item.setRecebimentoCompra(refRecebimento(getNullableInt(rs, "idRecebimentoCompra"), null));
        item.setItemOrdemCompra(refItemOrdem(getNullableInt(rs, "idItemOrdemCompra")));
        item.setProduto(refProduto(getNullableInt(rs, "idProduto"), rs.getString("produtoNome")));
        item.setArmazem(refArmazem(getNullableInt(rs, "idArmazem"), rs.getString("armazemNome")));
        item.setQuantidadeRecebida(rs.getInt("quantidadeRecebida"));
        item.setCustoUnitario(rs.getDouble("custoUnitario"));
        item.setCodigoLote(rs.getString("codigoLote"));
        item.setLote(refLote(getNullableInt(rs, "idLote"), rs.getString("codigoLote")));
        item.setSubtotal(rs.getDouble("subtotal"));
        item.setEstado(rs.getString("estado"));
        return item;
    }

    static FacturaFornecedor mapFactura(ResultSet rs) throws SQLException {
        FacturaFornecedor factura = new FacturaFornecedor();
        factura.setId(rs.getInt("id"));
        factura.setNumero(rs.getString("numero"));
        factura.setFornecedor(refFornecedor(getNullableInt(rs, "idFornecedor"), rs.getString("fornecedorNome")));
        factura.setOrdemCompra(refOrdem(getNullableInt(rs, "idOrdemCompra"), rs.getString("ordemNumero")));
        factura.setRecebimentoCompra(refRecebimento(getNullableInt(rs, "idRecebimentoCompra"), rs.getString("recebimentoNumero")));
        factura.setDataEmissao(rs.getString("dataEmissao"));
        factura.setDataVencimento(rs.getString("dataVencimento"));
        factura.setSubtotal(rs.getDouble("subtotal"));
        factura.setDesconto(rs.getDouble("desconto"));
        factura.setImposto(rs.getDouble("imposto"));
        factura.setTotal(rs.getDouble("total"));
        factura.setValorPago(rs.getDouble("valorPago"));
        factura.setSaldoPendente(rs.getDouble("saldoPendente"));
        factura.setEstado(rs.getString("estado"));
        factura.setObservacao(rs.getString("observacao"));
        factura.setUsuario(refUsuario(getNullableInt(rs, "idUsuario")));
        factura.setDataCriacao(rs.getString("dataCriacao"));
        factura.setDataActualizacao(rs.getString("dataActualizacao"));
        return factura;
    }

    static ItemFacturaFornecedor mapItemFactura(ResultSet rs) throws SQLException {
        ItemFacturaFornecedor item = new ItemFacturaFornecedor();
        item.setId(rs.getInt("id"));
        item.setFacturaFornecedor(refFactura(getNullableInt(rs, "idFacturaFornecedor"), null));
        item.setItemRecebimentoCompra(refItemRecebimento(getNullableInt(rs, "idItemRecebimentoCompra")));
        item.setProduto(refProduto(getNullableInt(rs, "idProduto"), rs.getString("produtoNome")));
        item.setDescricao(rs.getString("descricao"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setCustoUnitario(rs.getDouble("custoUnitario"));
        item.setDesconto(rs.getDouble("desconto"));
        item.setImposto(rs.getDouble("imposto"));
        item.setSubtotal(rs.getDouble("subtotal"));
        item.setTotalLinha(rs.getDouble("totalLinha"));
        return item;
    }

    static ContaPagar mapConta(ResultSet rs) throws SQLException {
        ContaPagar conta = new ContaPagar();
        conta.setId(rs.getInt("id"));
        conta.setFacturaFornecedor(refFactura(getNullableInt(rs, "idFacturaFornecedor"), rs.getString("facturaNumero")));
        conta.setFornecedor(refFornecedor(getNullableInt(rs, "idFornecedor"), rs.getString("fornecedorNome")));
        conta.setDataEmissao(rs.getString("dataEmissao"));
        conta.setDataVencimento(rs.getString("dataVencimento"));
        conta.setValorOriginal(rs.getDouble("valorOriginal"));
        conta.setValorPago(rs.getDouble("valorPago"));
        conta.setSaldoPendente(rs.getDouble("saldoPendente"));
        conta.setEstado(rs.getString("estado"));
        conta.setObservacao(rs.getString("observacao"));
        conta.setDataCriacao(rs.getString("dataCriacao"));
        conta.setDataActualizacao(rs.getString("dataActualizacao"));
        return conta;
    }

    static PagamentoFornecedor mapPagamento(ResultSet rs) throws SQLException {
        PagamentoFornecedor pagamento = new PagamentoFornecedor();
        pagamento.setId(rs.getInt("id"));
        pagamento.setContaPagar(refConta(getNullableInt(rs, "idContaPagar")));
        pagamento.setFornecedor(refFornecedor(getNullableInt(rs, "idFornecedor"), rs.getString("fornecedorNome")));
        pagamento.setDataPagamento(rs.getString("dataPagamento"));
        pagamento.setValorPago(rs.getDouble("valorPago"));
        pagamento.setMetodoPagamento(refMetodo(getNullableInt(rs, "idMetodoPagamento"), rs.getString("metodoNome")));
        pagamento.setOrigemPagamento(rs.getString("origemPagamento"));
        pagamento.setContaBancaria(refContaBancaria(getNullableInt(rs, "idContaBancaria"), rs.getString("contaBancariaNome")));
        pagamento.setReferencia(rs.getString("referencia"));
        pagamento.setDescricao(rs.getString("descricao"));
        pagamento.setEstado(rs.getString("estado"));
        pagamento.setUsuario(refUsuario(getNullableInt(rs, "idUsuario")));
        pagamento.setDataCriacao(rs.getString("dataCriacao"));
        return pagamento;
    }

    static MovimentoFinanceiro mapMovimento(ResultSet rs) throws SQLException {
        MovimentoFinanceiro movimento = new MovimentoFinanceiro();
        movimento.setId(rs.getInt("id"));
        movimento.setDataMovimento(rs.getString("dataMovimento"));
        movimento.setTipoMovimento(rs.getString("tipoMovimento"));
        movimento.setOrigem(rs.getString("origem"));
        movimento.setValor(rs.getDouble("valor"));
        movimento.setOrigemPagamento(rs.getString("origemPagamento"));
        movimento.setIdContaBancaria(getNullableInt(rs, "idContaBancaria"));
        movimento.setIdTransacao(getNullableInt(rs, "idTransacao"));
        movimento.setIdPagamentoFornecedor(getNullableInt(rs, "idPagamentoFornecedor"));
        movimento.setSaldoAnterior(rs.getDouble("saldoAnterior"));
        movimento.setSaldoPosterior(rs.getDouble("saldoPosterior"));
        movimento.setDescricao(rs.getString("descricao"));
        movimento.setIdUsuario(getNullableInt(rs, "idUsuario"));
        movimento.setDataCriacao(rs.getString("dataCriacao"));
        movimento.setFornecedorNome(rs.getString("fornecedorNome"));
        return movimento;
    }

    static Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    static Produto refProduto(Integer id, String nome) {
        if (id == null || id <= 0) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        return produto;
    }

    static Armazem refArmazem(Integer id, String nome) {
        if (id == null || id <= 0) {
            return null;
        }
        Armazem armazem = new Armazem();
        armazem.setId(id);
        armazem.setNome_arm(nome);
        return armazem;
    }

    static Fornecedor refFornecedor(Integer id, String nome) {
        if (id == null || id <= 0) {
            return null;
        }
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setRazaosocial_forn(nome);
        return fornecedor;
    }

    static Usuario refUsuario(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(id);
        return usuario;
    }

    static Requisicao refRequisicao(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        Requisicao requisicao = new Requisicao();
        requisicao.setId(id);
        return requisicao;
    }

    static ItemRequisicao refItemRequisicao(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        ItemRequisicao item = new ItemRequisicao();
        item.setId(id);
        return item;
    }

    static OrdemCompra refOrdem(Integer id, String numero) {
        if (id == null || id <= 0) {
            return null;
        }
        OrdemCompra ordem = new OrdemCompra();
        ordem.setId(id);
        ordem.setNumero(numero);
        return ordem;
    }

    static ItemOrdemCompra refItemOrdem(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        ItemOrdemCompra item = new ItemOrdemCompra();
        item.setId(id);
        return item;
    }

    static RecebimentoCompra refRecebimento(Integer id, String numero) {
        if (id == null || id <= 0) {
            return null;
        }
        RecebimentoCompra recebimento = new RecebimentoCompra();
        recebimento.setId(id);
        recebimento.setNumero(numero);
        return recebimento;
    }

    static ItemRecebimentoCompra refItemRecebimento(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        ItemRecebimentoCompra item = new ItemRecebimentoCompra();
        item.setId(id);
        return item;
    }

    static FacturaFornecedor refFactura(Integer id, String numero) {
        if (id == null || id <= 0) {
            return null;
        }
        FacturaFornecedor factura = new FacturaFornecedor();
        factura.setId(id);
        factura.setNumero(numero);
        return factura;
    }

    static ContaPagar refConta(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        ContaPagar conta = new ContaPagar();
        conta.setId(id);
        return conta;
    }

    static Lote refLote(Integer id, String codigo) {
        if (id == null || id <= 0) {
            return null;
        }
        Lote lote = new Lote();
        lote.setId(id);
        lote.setCodigo(codigo);
        return lote;
    }

    static MetodoPagamento refMetodo(Integer id, String nome) {
        if (id == null || id <= 0) {
            return null;
        }
        MetodoPagamento metodo = new MetodoPagamento();
        metodo.setId(id);
        metodo.setNomePagamento(nome);
        return metodo;
    }

    static ContaBancaria refContaBancaria(Integer id, String nome) {
        if (id == null || id <= 0) {
            return null;
        }
        ContaBancaria conta = new ContaBancaria();
        conta.setId(id);
        conta.setNome_banco(nome);
        return conta;
    }
}
