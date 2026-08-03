/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.fornecedor.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.util.Conexao;


/**
 *
 * @author JM-Tecnologias
 */
//public class EnderecoFornecedorDAO implements EnderecoFornecedorDAOImpl<EnderecoFornecedor> {
//
//    private final Connection con;
//    private PreparedStatement ps;
//    private String sql;
//
//    public EnderecoFornecedorDAO() throws SQLException {
//        this.con = Conexao.getConexao();
//    }
//
//    @Override
//    public boolean addEntity(EnderecoFornecedor t) {
//        boolean isSuccess = false;
//        try {
//            sql = "insert into endereco_fornecedor (provincia,cidade,bairro,avenida,"
//                    + "rua,codigo_postal,numero, idFornecedor)"
//                    + " value (?,?,?,?,?,?,?,?)";
//
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getProvincia_forn());
//            ps.setString(2, t.getCidade_forn());
//            ps.setString(3, t.getBairro_forn());
//            ps.setString(4, t.getAvenida_forn());
//            ps.setString(5, t.getRua_forn());
//            ps.setInt(6, t.getCodigoPostal_forn());
//            ps.setInt(7, t.getNumero_forn());
//            ps.setInt(8, t.getFornecedor().getId());
//
//            int rowsAffected = ps.executeUpdate();
//
//            if (rowsAffected > 0) {
//                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
//            }
//
//        } catch (SQLException ex) {
//            DaoErrorHandler.log("Erro persistencia", "Detalhes: " + ex);
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return isSuccess;
//    }
//
//    @Override
//    public boolean updateEntityByID(EnderecoFornecedor t, int id) {
//        boolean isSuccess = false;
//        try {
//            sql = "update endereco_fornecedor set provincia=?,cidade=?,bairro=?,avenida=?,"
//                    + "rua=?,codigo_postal=?,numero=? where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setString(1, t.getProvincia_forn());
//            ps.setString(2, t.getCidade_forn());
//            ps.setString(3, t.getBairro_forn());
//            ps.setString(4, t.getAvenida_forn());
//            ps.setString(5, t.getRua_forn());
//            ps.setInt(6, t.getCodigoPostal_forn());
//            ps.setInt(7, t.getNumero_forn());
//            ps.setInt(8, id);
//
//            int rowsAffected = ps.executeUpdate();
//
//            if (rowsAffected > 0) {
//                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
//            }
//
//        } catch (SQLException ex) {
//            DaoErrorHandler.log("Erro persistencia", "Detalhes: " + ex);
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return isSuccess;
//    }
//
//    @Override
//    public ObservableList<EnderecoFornecedor> listAllEntities() {
//        ObservableList<EnderecoFornecedor> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from endereco_fornecedor";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                EnderecoFornecedor c = new EnderecoFornecedor();
//                c.setId(result.getInt("id"));
//                c.setProvincia_forn(result.getString("provincia"));
//                c.setCidade_forn(result.getString("cidade"));
//                c.setBairro_forn(result.getString("bairro"));
//                c.setAvenida_forn(result.getString("avenida"));
//                c.setRua_forn(result.getString("rua"));
//                c.setCodigoPostal_forn(result.getInt("codigo_postal"));
//                c.setNumero_forn(result.getInt("numero"));
//                lista.add(c);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            DaoErrorHandler.log("Informação", "Erro ao Buscar Endereco: " + ex);
//        }
//        return lista;
//    }
//    @Override
//    public ObservableList<EnderecoFornecedor> searchEntityByCLiente(int id) {
//        ObservableList<EnderecoFornecedor> lista = FXCollections.observableArrayList();
//        try {
//            sql = "select * from endereco_fornecedor where idFornecedor=?";
//            ps = con.prepareStatement(sql);
//            ps.setInt(1,id);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                EnderecoFornecedor c = new EnderecoFornecedor();
//                c.setId(result.getInt("id"));
//                c.setProvincia_forn(result.getString("provincia"));
//                c.setCidade_forn(result.getString("cidade"));
//                c.setBairro_forn(result.getString("bairro"));
//                c.setAvenida_forn(result.getString("avenida"));
//                c.setRua_forn(result.getString("rua"));
//                c.setCodigoPostal_forn(result.getInt("codigo_postal"));
//                c.setNumero_forn(result.getInt("numero"));
//                lista.add(c);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            DaoErrorHandler.log("Informação", "Erro ao Buscar Endereco: " + ex);
//        }
//        return lista;
//    }
//    
//     @Override
//    public boolean deleteEntity(int id) {
//        boolean isSuccess = false;
//        try {
//            sql = "delete from endereco_fornecedor where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            int rowsAffected = ps.executeUpdate();
//
//            if (rowsAffected > 0) {
//                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
//            }
//
//        } catch (SQLException ex) {
//            DaoErrorHandler.log("Erro persistencia", "Detalhes: " + ex);
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return isSuccess;
//    }
//}


