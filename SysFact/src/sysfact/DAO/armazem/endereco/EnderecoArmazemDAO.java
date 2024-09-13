/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.armazem.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.armazem.endereco.EnderecoArmazem;

import sysfact.util.Conexao;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoArmazemDAO implements EnderecoArmazemDAOImpl<EnderecoArmazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(EnderecoArmazem t) {
        try {
            sql = "insert into endereco_armazem (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, tipo, fk_id_fornecedor, fk_id_usuario)"
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
            ps.setInt(9, t.getArmazem().getId());
            ps.setInt(10, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(EnderecoArmazem t, int id) {
        try {
            sql = "update endereco_armazem set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=?, tipo=?,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia());
            ps.setString(2, t.getCidade());
            ps.setString(3, t.getBairro());
            ps.setString(4, t.getAvenida());
            ps.setString(5, t.getRua());
            ps.setInt(6, t.getCodigoPostal());
            ps.setInt(7, t.getNumero());
            ps.setString(8, t.getTipo());
            ps.setInt(9, t.getUsuario().getId());
            ps.setInt(10, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<EnderecoArmazem> listAllEntities() {
        ObservableList<EnderecoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoArmazem c = new EnderecoArmazem();
                c.setId(result.getInt("id"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setTipo(result.getString("tipo"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
    @Override
    public ObservableList<EnderecoArmazem> searchEntityByCLiente(EnderecoArmazem agentID) {
        ObservableList<EnderecoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_armazem where fk_id_armazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,agentID.getArmazem().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoArmazem c = new EnderecoArmazem();
                c.setId(result.getInt("id"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setTipo(result.getString("tipo"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar endereco: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
    
     @Override
    public void deleteEntity(EnderecoArmazem id) {
        try {
            sql = "delete from endereco_armazem where id=?";
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
