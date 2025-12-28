/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.produto.Produto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.stock.Stock;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.util.AlertUtilities;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoDAO implements ProdutoDAOImpl<Produto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Produto t) {
        boolean isSuccess = false;
        try {
            sql = "insert into produto (nome, codigo_barra, descricao,fk_id_categoria, "
                    + "fk_id_unidade_medida,fk_id_imposto,controle_stock,nivelstock, tipo, unidadePorTipo) value (?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidadeMedida().getId());
            ps.setInt(6, t.getImposto().getId());
            ps.setBoolean(7, t.isControle_stock());
            ps.setInt(8, t.getNivelstock());
            ps.setString(9, t.getTipoProduto());
            ps.setInt(10, t.getUnidadesPorTipo());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
//            showDialog("Erro persistencia", "Detalhes: " + ex); 
            System.out.println("Erro persistencia:" + ex);

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
    public boolean updateEntityByID(Produto t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update produto set nome=?,codigo_barra=? ,descricao=?,fk_id_categoria=?, "
                    + "fk_id_unidade_medida=?,fk_id_imposto=?, nivelstock=?, controle_stock=?, tipo=?,"
                    + " unidadePorTipo=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidadeMedida().getId());
            ps.setInt(6, t.getImposto().getId());
            ps.setInt(7, t.getNivelstock());
            ps.setBoolean(8, t.isControle_stock());
            ps.setString(9, t.getTipoProduto());
            ps.setInt(10, t.getUnidadesPorTipo());
            ps.setInt(11, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
//            showDialog("Erro persistencia", "Detalhes: " + ex);
            System.out.println("Erro persistencia Detalhes: " + ex);

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
    public ObservableList<Produto> getCombinedEntities() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.controle_stock, p.nivelstock, "
                    + "p.tipo, p.unidadePorTipo, "
                    + "		c.id as id_categoria, c.familia as categoria,"
                    + "         u.id as id_unidade, u.identificador, u.sigla,"
                    + "         t.id as id_taxa, t.nome as taxa, t.percentagem,"
                    + "FROM produto p "
                    + "LEFT JOIN categoria c ON c.id = p.fk_id_categoria "
                    + "LEFT JOIN unidade_medida u ON u.id = p.fk_id_unidade_medida "
                    + "LEFT JOIN imposto t ON t.id = p.fk_id_imposto ";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto p = new Produto();
                p.setId(result.getInt("id"));
                p.setNome(result.getString("nome"));
                p.setCodigo_barra(result.getString("codigo_barra"));
                p.setDescricao(result.getString("descricao"));
                p.setNivelstock(result.getInt("nivelstock"));
                p.setTipoProduto(result.getString("tipo"));
                p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("id_categoria"));
                cat.setFamilia(result.getString("categoria"));
                p.setCategoria(cat);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("id_unidade"));
                u.setNome(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                p.setUnidadeMedida(u);
                Imposto t = new Imposto();
                t.setId(result.getInt("id_taxa"));
                t.setNome(result.getString("taxa"));
                t.setPercentagem(result.getDouble("percentagem"));
                p.setImposto(t);
                p.setControle_stock(result.getBoolean("controle_stock"));
                lista.add(p);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public List<Produto> listAllEntities() {
        List<Produto> lista = new ArrayList<>();
        try {
            sql = "SELECT * FROM produto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto p = new Produto();
                p.setId(result.getInt("id"));
                p.setNome(result.getString("nome"));
                p.setCodigo_barra(result.getString("codigo_barra"));
                p.setDescricao(result.getString("descricao"));
                p.setNivelstock(result.getInt("nivelstock"));
                p.setTipoProduto(result.getString("tipo"));
                p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("fk_id_categoria"));
                p.setCategoria(cat);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("fk_id_unidade_medida"));
                p.setUnidadeMedida(u);
                Imposto t = new Imposto();
                t.setId(result.getInt("fk_id_imposto"));
                p.setImposto(t);
                p.setControle_stock(result.getBoolean("controle_stock"));
                lista.add(p);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public ObservableList<Stock> listInventario() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            sql = "select p.id as idProduto, p.nome as nomeProduto, p.codigo_barra,"
                    + " p.descricao,p.controle_stock, p.nivelstock, p.tipo, p.unidadePorTipo, "
                    + "	a.id as idArmazem, a.nome as nomeArmazem, a.tipo, "
                    + " c.id as idCategoria, c.familia, "
                    + "	u.id as idUnidade, u.sigla as siglaUnidade, "
                    + " i.id as idImposto, i.nome as nomeImposto, "
                    + " pv.precoNormal, pv.precoVenda, pv.precoFinal, "
                    + " s.saldo "
                    + "	from  produto p "
                    + "	join stock s on p.id=s.fk_id_produto "
                    + " join precovenda pv on p.id=pv.idProduto and s.fk_id_armazem=pv.idArmazem "
                    + " join armazem a on s.fk_id_armazem=a.id "
                    + " join categoria c on p.fk_id_categoria=c.id "
                    + " join unidade_medida u on p.fk_id_unidade_medida=u.id "
                    + " join imposto i on p.fk_id_imposto=i.id ";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Stock s = new Stock();
                Produto p = new Produto();
                p.setId(result.getInt("idProduto"));
                p.setNome(result.getString("nomeProduto"));
                p.setCodigo_barra(result.getString("codigo_barra"));
                p.setDescricao(result.getString("descricao"));
                p.setControle_stock(result.getBoolean("controle_stock"));
                p.setNivelstock(result.getInt("nivelstock"));
                p.setTipoProduto(result.getString("tipo"));
                p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
                ppa.setPreco(result.getDouble("PrecoProdutoArmazem"));
                Armazem a = new Armazem();
                ppa.setArmazem(a);
                a.setId(result.getInt("idArmazem"));
                a.setNome_arm(result.getString("nomeArmazem"));
                a.setTipo(result.getString("tipo"));
                s.setArmazem(a);
                Categoria c = new Categoria();
                c.setId(result.getInt("idCategoria"));
                c.setFamilia(result.getString("familia"));
                p.setCategoria(c);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("idUnidade"));
                u.setSigla(result.getString("siglaUnidade"));
                p.setUnidadeMedida(u);
                Imposto i = new Imposto();
                i.setId(result.getInt("idImposto"));
                i.setNome(result.getString("nomeImposto"));
                p.setImposto(i);
                s.setProduto(p);
                s.setSaldo(result.getInt("saldo"));
                lista.add(s);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro Detalhes: " + ex);
            AlertUtilities.showDialog("Erro de consulta", "Detalhes: " + ex);

        }
        return lista;
    }

    /*Busca o ID do ultimo produto adicionado para poder acicionar o preco*/
    @Override
    public Produto getLastEntity() {
        Produto p = null;
        try {
            sql = "SELECT * FROM produto ORDER BY id DESC LIMIT 1";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                p = new Produto();
                p.setId(result.getInt("id"));
                p.setNome(result.getString("nome"));
                p.setCodigo_barra(result.getString("codigo_barra"));
                p.setDescricao(result.getString("descricao"));
                p.setNivelstock(result.getInt("nivelstock"));
                p.setTipoProduto(result.getString("tipo"));
                p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("fk_id_categoria"));
                p.setCategoria(cat);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("fk_id_unidade_medida"));
                p.setUnidadeMedida(u);
                Imposto t = new Imposto();
                t.setId(result.getInt("fk_id_imposto"));
                p.setImposto(t);
                p.setControle_stock(result.getBoolean("controle_stock"));

            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return p;
    }

    public int countProdutos(Integer cat) {
        int total = 0;
        try {
            sql = "select count(*) as total_produto from produto p "
                    + "LEFT JOIN categoria c ON c.id=p.idCategoria "
                    + "where (? IS NULL OR c.id = ?)";
            ps = con.prepareStatement(sql);
            // Se a categoria não for informado, passar valor nulo
            if (cat == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, cat);
                ps.setInt(2, cat);
            }
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                total = result.getInt("total_produto");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro de contagem: " + ex);
//            showDialog("Erro de contagem", "Detalhes: " + ex);

        }

        return total;
    }

    @Override
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        try {
            sql = "delete from produto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
//            showDialog("Erro de exclusao", "Detalhes: " + ex);
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

    public static void main(String[] args) throws SQLException {
        Produto p = new Produto();
        p.setNome("Prod sem IVA");
        p.setCodigo_barra("1234567890124");
        p.setDescricao("Descricar de teste do Produto 4");
        p.setControle_stock(true);
        p.setNivelstock(14);
        Categoria c = new Categoria();
        c.setId(1);
        p.setCategoria(c);
        UnidadeMedida u = new UnidadeMedida();
        u.setId(1);
        p.setUnidadeMedida(u);
        Imposto i = new Imposto();
        i.setId(1);
        p.setImposto(i);
        ProdutoDAO dao = new ProdutoDAO();
        dao.addEntity(p);
        for (Produto pro : dao.listAllEntities()) {
            System.out.println(pro.getNome());
        }
        System.out.println("O Last: " + dao.getLastEntity().getNivelstock());
//        dao.deleteEntity(2);
        System.out.println("O Last: " + dao.getLastEntity().getNivelstock());

    }

}
