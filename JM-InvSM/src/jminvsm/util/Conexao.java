/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsavel por moldar a conexao com de dados
 *
 * @author JM-Tecnologias
 */
public class Conexao {

    private static final String db = "jdbc:mysql://localhost:3306/_sysface";
    private static final String user = "root";
    private static final String pws = "Informatico12";

    /**
     * Metodo que busca o conexao com o banco de dados usando o driver de
     * conexao JDBC.Driver para o banco MySQL
     */
    public static Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Conectado!");
            return DriverManager.getConnection(db, user, pws);
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex.getMessage());
        }
    }
}
