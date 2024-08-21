/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import sysfact.conexao.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class UsuarioDAO {
    private Connection con;
    
    public UsuarioDAO() throws SQLException{
        this.con = Conexao.getConexao();
    }
    
    
}
