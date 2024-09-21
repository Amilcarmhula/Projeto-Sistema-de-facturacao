/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.vendas.factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.model.vendas.factura.Factura;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class FacturaDAO implements FacturaDAOImpl<Factura> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public FacturaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(Factura t) {
        try {
            sql = "insert into factura (numero,data_vencimento, estado,"
                    + "fk_id_empresa,fk_id_cliente,fk_id_usuario)"
                    + " value (?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNumero());
            ps.setString(2, t.getData_vencimento());
            ps.setBoolean(3, t.isEstado());
            ps.setInt(4, t.getEmpresa().getId());
            ps.setInt(5, t.getCliente().getId());
            ps.setInt(6, t.getUsuario().getId());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao criar Factura: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
//
//    @Override
//    public void updateEntityByID(Factura t, int id) {
//        try {
//            sql = "update factura set tipo=?, fk_id_cliente=?"
//                    + " where id=?";
//            ps = con.prepareStatement(sql);
//
//             ps.setString(1, t.getTipo());
//            ps.setInt(2, t.getFk_id_agente());
//            ps.setInt(3, id);
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar  Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public List<Factura> listAllEntities() {
//        List<Factura> lista = new ArrayList<>();
//        try {
//            sql = "select * from factura";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Factura u = new Factura();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
//                u.setNumero(result.getLong("numero"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setData_vencimento(result.getString("data_vencimento"));
//                u.setEstado(result.getBoolean("estado"));
//                u.setTotal(result.getDouble("total"));
//                u.setFk_id_cliente(result.getInt("fk_id_cliente"));
//                u.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
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
//    public Factura searchEntityByName(String number) {
//        Factura u = null;
//        try {
//            sql = "select * from factura where numero like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + number + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                u = new Factura();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
//                u.setNumero(result.getLong("numero"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setData_vencimento(result.getString("data_vencimento"));
//                u.setEstado(result.getBoolean("estado"));
//                u.setTotal(result.getDouble("total"));
//                u.setFk_id_cliente(result.getInt("fk_id_cliente"));
//                u.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Factura: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return u;
//
//    }
//
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from factura where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    @Override
//    public void deleteEntityByUSER(int id) {
//        try {
//            sql =  "update factura set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
