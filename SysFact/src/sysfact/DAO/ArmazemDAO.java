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
import sysfact.logical.Armazem;

/**
 *
 * @author JM-Tecnologias
 */
public class ArmazemDAO  implements DaoImpl<Armazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Armazem t) {
       try {
            sql = "insert into armazem (nome,provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero) value (?,?,?,?,?,?,?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setString(1, t.getNome());
            ps.setString(2, t.getProvincia());
            ps.setString(3, t.getCidade());
            ps.setString(4, t.getBairro());
            ps.setString(5, t.getAvenida());
            ps.setString(6, t.getRua());
            ps.setInt(7, t.getCodigoPostal());
            ps.setInt(8, t.getNumero());

            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(Armazem t, int id) {
        try {
            sql = "update armazem set nome=?, provincia=?, cidade=?, bairro=?,"
                    + "avenida=?, rua=?, codigo_postal=?, numero=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getProvincia());
            ps.setString(3, t.getCidade());
            ps.setString(4, t.getBairro());
            ps.setString(5, t.getAvenida());
            ps.setString(6, t.getRua());
            ps.setInt(7, t.getCodigoPostal());
            ps.setInt(8, t.getNumero());
            ps.setInt(9, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Armazem> listAllEntities() {
        List<Armazem> lista = new ArrayList<>();
        try {
            sql = "select * from armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Armazem c = new Armazem();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Armazem: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Armazem searchEntityByName(String name) {
        Armazem c = null;
        try {
            sql = "select * from armazem where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new Armazem();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setProvincia(result.getString("provincia"));
                c.setCidade(result.getString("cidade"));
                c.setBairro(result.getString("bairro"));
                c.setAvenida(result.getString("avenida"));
                c.setRua(result.getString("rua"));
                c.setCodigoPostal(result.getInt("codigo_postal"));
                c.setNumero(result.getInt("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Armazem: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from armazem where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update armazem set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
