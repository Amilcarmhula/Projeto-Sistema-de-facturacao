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
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.preco.PrecoVenda;
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
            sql = "SELECT "
                    + "    p.id               AS idProduto,"
                    + "    p.nome             AS nomeProduto, "
                    + "    p.codigo_barra, "
                    + "    p.descricao, "
                    + "    p.controle_stock, "
                    + "    p.nivelstock, "
                    + "    p.tipoProduto, "
                    + "    p.unidadePorTipo, "
                    + "    a.id               AS idArmazem, "
                    + "    a.nome             AS nomeArmazem, "
                    + "    a.tipo             AS tipoArmazem, "
                    + "    c.id               AS idCategoria, "
                    + "    c.grupo, "
                    + "    c.subgrupo, "
                    + "    c.familia, "
                    + "    u.id               AS idUnidade, "
                    + "    u.nome             AS unidadeNome, "
                    + "    u.sigla            AS siglaUnidade, "
                    + "    i.id               AS idImposto, "
                    + "    i.nome             AS nomeImposto, "
                    + "    i.percentagem      AS impPerc, "
                    + "    pv.precoNormal, "
                    + "    pv.precoVenda, "
                    + "    pv.precoFinal, "
                    + "    pv.estado          AS estadoPreco, "
                    + "    s.saldo "
                    + "FROM stock s "
                    + "JOIN produto p       ON p.id = s.idProduto "
                    + "JOIN armazem a       ON a.id = s.idArmazem "
                    + "JOIN categoria c     ON c.id = p.idCategoria "
                    + "LEFT JOIN unidadeMedida u ON u.id = p.idUnidade "
                    + "LEFT JOIN imposto i  ON i.id = p.idImposto "
                    + "LEFT JOIN precovenda pv "
                    + "    ON pv.id = ("
                    + "        SELECT pv2.id "
                    + "        FROM precovenda pv2 "
                    + "        WHERE pv2.idProduto = s.idProduto "
                    + "          AND pv2.idArmazem = s.idArmazem "
                    + "          AND pv2.estado = 'activo' "
                    + "        ORDER BY pv2.data_criacao DESC "
                    + "        LIMIT 1 )";
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Stock stock = new Stock();

                Produto p = new Produto();
                p.setId(rs.getInt("idProduto"));
                p.setNome(rs.getString("nomeProduto"));
                p.setCodigo_barra(rs.getString("codigo_barra"));
                p.setDescricao(rs.getString("descricao"));
                p.setControle_stock(rs.getBoolean("controle_stock"));
                p.setNivelstock(rs.getInt("nivelstock"));
                p.setTipoProduto(rs.getString("tipoProduto"));
                p.setUnidadesPorTipo(rs.getInt("unidadePorTipo"));

                Categoria c = new Categoria();
                c.setId(rs.getInt("idCategoria"));
                c.setGrupo(rs.getString("grupo"));
                c.setSubgrupo(rs.getString("subgrupo"));
                c.setFamilia(rs.getString("familia"));
                p.setCategoria(c);

                UnidadeMedida u = new UnidadeMedida();
                u.setId(rs.getInt("idUnidade"));
                u.setNome(rs.getString("unidadeNome"));
                u.setSigla(rs.getString("siglaUnidade"));
                p.setUnidadeMedida(u);

                Imposto i = new Imposto();
                i.setId(rs.getInt("idImposto"));
                i.setNome(rs.getString("nomeImposto"));
                i.setPercentagem(rs.getDouble("impPerc"));
                p.setImposto(i);
                

                Armazem a = new Armazem();
                a.setId(rs.getInt("idArmazem"));
                a.setNome_arm(rs.getString("nomeArmazem"));
                a.setTipo(rs.getString("tipoArmazem"));
                
                PrecoVenda pv = new PrecoVenda();
                pv.setPrecoNormal(rs.getDouble("precoNormal"));
                pv.setPrecoVenda(rs.getDouble("precoVenda"));
                pv.setPrecoFinal(rs.getDouble("precoFinal"));
                pv.setEstado(rs.getString("estadoPreco"));

                stock.setProduto(p);
                stock.setArmazem(a);
                stock.setPrecoVenda(pv);
                stock.setSaldo(rs.getInt("saldo"));

                lista.add(stock);
            }

            rs.close();
            ps.close();

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
        try {
            sql = "SELECT p.nome AS produto, s.saldo "
                    + "FROM produto p "
                    + "JOIN stock s ON p.id = s.idProduto "
                    + "JOIN categoria c ON p.idCategoria = c.id "
                    + "JOIN armazem a ON s.idArmazem = a.id "
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
                    + "LEFT JOIN produto p ON p.id=s.idProduto "
                    + "LEFT JOIN armazem a ON a.id=s.idArmazem "
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
                    + " LEFT JOIN armazem a ON a.id=s.idArmazem "
                    + " LEFT JOIN produto p ON p.id=s.idProduto "
                    + " LEFT JOIN categoria c ON c.id=p.idCategoria "
                    + " where (? IS NULL OR a.id=?) AND (? IS NULL OR c.id=?)";
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

    public static void main(String[] args) throws SQLException {
//        StockDAO dao = new StockDAO();
//
//        // Executa o método que queremos testar
//        ObservableList<Stock> inventario = dao.listInventario();
//
//        System.out.println("=== Teste de Inventário ===");
//        for (Stock s : inventario) {
//            System.out.println("ID: " + s.getProduto().getId());
//            System.out.println("Produto: " + s.getProduto().getNome());
//            System.out.println("Armazém: " + s.getArmazem().getNome_arm());
//            System.out.println("Saldo: " + s.getSaldo());
//
//            System.out.println("---- Preços encontrados ----");
//            for (PrecoVenda pv : s.getProduto().getPrecoVenda()) {
//                System.out.println("   ID Preço: " + pv.getId());
//                System.out.println("   Preço Normal: " + pv.getPrecoNormal());
//                System.out.println("   Preço Venda : " + pv.getPrecoVenda());
//                System.out.println("   Preço Final : " + pv.getPrecoFinal());
//                System.out.println("   Data Validade: " + pv.getDataValidade());
//                System.out.println("   Estado: " + pv.getEstado());
//                System.out.println("----------------------------");
//            }
//            System.out.println("========================================\n");
//        }
//
//        if (inventario.isEmpty()) {
//            System.out.println("⚠ Nenhum produto retornado. Verifique se há dados na tabela stock/precovenda.");
//        }

// _______________________________________________________________________________________________________  
        StockDAO stockDAO = new StockDAO();

        // Chamar o método
        ObservableList<Stock> inventario = stockDAO.listInventario();

        // Verificar se veio algo
        if (inventario == null || inventario.isEmpty()) {
            System.out.println("Nenhum item encontrado no inventário.");
        } else {
            for (Stock s : inventario) {
                System.out.println("=== Produto ===");
                System.out.println("ID Produto: " + s.getProduto().getId());
                System.out.println("Nome Produto: " + s.getProduto().getNome());
                System.out.println("Código de Barra: " + s.getProduto().getCodigo_barra());
                System.out.println("Descrição: " + s.getProduto().getDescricao());
                System.out.println("Controle Stock: " + s.getProduto().isControle_stock());
                System.out.println("Nível Stock: " + s.getProduto().getNivelstock());
                
                System.out.println("=== Preco de Venda ===");
                System.out.println("Preco Normal: "+s.getPrecoVenda().getPrecoNormal());
                System.out.println("Preco Venda: "+s.getPrecoVenda().getPrecoVenda());
                System.out.println("Preco Final: "+s.getPrecoVenda().getPrecoFinal());

                System.out.println("=== Armazém ===");
                System.out.println("ID Armazém: " + s.getArmazem().getId());
                System.out.println("Nome Armazém: " + s.getArmazem().getNome_arm());
                System.out.println("Tipo Armazém: " + s.getArmazem().getTipo());

                System.out.println("=== Categoria ===");
                System.out.println("ID Categoria: " + s.getProduto().getCategoria().getId());
                System.out.println("Família: " + s.getProduto().getCategoria().getFamilia());

                System.out.println("=== Unidade de Medida ===");
                System.out.println("ID Unidade: " + s.getProduto().getUnidadeMedida().getId());
                System.out.println("Sigla: " + s.getProduto().getUnidadeMedida().getSigla());

                System.out.println("=== Imposto ===");
                System.out.println("ID Imposto: " + s.getProduto().getImposto().getId());
                System.out.println("Nome Imposto: " + s.getProduto().getImposto().getNome());

                System.out.println("=== Stock ===");
                System.out.println("Saldo: " + s.getSaldo());
                System.out.println("----------------------------------");
            }
        }

    }
}
