/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.produto.fornecido;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.util.Conexao;
import jminvsm.util.AlertUtilities;

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

    public Map<String, Integer> getMovimentos(Integer cat, Integer arm) {
        Map<String, Integer> mapa = new HashMap<>();
        try {
            sql = "SELECT "
                    + "SUM(CASE WHEN mp.tipo_movimento = 'Entrada' THEN mp.quantidade ELSE 0 END) as entradas,"
                    + "SUM(CASE WHEN mp.tipo_movimento = 'Saída' THEN mp.quantidade ELSE 0 END) AS saidas,"
                    + "MONTH(mp.data_movimento) AS mes, "
                    + "YEAR(mp.data_movimento) AS ano "
                    + "FROM movimento_produto mp "
                    + "LEFT JOIN produto p ON p.id = mp.fk_id_produto "
                    + "LEFT JOIN categoria c ON c.id = p.fk_id_categoria "
                    + "LEFT JOIN armazem a ON a.id = mp.fk_id_armazem "
                    + "WHERE (? IS NULL OR c.id = ?) "
                    + "AND (? IS NULL OR a.id = ?) "
                    + "AND MONTH(mp.data_movimento)=MONTH(current_date()) "
                    + "AND YEAR(mp.data_movimento)=YEAR(current_date()) "
                    + "GROUP BY mes, ano";
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
                mapa.put("entradas", result.getInt("entradas"));
                mapa.put("saidas", result.getInt("saidas"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro: "+ex);
//            AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro Dados", "Erro ao carregar movimentos de produto: " + ex);
        }
        return mapa;
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
