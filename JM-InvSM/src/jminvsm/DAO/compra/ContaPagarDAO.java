package jminvsm.dao.compra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.compra.ContaPagar;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class ContaPagarDAO implements ContaPagarDAOImpl {

    private static final String SELECT_BASE = "SELECT cp.*, ff.numero AS facturaNumero, f.razaosocial AS fornecedorNome "
            + "FROM conta_pagar cp "
            + "LEFT JOIN factura_fornecedor ff ON ff.id=cp.idFacturaFornecedor "
            + "LEFT JOIN fornecedor f ON f.id=cp.idFornecedor ";

    @Override
    public boolean addEntity(ContaPagar entity) {
        String sql = "INSERT INTO conta_pagar (idFacturaFornecedor,idFornecedor,dataEmissao,dataVencimento,"
                + "valorOriginal,valorPago,saldoPendente,estado,observacao) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar conta a pagar: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEntityByID(ContaPagar entity, int id) {
        String sql = "UPDATE conta_pagar SET idFacturaFornecedor=?,idFornecedor=?,dataEmissao=?,dataVencimento=?,"
                + "valorOriginal=?,valorPago=?,saldoPendente=?,estado=?,observacao=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(10, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar conta a pagar: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE conta_pagar SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado da conta: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ContaPagar> listAllEntities() {
        return queryContas(SELECT_BASE + "WHERE COALESCE(cp.deleteStatus,0)=0 ORDER BY cp.id DESC");
    }

    @Override
    public ObservableList<ContaPagar> getEntitiesByEstado(String estado) {
        return queryContas(SELECT_BASE + "WHERE COALESCE(cp.deleteStatus,0)=0 AND cp.estado='" + estado + "' ORDER BY cp.id DESC");
    }

    @Override
    public ObservableList<ContaPagar> getEntitiesByFornecedor(int idFornecedor) {
        ObservableList<ContaPagar> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE COALESCE(cp.deleteStatus,0)=0 AND cp.idFornecedor=? ORDER BY cp.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapConta(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar contas por fornecedor: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public ContaPagar getEntityByID(int id) {
        String sql = SELECT_BASE + "WHERE cp.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return CompraDaoSupport.mapConta(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar conta a pagar: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("conta_pagar", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover conta a pagar: " + ex.getMessage());
            return false;
        }
    }

    private ObservableList<ContaPagar> queryContas(String sql) {
        ObservableList<ContaPagar> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapConta(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar contas a pagar: " + ex.getMessage());
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, ContaPagar conta) throws SQLException {
        ps.setInt(1, conta.getFacturaFornecedor().getId());
        ps.setInt(2, conta.getFornecedor().getId());
        ps.setString(3, conta.getDataEmissao());
        ps.setString(4, conta.getDataVencimento());
        ps.setDouble(5, conta.getValorOriginal());
        ps.setDouble(6, conta.getValorPago());
        ps.setDouble(7, conta.getSaldoPendente());
        ps.setString(8, conta.getEstado());
        ps.setString(9, conta.getObservacao());
    }
}
