/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.armazem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.armazem.Armazem;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ArmazemDAO implements ArmazemDAOImpl<Armazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Armazem t) {
       try {
            sql = "insert into armazem (nome,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero) value (?,?,?,?,?,?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setString(1, t.getNome());
            ps.setString(2, t.getCidade());
            ps.setString(3, t.getBairro());
            ps.setString(4, t.getAvenida());
            ps.setString(5, t.getRua());
            ps.setInt(6, t.getCodigoPostal());
            ps.setInt(7, t.getNumero());

            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(Armazem t) {
        try {
            sql = "update armazem set nome=?, cidade=?, bairro=?,"
                    + "avenida=?, rua=?, codigo_postal=?, numero=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCidade());
            ps.setString(3, t.getBairro());
            ps.setString(4, t.getAvenida());
            ps.setString(5, t.getRua());
            ps.setInt(6, t.getCodigoPostal());
            ps.setInt(7, t.getNumero());
            ps.setInt(8, t.getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<Armazem> listAllEntities() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Armazem c = new Armazem();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                if (c.isActive_or_not()) {
                    lista.add(c);
                }
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    
    
    @Override
    public void deleteEntityByUSER(Armazem x) {
        try {
            sql =  "update armazem set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
