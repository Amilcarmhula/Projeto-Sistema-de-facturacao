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
import sysfact.logical.Cliente;

/**
 *
 * @author JM-Tecnologias
 */
public class ClienteDAO {

    private Connection con;

    public ClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    public void adicionaCliente(Cliente c) {
        try {
            String sql = "insert into cliente (nome,apelido,nuit) value (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getApelido());
            stmt.setInt(3, c.getNuit());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizaCliente(Cliente c, int id) {
        try {
            String sql = "update cliente set nome=? ,apelido=? ,nuit=?"
                    + " where id=?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getApelido());
            stmt.setInt(3, c.getNuit());
            stmt.setInt(4, id);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar Cliente: " 
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Cliente> listaCliente() {
        List<Cliente> lista = new ArrayList<>();
        try {
            String sql = "select * from cliente";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Cliente c = new Cliente();
                c.setId(result.getInt("id"));
                c.setApelido(result.getString("apelido"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setNome(result.getString("nome"));
                c.setNuit(result.getInt("nuit"));
                lista.add(c);
            }
            result.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar Clientes: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }

    /*
    Pesquisas por nome ou outra celula no banco de dados que devem retornar todos 
    elementos que contenham o parametro da pesquisa devem usar o while e nao o if;
    e usando o while os elementos devem ser armazenados numa lista, para posteriormente
    serem visualizados numa tabela
     */
    public Cliente listaClientePorNome(String nome) {
        Cliente cliente = null;
        try {
            String sql = "select * from cliente where nome LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                cliente = new Cliente();
                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setApelido(result.getString("apelido"));
                cliente.setNuit(result.getInt("nuit"));
                cliente.setData_criacao(result.getString("data_criacao"));
            }
            result.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar Clientes: " + e,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return cliente;
    }

//    public List<Cliente> listaClientePorNome(String nome) {
//        List<Cliente> cli = new ArrayList<>();
//        Cliente cliente = null;
//        try {
//            String sql = "select * from cliente where nome LIKE ?";
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setString(1, "%" + nome + "%");
//            ResultSet result = stmt.executeQuery();
//            while (result.next()) {
//                cliente = new Cliente();
//                cliente.setApelido(result.getString("apelido"));
//                cliente.setData_criacao(result.getString("data_criacao"));
//                cliente.setId(result.getInt("id"));
//                cliente.setNome(result.getString("nome"));
//                cliente.setNuit(result.getInt("nuit"));
//                cli.add(cliente);
//            }
//            result.close();
//            stmt.close();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Falha ao Bucar Clientes: " + e,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return cli;
//    }
    
}
