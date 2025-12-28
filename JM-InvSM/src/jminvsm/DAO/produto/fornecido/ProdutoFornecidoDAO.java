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
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecidoDAO implements ProdutoFornecidoDAOImpl<ProdutoFornecido> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoFornecidoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(ProdutoFornecido t) {
        boolean isSuccess = false;
        try {
            sql = "insert into produto_fornecido (quantidade,data_fornecida,custo, fk_id_armazem,fk_id_produto, "
                    + "fk_id_fornecedor,fk_id_usuario,unidade_fornecida,qtd_por_unidade) value (?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setInt(1, t.getQuantidade());
            ps.setString(2, t.getData_fornecida());
            ps.setDouble(3, t.getCusto());
            ps.setInt(4, t.getArmazem().getId());
            ps.setInt(5, t.getProduto().getId());
            ps.setInt(6, t.getFornecedor().getId());
            ps.setInt(7, t.getUsuario().getId());
            ps.setString(8, t.getUnidade_fornecida());
            ps.setInt(9, t.getQtd_por_unidade());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
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
        try {
            sql = "SELECT SUM(CASE WHEN m.tipoMovimento = 'Entrada' THEN (m.quantidade * p.unidadePorTipo) ELSE 0 END) AS entradas, "
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
            ps = con.prepareStatement(sql);

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
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                mapa.put("entradas", result.getInt("entradas"));
                mapa.put("saidas", result.getInt("saidas"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
//            AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro Dados", "Erro ao carregar movimentos de produto: " + ex);
        }
        return mapa;
    }
    
//    public static void main(String[] args) throws SQLException {
//        System.out.println("Teste");
//        ProdutoFornecidoDAO d = new ProdutoFornecidoDAO();
//        Map<String, Integer> mapinha = d.getMovimentos(1, 1);
//        for (Map.Entry<String, Integer> entry : mapinha.entrySet()) {
//            System.out.println(entry.getKey()+": "+entry.getValue());
//            
//        }
//
//    }
}

//    @Override
//    public boolean updateEntityByID(ProdutoFornecido t, int id) {
//        boolean isSuccess = false;
//        try {
//            sql = "update produto_fornecido set quantidade=?,data_fornecida=?,custo=?, fk_id_armazem=?, "
//                    + "fk_id_fornecedor=?,fk_id_usuario=? where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, t.getQuantidade());
//            ps.setString(2, t.getData_fornecida());
//            ps.setDouble(3, t.getCusto());
//            ps.setInt(4, t.getArmazem().getId());
//            ps.setInt(5, t.getFornecedor().getId());
//            ps.setInt(6, t.getUsuario().getId());
//            int rowsAffected = ps.executeUpdate();
//
//            if (rowsAffected > 0) {
//                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
//            }
//
//        } catch (SQLException ex) {
//            showDialog("Erro persistencia", "Detalhes: " + ex);
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return isSuccess;
//    }

//}
