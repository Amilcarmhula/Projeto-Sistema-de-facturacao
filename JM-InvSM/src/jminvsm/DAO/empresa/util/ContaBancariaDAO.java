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
import javax.swing.JOptionPane;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaBancariaDAO implements DaoImpl<ContaBancaria> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContaBancariaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(ContaBancaria t) {
        try {
            sql = "insert into contabancaria (nome_banco,numero,nib,nuib,"
                    + "fk_id_empresa) value (?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome_banco());
            ps.setString(2, t.getNumero_conta());
            ps.setString(3, t.getNib());
            ps.setString(4, t.getNuib());
            ps.setInt(5, t.getEmpresa().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Conta Bancaria: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(ContaBancaria t, int id) {
        try {
            sql = "update contabancaria set nome_banco=?,numero=?,nib=?,nuib=?,"
                    + "fk_id_empresa=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome_banco());
            ps.setString(2, t.getNumero_conta());
            ps.setString(3, t.getNib());
            ps.setString(4, t.getNuib());
            ps.setInt(5, t.getEmpresa().getId());
            ps.setInt(6, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Conta Bancaria: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<ContaBancaria> listAllEntities() {
        List<ContaBancaria> lista = new ArrayList<>();
        try {
            sql = "select * from contabancaria";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
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
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Conta Bancaria: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public ContaBancaria searchEntityByName(String name) {
        ContaBancaria c = null;
        try {
            sql = "select * from contabancaria where nome_banco like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
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
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Conta Bancaria: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from contabancaria where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Conta Bancaria: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update contabancaria set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Conta Bancaria: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
