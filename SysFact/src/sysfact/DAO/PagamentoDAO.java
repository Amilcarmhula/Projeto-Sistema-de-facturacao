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
import sysfact.logical.Pagamento;

/**
 *
 * @author JM-Tecnologias
 */
public class PagamentoDAO implements DaoImpl<Pagamento> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public PagamentoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Pagamento t) {
        try {
            sql = "insert into pagamento (data_pagamento,referencia_numero_fatura"
                    + ",valor,fk_id_metodo_pagamento,fk_id_factura,fk_id_usuario)"
                    + " value (?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getData_pagamento());
            ps.setLong(2, t.getReferencia_numero_fatura());
            ps.setDouble(3, t.getValor());
            ps.setDouble(4, t.getFk_id_metodo_pagamento());
            ps.setInt(5, t.getFk_id_factura());
            ps.setInt(6, t.getFk_id_usuario());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar pagamento: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Pagamento t, int id) {
        try {
            sql = "update pagamento set data_pagamento=?,referencia_numero_fatura=?"
                    + ",valor=?,fk_id_metodo_pagamento=?,fk_id_factura=?,fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getData_pagamento());
            ps.setLong(2, t.getReferencia_numero_fatura());
            ps.setDouble(3, t.getValor());
            ps.setDouble(4, t.getFk_id_metodo_pagamento());
            ps.setInt(5, t.getFk_id_factura());
            ps.setInt(6, t.getFk_id_usuario());
            ps.setInt(7, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar  pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Pagamento> listAllEntities() {
        List<Pagamento> lista = new ArrayList<>();
        try {
            sql = "select * from pagamento";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Pagamento u = new Pagamento();
                u.setId(result.getInt("id"));
                u.setData_pagamento(result.getString("data_pagamento"));
                u.setReferencia_numero_fatura(result.getLong("referencia_numero_fatura"));
                u.setValor(result.getDouble("valor"));
                u.setFk_id_metodo_pagamento(result.getInt("fk_id_metodo_pagamento"));
                u.setFk_id_factura(result.getInt("fk_id_factura"));
                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(u);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar pagamento: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public Pagamento searchEntityByName(String number) {
        Pagamento u = null;
        try {
            sql = "select * from pagamento where referencia_numero_fatura like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + number + "%");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                u = new Pagamento();
                u.setId(result.getInt("id"));
                u.setData_pagamento(result.getString("data_pagamento"));
                u.setReferencia_numero_fatura(result.getLong("referencia_numero_fatura"));
                u.setValor(result.getDouble("valor"));
                u.setFk_id_metodo_pagamento(result.getInt("fk_id_metodo_pagamento"));
                u.setFk_id_factura(result.getInt("fk_id_factura"));
                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar pagamento: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return u;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from pagamento where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update pagamento set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar pagamento: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
