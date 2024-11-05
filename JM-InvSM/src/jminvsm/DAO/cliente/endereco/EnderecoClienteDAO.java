/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.cliente.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;


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
    public boolean addEntity(EnderecoCliente t) {
        boolean isSuccess = false;
        try {
            sql = "insert into endereco_cliente (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, tipo, fk_id_cliente, fk_id_usuario)"
                    + " value (?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_cli());
            ps.setString(2, t.getCidade_cli());
            ps.setString(3, t.getBairro_cli());
            ps.setString(4, t.getAvenida_cli());
            ps.setString(5, t.getRua_cli());
            ps.setInt(6, t.getCodigoPostal_cli());
            ps.setInt(7, t.getNumero_cli());
            ps.setString(8, t.getTipo_cli());
            ps.setInt(9, t.getCliente().getId());
            ps.setInt(10, t.getUsuario().getId());

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
    public boolean updateEntityByID(EnderecoCliente t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update endereco_cliente set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=?, tipo=?,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_cli());
            ps.setString(2, t.getCidade_cli());
            ps.setString(3, t.getBairro_cli());
            ps.setString(4, t.getAvenida_cli());
            ps.setString(5, t.getRua_cli());
            ps.setInt(6, t.getCodigoPostal_cli());
            ps.setInt(7, t.getNumero_cli());
            ps.setString(8, t.getTipo_cli());
            ps.setInt(9, t.getUsuario().getId());
            ps.setInt(10, id);

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
    public ObservableList<EnderecoCliente> listAllEntities() {
        ObservableList<EnderecoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_cliente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoCliente c = new EnderecoCliente();
                c.setId(result.getInt("id"));
                c.setProvincia_cli(result.getString("provincia"));
                c.setCidade_cli(result.getString("cidade"));
                c.setBairro_cli(result.getString("bairro"));
                c.setAvenida_cli(result.getString("avenida"));
                c.setRua_cli(result.getString("rua"));
                c.setCodigoPostal_cli(result.getInt("codigo_postal"));
                c.setNumero_cli(result.getInt("numero"));
                c.setTipo_cli(result.getString("tipo"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog( "Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }
    @Override
    public ObservableList<EnderecoCliente> searchEntityByCLiente(int idCliente) {
        ObservableList<EnderecoCliente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_cliente where fk_id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,idCliente);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoCliente c = new EnderecoCliente();
                c.setId(result.getInt("id"));
                c.setProvincia_cli(result.getString("provincia"));
                c.setCidade_cli(result.getString("cidade"));
                c.setBairro_cli(result.getString("bairro"));
                c.setAvenida_cli(result.getString("avenida"));
                c.setRua_cli(result.getString("rua"));
                c.setCodigoPostal_cli(result.getInt("codigo_postal"));
                c.setNumero_cli(result.getInt("numero"));
                c.setTipo_cli(result.getString("tipo"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog( "Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }
    
     @Override
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        try {
            sql = "delete from endereco_cliente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de exclusao", "Detalhes: " + ex);
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
}
