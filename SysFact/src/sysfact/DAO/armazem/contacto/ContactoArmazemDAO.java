/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.armazem.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.armazem.contacto.ContactoArmazem;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoArmazemDAO implements ContactoArmazemDAOImpl<ContactoArmazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContactoArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ContactoArmazem t) {
        try {
            sql = "insert into contacto_armazem (contacto, email,pessoa_de_contacto, "
                    + "fk_id_armazem, fk_id_usuario) value (?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getResponsavel());
            ps.setInt(4, t.getArmazem().getId());
            ps.setInt(5, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContactoArmazem t, int id) {
        try {
            sql = "update contacto_armazem set contacto=?, email=? ,"
                    + "pessoa_de_contacto=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getResponsavel());
            ps.setInt(4, t.getUsuario().getId());
            ps.setInt(5, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<ContactoArmazem> listAllEntities() {
        ObservableList<ContactoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoArmazem c = new ContactoArmazem();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setResponsavel(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ObservableList<ContactoArmazem> searchEntityByCLiente(ContactoArmazem agentID) {
        ObservableList<ContactoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_armazem where fk_id_armazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, agentID.getArmazem().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoArmazem c = new ContactoArmazem();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setResponsavel(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar armazem: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public void deleteEntity(ContactoArmazem x) {
        try {
            sql = "delete from contacto_armazem where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