public class EnderecoFornecedorDAO implements EnderecoFornecedorDAOImpl<EnderecoFornecedor> {

    @Override
    public boolean addEntity(EnderecoFornecedor c) {
        boolean isSuccess = false;

        String sql = "insert into endereco_fornecedor "
                + "(provincia,cidade,bairro,avenida,rua,codigo_postal,numero,idFornecedor) "
                + "value (?,?,?,?,?,?,?,?)";

        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getProvincia_forn());
            ps.setString(2, c.getCidade_forn());
            ps.setString(3, c.getBairro_forn());
            ps.setString(4, c.getAvenida_forn());
            ps.setString(5, c.getRua_forn());
            ps.setInt(6, c.getCodigoPostal_forn());
            ps.setInt(7, c.getNumero_forn());
            ps.setInt(8, c.getFornecedor().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia EnderecoFornecedor", "Detalhes: " + ex);
        }

        return isSuccess;
    }

    @Override
    public boolean updateEntityByID(EnderecoFornecedor c, int id) {
        boolean isSuccess = false;

        String sql = "update endereco_fornecedor set "
                + "provincia=?,cidade=?,bairro=?,avenida=?,rua=?,"
                + "codigo_postal=?,numero=? where id=? AND deleteStatus=0";

        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getProvincia_forn());
            ps.setString(2, c.getCidade_forn());
            ps.setString(3, c.getBairro_forn());
            ps.setString(4, c.getAvenida_forn());
            ps.setString(5, c.getRua_forn());
            ps.setInt(6, c.getCodigoPostal_forn());
            ps.setInt(7, c.getNumero_forn());
            ps.setInt(8, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro actualizacao EnderecoFornecedor", "Detalhes: " + ex);
        }

        return isSuccess;
    }

    @Override
    public ObservableList<EnderecoFornecedor> listAllEntities() {

        ObservableList<EnderecoFornecedor> lista =
                FXCollections.observableArrayList();

        String sql = "select * from endereco_fornecedor WHERE deleteStatus=0";

        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet result = ps.executeQuery()) {

            while (result.next()) {

                EnderecoFornecedor c = new EnderecoFornecedor();

                c.setId(result.getInt("id"));
                c.setProvincia_forn(result.getString("provincia"));
                c.setCidade_forn(result.getString("cidade"));
                c.setBairro_forn(result.getString("bairro"));
                c.setAvenida_forn(result.getString("avenida"));
                c.setRua_forn(result.getString("rua"));
                c.setCodigoPostal_forn(result.getInt("codigo_postal"));
                c.setNumero_forn(result.getInt("numero"));

                lista.add(c);
            }

        } catch (SQLException e) {
            DaoErrorHandler.log("Erro de listagem EnderecoFornecedor!", "Detalhes: " + e);
        }

        return lista;
    }

    @Override
    public ObservableList<EnderecoFornecedor> searchEntityByCLiente(int id) {

        ObservableList<EnderecoFornecedor> lista =
                FXCollections.observableArrayList();

        String sql = "select * from endereco_fornecedor where idFornecedor=? AND deleteStatus=0";

        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet result = ps.executeQuery()) {

                while (result.next()) {

                    EnderecoFornecedor c = new EnderecoFornecedor();

                    c.setId(result.getInt("id"));
                    c.setProvincia_forn(result.getString("provincia"));
                    c.setCidade_forn(result.getString("cidade"));
                    c.setBairro_forn(result.getString("bairro"));
                    c.setAvenida_forn(result.getString("avenida"));
                    c.setRua_forn(result.getString("rua"));
                    c.setCodigoPostal_forn(result.getInt("codigo_postal"));
                    c.setNumero_forn(result.getInt("numero"));

                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            DaoErrorHandler.log("Erro de consulta EnderecoFornecedor!", "Detalhes: " + e);
        }

        return lista;
    }

    @Override
    public boolean deleteEntity(int id) {

        try {
            return jminvsm.util.SoftDeleteUtils.softDelete("endereco_fornecedor", id);
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro exclusao EnderecoFornecedor", "Detalhes: " + ex);
        }

        return false;
    }
}
