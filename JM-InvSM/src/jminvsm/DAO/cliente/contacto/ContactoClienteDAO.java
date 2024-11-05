/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.cliente.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.cliente.contacto.ContactoCliente;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoClienteDAO implements ContactoClienteDAOImpl<ContactoCliente> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContactoClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(ContactoCliente t) {
        boolean isSuccess = false;
        try {
            sql = "insert into contacto_cliente (contacto, email,website,pessoa_de_contacto, "
                    + "fk_id_cliente, fk_id_usuario) value (?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto_cli());
            ps.setString(2, t.getEmail_cli());
            ps.setString(3, t.getWebsite_cli());
            ps.setString(4, t.getResponsavel());
            ps.setInt(5, t.getCliente().getId());
            ps.setInt(6, t.getUsuario().getId());

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

    @Override
    public boolean updateEntityByID(ContactoCliente t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update contacto_cliente set contacto=?, email=? ,website=? ,"
                    + "pessoa_de_contacto=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto_cli());
            ps.setString(2, t.getEmail_cli());
            ps.setString(3, t.getWebsite_cli());
            ps.setString(4, t.getResponsavel());
            ps.setInt(5, t.getUsuario().getId());
            ps.setInt(6, id);

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
    public ObservableList<ContactoCliente> listAllEntities() {
        ObservableList<ContactoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoCliente c = new ContactoCliente();
                c.setId(result.getInt("id"));
                c.setContacto_cli(result.getString("contacto"));
                c.setEmail_cli(result.getString("email"));
                c.setWebsite_cli(result.getString("website"));
                c.setResponsavel(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ObservableList<ContactoCliente> searchEntityByCLiente(int idCliente) {
        ObservableList<ContactoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_cliente where fk_id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoCliente c = new ContactoCliente();
                c.setId(result.getInt("id"));
                c.setContacto_cli(result.getString("contacto"));
                c.setEmail_cli(result.getString("email"));
                c.setWebsite_cli(result.getString("website"));
                c.setResponsavel(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Contacto: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        try {
            sql = "delete from contacto_cliente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
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
}
