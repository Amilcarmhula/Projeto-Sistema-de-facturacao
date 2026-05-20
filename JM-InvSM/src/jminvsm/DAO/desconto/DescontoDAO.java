/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.desconto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.desconto.Desconto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class DescontoDAO implements DescontoDAOImpl<Desconto> {

    @Override
    public boolean addEntity(Desconto t) {
        boolean isSuccess = false;
        String sql = "insert into desconto (nome,descricao,percentagem)"
                + " value (?,?,?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setDouble(3, t.getPercentagem());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia: banco de dados", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntity(Desconto t, int id) {
        boolean isSuccess = false;
        String sql = "update desconto set nome=?,descricao=?,percentagem=? where id=?";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setDouble(3, t.getPercentagem());

            ps.setInt(4, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Desconto> listAllEntities() {
        ObservableList<Desconto> lista = FXCollections.observableArrayList();
        Desconto c;
        String sql = "select * from desconto";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new Desconto();
                    c.setId(result.getInt("id"));
                    c.setNome(result.getString("nome"));
                    c.setPercentagem(result.getDouble("percentagem"));
                    c.setDescricao(result.getString("descricao"));
                    c.setData_criacao(result.getString("data_criacao"));
                    lista.add(c);
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta: banco de dados", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        String sql = "delete from desconto where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de exclusao: banco de dados", "Detalhes: " + ex);
        }
        return isSuccess;
    }
}
