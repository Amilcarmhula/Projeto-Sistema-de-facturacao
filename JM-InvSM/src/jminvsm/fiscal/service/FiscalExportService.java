package jminvsm.fiscal.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jminvsm.fiscal.dao.FiscalDataRepository;
import jminvsm.fiscal.export.CsvFiscalExporter;
import jminvsm.fiscal.export.XlsxFiscalExporter;
import jminvsm.fiscal.export.SaftMozXmlExporter;
import jminvsm.fiscal.model.FiscalDocument;
import jminvsm.fiscal.model.FiscalExportOutcome;
import jminvsm.fiscal.model.FiscalValidationResult;
import jminvsm.util.AuditoriaContext;
import jminvsm.util.Conexao;

public class FiscalExportService {

    public static final long RECOMMENDED_MAX_BYTES = 2L * 1024L * 1024L;

    private final FiscalDataRepository repository;
    private final FiscalDocumentValidator validator;
    private final CsvFiscalExporter csvExporter;
    private final XlsxFiscalExporter xlsxExporter;
    private final SaftMozXmlExporter saftExporter;
    private final SaftXmlValidator saftValidator;

    public FiscalExportService() {
        this(new FiscalDataRepository(), new FiscalDocumentValidator(),
                new CsvFiscalExporter(), new XlsxFiscalExporter(),
                new SaftMozXmlExporter(), new SaftXmlValidator());
    }

    FiscalExportService(FiscalDataRepository repository, FiscalDocumentValidator validator,
            CsvFiscalExporter csvExporter, XlsxFiscalExporter xlsxExporter,
            SaftMozXmlExporter saftExporter, SaftXmlValidator saftValidator) {
        this.repository = repository;
        this.validator = validator;
        this.csvExporter = csvExporter;
        this.xlsxExporter = xlsxExporter;
        this.saftExporter = saftExporter;
        this.saftValidator = saftValidator;
    }

    public FiscalValidationResult validate(int companyId, LocalDate start, LocalDate end)
            throws SQLException {
        List<FiscalDocument> documents = repository.findDocuments(companyId, start, end);
        return validator.validate(documents, start, end);
    }

    public FiscalExportOutcome exportPhase1(int companyId, LocalDate start,
            LocalDate end, Path outputDirectory) throws SQLException, IOException {
        List<FiscalDocument> documents = repository.findDocuments(companyId, start, end);
        FiscalValidationResult validation = validator.validate(documents, start, end);
        if (!validation.isValid()) {
            throw new FiscalValidationException(validation);
        }

        String period = start.format(DateTimeFormatter.BASIC_ISO_DATE) + "_"
                + end.format(DateTimeFormatter.BASIC_ISO_DATE);
        Path work = Files.createTempDirectory("jm-invsm-fiscal-");
        try {
            csvExporter.export(documents, work);
            Path xlsx = work.resolve("facturacao_" + period + ".xlsx");
            xlsxExporter.export(documents, xlsx);
            writeValidation(validation, work.resolve("validacao.csv"));
            writeManifest(documents, start, end, work.resolve("manifesto.txt"));

            Files.createDirectories(outputDirectory);
            Path packagePath = outputDirectory.resolve("JM-InvSM_Fiscal_" + period + ".zip");
            zipDirectory(work, packagePath);
            String hash = sha256(packagePath);
            long size = Files.size(packagePath);
            registerExport(companyId, start, end, "XLSX", packagePath.getFileName().toString(),
                    hash, documents, validation);
            return new FiscalExportOutcome(packagePath, validation, documents.size(), size, hash);
        } finally {
            deleteRecursively(work);
        }
    }

    public FiscalExportOutcome exportPhase2(int companyId, LocalDate start,
            LocalDate end, Path outputDirectory) throws SQLException, IOException {
        List<FiscalDocument> documents = repository.findDocuments(companyId, start, end);
        FiscalValidationResult validation = validator.validate(documents, start, end);
        if (!validation.isValid()) {
            throw new FiscalValidationException(validation);
        }

        String period = start.format(DateTimeFormatter.BASIC_ISO_DATE) + "_"
                + end.format(DateTimeFormatter.BASIC_ISO_DATE);
        Path work = Files.createTempDirectory("jm-invsm-saft-");
        try {
            Path xml = work.resolve("SAFT_MOZ_" + period + ".xml");
            saftExporter.export(documents, start, end, xml);
            java.io.InputStream xsd = FiscalExportService.class.getResourceAsStream(
                    "/jminvsm/fiscal/schema/saft-moz-provisional-1.0.xsd");
            List<String> xsdErrors;
            try {
                xsdErrors = saftValidator.validate(xml, xsd);
            } finally {
                if (xsd != null) xsd.close();
            }
            if (!xsdErrors.isEmpty()) {
                throw new IOException("SAF-T XML reprovado pelo XSD provisório: "
                        + xsdErrors.get(0));
            }
            java.io.InputStream schema = FiscalExportService.class.getResourceAsStream(
                    "/jminvsm/fiscal/schema/saft-moz-provisional-1.0.xsd");
            if (schema != null) {
                try { Files.copy(schema, work.resolve("saft-moz-provisional-1.0.xsd")); }
                finally { schema.close(); }
            }
            writeValidation(validation, work.resolve("validacao.csv"));
            writeManifest(documents, start, end, work.resolve("manifesto.txt"));
            Files.createDirectories(outputDirectory);
            Path packagePath = outputDirectory.resolve("JM-InvSM_SAFT_MOZ_" + period + ".zip");
            zipDirectory(work, packagePath);
            String hash = sha256(packagePath);
            long size = Files.size(packagePath);
            registerExport(companyId, start, end, "SAFT_XML",
                    packagePath.getFileName().toString(), hash, documents, validation);
            return new FiscalExportOutcome(packagePath, validation, documents.size(), size, hash);
        } finally {
            deleteRecursively(work);
        }
    }

