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

    private static final String db = "jdbc:mysql://localhost:3306/_sysface_teste";
    private static final String user = "root";
    private static final String pws = "Informatico@12";

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
//package jminvsm.util;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class Conexao {
//
//    private static HikariDataSource dataSource;
//
//    static {
//
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(
//                "jdbc:mysql://localhost:3306/_sysface_teste"
//                + "?useSSL=false"
//                + "&allowPublicKeyRetrieval=true"
//                + "&serverTimezone=UTC"
//        );
////        config.setJdbcUrl(
////                "jdbc:mysql://95.216.50.17:3306/_sysface"
////                + "?useSSL=false"
////                + "&allowPublicKeyRetrieval=true"
////                + "&serverTimezone=UTC"
////        );
//        
//        config.setUsername("root");
////        config.setUsername("jmuser");
//        config.setPassword("Informatico@12");
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        // Configurações importantes
//        config.setMaximumPoolSize(10);   // conexões simultâneas
//        config.setMinimumIdle(2);
//        config.setIdleTimeout(50000);
//        config.setConnectionTimeout(50000);
//        config.setMaxLifetime(1500000);
//
//        dataSource = new HikariDataSource(config);
//
//        System.out.println("Pool de conexões iniciado!");
//    }
//
//    public static Connection getConexao() throws SQLException {
//        return dataSource.getConnection();
//    }
//}
