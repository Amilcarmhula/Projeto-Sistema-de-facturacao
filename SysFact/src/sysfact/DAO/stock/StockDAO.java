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
import sysfact.util.MetodosUteis;

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
    public ObservableList<Stock> getStock() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            sql = "select p.nome as produto, s.saldo from stock s "
                    + "LEFT JOIN armazem a ON a.id=s.fk_id_armazem=a.id "
                    + "LEFT JOIN produto p ON s.fk_id_produto=p.id";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Stock s = new Stock();
                Produto p = new Produto();
                p.setNome(result.getString("produto"));
                s.setSaldo(result.getInt("saldo"));
                s.setProduto(p);
//                Armazem arm = new Armazem();
//                arm.setId(result.getInt("fk_id_armazem"));
                lista.add(s);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            MetodosUteis.showDialog(FontAwesomeIcon.COG, "Erro", "Erro ao Buscar Produto: "+ex);
        }
        return lista;
    }
}
