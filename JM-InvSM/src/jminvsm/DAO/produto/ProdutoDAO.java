/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.JOptionPane;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.produto.Produto;
import jminvsm.model.stock.Stock;
import jminvsm.model.taxa_imposto.TaxaImposto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import static jminvsm.util.AlertUtilities.showDialog;
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
            sql = "insert into produto (nome,codigo_barra, descricao,fk_id_categoria, "
                    + "fk_id_unidade_medida,fk_id_taxa_imposto,contole_stock,nivelstock) value (?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNomeProduto());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidade_medida().getId());
            ps.setInt(6, t.getTaxa_imposto().getId());
            ps.setBoolean(7, t.isContole_stock());
            ps.setInt(8, t.getNivelstock());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
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
            sql = "update produto set nome=?,codigo_barra=?, descricao=?,fk_id_categoria=?, "
                    + "fk_id_unidade_medida=?,fk_id_taxa_imposto=?, nivelstock=?, contole_stock=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNomeProduto());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidade_medida().getId());
            ps.setInt(6, t.getTaxa_imposto().getId());
            ps.setInt(7, t.getNivelstock());
            ps.setBoolean(8, t.isContole_stock());
            ps.setInt(9, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
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
            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.contole_stock, p.nivelstock, "
                    + "		c.id as id_categoria, c.familia as categoria,"
                    + "         u.id as id_unidade, u.identificador, u.sigla,"
                    + "         t.id as id_taxa, t.identificador as taxa, t.percentagem,"
                    + "         s.saldo, s.fk_id_armazem, "
                    + "         a.nome AS nome_armazem, a.estado AS estado_armazem "
                    + "FROM produto p "
                    + "LEFT JOIN categoria c ON c.id = p.fk_id_categoria "
                    + "LEFT JOIN unidade_medida u ON u.id = p.fk_id_unidade_medida "
                    + "LEFT JOIN taxa_imposto t ON t.id = p.fk_id_taxa_imposto "
                    + "LEFT JOIN stock s ON s.fk_id_produto=p.id "
                    + "LEFT JOIN armazem a ON a.id = s.fk_id_armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNomeProduto(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setDescricao(result.getString("descricao"));
                c.setNivelstock(result.getInt("nivelstock"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("id_categoria"));
                cat.setFamilia(result.getString("categoria"));
                c.setCategoria(cat);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("id_unidade"));
                u.setIdentificador(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                c.setUnidade_medida(u);
                TaxaImposto t = new TaxaImposto();
                t.setId(result.getInt("id_taxa"));
                t.setNomeImposto(result.getString("taxa"));
                t.setPercentagemImposto(result.getDouble("percentagem"));
                c.setTaxa_imposto(t);
                Stock s = new Stock();
                s.setSaldo(result.getInt("saldo"));
                c.setStock(s);
                c.setContole_stock(result.getBoolean("contole_stock"));
                Armazem arm = new Armazem();
                arm.setId(result.getInt("fk_id_armazem"));
                arm.setNome_arm(result.getString("nome_armazem"));
                arm.setEstado(result.getBoolean("estado_armazem"));
                c.setArmazem(arm);
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    /*Busca o ID do ultimo produto adicionado para poder acicionar o preco*/
    @Override
    public Produto getLastEntity() {
        Produto x = null;
        try {
            sql = "SELECT id FROM produto ORDER BY id DESC LIMIT 1";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                x = new Produto();
                x.setId(result.getInt("id"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return x;
    }

    public int countProdutos(Integer cat) {
        int total = 0;
        try {
            sql = "select count(*) as total_produto from produto p "
                    + "LEFT JOIN categoria c ON c.id=p.fk_id_categoria "
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
            showDialog("Erro de contagem", "Detalhes: " + ex);
        }

        return total;
    }

//    @Override
//    public ObservableList<Produto> listAllEntities() {
//        ObservableList<Produto> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from produto";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Produto c = new Produto();
//                c.setId(result.getInt("id"));
//                c.setNomeProduto(result.getString("nome"));
//                c.setCodigo_barra(result.getString("codigo_barra"));
//                c.setDescricao(result.getString("descricao"));
//                Categoria cat = new Categoria();
//                cat.setId(result.getInt("fk_id_categoria"));
//                c.setCategoria(cat);
////                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
////                c.setUnidade_medida(result.getInt("fk_id_unidade_medida"));
//                c.setData_criacao(result.getString("data_criacao"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//    @Override
//    public ObservableList<Produto> listAllEntities2() {
//        ObservableList<Produto> lista = FXCollections.observableArrayList();
//
//        try {
//            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.fk_id_categoria,p.nivelstock"
//                    + "(SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,"
//                    + "(SELECT tipo FROM categoria WHERE p.fk_id_categoria = categoria.id) AS cat_produto,"
//                    + "p.active_or_not FROM produto p";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Produto c = new Produto();
//                c.setId(result.getInt("id"));
//                c.setNomeProduto(result.getString("nome"));
//                c.setCodigo_barra(result.getString("codigo_barra"));
//                c.setDescricao(result.getString("descricao"));
//                c.setNivelstock(result.getInt("nivelstock"));
//                Categoria cat = new Categoria();
//                cat.setId(result.getInt("fk_id_categoria"));
//                cat.setTipo(result.getString("cat_produto"));
//                c.setCategoria(cat);
////                c.setTaxa_imposto(result.getInt("fk_id_taxa_imposto"));
////                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
//                UnidadeMedida u = new UnidadeMedida();
//                u.setSigla(result.getString("sigla_unidade"));
//                c.setUnidade_medida(u);
////                c.setData_criacao(result.getString("data_criacao"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//    public ObservableList<Produto> searchEntityByCategoria(Produto prodID) {
//        ObservableList<Produto> lista = FXCollections.observableArrayList();
//
//        try {
//            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.fk_id_categoria,"
//                    + "                     (SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,"
//                    + "                     p.active_or_not FROM produto p where fk_id_categoria=? ";
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, prodID.getCategoria().getId());
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Produto c = new Produto();
//                c.setId(result.getInt("id"));
//                c.setNomeProduto(result.getString("nome"));
//                c.setCodigo_barra(result.getString("codigo_barra"));
//                c.setDescricao(result.getString("descricao"));
//                Categoria cat = new Categoria();
//                cat.setId(result.getInt("fk_id_categoria"));
//                c.setCategoria(cat);
////                c.setTaxa_imposto(result.getInt("fk_id_taxa_imposto"));
////                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
//                UnidadeMedida u = new UnidadeMedida();
//                u.setSigla(result.getString("sigla_unidade"));
//                c.setUnidade_medida(u);
////                c.setData_criacao(result.getString("data_criacao"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//    @Override
//    public Produto searchEntityByName(String name) {
//        Produto c = null;
//        try {
//            sql = "select * from produto where nome like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + name + "%");
//            ResultSet result = ps.executeQuery();
////            if (result.next()) {
//            while (result.next()) {
//                c = new Produto();
//                c.setId(result.getInt("id"));
//                c.setNome(result.getString("nome"));
//                c.setCodigo_barra(result.getString("codigo_barra"));
//                c.setPreco(result.getDouble("preco"));
//                c.setDescricao(result.getString("descricao"));
//                c.setFk_id_categoria(result.getInt("fk_id_categoria"));
//                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
//                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Produto: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return c;
//
//    }
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from produto where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar produto: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    @Override
    public void deleteEntityByUSER(Produto x) {
        try {
            sql = "delete from produto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            JOptionPane.showMessageDialog(null, "Erro ao deletar produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
