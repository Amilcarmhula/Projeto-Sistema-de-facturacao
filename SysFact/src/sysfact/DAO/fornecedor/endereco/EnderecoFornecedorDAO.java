/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.fornecedor.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.fornecedor.endereco.EnderecoFornecedor;

import sysfact.util.Conexao;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoFornecedorDAO implements EnderecoFornecedorDAOImpl<EnderecoFornecedor> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoFornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(EnderecoFornecedor t) {
        try {
            sql = "insert into endereco_fornecedor (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, tipo, fk_id_fornecedor, fk_id_usuario)"
                    + " value (?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_forn());
            ps.setString(2, t.getCidade_forn());
            ps.setString(3, t.getBairro_forn());
            ps.setString(4, t.getAvenida_forn());
            ps.setString(5, t.getRua_forn());
            ps.setInt(6, t.getCodigoPostal_forn());
            ps.setInt(7, t.getNumero_forn());
            ps.setString(8, t.getTipo_forn());
            ps.setInt(9, t.getFornecedor().getId());
            ps.setInt(10, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(EnderecoFornecedor t, int id) {
        try {
            sql = "update endereco_fornecedor set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=?, tipo=?,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_forn());
            ps.setString(2, t.getCidade_forn());
            ps.setString(3, t.getBairro_forn());
            ps.setString(4, t.getAvenida_forn());
            ps.setString(5, t.getRua_forn());
            ps.setInt(6, t.getCodigoPostal_forn());
            ps.setInt(7, t.getNumero_forn());
            ps.setString(8, t.getTipo_forn());
            ps.setInt(9, t.getUsuario().getId());
            ps.setInt(10, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Endereco: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
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
                c.setTipo_forn(result.getString("tipo"));
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
    public ObservableList<EnderecoFornecedor> searchEntityByCLiente(EnderecoFornecedor agentID) {
        ObservableList<EnderecoFornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_fornecedor where fk_id_fornecedor=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,agentID.getFornecedor().getId());
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
                c.setTipo_forn(result.getString("tipo"));
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
    public void deleteEntity(EnderecoFornecedor id) {
        try {
            sql = "delete from endereco_fornecedor where id=?";
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
