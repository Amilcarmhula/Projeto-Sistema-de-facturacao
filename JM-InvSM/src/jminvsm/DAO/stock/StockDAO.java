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
            sql = "SELECT p.id AS idProduto,  p.nome AS nomeProduto,p.codigo_barra,"
                    + "  p.descricao, p.controle_stock, p.nivelstock, p.tipo, p.unidadePorTipo, a.id AS idArmazem,"
                    + " a.nome AS nomeArmazem, a.tipo as tipoArmazem, c.id AS idCategoria, c.familia,"
                    + " u.id AS idUnidade, u.nome as unidadeNome, u.sigla AS siglaUnidade, i.id AS idImposto, "
                    + "    i.nome AS nomeImposto, i.percentagem as impPerc, pv.precoNormal, pv.precoVenda, "
                    + "    pv.precoFinal, pv.estado as estadoPreco, s.saldo "
                    + "FROM stock s "
                    + "JOIN produto p ON s.fk_id_produto = p.id "
                    + "JOIN armazem a ON s.fk_id_armazem = a.id "
                    + "JOIN categoria c ON p.fk_id_categoria = c.id "
                    + "JOIN unidade_medida u ON p.fk_id_unidade_medida = u.id "
                    + "JOIN imposto i ON p.fk_id_imposto = i.id "
                    + "LEFT JOIN precovenda pv ON pv.id = (SELECT pv2.id "
                    + "        FROM precovenda pv2 "
                    + "        WHERE pv2.idProduto = s.fk_id_produto "
                    + "          AND pv2.idArmazem = s.fk_id_armazem "
                    + "        ORDER BY pv2.data_criacao DESC "
                    + "        LIMIT 1)";
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
                p.setTipo(result.getString("tipo"));
                p.setUnidadesPorTipo(result.getInt("unidadePorTipo"));
                PrecoVenda pv = new PrecoVenda();
                pv.setPrecoNormal(result.getDouble("precoNormal"));
                pv.setPrecoVenda(result.getDouble("precoVenda"));
                pv.setPrecoFinal(result.getDouble("precoFinal"));
                pv.setEstado(result.getString("estadoPreco"));
                p.setPrecoVenda(pv);
                Armazem a = new Armazem();
                a.setId(result.getInt("idArmazem"));
                a.setNome_arm(result.getString("nomeArmazem"));
                a.setTipo(result.getString("tipoArmazem"));
                s.setArmazem(a);
                Categoria c = new Categoria();
                c.setId(result.getInt("idCategoria"));
                c.setFamilia(result.getString("familia"));
                p.setCategoria(c);
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("idUnidade"));
                u.setNome(result.getString("unidadeNome"));
                u.setSigla(result.getString("siglaUnidade"));
                p.setUnidade_medida(u);
                Imposto i = new Imposto();
                i.setId(result.getInt("idImposto"));
                i.setNome(result.getString("nomeImposto"));
                i.setPercentagem(result.getDouble("impPerc"));
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

                System.out.println("=== Armazém ===");
                System.out.println("ID Armazém: " + s.getArmazem().getId());
                System.out.println("Nome Armazém: " + s.getArmazem().getNome_arm());
                System.out.println("Tipo Armazém: " + s.getArmazem().getTipo());

                System.out.println("=== Categoria ===");
                System.out.println("ID Categoria: " + s.getProduto().getCategoria().getId());
                System.out.println("Família: " + s.getProduto().getCategoria().getFamilia());

                System.out.println("=== Unidade de Medida ===");
                System.out.println("ID Unidade: " + s.getProduto().getUnidade_medida().getId());
                System.out.println("Sigla: " + s.getProduto().getUnidade_medida().getSigla());

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