    private void writeValidation(FiscalValidationResult validation, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writer.write("\"Severidade\";\"Documento\";\"Campo\";\"Mensagem\"\r\n");
            for (FiscalValidationResult.Issue issue : validation.getIssues()) {
                writer.write(csv(issue.getSeverity().name()) + ";" + csv(issue.getDocumentNumber())
                        + ";" + csv(issue.getField()) + ";" + csv(issue.getMessage()) + "\r\n");
            }
        }
    }

    private void writeManifest(List<FiscalDocument> documents, LocalDate start,
            LocalDate end, Path path) throws IOException {
        BigDecimal total = BigDecimal.ZERO;
        for (FiscalDocument d : documents) total = total.add(d.getGrossTotal());
        String value = "Modelo=" + FiscalDocument.MODEL_VERSION + "\r\n"
                + "Estado=PROVISORIO_NAO_OFICIAL\r\n"
                + "PeriodoInicio=" + start + "\r\nPeriodoFim=" + end + "\r\n"
                + "QuantidadeDocumentos=" + documents.size() + "\r\n"
                + "TotalDocumentos=" + total.toPlainString() + "\r\n"
                + "Nota=Mapear para o modelo oficial fornecido pela AT antes da submissao.\r\n";
        Files.write(path, value.getBytes(StandardCharsets.UTF_8));
    }

    private void zipDirectory(Path directory, Path target) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(target))) {
            try (java.util.stream.Stream<Path> paths = Files.list(directory)) {
                for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                    zip.putNextEntry(new ZipEntry(file.getFileName().toString()));
                    Files.copy(file, zip);
                    zip.closeEntry();
                }
            }
        }
    }

    private void registerExport(int companyId, LocalDate start, LocalDate end,
            String format, String fileName, String hash, List<FiscalDocument> documents,
            FiscalValidationResult validation) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        for (FiscalDocument d : documents) total = total.add(d.getGrossTotal());
        String sql = "INSERT INTO fiscal_exportacao "
                + "(empresa_id,periodo_inicio,periodo_fim,formato,versao_modelo,"
                + "nome_ficheiro,hash_sha256,quantidade_documentos,total_documentos,"
                + "estado,erros_validacao,avisos_validacao,gerado_por) "
                + "VALUES (?,?,?,?,?,?,?,?,?,'VALIDADO',?,?,?)";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setDate(2, java.sql.Date.valueOf(start));
            ps.setDate(3, java.sql.Date.valueOf(end));
            ps.setString(4, format);
            ps.setString(5, FiscalDocument.MODEL_VERSION);
            ps.setString(6, fileName);
            ps.setString(7, hash);
            ps.setInt(8, documents.size());
            ps.setBigDecimal(9, total);
            ps.setInt(10, validation.getErrorCount());
            ps.setInt(11, validation.getWarningCount());
            Integer userId = AuditoriaContext.getUsuarioActualId();
            if (userId == null) ps.setNull(12, java.sql.Types.INTEGER);
            else ps.setInt(12, userId);
            ps.executeUpdate();
        }
    }

    private String sha256(Path path) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream input = Files.newInputStream(path)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = input.read(buffer)) >= 0) digest.update(buffer, 0, read);
            }
            StringBuilder value = new StringBuilder();
            for (byte b : digest.digest()) value.append(String.format("%02x", b));
            return value.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não disponível.", ex);
        }
    }

    private void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) return;
        try (java.util.stream.Stream<Path> paths = Files.walk(path)) {
            paths.sorted(java.util.Comparator.reverseOrder()).forEach(value -> {
                try { Files.deleteIfExists(value); } catch (IOException ignored) { }
            });
        }
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    public static class FiscalValidationException extends IOException {
        private final FiscalValidationResult validation;
        public FiscalValidationException(FiscalValidationResult validation) {
            super("A exportação foi bloqueada por " + validation.getErrorCount()
                    + " erro(s) fiscal(is).");
            this.validation = validation;
        }
        public FiscalValidationResult getValidation() { return validation; }
    }
}
