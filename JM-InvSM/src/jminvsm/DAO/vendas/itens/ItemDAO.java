/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.vendas.itens;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jminvsm.fiscal.service.FiscalSnapshotCalculator;
import jminvsm.fiscal.service.FiscalSnapshotCalculator.LineSnapshot;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.preco.PrecoProdutoArmazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class ItemDAO implements ItemDAOImpl<Item> {

    @Override
    public void addEntity(List<Item> items) {
        String sql = "insert into itemfactura (quantidade, subtotal, fk_id_produto, fk_numero,"
                + "fk_id_usuario, fk_id_armazem, qtd_por_unidade, numero_linha, unidade_vendida,"
                + "preco_unitario, desconto_linha, codigo_imposto, taxa_imposto, motivo_isencao,"
                + "valor_liquido, base_tributavel, valor_imposto, valor_bruto, total_linha)"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            int numeroLinha = 1;
            for (Item t : items) {
                LineSnapshot fiscal = FiscalSnapshotCalculator.line(t, numeroLinha++);

                ps.setInt(1, t.getQuantidade());// ***********
                ps.setDouble(2, t.getSubtotal());// ***********
                ps.setInt(3, t.getProduto().getId());// ***********
                System.out.println("Id do produto teste: " + t.getProduto().getId());
                ps.setString(4, t.getFactura().getNumero_fac());// ***********
                ps.setInt(5, t.getUsuario().getId());// ***********
                ps.setInt(6, t.getArmazem().getId());// ***********
                ps.setInt(7, t.getQtd_por_unidade());
                ps.setInt(8, fiscal.getLineNumber());
                ps.setString(9, fiscal.getUnit());
                ps.setBigDecimal(10, fiscal.getUnitPrice());
                ps.setBigDecimal(11, fiscal.getDiscount());
                ps.setString(12, fiscal.getTaxCode());
                ps.setBigDecimal(13, fiscal.getTaxPercentage());
                ps.setString(14, fiscal.getExemptionReason());
                ps.setBigDecimal(15, fiscal.getNetTotal());
                ps.setBigDecimal(16, fiscal.getTaxBase());
                ps.setBigDecimal(17, fiscal.getTaxAmount());
                ps.setBigDecimal(18, fiscal.getGrossTotal());
                ps.setBigDecimal(19, fiscal.getLineTotal());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Item: id: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void addEntityQuotation(List<Item> items) {
        String sql = "insert into itemcotacao (quantidade, subtotal,fk_id_produto,fk_numero,"
                + "fk_id_usuario, fk_id_armazem,qtd_por_unidade) values (?,?,?,?,?,?,?)";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            for (Item t : items) {
                ps.setInt(1, t.getQuantidade());// ***********
                ps.setDouble(2, t.getSubtotal());// ***********
                ps.setInt(3, t.getProduto().getId());// ***********
                ps.setString(4, t.getFactura().getNumero_fac());// ***********
                ps.setInt(5, t.getUsuario().getId());// ***********
                ps.setInt(6, t.getArmazem().getId());// ***********
                ps.setInt(7, t.getQtd_por_unidade());
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Item: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean converterCotacaoParaFactura(String numeroCotacao, String numeroFactura) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            showDialog("Erro", "Numero da cotacao nao informado.");
            return false;
        }
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            showDialog("Erro", "Numero da factura destino nao informado.");
            return false;
        }

        String sqlContaOrigem = "SELECT COUNT(*) AS total FROM itemcotacao WHERE fk_numero = ? AND deleteStatus = 0";
        String sqlContaDestino = "SELECT COUNT(*) AS total FROM itemfactura WHERE fk_numero = ? AND deleteStatus = 0";
        String sqlConverter = "INSERT INTO itemfactura "
                + "(quantidade, subtotal, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem, qtd_por_unidade) "
                + "SELECT quantidade, subtotal, fk_id_produto, ?, fk_id_usuario, fk_id_armazem, COALESCE(qtd_por_unidade, 1) "
                + "FROM itemcotacao WHERE fk_numero = ? AND deleteStatus = 0";

        try ( Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);
            try {
                if (countByNumero(con, sqlContaOrigem, numeroCotacao) == 0) {
                    throw new SQLException("A cotacao nao possui itens para converter.");
                }
                if (countByNumero(con, sqlContaDestino, numeroFactura) > 0) {
                    throw new SQLException("A factura destino ja possui itens registados.");
                }

                try ( PreparedStatement ps = con.prepareStatement(sqlConverter)) {
                    ps.setString(1, numeroFactura);
                    ps.setString(2, numeroCotacao);
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Nenhum item foi convertido.");
                    }
                }

                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                showDialog("Erro de conversao", "Detalhes: " + ex.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            showDialog("Erro de conversao", "Detalhes: " + ex);
        }
        return false;
    }

    @Override
    public List<Item> getItensCotacaoEditaveis(String numeroCotacao) {
        List<Item> lista = new ArrayList<>();
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            showDialog("Erro", "Numero da cotacao nao informado.");
            return lista;
        }

        String sql = "SELECT "
                + "i.id AS idItem, i.quantidade, i.subtotal, COALESCE(i.qtd_por_unidade, 1) AS qtd_por_unidade, "
                + "i.fk_numero, i.fk_id_usuario, "
                + "p.id AS idProduto, p.nome AS nomeProduto, p.descricao AS descricaoProduto, "
                + "p.tipoProduto, p.unidadePorTipo, "
                + "a.id AS idArmazem, a.nome AS nomeArmazem, "
                + "um.id AS idUnidade, um.nome AS nomeUnidade, um.sigla AS siglaUnidade, "
                + "ip.id AS idImposto, ip.nome AS nomeImposto, ip.percentagem AS percentagemImposto, "
                + "ppa.id AS idPreco, ppa.precoBase, ppa.precoVenda, ppa.precoFinal, ppa.estado AS estadoPreco "
                + "FROM itemcotacao i "
                + "JOIN produto p ON p.id = i.fk_id_produto "
                + "JOIN armazem a ON a.id = i.fk_id_armazem "
                + "LEFT JOIN unidadeMedida um ON um.id = p.idUnidade "
                + "LEFT JOIN imposto ip ON ip.id = p.idImposto "
                + "LEFT JOIN precoProdutoArmazem ppa ON ppa.idProduto = i.fk_id_produto "
                + "AND ppa.idArmazem = i.fk_id_armazem AND ppa.estado = 'activo' "
                + "WHERE i.fk_numero = ? AND i.deleteStatus = 0 "
                + "ORDER BY i.id";

        try ( Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCotacao);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapItemCotacaoEditavel(rs));
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de consulta", "Detalhes: " + ex);
        }
        return lista;
    }

    @Override
    public boolean substituirItensCotacao(String numeroCotacao, List<Item> itens) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            showDialog("Erro", "Numero da cotacao nao informado.");
            return false;
        }
        if (itens == null || itens.isEmpty()) {
            showDialog("Erro", "A cotacao deve possuir pelo menos um item.");
            return false;
        }

        String sqlExisteCotacao = "SELECT COUNT(*) AS total FROM factura WHERE numero = ?";
        String sqlDeleteItens = "UPDATE itemcotacao SET deleteStatus=1, deletedAt=NOW(), deletedBy=? "
                + "WHERE fk_numero=? AND deleteStatus=0";
        String sqlInsertItem = "INSERT INTO itemcotacao "
                + "(quantidade, subtotal, fk_id_produto, fk_numero, fk_id_usuario, fk_id_armazem, qtd_por_unidade) "
                + "VALUES (?,?,?,?,?,?,?)";
        String sqlUpdateFactura = "UPDATE factura SET total = ?, saldoPendente = ? WHERE numero = ?";

        try ( Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);
            try {
                if (countByNumero(con, sqlExisteCotacao, numeroCotacao) == 0) {
                    throw new SQLException("Cotacao nao encontrada.");
                }

                BigDecimal total = BigDecimal.ZERO;
                try ( PreparedStatement ps = con.prepareStatement(sqlDeleteItens)) {
                    Integer idUsuario = jminvsm.util.AuditoriaContext.getUsuarioActualId();
                    if (idUsuario == null) {
                        ps.setNull(1, java.sql.Types.INTEGER);
                    } else {
                        ps.setInt(1, idUsuario);
                    }
                    ps.setString(2, numeroCotacao);
                    ps.executeUpdate();
                }

                try ( PreparedStatement ps = con.prepareStatement(sqlInsertItem)) {
                    for (Item item : itens) {
                        validarItemCotacao(item);
                        BigDecimal subtotal = valorSeguro(item.getSubtotalDecimal());
                        total = total.add(subtotal);
                        ps.setInt(1, item.getQuantidade());
                        ps.setBigDecimal(2, subtotal);
                        ps.setInt(3, item.getProduto().getId());
                        ps.setString(4, numeroCotacao);
                        ps.setInt(5, item.getUsuario().getId());
                        ps.setInt(6, item.getArmazem().getId());
                        ps.setInt(7, item.getQtd_por_unidade() <= 0 ? 1 : item.getQtd_por_unidade());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                try ( PreparedStatement ps = con.prepareStatement(sqlUpdateFactura)) {
                    ps.setBigDecimal(1, total);
                    ps.setBigDecimal(2, total);
                    ps.setString(3, numeroCotacao);
                    ps.executeUpdate();
                }

                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                showDialog("Erro ao guardar cotacao", "Detalhes: " + ex.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            showDialog("Erro ao guardar cotacao", "Detalhes: " + ex);
        }
        return false;
    }

    public List<Item> getItensData(String factura) {
        List<Item> lista = new ArrayList<>();
        Item i = null;
        String sql = "SELECT "
                + "    it.id AS item_id,"
                + "    it.quantidade AS item_quantidade,"
                + "    it.qtd_por_unidade AS item_qtd_por_unidade,"
                + "    (it.quantidade * COALESCE(it.qtd_por_unidade, 1)) AS item_quantidade_total_unidades,"
                + "    it.subtotal AS item_subtotal,"
                + "    it.data_adicao AS item_data_adicao,"
                
                + "    p.id AS produto_id,"
                + "    p.nome AS produto_nome,"
                + "    p.codigo_barra AS produto_codigo_barra,"
                + "    p.tipoProduto AS produto_tipo,"
                + "    p.descricao AS produto_descricao,"
                
                + "    a.id AS armazem_id,"
                + "    a.nome AS armazem_nome,"
                + "    a.tipo AS armazem_tipo,"
                + "    a.descricao AS armazem_descricao,"
                
                + "    imp.id AS imposto_id,"
                + "    imp.nome AS imposto_nome,"
                + "    imp.percentagem AS imposto_percentagem,"
                + "    imp.descricao AS imposto_descricao,"
                
                + "    ppa.id AS preco_id,"
                + "    ppa.precoBase AS preco_base,"
                + "    ppa.precoVenda AS preco_venda,"
                + "    ppa.precoFinal AS preco_final,"
                + "    ppa.dataInicioVigencia AS preco_data_inicio_vigencia,"
                + "    ppa.dataFimVigencia AS preco_data_fim_vigencia,"
                + "    ppa.estado AS preco_estado,"
                
                + "    d.id AS desconto_id,"
                + "    d.nome AS desconto_nome,"
                + "    d.percentagem AS desconto_percentagem,"
                + "    d.descricao AS desconto_descricao,"
                + "    pd.dataInicio AS desconto_data_inicio,"
                + "    pd.dataFim AS desconto_data_fim,"
                + "    pd.estado AS desconto_estado "
                
                + "FROM factura f "
                + "JOIN itemfactura it "
                + "    ON CONVERT(it.fk_numero USING utf8mb4) COLLATE utf8mb4_unicode_ci "
                + "     = CONVERT(f.numero USING utf8mb4) COLLATE utf8mb4_unicode_ci "
                + "JOIN produto p "
                + "    ON p.id = it.fk_id_produto "
                + "JOIN armazem a "
                + "    ON a.id = it.fk_id_armazem "
                + "LEFT JOIN imposto imp "
                + "    ON imp.id = p.idImposto "
                + "LEFT JOIN precoProdutoArmazem ppa "
                + "    ON ppa.id = ("
                + "        SELECT ppa2.id "
                + "        FROM precoProdutoArmazem ppa2 "
                + "        WHERE ppa2.idProduto = it.fk_id_produto "
                + "          AND ppa2.idArmazem = it.fk_id_armazem "
                + "          AND it.data_adicao BETWEEN ppa2.dataInicioVigencia AND ppa2.dataFimVigencia IS NULL "
                + "        ORDER BY ppa2.dataInicioVigencia DESC, ppa2.id DESC "
                + "        LIMIT 1) "
                + "LEFT JOIN produtodesconto pd "
                + "    ON pd.id = ("
                + "        SELECT pd2.id "
                + "        FROM produtodesconto pd2 "
                + "        WHERE pd2.idProduto = it.fk_id_produto "
                + "          AND pd2.idArmazem = it.fk_id_armazem "
                + "          AND pd2.estado = TRUE "
                + "          AND f.data_emissao BETWEEN pd2.dataInicio AND pd2.dataFim "
                + "        ORDER BY pd2.dataInicio DESC, pd2.id DESC "
                + "        LIMIT 1) "
                + "LEFT JOIN desconto d "
                + "    ON d.id = pd.idDesconto "
                + "WHERE CONVERT(f.numero USING utf8mb4) COLLATE utf8mb4_unicode_ci "
                + "    = CONVERT(? USING utf8mb4) COLLATE utf8mb4_unicode_ci "
                + "ORDER BY it.id;";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, factura);
            try ( ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    i = new Item();
                    i.setId(result.getInt("item_id"));
                    i.setQuantidade(result.getInt("item_quantidade"));
                    i.setQtd_por_unidade(result.getInt("item_qtd_por_unidade"));
                    i.setSubtotal(result.getDouble("item_subtotal"));
                    i.setData_criacao(result.getString("item_data_adicao"));
                    
                    Produto p = new Produto();
                    p.setId(result.getInt("produto_id"));
                    p.setNome(result.getString("produto_nome"));
                    p.setCodigo_barra(result.getString("produto_codigo_barra"));
                    p.setTipoProduto(result.getString("produto_tipo"));
                    p.setDescricao(result.getString("produto_descricao"));
                    
                    Armazem a = new Armazem();
                    a.setId(result.getInt("armazem_id"));
                    a.setNome_arm(result.getString("armazem_nome"));
                    a.setTipo(result.getString("armazem_tipo"));
                    a.setDescricao_arm(result.getString("armazem_descricao"));
                    
                    Imposto t = new Imposto();
                    t.setId(result.getInt("imposto_id"));
                    t.setNome(result.getString("imposto_nome"));
                    t.setPercentagem(result.getDouble("imposto_percentagem"));
                    t.setDescricao(result.getString("imposto_descricao"));
                    
                    PrecoProdutoArmazem ppa = new PrecoProdutoArmazem();
                    ppa.setId(result.getInt("preco_id"));
                    ppa.setPrecoBase(result.getDouble("preco_base"));
                    ppa.setPrecoVenda(result.getDouble("preco_venda"));
                    ppa.setPrecoFinal(result.getDouble("preco_final"));
                    ppa.setDataInicioVigencia(result.getString("preco_data_inicio_vigencia"));
                    ppa.setDataFimVigencia(result.getString("preco_data_fim_vigencia"));
                    ppa.setEstado(result.getString("preco_estado"));
                    
                    Desconto d = new Desconto();
                    d.setId(result.getInt("desconto_id"));
                    d.setNome(result.getString("desconto_nome"));
                    d.setPercentagem(result.getDouble("desconto_percentagem"));
                    d.setDescricao(result.getString("desconto_descricao"));
                    
                    ProdutoDesconto pd = new ProdutoDesconto();
                    pd.setDataInicio(result.getString("desconto_data_inicio"));
                    pd.setDataFim(result.getString("desconto_data_fim"));
                    pd.setEstado(result.getBoolean("desconto_estado"));
                    pd.setArmazem(a);
                    pd.setDesconto(d);
                    
                    
                    i.setArmazem(a);
                    p.setImposto(t);
                    p.setDescontoProduto(pd);
                    i.setProduto(p);
                    i.setPrecoProdutoArmazem(ppa);

                    lista.add(i);
                }
            }
        } catch (SQLException e) {
            showDialog("Erro", "Detalhes: " + e);
        }
        return lista;
    }

    private int countByNumero(Connection con, String sql, String numero) throws SQLException {
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numero);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    private Item mapItemCotacaoEditavel(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("idProduto"));
        produto.setNome(rs.getString("nomeProduto"));
        produto.setDescricao(rs.getString("descricaoProduto"));
        produto.setTipoProduto(rs.getString("tipoProduto"));
        produto.setUnidadesPorTipo(rs.getInt("unidadePorTipo"));

        UnidadeMedida unidade = new UnidadeMedida();
        unidade.setId(rs.getInt("idUnidade"));
        unidade.setNome(rs.getString("nomeUnidade"));
        unidade.setSigla(rs.getString("siglaUnidade"));
        produto.setUnidadeMedida(unidade);

        Imposto imposto = new Imposto();
        imposto.setId(rs.getInt("idImposto"));
        imposto.setNome(rs.getString("nomeImposto"));
        imposto.setPercentagem(rs.getDouble("percentagemImposto"));
        produto.setImposto(imposto);

        Armazem armazem = new Armazem();
        armazem.setId(rs.getInt("idArmazem"));
        armazem.setNome_arm(rs.getString("nomeArmazem"));

        PrecoProdutoArmazem preco = new PrecoProdutoArmazem();
        preco.setId(rs.getInt("idPreco"));
        preco.setProduto(produto);
        preco.setArmazem(armazem);
        preco.setPrecoBase(valorSeguro(rs.getBigDecimal("precoBase")));
        preco.setPrecoVenda(valorSeguro(rs.getBigDecimal("precoVenda")));
        preco.setPrecoFinal(valorSeguro(rs.getBigDecimal("precoFinal")));
        preco.setEstado(rs.getString("estadoPreco"));

        Factura factura = new Factura();
        factura.setNumero_fac(rs.getString("fk_numero"));

        Item item = new Item();
        item.setId(rs.getInt("idItem"));
        item.setRef(produto.getId() + "#" + armazem.getId());
        item.setQuantidade(rs.getInt("quantidade"));
        item.setSubtotal(valorSeguro(rs.getBigDecimal("subtotal")));
        item.setQtd_por_unidade(rs.getInt("qtd_por_unidade"));
        item.setUnidade_vendida(produto.getTipoProduto());
        item.setProduto(produto);
        item.setPrecoProdutoArmazem(preco);
        item.setArmazem(armazem);
        item.setFactura(factura);
        return item;
    }

    private void validarItemCotacao(Item item) throws SQLException {
        if (item == null) {
            throw new SQLException("Existe item invalido na cotacao.");
        }
        if (item.getProduto() == null || item.getProduto().getId() <= 0) {
            throw new SQLException("Existe item sem produto valido.");
        }
        if (item.getArmazem() == null || item.getArmazem().getId() <= 0) {
            throw new SQLException("Existe item sem armazem valido.");
        }
        if (item.getUsuario() == null || item.getUsuario().getId() <= 0) {
            throw new SQLException("Usuario responsavel nao identificado.");
        }
        if (item.getQuantidade() <= 0) {
            throw new SQLException("A quantidade dos itens deve ser maior que zero.");
        }
    }

    private BigDecimal valorSeguro(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

//
//    @Override
//    public void updateEntityByID(Item t, int id) {
//        try {
//            sql = "update item_factura set tipo=?,quantidade=?,preco_unitario=?,"
//                    + "subtotal=?,fk_id_desconto=?,fk_id_produto=?,fk_id_factura=?,"
//                    + "fk_id_servico=?,fk_id_usuario=?"
//                    + " where id=?";
//            ps = con.prepareStatement(sql);
//
////            ps.setString(1, t.getTipo());
////            ps.setDouble(2, t.getQuantidade());
////            ps.setDouble(3, t.getPreco_unitario());
////            ps.setDouble(4, t.getSubtotal());
////            ps.setInt(5, t.getFk_id_desconto());
////            ps.setInt(6, t.getFk_id_produto());
////            ps.setInt(7, t.getFk_id_factura());
////            ps.setInt(8, t.getFk_id_servico());
////            ps.setInt(9, t.getFk_id_usuario());
//            ps.setInt(10, id);
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar  Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public List<Item> listAllEntities() {
//        List<Item> lista = new ArrayList<>();
//        try {
//            sql = "select * from item_factura";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Item u = new Item();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
////                u.setQuantidade(result.getLong("quantidade"));
////                u.setPreco_unitario(result.getDouble("preco_unitario"));
////                u.setSubtotal(result.getDouble("subtotal"));
////                u.setFk_id_desconto(result.getInt("fk_id_desconto"));
////                u.setFk_id_produto(result.getInt("fk_id_produto"));
////                u.setFk_id_factura(result.getInt("fk_id_factura"));
////                u.setFk_id_servico(result.getInt("fk_id_servico"));
////                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
////                u.setData_criacao(result.getString("data_criacao"));
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
//    public Item searchEntityByName(String number) {
//        Item u = null;
//        try {
//            sql = "select * from item_factura where fk_id_factura like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + number + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                u = new Item();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
////                u.setQuantidade(result.getLong("quantidade"));
////                u.setPreco_unitario(result.getDouble("preco_unitario"));
////                u.setSubtotal(result.getDouble("subtotal"));
////                u.setFk_id_desconto(result.getInt("fk_id_desconto"));
////                u.setFk_id_produto(result.getInt("fk_id_produto"));
////                u.setFk_id_factura(result.getInt("fk_id_factura"));
////                u.setFk_id_servico(result.getInt("fk_id_servico"));
////                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
////                u.setData_criacao(result.getString("data_criacao"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Item: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return u;
//
//    }
//
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from item_factura where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public void deleteEntityByUSER(int id) {
//        try {
//            sql = "update item_factura set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Item: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
