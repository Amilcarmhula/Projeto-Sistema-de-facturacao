/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.preco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.preco.PrecoVenda;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class PrecoVendaDAO implements PrecoVendaDAOImpl<PrecoVenda> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public PrecoVendaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(PrecoVenda x) {
        boolean isSuccess = false;
        try {
            sql = "insert into precovenda (precoNormal,data_inicio, data_fim, "
                    + "fk_id_produto ,fk_id_usuario,precoInclTaxa,precoExclTaxa) value (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, x.getPreco());
            ps.setString(2, x.getData_inicio());
            ps.setString(3, x.getData_fim());
            ps.setInt(4, x.getProduto().getId());
            ps.setInt(5, x.getUsuario().getId());
            ps.setDouble(6, x.getPrecoInclTaxa());
            ps.setDouble(7, x.getPrecoExclTaxa());
            

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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

    @Override
    public boolean updateEntityByID(PrecoVenda x, int id) {
        boolean isSuccess = false;
        try {
            sql = "update precovenda set precoNormal=?,data_inicio=?, data_fim=?"
                    + " ,fk_id_usuario=?, precoInclTaxa=? ,precoExclTaxa=? where id=?";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, x.getPreco());
            ps.setString(2, x.getData_inicio());
            ps.setString(3, x.getData_fim());
            ps.setInt(4, x.getUsuario().getId());
            ps.setDouble(5, x.getPrecoInclTaxa());
            ps.setDouble(6, x.getPrecoExclTaxa());
            ps.setInt(7, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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

    @Override
    public ObservableList<PrecoVenda> getEntityByProdutoID(int idProduto) {
        ObservableList<PrecoVenda> lista = FXCollections.observableArrayList();
        PrecoVenda pv = null;
        try {
            sql = "select * from precovenda where fk_id_produto=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,  idProduto);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                //melhorar esta funcao
                pv = new PrecoVenda();
                pv.setId(result.getInt("id"));
                pv.setPreco(result.getDouble("precoNormal"));
                pv.setPrecoInclTaxa(result.getDouble("precoInclTaxa"));
                pv.setPrecoExclTaxa(result.getDouble("precoExclTaxa"));
                pv.setData_inicio(result.getString("data_inicio"));
                pv.setData_fim(result.getString("data_fim"));
                pv.setEstado(result.getString("estado"));
                lista.add(pv);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog( "Erro de consulta!", "Detalhes: " + e);
        }
        return lista;
    }
    //    public Agente searchEntityByName(String nome) {
//        Agente c = null;
//        try {
//            sql = "select * from agente where nome LIKE ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + nome + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                c = new Agente();
//                c.setId(result.getInt("id"));
//                c.setTipo(result.getString("tipo"));
//                c.setApelido(result.getString("apelido"));
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setNome(result.getString("nome"));
//                c.setNuit(result.getInt("nuit"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Falha ao Bucar agente: " + e,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return c;
//    }

    @Override
    public boolean deleteEntityByUSER(int id) {
        boolean isSuccess = false;
        try {
            sql = "update precovenda set estado='desactivo' where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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
   

    


}
