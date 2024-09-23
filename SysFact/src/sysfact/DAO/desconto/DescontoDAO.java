/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.desconto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.desconto.Desconto;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class DescontoDAO   implements DescontoDAOImpl<Desconto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public DescontoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    
        @Override
    public ObservableList<Desconto> listAllEntities() {
        ObservableList<Desconto> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from desconto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Desconto c = new Desconto();
                c.setId(result.getInt("id"));
                c.setNome(result.getString("nome"));
                c.setPercentagem(result.getDouble("percentagem"));
                c.setDescricao(result.getString("descricao"));
                c.setData_criacao(result.getString("data_criacao"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Desconto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
    
//    @Override
//    public Map<String, Double> mapDesconto() {
//        Map<String, Double> mapa = new HashMap<>();
//        try {
//            sql = "select * from desconto";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Desconto d = new Desconto();
//                mapa.put(result.getString("sigla"), result.getInt("id"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao mapear unidade de medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return mapa;
//    }
//    
//    @Override
//    public void addEntity(Desconto t) {
//       try {
//            sql = "insert into desconto (nome,percentagem,valor_fixo,descricao)"
//                    + " value (?,?,?,?)";
//        
//            ps = con.prepareStatement(sql);
//        
//            ps.setString(1, t.getNome());
//            ps.setDouble(2, t.getPercentagem());
//            ps.setDouble(3, t.getValorFixo());
//            ps.setString(4, t.getDescricao());
//
//            ps.execute();
//            ps.close();
//            } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao adicionar desconto: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        
//    }
//
//    @Override
//    public void updateEntityByID(Desconto t, int id) {
//        try {
//            sql = "update armazem set nome=?,percentagem=?,valor_fixo=?,descricao=? where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getNome());
//            ps.setDouble(2, t.getPercentagem());
//            ps.setDouble(3, t.getValorFixo());
//            ps.setString(4, t.getDescricao());
//            ps.setInt(5, id);
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar desconto: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//

//
//    @Override
//    public Desconto searchEntityByName(String name) {
//        Desconto c = null;
//        try {
//            sql = "select * from desconto where nome like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + name + "%");
//            ResultSet result = ps.executeQuery();
////            if (result.next()) {
//            while (result.next()) {
//                c = new Desconto();
//                c.setId(result.getInt("id"));
//                c.setNome(result.getString("nome"));
//                c.setPercentagem(result.getDouble("percentagem"));
//                c.setValorFixo(result.getDouble("valor_fixo"));
//                c.setDescricao(result.getString("descricao"));
//                c.setData_criacao(result.getString("data_criacao"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
//                
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Armazem: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return c;
//
//    }
//
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from desconto where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    @Override
//    public void deleteEntityByUSER(int id) {
//        try {
//            sql =  "update desconto set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
