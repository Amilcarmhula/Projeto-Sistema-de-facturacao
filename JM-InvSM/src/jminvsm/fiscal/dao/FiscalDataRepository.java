package jminvsm.fiscal.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jminvsm.fiscal.model.FiscalDocument;
import jminvsm.util.Conexao;

public class FiscalDataRepository {

    public List<FiscalDocument> findDocuments(int companyId, LocalDate start, LocalDate end)
            throws SQLException {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Período fiscal inválido.");
        }

        Map<String, FiscalDocument> documents = new LinkedHashMap<String, FiscalDocument>();
        try (Connection connection = Conexao.getConexao()) {
            loadHeaders(connection, companyId, start, end, documents);
            loadLines(connection, companyId, start, end, documents);
        }
        return new ArrayList<FiscalDocument>(documents.values());
    }

    private void loadHeaders(Connection connection, int companyId, LocalDate start, LocalDate end,
            Map<String, FiscalDocument> documents) throws SQLException {
        String sql = "SELECT v.*, "
                + "(SELECT CONCAT_WS(', ', ec.avenida, ec.rua, ec.numero, ec.bairro, ec.cidade, ec.provincia) "
                + " FROM endereco_cliente ec WHERE ec.fk_id_cliente=v.fk_id_cliente "
                + " AND COALESCE(ec.deleteStatus,0)=0 ORDER BY ec.id LIMIT 1) AS endereco_cliente "
                + "FROM vw_fiscal_facturas v "
                + "WHERE v.fk_id_empresa=? AND v.data_emissao BETWEEN ? AND ? "
                + "ORDER BY v.data_emissao, v.numero";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FiscalDocument d = new FiscalDocument();
                    d.setIssuerTaxId(rs.getString("nuit_emitente"));
                    d.setIssuerName(rs.getString("nome_emitente"));
                    d.setNumber(rs.getString("numero"));
                    d.setDocumentType(rs.getString("tipo_documento"));
                    d.setSeries(rs.getString("serie"));
                    Date issueDate = rs.getDate("data_emissao");
                    if (issueDate != null) d.setIssueDate(issueDate.toLocalDate());
                    Timestamp entryDate = rs.getTimestamp("data_hora_registo");
                    if (entryDate != null) d.setSystemEntryDate(entryDate.toLocalDateTime());
                    d.setCustomerTaxId(rs.getString("nuit_cliente"));
                    d.setCustomerName(rs.getString("nome_cliente"));
                    d.setCustomerAddress(rs.getString("endereco_cliente"));
                    d.setCurrencyCode(rs.getString("moeda"));
                    d.setCurrencyAmount(rs.getBigDecimal("taxa_cambio"));
                    d.setStatus(normalizeStatus(rs.getString("estado")));
                    d.setNetTotal(rs.getBigDecimal("total_liquido"));
                    d.setTaxPayable(rs.getBigDecimal("total_imposto"));
                    d.setGrossTotal(rs.getBigDecimal("total"));
                    d.setSourceDocument(rs.getString("documento_origem"));
                    d.setCancellationReason(rs.getString("motivo_anulacao"));
                    d.setDocumentHash(rs.getString("hash_documento"));
                    documents.put(d.getNumber(), d);
                }
            }
        }
    }

    private void loadLines(Connection connection, int companyId, LocalDate start, LocalDate end,
            Map<String, FiscalDocument> documents) throws SQLException {
        String sql = "SELECT i.fk_numero, COALESCE(i.numero_linha, i.id) AS numero_linha, "
                + "CAST(p.id AS CHAR) AS codigo_produto, p.nome, i.quantidade, "
                + "COALESCE(i.unidade_vendida, um.sigla, 'UN') AS unidade, "
                + "i.preco_unitario, i.desconto_linha, i.codigo_imposto, i.taxa_imposto, "
                + "i.motivo_isencao, i.valor_liquido, i.base_tributavel, i.valor_imposto, "
                + "i.valor_bruto, i.total_linha, i.subtotal "
                + "FROM itemfactura i "
                + "JOIN factura f ON f.numero=i.fk_numero "
                + "JOIN produto p ON p.id=i.fk_id_produto "
                + "LEFT JOIN unidadeMedida um ON um.id=p.idUnidade "
                + "WHERE f.fk_id_empresa=? AND f.data_emissao BETWEEN ? AND ? "
                + "AND COALESCE(f.deleteStatus,0)=0 AND COALESCE(i.deleteStatus,0)=0 "
                + "ORDER BY i.fk_numero, numero_linha, i.id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FiscalDocument document = documents.get(rs.getString("fk_numero"));
                    if (document == null) continue;
                    FiscalDocument.Line line = new FiscalDocument.Line();
                    line.setLineNumber(rs.getInt("numero_linha"));
                    line.setProductCode(rs.getString("codigo_produto"));
                    line.setDescription(rs.getString("nome"));
                    line.setQuantity(rs.getBigDecimal("quantidade"));
                    line.setUnitOfMeasure(rs.getString("unidade"));
                    line.setUnitPrice(rs.getBigDecimal("preco_unitario"));
                    line.setLineDiscount(rs.getBigDecimal("desconto_linha"));
                    line.setTaxCode(rs.getString("codigo_imposto"));
                    line.setTaxPercentage(rs.getBigDecimal("taxa_imposto"));
                    line.setTaxExemptionReason(rs.getString("motivo_isencao"));
                    line.setNetTotal(rs.getBigDecimal("valor_liquido"));
                    line.setTaxBase(rs.getBigDecimal("base_tributavel"));
                    line.setTaxAmount(rs.getBigDecimal("valor_imposto"));
                    line.setGrossTotal(rs.getBigDecimal("valor_bruto"));
                    BigDecimal totalLine = rs.getBigDecimal("total_linha");
                    line.setLineTotal(totalLine != null ? totalLine : rs.getBigDecimal("subtotal"));
                    document.addLine(line);
                }
            }
        }
    }

    private String normalizeStatus(String status) {
        if (status == null) return "N";
        String value = status.trim().toLowerCase();
        return value.contains("cancel") || value.contains("anulad") ? "A" : "N";
    }
}

