/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jminvsm.teste;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import javax.imageio.ImageIO;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
//import java.io.File;
//import java.nio.file.FileSystem;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import javafx.collections.ObservableList;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRExporterParameter;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.JRXlsExporter;
//import sysfact.dao.cliente.ClienteDAO;
//import sysfact.dao.stock.StockDAO;
//import sysfact.dao.vendas.factura.FacturaDAO;
//import sysfact.dao.vendas.itens.ItemDAO;
//import sysfact.model.stock.Stock;
//import sysfact.model.vendas.factura.Factura;
//import sysfact.model.vendas.factura.itens.Item;
//import sysfact.reports.facturas.ClienteData;
//import sysfact.util.ValidationUtilities;

import com.google.zxing.*;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 *
 * @author JM-Tecnologias
 */
public class Teste {

    public static void criaParcelas(int numeroDeParcelas, int intervalo) {
        LocalDate dataActual = LocalDate.now();

        LocalDate dataFinalParcelas = null;
        for (int i = 1; i <= numeroDeParcelas; i++) {

            if (i == 1) {
                // a variavel X armazena os dias restantes ate a data de vencimento da primeira parcela
//                int x = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth();
//                int x = LocalDate.now().lengthOfMonth() - LocalDate.now().plusDays(intervalo);
                //cria a data de vencimento da primeira parcela
                dataFinalParcelas = dataActual.plusDays(i * intervalo);
                // actualiza a data actual para a data de vencimento da primeira paracela para iniciar a contagem em funcao dos intervalos
                dataActual = dataActual.plusDays(i * intervalo);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataActual.plusDays(2);
                }
                if (dataActual.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataActual.plusDays(1);
                }
                System.out.println("1* Data de vencimento: " + i + " " + dataFinalParcelas);
            } else {
                //adiciona o intervalo de dias de parcelamento para as parcelas subsequentes
                dataFinalParcelas = dataActual.plusDays((i - 1) * intervalo);
                //adiciona dias caso a data de vencimento conscida com uma sabado ou domingo
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SATURDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(2);
                }
                if (dataFinalParcelas.getDayOfWeek() == LocalDate.now().getDayOfWeek().SUNDAY) {
                    dataFinalParcelas = dataFinalParcelas.plusDays(1);
                }
                System.out.println("Data de vencimento: " + i + " " + dataFinalParcelas);
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        try {
//            // Caminho da imagem com o código de barras
//            File file = new File("codigo_de_barras.png");
//
//            // Ler a imagem como BufferedImage
//            BufferedImage bufferedImage = ImageIO.read(file);
//
//            // Preparar a fonte luminosa para a leitura
//            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//            // Configuração para o decodificador
//            MultiFormatReader reader = new MultiFormatReader();
//            Result result = reader.decode(bitmap);
//
//            // Exibir o conteúdo do código de barras
//            System.out.println("Código de barras lido: " + result.getText());
//            System.out.println("Formato: " + result.getBarcodeFormat());
//        } catch (NotFoundException e) {
//            System.err.println("Nenhum código de barras foi encontrado na imagem.");
//        } catch (IOException e) {
//            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
//        }
        
//        LocalDate currentDate = LocalDate.now(); // Get the current date +"\\src\\sysfact\\reports\\", "../"
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("+ dd - MMM - yyyy");
//        System.out.println(currentDate.format(df).replace("+", "Maputo, aos ").replace("-", "de"));
//        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("+ dd - MMM - yyyy")).replace("+", "Maputo, aos ").replace("-", "de"));
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) throws SQLException {
//        String numero = "";
//        Integer num;
//        num = Integer.parseInt(numero.equals("")?"0":numero);
//        System.out.println(num);
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
//    }
}
