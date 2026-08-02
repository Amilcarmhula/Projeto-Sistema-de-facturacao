package jminvsm.dao.requisicao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.requisicao.ItemRequisicao;
import jminvsm.util.Conexao;
import jminvsm.util.SoftDeleteUtils;

public class ItemRequisicaoDAO implements ItemRequisicaoDAOImpl {

    private final RequisicaoDAO requisicaoDao = new RequisicaoDAO();

    @Override
    public boolean addEntity(ItemRequisicao entity) {
        String sql = "INSERT INTO itemRequisicao (quantidade,idRequisicao,idProduto,idArmazem) VALUES (?,?,?,?)";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao adicionar item da requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEntityByID(ItemRequisicao entity, int id) {
        String sql = "UPDATE itemRequisicao SET quantidade=?,idRequisicao=?,idProduto=?,idArmazem=? WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, entity);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar item da requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ItemRequisicao getEntityByID(int id) {
        String sql = "SELECT idRequisicao FROM itemRequisicao WHERE id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    for (ItemRequisicao item : getItensByRequisicao(rs.getInt("idRequisicao"))) {
                        if (item.getId() == id) {
                            return item;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar item da requisicao: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteEntity(int id) {
        try {
            return SoftDeleteUtils.softDelete("itemRequisicao", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao remover item da requisicao: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<ItemRequisicao> getItensByRequisicao(int idRequisicao) {
        return requisicaoDao.getItens(idRequisicao);
    }

    private void fillStatement(PreparedStatement ps, ItemRequisicao item) throws SQLException {
        ps.setInt(1, item.getQuantidade());
        ps.setInt(2, item.getRequisicao().getId());
        ps.setInt(3, item.getProduto().getId());
        ps.setInt(4, item.getArmazem().getId());
    }
}
