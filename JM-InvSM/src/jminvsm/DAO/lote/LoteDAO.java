package jminvsm.dao.lote;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.lote.Lote;
import jminvsm.model.produto.Produto;
import jminvsm.util.Conexao;
import jminvsm.dao.DaoErrorHandler;
public class LoteDAO implements LoteDAOImpl {

    @Override
    public boolean addEntity(Lote entity) {
        String sql = "INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, "
                + "quantidadeInicial, quantidadeAtual, custoUnitario, estado) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar lote: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateEntityByID(Lote entity, int id) {
        String sql = "UPDATE lote SET codigo=?, idProduto=?, idArmazem=?, idFornecedor=?, dataEntrada=?, "
                + "quantidadeInicial=?, quantidadeAtual=?, custoUnitario=?, estado=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(10, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar lote: " + ex);
            return false;
        }
    }

    @Override
    public ObservableList<Lote> listAllEntities() {
        ObservableList<Lote> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " ORDER BY l.dataEntrada ASC, l.id ASC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapLote(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar lotes: " + ex);
        }
        return lista;
    }

    @Override
    public Lote getEntityByID(int id) {
        String sql = baseSelect() + " AND l.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLote(rs);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar lote: " + ex);
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return jminvsm.util.SoftDeleteUtils.softDelete("lote", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover lote: " + ex);
            return false;
        }
    }

    @Override
    public Lote getLastEntity() {
        String sql = baseSelect() + " ORDER BY l.id DESC LIMIT 1";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapLote(rs);
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar ultimo lote: " + ex);
        }
        return null;
    }

    @Override
    public ObservableList<Lote> getLotesDisponiveis(Integer idProduto, Integer idArmazem) {
        ObservableList<Lote> lista = FXCollections.observableArrayList();
        String sql = baseSelect() + " AND l.quantidadeAtual > 0 "
                + "AND l.estado = 'Ativo' "
                + "AND (? IS NULL OR l.idProduto = ?) "
                + "AND (? IS NULL OR l.idArmazem = ?) "
                + "ORDER BY l.dataEntrada ASC, l.id ASC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableInt(ps, 1, idProduto);
            setNullableInt(ps, 3, idArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapLote(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar lotes disponiveis: " + ex);
        }
        return lista;
    }

    @Override
    public boolean updateQuantidadeAtual(int idLote, int quantidadeAtual) {
        String sql = "UPDATE lote SET quantidadeAtual=?, estado=IF(? <= 0, 'Esgotado', 'Ativo') WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantidadeAtual);
            ps.setInt(2, quantidadeAtual);
            ps.setInt(3, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar saldo do lote: " + ex);
            return false;
        }
    }

    private void fillStatement(PreparedStatement ps, Lote entity) throws SQLException {
        ps.setString(1, entity.getCodigo());
        ps.setInt(2, entity.getProduto().getId());
        ps.setInt(3, entity.getArmazem().getId());
        ps.setInt(4, entity.getFornecedor().getId());
        ps.setString(5, entity.getDataEntrada());
        ps.setInt(6, entity.getQuantidadeInicial());
        ps.setInt(7, entity.getQuantidadeAtual());
        ps.setBigDecimal(8, entity.getCustoUnitarioDecimal() != null ? entity.getCustoUnitarioDecimal() : BigDecimal.ZERO);
        ps.setString(9, entity.getEstado());
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
        return "SELECT l.id, l.codigo, l.idProduto, l.idArmazem, l.idFornecedor, l.dataEntrada, "
                + "l.quantidadeInicial, l.quantidadeAtual, l.custoUnitario, l.valorTotal, l.estado, "
                + "p.nome AS nomeProduto, a.nome AS nomeArmazem, f.razaosocial AS nomeFornecedor "
                + "FROM lote l "
                + "JOIN produto p ON p.id = l.idProduto "
                + "JOIN armazem a ON a.id = l.idArmazem "
                + "LEFT JOIN fornecedor f ON f.id = l.idFornecedor "
                + "WHERE l.deleteStatus = 0 "
                + "AND p.deleteStatus = 0 "
                + "AND a.deleteStatus = 0 "
                + "AND (f.id IS NULL OR f.deleteStatus = 0)";
    }

    private Lote mapLote(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("idProduto"));
        produto.setNome(rs.getString("nomeProduto"));

        Armazem armazem = new Armazem();
        armazem.setId(rs.getInt("idArmazem"));
        armazem.setNome_arm(rs.getString("nomeArmazem"));

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getInt("idFornecedor"));
        fornecedor.setRazaosocial_forn(rs.getString("nomeFornecedor"));

        Lote lote = new Lote();
        lote.setId(rs.getInt("id"));
        lote.setCodigo(rs.getString("codigo"));
        lote.setProduto(produto);
        lote.setArmazem(armazem);
        lote.setFornecedor(fornecedor);
        lote.setDataEntrada(rs.getString("dataEntrada"));
        lote.setQuantidadeInicial(rs.getInt("quantidadeInicial"));
        lote.setQuantidadeAtual(rs.getInt("quantidadeAtual"));
        lote.setCustoUnitario(rs.getBigDecimal("custoUnitario"));
        lote.setValorTotal(rs.getBigDecimal("valorTotal"));
        lote.setEstado(rs.getString("estado"));
        return lote;
    }
}
