/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.fornecedor.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
//public class ContactoFornecedorDAO implements ContactoFornecedorDAOImpl<ContactoFornecedor> {
//
//    private final Connection con;
//    private PreparedStatement ps;
//    private String sql;
//
//    public ContactoFornecedorDAO() throws SQLException {
//        this.con = Conexao.getConexao();
//    }
//
//    @Override
//    public boolean addEntity(ContactoFornecedor t) {
//        boolean isSuccess = false;
//        try {
//            sql = "insert into contacto_fornecedor (contacto, email,website,pessoa_de_contacto, "
//                    + "idFornecedor) value (?,?,?,?,?)";
//
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getContacto_forn());
//            ps.setString(2, t.getEmail_forn());
//            ps.setString(3, t.getWebsite_forn());
//            ps.setString(4, t.getResponsavel_forn());
//            ps.setInt(5, t.getFornecedor().getId());
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
//
//    @Override
//    public boolean updateEntityByID(ContactoFornecedor t, int id) {
//        boolean isSuccess = false;
//        try {
//            sql = "update contacto_fornecedor set contacto=?, email=? ,website=? ,"
//                    + "pessoa_de_contacto=? where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getContacto_forn());
//            ps.setString(2, t.getEmail_forn());
//            ps.setString(3, t.getWebsite_forn());
//            ps.setString(4, t.getResponsavel_forn());
//            ps.setInt(5, id);
//
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
//
//    @Override
//    public ObservableList<ContactoFornecedor> listAllEntities() {
//        ObservableList<ContactoFornecedor> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from contacto_fornecedor";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                ContactoFornecedor c = new ContactoFornecedor();
//                c.setId(result.getInt("id"));
//                c.setContacto_forn(result.getString("contacto"));
//                c.setEmail_forn(result.getString("email"));
//                c.setWebsite_forn(result.getString("website"));
//                c.setResponsavel_forn(result.getString("pessoa_de_contacto"));
//                lista.add(c);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//
//    @Override
//    public ObservableList<ContactoFornecedor> searchEntityByCLiente(int id) {
//        ObservableList<ContactoFornecedor> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from contacto_fornecedor where idFornecedor=?";
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                ContactoFornecedor c = new ContactoFornecedor();
//                c.setId(result.getInt("id"));
//                c.setContacto_forn(result.getString("contacto"));
//                c.setEmail_forn(result.getString("email"));
//                c.setWebsite_forn(result.getString("website"));
//                c.setResponsavel_forn(result.getString("pessoa_de_contacto"));
//                lista.add(c);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Contacto: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//
//    @Override
//    public boolean deleteEntity(int x) {
//        boolean isSuccess = false;
//        try {
//            sql = "delete from contacto_fornecedor where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, x);
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
public class ContactoFornecedorDAO implements ContactoFornecedorDAOImpl<ContactoFornecedor> {

    private final Connection con;

    public ContactoFornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    // ================= INSERT =================
    @Override
    public boolean addEntity(ContactoFornecedor t) {

        String sql = "insert into contacto_fornecedor (contacto, email,website,pessoa_de_contacto, "
                    + "idFornecedor) values (?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_forn());
            ps.setString(2, t.getEmail_forn());
            ps.setString(3, t.getWebsite_forn());
            ps.setString(4, t.getResponsavel_forn());
            ps.setInt(5, t.getFornecedor().getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir contacto fornecedor", e);
        }
    }

    // ================= UPDATE =================
    @Override
    public boolean updateEntityByID(ContactoFornecedor t, int id) {

        String sql = "update contacto_fornecedor set contacto=?, email=? ,website=? ,"
                    + "pessoa_de_contacto=? where id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_forn());
            ps.setString(2, t.getEmail_forn());
            ps.setString(3, t.getWebsite_forn());
            ps.setString(4, t.getResponsavel_forn());
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar contacto", e);
        }
    }

    // ================= LIST ALL =================
    @Override
    public ObservableList<ContactoFornecedor> listAllEntities() {

        ObservableList<ContactoFornecedor> lista
                = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacto_fornecedor";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResult(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contactos", e);
        }

        return lista;
    }

    // ================= SEARCH BY FORNECEDOR =================
    @Override
    public ObservableList<ContactoFornecedor> searchEntityByFornecedor(int idFornecedor) {

        ObservableList<ContactoFornecedor> lista
                = FXCollections.observableArrayList();

        String sql
                = "SELECT * FROM contacto_fornecedor WHERE idFornecedor=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFornecedor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResult(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar contacto", e);
        }

        return lista;
    }

    // ================= DELETE =================
    @Override
    public boolean deleteEntity(int id) {

        String sql
                = "DELETE FROM contacto_fornecedor WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao eliminar contacto", e);
        }
    }

    // ================= MAPPER =================
    private ContactoFornecedor mapResult(ResultSet rs)
            throws SQLException {

        ContactoFornecedor c = new ContactoFornecedor();

        c.setId(rs.getInt("id"));
        c.setContacto_forn(rs.getString("contacto"));
        c.setEmail_forn(rs.getString("email"));
        c.setWebsite_forn(rs.getString("website"));
        c.setResponsavel_forn(rs.getString("pessoa_de_contacto"));

        return c;
    }
}
