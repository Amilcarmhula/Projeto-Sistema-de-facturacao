/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.taxaimposto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class TaxaImpostoDAO  implements TaxaImpostoDAOImpl<TaxaImposto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public TaxaImpostoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
   @Override
    public void addEntity(TaxaImposto t) {
        try {
            sql = "insert into taxa_imposto (identificador, percentagem, descricao)"
                    + " value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getIdentificador());
            ps.setDouble(2, t.getPercentagem());
            ps.setString(3, t.getDescricao());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(TaxaImposto  t, int id) {
        try {
            sql = "update taxa_imposto set identificador=?, percentagem=?, descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getIdentificador());
            ps.setDouble(2, t.getPercentagem());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Taxa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<TaxaImposto> listAllEntities() {
        ObservableList<TaxaImposto> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from taxa_imposto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                TaxaImposto ti = new TaxaImposto();
                ti.setId(result.getInt("id"));
                ti.setIdentificador(result.getString("identificador"));
                ti.setPercentagem(result.getDouble("percentagem"));
                ti.setDescricao(result.getString("descricao"));
                lista.add(ti);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
    
    @Override
    public TaxaImposto getEntityByID(int id) {
        TaxaImposto ti = null;
        try {
            sql = "select * from taxa_imposto where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                ti = new TaxaImposto();
                ti.setId(result.getInt("id"));
                ti.setIdentificador(result.getString("identificador"));
                ti.setPercentagem(result.getDouble("percentagem"));
                ti.setDescricao(result.getString("descricao"));
                ti.setData_criacao(result.getString("data_criacao"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return ti;
    }
    
    
    @Override
    public Map<String, Integer> mapTaxaImposto() {
        Map<String, Integer> mapa = new HashMap<>();
        try {
            sql = "select * from taxa_imposto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                TaxaImposto u = new TaxaImposto();
                    mapa.put(result.getString("identificador"), result.getInt("id"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao mapear Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return mapa;
    }

   


    @Override
    public void deleteEntity(int x) {
        try {
            sql =  "delete from taxa_imposto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Taxa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
