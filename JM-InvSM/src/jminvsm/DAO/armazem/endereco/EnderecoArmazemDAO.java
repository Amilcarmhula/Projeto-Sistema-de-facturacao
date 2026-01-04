/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.armazem.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.endereco.EnderecoArmazem;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoArmazemDAO implements EnderecoArmazemDAOImpl<EnderecoArmazem> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(EnderecoArmazem t) {
        boolean isSuccess = false;
        try {
            sql = "insert into endereco_armazem (provincia,cidade,bairro,avenida,"
                    + "rua,codigo_postal,numero, idArmazem)"
                    + " value (?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_arm());
            ps.setString(2, t.getCidade_arm());
            ps.setString(3, t.getBairro_arm());
            ps.setString(4, t.getAvenida_arm());
            ps.setString(5, t.getRua_arm());
            ps.setInt(6, t.getCodigoPostal_arm());
            ps.setInt(7, t.getNumero_arm());
            ps.setInt(8, t.getArmazem().getId());

            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de persistencia!", "Detalhes: " + ex);
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
    public boolean updateEntityByID(EnderecoArmazem t, int id) {
        boolean isSuccess = false;
        try {
            sql = "update endereco_armazem set provincia=?,cidade=?,bairro=?,avenida=?,"
                    + "rua=?,codigo_postal=?,numero=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_arm());
            ps.setString(2, t.getCidade_arm());
            ps.setString(3, t.getBairro_arm());
            ps.setString(4, t.getAvenida_arm());
            ps.setString(5, t.getRua_arm());
            ps.setInt(6, t.getCodigoPostal_arm());
            ps.setInt(7, t.getNumero_arm());
            ps.setInt(8, id);

            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro de persistencia!", "Detalhes: " + ex);
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
    public ObservableList<EnderecoArmazem> listAllEntities() {
        ObservableList<EnderecoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_armazem";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoArmazem c = new EnderecoArmazem();
                c.setId(result.getInt("id"));
                c.setProvincia_arm(result.getString("provincia"));
                c.setCidade_arm(result.getString("cidade"));
                c.setBairro_arm(result.getString("bairro"));
                c.setAvenida_arm(result.getString("avenida"));
                c.setRua_arm(result.getString("rua"));
                c.setCodigoPostal_arm(result.getInt("codigo_postal"));
                c.setNumero_arm(result.getInt("numero"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog("Erro de consulta!", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public ObservableList<EnderecoArmazem> searchEntityByCLiente(int idCliente) {
        ObservableList<EnderecoArmazem> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from endereco_armazem where idArmazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoArmazem c = new EnderecoArmazem();
                c.setId(result.getInt("id"));
                c.setProvincia_arm(result.getString("provincia"));
                c.setCidade_arm(result.getString("cidade"));
                c.setBairro_arm(result.getString("bairro"));
                c.setAvenida_arm(result.getString("avenida"));
                c.setRua_arm(result.getString("rua"));
                c.setCodigoPostal_arm(result.getInt("codigo_postal"));
                c.setNumero_arm(result.getInt("numero"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog("Erro de consulta!", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public boolean deleteEntity(int id) {
        boolean isSuccess = false;
        try {
            sql = "delete from endereco_armazem where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
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
}
