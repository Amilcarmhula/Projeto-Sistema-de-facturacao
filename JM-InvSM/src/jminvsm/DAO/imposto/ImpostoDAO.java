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
public class ImpostoDAO implements ImpostoDAOImpl<Imposto> {

    @Override
    public boolean addEntity(Imposto t) {
        boolean isSuccess = false;
        String sql = "insert into imposto (nome, percentagem, descricao)"
                + " values (?,?,?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setDouble(2, t.getPercentagem());
            ps.setString(3, t.getDescricao());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(Imposto t, int id) {
        boolean isSuccess = false;
        String sql = "update imposto set nome=?, percentagem=?, descricao=?"
                + " where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

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
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Imposto> listAllEntities() {
        ObservableList<Imposto> lista = FXCollections.observableArrayList();
        String sql = "select * from imposto";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Imposto ti = new Imposto();
                    ti.setId(result.getInt("id"));
                    ti.setNome(result.getString("nome"));
                    ti.setPercentagem(result.getDouble("percentagem"));
                    ti.setDescricao(result.getString("descricao"));
                    lista.add(ti);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Imposto getEntityByID(int id) {
        Imposto ti = null;
        String sql = "select * from imposto where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                ps.setInt(1, id);
                while (result.next()) {
                    ti = new Imposto();
                    ti.setId(result.getInt("id"));
                    ti.setNome(result.getString("nome"));
                    ti.setPercentagem(result.getDouble("percentagem"));
                    ti.setDescricao(result.getString("descricao"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return ti;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        String sql = "delete from imposto where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }
}
