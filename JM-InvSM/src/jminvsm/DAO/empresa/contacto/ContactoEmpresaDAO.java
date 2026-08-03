/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.empresa.contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresaDAO implements ContactoEmpresaDAOImpl<ContactoEmpresa> {

    @Override
    public void addEntity(ContactoEmpresa t) {
        String sql = "insert into contacto_empresa (contacto, email,website, "
                + "fk_id_empresa) value (?,?,?,?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_emp());
            ps.setString(2, t.getEmail_emp());
            ps.setString(3, t.getWebsite_emp());
            if (t.getEmpresa() != null) {
                ps.setInt(4, t.getEmpresa().getId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Falha ao adicionar Contacto: " + ex);
        }

    }

    @Override
    public void updateEntityByID(ContactoEmpresa t, int id) {
        String sql = "update contacto_empresa set contacto=?, email=?, "
                + "website=?, fk_id_empresa=? where id=? AND deleteStatus=0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getContacto_emp());
            ps.setString(2, t.getEmail_emp());
            ps.setString(3, t.getWebsite_emp());
            if (t.getEmpresa() != null) {
                ps.setInt(4, t.getEmpresa().getId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setInt(5, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Falha ao actulizar Contacto: "
                    + ex);
        }
    }

    @Override
    public List<ContactoEmpresa> listAllEntities() {
        List<ContactoEmpresa> lista = new ArrayList<>();
        String sql = "select * from contacto_empresa WHERE deleteStatus=0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    ContactoEmpresa c = new ContactoEmpresa();
                    c.setId(result.getInt("id"));
                    c.setContacto_emp(result.getString("contacto"));
                    c.setEmail_emp(result.getString("email"));
                    c.setWebsite_emp(result.getString("website"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                    c.setData_criacao(result.getString("data_criacao"));
                    lista.add(c);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Erro ao Buscar Fornecedor: " + ex);
        }
        return lista;
    }

    @Override
    public ContactoEmpresa searchEntityByName(String contact) {
        ContactoEmpresa c = null;
        String sql = "select * from contacto_empresa where contacto like ? AND deleteStatus=0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + contact + "%");
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new ContactoEmpresa();
                    c.setId(result.getInt("id"));
                    c.setContacto_emp(result.getString("contacto"));
                    c.setEmail_emp(result.getString("email"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                    c.setData_criacao(result.getString("data_criacao"));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Erro ao pesquisar Contacto: " + ex);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            jminvsm.util.SoftDeleteUtils.softDelete("contacto_empresa", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Erro ao deletar Contacto: "
                    + ex);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            jminvsm.util.SoftDeleteUtils.softDelete("contacto_empresa", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Informação", "Erro ao deletar Contacto: "
                    + ex);
        }
    }
}
