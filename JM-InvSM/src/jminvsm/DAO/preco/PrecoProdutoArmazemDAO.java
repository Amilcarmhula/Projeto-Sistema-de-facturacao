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
package jminvsm.dao.preco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
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
            sql = "INSERT INTO precoProdutoArmazem (idProduto, idArmazem, preco) "
                    + "VALUES (?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setInt(1, x.getProduto().getId());
            ps.setInt(2, x.getArmazem().getId());
            if (x.getPreco() != null) {
                ps.setDouble(3, x.getPreco());
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
    
    public static void main(String[] args) throws SQLException {
        PrecoProdutoArmazemDAO dao = new PrecoProdutoArmazemDAO();
        
        PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
        ppa.setPreco(200.0);
        Armazem a = new Armazem();
        a.setId(1);
        ppa.setArmazem(a);
        Produto p = new Produto();
        p.setId(6);
        ppa.setProduto(p);
        
        dao.addEntity(ppa);
    }
}
