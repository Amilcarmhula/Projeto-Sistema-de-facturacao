/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.util.Conexao;
import sysfact.logical.MetodoPagamento;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodoPagamentoDAO implements DaoImpl<MetodoPagamento> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public MetodoPagamentoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(MetodoPagamento t) {
        try {
            sql = "insert into metodo_pagamento (nome,descricao) value (?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Metodo Pagamento: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(MetodoPagamento t, int id) {
        try {
            sql = "update metodo_pagamento set nome=?,descricao=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNome());
            ps.setString(2, t.getDescricao());
            ps.setInt(3, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Metodo Pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<MetodoPagamento> listAllEntities() {
        List<MetodoPagamento> lista = new ArrayList<>();
        try {
            sql = "select * from metodo_pagamento";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                MetodoPagamento u = new MetodoPagamento();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setDescricao(result.getString("descricao"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(u);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Metodo Pagamento: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public MetodoPagamento searchEntityByName(String number) {
        MetodoPagamento u = null;
        try {
            sql = "select * from metodo_pagamento where nome like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + number + "%");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                u = new MetodoPagamento();
                u.setId(result.getInt("id"));
                u.setNome(result.getString("nome"));
                u.setDescricao(result.getString("descricao"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Metodo Pagamento: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return u;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from metodo_pagamento where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Metodo Pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update metodo_pagamento set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Metodo Pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
