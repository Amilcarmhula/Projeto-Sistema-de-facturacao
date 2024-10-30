/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.parcela;

import com.mysql.cj.xdevapi.Result;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.parcelas.Parcela;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ParcelaDAO implements ParcelaDAOImpl<Parcela>{
    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ParcelaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Parcela t) {
         boolean isSuccess = false;
        try {
            sql = "insert into parcelas (parcelaNumero,taxaJuros,valorParcela,"
                    + "dataVencimento,fk_factura,fk_usuario,taxaDeAtraso)"
                    + " value (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, t.getParcelaNumero());
            ps.setDouble(2, t.getTaxaJuros());
            ps.setDouble(3, t.getValorParcela());
            ps.setString(4, t.getDataVencimento());
            ps.setString(5, t.getFactura().getNumero_fac());
            ps.setInt(6, t.getUsuario().getId());
            ps.setDouble(7, t.getTaxaDeAtraso());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Parcela> getEntities(String fk_numero) {
        ObservableList<Parcela> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from parcelas where fk_factura=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, fk_numero);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Parcela p = new Parcela();
                p.setId(result.getInt("id"));
                p.setParcelaNumero(result.getInt("parcelaNumero"));
                p.setTaxaJuros(result.getDouble("taxaJuros"));
                p.setTaxaDeAtraso(result.getDouble("taxaDeAtraso"));
                p.setValorParcela(result.getDouble("valorParcela"));
                p.setValorPagoParcela(result.getDouble("valorPagoParcela"));
                p.setDataVencimento(result.getString("dataVencimento"));
                p.setDataPagamento(result.getString("dataPagamento"));
                p.setEstado(result.getString("estado"));
                lista.add(p);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog(FontAwesomeIcon.TIMES, "Erro de consulra", "Detalhes: " + ex);
        }
        return lista;
    }
    
    
   
}
