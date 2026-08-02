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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class FornecedorDAO implements FornecedorDAOImpl<Fornecedor> {

    @Override
    public boolean addEntity(Fornecedor c) {
        boolean isSuccess = false;
        String sql = "insert into fornecedor (tipo,razaosocial,nuit) value (?,?,?)";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTipo_forn());
            ps.setString(2, c.getRazaosocial_forn());
            ps.setInt(3, c.getNuit_forn());

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
    public boolean updateEntityByID(Fornecedor c, int id) {
        boolean isSuccess = false;
        String sql = "update fornecedor set tipo=?, razaosocial=? ,nuit=?"
                + " where id=? AND deleteStatus=0";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {

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
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Fornecedor> listAllEntities() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        String sql = "SELECT f.id,f.tipo,f.razaosocial,f.nuit, "
                + "GROUP_CONCAT(CONCAT(ct.contacto) SEPARATOR ' ') AS contatos, "
                + "GROUP_CONCAT(DISTINCT CONCAT(e.id, ';', e.avenida, ';',e.rua, ';',e.numero, ';',e.bairro, ';',e.cidade, ';',e.provincia, ';',e.codigo_postal, '.' )) AS address "
                + "FROM fornecedor f "
                + "LEFT JOIN contacto_fornecedor ct ON ct.idFornecedor = f.id AND ct.deleteStatus = 0 "
                + "LEFT JOIN endereco_fornecedor e ON e.idFornecedor = f.id AND e.deleteStatus = 0 "
                + "WHERE f.deleteStatus = 0 "
                + "GROUP BY f.id, f.tipo, f.razaosocial, f.nuit;";

        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            try ( ResultSet result = ps.executeQuery()) {
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

                    String enderecos = result.getString("address");
                    EnderecoFornecedor end;
                    if (enderecos != null && !enderecos.trim().isEmpty()) {
                        for (String splited : enderecos.split("\\.")) {
//                            System.out.println(splited);
                            String[] split = splited.split(";");
                            end = new EnderecoFornecedor();
                            end.setId(Integer.parseInt(split[0]));
                            end.setAvenida_forn(split[1]);
                            end.setRua_forn(split[2]);
                            end.setNumero_forn(Integer.parseInt(split[3]));
                            end.setBairro_forn(split[4]);
                            end.setCidade_forn(split[5]);
                            end.setProvincia_forn(split[6]);
                            end.setCodigoPostal_forn(Integer.parseInt(split[7]));
                            c.setEnderecoFornecedor(end);
                        }
                    }
//                    c.setEnderecoFornecedor(end);
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

    @Override
    public Fornecedor getLastEntity() {
        Fornecedor c = null;
        String sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor WHERE deleteStatus=0 ORDER BY id DESC LIMIT 1;";

        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            try ( ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new Fornecedor();
                    c.setId(result.getInt("id"));
                    c.setTipo_forn(result.getString("tipo"));
                    c.setRazaosocial_forn(result.getString("razaosocial"));
                    c.setNuit_forn(result.getInt("nuit"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }

    @Override
    public Fornecedor getEntityByID(int id) {
        Fornecedor c = null;
        String sql = "SELECT id,tipo,razaosocial,nuit FROM fornecedor where id=? AND deleteStatus=0";

        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new Fornecedor();
                    c.setId(result.getInt("id"));
                    c.setTipo_forn(result.getString("tipo"));
                    c.setRazaosocial_forn(result.getString("razaosocial"));
                    c.setNuit_forn(result.getInt("nuit"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar fornecedor: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }

    @Override
    public ObservableList<Fornecedor> listAllFornecedores() {
        ObservableList<Fornecedor> lista = FXCollections.observableArrayList();
        String sql = "select * from fornecedor WHERE deleteStatus=0";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {

            try ( ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Fornecedor c = new Fornecedor();
                    c.setId(result.getInt("id"));
                    c.setTipo_forn(result.getString("tipo"));
                    c.setRazaosocial_forn(result.getString("razaosocial"));
                    c.setNuit_forn(result.getInt("nuit"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);

        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int x) {
        try {
            return jminvsm.util.SoftDeleteUtils.softDelete("fornecedor", x);

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return false;
    }

    @Override
    public int CountFornecedor() {
        int total = 0;
        String sql = "select count(*) as total from fornecedor WHERE deleteStatus=0";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {

            try ( ResultSet result = ps.executeQuery()) {
                if (result.next()) { // Verifica se há resultados
                    total = result.getInt("total");
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de Contagem de Fornecedores!", "Detalhes: " + ex);
        }

        return total;
    }
    public static void main(String[] args) {
        FornecedorDAO dao = new FornecedorDAO();
        dao.listAllEntities();
    }

}
