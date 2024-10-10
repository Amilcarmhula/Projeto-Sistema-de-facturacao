/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.categoria.Categoria;
import sysfact.util.Conexao;

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
    public void addEntity(Categoria t) {
        try {
            sql = "insert into categoria (tipo, grupo, subgrupo, familia,"
                    + " descricao) value (?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getTipo());
            ps.setString(2, t.getGrupo());
            ps.setString(3, t.getSubgrupo());
            ps.setString(4, t.getFamilia());
            ps.setString(5, t.getDescricao());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Categoria: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(Categoria t) {
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

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar categoria: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(null, "Erro ao Buscar categoria: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public void deleteEntityByUSER(Categoria x) {
        try {
            sql = "delete from categoria where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar categoria: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
