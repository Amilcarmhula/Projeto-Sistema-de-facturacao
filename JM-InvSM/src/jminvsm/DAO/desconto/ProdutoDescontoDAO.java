package jminvsm.dao.desconto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.produto.Produto;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoDescontoDAO {

    // CREATE
    public boolean addEntity(ProdutoDesconto t) {
        boolean isSuccess = false;
        String sql = "INSERT INTO produtodesconto (idProduto, idDesconto, dataInicio, dataFim,idArmazem) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getProduto().getId());
            ps.setInt(2, t.getDesconto().getId());
            ps.setString(3, t.getDataInicio());
            ps.setString(4, t.getDataFim());
            ps.setInt(5, t.getArmazem().getId());

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao inserir ProdutoDesconto:: " + ex);
        }
        return isSuccess;
    }

    public boolean callAplicaDesconto(ProdutoDesconto t) {
        boolean isSuccess = false;
        String sql = "CALL sp_aplicar_desconto_ao_preco(?, ?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getProduto().getId());
            ps.setInt(2, t.getArmazem().getId());

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao inserir: " + ex);
        }
        return isSuccess;
    }
    
    public boolean callDesativaDescontoProduto(ProdutoDesconto t) {
        boolean isSuccess = false;
        String sql = "CALL sp_desativaDescontoProduto(?, ?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getProduto().getId());
            ps.setInt(2, t.getArmazem().getId());

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao inserir: " + ex);
        }
        return isSuccess;
    }

    // READ ALL
    public ObservableList<ProdutoDesconto> getEntityByID(int idProduto) {
        ObservableList<ProdutoDesconto> lista = FXCollections.observableArrayList();
        ProdutoDesconto pd = null;
        String sql = "select p.nome as nomeProduto, p.descricao as descricaoProduto,"
                + "	a.nome as nomeArmazem, a.tipo as tipoArmazem, a.descricao as descicaoArmazem, "
                + " d.nome as nomeDesconto, "
                + " pd.dataInicio, pd.dataFim, d.percentagem "
                + "	from produtodesconto pd "
                + "	join produto p on pd.idProduto=p.id "
                + " join desconto d on pd.idDesconto=d.id "
                + " join armazem a on pd.idArmazem=a.id "
                + " where idProduto=? "
                + " AND pd.deleteStatus=0 "
                + " AND p.deleteStatus=0 "
                + " AND d.deleteStatus=0 "
                + " AND a.deleteStatus=0 ";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idProduto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pd = new ProdutoDesconto();
                    Produto p = new Produto();
                    p.setNome(rs.getString("nomeProduto"));
                    p.setDescricao(rs.getString("descricaoProduto"));
                    pd.setProduto(p);

                    Armazem a = new Armazem();
                    a.setNome_arm(rs.getString("nomeArmazem"));
                    a.setDescricao_arm(rs.getString("descicaoArmazem"));
                    a.setTipo(rs.getString("tipoArmazem"));
                    pd.setArmazem(a);

                    Desconto d = new Desconto();
                    d.setNome(rs.getString("nomeDesconto"));
                    d.setPercentagem(rs.getDouble("percentagem"));
                    pd.setDesconto(d);

                    pd.setDataInicio(rs.getString("dataInicio"));
                    pd.setDataFim(rs.getString("dataFim"));

                    lista.add(pd);
                }
            }
        } catch (SQLException e) {
            DaoErrorHandler.log("Erro de consulta!", "Detalhes: " + e);
        }
        return lista;
    }

    // DELETE
    public boolean delete(ProdutoDesconto t) {
        boolean isSuccess = false;
        String sql = "UPDATE produtodesconto SET deleteStatus=1, deletedAt=NOW(), deletedBy=? "
                + "WHERE idProduto=? AND idDesconto=? AND dataInicio=? AND dataFim=? AND idArmazem=? "
                + "AND deleteStatus=0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            Integer idUsuario = jminvsm.util.AuditoriaContext.getUsuarioActualId();
            if (idUsuario == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, idUsuario);
            }
            ps.setInt(2, t.getProduto().getId());
            ps.setInt(3, t.getDesconto().getId());
            ps.setString(4, t.getDataInicio());
            ps.setString(5, t.getDataFim());
            ps.setInt(6, t.getArmazem().getId());

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao apagar ProdutoDesconto: " + ex);
        }
        return isSuccess;
    }
}
