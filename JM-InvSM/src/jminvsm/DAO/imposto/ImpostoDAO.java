/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.imposto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.imposto.Imposto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ImpostoDAO  implements ImpostoDAOImpl<Imposto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ImpostoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
   @Override
    public boolean addEntity(Imposto t) {
        boolean isSuccess = false;
        try {
            sql = "insert into imposto (nome, percentagem, descricao)"
                    + " value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNome());
            ps.setDouble(2, t.getPercentagem());
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
    public boolean updateEntityByID(Imposto  t, int id) {
                boolean isSuccess = false;
        try {
            sql = "update imposto set nome=?, percentagem=?, descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setDouble(2, t.getPercentagem());
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
    public ObservableList<Imposto> listAllEntities() {
        ObservableList<Imposto> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from imposto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Imposto ti = new Imposto();
                ti.setId(result.getInt("id"));
                ti.setNome(result.getString("nome"));
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
    public Imposto getEntityByID(int id) {
        Imposto ti = null;
        try {
            sql = "select * from imposto where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                ti = new Imposto();
                ti.setId(result.getInt("id"));
                ti.setNome(result.getString("nome"));
                ti.setPercentagem(result.getDouble("percentagem"));
                ti.setDescricao(result.getString("descricao"));
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
            sql =  "delete from imposto where id=?";
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
