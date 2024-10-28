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
import javax.swing.JOptionPane;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresaDAO implements ContactoEmpresaDAOImpl<ContactoEmpresa> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContactoEmpresaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ContactoEmpresa t) {
        try {
            sql = "insert into contacto_empresa (contacto, email,website, "
                    + "fk_id_empresa) value (?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getContacto_emp());
            ps.setString(2, t.getEmail_emp());
            ps.setString(3, t.getWebsite_emp());
            ps.setInt(4, t.getEmpresa().getId());


            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Contacto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContactoEmpresa t, int id) {
        try {
            sql = "update contacto_empresa set contacto=?, email=?, "
                    + "website=?, fk_id_empresa=? where id=?";
            ps = con.prepareStatement(sql);

             ps.setString(1, t.getContacto_emp());
            ps.setString(2, t.getEmail_emp());
            ps.setString(3, t.getWebsite_emp());
            ps.setInt(4, t.getEmpresa().getId());
            ps.setInt(5, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Contacto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<ContactoEmpresa> listAllEntities() {
        List<ContactoEmpresa> lista = new ArrayList<>();
        try {
            sql = "select * from contacto_empresa";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
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
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Fornecedor: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ContactoEmpresa searchEntityByName(String contact) {
        ContactoEmpresa c = null;
        try {
            sql = "select * from contacto_empresa where contacto like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + contact + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new ContactoEmpresa();
                c.setId(result.getInt("id"));
                c.setContacto_emp(result.getString("contacto"));
                c.setEmail_emp(result.getString("email"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
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
            sql = "delete from contacto_empresa where id=?";
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
            sql = "update contacto_empresa set active_or_not=false where id=?";
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
