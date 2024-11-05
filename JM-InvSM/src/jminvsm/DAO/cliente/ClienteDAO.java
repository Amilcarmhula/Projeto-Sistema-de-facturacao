/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

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
    public boolean addEntity(Cliente c) {
        boolean isSuccess = false;
        try {
            sql = "insert into cliente (tipo,nome,razaosocial,nuit,fk_id_usuario) value (?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_cli());
            ps.setString(3, c.getRazao_cli());
            ps.setInt(4, c.getNuit_cli());
            ps.setInt(5, c.getUsuario().getId());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(Cliente c, int id) {
        boolean isSuccess = false;
        try {
            sql = "update cliente set tipo=?, nome=? ,razaosocial=? ,nuit=?, fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_cli());
            ps.setString(3, c.getRazao_cli());
            ps.setInt(4, c.getNuit_cli());
            ps.setInt(5, c.getUsuario().getId());
            ps.setInt(6, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Cliente> listAllEntitiesToClienteView() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT c.id,c.tipo,c.nome,c.razaosocial ,c.nuit, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM cliente c "
                    + "LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id "
                    + "GROUP BY c.id, c.nome, c.razaosocial, c.nuit;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Cliente c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_cli(result.getString("nome"));
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNuit_cli(result.getInt("nuit"));
                List<ContactoCliente> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoCliente cc = new ContactoCliente();
                        cc.setContacto_cli(split);
                        listaContactos.add(cc);
                    }
                }

                c.setContactosCliente(listaContactos);
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
    public ObservableList<Cliente> listClientesForSearch() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT c.id,c.tipo,c.nome,c.razaosocial,c.nuit,"
                    + "GROUP_CONCAT(DISTINCT ct.contacto SEPARATOR ' ') AS contatos, "
                    + "ct.email as email, "
                    + "GROUP_CONCAT(DISTINCT CONCAT(e.id, '- ', e.avenida, '- ',e.rua, '- ',e.numero, '- ',e.bairro, '- ',e.cidade, '- ',e.provincia, '- ',e.codigo_postal )) AS endereco "
                    + "FROM cliente c "
                    + "LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id "
                    + "LEFT JOIN endereco_cliente e ON e.fk_id_cliente = c.id AND e.tipo = 'Facturação' "
                    + "GROUP BY c.id, c.nome, c.razaosocial, c.nuit";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Cliente c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_cli(result.getString("nome"));
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNuit_cli(result.getInt("nuit"));
                ContactoCliente cont = new ContactoCliente();
                cont.setEmail_cli(result.getString("email"));
                c.setContactoCliente(cont);

                List<ContactoCliente> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoCliente cc = new ContactoCliente();
                        cc.setContacto_cli(split);
                        listaContactos.add(cc);
                    }
                }
                c.setContactosCliente(listaContactos);

//                List<EnderecoCliente> listaEnderecos = new ArrayList<>();
                String enderecos = result.getString("endereco");
                EnderecoCliente end = null;
                if (enderecos != null && !enderecos.trim().isEmpty()) {
                    String[] split = enderecos.split("- ");
                    end = new EnderecoCliente();
                    end.setId(Integer.parseInt(split[0]));
                    end.setAvenida_cli(split[1]);
                    end.setRua_cli(split[2]);
                    end.setNumero_cli(Integer.parseInt(split[3]));
                    end.setBairro_cli(split[4]);
                    end.setCidade_cli(split[5]);
                    end.setProvincia_cli(split[6]);
                    end.setCodigoPostal_cli(Integer.parseInt(split[7]));
                }
                c.setEnderecoCliente(end);
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
    public Cliente getFullClieneByID(int id) {
        Cliente c = null;
        try {
            sql = "SELECT c.id,c.tipo,c.nome,c.razaosocial,c.nuit,"
                    + "GROUP_CONCAT(DISTINCT ct.contacto SEPARATOR ' ') AS contatos, "
                    + "ct.email as email, "
                    + "GROUP_CONCAT(DISTINCT CONCAT(e.id, '- ', e.avenida, '- ',e.rua, '- ',e.numero, '- ',e.bairro, '- ',e.cidade, '- ',e.provincia, '- ',e.codigo_postal )) AS endereco "
                    + "FROM cliente c "
                    + "LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id "
                    + "LEFT JOIN endereco_cliente e ON e.fk_id_cliente = c.id AND e.tipo = 'Facturação' "
                    + "where c.id=? "
                    + "GROUP BY c.id, c.nome, c.razaosocial, c.nuit";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_cli(result.getString("nome"));
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNuit_cli(result.getInt("nuit"));
                ContactoCliente cont = new ContactoCliente();
                cont.setEmail_cli(result.getString("email"));
                c.setContactoCliente(cont);

                List<ContactoCliente> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoCliente cc = new ContactoCliente();
                        cc.setContacto_cli(split);
                        listaContactos.add(cc);
                    }
                }
                c.setContactosCliente(listaContactos);

//                List<EnderecoCliente> listaEnderecos = new ArrayList<>();
                String enderecos = result.getString("endereco");
                EnderecoCliente end = null;
                if (enderecos != null && !enderecos.trim().isEmpty()) {
                    String[] split = enderecos.split("- ");
                    end = new EnderecoCliente();
                    end.setId(Integer.parseInt(split[0]));
                    end.setAvenida_cli(split[1]);
                    end.setRua_cli(split[2]);
                    end.setNumero_cli(Integer.parseInt(split[3]));
                    end.setBairro_cli(split[4]);
                    end.setCidade_cli(split[5]);
                    end.setProvincia_cli(split[6]);
                    end.setCodigoPostal_cli(Integer.parseInt(split[7]));
                }
                c.setEnderecoCliente(end);

            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return c;
    }

    @Override
    public Cliente getLastEntity() {
        Cliente c = null;
        try {
            sql = "SELECT id,tipo,nome,razaosocial,nuit FROM cliente ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNome_cli(result.getString("nome"));
                c.setNuit_cli(result.getInt("nuit"));
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
            sql = "SELECT id,tipo,nome,razaosocial,nuit FROM cliente where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Cliente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNome_cli(result.getString("nome"));
                c.setNuit_cli(result.getInt("nuit"));
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
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        try {
            sql = "delete from cliente WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public int CountClientes() {
        int total = 0;
        try {
            sql = "select count(*) as total from cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            if (result.next()) { // Verifica se há resultados
                total = result.getInt("total");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return total;
    }

}
