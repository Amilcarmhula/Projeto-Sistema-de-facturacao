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
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoVenda;
import jminvsm.model.produto.Produto;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.precoVenda.ServicePrecoVenda;
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
    public ObservableList<PrecoVenda> getEntityByID(int idProduto, int idArmazem) {
        ObservableList<PrecoVenda> lista = FXCollections.observableArrayList();
        PrecoVenda pv = null;
        try {
            sql = "select * from precovenda where idProduto=? and idArmazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,  idProduto);
            ps.setInt(2,  idArmazem);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                //melhorar esta funcao
                pv = new PrecoVenda();
                pv.setId(result.getInt("id"));
                pv.setPrecoNormal(result.getDouble("precoNormal"));
                pv.setPrecoVenda(result.getDouble("precoVenda"));
                pv.setPrecoFinal(result.getDouble("precoFinal"));
                pv.setDataValidade(result.getString("dataValidade"));
                pv.setEstado(result.getString("estado"));
                Produto p = new Produto();
                p.setId(result.getInt("idProduto"));
                pv.setProduto(p);
                Armazem a = new Armazem();
                a.setId(result.getInt("idArmazem"));
                pv.setArmazem(a);
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                pv.setUsuario(u);
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
