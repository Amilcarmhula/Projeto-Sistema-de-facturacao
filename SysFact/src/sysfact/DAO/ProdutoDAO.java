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
import sysfact.logical.Produto;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoDAO implements DaoImpl<Produto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(Produto t) {
       try {
            sql = "insert into produto (nome,codigo_barra,preco, descricao,fk_id_categoria, "
                    + "fk_id_taxa_imposto, fk_id_unidade_medida) value (?,?,?,?,?,?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setDouble(3, t.getPreco());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getFk_id_categoria());
            ps.setInt(6, t.getFk_id_taxa_imposto());
            ps.setInt(7, t.getFk_id_unidade_medida());

            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(Produto t, int id) {
        try {
            sql = "update produto set nome=?,codigo_barra=?,preco=?, descricao=?,fk_id_categoria=?, "
                    + "fk_id_taxa_imposto=?, fk_id_unidade_medida=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setDouble(3, t.getPreco());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getFk_id_categoria());
            ps.setInt(6, t.getFk_id_taxa_imposto());
            ps.setInt(7, t.getFk_id_unidade_medida());
            ps.setInt(8, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Produto> listAllEntities() {
        List<Produto> lista = new ArrayList<>();
        try {
            sql = "select * from produto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setPreco(result.getDouble("preco"));
                c.setDescricao(result.getString("descricao"));
                c.setFk_id_categoria(result.getInt("fk_id_categoria"));
                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Produto searchEntityByName(String name) {
        Produto c = null;
        try {
            sql = "select * from produto where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setPreco(result.getDouble("preco"));
                c.setDescricao(result.getString("descricao"));
                c.setFk_id_categoria(result.getInt("fk_id_categoria"));
                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Produto: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from produto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update produto set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
