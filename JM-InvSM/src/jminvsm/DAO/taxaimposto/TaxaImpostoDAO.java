/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.taxaimposto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.taxa_imposto.TaxaImposto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

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
    public boolean addEntity(TaxaImposto t) {
        boolean isSuccess = false;
        try {
            sql = "insert into taxa_imposto (identificador, percentagem, descricao)"
                    + " value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNomeImposto());
            ps.setDouble(2, t.getPercentagemImposto());
            ps.setString(3, t.getDescricao());

           int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(TaxaImposto  t, int id) {
                boolean isSuccess = false;
        try {
            sql = "update taxa_imposto set identificador=?, percentagem=?, descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNomeImposto());
            ps.setDouble(2, t.getPercentagemImposto());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
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
                ti.setNomeImposto(result.getString("identificador"));
                ti.setPercentagemImposto(result.getDouble("percentagem"));
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
                ti.setNomeImposto(result.getString("identificador"));
                ti.setPercentagemImposto(result.getDouble("percentagem"));
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
    public boolean deleteEntity(int x) {
                boolean isSuccess = false;
        try {
            sql =  "delete from taxa_imposto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
