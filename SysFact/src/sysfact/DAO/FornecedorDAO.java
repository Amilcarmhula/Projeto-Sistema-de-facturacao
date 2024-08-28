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
import sysfact.logical.Fornecedor;

/**
 *
 * @author JM-Tecnologias
 */
public class FornecedorDAO implements DaoImpl<Fornecedor> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public FornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(Fornecedor t) {
       try {
            sql = "insert into fornecedor (nome, nuit) value (?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setString(1, t.getNome());
            ps.setInt(2, t.getNuit());

            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar fornecedor: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(Fornecedor t, int id) {
        try {
            sql = "update fornecedor set nome=?, nuit=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setInt(2, t.getNuit());
            ps.setInt(3, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar fornecedor: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Fornecedor> listAllEntities() {
        List<Fornecedor> lista = new ArrayList<>();
        try {
            sql = "select * from fornecedor";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Fornecedor c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setEstadoActive(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Fornecedor: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Fornecedor searchEntityByName(String name) {
        Fornecedor c = null;
        try {
            sql = "select * from fornecedor where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setEstadoActive(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Fornecedor: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from fornecedor where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar fornecedor: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update fornecedor set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar fornecedor: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
