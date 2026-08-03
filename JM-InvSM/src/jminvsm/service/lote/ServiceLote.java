package jminvsm.service.lote;

import java.math.BigDecimal;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.lote.LoteDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.lote.Lote;
import jminvsm.model.produto.Produto;
import jminvsm.service.ServiceException;

public class ServiceLote {

    private final LoteDAO loteDao;
    private boolean opsSuccess;

    public ServiceLote() throws SQLException {
        this.loteDao = new LoteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registaLote(String codigo, Integer idProduto, Integer idArmazem, Integer idFornecedor,
            String dataEntrada, Integer quantidadeInicial, Integer quantidadeAtual, BigDecimal custoUnitario) {
        setOpsSuccess(false);
        if (codigo == null || codigo.trim().isEmpty() || idProduto == null || idProduto <= 0
                || idArmazem == null || idArmazem <= 0 || dataEntrada == null || dataEntrada.trim().isEmpty()
                || quantidadeAtual == null || quantidadeAtual <= 0 || custoUnitario == null || custoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Dados do lote invalidos!");
        }

        Lote lote = montaLote(codigo, idProduto, idArmazem, idFornecedor, dataEntrada,
                quantidadeInicial, quantidadeAtual, custoUnitario, "Ativo");
        if (loteDao.addEntity(lote)) {
            setOpsSuccess(true);
        }
    }

    public ObservableList<Lote> listaLotes() {
        return loteDao.listAllEntities();
    }

    public ObservableList<Lote> listaLotesDisponiveis(Integer idProduto, Integer idArmazem) {
        return loteDao.getLotesDisponiveis(idProduto, idArmazem);
    }

    public Lote consultaLoteByID(int id) {
        return loteDao.getEntityByID(id);
    }

    public Lote consultaUltimoLote() {
        return loteDao.getLastEntity();
    }

    public boolean atualizaQuantidadeAtual(int idLote, int quantidadeAtual) {
        setOpsSuccess(false);
        if (idLote <= 0 || quantidadeAtual < 0) {
            throw new ServiceException("Dados para atualizar lote invalidos!");
        }
        if (loteDao.updateQuantidadeAtual(idLote, quantidadeAtual)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    private Lote montaLote(String codigo, int idProduto, int idArmazem, Integer idFornecedor,
            String dataEntrada, Integer quantidadeInicial, int quantidadeAtual, BigDecimal custoUnitario, String estado) {
        Produto produto = new Produto();
        produto.setId(idProduto);

        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(idFornecedor == null || idFornecedor <= 0 ? 1 : idFornecedor);

        Lote lote = new Lote();
        lote.setCodigo(codigo);
        lote.setProduto(produto);
        lote.setArmazem(armazem);
        lote.setFornecedor(fornecedor);
        lote.setDataEntrada(dataEntrada);
        lote.setQuantidadeInicial(quantidadeInicial == null || quantidadeInicial <= 0 ? quantidadeAtual : quantidadeInicial);
        lote.setQuantidadeAtual(quantidadeAtual);
        lote.setCustoUnitario(custoUnitario);
        lote.setEstado(estado);
        return lote;
    }
}
