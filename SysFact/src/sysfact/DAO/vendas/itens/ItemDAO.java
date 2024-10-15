/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.vendas.itens;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.model.desconto.Desconto;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.taxa_imposto.TaxaImposto;
import sysfact.model.vendas.factura.Factura;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.reports.facturas.itensFacturaData;
import sysfact.util.Conexao;
import sysfact.util.ValidationUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class ItemDAO implements ItemDAOImpl<Item> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ItemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(List<Item> items) {
        try {
            for (Item t : items) {
                sql = "insert into itemfactura (quantidade, subtotal,fk_id_desconto,fk_id_produto,fk_numero,"
                        + "fk_id_usuario, fk_id_armazem) value (?,?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, t.getQuantidade());// ***********
                ps.setDouble(2, t.getSubtotal());// ***********
                ps.setInt(3, t.getDesconto().getId());// ***********
                ps.setInt(4, t.getProduto().getId());// ***********
                ps.setString(5, t.getFactura().getNumero_fac());// ***********
                ps.setInt(6, t.getUsuario().getId());// ***********
                ps.setInt(7, t.getArmazem().getId());// ***********

                ps.execute();
                ps.close();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Item: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Item> getItensData(String factura) {
        List<Item> lista = new ArrayList<>();
        Item i = null;
        try {
            sql = "SELECT p.nome, p.descricao, pv.preco, "
                    + "i.quantidade, i.subtotal, "
                    + "ip.identificador as descricao_imposto, ip.percentagem AS imposto, "
                    + "d.nome AS descricao_desconto, d.percentagem AS desconto, "
                    + "f.numero "
                    + "FROM itemfactura i "
                    + "LEFT JOIN factura f ON f.numero = i.fk_numero "
                    + "LEFT JOIN desconto d ON d.id = i.fk_id_desconto "
                    + "LEFT JOIN produto p ON p.id = i.fk_id_produto "
                    + "LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id "
                    + "LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto "
                    + "WHERE i.fk_numero = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, factura);
            ResultSet result = ps.executeQuery();
            double sub = 0;
            while (result.next()) {
                i = new Item();
                
                Desconto d = new Desconto();
                d.setNomeDesconto(result.getString("descricao_desconto"));
                d.setPercentagemDesconto(result.getDouble("desconto"));
                i.setDesconto(d);
                i.setQuantidade(result.getInt("quantidade"));
                i.setSubtotal(result.getDouble("subtotal"));
                Produto p = new Produto();
                p.setNomeProduto(result.getString("nome"));
                p.setDescricao(result.getString("descricao"));
                PrecoVenda pv = new PrecoVenda();
                pv.setPreco(result.getDouble("preco"));
                p.setPrecovenda(pv);
                TaxaImposto t = new TaxaImposto();
                t.setNomeImposto(result.getString("descricao_imposto"));
                t.setPercentagemImposto(result.getDouble("imposto"));
                p.setTaxa_imposto(t);
                i.setProduto(p);
                Factura f = new Factura();
                f.setNumero_fac(result.getString("numero"));
                i.setFactura(f);
//                sub += i.getQuantidadeProduto() * i.getPrecoProduto();
//                i.setSubtotal(sub);
                lista.add(i);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            ValidationUtilities.showDialog(FontAwesomeIcon.TIMES, "Informação", "Erro ao carregar itens da Factura: " + e);
        }
        return lista;
    }
//
//    @Override
//    public void updateEntityByID(Item t, int id) {
//        try {
//            sql = "update item_factura set tipo=?,quantidade=?,preco_unitario=?,"
//                    + "subtotal=?,fk_id_desconto=?,fk_id_produto=?,fk_id_factura=?,"
//                    + "fk_id_servico=?,fk_id_usuario=?"
//                    + " where id=?";
//            ps = con.prepareStatement(sql);
//
////            ps.setString(1, t.getTipo());
////            ps.setDouble(2, t.getQuantidade());
////            ps.setDouble(3, t.getPreco_unitario());
////            ps.setDouble(4, t.getSubtotal());
////            ps.setInt(5, t.getFk_id_desconto());
////            ps.setInt(6, t.getFk_id_produto());
////            ps.setInt(7, t.getFk_id_factura());
////            ps.setInt(8, t.getFk_id_servico());
////            ps.setInt(9, t.getFk_id_usuario());
//            ps.setInt(10, id);
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar  Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public List<Item> listAllEntities() {
//        List<Item> lista = new ArrayList<>();
//        try {
//            sql = "select * from item_factura";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Item u = new Item();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
////                u.setQuantidade(result.getLong("quantidade"));
////                u.setPreco_unitario(result.getDouble("preco_unitario"));
////                u.setSubtotal(result.getDouble("subtotal"));
////                u.setFk_id_desconto(result.getInt("fk_id_desconto"));
////                u.setFk_id_produto(result.getInt("fk_id_produto"));
////                u.setFk_id_factura(result.getInt("fk_id_factura"));
////                u.setFk_id_servico(result.getInt("fk_id_servico"));
////                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
////                u.setData_criacao(result.getString("data_criacao"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//                lista.add(u);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Factura: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//
//    @Override
//    public Item searchEntityByName(String number) {
//        Item u = null;
//        try {
//            sql = "select * from item_factura where fk_id_factura like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + number + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                u = new Item();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
////                u.setQuantidade(result.getLong("quantidade"));
////                u.setPreco_unitario(result.getDouble("preco_unitario"));
////                u.setSubtotal(result.getDouble("subtotal"));
////                u.setFk_id_desconto(result.getInt("fk_id_desconto"));
////                u.setFk_id_produto(result.getInt("fk_id_produto"));
////                u.setFk_id_factura(result.getInt("fk_id_factura"));
////                u.setFk_id_servico(result.getInt("fk_id_servico"));
////                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
////                u.setData_criacao(result.getString("data_criacao"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Item: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return u;
//
//    }
//
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from item_factura where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public void deleteEntityByUSER(int id) {
//        try {
//            sql = "update item_factura set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
