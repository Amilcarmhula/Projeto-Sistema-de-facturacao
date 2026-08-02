package jminvsm.dao.compra;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.compra.PagamentoFornecedor;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class PagamentoFornecedorDAO implements PagamentoFornecedorDAOImpl {

    private static final String SELECT_BASE = "SELECT pf.*, f.razaosocial AS fornecedorNome, mp.nome AS metodoNome, "
            + "cb.nome_banco AS contaBancariaNome FROM pagamento_fornecedor pf "
            + "LEFT JOIN fornecedor f ON f.id=pf.idFornecedor "
            + "LEFT JOIN metodopagamento mp ON mp.id=pf.idMetodoPagamento "
            + "LEFT JOIN contabancaria cb ON cb.id=pf.idContaBancaria ";

    @Override
    public boolean addEntity(PagamentoFornecedor entity) {
        String sql = "INSERT INTO pagamento_fornecedor (idContaPagar,idFornecedor,dataPagamento,valorPago,"
                + "idMetodoPagamento,origemPagamento,idContaBancaria,referencia,descricao,estado,idUsuario) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar pagamento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEntityByID(PagamentoFornecedor entity, int id) {
        String sql = "UPDATE pagamento_fornecedor SET idContaPagar=?,idFornecedor=?,dataPagamento=?,valorPago=?,"
                + "idMetodoPagamento=?,origemPagamento=?,idContaBancaria=?,referencia=?,descricao=?,estado=?,idUsuario=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(12, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar pagamento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean registarPagamento(int idContaPagar, String dataPagamento, double valorPago,
            int idMetodoPagamento, String origemPagamento, Integer idContaBancaria,
            String referencia, String descricao, int idUsuario) {
        try (Connection con = Conexao.getConexao();
                CallableStatement cs = con.prepareCall("{CALL RegistarPagamentoFornecedor(?,?,?,?,?,?,?,?,?)}")) {
            cs.setInt(1, idContaPagar);
            cs.setString(2, dataPagamento);
            cs.setDouble(3, valorPago);
            cs.setInt(4, idMetodoPagamento);
            cs.setString(5, origemPagamento);
            if (idContaBancaria == null || idContaBancaria <= 0) {
                cs.setNull(6, java.sql.Types.INTEGER);
            } else {
                cs.setInt(6, idContaBancaria);
            }
            cs.setString(7, referencia);
            cs.setString(8, descricao);
            cs.setInt(9, idUsuario);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de processamento", "Erro ao registar pagamento: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<PagamentoFornecedor> listAllEntities() {
        return queryPagamentos(SELECT_BASE + "WHERE COALESCE(pf.deleteStatus,0)=0 ORDER BY pf.id DESC");
    }

    @Override
    public ObservableList<PagamentoFornecedor> getEntitiesByContaPagar(int idContaPagar) {
        ObservableList<PagamentoFornecedor> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE COALESCE(pf.deleteStatus,0)=0 AND pf.idContaPagar=? ORDER BY pf.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContaPagar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapPagamento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar pagamentos por conta: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public ObservableList<PagamentoFornecedor> getEntitiesByFornecedor(int idFornecedor) {
        ObservableList<PagamentoFornecedor> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE COALESCE(pf.deleteStatus,0)=0 AND pf.idFornecedor=? ORDER BY pf.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapPagamento(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar pagamentos por fornecedor: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public PagamentoFornecedor getEntityByID(int id) {
        String sql = SELECT_BASE + "WHERE pf.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return CompraDaoSupport.mapPagamento(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar pagamento: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("pagamento_fornecedor", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover pagamento: " + ex.getMessage());
            return false;
        }
    }

    private ObservableList<PagamentoFornecedor> queryPagamentos(String sql) {
        ObservableList<PagamentoFornecedor> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapPagamento(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar pagamentos: " + ex.getMessage());
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, PagamentoFornecedor pagamento) throws SQLException {
        ps.setInt(1, pagamento.getContaPagar().getId());
        ps.setInt(2, pagamento.getFornecedor().getId());
        ps.setString(3, pagamento.getDataPagamento());
        ps.setDouble(4, pagamento.getValorPago());
        ps.setInt(5, pagamento.getMetodoPagamento().getId());
        ps.setString(6, pagamento.getOrigemPagamento());
        if (pagamento.getContaBancaria() == null || pagamento.getContaBancaria().getId() <= 0) {
            ps.setNull(7, java.sql.Types.INTEGER);
        } else {
            ps.setInt(7, pagamento.getContaBancaria().getId());
        }
        ps.setString(8, pagamento.getReferencia());
        ps.setString(9, pagamento.getDescricao());
        ps.setString(10, pagamento.getEstado());
        if (pagamento.getUsuario() == null || pagamento.getUsuario().getId() <= 0) {
            ps.setNull(11, java.sql.Types.INTEGER);
        } else {
            ps.setInt(11, pagamento.getUsuario().getId());
        }
    }
}
