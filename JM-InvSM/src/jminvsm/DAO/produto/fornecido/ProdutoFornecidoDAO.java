/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.produto.fornecido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.util.AlertUtilities;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecidoDAO implements ProdutoFornecidoDAOImpl<ProdutoFornecido> {

    @Override
    public boolean addEntity(ProdutoFornecido t) {
        boolean isSuccess = false;
        String sql = "INSERT INTO lote (codigo, idProduto, idArmazem, idFornecedor, dataEntrada, quantidadeAtual, custoUnitario) value (?,?,?,?,?,?,?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getCodigo());
            ps.setInt(2, t.getProduto().getId());
            ps.setInt(3, t.getArmazem().getId());
            ps.setInt(4, t.getFornecedor().getId());
            ps.setString(5, t.getDataEntrada());
            ps.setInt(6, t.getQuantidadeActual());
            ps.setDouble(7, t.getCustoUnitario());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public ObservableList<ProdutoFornecido> getLotesForStock(Integer idProduto) {
        ObservableList<ProdutoFornecido> lista = FXCollections.observableArrayList();
        String sql = "SELECT "
                + "    l.id as idLote, "
                + "    l.codigo AS loteCode, "
                + "    a.nome AS armazem, "
                + "    l.dataEntrada, "
                + "    l.quantidadeInicial, "
                + "    l.quantidadeAtual, "
                + "    l.custoUnitario, "
                + "    l.valorTotal, "
                + "    l.estado "
                + "FROM lote l "
                + "JOIN armazem a ON a.id = l.idArmazem "
                + "WHERE l.idProduto = ? "
                + "ORDER BY l.dataEntrada ASC;";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                ps.setInt(1, idProduto);
                while (result.next()) {
                    ProdutoFornecido l = new ProdutoFornecido();
                    l.setId(result.getInt("idLote"));
                    l.setCodigo(result.getString("loteCode"));
                    Armazem a = new Armazem();
                    a.setNome_arm(result.getString("armazem"));
                    l.setArmazem(a);
                    l.setDataEntrada(result.getString("dataEntrada"));
                    l.setQuantidadeInicial(result.getInt("quantidadeInicial"));
                    l.setQuantidadeActual(result.getInt("quantidadeAtual"));
                    l.setCustoUnitario(result.getDouble("custoUnitario"));
                    l.setValorTotal(result.getDouble("valorTotal"));
                    l.setEstado(result.getString("estado"));
                    lista.add(l);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro Detalhes: " + ex);
            AlertUtilities.showDialog("Erro de consulta", "Detalhes: " + ex);

        }
        return lista;
    }

    /**
     *
     * @param cat ID da categoria que pretendemos consultar o movimento
     * @param arm ID do armazem que pretendemos consultar o movimento
     * @return um mapa de movimentos
     */
    @Override
    public Map<String, Integer> getMovimentos(Integer cat, Integer arm) {
        Map<String, Integer> mapa = new HashMap<>();
        String sql = "SELECT SUM(CASE WHEN m.tipoMovimento = 'Entrada' THEN (m.quantidade * p.unidadePorTipo) ELSE 0 END) AS entradas, "
                + "    SUM(CASE WHEN m.tipoMovimento = 'Saida' THEN (m.quantidade * p.unidadePorTipo) ELSE 0 END) AS saidas, "
                + "    MONTH(m.dataMovimento) AS mes, "
                + "    YEAR(m.dataMovimento) AS ano "
                + "FROM movimento m "
                + "INNER JOIN produto p   ON p.id = m.idProduto "
                + "INNER JOIN categoria c ON c.id = p.idCategoria "
                + "INNER JOIN armazem a   ON a.id = m.idArmazem "
                + "WHERE (? IS NULL OR c.id = ?) "
                + "    AND (? IS NULL OR a.id = ?) "
                + "    AND MONTH(m.dataMovimento) = MONTH(CURRENT_DATE()) "
                + "    AND YEAR(m.dataMovimento) = YEAR(CURRENT_DATE()) "
                + "GROUP BY ano, mes "
                + "ORDER BY ano, mes";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {

                // Se a categoria ou armazém não forem informados, passar valores nulos
                if (cat == null) {
                    ps.setNull(1, java.sql.Types.INTEGER); // Para ? IS NULL
                    ps.setNull(2, java.sql.Types.INTEGER); // Para c.id = ?
                } else {
                    ps.setInt(1, cat);  // Para ? IS NULL
                    ps.setInt(2, cat);  // Para c.id = ?
                }

                if (arm == null) {
                    ps.setNull(3, java.sql.Types.INTEGER); // Para ? IS NULL
                    ps.setNull(4, java.sql.Types.INTEGER); // Para a.id = ?
                } else {
                    ps.setInt(3, arm);  // Para ? IS NULL
                    ps.setInt(4, arm);  // Para a.id = ?
                }
                while (result.next()) {
                    mapa.put("entradas", result.getInt("entradas"));
                    mapa.put("saidas", result.getInt("saidas"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            showDialog("Erro Dados", "Erro ao carregar movimentos de produto: " + ex);
        }
        return mapa;
    }

    public ProdutoFornecido getLastCodeLote() {
        ProdutoFornecido pf = null;
        String sql = "select codigo from lote order by id DESC LIMIT 1";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    pf = new ProdutoFornecido();
                    pf.setCodigoForSave(result.getString("codigo"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return pf;
    }
}
