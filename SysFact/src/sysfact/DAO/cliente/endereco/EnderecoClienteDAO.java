/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.cliente.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.cliente.Cliente;
import sysfact.model.cliente.endereco.EnderecoCliente;
import sysfact.model.usuario.Usuario;
import sysfact.util.Conexao;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoClienteDAO implements EnderecoClienteDAOImpl<EnderecoCliente> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(EnderecoCliente t) {
        try {
            sql = "insert into endereco_cliente (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, tipo, fk_id_cliente, fk_id_usuario)"
                    + " value (?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia());
            ps.setString(2, t.getCidade());
            ps.setString(3, t.getBairro());
            ps.setString(4, t.getAvenida());
            ps.setString(5, t.getRua());
            ps.setInt(6, t.getCodigoPostal());
            ps.setInt(7, t.getNumero());
            ps.setString(8, t.getTipo());
            ps.setInt(9, t.getCliente().getId());
            ps.setInt(10, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(EnderecoCliente t) {
        try {
            sql = "update endereco_cliente set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=?, tipo=?, fk_id_cliente=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia());
            ps.setString(2, t.getCidade());
            ps.setString(3, t.getBairro());
            ps.setString(4, t.getAvenida());
            ps.setString(5, t.getRua());
            ps.setInt(6, t.getCodigoPostal());
            ps.setInt(7, t.getNumero());
            ps.setString(8, t.getTipo());
            ps.setInt(9, t.getCliente().getId());
            ps.setInt(10, t.getUsuario().getId());
            ps.setInt(11, t.getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<EnderecoCliente> listAllEntities() {
        ObservableList<EnderecoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoCliente c = new EnderecoCliente();
                c.setId(result.getInt("id"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                Cliente a = new Cliente();
                a.setId(result.getInt("fk_id_cliente"));
                c.setCliente(a);
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                c.setUsuario(u);
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo(result.getString("tipo"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

//    @Override
//    public EnderecoCliente searchEntityByName(String name) {
//        EnderecoCliente c = null;
//        try {
//            sql = "select * from endereco_agente where bairro like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + name + "%");
//            ResultSet result = ps.executeQuery();
////            if (result.next()) {
//            while (result.next()) {
//                c = new EnderecoCliente();
//                c.setId(result.getInt("id"));
//                c.setProvincia(result.getString("provincia"));
//                c.setCidade(result.getString("cidade"));
//                c.setBairro(result.getString("bairro"));
//                c.setAvenida(result.getString("avenida"));
//                c.setRua(result.getString("rua"));
//                c.setCodigoPostal(result.getInt("codigo_postal"));
//                c.setNumero(result.getInt("numero"));
//                c.setFk_id_agente(result.getInt("fk_id_agente"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setTipo(result.getString("tipo"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Endereco: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return c;
//
//    }

    public ObservableList<EnderecoCliente> searchEntityByAgente(EnderecoCliente agentID) {
        ObservableList<EnderecoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_cliente where fk_id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,agentID.getCliente().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoCliente c = new EnderecoCliente();
                c.setId(result.getInt("id"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                Cliente a = new Cliente();
                a.setId(result.getInt("fk_id_cliente"));
                c.setCliente(a);
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                c.setUsuario(u);
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo(result.getString("tipo"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from endereco_agente where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    @Override
    public void deleteEntityByUSER(EnderecoCliente id) {
        try {
            sql = "update endereco_cliente set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
