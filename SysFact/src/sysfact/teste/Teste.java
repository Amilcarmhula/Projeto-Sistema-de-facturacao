/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact.teste;

import java.sql.SQLException;
import java.util.List;
import org.w3c.dom.css.Counter;
import sysfact.DAO.CategoriaDAO;
import sysfact.DAO.ClienteDAO;
import sysfact.DAO.UsuarioDAO;
import sysfact.logical.Categoria;
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
        Categoria cat = new Categoria();
        CategoriaDAO cd = new CategoriaDAO();
        
        ud.deleteEntityByUSER(7);

//        cat.setTipo("Produto");
//        cat.setGrupo("Vestuário");
//        cat.setSubgrupo("Roupas Casuais");
//        cat.setFamilia("Calças Jeans");
//        cat.setDescricao("O grupo de Vestuário engloba todos os tipos de roupas e acessórios usados para cobrir, proteger e adornar o corpo. Esses produtos são essenciais para a expressão pessoal, proteção contra o clima e conformidade com normas sociais.");
//        cd.addEntity(cat);
        
//        List<Categoria> lista = cd.listAllEntities();
//        for (Categoria categ : lista) {
//            System.out.println("ID: " + categ.getId());
//            System.out.println("Tipo: " + categ.getTipo());
//            System.out.println("Grupo: " + categ.getGrupo());
//            System.out.println("Subgrupo: " + categ.getSubgrupo());
//            System.out.println("Familia: " + categ.getFamilia());
//            System.out.println("Descricao: " + categ.getDescricao());
//            System.out.println("Data added: " + categ.getData_criacao());
//            System.out.println("Status: " + categ.isActive_or_not());
//            System.out.println("");
//        }
        

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

//        ud.deleteEntity(5);
//        List<Usuario> lista = ud.listAllEntities();
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
