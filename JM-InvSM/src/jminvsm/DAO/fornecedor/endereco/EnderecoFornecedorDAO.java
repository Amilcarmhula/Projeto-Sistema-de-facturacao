/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.fornecedor.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoFornecedorDAO implements EnderecoFornecedorDAOImpl<EnderecoFornecedor> {

    private final Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoFornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(EnderecoFornecedor t) {
        boolean isSuccess = false;
        try {
            sql = "insert into endereco_fornecedor (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, fk_id_fornecedor, fk_id_usuario)"
                    + " value (?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_forn());
            ps.setString(2, t.getCidade_forn());
            ps.setString(3, t.getBairro_forn());
            ps.setString(4, t.getAvenida_forn());
            ps.setString(5, t.getRua_forn());
            ps.setInt(6, t.getCodigoPostal_forn());
            ps.setInt(7, t.getNumero_forn());
            ps.setInt(8, t.getFornecedor().getId());
            ps.setInt(9, t.getUsuario().getId());

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
    public boolean updateEntityByID(EnderecoFornecedor t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update endereco_fornecedor set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=?,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_forn());
            ps.setString(2, t.getCidade_forn());
            ps.setString(3, t.getBairro_forn());
            ps.setString(4, t.getAvenida_forn());
            ps.setString(5, t.getRua_forn());
            ps.setInt(6, t.getCodigoPostal_forn());
            ps.setInt(7, t.getNumero_forn());
            ps.setInt(8, t.getUsuario().getId());
            ps.setInt(9, id);

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
    public ObservableList<EnderecoFornecedor> listAllEntities() {
        ObservableList<EnderecoFornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_fornecedor";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoFornecedor c = new EnderecoFornecedor();
                c.setId(result.getInt("id"));
                c.setProvincia_forn(result.getString("provincia"));
                c.setCidade_forn(result.getString("cidade"));
                c.setBairro_forn(result.getString("bairro"));
                c.setAvenida_forn(result.getString("avenida"));
                c.setRua_forn(result.getString("rua"));
                c.setCodigoPostal_forn(result.getInt("codigo_postal"));
                c.setNumero_forn(result.getInt("numero"));
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
    public ObservableList<EnderecoFornecedor> searchEntityByCLiente(int id) {
        ObservableList<EnderecoFornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_fornecedor where fk_id_fornecedor=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoFornecedor c = new EnderecoFornecedor();
                c.setId(result.getInt("id"));
                c.setProvincia_forn(result.getString("provincia"));
                c.setCidade_forn(result.getString("cidade"));
                c.setBairro_forn(result.getString("bairro"));
                c.setAvenida_forn(result.getString("avenida"));
                c.setRua_forn(result.getString("rua"));
                c.setCodigoPostal_forn(result.getInt("codigo_postal"));
                c.setNumero_forn(result.getInt("numero"));
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
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        try {
            sql = "delete from endereco_fornecedor where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
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
}
