/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.conexao.Conexao;
import sysfact.logical.EnderecoCliente;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoClienteDAO implements DaoImpl<EnderecoCliente> {

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
            ps.setInt(9, t.getFk_id_cliente());
            ps.setInt(10, t.getFk_id_usuario());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(EnderecoCliente t, int id) {
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
            ps.setInt(9, t.getFk_id_cliente());
            ps.setInt(10, t.getFk_id_usuario());
            ps.setInt(11, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<EnderecoCliente> listAllEntities() {
        List<EnderecoCliente> lista = new ArrayList<>();
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
                c.setFk_id_cliente(result.getInt("fk_id_cliente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo(result.getString("tipo"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public EnderecoCliente searchEntityByName(String name) {
        EnderecoCliente c = null;
        try {
            sql = "select * from endereco_cliente where bairro like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new EnderecoCliente();
                c.setId(result.getInt("id"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setFk_id_cliente(result.getInt("fk_id_cliente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo(result.getString("tipo"));
                c.setActive_or_not(result.getBoolean("active_or_not"));

            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Endereco: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from endereco_cliente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update endereco_cliente set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
