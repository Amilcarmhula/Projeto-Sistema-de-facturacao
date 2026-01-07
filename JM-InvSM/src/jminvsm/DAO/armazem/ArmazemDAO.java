/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.armazem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ArmazemDAO implements ArmazemDAOImpl<Armazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Armazem c) {
        boolean isSuccess = false;
        try {
            sql = "insert into armazem (tipo,nome,descricao) value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_arm());
            ps.setString(3, c.getDescricao_arm());
            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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
    public boolean updateEntityByID(Armazem c, int id) {
        boolean isSuccess = false;
        try {
            sql = "update armazem set tipo=?,nome=?,descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_arm());
            ps.setString(3, c.getDescricao_arm());
            ps.setInt(4, id);
// Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de persistencia", "Detalhes: " + ex);
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
    public ObservableList<Armazem> listAllEntities() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT a.id, a.tipo, a.nome, a.descricao, a.estado, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM armazem a "
                    + "LEFT JOIN contacto_armazem ct ON ct.idArmazem = a.id "
                    + "GROUP BY a.id, a.tipo, a.nome, a.descricao, a.estado";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Armazem c = new Armazem();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_arm(result.getString("nome"));
                c.setDescricao_arm(result.getString("descricao"));
                c.setEstado(result.getBoolean("estado"));
                List<ContactoArmazem> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoArmazem cc = new ContactoArmazem();
                        cc.setContacto_arm(split);
                        listaContactos.add(cc);
                    }
                }

                c.setContactoArmazem(listaContactos);
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog( "Erro de consulta!", "Detalhes: " + e);

        }
        return lista;
    }

    @Override
    public Armazem getLastEntity() {
        Armazem c = null;
        try {
            sql = "SELECT id,tipo,nome,descricao FROM armazem ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Armazem();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_arm(result.getString("nome"));
                c.setDescricao_arm(result.getString("descricao"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);
        }
        return c;
    }

    @Override
    public Armazem getEntityByID(int id) {
        Armazem c = null;
        try {
            sql = "SELECT id,tipo,nome,descricao FROM armazem where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Armazem();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_arm(result.getString("nome"));
                c.setDescricao_arm(result.getString("descricao"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);
        }
        return c;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;
        try {
            sql = "delete from armazem WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de exclusao", "Detalhes: " + ex);
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
    public ObservableList<Armazem> listAllArmazens() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Armazem c = new Armazem();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_arm(result.getString("nome"));
                c.setDescricao_arm(result.getString("descricao"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);

        }
        return lista;
    }
    
    public static void main(String[] args) throws SQLException {
     ArmazemDAO dao = new ArmazemDAO();
        for (Armazem a : dao.listAllArmazens()) {
            System.out.println("Arm: "+a.getNome_arm());
        }
     
    }

}
