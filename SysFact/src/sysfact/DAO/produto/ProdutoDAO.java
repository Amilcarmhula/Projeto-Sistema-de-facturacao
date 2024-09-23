/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.JOptionPane;
import sysfact.model.armazem.Armazem;
import sysfact.model.categoria.Categoria;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.stock.Stock;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.util.Conexao;

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
    public void addEntity(Produto t) {
        try {
            sql = "insert into produto (nome,codigo_barra, descricao,fk_id_categoria, "
                    + "fk_id_unidade_medida,fk_id_taxa_imposto,contole_stock) value (?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidade_medida().getId());
            ps.setInt(6, t.getTaxa_imposto().getId());
            ps.setBoolean(7, t.isContole_stock());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    @Override
    public void updateEntityByID(Produto t, int id) {
        try {
            sql = "update produto set nome=?,codigo_barra=?, descricao=?,fk_id_categoria=?, "
                    + "fk_id_unidade_medida=?,fk_id_taxa_imposto=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCodigo_barra());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, t.getCategoria().getId());
            ps.setInt(5, t.getUnidade_medida().getId());
            ps.setInt(6, t.getTaxa_imposto().getId());
            ps.setInt(7, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public ObservableList<Produto> getCombinedEntities() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.contole_stock, "
                    + "		c.id as id_categoria, c.familia as categoria,"
                    + "         u.id as id_unidade, u.identificador, u.sigla,"
                    + "         t.id as id_taxa, t.identificador as taxa, t.percentagem,"
                    + "         pv.id as id_preco, pv.preco, pv.data_inicio, pv.data_fim,"
                    + "         s.saldo, s.fk_id_armazem, "
                    + "         a.nome AS nome_armazem, a.estado AS estado_armazem " 
                    + "FROM produto p "
                    + "LEFT JOIN categoria c ON c.id = p.fk_id_categoria "
                    + "LEFT JOIN unidade_medida u ON u.id = p.fk_id_unidade_medida "
                    + "LEFT JOIN taxa_imposto t ON t.id = p.fk_id_taxa_imposto "
                    + "LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id AND pv.estado=true "
                    + "LEFT JOIN stock s ON s.fk_id_produto=p.id "
                    + "LEFT JOIN armazem a ON a.id = s.fk_id_armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setDescricao(result.getString("descricao"));
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
                t.setIdentificador(result.getString("taxa"));
                t.setPercentagem(result.getDouble("percentagem"));
                c.setTaxa_imposto(t);
                PrecoVenda p = new PrecoVenda();
                p.setId(result.getInt("id_preco"));
                p.setPreco(result.getDouble("preco"));
                p.setData_inicio(result.getString("data_inicio"));
                p.setData_fim(result.getString("data_fim"));
                c.setPrecovenda(p);
                Stock s = new Stock();
                s.setSaldo(result.getInt("saldo"));
                c.setStock(s);
                c.setContole_stock(result.getBoolean("contole_stock"));
                Armazem arm = new Armazem();
                arm.setId(result.getInt("fk_id_armazem"));
                arm.setNome(result.getString("nome_armazem"));
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

    

    

    @Override
    public ObservableList<Produto> listAllEntities() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from produto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setDescricao(result.getString("descricao"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("fk_id_categoria"));
                c.setCategoria(cat);
//                c.setFk_id_taxa_imposto(result.getInt("fk_id_taxa_imposto"));
//                c.setUnidade_medida(result.getInt("fk_id_unidade_medida"));
                c.setData_criacao(result.getString("data_criacao"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ObservableList<Produto> listAllEntities2() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();

        try {
            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.fk_id_categoria,"
                    + "(SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,"
                    + "(SELECT tipo FROM categoria WHERE p.fk_id_categoria = categoria.id) AS cat_produto,"
                    + "p.active_or_not FROM produto p";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setDescricao(result.getString("descricao"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("fk_id_categoria"));
                cat.setTipo(result.getString("cat_produto"));
                c.setCategoria(cat);
//                c.setTaxa_imposto(result.getInt("fk_id_taxa_imposto"));
//                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
                UnidadeMedida u = new UnidadeMedida();
                u.setSigla(result.getString("sigla_unidade"));
                c.setUnidade_medida(u);
//                c.setData_criacao(result.getString("data_criacao"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public ObservableList<Produto> searchEntityByCategoria(Produto prodID) {
        ObservableList<Produto> lista = FXCollections.observableArrayList();

        try {
            sql = "SELECT p.id, p.nome, p.codigo_barra, p.descricao, p.fk_id_categoria,"
                    + "                     (SELECT sigla FROM unidade_medida WHERE p.fk_id_unidade_medida = unidade_medida.id) AS sigla_unidade,"
                    + "                     p.active_or_not FROM produto p where fk_id_categoria=? ";
            ps = con.prepareStatement(sql);
            ps.setInt(1, prodID.getCategoria().getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Produto c = new Produto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setCodigo_barra(result.getString("codigo_barra"));
                c.setDescricao(result.getString("descricao"));
                Categoria cat = new Categoria();
                cat.setId(result.getInt("fk_id_categoria"));
                c.setCategoria(cat);
//                c.setTaxa_imposto(result.getInt("fk_id_taxa_imposto"));
//                c.setFk_id_unidade_medida(result.getInt("fk_id_unidade_medida"));
                UnidadeMedida u = new UnidadeMedida();
                u.setSigla(result.getString("sigla_unidade"));
                c.setUnidade_medida(u);
//                c.setData_criacao(result.getString("data_criacao"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

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
            sql = "update produto set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar produto: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
