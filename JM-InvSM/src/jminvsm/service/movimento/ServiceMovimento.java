package jminvsm.service.movimento;

import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import jminvsm.dao.movimento.MovimentoDAO;
import jminvsm.dao.movimento.MovimentoLoteDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.lote.Lote;
import jminvsm.model.movimento.Movimento;
import jminvsm.model.movimento.MovimentoLote;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.service.ServiceException;

public class ServiceMovimento {

    private final MovimentoDAO movimentoDao;
    private final MovimentoLoteDAO movimentoLoteDao;
    private boolean opsSuccess;

    public ServiceMovimento() throws SQLException {
        this.movimentoDao = new MovimentoDAO();
        this.movimentoLoteDao = new MovimentoLoteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registaMovimento(String tipoOrigem, String tipoMovimento, Integer idProduto, Integer idArmazem,
            Integer quantidade, Integer idLote, Integer idItemRequisicao) {
        setOpsSuccess(false);
        if (tipoOrigem == null || tipoMovimento == null || idProduto == null || idProduto <= 0
                || idArmazem == null || idArmazem <= 0 || quantidade == null || quantidade <= 0) {
            throw new ServiceException("Dados do movimento invalidos!");
        }

        Movimento movimento = montaMovimento(tipoOrigem, tipoMovimento, idProduto, idArmazem, quantidade, idLote, idItemRequisicao);
        if (movimentoDao.addEntity(movimento)) {
            setOpsSuccess(true);
        }
    }

    public boolean registaMovimentoLote(int idMovimento, int idLote, int quantidadeRetirada) {
        setOpsSuccess(false);
        if (idMovimento <= 0 || idLote <= 0 || quantidadeRetirada <= 0) {
            throw new ServiceException("Dados do movimento por lote invalidos!");
        }

        Movimento movimento = new Movimento();
        movimento.setId(idMovimento);

        Lote lote = new Lote();
        lote.setId(idLote);

        MovimentoLote movimentoLote = new MovimentoLote();
        movimentoLote.setMovimento(movimento);
        movimentoLote.setLote(lote);
        movimentoLote.setQuantidadeRetirada(quantidadeRetirada);

        if (movimentoLoteDao.addEntity(movimentoLote)) {
            setOpsSuccess(true);
        }
        return isOpsSuccess();
    }

    public Movimento consultaMovimentoByID(int id) {
        return movimentoDao.getEntityByID(id);
    }

    public ObservableList<Movimento> consultaMovimentosRequisicao(int idRequisicao) {
        return movimentoDao.getMovimentosByRequisicao(idRequisicao);
    }

    public ObservableList<Movimento> consultaMovimentosProduto(Integer idProduto, Integer idArmazem) {
        return movimentoDao.getMovimentosByProduto(idProduto, idArmazem);
    }

    public ObservableList<MovimentoLote> consultaMovimentoLotes(int idMovimento) {
        return movimentoLoteDao.getMovimentoLotesByMovimento(idMovimento);
    }

    private Movimento montaMovimento(String tipoOrigem, String tipoMovimento, int idProduto, int idArmazem,
            int quantidade, Integer idLote, Integer idItemRequisicao) {
        Produto produto = new Produto();
        produto.setId(idProduto);

        Armazem armazem = new Armazem();
        armazem.setId(idArmazem);

        Lote lote = null;
        if (idLote != null && idLote > 0) {
            lote = new Lote();
            lote.setId(idLote);
        }

        ItemRequisicao itemRequisicao = null;
        if (idItemRequisicao != null && idItemRequisicao > 0) {
            itemRequisicao = new ItemRequisicao();
            itemRequisicao.setId(idItemRequisicao);
        }

        Movimento movimento = new Movimento();
        movimento.setTipoOrigem(tipoOrigem);
        movimento.setTipoMovimento(tipoMovimento);
        movimento.setDataMovimento(LocalDate.now());
        movimento.setProduto(produto);
        movimento.setArmazem(armazem);
        movimento.setQuantidade(quantidade);
        movimento.setLote(lote);
        movimento.setItemRequisicao(itemRequisicao);
        return movimento;
    }
}
