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
import sysfact.logical.ProdutoFornecido;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecidoDAO implements DaoImpl<ProdutoFornecido> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoFornecidoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(ProdutoFornecido t) {
       try {
            sql = "insert into produto_fornecido (quantidade,data_fornedida,custo, fk_id_armazem,fk_id_produto, "
                    + "fk_id_agente,fk_id_usuario) value (?,?,?,?,?,?,?)";
        
            ps = con.prepareStatement(sql);
        
            ps.setInt(1, t.getQuantidade());
            ps.setString(2, t.getData_fornedida());
            ps.setDouble(3, t.getCusto());
            ps.setInt(4, t.getFk_id_armazem());
            ps.setInt(5, t.getFk_id_produto());
            ps.setInt(6, t.getFk_id_agente());
            ps.setInt(7, t.getFk_id_usuario());


            ps.execute();
            ps.close();
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Produto fornecido: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void updateEntityByID(ProdutoFornecido t, int id) {
        try {
            sql = "update produto_fornecido set quantidade=?,data_fornedida=?,"
                    + "custo=?, fk_id_armazem=?,fk_id_produto=?,"
                    + "fk_id_agente=?, fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, t.getQuantidade());
            ps.setString(2, t.getData_fornedida());
            ps.setDouble(3, t.getCusto());
            ps.setInt(4, t.getFk_id_armazem());
            ps.setInt(5, t.getFk_id_produto());
            ps.setInt(6, t.getFk_id_agente());
            ps.setInt(7, t.getFk_id_usuario());

            ps.setInt(8, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Produto Fornecido: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<ProdutoFornecido> listAllEntities() {
        List<ProdutoFornecido> lista = new ArrayList<>();
        try {
            sql = "select * from produto_fornecido";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ProdutoFornecido c = new ProdutoFornecido();
                c.setId(result.getInt("id"));
                c.setQuantidade(result.getInt("quantidade"));
                c.setData_fornedida(result.getString("data_fornecida"));
                c.setCusto(result.getDouble("custo"));
                c.setFk_id_armazem(result.getInt("fk_id_armazem"));
                c.setFk_id_produto(result.getInt("fk_id_produto"));
                c.setFk_id_agente(result.getInt("fk_id_agente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
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
    public ProdutoFornecido searchEntityByName(String data) {
        ProdutoFornecido c = null;
        try {
            sql = "select * from produto_fornecido where data_fornedida like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + data + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new ProdutoFornecido();
                c.setId(result.getInt("id"));
                c.setQuantidade(result.getInt("quantidade"));
                c.setData_fornedida(result.getString("data_fornecida"));
                c.setCusto(result.getDouble("custo"));
                c.setFk_id_armazem(result.getInt("fk_id_armazem"));
                c.setFk_id_produto(result.getInt("fk_id_produto"));
                c.setFk_id_agente(result.getInt("fk_id_agente"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Produto fornecido: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from produto_fornecido where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Produto fornecido: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update produto_fornecido set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Produto fornecido: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
