package jminvsm.dao.movimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.lote.Lote;
import jminvsm.model.movimento.Movimento;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.util.Conexao;
import jminvsm.dao.DaoErrorHandler;
public class MovimentoDAO implements MovimentoDAOImpl {

    @Override
    public boolean addEntity(Movimento entity) {
        String sql = "INSERT INTO movimento (tipoOrigem, tipoMovimento, dataMovimento, quantidade, "
                + "idArmazem, idProduto, idLote, idItemRequisicao) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar movimento: " + ex);
            return false;
        }
    }

    @Override
    public Movimento getEntityByID(int id) {
        String sql = baseSelect() + " WHERE m.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMovimento(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar movimento: " + ex);
        }
        return null;
    }

    @Override
    public ObservableList<Movimento> getMovimentosByRequisicao(int idRequisicao) {
        ObservableList<Movimento> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " WHERE ir.idRequisicao=? ORDER BY m.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRequisicao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMovimento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar movimentos da requisicao: " + ex);
        }
        return lista;
    }

    @Override
    public ObservableList<Movimento> getMovimentosByProduto(Integer idProduto, Integer idArmazem) {
        ObservableList<Movimento> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " WHERE (? IS NULL OR m.idProduto = ?) "
                + "AND (? IS NULL OR m.idArmazem = ?) ORDER BY m.dataMovimento DESC, m.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableInt(ps, 1, idProduto);
            setNullableInt(ps, 3, idArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMovimento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar movimentos por produto: " + ex);
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, Movimento entity) throws SQLException {
        ps.setString(1, entity.getTipoOrigem());
        ps.setString(2, entity.getTipoMovimento());
        ps.setString(3, entity.getDataMovimento());
        ps.setInt(4, entity.getQuantidade());
        ps.setInt(5, entity.getArmazem().getId());
        ps.setInt(6, entity.getProduto().getId());
        if (entity.getLote() == null || entity.getLote().getId() <= 0) {
            ps.setNull(7, java.sql.Types.INTEGER);
        } else {
            ps.setInt(7, entity.getLote().getId());
        }
        if (entity.getItemRequisicao() == null || entity.getItemRequisicao().getId() <= 0) {
            ps.setNull(8, java.sql.Types.INTEGER);
        } else {
            ps.setInt(8, entity.getItemRequisicao().getId());
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, java.sql.Types.INTEGER);
            ps.setNull(index + 1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
            ps.setInt(index + 1, value);
        }
    }

    private String baseSelect() {
        return "SELECT m.id, m.tipoOrigem, m.tipoMovimento, m.dataMovimento, m.quantidade, "
                + "m.idArmazem, m.idProduto, m.idLote, m.idItemRequisicao, "
                + "a.nome AS nomeArmazem, p.nome AS nomeProduto, l.codigo AS codigoLote, "
                + "ir.idRequisicao "
                + "FROM movimento m "
                + "JOIN armazem a ON a.id = m.idArmazem "
                + "JOIN produto p ON p.id = m.idProduto "
                + "LEFT JOIN lote l ON l.id = m.idLote "
                + "LEFT JOIN itemRequisicao ir ON ir.id = m.idItemRequisicao";
    }

    private Movimento mapMovimento(ResultSet rs) throws SQLException {
        Armazem armazem = new Armazem();
        armazem.setId(rs.getInt("idArmazem"));
        armazem.setNome_arm(rs.getString("nomeArmazem"));

        Produto produto = new Produto();
        produto.setId(rs.getInt("idProduto"));
        produto.setNome(rs.getString("nomeProduto"));

        Lote lote = null;
        int idLote = rs.getInt("idLote");
        if (!rs.wasNull()) {
            lote = new Lote();
            lote.setId(idLote);
            lote.setCodigo(rs.getString("codigoLote"));
        }

        ItemRequisicao item = null;
        int idItem = rs.getInt("idItemRequisicao");
        if (!rs.wasNull()) {
            Requisicao requisicao = new Requisicao();
            requisicao.setId(rs.getInt("idRequisicao"));
            item = new ItemRequisicao();
            item.setId(idItem);
            item.setRequisicao(requisicao);
        }

        Movimento movimento = new Movimento();
        movimento.setId(rs.getInt("id"));
        movimento.setTipoOrigem(rs.getString("tipoOrigem"));
        movimento.setTipoMovimento(rs.getString("tipoMovimento"));
        movimento.setDataMovimento(rs.getString("dataMovimento"));
        movimento.setQuantidade(rs.getInt("quantidade"));
        movimento.setArmazem(armazem);
        movimento.setProduto(produto);
        movimento.setLote(lote);
        movimento.setItemRequisicao(item);
        return movimento;
    }
}
