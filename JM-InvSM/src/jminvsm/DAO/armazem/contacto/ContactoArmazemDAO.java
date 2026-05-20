/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.armazem.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoArmazemDAO implements ContactoArmazemDAOImpl<ContactoArmazem> {

//    private final Connection con;
//    private PreparedStatement ps;
//    private String sql;
//
//    public ContactoArmazemDAO() throws SQLException {
//        this.con = Conexao.getConexao();
//    }

    @Override
    public boolean addEntity(ContactoArmazem t) {
        boolean isSuccess = false;
        String sql = "insert into contacto_armazem (contacto, email,pessoa_de_contacto, "
                    + "idArmazem) value (?,?,?,?)";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_arm());
            ps.setString(2, t.getEmail_arm());
            ps.setString(3, t.getResponsavel_arm());
            ps.setInt(4, t.getArmazem().getId());

            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de persistencia!", "Detalhes: "+ex);
        } 
        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(ContactoArmazem t, int id) {
        boolean isSuccess = false;
        String sql = "update contacto_armazem set contacto=?, email=? ,"
                    + "pessoa_de_contacto=? where id=?";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_arm());
            ps.setString(2, t.getEmail_arm());
            ps.setString(3, t.getResponsavel_arm());
            ps.setInt(4, id);

            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de persistencia!", "Detalhes: "+ex);
        } 
        return isSuccess;
    }

    @Override
    public ObservableList<ContactoArmazem> listAllEntities() {
        ObservableList<ContactoArmazem> lista = FXCollections.observableArrayList();
        String sql = "select * from contacto_armazem";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
            while (result.next()) {
                ContactoArmazem c = new ContactoArmazem();
                c.setId(result.getInt("id"));
                c.setContacto_arm(result.getString("contacto"));
                c.setEmail_arm(result.getString("email"));
                c.setResponsavel_arm(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta!", "Detalhes: "+ex);
        }
        return lista;
    }

    @Override
    public ObservableList<ContactoArmazem> searchEntityByCLiente(int idArmazem) {
        ObservableList<ContactoArmazem> lista = FXCollections.observableArrayList();
        String sql = "select * from contacto_armazem where idArmazem=?";
        try (
                Connection con = Conexao.getConexao(); 
                PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idArmazem);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                ContactoArmazem c = new ContactoArmazem();
                c.setId(result.getInt("id"));
                c.setContacto_arm(result.getString("contacto"));
                c.setEmail_arm(result.getString("email"));
                c.setResponsavel_arm(result.getString("pessoa_de_contacto"));
                lista.add(c);
            }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta!", "Detalhes: "+ex);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        String sql = "delete from contacto_armazem where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            showDialog("Erro de exclusao", "Detalhes: " + ex);
        } 
        
        return isSuccess;
    }
}
