package jminvsm.fiscal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import jminvsm.fiscal.export.CsvFiscalExporter;
import jminvsm.fiscal.export.SaftMozXmlExporter;
import jminvsm.fiscal.export.XlsxFiscalExporter;
import jminvsm.fiscal.model.FiscalDocument;
import jminvsm.fiscal.model.FiscalValidationResult;
import jminvsm.fiscal.service.FiscalDocumentValidator;
import jminvsm.fiscal.service.SaftXmlValidator;

/**
 * Teste sem JUnit para poder ser executado no projecto Ant legado.
 */
public class FiscalExportSelfTest {

    public static void main(String[] args) throws Exception {
        FiscalDocument document = sample();
        List<FiscalDocument> documents = Arrays.asList(document);
        FiscalValidationResult validation = new FiscalDocumentValidator().validate(
                documents, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31));
        require(validation.isValid(), "Documento de teste deve ser válido.");

        Path output = Files.createTempDirectory("jm-fiscal-self-test-");
        new CsvFiscalExporter().export(documents, output);
        Path xlsx = output.resolve("fiscal.xlsx");
        new XlsxFiscalExporter().export(documents, xlsx);
        require(Files.size(xlsx) > 0, "XLSX vazio.");
        try (ZipFile zip = new ZipFile(xlsx.toFile())) {
            require(zip.getEntry("xl/worksheets/sheet1.xml") != null, "Folha Facturas ausente.");
            require(zip.getEntry("xl/worksheets/sheet2.xml") != null, "Folha Linhas ausente.");
            require(zip.getEntry("xl/worksheets/sheet3.xml") != null, "Folha Resumo IVA ausente.");
        }

        Path xml = output.resolve("saft.xml");
        new SaftMozXmlExporter().export(documents,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31), xml);
        InputStream xsd = FiscalExportSelfTest.class.getResourceAsStream(
                "/jminvsm/fiscal/schema/saft-moz-provisional-1.0.xsd");
        List<String> schemaErrors = new SaftXmlValidator().validate(xml, xsd);
        require(schemaErrors.isEmpty(), "XML inválido: " + schemaErrors);

        String csv = new String(Files.readAllBytes(output.resolve("facturas.csv")), "UTF-8");
        require(csv.contains("FT A/2026/000001"), "Factura não encontrada no CSV.");
        System.out.println("FiscalExportSelfTest OK: " + output);
    }

    private static FiscalDocument sample() {
        FiscalDocument d = new FiscalDocument();
        d.setIssuerTaxId("400123456");
        d.setIssuerName("Empresa Teste, Lda");
        d.setNumber("FT A/2026/000001");
        d.setDocumentType("FT");
        d.setSeries("A");
        d.setIssueDate(LocalDate.of(2026, 7, 1));
        d.setSystemEntryDate(LocalDateTime.of(2026, 7, 1, 9, 30));
        d.setCustomerTaxId("401987654");
        d.setCustomerName("Cliente Teste, Lda");
        d.setCustomerAddress("Maputo");
        d.setCurrencyCode("MZN");
        d.setCurrencyAmount(BigDecimal.ONE);
        d.setStatus("N");
        d.setNetTotal(new BigDecimal("2000.00"));
        d.setTaxPayable(new BigDecimal("320.00"));
        d.setGrossTotal(new BigDecimal("2320.00"));
        d.setDocumentHash("f00df00df00df00df00df00df00df00df00df00df00df00df00df00df00d1234");

        FiscalDocument.Line line = new FiscalDocument.Line();
        line.setLineNumber(1);
        line.setProductCode("PROD-001");
        line.setDescription("Produto teste");
        line.setQuantity(new BigDecimal("2.000"));
        line.setUnitOfMeasure("UN");
        line.setUnitPrice(new BigDecimal("1000.000000"));
        line.setLineDiscount(BigDecimal.ZERO);
        line.setTaxCode("IVA");
        line.setTaxPercentage(new BigDecimal("16.00"));
        line.setNetTotal(new BigDecimal("2000.00"));
        line.setTaxBase(new BigDecimal("2000.00"));
        line.setTaxAmount(new BigDecimal("320.00"));
        line.setGrossTotal(new BigDecimal("2320.00"));
        line.setLineTotal(new BigDecimal("2320.00"));
        d.addLine(line);
        return d;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}

