/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.conexao.Conexao;
import sysfact.logical.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class UsuarioDAO implements DaoImpl<Usuario> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public UsuarioDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    public Usuario login(Usuario u) {
        Usuario user = null;
        try {
            sql = "select * from usuario where usuario=? and codigo=? and senha=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getUsuario());
            ps.setInt(2, u.getCodigo());
            ps.setInt(3, u.getSenha());

            ResultSet result = ps.executeQuery();
            while (result.next()) {
                user = new Usuario();
                user.setId(result.getInt("id"));
                user.setNome(result.getString("nome"));
                user.setUsuario(result.getString("usuario"));
                user.setSenha(result.getInt("senha"));
                user.setCodigo(result.getInt("codigo"));
                user.setData_criacao(result.getString("data_criacao"));
                user.setEstadoActive(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Usuario nao encontrado: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return user;
    }
    

    @Override
    public void addEntity(Usuario t) {
        try {
            sql = "insert into usuario (nome, usuario, senha, codigo) value (?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNome());
            ps.setString(2, t.getUsuario());
            ps.setInt(3, t.getSenha());
            ps.setInt(4, t.getCodigo());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Usuario: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Usuario t, int id) {
        try {
            sql = "update usuario set nome=?, usuario=?, senha=?, codigo=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getUsuario());
            ps.setInt(3, t.getSenha());
            ps.setInt(4, t.getCodigo());
            ps.setInt(5, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Usuario: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Usuario> listAllEntities() {
        List<Usuario> lista = new ArrayList<>();
        try {
            sql = "select * from usuario";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Usuario u = new Usuario();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setUsuario(result.getString("usuario"));
                u.setSenha(result.getInt("senha"));
                u.setCodigo(result.getInt("codigo"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setEstadoActive(result.getBoolean("active_or_not"));
                lista.add(u);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao Buscar Usuarios: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Usuario searchEntityByName(String name) {
        Usuario u = null;
        try {
            sql = "select * from usuario where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                u = new Usuario();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setUsuario(result.getString("usuario"));
                u.setSenha(result.getInt("senha"));
                u.setCodigo(result.getInt("codigo"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setEstadoActive(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Usuario: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return u;

    }

//    @Override
//    public List<Usuario> searchListEntityByName(String name) {
//        List<Usuario>  user= new ArrayList<>();
//        Usuario u = null;
//        try {
//            sql = "select * from usuario where nome like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + name + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                u = new Usuario();
//                u.setId(result.getInt("id"));
//                u.setNome(result.getString("nome"));
//                u.setUsuario(result.getString("usuario"));
//                u.setSenha(result.getInt("senha"));
//                u.setCodigo(result.getInt("codigo"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setEstadoActive(result.getBoolean("active_or_not"));
//                user.add(u);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Usuario: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return user;
//    }
    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from usuario where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Usuario: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update usuario set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Usuario: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
