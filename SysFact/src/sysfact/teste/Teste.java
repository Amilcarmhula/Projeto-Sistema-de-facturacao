/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact.teste;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import org.w3c.dom.css.Counter;
import sysfact.DAO.ArmazemDAO;
import sysfact.DAO.CategoriaDAO;
import sysfact.DAO.AgenteDAO;
import sysfact.DAO.ContactoAgenteDAO;
import sysfact.DAO.DescontoDAO;
import sysfact.DAO.AgenteDAO;
import sysfact.DAO.ProdutoDAO;
import sysfact.DAO.ProdutoFornecidoDAO;
import sysfact.DAO.ServicoDAO;
import sysfact.DAO.TaxaImpostoDAO;
import sysfact.DAO.UnidadeMedidaDAO;
import sysfact.DAO.UsuarioDAO;
import sysfact.logical.Armazem;
import sysfact.logical.Categoria;
import sysfact.logical.Agente;
import sysfact.logical.ContactoAgente;
import sysfact.logical.Desconto;
import sysfact.logical.Agente;
import sysfact.logical.Produto;
import sysfact.logical.ProdutoFornecido;
import sysfact.logical.Servico;
import sysfact.logical.TaxaImposto;
import sysfact.logical.UnidadeMedida;
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
        ContactoAgenteDAO cad = new ContactoAgenteDAO();
        ObservableList<ContactoAgente> lista = cad.searchEntityByAgente(2);
        for (ContactoAgente ca : lista) {
            System.out.println("ID: " + ca.getId());
        }
//        Agente a = new Agente();
//        a.setTipo("Cliente");
//        a.setNome("Amilcar");
//        a.setApelido("Mula");
//        a.setNuit(123456789);
//        a.setFk_id_usuario(1);
//        AgenteDAO ad = new AgenteDAO();
//        ad.addEntity(a);

//        Usuario u = new Usuario();
//        UsuarioDAO ud = new UsuarioDAO();
//        u.setUsuario("root");
//        u.setCodigo(407);
//        u.setSenha(4321);
//
//        Usuario user = ud.login(u);
//        System.out.println("ID: " + user.getId());
//        System.out.println("Nome: " + user.getNome());
//        System.out.println("Username: " + user.getUsuario());
//        System.out.println("Code: " + user.getCodigo());
//        System.out.println("Senha: " + user.getSenha());
//        System.out.println("Date added: " + user.getData_criacao());
//        System.out.println("Status: " + user.isActive_or_not());
//        System.out.println("");
//        f.setNome("Fornecedor 4");
//        f.setNuit(999888777);
//        fd.addEntity(f);
//        CategoriaDAO cd = new CategoriaDAO();
//        Categoria c = cd.searchEntityByName("Sofás");
//        System.out.println("ID: " + c.getId());
//        System.out.println("Nome: " + c.getTipo());
//        System.out.println("");
//
//        TaxaImpostoDAO td = new TaxaImpostoDAO();
//        TaxaImposto t = td.searchEntityByName("IVA");
//        System.out.println("ID: " + t.getId());
//        System.out.println("Nome: " + t.getIdentificador());
//        System.out.println("");
//
//        UsuarioDAO ud = new UsuarioDAO();
//        Usuario u = ud.searchEntityByName("Jr Mula");
//        System.out.println("ID: " + u.getId());
//        System.out.println("Nome: " + u.getNome());
//        System.out.println("");
//
//        UnidadeMedidaDAO umd = new UnidadeMedidaDAO();
//        UnidadeMedida um = umd.searchEntityByName("tonelada");
//        System.out.println("ID: " + um.getId());
//        System.out.println("Nome: " + um.getIdentificador());
//        System.out.println("");
//
////        ServicoDAO sd = new ServicoDAO();
////        Servico s = new Servico("venda de Eletronicos", "Servico 1", c, t, u);
////        sd.addEntity(s);
////        ProdutoDAO pd = new ProdutoDAO();
////        Produto p = new Produto("Cadeira singular", "12212", 1500.50, "Cadeira de uma Pessoa", c, um, t);
////        pd.addEntity(p);
//        FornecedorDAO fd = new FornecedorDAO();
//        Fornecedor f = fd.searchEntityByName("Fornecedor 4");
//
//        ProdutoDAO pd = new ProdutoDAO();
//        Produto p = pd.searchEntityByName("Cadeira singular");
//
//        ArmazemDAO ad = new ArmazemDAO();
//        Armazem a = ad.searchEntityByName("HomoSotore");
//
//        ProdutoFornecidoDAO pfd = new ProdutoFornecidoDAO();
//        ProdutoFornecido pf = new ProdutoFornecido(10, "2024-08-28 7:03:00", 13684.39, a, p, f, u);
////        pfd.addEntity(pf);
//        AgenteDAO clid = new AgenteDAO();
//        Agente cli = new Agente("Agente 1", "Mula", 123456789, u);
//        clid.addEntity(cli);
//        ContactoFornecedorDAO cfd = new ContactoFornecedorDAO();
//        ContactoFornecedor cf = new ContactoFornecedor(ff, "878201993", "teste6@mail.com");
//        ContactoFornecedor cf = new ContactoFornecedor();
//        cf.setContacto("843333333");
//        cf.setEmail("teste5@mail.com");
//        cf.setFk_id_Fornecedor(ff.getId());
//        cfd.addEntity(cf);
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
//        Agente cliente = cdao.listaClientePorNome("Amilc");
//        System.out.println(cliente.getNuit());
//        List<Cliente> lista = cdao.listaClientePorNome("Ven");
//        for (Agente cliente : lista) {
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
