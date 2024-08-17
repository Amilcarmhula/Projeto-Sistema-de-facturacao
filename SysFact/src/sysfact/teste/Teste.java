/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact.teste;

import java.sql.SQLException;
import java.util.List;
import sysfact.cliente.Cliente;
import sysfact.cliente.DAO.ClienteDAO;

/**
 *
 * @author JM-Tecnologias
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Cliente c = new Cliente();
        ClienteDAO cdao = new ClienteDAO();

//        c.setNome("Junior ");
//        c.setApelido("Mula Filho");
//        c.setNuit(987654321);
//        c.setData_criacao("2024-08-17 23:23:32");
//        cdao.adicionaCliente(c);

//        List<Cliente> lista = cdao.listaCliente();
//        for (Cliente cliente : lista) {
//            System.out.println(cliente.getNome());
//        }

        Cliente cliente = cdao.listaClientePorNome("Junior ");
        System.out.println(cliente.getNuit());

    }

}
