/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.preco.PrecoProdutoArmazem;
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

    public Stock getProdutoForStock(int idProduto) {
        Stock s = null;
        String sql = "select p.id as idProduto, p.nome as nomeProduto, p.codigo_barra, "
                + " p.descricao,p.controle_stock, p.nivelstock, p.tipoProduto, p.unidadePorTipo, "
                + " c.id as idCategoria,c.grupo, c.subgrupo, c.familia,"
                + " u.id as idUnidade, u.nome as nomeUnidade, u.sigla as siglaUnidade, "
                + " s.saldo "
                + " from  produto p "
                + " left join stock s on p.id=s.idProduto "
                + " left join categoria c on p.idCategoria=c.id "
                + " left join unidadeMedida u on p.idUnidade=u.id where p.id=? ";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    s = new Stock();
                    Produto p = new Produto();
                    p.setId(result.getInt("idProduto"));
                    p.setNome(result.getString("nomeProduto"));
                    p.setCodigo_barra(result.getString("codigo_barra"));
                    p.setDescricao(result.getString("descricao"));
                    p.setControle_stock(result.getBoolean("controle_stock"));
                    p.setNivelstock(result.getInt("nivelstock"));
                    p.setTipoProduto(result.getString("tipoProduto"));
                    p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                    Categoria cat = new Categoria();
                    cat.setId(result.getInt("idCategoria"));
                    cat.setGrupo(result.getString("grupo"));
                    cat.setSubgrupo(result.getString("subgrupo"));
                    cat.setFamilia(result.getString("familia"));
                    p.setCategoria(cat);
                    UnidadeMedida u = new UnidadeMedida();
                    u.setId(result.getInt("idUnidade"));
                    u.setNome(result.getString("nomeUnidade"));
                    u.setSigla(result.getString("siglaUnidade"));
                    p.setUnidadeMedida(u);
                    s.setSaldo(result.getInt("saldo"));
                    s.setProduto(p);

                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Produto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }

    // corrigir
    public ObservableList<Stock> listStock(Integer idArm) {

        ObservableList<Stock> lista = FXCollections.observableArrayList();
        String sql = "SELECT "
                + "    p.id                    AS idProduto,"
                + "    p.nome                  AS nomeProduto,"
                + "    p.descricao             AS descricaoProduto,"
                + "    p.controle_stock, "
                + "    p.tipoProduto, "
                + "    p.unidadePorTipo, "
                + "    c.id                    AS idCategoria, "
                + "    c.grupo, "
                + "    c.subgrupo, "
                + "    c.familia, "
                + "    a.nome as nomeArmazem, "
                + "    a.tipo as tipoArmazem, "
                + "    um.nome                 AS nomeUnidade,"
                + "    um.sigla                AS siglaUnidade,"
                + "    i.id                    AS idImposto,"
                + "    i.nome                  AS nomeImposto,"
                + "    i.percentagem           AS percentagemImposto,"
                + "    s.idArmazem,"
                + "    ppa.precoBase,"
                + "    ppa.precoVenda,"
                + "    ppa.precoFinal,"
                + "    ppa.dataInicioVigencia,"
                + "    ppa.dataFimVigencia,"
                + "    ppa.estado AS estadoPreco,"
                + "    s.saldo "
                + "FROM stock s "
                + "JOIN produto p ON p.id = s.idProduto "
                + "JOIN Armazem a ON a.id = s.idArmazem "
                + "LEFT JOIN categoria c ON c.id = p.idCategoria "
                + "LEFT JOIN unidadeMedida um "
                + "    ON um.id = p.idUnidade "
                + "LEFT JOIN imposto i "
                + "    ON i.id = p.idImposto "
                + "LEFT JOIN precoProdutoArmazem ppa "
                + "    ON ppa.idProduto = s.idProduto "
                + "   AND ppa.idArmazem = s.idArmazem "
                + "   AND ppa.estado = 'activo'  "
                + "WHERE s.deleteStatus = 0 "
                + "AND p.deleteStatus = 0 "
                + "AND a.deleteStatus = 0 "
                + "AND (c.id IS NULL OR c.deleteStatus = 0) "
                + "AND (? IS NULL OR s.idArmazem = ?) ";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (idArm == null) {
                ps.setNull(1, java.sql.Types.INTEGER); // Para ? IS NULL
                ps.setNull(2, java.sql.Types.INTEGER); // Para ? IS NULL
            } else {
                ps.setInt(1, idArm);  // Para ? IS NULL
                ps.setInt(2, idArm);  // Para ? IS NULL
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stock stock = new Stock();
                    Produto p = new Produto();
                    p.setId(rs.getInt("idProduto"));
                    p.setNome(rs.getString("nomeProduto"));
                    p.setDescricao(rs.getString("descricaoProduto"));
                    p.setControle_stock(rs.getBoolean("controle_stock"));
                    p.setTipoProduto(rs.getString("tipoProduto"));
                    p.setUnidadesPorTipo(rs.getInt("unidadePorTipo"));

                    Categoria cat = new Categoria();
                    cat.setId(rs.getInt("idCategoria"));
                    cat.setGrupo(rs.getString("grupo"));
                    cat.setSubgrupo(rs.getString("subgrupo"));
                    cat.setFamilia(rs.getString("familia"));
                    p.setCategoria(cat);

                    UnidadeMedida u = new UnidadeMedida();
                    u.setNome(rs.getString("nomeUnidade"));
                    u.setSigla(rs.getString("siglaUnidade"));
                    p.setUnidadeMedida(u);

                    Imposto i = new Imposto();
                    i.setId(rs.getInt("idImposto"));
                    i.setNome(rs.getString("nomeImposto"));
                    i.setPercentagem(rs.getDouble("percentagemImposto"));
                    p.setImposto(i);

                    Armazem a = new Armazem();
                    a.setId(rs.getInt("idArmazem"));
                    a.setNome_arm(rs.getString("nomeArmazem"));
                    a.setTipo(rs.getString("tipoArmazem"));

                    PrecoProdutoArmazem pv = new PrecoProdutoArmazem();
                    pv.setPrecoBase(rs.getDouble("precoBase"));
                    pv.setPrecoVenda(rs.getDouble("precoVenda"));
                    pv.setPrecoFinal(rs.getDouble("precoFinal"));
                    pv.setDataInicioVigencia(rs.getString("dataInicioVigencia"));
                    pv.setDataFimVigencia(rs.getString("dataFimVigencia"));
                    pv.setEstado(rs.getString("estadoPreco"));

                    stock.setProduto(p);
                    stock.setArmazem(a);
                    stock.setPrecoProdutoArmazem(pv);
                    stock.setSaldo(rs.getInt("saldo"));

                    lista.add(stock);
                }

            }

        } catch (SQLException ex) {
            System.out.println("Erro Detalhes: " + ex);
            AlertUtilities.showDialog("Erro de consulta", ex.getMessage());
        }

        return lista;
    }

    public ObservableList<Stock> listProdutosParaVendaSemStock(Integer idArm) {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        String sql = "SELECT "
                + "    p.id                    AS idProduto,"
                + "    p.nome                  AS nomeProduto,"
                + "    p.descricao             AS descricaoProduto,"
                + "    p.controle_stock, "
                + "    p.tipoProduto, "
                + "    p.unidadePorTipo, "
                + "    c.id                    AS idCategoria, "
                + "    c.grupo, "
                + "    c.subgrupo, "
                + "    c.familia, "
                + "    a.id                    AS idArmazem, "
                + "    a.nome                  AS nomeArmazem, "
                + "    a.tipo                  AS tipoArmazem, "
                + "    um.nome                 AS nomeUnidade,"
                + "    um.sigla                AS siglaUnidade,"
                + "    i.id                    AS idImposto,"
                + "    i.nome                  AS nomeImposto,"
                + "    i.percentagem           AS percentagemImposto,"
                + "    ppa.precoBase,"
                + "    ppa.precoVenda,"
                + "    ppa.precoFinal,"
                + "    ppa.dataInicioVigencia,"
                + "    ppa.dataFimVigencia,"
                + "    ppa.estado AS estadoPreco "
                + "FROM precoProdutoArmazem ppa "
                + "JOIN produto p ON p.id = ppa.idProduto "
                + "JOIN armazem a ON a.id = ppa.idArmazem "
                + "LEFT JOIN categoria c ON c.id = p.idCategoria "
                + "LEFT JOIN unidadeMedida um ON um.id = p.idUnidade "
                + "LEFT JOIN imposto i ON i.id = p.idImposto "
                + "WHERE ppa.estado = 'activo' "
                + "AND ppa.deleteStatus = 0 "
                + "AND p.deleteStatus = 0 "
                + "AND a.deleteStatus = 0 "
                + "AND (c.id IS NULL OR c.deleteStatus = 0) "
                + "AND (? IS NULL OR ppa.idArmazem = ?) "
                + "ORDER BY p.nome, a.nome";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (idArm == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, idArm);
                ps.setInt(2, idArm);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stock stock = new Stock();

                    Produto p = new Produto();
                    p.setId(rs.getInt("idProduto"));
                    p.setNome(rs.getString("nomeProduto"));
                    p.setDescricao(rs.getString("descricaoProduto"));
                    p.setControle_stock(rs.getBoolean("controle_stock"));
                    p.setTipoProduto(rs.getString("tipoProduto"));
                    p.setUnidadesPorTipo(rs.getInt("unidadePorTipo"));

                    Categoria cat = new Categoria();
                    cat.setId(rs.getInt("idCategoria"));
                    cat.setGrupo(rs.getString("grupo"));
                    cat.setSubgrupo(rs.getString("subgrupo"));
                    cat.setFamilia(rs.getString("familia"));
                    p.setCategoria(cat);

                    UnidadeMedida u = new UnidadeMedida();
                    u.setNome(rs.getString("nomeUnidade"));
                    u.setSigla(rs.getString("siglaUnidade"));
                    p.setUnidadeMedida(u);

                    Imposto i = new Imposto();
                    i.setId(rs.getInt("idImposto"));
                    i.setNome(rs.getString("nomeImposto"));
                    i.setPercentagem(rs.getDouble("percentagemImposto"));
                    p.setImposto(i);

                    Armazem a = new Armazem();
                    a.setId(rs.getInt("idArmazem"));
                    a.setNome_arm(rs.getString("nomeArmazem"));
                    a.setTipo(rs.getString("tipoArmazem"));

                    PrecoProdutoArmazem pv = new PrecoProdutoArmazem();
                    pv.setPrecoBase(rs.getDouble("precoBase"));
                    pv.setPrecoVenda(rs.getDouble("precoVenda"));
                    pv.setPrecoFinal(rs.getDouble("precoFinal"));
                    pv.setDataInicioVigencia(rs.getString("dataInicioVigencia"));
                    pv.setDataFimVigencia(rs.getString("dataFimVigencia"));
                    pv.setEstado(rs.getString("estadoPreco"));

                    stock.setProduto(p);
                    stock.setArmazem(a);
                    stock.setPrecoProdutoArmazem(pv);
                    stock.setSaldo(0);

                    lista.add(stock);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro Detalhes: " + ex);
            AlertUtilities.showDialog("Erro de consulta", ex.getMessage());
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
        String sql = "SELECT p.nome AS produto, s.saldo "
                + "FROM produto p "
                + "JOIN stock s ON p.id = s.idProduto "
                + "JOIN categoria c ON p.idCategoria = c.id "
                + "JOIN armazem a ON s.idArmazem = a.id "
                + "WHERE p.deleteStatus = 0 "
                + "AND s.deleteStatus = 0 "
                + "AND c.deleteStatus = 0 "
                + "AND a.deleteStatus = 0 "
                + "AND (? IS NULL OR c.id = ?) "
                + "AND (? IS NULL OR a.id = ?) ";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

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

            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Stock s = new Stock();
                    Produto p = new Produto();
                    p.setNome(result.getString("produto"));
                    s.setSaldo(result.getInt("saldo"));
                    s.setProduto(p);
                    lista.add(s);
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes Stock: " + ex);
        }
        return lista;
    }

    @Override
    public ObservableList<Stock> getResumoStock(Integer idProduto) {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        String sql = "SELECT "
                + "    a.nome AS armazem, "
                + "    s.saldo "
                + "FROM stock s "
                + "JOIN produto p ON p.id = s.idProduto "
                + "JOIN armazem a ON a.id = s.idArmazem "
                + "WHERE p.id = ? "
                + "AND p.deleteStatus = 0 "
                + "AND s.deleteStatus = 0 "
                + "AND a.deleteStatus = 0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Stock s = new Stock();
                    Armazem a = new Armazem();
                    a.setNome_arm(result.getString("armazem"));
                    s.setArmazem(a);
                    s.setSaldo(result.getInt("saldo"));
                    lista.add(s);
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    /**
     * Este metodo faz a listagem dos produtos com estoque baixo em casa armazem
     * @return 
     */
    @Override
    public ObservableList<Stock> loadLowStock() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        String sql = "select p.nome as Produto, a.nome as Armazem, s.saldo "
                + "from stock s "
                + "LEFT JOIN produto p ON p.id=s.idProduto "
                + "LEFT JOIN armazem a ON a.id=s.idArmazem "
                + "where s.saldo<=p.nivelstock "
                + "AND s.deleteStatus = 0 "
                + "AND p.deleteStatus = 0 "
                + "AND a.deleteStatus = 0";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
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
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    /**
     * Este metodo faz a contagem dos produtos remanescente no estoque quando
     * informados a categoria e o armazem que os contem
     */
    public int countStock(Integer cat, Integer arm) {
        int total = 0;
        String sql = "select sum(s.saldo) as total_stock from stock s "
                + " LEFT JOIN armazem a ON a.id=s.idArmazem "
                + " LEFT JOIN produto p ON p.id=s.idProduto "
                + " LEFT JOIN categoria c ON c.id=p.idCategoria "
                + " where s.deleteStatus=0 "
                + " AND p.deleteStatus=0 "
                + " AND a.deleteStatus=0 "
                + " AND c.deleteStatus=0 "
                + " AND (? IS NULL OR a.id=?) AND (? IS NULL OR c.id=?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
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
            try (ResultSet result = ps.executeQuery()) {
                
                if (result.next()) {
                    total = result.getInt("total_stock");
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de contagem", "Detalhes: " + ex);
        }

        return total;
    }
}
