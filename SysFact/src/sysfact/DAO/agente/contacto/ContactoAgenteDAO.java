/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.agente.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.contacto.agente.ContactoAgente;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoAgenteDAO implements ContactoAgenteDAOImpl<ContactoAgente> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContactoAgenteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ContactoAgente t) {
        try {
            sql = "insert into contacto_agente (contacto, email, "
                    + "fk_id_agente, fk_id_usuario) value (?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setInt(3, t.getFk_id_agente());
            ps.setInt(4, t.getFk_id_usuario());


            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContactoAgente t) {
        try {
            sql = "update contacto_agente set contacto=?, email=?, "
                    + "fk_id_agente=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

             ps.setString(1, t.getContacto());
            ps.setString(2, t.getEmail());
            ps.setInt(3, t.getFk_id_agente());
            ps.setInt(4, t.getFk_id_usuario());
            ps.setInt(5, t.getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<ContactoAgente> listAllEntities() {
        ObservableList<ContactoAgente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_agente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoAgente c = new ContactoAgente();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setFk_id_agente(result.getInt("fk_id_agente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

//    @Override
//    public ContactoAgente searchEntityByName(String contact) {
//        ContactoAgente c = null;
//        try {
//            sql = "select * from contacto_agente where contacto like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + contact + "%");
//            ResultSet result = ps.executeQuery();
////            if (result.next()) {
//            while (result.next()) {
//                c = new ContactoAgente();
//                c.setId(result.getInt("id"));
//                c.setContacto(result.getString("contacto"));
//                c.setEmail(result.getString("email"));
//                c.setFk_id_agente(result.getInt("fk_id_agente"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Contacto: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return c;
//    }
//    
    @Override
    public ObservableList<ContactoAgente> searchEntityByAgente(ContactoAgente agentID) {
        ObservableList<ContactoAgente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from contacto_agente where fk_id_agente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,agentID.getFk_id_agente());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ContactoAgente c = new ContactoAgente();
                c.setId(result.getInt("id"));
                c.setContacto(result.getString("contacto"));
                c.setEmail(result.getString("email"));
                c.setFk_id_agente(result.getInt("fk_id_agente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
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
   

//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from contacto_agente where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Contacto: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    @Override
    public void deleteEntityByUSER(ContactoAgente x) {
        try {
            sql = "update contacto_agente set active_or_not=false where id=?";
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
