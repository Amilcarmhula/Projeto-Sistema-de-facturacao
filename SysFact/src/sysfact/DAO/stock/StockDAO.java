/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.stock;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sysfact.model.armazem.Armazem;
import sysfact.model.produto.Produto;
import sysfact.model.stock.Stock;
import sysfact.util.Conexao;
import sysfact.util.LoadAndMoveUtilities;
import sysfact.util.AlertUtilities;
import static sysfact.util.AlertUtilities.showDialog;

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
                p.setNomeProduto(result.getString("produto"));
                s.setSaldo(result.getInt("saldo"));
                s.setProduto(p);
                lista.add(s);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            AlertUtilities.showDialog(FontAwesomeIcon.COG, "Erro", "Erro ao Buscar Produto: " + ex);
        }
        return lista;
    }

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
            showDialog(FontAwesomeIcon.TIMES, "Erro de contagem", "Detalhes: " + ex);
        }

        return total;
    }
}
