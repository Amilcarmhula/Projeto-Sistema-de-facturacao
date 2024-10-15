/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.armazem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.armazem.Armazem;
import sysfact.model.armazem.contacto.ContactoArmazem;
import sysfact.util.Conexao;

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
    public void addEntity(Armazem c) {
        try {
            sql = "insert into armazem (tipo,nome,descricao) value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_arm());
            ps.setString(3, c.getDescricao_arm());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar armazem: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Armazem c, int id) {
        try {
            sql = "update armazem set tipo=?,nome=?,descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome_arm());
            ps.setString(3, c.getDescricao_arm());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar armazem: "
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<Armazem> listAllEntities() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT a.id, a.tipo, a.nome, a.descricao, a.estado, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM armazem a "
                    + "LEFT JOIN contacto_armazem ct ON ct.fk_id_armazem = a.id "
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
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

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
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public void deleteEntity(int x) {
        try {
            sql = "delete from armazem WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar armazem: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    
    
    
    
    @Override
    public ObservableList<Armazem> listAllEntitiesTypeArmazem() {
        ObservableList<Armazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Armazem c = new Armazem();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setNome_arm(result.getString("razaosocial"));
                c.setDescricao_arm(result.getString("descricao"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar armazem: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

   
    
}
