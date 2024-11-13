/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.metodoPagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class MetodoPagamentoDAO implements MetodoImpl<MetodoPagamento>{
    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public MetodoPagamentoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
     @Override
    public ObservableList<MetodoPagamento> listAllEntities() {
        ObservableList<MetodoPagamento> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from metodo_pagamento";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                MetodoPagamento m = new MetodoPagamento();
                m.setId(result.getInt("id"));
                m.setNomePagamento(result.getString("nome"));
                m.setDescricao(result.getString("descricao"));
                m.setData_criacao(result.getString("data_criacao"));
                lista.add(m);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }
}
