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
import jminvsm.model.compra.ItemOrdemCompra;
import jminvsm.model.compra.OrdemCompra;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class OrdemCompraDAO implements OrdemCompraDAOImpl {

    private static final String SELECT_BASE = "SELECT oc.*, f.razaosocial AS fornecedorNome "
            + "FROM ordem_compra oc LEFT JOIN fornecedor f ON f.id = oc.idFornecedor ";

    @Override
    public boolean addEntity(OrdemCompra entity) {
        String sql = "INSERT INTO ordem_compra (numero,idRequisicao,idFornecedor,dataEmissao,"
                + "dataPrevistaEntrega,estado,subtotal,desconto,imposto,observacao,idUsuario) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar ordem de compra: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addEntityWithItems(OrdemCompra ordemCompra) {
        String sqlOrdem = "INSERT INTO ordem_compra (numero,idRequisicao,idFornecedor,dataEmissao,"
                + "dataPrevistaEntrega,estado,subtotal,desconto,imposto,observacao,idUsuario) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        String sqlItem = "INSERT INTO item_ordem_compra (idOrdemCompra,idItemRequisicao,idProduto,"
                + "idArmazem,quantidadeSolicitada,quantidadeRecebida,custoUnitario,desconto,imposto,estado) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sqlOrdem, Statement.RETURN_GENERATED_KEYS)) {
                fillStatement(ps, ordemCompra);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        ordemCompra.setId(keys.getInt(1));
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                if (ordemCompra.getItens() != null) {
                    for (ItemOrdemCompra item : ordemCompra.getItens()) {
                        fillItemStatement(ps, item, ordemCompra.getId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            atualizarSubtotal(con, ordemCompra.getId());
            con.commit();
            return true;
        } catch (SQLException ex) {
            rollback(con);
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar ordem de compra com itens: " + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }

    @Override
    public boolean updateEntityByID(OrdemCompra entity, int id) {
        String sql = "UPDATE ordem_compra SET numero=?,idRequisicao=?,idFornecedor=?,dataEmissao=?,"
                + "dataPrevistaEntrega=?,estado=?,subtotal=?,desconto=?,imposto=?,observacao=?,idUsuario=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(12, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar ordem de compra: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean criarDeRequisicao(int idRequisicao, int idFornecedor, String dataPrevistaEntrega, int idUsuario) {
        try (Connection con = Conexao.getConexao();
                CallableStatement cs = con.prepareCall("{CALL CriarOrdemCompraDeRequisicao(?,?,?,?)}")) {
            cs.setInt(1, idRequisicao);
            cs.setInt(2, idFornecedor);
            cs.setString(3, dataPrevistaEntrega);
            cs.setInt(4, idUsuario);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de processamento", "Erro ao criar ordem de compra: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean existeOrdemActivaParaRequisicaoFornecedor(int idRequisicao, int idFornecedor) {
        String sql = "SELECT COUNT(*) FROM ordem_compra WHERE idRequisicao=? AND idFornecedor=? "
                + "AND estado NOT IN ('Cancelada','Recebida')";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRequisicao);
            ps.setInt(2, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao validar ordem existente: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE ordem_compra SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado da ordem de compra: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<OrdemCompra> listAllEntities() {
        return queryOrdens(SELECT_BASE + "WHERE COALESCE(oc.deleteStatus,0)=0 ORDER BY oc.id DESC");
    }

    @Override
    public ObservableList<OrdemCompra> getEntitiesByEstado(String estado) {
        String sql = SELECT_BASE + "WHERE COALESCE(oc.deleteStatus,0)=0 AND oc.estado=? ORDER BY oc.id DESC";
        ObservableList<OrdemCompra> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapOrdem(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar ordens de compra por estado: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public OrdemCompra getEntityByID(int id) {
        String sql = SELECT_BASE + "WHERE oc.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrdemCompra ordem = CompraDaoSupport.mapOrdem(rs);
                    ordem.setItens(getItens(id));
                    return ordem;
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar ordem de compra: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("ordem_compra", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover ordem de compra: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ItemOrdemCompra> getItens(int idOrdemCompra) {
        String sql = "SELECT ioc.*, p.nome AS produtoNome, a.nome AS armazemNome "
                + "FROM item_ordem_compra ioc "
                + "LEFT JOIN produto p ON p.id=ioc.idProduto "
                + "LEFT JOIN armazem a ON a.id=ioc.idArmazem "
                + "WHERE ioc.idOrdemCompra=? ORDER BY ioc.id";
        ObservableList<ItemOrdemCompra> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrdemCompra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapItemOrdem(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar itens da ordem de compra: " + ex.getMessage());
        }
        return lista;
    }

    private ObservableList<OrdemCompra> queryOrdens(String sql) {
        ObservableList<OrdemCompra> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapOrdem(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar ordens de compra: " + ex.getMessage());
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, OrdemCompra ordem) throws SQLException {
        ps.setString(1, ordem.getNumero());
        setNullableInt(ps, 2, ordem.getRequisicao() != null ? ordem.getRequisicao().getId() : null);
        ps.setInt(3, ordem.getFornecedor().getId());
        ps.setString(4, ordem.getDataEmissao());
        ps.setString(5, ordem.getDataPrevistaEntrega());
        ps.setString(6, ordem.getEstado());
        ps.setDouble(7, ordem.getSubtotal());
        ps.setDouble(8, ordem.getDesconto());
        ps.setDouble(9, ordem.getImposto());
        ps.setString(10, ordem.getObservacao());
        setNullableInt(ps, 11, ordem.getUsuario() != null ? ordem.getUsuario().getId() : null);
    }

    private void fillItemStatement(PreparedStatement ps, ItemOrdemCompra item, int idOrdem) throws SQLException {
        ps.setInt(1, idOrdem);
        setNullableInt(ps, 2, item.getItemRequisicao() != null ? item.getItemRequisicao().getId() : null);
        ps.setInt(3, item.getProduto().getId());
        ps.setInt(4, item.getArmazem().getId());
        ps.setInt(5, item.getQuantidadeSolicitada());
        ps.setInt(6, item.getQuantidadeRecebida());
        ps.setDouble(7, item.getCustoUnitario());
        ps.setDouble(8, item.getDesconto());
        ps.setDouble(9, item.getImposto());
        ps.setString(10, item.getEstado());
    }

    private void atualizarSubtotal(Connection con, int idOrdem) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE ordem_compra SET subtotal=(SELECT COALESCE(SUM(quantidadeSolicitada*custoUnitario),0) FROM item_ordem_compra WHERE idOrdemCompra=?), total=(SELECT COALESCE(SUM(quantidadeSolicitada*custoUnitario),0) FROM item_ordem_compra WHERE idOrdemCompra=?) WHERE id=?")) {
            ps.setInt(1, idOrdem);
            ps.setInt(2, idOrdem);
            ps.setInt(3, idOrdem);
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
