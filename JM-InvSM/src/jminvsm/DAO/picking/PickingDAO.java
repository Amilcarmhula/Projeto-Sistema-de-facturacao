package jminvsm.dao.picking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.picking.Picking;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.model.usuario.Usuario;
import jminvsm.util.Conexao;

public class PickingDAO implements PickingDAOImpl {

    @Override
    public boolean addEntity(Picking entity) {
        String sql = "INSERT INTO picking (tipo,estado,idOperador,dataInicio,dataFim,idRequisicao) VALUES (?,?,?,?,?,?)";
        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                fillStatement(ps, entity);
                ps.executeUpdate();
            }
            if (entity.getRequisicao() != null && entity.getRequisicao().getId() > 0) {
                try (PreparedStatement ps = con.prepareStatement("UPDATE requisicao SET estado='EmPicking' WHERE id=?")) {
                    ps.setInt(1, entity.getRequisicao().getId());
                    ps.executeUpdate();
                }
            }
            con.commit();
            return true;
        } catch (SQLException ex) {
            rollback(con);
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar picking: " + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }

    @Override
    public boolean updateEntityByID(Picking entity, int id) {
        String sql = "UPDATE picking SET tipo=?,estado=?,idOperador=?,dataInicio=?,dataFim=?,idRequisicao=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar picking: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Picking getEntityByID(int id) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM picking WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPicking(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar picking: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public Picking getPickingByRequisicao(int idRequisicao) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM picking WHERE idRequisicao=? ORDER BY id DESC LIMIT 1")) {
            ps.setInt(1, idRequisicao);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPicking(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar picking da requisicao: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ObservableList<Picking> getPickingsByEstado(String estado) {
        ObservableList<Picking> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM picking WHERE estado=? ORDER BY id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapPicking(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar pickings: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public boolean updateEstadoByRequisicao(int idRequisicao, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE picking SET estado=? WHERE idRequisicao=?")) {
            ps.setString(1, estado);
            ps.setInt(2, idRequisicao);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado do picking: " + ex.getMessage());
            return false;
        }
    }

    private Picking mapPicking(ResultSet rs) throws SQLException {
        Picking picking = new Picking();
        picking.setId(rs.getInt("id"));
        picking.setTipo(rs.getString("tipo"));
        picking.setEstado(rs.getString("estado"));
        Usuario operador = new Usuario();
        operador.setId(rs.getInt("idOperador"));
        picking.setOperador(rs.wasNull() ? null : operador);
        picking.setDataInicio(rs.getString("dataInicio"));
        picking.setDataFim(rs.getString("dataFim"));
        Requisicao requisicao = new Requisicao();
        requisicao.setId(rs.getInt("idRequisicao"));
        picking.setRequisicao(rs.wasNull() ? null : requisicao);
        return picking;
    }

    private void fillStatement(PreparedStatement ps, Picking picking) throws SQLException {
        ps.setString(1, picking.getTipo());
        ps.setString(2, picking.getEstado());
        if (picking.getOperador() == null || picking.getOperador().getId() <= 0) {
            ps.setNull(3, java.sql.Types.INTEGER);
        } else {
            ps.setInt(3, picking.getOperador().getId());
        }
        ps.setString(4, picking.getDataInicio());
        ps.setString(5, picking.getDataFim());
        if (picking.getRequisicao() == null || picking.getRequisicao().getId() <= 0) {
            ps.setNull(6, java.sql.Types.INTEGER);
        } else {
            ps.setInt(6, picking.getRequisicao().getId());
        }
    }

    private void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro transacao", "Erro ao reverter transacao: " + ex.getMessage());
        }
    }

    private void close(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro conexao", "Erro ao fechar conexao: " + ex.getMessage());
        }
    }
}
