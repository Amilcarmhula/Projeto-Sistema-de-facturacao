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
import sysfact.conexao.Conexao;
import sysfact.logical.Empresa;

/**
 *
 * @author JM-Tecnologias
 */
public class EmpresaDAO implements DaoImpl<Empresa> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EmpresaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Empresa c) {
        try {
            sql = "insert into empresa (nome,apelido,nuit,) value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setInt(3, c.getNuit());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar empresa: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Empresa c, int id) {
        try {
            sql = "update empresa set nome=? ,apelido=? ,nuit=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setInt(3, c.getNuit());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar empresa: "
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Empresa> listAllEntities() {
        List<Empresa> lista = new ArrayList<>();
        try {
            sql = "select * from empresa";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Empresa c = new Empresa();
                c.setId(result.getInt("id"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setActive_or_not(result.getBoolean("active_or_not"));

                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar empresa: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

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
    public Empresa searchEntityByName(String nome) {
        Empresa c = null;
        try {
            sql = "select * from empresa where nome LIKE ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nome + "%");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new Empresa();
                c.setId(result.getInt("id"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                c.setActive_or_not(result.getBoolean("active_or_not"));

            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar empresa: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;
    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from empresa where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar empresa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update empresa set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar empresa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
