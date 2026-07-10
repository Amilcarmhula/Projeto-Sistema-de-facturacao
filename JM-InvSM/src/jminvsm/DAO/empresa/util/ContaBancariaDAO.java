/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.empresa.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jminvsm.model.empresa.conta.ContaBancaria;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaBancariaDAO implements DaoImpl<ContaBancaria> {

    @Override
    public void addEntity(ContaBancaria t) {
        String sql = "insert into contabancaria (nome_banco,numero,nib,nuib,"
                + "fk_id_empresa) value (?,?,?,?,?)";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNome_banco());
            ps.setString(2, t.getNumero_conta());
            ps.setString(3, t.getNib());
            ps.setString(4, t.getNuib());
            if (t.getEmpresa() != null) {
                ps.setInt(5, t.getEmpresa().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }

    }

    @Override
    public void updateEntityByID(ContaBancaria t, int id) {
        String sql = "update contabancaria set nome_banco=?,numero=?,nib=?,nuib=?,"
                + "fk_id_empresa=? where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNome_banco());
            ps.setString(2, t.getNumero_conta());
            ps.setString(3, t.getNib());
            ps.setString(4, t.getNuib());
            if (t.getEmpresa() != null) {
                ps.setInt(5, t.getEmpresa().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
    }

    @Override
    public List<ContaBancaria> listAllEntities() {
        List<ContaBancaria> lista = new ArrayList<>();
        String sql = "select * from contabancaria";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    ContaBancaria c = new ContaBancaria();
                    c.setId(result.getInt("id"));
                    c.setNome_banco(result.getString("nome_banco"));
                    c.setNumero_conta(result.getString("numero"));
                    c.setNib(result.getString("nib"));
                    c.setNuib(result.getString("nuib"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                    c.setData_criacao(result.getString("data_criacao"));
                    lista.add(c);
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public ContaBancaria searchEntityByName(String name) {
        ContaBancaria c = null;

        String sql = "select * from contabancaria where nome_banco like ?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + name + "%");
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    c = new ContaBancaria();
                    c.setId(result.getInt("id"));
                    c.setNome_banco(result.getString("nome_banco"));
                    c.setNumero_conta(result.getString("numero"));
                    c.setNib(result.getString("nib"));
                    c.setNuib(result.getString("nuib"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                    c.setData_criacao(result.getString("data_criacao"));
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        String sql = "delete from contabancaria where id=?";
        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        String sql = "update contabancaria set active_or_not=false where id=?";

        try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
    }
}
