/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.usuario;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import sysfact.SysFact;
import sysfact.dao.empresa.EmpresaDAO;
import sysfact.model.usuario.NivelAcesso;
import sysfact.model.usuario.Permissao;
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;
import sysfact.util.LoadAndMoveUtilities;
import static sysfact.util.AlertUtilities.showDialog;
import static sysfact.util.AlertUtilities.showSuccessNotification;

/**
 *
 * @author JM-Tecnologias
 */
public class UsuarioDAO implements UsuarioDAOImpl<Usuario> {

    private LoadAndMoveUtilities metodosUteis;
    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public UsuarioDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    public Usuario login(String u, String s) {
        Usuario user = null;
        try {
            sql = "select u.id, u.nome, u.email,u.estado,"
                    + "p.tipo,n.descricao "
                    + "from usuario u "
                    + "LEFT JOIN nivel_acesso n ON u.fk_nivel_acesso=n.id "
                    + "LEFT JOIN permissao p ON u.fk_permissao=p.id "
                    + "where usuario=? and senha=? and estado=1";
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, u);
            ps.setString(2, s);

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                user = new Usuario();
                user.setId(result.getInt("id"));
                user.setNome(result.getString("nome"));
                user.setEmail(result.getString("email"));
                user.setEstado(result.getBoolean("estado"));
                NivelAcesso n = new NivelAcesso();
                n.setDescricao(result.getString("descricao"));
                user.setNivelAcesso(n);
                Permissao p = new Permissao();
                p.setTipo(result.getString("tipo"));
                user.setPermissao(p);
            } else {
                showDialog(FontAwesomeIcon.TIMES, "Info", "Usuario nao existe!!");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro", "Detalhes: " + ex);
        }
        return user;
    }

//
//    @Override
//    public void addEntity(Usuario t) {
//        try {
//            sql = "insert into usuario (nome, usuario, senha, codigo) value (?,?,?,?)";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, t.getNome());
//            ps.setString(2, t.getUsuario());
//            ps.setString(3, t.getSenha());
//            ps.setInt(4, t.getCodigo());
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao adicionar Usuario: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public void updateEntityByID(Usuario t) {
//        try {
//            sql = "update usuario set nome=?, usuario=?, senha=?, codigo=?"
//                    + " where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getNome());
//            ps.setString(2, t.getUsuario());
//            ps.setString(3, t.getSenha());
//            ps.setInt(4, t.getCodigo());
//            ps.setInt(5, t.getId());
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar Usuario: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public ObservableList<Usuario> listAllEntities() {
//        ObservableList<Usuario> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from usuario";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Usuario u = new Usuario();
//                u.setId(result.getInt("id"));
//                u.setNome(result.getString("nome"));
//                u.setUsuario(result.getString("usuario"));
//                u.setSenha(result.getString("senha"));
//                u.setCodigo(result.getInt("codigo"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setEstadoActive(result.getBoolean("active_or_not"));
//                lista.add(u);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao Buscar Usuarios: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
////
////    @Override
////    public Usuario searchEntityByName(String name) {
////        Usuario u = null;
////        try {
////            sql = "select * from usuario where nome like ?";
////            ps = con.prepareStatement(sql);
////            ps.setString(1, "%" + name + "%");
////            ResultSet result = ps.executeQuery();
//////            if (result.next()) {
////            while (result.next()) {
////                u = new Usuario();
////                u.setId(result.getInt("id"));
////                u.setNome(result.getString("nome"));
////                u.setUsuario(result.getString("usuario"));
////                u.setSenha(result.getInt("senha"));
////                u.setCodigo(result.getInt("codigo"));
////                u.setData_criacao(result.getString("data_criacao"));
////                u.setEstadoActive(result.getBoolean("active_or_not"));
////            }
////            result.close();
////            ps.close();
////        } catch (SQLException ex) {
////            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Usuario: " + ex,
////                    "Informação", JOptionPane.ERROR_MESSAGE);
////        }
////        return u;
////
////    }
//
////    @Override
////    public List<Usuario> searchListEntityByName(String name) {
////        List<Usuario>  user= new ArrayList<>();
////        Usuario u = null;
////        try {
////            sql = "select * from usuario where nome like ?";
////            ps = con.prepareStatement(sql);
////            ps.setString(1, "%" + name + "%");
////            ResultSet result = ps.executeQuery();
////            while (result.next()) {
////                u = new Usuario();
////                u.setId(result.getInt("id"));
////                u.setNome(result.getString("nome"));
////                u.setUsuario(result.getString("usuario"));
////                u.setSenha(result.getInt("senha"));
////                u.setCodigo(result.getInt("codigo"));
////                u.setData_criacao(result.getString("data_criacao"));
////                u.setEstadoActive(result.getBoolean("active_or_not"));
////                user.add(u);
////            }
////            result.close();
////            ps.close();
////        } catch (SQLException ex) {
////            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Usuario: " + ex,
////                    "Informação", JOptionPane.ERROR_MESSAGE);
////        }
////        return user;
////    }
////    @Override
////    public void deleteEntityByROOT(int id) {
////        try {
////            sql = "delete from usuario where id=?";
////            ps = con.prepareStatement(sql);
////
////            ps.setInt(1, id);
////            ps.execute();
////            ps.close();
////        } catch (SQLException ex) {
////            JOptionPane.showMessageDialog(null, "Erro ao deletar Usuario: "
////                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
////        }
////    }
//
//    @Override
//    public void deleteEntityByUSER(Usuario u) {
//        try {
//            sql = "update usuario set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, u.getId());
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Usuario: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
