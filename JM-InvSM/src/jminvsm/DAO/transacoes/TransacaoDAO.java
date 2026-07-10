/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.transacoes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.parcelas.Parcela;
import jminvsm.model.transacao.Transacao;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class TransacaoDAO implements TransacaoDAOImpl<Transacao> {


    @Override
    public boolean addTansacaoAvista(Transacao t) {
        boolean isSuccess = false;
        String sql = "insert into transacoes (data_pagamento,valorRecebido,tipo_transacao,"
                    + "descricao,fk_id_contabancaria,"
                    + "fk_id_metodo_pagamento, fk_numero, fk_id_usuario, valorPago, trocos) values (?,?,?,?,?,?,?,?,?,?)";
        try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getData_pagamento());
            ps.setBigDecimal(2, t.getValorRecebidoDecimal());
            ps.setString(3, t.getTipo_transacao());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getContabancaria().getId());
            ps.setInt(6, t.getMetodo().getId());
            ps.setString(7, t.getFactura().getNumero_fac());
            ps.setInt(8, t.getUsuario().getId());
            ps.setBigDecimal(9, t.getValorPagoDecimal());
            ps.setBigDecimal(10, t.getTrocosDecimal());
            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserÃ§Ã£o foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: "+ex+" Data: "+t.getData_pagamento());
            showDialog( "Erro persistencia", "Detalhes: " + ex);
        }
        return isSuccess;
    }
    
    public boolean addTansacaoParcelada(Transacao t) {
        boolean isSuccess = false;
            String sql = "insert into transacoes (data_pagamento,valorRecebido,tipo_transacao,"
                    + "descricao,fk_id_contabancaria,"
                    + "fk_id_metodo_pagamento, fk_numero, fk_id_usuario, valorPago, trocos,fk_parcela) value (?,?,?,?,?,?,?,?,?,?,?)";
            try (
                Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getData_pagamento());
            ps.setBigDecimal(2, t.getValorRecebidoDecimal());
            ps.setString(3, t.getTipo_transacao());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getContabancaria().getId());
            ps.setInt(6, t.getMetodo().getId());
            ps.setString(7, t.getFactura().getNumero_fac());
            ps.setInt(8, t.getUsuario().getId());
            ps.setBigDecimal(9, t.getValorPagoDecimal());
            ps.setBigDecimal(10, t.getTrocosDecimal());
            ps.setInt(11, t.getParcela().getId_parcela());
            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserÃ§Ã£o foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: "+ex+" Data: "+t.getData_pagamento());
            showDialog( "Erro persistencia", "Detalhes: " + ex);
        } 
        return isSuccess;
    }
    

    @Override
    public ObservableList<Transacao> getTransacoes(String numero) {
        ObservableList<Transacao> lista = FXCollections.observableArrayList();
        String sql = "SELECT  t.fk_parcela as refParcela,  t.id, t.valorRecebido, t.valorPago, t.trocos, t.data_pagamento, t.data_criacao, t.descricao, m.nome "
                    + "	from transacoes t "
                    + "	LEFT JOIN metodo_pagamento m ON m.id=t.fk_id_metodo_pagamento "
                    + " where fk_numero=? order by fk_parcela";
            try (
                Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
                                ps.setString(1, numero);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                Transacao t = new Transacao();
                t.setId(result.getInt("id"));
                t.setValorRecebido(result.getBigDecimal("valorRecebido"));
                t.setValorPago(result.getBigDecimal("valorPago"));
                t.setTrocos(result.getBigDecimal("trocos"));
                t.setData_pagamento(result.getString("data_pagamento"));
                t.setData_criacao(result.getString("data_criacao"));
                t.setDescricao(result.getString("descricao"));
                MetodoPagamento m = new MetodoPagamento();
                m.setNomePagamento(result.getString("nome"));
                t.setMetodo(m);
                Parcela p = new Parcela();
                p.setId_parcela(result.getInt("refParcela"));
                t.setParcela(p);
                lista.add(t);
            }
            }
        } catch (SQLException e) {
            showDialog("Erro de consulta", "Detalhes: " + e);
        }
        return lista;
    }

}
