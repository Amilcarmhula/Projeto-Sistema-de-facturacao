/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.contacorrente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jminvsm.model.contacorrente.ContaCorrente;
import jminvsm.model.transacao.Transacao;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaCorrenteDAO implements ContaCorrenteDAOImpl<ContaCorrente>{
    private final Connection con;
    private PreparedStatement ps;
    private String sql;

    public ContaCorrenteDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public ContaCorrente getLastEntity() { 
        ContaCorrente c = null;
        try {
            sql = "SELECT id,saldo_inicial, saldo_atual,fk_id_transacoes FROM contacorrente ORDER BY id DESC LIMIT 1;";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                c = new ContaCorrente();
                c.setId(result.getInt("id"));
                c.setSaldo_inicial(result.getDouble("saldo_inicial"));
                c.setSaldo_actual(result.getDouble("saldo_atual"));
                Transacao t = new Transacao();
                t.setId(result.getInt("fk_id_transacoes"));
                c.setTransacao(t);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog( "Erro consulta", "Detalhes: " + e);
        }
        return c;
    }
}
