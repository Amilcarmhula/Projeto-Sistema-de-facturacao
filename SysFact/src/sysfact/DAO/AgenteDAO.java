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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sysfact.conexao.Conexao;
import sysfact.logical.Agente;

/**
 *
 * @author JM-Tecnologias
 */
public class AgenteDAO implements DaoImpl<Agente>{

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public AgenteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Agente c) {
       try {
            sql = "insert into agente (tipo,nome,apelido,nuit,fk_id_usuario) value (?,?,?,?,?)";
            ps= con.prepareStatement(sql);
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getApelido());
            ps.setInt(4, c.getNuit());
            ps.setInt(5, c.getFk_id_usuario());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar agente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Agente c, int id) {
        try {
            sql = "update agente set tipo=?, nome=? ,apelido=? ,nuit=?, fk_id_usuario=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getApelido());
            ps.setInt(4, c.getNuit());
            ps.setInt(5, c.getFk_id_usuario());
            ps.setInt(6, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar agente: " 
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ObservableList<Agente> listAllEntities() {
//        List<Agente> lista = new ArrayList<>();
        ObservableList<Agente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from agente";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Agente c = new Agente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar agente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }
    
    public ObservableList<Agente> searchEntityByName2(String nome) {
        ObservableList<Agente> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from agente where nome LIKE ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nome + "%");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Agente c = new Agente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar agente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

    /*
    Pesquisas por nome ou outra celula no banco de dados que devem retornar todos 
    elementos que contenham o parametro da pesquisa devem usar o while e nao o if;
    e usando o while os elementos devem ser armazenados numa lista, para posteriormente
    serem visualizados numa tabela
     */
     @Override
    public Agente searchEntityByName(String nome) {
        Agente c = null;
        try {
            sql = "select * from agente where nome LIKE ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nome + "%");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Agente();
                c.setId(result.getInt("id"));
                c.setTipo(result.getString("tipo"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setActive_or_not(result.getBoolean("active_or_not"));
                c.setFk_id_usuario(result.getInt("fk_id_usuario"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar agente: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }
     @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from agente where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar agente: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql =  "update agente set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar agente: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
