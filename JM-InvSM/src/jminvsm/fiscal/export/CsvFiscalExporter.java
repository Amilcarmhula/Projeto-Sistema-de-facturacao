package jminvsm.fiscal.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jminvsm.fiscal.model.FiscalDocument;

public class CsvFiscalExporter {

    private static final String[] INVOICE_HEADER = {
        "VersaoModelo","NUITEmitente","NomeEmitente","NumeroDocumento",
        "TipoDocumento","Serie","DataEmissao","DataHoraRegisto","NUITCliente",
        "NomeCliente","EnderecoCliente","Moeda","TaxaCambio","Estado",
        "TotalLiquido","TotalImposto","TotalDocumento","DocumentoOrigem",
        "MotivoAnulacao","HashDocumento"
    };
    private static final String[] LINE_HEADER = {
        "NumeroDocumento","NumeroLinha","CodigoProduto","Descricao","Quantidade",
        "Unidade","PrecoUnitario","DescontoLinha","CodigoImposto","TaxaImposto",
        "MotivoIsencao","ValorLiquido","BaseTributavel","ValorImposto",
        "ValorBruto","TotalLinha"
    };

    public void export(List<FiscalDocument> documents, Path directory) throws IOException {
        Files.createDirectories(directory);
        writeInvoices(documents, directory.resolve("facturas.csv"));
        writeLines(documents, directory.resolve("linhas.csv"));
        writeTaxSummary(documents, directory.resolve("resumo_iva.csv"));
    }

    private void writeInvoices(List<FiscalDocument> documents, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeRow(writer, INVOICE_HEADER);
            for (FiscalDocument d : documents) {
                writeRow(writer, new String[] {
                    FiscalDocument.MODEL_VERSION, d.getIssuerTaxId(), d.getIssuerName(),
                    d.getNumber(), d.getDocumentType(), d.getSeries(),
                    d.getIssueDate() == null ? "" : d.getIssueDate().toString(),
                    d.getSystemEntryDate() == null ? "" : d.getSystemEntryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    d.getCustomerTaxId(), d.getCustomerName(), d.getCustomerAddress(),
                    d.getCurrencyCode(), decimal(d.getCurrencyAmount()), d.getStatus(),
                    decimal(d.getNetTotal()), decimal(d.getTaxPayable()), decimal(d.getGrossTotal()),
                    d.getSourceDocument(), d.getCancellationReason(), d.getDocumentHash()
                });
            }
        }
    }

    private void writeLines(List<FiscalDocument> documents, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeRow(writer, LINE_HEADER);
            for (FiscalDocument d : documents) {
                for (FiscalDocument.Line line : d.getLines()) {
                    writeRow(writer, new String[] {
                        d.getNumber(), Integer.toString(line.getLineNumber()), line.getProductCode(),
                        line.getDescription(), decimal(line.getQuantity()), line.getUnitOfMeasure(),
                        decimal(line.getUnitPrice()), decimal(line.getLineDiscount()), line.getTaxCode(),
                        decimal(line.getTaxPercentage()), line.getTaxExemptionReason(),
                        decimal(line.getNetTotal()), decimal(line.getTaxBase()), decimal(line.getTaxAmount()),
                        decimal(line.getGrossTotal()), decimal(line.getLineTotal())
                    });
                }
            }
        }
    }

    private void writeTaxSummary(List<FiscalDocument> documents, Path path) throws IOException {
        java.util.Map<String, BigDecimal[]> totals = new java.util.TreeMap<String, BigDecimal[]>();
        for (FiscalDocument d : documents) {
            for (FiscalDocument.Line line : d.getLines()) {
                String key = value(line.getTaxCode()) + "|" + decimal(line.getTaxPercentage());
                BigDecimal[] row = totals.get(key);
                if (row == null) {
                    row = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
                    totals.put(key, row);
                }
                row[0] = row[0].add(line.getTaxBase());
                row[1] = row[1].add(line.getTaxAmount());
                row[2] = row[2].add(line.getLineTotal());
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeRow(writer, new String[] {"CodigoImposto","Taxa","BaseTributavel","ValorImposto","ValorBruto"});
            for (java.util.Map.Entry<String, BigDecimal[]> entry : totals.entrySet()) {
                String[] key = entry.getKey().split("\\|", -1);
                BigDecimal[] row = entry.getValue();
                writeRow(writer, new String[] { key[0], key[1], decimal(row[0]), decimal(row[1]), decimal(row[2]) });
            }
        }
    }

    private void writeRow(BufferedWriter writer, String[] values) throws IOException {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) writer.write(';');
            writer.write('"');
            writer.write(value(values[i]).replace("\"", "\"\""));
            writer.write('"');
        }
        writer.write("\r\n");
    }

    private String decimal(BigDecimal value) {
        return value == null ? "" : value.setScale(Math.max(2, value.scale()), BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private String value(String value) { return value == null ? "" : value; }
}

