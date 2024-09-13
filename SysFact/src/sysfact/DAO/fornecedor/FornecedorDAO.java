/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.fornecedor.Fornecedor;
import sysfact.model.fornecedor.contacto.ContactoFornecedor;
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class FornecedorDAO implements FornecedorDAOImpl<Fornecedor> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public FornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Fornecedor c) {
        try {
            sql = "insert into fornecedor (tipo,razaosocial,nuit,fk_id_usuario) value (?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getRazaosocial());
            ps.setInt(3, c.getNuit());
            ps.setInt(4, c.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar fornecedor: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Fornecedor c, int id) {
        try {
            sql = "update fornecedor set tipo=?, razaosocial=? ,nuit=?, fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getRazaosocial());
            ps.setInt(3, c.getNuit());
            ps.setInt(4, c.getUsuario().getId());
            ps.setInt(5, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar fornecedor: "
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<Fornecedor> listAllEntities() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT f.id,f.tipo,f.razaosocial,f.nuit, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM fornecedor f "
                    + "LEFT JOIN contacto_fornecedor ct ON ct.fk_id_fornecedor = f.id "
                    + "GROUP BY f.id, f.tipo, f.razaosocial, f.nuit;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Fornecedor c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazaosocial(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
                List<ContactoFornecedor> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoFornecedor cc = new ContactoFornecedor();
                        cc.setContacto(split);
                        listaContactos.add(cc);
                    }
                }

                c.setContactoFornecedor(listaContactos);
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }
    
    @Override
    public Fornecedor getLastEntity() {
        Fornecedor c = null;
        try {
            sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazaosocial(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public Fornecedor getEntityByID(int id) {
        Fornecedor c = null;
        try {
            sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazaosocial(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public void deleteEntity(int x) {
        try {
            sql = "delete from fornecedor WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar fornecedor: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    
    
    
    
    @Override
    public ObservableList<Fornecedor> listAllEntitiesTypeFornecedor() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from fornecedor";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Fornecedor c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazaosocial(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                c.setUsuario(u);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

   
    
}
