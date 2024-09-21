/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.vendas.itens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.util.Conexao;

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
//
//    @Override
//    public void addEntity(List<Item> items) {
//        try {
//            for (Item t : items) {
//                    sql = "insert into item_factura (quantidade,fk_id_precovenda,"
//                            + "subtotal,fk_id_desconto,fk_id_taxa_imposto,fk_id_produto,fk_id_factura,"
//                            + "fk_id_usuario) value (?,?,?,?,?,?,?,?)";
//                    ps = con.prepareStatement(sql);
//                    ps.setInt(1, t.getQuantidade());
//                    ps.setDouble(3, t.getSubtotal());
//                    ps.setInt(4, t.getDesconto().getId());
//                    ps.setInt(6, t.getProduto().getId());
//                    ps.setInt(7, t.getFactura().getId());
//                    ps.setInt(8, t.getUsuario().getId());
//                
//                ps.execute();
//                ps.close();
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao adicionar Item: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
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
