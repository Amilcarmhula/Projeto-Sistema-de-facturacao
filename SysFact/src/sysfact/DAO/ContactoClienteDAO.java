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
import sysfact.logical.ContactoCliente;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoClienteDAO implements DaoImpl<ContactoCliente> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContactoClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ContactoCliente t) {
        try {
            sql = "insert into contacto_cliente (contacto, email, "
                    + "fk_id_cliente, fk_id_usuario) value (?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setInt(3, t.getFk_id_cliente());
            ps.setInt(4, t.getFk_id_usuario());


            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContactoCliente t, int id) {
        try {
            sql = "update contacto_cliente set contacto=?, email=?, "
                    + "fk_id_cliente=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

             ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setInt(3, t.getFk_id_cliente());
            ps.setInt(4, t.getFk_id_usuario());
            ps.setInt(5, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<ContactoCliente> listAllEntities() {
        List<ContactoCliente> lista = new ArrayList<>();
        try {
            sql = "select * from contacto_cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoCliente c = new ContactoCliente();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setFk_id_cliente(result.getInt("fk_id_cliente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Fornecedor: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ContactoCliente searchEntityByName(String contact) {
        ContactoCliente c = null;
        try {
            sql = "select * from contacto_cliente where contacto like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + contact + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new ContactoCliente();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setFk_id_cliente(result.getInt("fk_id_cliente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Contacto: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from contacto_cliente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update contacto_cliente set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
