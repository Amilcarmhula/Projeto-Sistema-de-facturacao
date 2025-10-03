/*
 * The MIT License
 *
 * Copyright 2025 JM-Tecnologias.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jminvsm.dao.desconto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.produto.Produto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoDescontoDAO {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public ProdutoDescontoDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    // CREATE
    public boolean addEntity(ProdutoDesconto t) {
        boolean isSuccess = false;
        try {
            sql = "INSERT INTO produtodesconto (idProduto, idDesconto, dataInicio, dataFim, percentagem, idArmazem) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setInt(1, t.getProduto().getId());
            ps.setInt(2, t.getDesconto().getId());
            ps.setString(3, t.getDataInicio());
            ps.setString(4, t.getDataFim());
            ps.setInt(6, t.getArmazem().getId());
            if (t.getPercentagem() != null) {
                ps.setDouble(5, t.getPercentagem());
            } else {
                ps.setNull(5, Types.DOUBLE);
            }

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao inserir ProdutoDesconto: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return isSuccess;
    }

    // READ ALL
    public ObservableList<ProdutoDesconto> getEntityByID(int idProduto, int idArmazem) {
        ObservableList<ProdutoDesconto> lista = FXCollections.observableArrayList();
        ProdutoDesconto pd = null;
        try {
            sql = "select p.nome as nomeProduto, p.descricao as descricaoProduto,"
                    + "	a.nome as nomeArmazem, a.tipo as tipoArmazem, a.descricao as descicaoArmazem, "
                    + " d.nome as nomeDesconto, "
                    + " pd.dataInicio, pd.dataFim, pd.percentagem "
                    + "	from produtodesconto pd "
                    + "	join produto p on pd.idProduto=p.id "
                    + " join desconto d on pd.idDesconto=d.id "
                    + " join armazem a on pd.idArmazem=a.id "
                    + " where idProduto=? and idArmazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1,  idProduto);
            ps.setInt(2,  idArmazem);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pd = new ProdutoDesconto();
                Produto p = new Produto();
                p.setNome(rs.getString("nomeProduto"));
                p.setDescricao(rs.getString("descricaoProduto"));
                pd.setProduto(p);

                Armazem a = new Armazem();
                a.setNome_arm(rs.getString("nomeArmazem"));
                a.setDescricao_arm(rs.getString("descicaoArmazem"));
                a.setTipo(rs.getString("tipoArmazem"));
                pd.setArmazem(a);
                
                Desconto d = new Desconto();
                d.setNome(rs.getString("nomeDesconto"));
                pd.setDesconto(d);

                pd.setDataInicio(rs.getString("dataInicio"));
                pd.setDataFim(rs.getString("dataFim"));
                pd.setPercentagem(rs.getDouble("percentagem"));

                lista.add(pd);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            showDialog( "Erro de consulta!", "Detalhes: " + e);
        }
        return lista;
    }

    // DELETE
    public boolean delete(ProdutoDesconto t) {
        boolean isSuccess = false;
        try {
            sql = "DELETE FROM produtodesconto WHERE idProduto=? AND idDesconto=? AND dataInicio=? AND dataFim=? AND idArmazem=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, t.getProduto().getId());
            ps.setInt(2, t.getDesconto().getId());
            ps.setString(3, t.getDataInicio());
            ps.setString(4, t.getDataFim());
            ps.setInt(5, t.getArmazem().getId());

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao apagar ProdutoDesconto: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return isSuccess;
    }

    public static void main(String[] args) throws SQLException {
        ProdutoDescontoDAO dao = new ProdutoDescontoDAO();
        ProdutoDesconto pd = new ProdutoDesconto();
        Produto p = new Produto();
        p.setId(6);
        pd.setProduto(p);
        Armazem a = new Armazem();
        a.setId(2);
        pd.setArmazem(a);
        Desconto d = new Desconto();
        d.setId(2);
        pd.setDesconto(d);
        pd.setPercentagem(7.0);
        pd.setDataInicio("2025-09-03");
        pd.setDataFim("2025-09-21");
        dao.addEntity(pd);

//        for(ProdutoDesconto des: dao.listAll()){
//            System.out.println(des.getPercentagem());
//        }
//        dao.delete(pd);
//        for(ProdutoDesconto des: dao.listAll()){
//            System.out.println(des.getPercentagem());
//        }
    }
}
