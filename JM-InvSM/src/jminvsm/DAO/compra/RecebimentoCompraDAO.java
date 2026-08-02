package jminvsm.dao.compra;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.compra.ItemRecebimentoCompra;
import jminvsm.model.compra.RecebimentoCompra;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class RecebimentoCompraDAO implements RecebimentoCompraDAOImpl {

    private static final String SELECT_BASE = "SELECT rc.*, oc.numero AS ordemNumero, f.razaosocial AS fornecedorNome "
            + "FROM recebimento_compra rc "
            + "LEFT JOIN ordem_compra oc ON oc.id=rc.idOrdemCompra "
            + "LEFT JOIN fornecedor f ON f.id=rc.idFornecedor ";

    @Override
    public boolean addEntity(RecebimentoCompra entity) {
        String sql = "INSERT INTO recebimento_compra (numero,idOrdemCompra,idFornecedor,dataRecebimento,"
                + "documentoFornecedor,estado,totalRecebido,observacao,idUsuario) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar recebimento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addEntityWithItems(RecebimentoCompra recebimentoCompra) {
        String sqlRecebimento = "INSERT INTO recebimento_compra (numero,idOrdemCompra,idFornecedor,dataRecebimento,"
                + "documentoFornecedor,estado,totalRecebido,observacao,idUsuario) VALUES (?,?,?,?,?,?,?,?,?)";
        String sqlItem = "INSERT INTO item_recebimento_compra (idRecebimentoCompra,idItemOrdemCompra,idProduto,"
                + "idArmazem,quantidadeRecebida,custoUnitario,codigoLote,idLote,estado) VALUES (?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sqlRecebimento, Statement.RETURN_GENERATED_KEYS)) {
                fillStatement(ps, recebimentoCompra);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        recebimentoCompra.setId(keys.getInt(1));
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                if (recebimentoCompra.getItens() != null) {
                    for (ItemRecebimentoCompra item : recebimentoCompra.getItens()) {
                        fillItemStatement(ps, item, recebimentoCompra.getId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            atualizarCustosOrdemCompra(con, recebimentoCompra.getId());
            atualizarTotalRecebido(con, recebimentoCompra.getId());
            con.commit();
            return true;
        } catch (SQLException ex) {
            rollback(con);
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar recebimento com itens: " + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }

    @Override
    public boolean updateEntityByID(RecebimentoCompra entity, int id) {
        String sql = "UPDATE recebimento_compra SET numero=?,idOrdemCompra=?,idFornecedor=?,dataRecebimento=?,"
                + "documentoFornecedor=?,estado=?,totalRecebido=?,observacao=?,idUsuario=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(10, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar recebimento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean confirmarRecebimento(int idRecebimento) {
        try (Connection con = Conexao.getConexao();
                CallableStatement cs = con.prepareCall("{CALL ConfirmarRecebimentoCompra(?)}")) {
            cs.setInt(1, idRecebimento);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de processamento", "Erro ao confirmar recebimento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE recebimento_compra SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado do recebimento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<RecebimentoCompra> listAllEntities() {
        return queryRecebimentos(SELECT_BASE + "WHERE COALESCE(rc.deleteStatus,0)=0 ORDER BY rc.id DESC");
    }

    @Override
    public ObservableList<RecebimentoCompra> getEntitiesByEstado(String estado) {
        ObservableList<RecebimentoCompra> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE COALESCE(rc.deleteStatus,0)=0 AND rc.estado=? ORDER BY rc.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapRecebimento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar recebimentos por estado: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public RecebimentoCompra getEntityByID(int id) {
        String sql = SELECT_BASE + "WHERE rc.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RecebimentoCompra recebimento = CompraDaoSupport.mapRecebimento(rs);
                    recebimento.setItens(getItens(id));
                    return recebimento;
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar recebimento: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("recebimento_compra", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover recebimento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ItemRecebimentoCompra> getItens(int idRecebimentoCompra) {
        ObservableList<ItemRecebimentoCompra> lista = FXCollections.observableArrayList();
        String sql = "SELECT irc.*, p.nome AS produtoNome, a.nome AS armazemNome "
                + "FROM item_recebimento_compra irc "
                + "LEFT JOIN produto p ON p.id=irc.idProduto "
                + "LEFT JOIN armazem a ON a.id=irc.idArmazem "
                + "WHERE irc.idRecebimentoCompra=? ORDER BY irc.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRecebimentoCompra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapItemRecebimento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar itens do recebimento: " + ex.getMessage());
        }
        return lista;
    }

    private ObservableList<RecebimentoCompra> queryRecebimentos(String sql) {
        ObservableList<RecebimentoCompra> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapRecebimento(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar recebimentos: " + ex.getMessage());
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, RecebimentoCompra recebimento) throws SQLException {
        ps.setString(1, recebimento.getNumero());
        ps.setInt(2, recebimento.getOrdemCompra().getId());
        ps.setInt(3, recebimento.getFornecedor().getId());
        ps.setString(4, recebimento.getDataRecebimento());
        ps.setString(5, recebimento.getDocumentoFornecedor());
        ps.setString(6, recebimento.getEstado());
        ps.setDouble(7, recebimento.getTotalRecebido());
        ps.setString(8, recebimento.getObservacao());
        setNullableInt(ps, 9, recebimento.getUsuario() != null ? recebimento.getUsuario().getId() : null);
    }

    private void fillItemStatement(PreparedStatement ps, ItemRecebimentoCompra item, int idRecebimento) throws SQLException {
        ps.setInt(1, idRecebimento);
        setNullableInt(ps, 2, item.getItemOrdemCompra() != null ? item.getItemOrdemCompra().getId() : null);
        ps.setInt(3, item.getProduto().getId());
        ps.setInt(4, item.getArmazem().getId());
        ps.setInt(5, item.getQuantidadeRecebida());
        ps.setDouble(6, item.getCustoUnitario());
        ps.setString(7, item.getCodigoLote());
        setNullableInt(ps, 8, item.getLote() != null ? item.getLote().getId() : null);
        ps.setString(9, item.getEstado());
    }

    private void atualizarCustosOrdemCompra(Connection con, int idRecebimento) throws SQLException {
        String sql = "UPDATE item_ordem_compra ioc JOIN item_recebimento_compra irc ON irc.idItemOrdemCompra=ioc.id "
                + "SET ioc.custoUnitario=irc.custoUnitario WHERE irc.idRecebimentoCompra=? AND COALESCE(ioc.custoUnitario,0)=0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRecebimento);
            ps.executeUpdate();
        }
    }

    private void atualizarTotalRecebido(Connection con, int idRecebimento) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE recebimento_compra SET totalRecebido=(SELECT COALESCE(SUM(quantidadeRecebida*custoUnitario),0) FROM item_recebimento_compra WHERE idRecebimentoCompra=?) WHERE id=?")) {
            ps.setInt(1, idRecebimento);
            ps.setInt(2, idRecebimento);
            ps.executeUpdate();
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null || value <= 0) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
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
