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

//        c.setNome("Amilcar Junior");
        c.setApelido("Mula Filho do Pai");
        c.setNuit(777888999);
        c.setData_criacao("2024-08-17 01:53:32");
        cdao.adicionaCliente(c);

//        List<Cliente> lista = cdao.listaCliente();
//        for (Cliente cliente : lista) {
//            System.out.println(cliente.getNome());
//        }

//        Cliente cliente = cdao.listaClientePorNome("Amilc");
//        System.out.println(cliente.getNuit());

//        List<Cliente> lista = cdao.listaClientePorNome("Ven");
//        for (Cliente cliente : lista) {
//            System.out.println(cliente.getNome());
//        }

//        c.setId(4);
//        c.setNome("Venmu");
//        c.setApelido("Mula Jr. Filho");
//        c.setNuit(987654321);
//        cdao.actualizaCliente(c, c.getId());
    }

}
