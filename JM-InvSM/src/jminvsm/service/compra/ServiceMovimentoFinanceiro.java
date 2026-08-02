package jminvsm.service.compra;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.compra.MovimentoFinanceiroDAO;
import jminvsm.model.compra.MovimentoFinanceiro;

public class ServiceMovimentoFinanceiro {

    private final MovimentoFinanceiroDAO movimentoFinanceiroDao;

    public ServiceMovimentoFinanceiro() throws SQLException {
        this.movimentoFinanceiroDao = new MovimentoFinanceiroDAO();
    }

    public ObservableList<MovimentoFinanceiro> listaMovimentos() {
        return movimentoFinanceiroDao.listAllEntities();
    }

    public ObservableList<MovimentoFinanceiro> listaMovimentosPorOrigemPagamento(String origemPagamento) {
        return movimentoFinanceiroDao.getEntitiesByOrigemPagamento(origemPagamento);
    }
}
