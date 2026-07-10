/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.parcela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.parcelas.Parcela;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ParcelaDAO implements ParcelaDAOImpl<Parcela> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ParcelaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Parcela t) {
        boolean isSuccess = false;
        
            String sql = "insert into parcelas (parcelaNumero,taxaJuros,valorParcela,"
                    + "dataVencimento,fk_factura,fk_usuario)"
                    + " values (?,?,?,?,?,?)";
            try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getParcelaNumero());
            ps.setBigDecimal(2, t.getTaxaJurosDecimal());
            ps.setBigDecimal(3, t.getValorParcelaDecimal());
            ps.setString(4, t.getDataVencimento());
            ps.setString(5, t.getFactura().getNumero_fac());
            ps.setInt(6, t.getUsuario().getId());
//            ps.setDouble(7, t.getTaxaDeAtraso());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }

    @Override
    public ObservableList<Parcela> getEntities(String fk_numero) {
        ObservableList<Parcela> lista = FXCollections.observableArrayList();
        String 
            sql = "select p.id, p.parcelaNumero, p.taxaJuros, p.taxaDeAtraso, "
                    + "(p.valorParcela*p.taxaDeAtraso)+p.valorParcela as valorParcela, "
                    + "p.valorPagoParcela, p.dataVencimento, p.dataPagamento, p.estado, "
                    + "p.fk_factura, p.fk_usuario "
                    + "from parcelas p where fk_factura=?";
           try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
                               ps.setString(1, fk_numero);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                Parcela p = new Parcela();
                p.setId_parcela(result.getInt("id"));
                p.setParcelaNumero(result.getInt("parcelaNumero"));
                p.setTaxaJuros(result.getBigDecimal("taxaJuros"));
                p.setTaxaDeAtraso(result.getBigDecimal("taxaDeAtraso"));
                p.setValorParcela(result.getBigDecimal("valorParcela"));
                p.setValorPagoParcela(result.getBigDecimal("valorPagoParcela"));
                p.setDataVencimento(result.getString("dataVencimento"));
                p.setDataPagamento(result.getString("dataPagamento"));
                p.setEstado(result.getString("estado"));
                lista.add(p);
            }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulra", "Detalhes: " + ex);
        }
        return lista;
    }

}
