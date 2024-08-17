/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.cliente.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sysfact.cliente.Cliente;
import sysfact.conexao.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ClienteDAO {
    private Connection con;
   
    public ClienteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    public void adicionaCliente(Cliente c){
        try {
            String sql ="insert into cliente (nome,apelido,nuit,data_criacao) value (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getApelido());
            stmt.setInt(3, c.getNuit());
            stmt.setString(4, c.getData_criacao());
            
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Cliente: "+e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public List<Cliente> listaCliente(){
        List<Cliente> lista = new ArrayList<>();
        try {
            String sql = "select * from cliente";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
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
            JOptionPane.showMessageDialog(null, "Falha ao Bucar Clientes: "+e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return lista;
    }
    
    public Cliente listaClientePorNome(String nome){
        Cliente cliente = null;
        try {        
            String sql = "select id, nome, apelido, nuit, data_criacao from cliente where nome=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet result = stmt.executeQuery();
            cliente = new Cliente();
            if(result.next()){
                cliente.setApelido(result.getString("apelido"));
                cliente.setData_criacao(result.getString("data_criacao"));
                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setNuit(result.getInt("nuit"));
            }
            result.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar Clientes: "+e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return cliente;
    }
    
    
}
