/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.categoria;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.categoria.Categoria;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class CategoriaDAO implements CategoriaDAOImpl<Categoria> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public CategoriaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Categoria t) {
        boolean isSuccess = false;
        try {
            sql = "insert into categoria (tipo, grupo, subgrupo, familia,"
                    + " descricao) value (?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getTipo());
            ps.setString(2, t.getGrupo());
            ps.setString(3, t.getSubgrupo());
            ps.setString(4, t.getFamilia());
            ps.setString(5, t.getDescricao());

            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro persistencia", "Detalhes: " + ex);
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
    public boolean updateEntityByID(Categoria t) {
        boolean isSuccess = false;
        try {
            sql = "update categoria set tipo=?, grupo=?, subgrupo=?, familia=?,"
                    + " descricao=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getTipo());
            ps.setString(2, t.getGrupo());
            ps.setString(3, t.getSubgrupo());
            ps.setString(4, t.getFamilia());
            ps.setString(5, t.getDescricao());
            ps.setInt(6, t.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro persistencia", "Detalhes: " + ex);
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
    public ObservableList<Categoria> listAllEntities() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from categoria";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Categoria c = new Categoria();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setGrupo(result.getString("grupo"));
                c.setSubgrupo(result.getString("subgrupo"));
                c.setFamilia(result.getString("familia"));
                c.setDescricao(result.getString("descricao"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        try {
            sql = "delete from categoria where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro de exclusao", "Detalhes: " + ex);
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
