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
package jminvsm.dao.precoProdutoArmazem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class PrecoProdutoArmazemDAO {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public PrecoProdutoArmazemDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    public boolean addEntity(PrecoProdutoArmazem x) {
        boolean isSuccess = false;
        try {
            sql = "CALL sp_registar_preco(?, ?, ?) ";

            ps = con.prepareStatement(sql);
            ps.setInt(1, x.getProduto().getId());
            ps.setInt(2, x.getArmazem().getId());
//            ps.setDouble(3, x.getPreco());
            if (x.getPrecoBase() != null) {
                ps.setDouble(3, x.getPrecoBase());
            } else {
                ps.setNull(3, Types.DOUBLE);
            }

            int rows = ps.executeUpdate();
            isSuccess = rows > 0;

        } catch (SQLException ex) {
            System.out.println("Erro ao inserir: " + ex);
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

    public ObservableList<PrecoProdutoArmazem> getEntityByID(int idProduto, int idArmazem) {
        ObservableList<PrecoProdutoArmazem> lista = FXCollections.observableArrayList();
        PrecoProdutoArmazem pv = null;
        try {
            sql = "select * from precoProdutoArmazem where idProduto=? and idArmazem=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProduto);
            ps.setInt(2, idArmazem);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                //melhorar esta funcao
                pv = new PrecoProdutoArmazem();
                pv.setId(result.getInt("id"));
//                pv.setPrecoNormal(result.getDouble("precoNormal"));
                pv.setPrecoVenda(result.getDouble("preco"));
                pv.setPrecoFinal(result.getDouble("precoFinal"));
                pv.setDataFimVigencia(result.getString("dataValidade"));
//                pv.setEstado(result.getString("estado"));
                Produto p = new Produto();
                p.setId(result.getInt("idProduto"));
                pv.setProduto(p);
                Armazem a = new Armazem();
                a.setId(result.getInt("idArmazem"));
                pv.setArmazem(a);
                lista.add(pv);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog("Erro de consulta!", "Detalhes: " + e);
        }
        return lista;
    }

    public ObservableList<PrecoProdutoArmazem> listAllPrecos(int idProd) {
        ObservableList<PrecoProdutoArmazem> listaPrecos = FXCollections.observableArrayList();
        PrecoProdutoArmazem pr = null;
        try {
            sql = "SELECT pr.id, pr.idProduto, pr.idArmazem, pr.precoBase, pr.precoVenda, "
                    + " pr.precoFinal, pr.dataInicioVigencia, pr.dataFimVigencia, pr.estado "
                    + " FROM precoProdutoArmazem pr "
                    + " LEFT JOIN produto p ON p.id = pr.idProduto "
                    + "where p.id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProd);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                pr = new PrecoProdutoArmazem();
                pr.setId(result.getInt("id"));
                Produto produto = new Produto();
                produto.setId(result.getInt("idProduto"));
                pr.setProduto(produto);
                Armazem a = new Armazem();
                a.setId(result.getInt("idArmazem"));
                pr.setArmazem(a);
                pr.setPrecoVenda(result.getDouble("precoVenda"));
                pr.setPrecoFinal(result.getDouble("precoFinal"));
                pr.setPrecoBase(result.getDouble("precoBase"));
                pr.setDataInicioVigencia(result.getString("dataInicioVigencia"));
                pr.setDataFimVigencia(result.getString("dataFimVigencia"));
                pr.setEstado(result.getString("estado"));
                listaPrecos.add(pr);
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }

        return listaPrecos;
    }

    public boolean deleteEntityByUSER(int id) {
        boolean isSuccess = false;
        try {
            sql = "update precoProdutoArmazem set estado='desactivo' where id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog("Erro persistencia", "Detalhes: " + ex);
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

    public static void main(String[] args) throws SQLException {
        PrecoProdutoArmazemDAO dao = new PrecoProdutoArmazemDAO();

        PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
//        ppa.setPreco(200.0);
//        Armazem a = new Armazem();
//        a.setId(1);
//        ppa.setArmazem(a);
//        Produto p = new Produto();
//        p.setId(6);
//        ppa.setProduto(p);
//
//        dao.addEntity(ppa);


    }
}
