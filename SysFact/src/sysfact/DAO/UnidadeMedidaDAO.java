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
import sysfact.logical.UnidadeMedida;

/**
 *
 * @author JM-Tecnologias
 */
public class UnidadeMedidaDAO  implements DaoImpl<UnidadeMedida> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public UnidadeMedidaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(UnidadeMedida t) {
        try {
            sql = "insert into unidade_medida (tipo, identificador, sigla)"
                    + " value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getTipo());
            ps.setString(2, t.getIdentificador());
            ps.setString(3, t.getSigla());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Unidade de medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(UnidadeMedida t, int id) {
        try {
            sql = "update unidade_medida set tipo=?, identificador=?, sigla=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

             ps.setString(1, t.getTipo());
            ps.setString(2, t.getIdentificador());
            ps.setString(3, t.getSigla());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar  unidade medida: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<UnidadeMedida> listAllEntities() {
        List<UnidadeMedida> lista = new ArrayList<>();
        try {
            sql = "select * from unidade_medida";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("id"));
                u.setTipo(result.getString("tipo"));
                u.setIdentificador(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(u);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Unidade de Medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public UnidadeMedida searchEntityByName(String name) {
        UnidadeMedida u = null;
        try {
            sql = "select * from unidade_medida where identificador like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                u = new UnidadeMedida();
                u.setId(result.getInt("id"));
                u.setTipo(result.getString("tipo"));
                u.setIdentificador(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Unidade de medida: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return u;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from unidade_medida where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar  unidade medida: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update unidade_medida set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar unidade medida: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
