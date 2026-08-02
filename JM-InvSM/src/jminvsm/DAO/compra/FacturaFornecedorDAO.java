package jminvsm.dao.compra;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.compra.FacturaFornecedor;
import jminvsm.model.compra.ItemFacturaFornecedor;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class FacturaFornecedorDAO implements FacturaFornecedorDAOImpl {

    private static final String SELECT_BASE = "SELECT ff.*, f.razaosocial AS fornecedorNome, oc.numero AS ordemNumero, "
            + "rc.numero AS recebimentoNumero FROM factura_fornecedor ff "
            + "LEFT JOIN fornecedor f ON f.id=ff.idFornecedor "
            + "LEFT JOIN ordem_compra oc ON oc.id=ff.idOrdemCompra "
            + "LEFT JOIN recebimento_compra rc ON rc.id=ff.idRecebimentoCompra ";

    @Override
    public boolean addEntity(FacturaFornecedor entity) {
        String sql = "INSERT INTO factura_fornecedor (numero,idFornecedor,idOrdemCompra,idRecebimentoCompra,dataEmissao,"
                + "dataVencimento,subtotal,desconto,imposto,total,valorPago,saldoPendente,estado,observacao,idUsuario) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar factura do fornecedor: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addEntityWithItems(FacturaFornecedor facturaFornecedor) {
        return addEntity(facturaFornecedor);
    }

    @Override
    public boolean updateEntityByID(FacturaFornecedor entity, int id) {
        String sql = "UPDATE factura_fornecedor SET numero=?,idFornecedor=?,idOrdemCompra=?,idRecebimentoCompra=?,dataEmissao=?,"
                + "dataVencimento=?,subtotal=?,desconto=?,imposto=?,total=?,valorPago=?,saldoPendente=?,estado=?,observacao=?,idUsuario=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(16, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar factura do fornecedor: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean criarDeRecebimento(String numeroFactura, int idRecebimento, String dataEmissao,
            String dataVencimento, int idUsuario) {
        try (Connection con = Conexao.getConexao();
                CallableStatement cs = con.prepareCall("{CALL CriarFacturaFornecedorDeRecebimento(?,?,?,?,?)}")) {
            cs.setString(1, numeroFactura);
            cs.setInt(2, idRecebimento);
            cs.setString(3, dataEmissao);
            cs.setString(4, dataVencimento);
            cs.setInt(5, idUsuario);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de processamento", "Erro ao criar factura do fornecedor: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE factura_fornecedor SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar estado da factura: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<FacturaFornecedor> listAllEntities() {
        return queryFacturas(SELECT_BASE + "WHERE COALESCE(ff.deleteStatus,0)=0 ORDER BY ff.id DESC");
    }

    @Override
    public ObservableList<FacturaFornecedor> getEntitiesByEstado(String estado) {
        ObservableList<FacturaFornecedor> lista = FXCollections.observableArrayList();
        String sql = SELECT_BASE + "WHERE COALESCE(ff.deleteStatus,0)=0 AND ff.estado=? ORDER BY ff.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapFactura(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar facturas por estado: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public FacturaFornecedor getEntityByID(int id) {
        String sql = SELECT_BASE + "WHERE ff.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FacturaFornecedor factura = CompraDaoSupport.mapFactura(rs);
                    factura.setItens(getItens(id));
                    return factura;
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar factura do fornecedor: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("factura_fornecedor", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover factura do fornecedor: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ItemFacturaFornecedor> getItens(int idFacturaFornecedor) {
        ObservableList<ItemFacturaFornecedor> lista = FXCollections.observableArrayList();
        String sql = "SELECT iff.*, p.nome AS produtoNome FROM item_factura_fornecedor iff "
                + "LEFT JOIN produto p ON p.id=iff.idProduto WHERE iff.idFacturaFornecedor=? ORDER BY iff.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFacturaFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(CompraDaoSupport.mapItemFactura(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar itens da factura: " + ex.getMessage());
        }
        return lista;
    }

    private ObservableList<FacturaFornecedor> queryFacturas(String sql) {
        ObservableList<FacturaFornecedor> lista = FXCollections.observableArrayList();
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(CompraDaoSupport.mapFactura(rs));
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar facturas: " + ex.getMessage());
        }
        return lista;
    }

    private void fillStatement(PreparedStatement ps, FacturaFornecedor factura) throws SQLException {
        ps.setString(1, factura.getNumero());
        ps.setInt(2, factura.getFornecedor().getId());
        setNullableInt(ps, 3, factura.getOrdemCompra() != null ? factura.getOrdemCompra().getId() : null);
        setNullableInt(ps, 4, factura.getRecebimentoCompra() != null ? factura.getRecebimentoCompra().getId() : null);
        ps.setString(5, factura.getDataEmissao());
        ps.setString(6, factura.getDataVencimento());
        ps.setDouble(7, factura.getSubtotal());
        ps.setDouble(8, factura.getDesconto());
        ps.setDouble(9, factura.getImposto());
        ps.setDouble(10, factura.getTotal());
        ps.setDouble(11, factura.getValorPago());
        ps.setDouble(12, factura.getSaldoPendente());
        ps.setString(13, factura.getEstado());
        ps.setString(14, factura.getObservacao());
        setNullableInt(ps, 15, factura.getUsuario() != null ? factura.getUsuario().getId() : null);
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null || value <= 0) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}
