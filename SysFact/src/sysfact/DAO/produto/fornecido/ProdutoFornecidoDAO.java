/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.produto.fornecido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.armazem.Armazem;
import sysfact.model.cliente.Cliente;
import sysfact.model.produto.Produto;
import sysfact.model.produto.fornecido.ProdutoFornecido;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecidoDAO implements ProdutoFornecidoDAOImpl<ProdutoFornecido> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoFornecidoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ProdutoFornecido t) {
        try {
            sql = "insert into produto_fornecido (quantidade,data_fornecida,custo, fk_id_armazem,fk_id_produto, "
                    + "fk_id_fornecedor,fk_id_usuario) value (?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setInt(1, t.getQuantidade());
            ps.setString(2, t.getData_fornecida());
            ps.setDouble(3, t.getCusto());
            ps.setInt(4, t.getArmazem().getId());
            ps.setInt(5, t.getProduto().getId());
            ps.setInt(6, t.getFornecedor().getId());
            ps.setInt(7, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Produto fornecido: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ProdutoFornecido t, int id) {
        try {
            sql = "update produto_fornecido set quantidade=?,data_fornecida=?,custo=?, fk_id_armazem=?, "
                    + "fk_id_fornecedor=?,fk_id_usuario=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, t.getQuantidade());
            ps.setString(2, t.getData_fornecida());
            ps.setDouble(3, t.getCusto());
            ps.setInt(4, t.getArmazem().getId());
            ps.setInt(5, t.getFornecedor().getId());
            ps.setInt(6, t.getUsuario().getId());

            ps.setInt(7, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Produto Fornecido: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<ProdutoFornecido> listAllEntities() {
        ObservableList<ProdutoFornecido> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT p.id,"
                    + "(SELECT nome FROM produto WHERE p.fk_id_produto = produto.id) AS nome_p,"
                    + "(SELECT nome FROM agente WHERE p.fk_id_agente = agente.id) AS nome_f,"
                    + "(SELECT nome FROM armazem WHERE p.fk_id_armazem = armazem.id) AS nome_a,"
                    + "p.quantidade, p.custo, p.data_fornecida, p.active_or_not FROM produto_fornecido p;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                ProdutoFornecido c = new ProdutoFornecido();
                c.setId(result.getInt("id"));
                c.setQuantidade(result.getInt("quantidade"));
                c.setData_fornecida(result.getString("data_fornecida"));
                c.setCusto(result.getDouble("custo"));
                Armazem a = new Armazem();
                a.setNome_arm(result.getString("nome_a"));
                c.setArmazem(a);
                Produto p = new Produto();
                p.setNomeProduto(result.getString("nome_p"));
                c.setProduto(p);
                Cliente ag = new Cliente();
                ag.setNome_cli(result.getString("nome_f"));
//                c.setAgente(ag);
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//                if (c.isActive_or_not()) {
//                    lista.add(c);
//                }
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto Fornecido: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
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
                c.setData_fornecida(result.getString("data_fornecida"));
                c.setCusto(result.getDouble("custo"));
                Armazem a = new Armazem();
                a.setId(result.getInt("fk_id_armazem"));
                c.setArmazem(a);
                Produto p = new Produto();
                p.setId(result.getInt("fk_id_produto"));
                c.setProduto(p);
                Cliente ag = new Cliente();
                ag.setId(result.getInt("fk_id_agente"));
//                c.setAgente(ag);
//                Usuario u = new Usuario();
//                u.setId(result.getInt("fk_id_usuario"));
//                c.setUsuario(u);
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
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
    public void deleteEntityByUSER(ProdutoFornecido x) {
        try {
            sql = "update produto_fornecido set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Produto fornecido: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
