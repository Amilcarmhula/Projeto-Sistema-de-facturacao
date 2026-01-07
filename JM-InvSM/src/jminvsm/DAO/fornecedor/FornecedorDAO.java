/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class FornecedorDAO implements FornecedorDAOImpl<Fornecedor> {

    private final Connection con;
    private PreparedStatement ps;
    private String sql;

    public FornecedorDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Fornecedor c) {
        boolean isSuccess = false;

        try {
            sql = "insert into fornecedor (tipo,razaosocial,nuit) value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTipo_forn());
            ps.setString(2, c.getRazaosocial_forn());
            ps.setInt(3, c.getNuit_forn());

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
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(Fornecedor c, int id) {
        boolean isSuccess = false;

        try {
            sql = "update fornecedor set tipo=?, razaosocial=? ,nuit=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo_forn());
            ps.setString(2, c.getRazaosocial_forn());
            ps.setInt(3, c.getNuit_forn());
            ps.setInt(4, id);

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
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Fornecedor> listAllEntities() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT f.id,f.tipo,f.razaosocial,f.nuit, "
                    + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos "
                    + "FROM fornecedor f "
                    + "LEFT JOIN contacto_fornecedor ct ON ct.idFornecedor = f.id "
                    + "GROUP BY f.id, f.tipo, f.razaosocial, f.nuit;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Fornecedor c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo_forn(result.getString("tipo"));
                c.setRazaosocial_forn(result.getString("razaosocial"));
                c.setNuit_forn(result.getInt("nuit"));
                List<ContactoFornecedor> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoFornecedor cc = new ContactoFornecedor();
                        cc.setContacto_forn(split);
                        listaContactos.add(cc);
                    }
                }

                c.setContactoFornecedor(listaContactos);
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

    @Override
    public Fornecedor getLastEntity() {
        Fornecedor c = null;
        try {
            sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo_forn(result.getString("tipo"));
                c.setRazaosocial_forn(result.getString("razaosocial"));
                c.setNuit_forn(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }

    @Override
    public Fornecedor getEntityByID(int id) {
        Fornecedor c = null;
        try {
            sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo_forn(result.getString("tipo"));
                c.setRazaosocial_forn(result.getString("razaosocial"));
                c.setNuit_forn(result.getInt("nuit"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
    
    @Override
    public ObservableList<Fornecedor> listAllFornecedores() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from fornecedor";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Fornecedor c = new Fornecedor();
                c.setId(result.getInt("id"));
                c.setTipo_forn(result.getString("tipo"));
                c.setRazaosocial_forn(result.getString("razaosocial"));
                c.setNuit_forn(result.getInt("nuit"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);

        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int x) {
        boolean isSuccess = false;

        try {
            sql = "delete from fornecedor WHERE id = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x);
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
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public int CountFornecedor() {
        int total = 0;
        try {
            sql = "select count(*) as total from fornecedor";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            if (result.next()) { // Verifica se há resultados
                total = result.getInt("total");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return total;
    }

}
