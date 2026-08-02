
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

    @Override
    public boolean addEntity(Armazem c) {
        boolean isSuccess = false;
        String sql = "insert into armazem (tipo,nome,descricao) value (?,?,?)";

        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_arm());
            ps.setString(3, c.getDescricao_arm());
            // Usando executeUpdate para verificar se uma linha foi inserida
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
    public boolean updateEntityByID(Armazem c, int id) {
        boolean isSuccess = false;
        String sql = "update armazem set tipo=?,nome=?,descricao=?"
                + " where id=? AND deleteStatus=0";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

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
            showDialog("Erro actualizacao de Armazem", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Armazem> listAllEntities() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        String sql = "SELECT a.id, a.tipo, a.nome, a.descricao, a.estado, "
                + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                + "FROM armazem a "
                + "LEFT JOIN contacto_armazem ct ON ct.idArmazem = a.id AND ct.deleteStatus = 0 "
                + "WHERE a.deleteStatus = 0 "
                + "GROUP BY a.id, a.tipo, a.nome, a.descricao, a.estado";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            showDialog("Erro de listagem de Armazem!", "Detalhes: " + e);

        }
        return lista;
    }

    @Override
    public Armazem getLastEntity() {
        Armazem c = null;
        String sql = "SELECT id,tipo,nome,descricao FROM armazem WHERE deleteStatus=0 ORDER BY id DESC LIMIT 1;";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new Armazem();
                    c.setId(result.getInt("id"));
                    c.setTipo(result.getString("tipo"));
                    c.setNome_arm(result.getString("nome"));
                    c.setDescricao_arm(result.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            showDialog("Erro de consulta de Armazem!", "Detalhes: " + e);
        }
        return c;
    }

    @Override
    public Armazem getEntityByID(int id) {
        Armazem c = null;
        String sql = "SELECT id,tipo,nome,descricao FROM armazem where id=? AND deleteStatus=0";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new Armazem();
                    c.setId(result.getInt("id"));
                    c.setTipo(result.getString("tipo"));
                    c.setNome_arm(result.getString("nome"));
                    c.setDescricao_arm(result.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            showDialog("Erro de consulta de Armazem!", "Detalhes: " + e);
        }
        return c;
    }

    @Override
    public boolean deleteEntity(int x) {
        try {
            return jminvsm.util.SoftDeleteUtils.softDelete("armazem", x);
        } catch (SQLException ex) {
            showDialog("Erro de exclusao de Armazem", "Detalhes: " + ex);
        }
        return false;
    }

    @Override
    public ObservableList<Armazem> listAllArmazens() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        String sql = "select * from armazem WHERE deleteStatus=0";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Armazem c = new Armazem();
                    c.setId(result.getInt("id"));
                    c.setTipo(result.getString("tipo"));
                    c.setNome_arm(result.getString("nome"));
                    c.setDescricao_arm(result.getString("descricao"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            showDialog("Erro de consulta de Armazem!", "Detalhes: " + e);

        }
        return lista;
    }


}
