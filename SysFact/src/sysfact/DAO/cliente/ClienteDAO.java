/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.cliente.Cliente;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ClienteDAO implements ClienteDAOImpl<Cliente> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Cliente c) {
        try {
            sql = "insert into cliente (tipo,nome,apelido,nuit,fk_id_usuario) value (?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getApelido());
            ps.setInt(4, c.getNuit());
            ps.setInt(5, c.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Cliente c, int id) {
        try {
            sql = "update cliente set tipo=?, nome=? ,apelido=? ,nuit=?, fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getApelido());
            ps.setInt(4, c.getNuit());
            ps.setInt(5, c.getUsuario().getId());
            ps.setInt(6, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar cliente: "
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<Cliente> listAllEntities() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT c.id,c.tipo,c.nome,c.apelido as razaosocial,c.nuit, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM cliente c "
                    + "LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id "
                    + "GROUP BY c.id, c.nome, c.apelido, c.nuit;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Cliente c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome(result.getString("nome"));
                c.setApelido(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
                List<ContactoCliente> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoCliente cc = new ContactoCliente();
                        cc.setContacto(split);
                        listaContactos.add(cc);
                    }
                }

                c.setContactoCliente(listaContactos);
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }
    
    @Override
    public Cliente getLastEntity() {
        Cliente c = null;
        try {
            sql = "SELECT id,tipo,nome,apelido,nuit FROM cliente ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public Cliente getEntityByID(int id) {
        Cliente c = null;
        try {
            sql = "SELECT id,tipo,nome,apelido,nuit FROM cliente where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public void deleteEntity(int x) {
        try {
            sql = "delete from cliente WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar cliente: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    
    
    
    
    @Override
    public ObservableList<Cliente> listAllEntitiesTypeFornecedor() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Cliente c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                c.setUsuario(u);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }
    
}
