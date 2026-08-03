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
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
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
                    + "pessoa_de_contacto=? where id=? AND deleteStatus=0";

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

        String sql = "SELECT * FROM contacto_fornecedor WHERE deleteStatus=0";

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
                = "SELECT * FROM contacto_fornecedor WHERE idFornecedor=? AND deleteStatus=0";

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

        try {
            return jminvsm.util.SoftDeleteUtils.softDelete(con, "contacto_fornecedor", id);

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
