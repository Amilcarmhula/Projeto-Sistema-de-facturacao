/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.produto.Produto;
import jminvsm.model.stock.Stock;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.util.Conexao;
import jminvsm.util.AlertUtilities;
import static jminvsm.util.AlertUtilities.showDialog;

/**
 *
 * @author JM-Tecnologias
 */
public class StockDAO implements StockDAOImpl<Stock> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public StockDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    public ObservableList<Stock> listInventario() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            sql = "select p.id as idProduto, p.nome as nomeProduto, p.codigo_barra,"
                    + " p.preco, p.descricao,p.controle_stock, p.nivelstock,"
                    + "	a.id as idArmazem, a.nome as nomeArmazem, a.tipo,"
                    + " c.id as idCategoria, c.familia,"
                    + "	u.id as idUnidade, u.sigla as siglaUnidade,"
                    + " i.id as idImposto, i.nome as nomeImposto, "
                    + " s.saldo "
                    + "	from stock s"
                    + "	join produto p on s.fk_id_produto=p.id "
                    + " join armazem a on s.fk_id_armazem=a.id "
                    + " join categoria c on p.fk_id_categoria=c.id "
                    + " join unidade_medida u on p.fk_id_unidade_medida=u.id "
                    + " join imposto i on p.fk_id_imposto=i.id";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Stock s = new Stock();
                Produto p = new Produto();
                p.setId(result.getInt("idProduto"));
                p.setPreco(result.getDouble("preco"));
                p.setNome(result.getString("nomeProduto"));
                p.setCodigo_barra(result.getString("codigo_barra"));
                p.setDescricao(result.getString("descricao"));
                p.setControle_stock(result.getBoolean("controle_stock"));
                p.setNivelstock(result.getInt("nivelstock"));
                Armazem a = new Armazem();
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
                p.setUnidade_medida(u);
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

    /**
     * Este metodo faz a listagem dos produtos e seu saldo remanescente no
     * estoque quando informados a categoria e o armazem que os contem
     */
    @Override
    public ObservableList<Stock> getStock(Integer cat, Integer arm) {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT p.nome AS produto, s.saldo "
                    + "FROM produto p "
                    + "JOIN stock s ON p.id = s.fk_id_produto "
                    + "JOIN categoria c ON p.fk_id_categoria = c.id "
                    + "JOIN armazem a ON s.fk_id_armazem = a.id "
                    + "WHERE (? IS NULL OR c.id = ?) "
                    + "AND (? IS NULL OR a.id = ?) ";
            ps = con.prepareStatement(sql);

            // Se a categoria ou armazém não forem informados, passar valores nulos
            if (cat == null) {
                ps.setNull(1, java.sql.Types.INTEGER); // Para ? IS NULL
                ps.setNull(2, java.sql.Types.INTEGER); // Para c.id = ?
            } else {
                ps.setInt(1, cat);  // Para ? IS NULL
                ps.setInt(2, cat);  // Para c.id = ?
            }

            if (arm == null) {
                ps.setNull(3, java.sql.Types.INTEGER); // Para ? IS NULL
                ps.setNull(4, java.sql.Types.INTEGER); // Para a.id = ?
            } else {
                ps.setInt(3, arm);  // Para ? IS NULL
                ps.setInt(4, arm);  // Para a.id = ?
            }
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Stock s = new Stock();
                Produto p = new Produto();
                p.setNome(result.getString("produto"));
                s.setSaldo(result.getInt("saldo"));
                s.setProduto(p);
                lista.add(s);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            AlertUtilities.showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    /**
     * Este metodo faz a listagem dos produtos com estoque baixo em casa armazem
     */
    @Override
    public ObservableList<Stock> loadLowStock() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            sql = "select p.nome as Produto, a.nome as Armazem, s.saldo "
                    + "from stock s "
                    + "LEFT JOIN produto p ON p.id=s.fk_id_produto "
                    + "LEFT JOIN armazem a ON a.id=s.fk_id_armazem "
                    + "where s.saldo<=p.nivelstock";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Stock s = new Stock();
                Produto p = new Produto();
                p.setNome(result.getString("Produto"));
                s.setProduto(p);
                Armazem a = new Armazem();
                a.setNome_arm(result.getString("Armazem"));
                s.setArmazem(a);
                s.setSaldo(result.getInt("saldo"));
                lista.add(s);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            AlertUtilities.showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    /**
     * Este metodo faz a contagem dos produtos remanescente no estoque quando
     * informados a categoria e o armazem que os contem
     */
    public int countStock(Integer cat, Integer arm) {
        int total = 0;
        try {
            sql = "select sum(s.saldo) as total_stock from stock s "
                    + "LEFT JOIN armazem a ON a.id=s.fk_id_armazem "
                    + "LEFT JOIN produto p ON p.id=s.fk_id_produto "
                    + "LEFT JOIN categoria c ON c.id=p.fk_id_categoria "
                    + "where "
                    + "(? IS NULL OR a.id=?) AND (? IS NULL OR c.id=?)";
            ps = con.prepareStatement(sql);
            // Se a categoria não for informado, passar valor nulo
            if (arm == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, arm);
                ps.setInt(2, arm);
            }

            if (cat == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, cat);
                ps.setInt(4, cat);
            }
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                total = result.getInt("total_stock");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog("Erro de contagem", "Detalhes: " + ex);
        }

        return total;
    }
}
