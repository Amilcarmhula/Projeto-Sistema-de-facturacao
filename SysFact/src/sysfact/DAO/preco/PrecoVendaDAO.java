/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.preco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;

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
    public void addEntity(PrecoVenda x) {
        try {
//            sql = "insert into precovenda (preco,data_inicio, data_fim, "
//                    + "fk_id_produto,fk_id_servico ,fk_id_usuario) value (?,?,?,?,?,?)";
            sql = "insert into precovenda (preco,data_inicio, data_fim, "
                    + "fk_id_produto ,fk_id_usuario) value (?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, x.getPreco());
            ps.setString(2, x.getData_inicio());
            ps.setString(3, x.getData_fim());
            ps.setInt(4, x.getProduto().getId());
//            ps.setInt(5, x.getServico().getId());
            ps.setInt(5, x.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Preco: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(PrecoVenda x) {
        try {
//            sql = "insert into precovenda (preco,data_inicio, data_fim, "
//                    + "fk_id_produto,fk_id_servico ,fk_id_usuario) value (?,?,?,?,?,?)";
            sql = "update precovenda set preco=?, data_inicio=?, data_fim=?, "
                    + "fk_id_produto=? ,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, x.getPreco());
            ps.setString(2, x.getData_inicio());
            ps.setString(3, x.getData_fim());
            ps.setInt(4, x.getProduto().getId());
//            ps.setInt(5, x.getServico().getId());
            ps.setInt(5, x.getUsuario().getId());
            ps.setInt(6, x.getId());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar Preco: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public PrecoVenda getEntityByProdutoID(PrecoVenda x) {
        PrecoVenda pv = null;
        try {
            sql = "select * from precovenda where fk_id_produto=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,  x.getProduto().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                pv = new PrecoVenda();
                pv.setId(result.getInt("id"));
                pv.setPreco(result.getDouble("preco"));
                pv.setData_inicio(result.getString("data_inicio"));
                pv.setData_fim(result.getString("data_fim"));
                Produto p = new Produto();
                p.setId(result.getInt("fk_id_produto"));
                pv.setProduto(p);
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                pv.setUsuario(u);
                pv.setData_criacao(result.getString("data_criacao"));
                pv.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar Preco: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return pv;
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
    public void deleteEntityByUSER(PrecoVenda x) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    


}
