package jminvsm.fiscal.export;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jminvsm.fiscal.model.FiscalDocument;

/**
 * Escritor XLSX mínimo, sem dependências externas. O ficheiro contém apenas
 * valores finais (sem macros nem fórmulas), adequado para entrega fiscal.
 */
public class XlsxFiscalExporter {

    public void export(List<FiscalDocument> documents, Path path) throws IOException {
        Files.createDirectories(path.toAbsolutePath().getParent());
        try (OutputStream file = Files.newOutputStream(path);
                ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(file))) {
            put(zip, "[Content_Types].xml", contentTypes());
            put(zip, "_rels/.rels", rootRelationships());
            put(zip, "xl/workbook.xml", workbook());
            put(zip, "xl/_rels/workbook.xml.rels", workbookRelationships());
            put(zip, "xl/styles.xml", styles());
            put(zip, "xl/worksheets/sheet1.xml", worksheet(invoiceRows(documents), invoiceWidths()));
            put(zip, "xl/worksheets/sheet2.xml", worksheet(lineRows(documents), lineWidths()));
            put(zip, "xl/worksheets/sheet3.xml", worksheet(summaryRows(documents), summaryWidths()));
        }
    }

    private List<List<Object>> invoiceRows(List<FiscalDocument> documents) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        rows.add(row("VersaoModelo","NUITEmitente","NomeEmitente","NumeroDocumento",
                "TipoDocumento","Serie","DataEmissao","DataHoraRegisto","NUITCliente",
                "NomeCliente","EnderecoCliente","Moeda","TaxaCambio","Estado",
                "TotalLiquido","TotalImposto","TotalDocumento","DocumentoOrigem",
                "MotivoAnulacao","HashDocumento"));
        for (FiscalDocument d : documents) {
            rows.add(row(FiscalDocument.MODEL_VERSION, d.getIssuerTaxId(), d.getIssuerName(),
                    d.getNumber(), d.getDocumentType(), d.getSeries(),
                    d.getIssueDate() == null ? "" : d.getIssueDate().toString(),
                    d.getSystemEntryDate() == null ? "" : d.getSystemEntryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    d.getCustomerTaxId(), d.getCustomerName(), d.getCustomerAddress(),
                    d.getCurrencyCode(), d.getCurrencyAmount(), d.getStatus(), d.getNetTotal(),
                    d.getTaxPayable(), d.getGrossTotal(), d.getSourceDocument(),
                    d.getCancellationReason(), d.getDocumentHash()));
        }
        return rows;
    }

    private List<List<Object>> lineRows(List<FiscalDocument> documents) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        rows.add(row("NumeroDocumento","NumeroLinha","CodigoProduto","Descricao","Quantidade",
                "Unidade","PrecoUnitario","DescontoLinha","CodigoImposto","TaxaImposto",
                "MotivoIsencao","ValorLiquido","BaseTributavel","ValorImposto",
                "ValorBruto","TotalLinha"));
        for (FiscalDocument d : documents) {
            for (FiscalDocument.Line line : d.getLines()) {
                rows.add(row(d.getNumber(), line.getLineNumber(), line.getProductCode(),
                        line.getDescription(), line.getQuantity(), line.getUnitOfMeasure(),
                        line.getUnitPrice(), line.getLineDiscount(), line.getTaxCode(),
                        line.getTaxPercentage(), line.getTaxExemptionReason(), line.getNetTotal(),
                        line.getTaxBase(), line.getTaxAmount(), line.getGrossTotal(), line.getLineTotal()));
            }
        }
        return rows;
    }

    private List<List<Object>> summaryRows(List<FiscalDocument> documents) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        rows.add(row("CodigoImposto","Taxa","BaseTributavel","ValorImposto","ValorBruto"));
        Map<String, BigDecimal[]> totals = new TreeMap<String, BigDecimal[]>();
        for (FiscalDocument d : documents) {
            for (FiscalDocument.Line line : d.getLines()) {
                String key = text(line.getTaxCode()) + "|" + line.getTaxPercentage().toPlainString();
                BigDecimal[] values = totals.get(key);
                if (values == null) {
                    values = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
                    totals.put(key, values);
                }
                values[0] = values[0].add(line.getTaxBase());
                values[1] = values[1].add(line.getTaxAmount());
                values[2] = values[2].add(line.getLineTotal());
            }
        }
        for (Map.Entry<String, BigDecimal[]> entry : totals.entrySet()) {
            String[] key = entry.getKey().split("\\|", -1);
            rows.add(row(key[0], new BigDecimal(key[1]), entry.getValue()[0],
                    entry.getValue()[1], entry.getValue()[2]));
        }
        return rows;
    }

    private String worksheet(List<List<Object>> rows, double[] widths) {
        StringBuilder xml = new StringBuilder(16384);
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xml.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        xml.append("<sheetViews><sheetView workbookViewId=\"0\"><pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/></sheetView></sheetViews>");
        xml.append("<cols>");
        for (int i = 0; i < widths.length; i++) {
            xml.append("<col min=\"").append(i + 1).append("\" max=\"").append(i + 1)
                    .append("\" width=\"").append(widths[i]).append("\" customWidth=\"1\"/>");
        }
        xml.append("</cols><sheetData>");
        for (int r = 0; r < rows.size(); r++) {
            xml.append("<row r=\"").append(r + 1).append("\"");
            if (r == 0) xml.append(" ht=\"28\" customHeight=\"1\"");
            xml.append(">");
            List<Object> values = rows.get(r);
            for (int c = 0; c < values.size(); c++) {
                Object value = values.get(c);
                String ref = columnName(c + 1) + (r + 1);
                if (value instanceof Number) {
                    xml.append("<c r=\"").append(ref).append("\" s=\"")
                            .append(r == 0 ? 1 : 2).append("\"><v>")
                            .append(value.toString()).append("</v></c>");
                } else {
                    xml.append("<c r=\"").append(ref).append("\" t=\"inlineStr\" s=\"")
                            .append(r == 0 ? 1 : 0).append("\"><is><t xml:space=\"preserve\">")
                            .append(escape(text(value))).append("</t></is></c>");
                }
            }
            xml.append("</row>");
        }
        xml.append("</sheetData><autoFilter ref=\"A1:")
                .append(columnName(widths.length)).append(Math.max(1, rows.size()))
                .append("\"/></worksheet>");
        return xml.toString();
    }

    private String styles() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<fonts count=\"2\"><font><sz val=\"10\"/><name val=\"Aptos\"/></font>"
                + "<font><b/><color rgb=\"FFFFFFFF\"/><sz val=\"10\"/><name val=\"Aptos\"/></font></fonts>"
                + "<fills count=\"3\"><fill><patternFill patternType=\"none\"/></fill>"
                + "<fill><patternFill patternType=\"gray125\"/></fill>"
                + "<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF16A6A1\"/><bgColor indexed=\"64\"/></patternFill></fill></fills>"
                + "<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>"
                + "<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>"
                + "<cellXfs count=\"3\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
                + "<xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\" applyFill=\"1\" applyFont=\"1\" applyAlignment=\"1\"><alignment horizontal=\"center\" vertical=\"center\" wrapText=\"1\"/></xf>"
                + "<xf numFmtId=\"4\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\" applyNumberFormat=\"1\"/></cellXfs>"
                + "</styleSheet>";
    }

    private String contentTypes() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet3.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "</Types>";
    }

    private String rootRelationships() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    private String workbook() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets><sheet name=\"Facturas\" sheetId=\"1\" r:id=\"rId1\"/>"
                + "<sheet name=\"Linhas\" sheetId=\"2\" r:id=\"rId2\"/>"
                + "<sheet name=\"Resumo IVA\" sheetId=\"3\" r:id=\"rId3\"/></sheets></workbook>";
    }

    private String workbookRelationships() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet2.xml\"/>"
                + "<Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet3.xml\"/>"
                + "<Relationship Id=\"rId4\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    private void put(ZipOutputStream zip, String name, String value) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(value.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private List<Object> row(Object... values) {
        List<Object> row = new ArrayList<Object>();
        for (Object value : values) row.add(value);
        return row;
    }

    private double[] invoiceWidths() { return new double[] {18,15,28,24,16,12,14,22,15,28,35,10,13,10,16,16,18,24,30,36}; }
    private double[] lineWidths() { return new double[] {24,12,16,35,14,12,16,16,16,14,30,16,16,16,16,16}; }
    private double[] summaryWidths() { return new double[] {20,12,20,18,18}; }

    private String columnName(int index) {
        StringBuilder name = new StringBuilder();
        while (index > 0) {
            int remainder = (index - 1) % 26;
            name.insert(0, (char) ('A' + remainder));
            index = (index - 1) / 26;
        }
        return name.toString();
    }

    private String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }

    private String text(Object value) { return value == null ? "" : value.toString(); }
}

