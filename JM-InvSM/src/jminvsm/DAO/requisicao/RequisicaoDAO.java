package jminvsm.dao.requisicao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.model.requisicao.Requisicao;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class RequisicaoDAO implements RequisicaoDAOImpl {

    @Override
    public boolean addEntity(Requisicao entity) {
        String sql = "INSERT INTO requisicao (dataRequisicao,dataNecessidade,estado,motivo) VALUES (?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addEntityWithItems(Requisicao requisicao) {
        String sqlReq = "INSERT INTO requisicao (dataRequisicao,dataNecessidade,estado,motivo) VALUES (?,?,?,?)";
        String sqlItem = "INSERT INTO itemRequisicao (quantidade,idRequisicao,idProduto,idArmazem) VALUES (?,?,?,?)";
        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sqlReq, Statement.RETURN_GENERATED_KEYS)) {
                fillStatement(ps, requisicao);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        requisicao.setId(keys.getInt(1));
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                for (ItemRequisicao item : requisicao.getItens()) {
                    ps.setInt(1, item.getQuantidade());
                    ps.setInt(2, requisicao.getId());
                    ps.setInt(3, item.getProduto().getId());
                    ps.setInt(4, item.getArmazem().getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
            return true;
        } catch (SQLException ex) {
            rollback(con);
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar requisicao com itens: " + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }

    @Override
    public boolean updateEntityByID(Requisicao entity, int id) {
        String sql = "UPDATE requisicao SET dataRequisicao=?,dataNecessidade=?,estado=?,motivo=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE requisicao SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado da requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean aprovarRequisicao(int id) {
        return callProcedure("AprovarRequisicao", id);
    }

    @Override
    public boolean cancelarRequisicao(int id) {
        return callProcedure("CancelarRequisicao", id);
    }

    @Override
    public boolean confirmarPicking(int idRequisicao) {
        return callProcedure("ConfirmarPicking", idRequisicao);
    }

    @Override
    public ObservableList<Requisicao> listAllEntities() {
        return queryRequisicoes("SELECT * FROM requisicao WHERE COALESCE(deleteStatus,0)=0 ORDER BY id DESC");
    }

    @Override
    public ObservableList<Requisicao> getEntitiesByEstado(String estado) {
        ObservableList<Requisicao> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM requisicao WHERE COALESCE(deleteStatus,0)=0 AND estado=? ORDER BY id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRequisicao(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar requisicoes por estado: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Requisicao getEntityByID(int id) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM requisicao WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Requisicao requisicao = mapRequisicao(rs);
                    requisicao.setItens(getItens(id));
                    return requisicao;
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar requisicao: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("requisicao", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ItemRequisicao> getItens(int idRequisicao) {
        ObservableList<ItemRequisicao> lista = FXCollections.observableArrayList();
        String sql = "SELECT ir.*, p.nome AS produtoNome, a.nome AS armazemNome "
                + "FROM itemRequisicao ir LEFT JOIN produto p ON p.id=ir.idProduto "
                + "LEFT JOIN armazem a ON a.id=ir.idArmazem WHERE ir.idRequisicao=? ORDER BY ir.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRequisicao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapItem(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar itens da requisicao: " + ex.getMessage());
        }
        return lista;
    }

    private ObservableList<Requisicao> queryRequisicoes(String sql) {
        ObservableList<Requisicao> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRequisicao(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar requisicoes: " + ex.getMessage());
        }
        return lista;
    }

    private boolean callProcedure(String name, int id) {
        try (Connection con = Conexao.getConexao(); CallableStatement cs = con.prepareCall("{CALL " + name + "(?)}")) {
            cs.setInt(1, id);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de processamento", "Erro ao executar " + name + ": " + ex.getMessage());
            return false;
        }
    }

    private Requisicao mapRequisicao(ResultSet rs) throws SQLException {
        Requisicao requisicao = new Requisicao();
        requisicao.setId(rs.getInt("id"));
        requisicao.setDataRequisicao(rs.getString("dataRequisicao"));
        requisicao.setDataNecessidade(rs.getString("dataNecessidade"));
        requisicao.setEstado(rs.getString("estado"));
        requisicao.setMotivo(rs.getString("motivo"));
        return requisicao;
    }

    private ItemRequisicao mapItem(ResultSet rs) throws SQLException {
        ItemRequisicao item = new ItemRequisicao();
        item.setId(rs.getInt("id"));
        item.setQuantidade(rs.getInt("quantidade"));
        Requisicao requisicao = new Requisicao();
        requisicao.setId(rs.getInt("idRequisicao"));
        item.setRequisicao(requisicao);
        Produto produto = new Produto();
        produto.setId(rs.getInt("idProduto"));
        produto.setNome(rs.getString("produtoNome"));
        item.setProduto(produto);
        Armazem armazem = new Armazem();
        armazem.setId(rs.getInt("idArmazem"));
        armazem.setNome_arm(rs.getString("armazemNome"));
        item.setArmazem(armazem);
        return item;
    }

    private void fillStatement(PreparedStatement ps, Requisicao requisicao) throws SQLException {
        ps.setString(1, requisicao.getDataRequisicao());
        ps.setString(2, requisicao.getDataNecessidade());
        ps.setString(3, requisicao.getEstado());
        ps.setString(4, requisicao.getMotivo());
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
