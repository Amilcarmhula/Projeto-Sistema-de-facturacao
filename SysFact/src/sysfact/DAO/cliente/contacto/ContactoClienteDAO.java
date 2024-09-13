/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.cliente.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.util.Conexao;

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
    public void addEntity(ContactoCliente t) {
        try {
            sql = "insert into contacto_cliente (contacto, email,website,pessoa_de_contacto, "
                    + "fk_id_cliente, fk_id_usuario) value (?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getWebsite());
            ps.setString(4, t.getResponsavel());
            ps.setInt(5, t.getCliente().getId());
            ps.setInt(6, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContactoCliente t, int id) {
        try {
            sql = "update contacto_cliente set contacto=?, email=? ,website=? ,"
                    + "pessoa_de_contacto=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getWebsite());
            ps.setString(4, t.getResponsavel());
            ps.setInt(5, t.getUsuario().getId());
            ps.setInt(6, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
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
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setWebsite(result.getString("website"));
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
    public ObservableList<ContactoCliente> searchEntityByCLiente(ContactoCliente agentID) {
        ObservableList<ContactoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_cliente where fk_id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, agentID.getCliente().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoCliente c = new ContactoCliente();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setWebsite(result.getString("website"));
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
    public void deleteEntity(ContactoCliente x) {
        try {
            sql = "delete from contacto_cliente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
