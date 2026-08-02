package jminvsm.dao.compra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.compra.MovimentoFinanceiro;
import jminvsm.util.Conexao;

public class MovimentoFinanceiroDAO {

    private static final String SELECT_BASE = "SELECT mf.*, f.razaosocial AS fornecedorNome "
            + "FROM movimento_financeiro mf "
            + "LEFT JOIN pagamento_fornecedor pf ON pf.id=mf.idPagamentoFornecedor "
            + "LEFT JOIN fornecedor f ON f.id=pf.idFornecedor ";

    public ObservableList<MovimentoFinanceiro> listAllEntities() {
        return queryMovimentos(SELECT_BASE + "ORDER BY mf.id DESC");
    }

    public ObservableList<MovimentoFinanceiro> getEntitiesByOrigemPagamento(String origemPagamento) {
        ObservableList<MovimentoFinanceiro> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE mf.origemPagamento=? ORDER BY mf.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, origemPagamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapMovimento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar movimentos por origem: " + ex.getMessage());
        }
        return lista;
    }

    private ObservableList<MovimentoFinanceiro> queryMovimentos(String sql) {
        ObservableList<MovimentoFinanceiro> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapMovimento(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar movimentos financeiros: " + ex.getMessage());
        }
        return lista;
    }
}
