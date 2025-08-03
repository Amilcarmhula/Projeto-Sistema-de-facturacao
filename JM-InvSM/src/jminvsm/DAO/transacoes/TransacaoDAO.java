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

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public TransacaoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addTansacaoAvista(Transacao t) {
        boolean isSuccess = false;
        try {
            sql = "insert into transacoes (data_pagamento,valorRecebido,tipo_transacao,"
                    + "descricao,fk_id_contabancaria,"
                    + "fk_id_metodo_pagamento, fk_numero, fk_id_usuario, valorPago, trocos) value (?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getData_pagamento());
            ps.setDouble(2, t.getValorRecebido());
            ps.setString(3, t.getTipo_transacao());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getContabancaria().getId());
            ps.setInt(6, t.getMetodo().getId());
            ps.setString(7, t.getFactura().getNumero_fac());
            ps.setInt(8, t.getUsuario().getId());
            ps.setDouble(9, t.getValorPago());
            ps.setDouble(10, t.getTrocos());
            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: "+ex+" Data: "+t.getData_pagamento());
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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
    
    public boolean addTansacaoParcelada(Transacao t) {
        boolean isSuccess = false;
        try {
            sql = "insert into transacoes (data_pagamento,valorRecebido,tipo_transacao,"
                    + "descricao,fk_id_contabancaria,"
                    + "fk_id_metodo_pagamento, fk_numero, fk_id_usuario, valorPago, trocos,fk_parcela) value (?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getData_pagamento());
            ps.setDouble(2, t.getValorRecebido());
            ps.setString(3, t.getTipo_transacao());
            ps.setString(4, t.getDescricao());
            ps.setInt(5, t.getContabancaria().getId());
            ps.setInt(6, t.getMetodo().getId());
            ps.setString(7, t.getFactura().getNumero_fac());
            ps.setInt(8, t.getUsuario().getId());
            ps.setDouble(9, t.getValorPago());
            ps.setDouble(10, t.getTrocos());
            ps.setDouble(11, t.getParcela().getId_parcela());
            // Usando executeUpdate para verificar se uma linha foi inserida
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            System.out.println("Erro: "+ex+" Data: "+t.getData_pagamento());
            showDialog( "Erro persistencia", "Detalhes: " + ex);
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
    public ObservableList<Transacao> getTransacoes(String numero) {
        ObservableList<Transacao> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT  t.fk_parcela as refParcela,  t.id, t.valorRecebido, t.valorPago, t.trocos, t.data_pagamento, t.data_criacao, t.descricao, m.nome "
                    + "	from transacoes t "
                    + "	LEFT JOIN metodo_pagamento m ON m.id=t.fk_id_metodo_pagamento "
                    + " where fk_numero=? order by fk_parcela";
            ps = con.prepareStatement(sql);
            ps.setString(1, numero);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Transacao t = new Transacao();
                t.setId(result.getInt("id"));
                t.setValorRecebido(result.getDouble("valorRecebido"));
                t.setValorPago(result.getDouble("valorPago"));
                t.setTrocos(result.getDouble("trocos"));
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
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta", "Detalhes: " + e);
        }
        return lista;
    }

}
