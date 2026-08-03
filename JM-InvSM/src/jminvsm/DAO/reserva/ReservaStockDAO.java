package jminvsm.dao.reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.reserva.ReservaStock;
import jminvsm.util.Conexao;
import jminvsm.dao.DaoErrorHandler;
public class ReservaStockDAO implements ReservaStockDAOImpl {

    @Override
    public boolean addEntity(ReservaStock entity) {
        String sql = "INSERT INTO reserva_stock (idProduto, idArmazem, idRequisicao, quantidade, estado, dataReserva) "
                + "VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar reserva de stock: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateEntityByID(ReservaStock entity, int id) {
        String sql = "UPDATE reserva_stock SET idProduto=?, idArmazem=?, idRequisicao=?, quantidade=?, estado=?, dataReserva=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar reserva de stock: " + ex);
            return false;
        }
    }

    @Override
    public ReservaStock getEntityByID(int id) {
        String sql = baseSelect() + " WHERE rs.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapReserva(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar reserva de stock: " + ex);
        }
        return null;
    }

    @Override
    public ObservableList<ReservaStock> getReservasByRequisicao(int idRequisicao) {
        ObservableList<ReservaStock> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " WHERE rs.idRequisicao=? ORDER BY rs.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRequisicao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapReserva(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar reservas da requisicao: " + ex);
        }
        return lista;
    }

    @Override
    public ObservableList<ReservaStock> getReservasAtivas(Integer idProduto, Integer idArmazem) {
        ObservableList<ReservaStock> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " WHERE rs.estado = 'Reservado' "
                + "AND (? IS NULL OR rs.idProduto = ?) "
                + "AND (? IS NULL OR rs.idArmazem = ?) "
                + "ORDER BY rs.dataReserva DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableInt(ps, 1, idProduto);
            setNullableInt(ps, 3, idArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapReserva(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar reservas ativas: " + ex);
        }
        return lista;
    }

    @Override
    public boolean updateEstadoByRequisicao(int idRequisicao, String estado) {
        String sql = "UPDATE reserva_stock SET estado=? WHERE idRequisicao=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idRequisicao);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado das reservas: " + ex);
            return false;
        }
    }

    private void fillStatement(PreparedStatement ps, ReservaStock entity) throws SQLException {
        ps.setInt(1, entity.getProduto().getId());
        ps.setInt(2, entity.getArmazem().getId());
        ps.setInt(3, entity.getRequisicao().getId());
        ps.setInt(4, entity.getQuantidade());
        ps.setString(5, entity.getEstado());
        ps.setString(6, entity.getDataReserva());
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
        return "SELECT rs.id, rs.idProduto, rs.idArmazem, rs.idRequisicao, rs.quantidade, rs.estado, rs.dataReserva, "
                + "p.nome AS nomeProduto, a.nome AS nomeArmazem, r.estado AS estadoRequisicao "
                + "FROM reserva_stock rs "
                + "JOIN produto p ON p.id = rs.idProduto "
                + "JOIN armazem a ON a.id = rs.idArmazem "
                + "JOIN requisicao r ON r.id = rs.idRequisicao";
    }

    private ReservaStock mapReserva(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("idProduto"));
        produto.setNome(rs.getString("nomeProduto"));

        Armazem armazem = new Armazem();
        armazem.setId(rs.getInt("idArmazem"));
        armazem.setNome_arm(rs.getString("nomeArmazem"));

        Requisicao requisicao = new Requisicao();
        requisicao.setId(rs.getInt("idRequisicao"));
        requisicao.setEstado(rs.getString("estadoRequisicao"));

        ReservaStock reserva = new ReservaStock();
        reserva.setId(rs.getInt("id"));
        reserva.setProduto(produto);
        reserva.setArmazem(armazem);
        reserva.setRequisicao(requisicao);
        reserva.setQuantidade(rs.getInt("quantidade"));
        reserva.setEstado(rs.getString("estado"));
        reserva.setDataReserva(rs.getString("dataReserva"));
        return reserva;
    }
}
