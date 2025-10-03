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

    private final Connection con;
    private PreparedStatement ps;
    private String sql;

    public DescontoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Desconto t) {
        boolean isSuccess = false;
        try {
            sql = "insert into desconto (nome,descricao)"
                    + " value (?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia: banco de dados", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                showDialog("Erro persistencia: banco de dados", "Detalhes: " + e);
            }
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntity(Desconto t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update desconto set nome=?,descricao=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setInt(3, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                showDialog("Erro de persistencia: banco de dados", "Detalhes: " + e);
            }
        }
        return isSuccess;
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
                c.setDescricao(result.getString("descricao"));
                c.setData_criacao(result.getString("data_criacao"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog("Erro de consulta: banco de dados", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        try {
            sql =  "delete from desconto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de exclusao: banco de dados", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                showDialog("Erro de exclusao: banco de dados", "Detalhes: " + e);
            }
        }
        return isSuccess;
    }
    
    public static void main(String[] args) throws SQLException {
        DescontoDAO dao = new DescontoDAO();
        
        for(Desconto d : dao.listAllEntities()){
            System.out.println("Nome: "+d.getNome());
            System.out.println("Descricao: "+d.getDescricao());
        }
        
        
    }
}
