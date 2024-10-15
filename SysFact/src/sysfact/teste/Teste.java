/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact.teste;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.dao.stock.StockDAO;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.stock.Stock;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.reports.facturas.ClienteData;
import sysfact.util.ValidationUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Double numero = 3.45000;
//        System.out.println(Double.valueOf());

//        Cliente c = new Cliente();
//        FacturaDAO dao = new FacturaDAO();
//        Factura f = dao.getFacturaData("2024-1010/2");
//        
//            System.out.println("nome: "+f.getNumero_fac());
//            System.out.println("nome: "+f.getData_criacao());
//            System.out.println("nome: "+f.getData_vencimento());
//            System.out.println("nome: "+f.getDocComerciais().getNome_doc());
//            System.out.println("nome: "+f.getDocComerciais().getSigla_doc());
//            System.out.println("nome: "+f.getCliente().getRazao_cli());
//            System.out.println("nome: "+f.getCliente().getNuit_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getAvenida_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getRua_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getNumero_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getBairro_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getCidade_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getProvincia_cli());
//            System.out.println("nome: "+f.getCliente().getEnderecoCliente().getCodigoPostal_cli());
//            System.out.println("nome: "+f.getCliente().getContactoCliente().getContacto_cli());
//            System.out.println("nome: "+f.getCliente().getContactoCliente().getEmail_cli());
//            System.out.println("nome: "+f.getEmpresa().getNome_emp());
//            System.out.println("nome: "+f.getEmpresa().getRazao_emp());
//            System.out.println("nome: "+f.getEmpresa().getNuit_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getAvenida_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getBairro_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getCidade_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getProvincia_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getRua_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getCodigoPostal_emp());
//            System.out.println("nome: "+f.getEmpresa().getEnderecoEmpresa().getNumero_emp());
//            System.out.println("nome: "+f.getEmpresa().getContactoEmpresa().getContacto_emp());
//            System.out.println("nome: "+f.getEmpresa().getContactoEmpresa().getEmail_emp());
//            System.out.println("nome: "+f.getEmpresa().getContactoEmpresa().getWebsite_emp());
//            System.out.println("nome: "+f.getEmpresa().getContaBancaria().getNome_banco());
//            System.out.println("nome: "+f.getEmpresa().getContaBancaria().getNib());
//            System.out.println("nome: "+f.getEmpresa().getContaBancaria().getNuib());
//            System.out.println("nome: "+f.getEmpresa().getContaBancaria().getNumero_conta());

            System.out.println("____________________________________________________");
                       
            
//            System.out.println("______________________Cliente______________________");
//            
//
//            
//        }
//        UnidadeMedida x = new UnidadeMedida();
//        UnidadeMedidaDAO z = new UnidadeMedidaDAO();
////        x.setId(3);
//        x.setIdentificador("kilo grama");
//        x.setSigla("kg");
//        z.addEntity(x);
//        ProdutoDAO pd = new ProdutoDAO();
//        ObservableList<Produto> lista = pd.listAllEntities2();
//        for (Produto p : lista) {
//            System.out.println(""+p.getTaxa_imposto().getIdentificador());
//            System.out.println(""+p.getUnidade_medida().getIdentificador());
//
//        }
//        for (Map.Entry<Integer, String> entry : mapa.entrySet()) {
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            System.out.println("Chave:"+key+" Valor:"+val);
//        }
//        UnidadeMedidaDAO umd = new UnidadeMedidaDAO();
//        Map<String, Integer> mapa = umd.mapUnidadeMedida();
//        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            System.out.println("Chave:"+key+" Valor:"+val);
//        }
//        
//        System.out.println(mapa);
//        AgenteService cad = new AgenteService();
//        ClienteDAO c = new ClienteDAO();
//        ObservableList<Agente> lista = c.listAllEntities();
//        for (Cliente ca : lista) {
//            System.out.println("ID: " + ca.getId());
//        }
//        Cliente a = new Cliente();
//        a.setTipo("Cliente");
//        a.setNome("Amilcar");
//        a.setApelido("Mula");
//        a.setNuit(123456789);
//        a.setFk_id_usuario(1);
//        ClienteDAO ad = new ClienteDAO();
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
//        ClienteDAO clid = new ClienteDAO();
//        Cliente cli = new Cliente("Cliente 1", "Mula", 123456789, u);
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
