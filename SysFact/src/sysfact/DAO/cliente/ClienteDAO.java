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
import sysfact.model.cliente.endereco.EnderecoCliente;
import sysfact.model.usuario.Usuario;
import sysfact.reports.facturas.ClienteData;
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
            sql = "insert into cliente (tipo,nome,razaosocial,nuit,fk_id_usuario) value (?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_cli());
            ps.setString(3, c.getRazao_cli());
            ps.setInt(4, c.getNuit_cli());
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
            sql = "update cliente set tipo=?, nome=? ,razaosocial=? ,nuit=?, fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_cli());
            ps.setString(3, c.getRazao_cli());
            ps.setInt(4, c.getNuit_cli());
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
    public ObservableList<Cliente> listAllForSearch() {
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
    public Cliente getClieneByID(int id) {
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

    public ClienteData getClinteFacturaData(String factura) {
        ClienteData c = null;
        try {
            sql = "SELECT f.numero, f.data_criacao, f.data_vencimento,"
                    + "dc.nome as tipo,"
                    + "c.razaosocial, c.nuit,"
                    + "CONCAT(e.avenida,', ',e.rua,', ',e.numero,'; ',e.bairro,', ',e.cidade,', ',e.provincia,'- ',e.codigo_postal ) as endereco,"
                    + "GROUP_CONCAT(DISTINCT ct.contacto SEPARATOR '; ') AS contatos,ct.email "
                    + "FROM factura f "
                    + "LEFT JOIN documentocomercial dc ON f.fk_id_doc = dc.id "
                    + "LEFT JOIN cliente c ON c.id = f.fk_id_cliente "
                    + "LEFT JOIN contacto_cliente ct ON ct.fk_id_cliente = c.id "
                    + "LEFT JOIN endereco_cliente e ON e.fk_id_cliente = c.id "
                    + "WHERE f.numero = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, factura);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new ClienteData();
                c.setApelido(result.getString("razaosocial"));
                c.setNuit(result.getInt("nuit"));
                c.setContacto(result.getString("contatos"));
                c.setEndereco(result.getString("endereco"));
                c.setEmail(result.getString("email"));
                c.setNumero(result.getString("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setData_vencimento(result.getString("data_vencimento"));
                c.setTipo(result.getString("tipo"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

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

}
