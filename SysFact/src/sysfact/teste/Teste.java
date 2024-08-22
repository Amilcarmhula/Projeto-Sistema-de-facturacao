/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact.teste;

import java.sql.SQLException;
import java.util.List;
import org.w3c.dom.css.Counter;
import sysfact.DAO.ClienteDAO;
import sysfact.DAO.UsuarioDAO;
import sysfact.logical.Cliente;
import sysfact.logical.Usuario;

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
        Usuario u = new Usuario();
        ClienteDAO cdao = new ClienteDAO();
        UsuarioDAO ud = new UsuarioDAO();

//        c.setNome("Amilcar");
//        c.setApelido("Mula Pai");
//        c.setNuit(777888999);
//        cdao.adicionaCliente(c);
//        List<Usuario> lista = ud.listAllEntities();
//        for (Usuario u : lista) {
//            System.out.println(u.isActive_or_not());
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
//        u.setId(1);
//        u.setNome("Carla Jorge");
//        u.setUsuario("user");
//        u.setSenha(12321);
//        u.setCodigo(123);
//        ud.updateEntityByID(u, u.getId());
//        ud.addEntity(u);

        ud.deleteEntity(5);
        List<Usuario> lista = ud.listAllEntities();
        for (Usuario usuario : lista) {
            System.out.println("ID: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Username: " + usuario.getUsuario());
            System.out.println("Code: " + usuario.getCodigo());
            System.out.println("Senha: " + usuario.getSenha());
            System.out.println("Data added: " + usuario.getData_criacao());
            System.out.println("Status: " + usuario.isActive_or_not());
            System.out.println("");
        }

//        List<Usuario> lista = ud.searchListEntityByName("Car");
//        for (Usuario usuario : lista) {
//            System.out.println("ID: " + usuario.getId());
//            System.out.println("Nome: " + usuario.getNome());
//            System.out.println("Username: " + usuario.getUsuario());
//            System.out.println("Code: " + usuario.getCodigo());
//            System.out.println("Senha: " + usuario.getSenha());
//            System.out.println("Data added: " + usuario.getData_criacao());
//            System.out.println("Status: " + usuario.isActive_or_not());
//            System.out.println("");
//        }

//        Usuario usuario = ud.searchEntityByName("lCar");
//        System.out.println("ID: " + usuario.getId());
//        System.out.println("Nome: " + usuario.getNome());
//        System.out.println("Username: " + usuario.getUsuario());
//        System.out.println("Code: " + usuario.getCodigo());
//        System.out.println("Senha: " + usuario.getSenha());
//        System.out.println("Data added: " + usuario.getData_criacao());
//        System.out.println("Status: " + usuario.isActive_or_not());
//        System.out.println("");
    }

}
