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
import sysfact.logical.TaxaImposto;

/**
 *
 * @author JM-Tecnologias
 */
public class TaxaImpostoDAO  implements DaoImpl<TaxaImposto> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public TaxaImpostoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
   @Override
    public void addEntity(TaxaImposto t) {
        try {
            sql = "insert into taxa_imposto (identificador, percentagem, descricao)"
                    + " value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getIdentificador());
            ps.setDouble(2, t.getPercentagem());
            ps.setString(3, t.getDescricao());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(TaxaImposto t, int id) {
        try {
            sql = "update taxa_imposto set identificador=?, percentagem=?, descricao=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getIdentificador());
            ps.setDouble(2, t.getPercentagem());
            ps.setString(3, t.getDescricao());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Taxa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<TaxaImposto> listAllEntities() {
        List<TaxaImposto> lista = new ArrayList<>();
        try {
            sql = "select * from taxa_imposto";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                TaxaImposto ti = new TaxaImposto();
                ti.setId(result.getInt("id"));
                ti.setIdentificador(result.getString("identificador"));
                ti.setPercentagem(result.getDouble("percentagem"));
                ti.setDescricao(result.getString("descricao"));
                ti.setData_criacao(result.getString("data_criacao"));
                ti.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(ti);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Taxa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public TaxaImposto searchEntityByName(String name) {
        TaxaImposto ti = null;
        try {
            sql = "select * from taxa_imposto where identificador like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                ti = new TaxaImposto();
                ti.setId(result.getInt("id"));
                ti.setIdentificador(result.getString("identificador"));
                ti.setPercentagem(result.getDouble("percentagem"));
                ti.setDescricao(result.getString("descricao"));
                ti.setData_criacao(result.getString("data_criacao"));
                ti.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Taxa: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return ti;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from taxa_imposto where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Taxa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update taxa_imposto set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Taxa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
