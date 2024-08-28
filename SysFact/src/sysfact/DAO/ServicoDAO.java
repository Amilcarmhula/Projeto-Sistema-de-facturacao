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
import sysfact.logical.Servico;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicoDAO implements DaoImpl<Servico> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ServicoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(Servico t) {
       try {
            sql = "insert into servico (nome, descricao,fk_id_categoria, "
                    + "fk_id_taxa_imposto, fk_id_usuario) value (?,?,?,?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setInt(3, t.getFk_id_categoria());
            ps.setInt(4, t.getFk_id_taxa_imposto());
            ps.setInt(5, t.getFk_id_usuario());

            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar servico: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(Servico t, int id) {
        try {
            sql = "update servico set nome=?, descricao=?,fk_id_categoria=?, "
                    + "fk_id_taxa_imposto=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setInt(3, t.getFk_id_categoria());
            ps.setInt(4, t.getFk_id_taxa_imposto());
            ps.setInt(5, t.getFk_id_usuario());
            ps.setInt(6, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar servico: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Servico> listAllEntities() {
        List<Servico> lista = new ArrayList<>();
        try {
            sql = "select * from servico";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Servico c = new Servico();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setDescricao(result.getString("descricao"));
                c.setFk_id_categoria(result.getInt("fk_id_categoria"));
                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar servico: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Servico searchEntityByName(String name) {
        Servico c = null;
        try {
            sql = "select * from servico where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new Servico();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setDescricao(result.getString("descricao"));
                c.setFk_id_categoria(result.getInt("fk_id_categoria"));
                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar servico: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from servico where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar servico: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update servico set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar servico: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
