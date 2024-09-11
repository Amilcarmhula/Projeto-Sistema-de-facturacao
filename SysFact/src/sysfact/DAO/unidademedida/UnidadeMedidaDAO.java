/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.unidademedida;

import sysfact.dao.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.model.unidade_medida.UnidadeMedida;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class UnidadeMedidaDAO  implements UnidadeMedidaDAOImpl<UnidadeMedida> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public UnidadeMedidaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(UnidadeMedida t) {
        try {
            sql = "insert into unidade_medida (identificador, sigla)"
                    + " value (?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getIdentificador());
            ps.setString(2, t.getSigla());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Unidade de medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(UnidadeMedida t) {
        try {
            sql = "update unidade_medida set identificador=?, sigla=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getIdentificador());
            ps.setString(2, t.getSigla());
            ps.setInt(3, t.getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar  unidade medida: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<UnidadeMedida> listAllEntities() {
        ObservableList<UnidadeMedida> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from unidade_medida";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                UnidadeMedida u = new UnidadeMedida();
                u.setId(result.getInt("id"));
                u.setIdentificador(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
                if (u.isActive_or_not()) {
                    lista.add(u);
                }
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Unidade de Medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
    
    @Override
    public UnidadeMedida getEntityByID(int id) {
        UnidadeMedida u = null;
        try {
            sql = "select * from unidade_medida where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                u = new UnidadeMedida();
                u.setId(result.getInt("id"));
                u.setIdentificador(result.getString("identificador"));
                u.setSigla(result.getString("sigla"));
                u.setData_criacao(result.getString("data_criacao"));
                u.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Unidade de Medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return u;
    }
    
    @Override
    public Map<String, Integer> mapUnidadeMedida() {
        Map<String, Integer> mapa = new HashMap<>();
        try {
            sql = "select * from unidade_medida";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                UnidadeMedida u = new UnidadeMedida();
                u.setActive_or_not(result.getBoolean("active_or_not"));
                if (u.isActive_or_not()) {
                    mapa.put(result.getString("sigla"), result.getInt("id"));
                }
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao mapear unidade de medida: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return mapa;
    }



//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from unidade_medida where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar  unidade medida: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
    @Override
    public void deleteEntityByUSER(UnidadeMedida x) {
        try {
            sql =  "update unidade_medida set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, x.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar unidade medida: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
