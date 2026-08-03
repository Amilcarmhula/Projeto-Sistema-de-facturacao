package jminvsm.dao.movimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.lote.Lote;
import jminvsm.model.movimento.Movimento;
import jminvsm.model.movimento.MovimentoLote;
import jminvsm.util.Conexao;
import jminvsm.dao.DaoErrorHandler;
public class MovimentoLoteDAO implements MovimentoLoteDAOImpl {

    @Override
    public boolean addEntity(MovimentoLote entity) {
        String sql = "INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada) VALUES (?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getMovimento().getId());
            ps.setInt(2, entity.getLote().getId());
            ps.setInt(3, entity.getQuantidadeRetirada());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar movimento por lote: " + ex);
            return false;
        }
    }

    @Override
    public ObservableList<MovimentoLote> getMovimentoLotesByMovimento(int idMovimento) {
        ObservableList<MovimentoLote> lista = FXCollections.observableArrayList();
        String sql = "SELECT ml.id, ml.idMovimento, ml.idLote, ml.quantidadeRetirada, l.codigo "
                + "FROM movimento_lote ml "
                + "JOIN lote l ON l.id = ml.idLote "
                + "WHERE ml.idMovimento=? ORDER BY ml.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMovimento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimento movimento = new Movimento();
                    movimento.setId(rs.getInt("idMovimento"));

                    Lote lote = new Lote();
                    lote.setId(rs.getInt("idLote"));
                    lote.setCodigo(rs.getString("codigo"));

                    MovimentoLote movimentoLote = new MovimentoLote();
                    movimentoLote.setId(rs.getInt("id"));
                    movimentoLote.setMovimento(movimento);
                    movimentoLote.setLote(lote);
                    movimentoLote.setQuantidadeRetirada(rs.getInt("quantidadeRetirada"));
                    lista.add(movimentoLote);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar movimentos por lote: " + ex);
        }
        return lista;
    }
}
