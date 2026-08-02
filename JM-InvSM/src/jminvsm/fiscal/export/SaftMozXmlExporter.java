package jminvsm.fiscal.export;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import jminvsm.fiscal.model.FiscalDocument;

/**
 * SAF-T (Moz) provisório. O namespace deixa claro que não é o XSD oficial.
 */
public class SaftMozXmlExporter {

    public static final String NAMESPACE = "urn:jm-invsm:saft-moz:provisional:1.0";

    public void export(List<FiscalDocument> documents, LocalDate start,
            LocalDate end, Path path) throws IOException {
        Files.createDirectories(path.toAbsolutePath().getParent());
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        try (OutputStream output = Files.newOutputStream(path)) {
            XMLStreamWriter xml = factory.createXMLStreamWriter(output, "UTF-8");
            try {
                xml.writeStartDocument("UTF-8", "1.0");
                xml.writeStartElement("", "AuditFile", NAMESPACE);
                xml.writeDefaultNamespace(NAMESPACE);
                xml.writeComment("MODELO PROVISORIO: substituir namespace/mapeamento pelo XSD oficial da AT.");
                writeHeader(xml, documents, start, end);
                writeMasterFiles(xml, documents);
                writeSourceDocuments(xml, documents);
                xml.writeEndElement();
                xml.writeEndDocument();
                xml.flush();
            } finally {
                xml.close();
            }
        } catch (XMLStreamException ex) {
            throw new IOException("Falha ao gerar SAF-T XML.", ex);
        }
    }

    private void writeHeader(XMLStreamWriter xml, List<FiscalDocument> documents,
            LocalDate start, LocalDate end) throws XMLStreamException {
        FiscalDocument first = documents.get(0);
        start(xml, "Header");
        element(xml, "AuditFileVersion", FiscalDocument.MODEL_VERSION);
        element(xml, "ModelStatus", "PROVISIONAL_NOT_OFFICIAL");
        element(xml, "CompanyID", text(first.getIssuerTaxId()));
        element(xml, "TaxRegistrationNumber", text(first.getIssuerTaxId()));
        element(xml, "CompanyName", text(first.getIssuerName()));
        element(xml, "FiscalYear", Integer.toString(start.getYear()));
        element(xml, "StartDate", start.toString());
        element(xml, "EndDate", end.toString());
        element(xml, "CurrencyCode", text(first.getCurrencyCode()));
        element(xml, "DateCreated", LocalDate.now().toString());
        element(xml, "TaxEntity", "Global");
        element(xml, "ProductCompanyTaxID", text(first.getIssuerTaxId()));
        element(xml, "SoftwareCertificateNumber", "PENDING_AT_CERTIFICATION");
        element(xml, "ProductID", "JM-InvSM/JM-Tecnologias");
        element(xml, "ProductVersion", FiscalDocument.MODEL_VERSION);
        end(xml);
    }

    private void writeMasterFiles(XMLStreamWriter xml, List<FiscalDocument> documents)
            throws XMLStreamException {
        Map<String, FiscalDocument> customers = new LinkedHashMap<String, FiscalDocument>();
        Map<String, FiscalDocument.Line> products = new LinkedHashMap<String, FiscalDocument.Line>();
        Map<String, FiscalDocument.Line> taxes = new LinkedHashMap<String, FiscalDocument.Line>();
        for (FiscalDocument d : documents) {
            String customerKey = isBlank(d.getCustomerTaxId()) ? "CONSUMIDOR_FINAL" : d.getCustomerTaxId();
            if (!customers.containsKey(customerKey)) customers.put(customerKey, d);
            for (FiscalDocument.Line line : d.getLines()) {
                if (!products.containsKey(line.getProductCode())) products.put(line.getProductCode(), line);
                String taxKey = text(line.getTaxCode()) + "|" + line.getTaxPercentage().toPlainString();
                if (!taxes.containsKey(taxKey)) taxes.put(taxKey, line);
            }
        }

        start(xml, "MasterFiles");
        start(xml, "Customers");
        for (Map.Entry<String, FiscalDocument> entry : customers.entrySet()) {
            FiscalDocument d = entry.getValue();
            start(xml, "Customer");
            element(xml, "CustomerID", entry.getKey());
            element(xml, "AccountID", "DESCONHECIDO");
            element(xml, "CustomerTaxID", text(d.getCustomerTaxId()));
            element(xml, "CompanyName", text(d.getCustomerName()));
            element(xml, "BillingAddress", text(d.getCustomerAddress()));
            element(xml, "SelfBillingIndicator", "0");
            end(xml);
        }
        end(xml);

        start(xml, "Products");
        for (FiscalDocument.Line line : products.values()) {
            start(xml, "Product");
            element(xml, "ProductType", "P");
            element(xml, "ProductCode", text(line.getProductCode()));
            element(xml, "ProductGroup", "GERAL");
            element(xml, "ProductDescription", text(line.getDescription()));
            element(xml, "ProductNumberCode", text(line.getProductCode()));
            end(xml);
        }
        end(xml);

        start(xml, "TaxTable");
        for (FiscalDocument.Line line : taxes.values()) {
            start(xml, "TaxTableEntry");
            element(xml, "TaxType", text(line.getTaxCode()));
            element(xml, "TaxCountryRegion", "MZ");
            element(xml, "TaxCode", text(line.getTaxCode()));
            element(xml, "Description", taxDescription(line));
            element(xml, "TaxPercentage", decimal(line.getTaxPercentage()));
            end(xml);
        }
        end(xml);
        end(xml);
    }

    private void writeSourceDocuments(XMLStreamWriter xml, List<FiscalDocument> documents)
            throws XMLStreamException {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        for (FiscalDocument d : documents) {
            if ("NC".equalsIgnoreCase(d.getDocumentType())) totalCredit = totalCredit.add(d.getGrossTotal());
            else totalDebit = totalDebit.add(d.getGrossTotal());
        }

        start(xml, "SourceDocuments");
        start(xml, "SalesInvoices");
        element(xml, "NumberOfEntries", Integer.toString(documents.size()));
        element(xml, "TotalDebit", decimal(totalDebit));
        element(xml, "TotalCredit", decimal(totalCredit));
        for (FiscalDocument d : documents) {
            start(xml, "Invoice");
            element(xml, "InvoiceNo", text(d.getNumber()));
            start(xml, "DocumentStatus");
            element(xml, "InvoiceStatus", text(d.getStatus()));
            element(xml, "InvoiceStatusDate", d.getSystemEntryDate() == null
                    ? d.getIssueDate().atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : d.getSystemEntryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            element(xml, "SourceID", "SYSTEM");
            element(xml, "SourceBilling", "P");
            if ("A".equals(d.getStatus())) element(xml, "Reason", text(d.getCancellationReason()));
            end(xml);
            element(xml, "Hash", text(d.getDocumentHash()));
            element(xml, "HashControl", text(d.getSeries()));
            element(xml, "Period", Integer.toString(d.getIssueDate().getMonthValue()));
            element(xml, "InvoiceDate", d.getIssueDate().toString());
            element(xml, "InvoiceType", text(d.getDocumentType()));
            element(xml, "SystemEntryDate", d.getSystemEntryDate() == null
                    ? d.getIssueDate().atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : d.getSystemEntryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            element(xml, "CustomerID", isBlank(d.getCustomerTaxId()) ? "CONSUMIDOR_FINAL" : d.getCustomerTaxId());
            if (!isBlank(d.getSourceDocument())) element(xml, "SourceDocumentID", d.getSourceDocument());
            for (FiscalDocument.Line line : d.getLines()) writeLine(xml, line, d.getIssueDate());
            start(xml, "DocumentTotals");
            element(xml, "TaxPayable", decimal(d.getTaxPayable()));
            element(xml, "NetTotal", decimal(d.getNetTotal()));
            element(xml, "GrossTotal", decimal(d.getGrossTotal()));
            element(xml, "CurrencyCode", text(d.getCurrencyCode()));
            element(xml, "CurrencyAmount", decimal(d.getCurrencyAmount()));
            end(xml);
            end(xml);
        }
        end(xml);
        end(xml);
    }

    private void writeLine(XMLStreamWriter xml, FiscalDocument.Line line, LocalDate taxPointDate)
            throws XMLStreamException {
        start(xml, "Line");
        element(xml, "LineNumber", Integer.toString(line.getLineNumber()));
        element(xml, "ProductCode", text(line.getProductCode()));
        element(xml, "ProductDescription", text(line.getDescription()));
        element(xml, "Quantity", decimal(line.getQuantity()));
        element(xml, "UnitOfMeasure", text(line.getUnitOfMeasure()));
        element(xml, "UnitPrice", decimal(line.getUnitPrice()));
        element(xml, "TaxPointDate", taxPointDate.toString());
        element(xml, "Description", text(line.getDescription()));
        element(xml, "CreditAmount", decimal(line.getNetTotal()));
        element(xml, "SettlementAmount", decimal(line.getLineDiscount()));
        start(xml, "Tax");
        element(xml, "TaxType", text(line.getTaxCode()));
        element(xml, "TaxCountryRegion", "MZ");
        element(xml, "TaxCode", text(line.getTaxCode()));
        element(xml, "TaxPercentage", decimal(line.getTaxPercentage()));
        element(xml, "TaxAmount", decimal(line.getTaxAmount()));
        end(xml);
        if (!isBlank(line.getTaxExemptionReason())) {
            element(xml, "TaxExemptionReason", line.getTaxExemptionReason());
        }
        end(xml);
    }

    private String taxDescription(FiscalDocument.Line line) {
        return text(line.getTaxCode()) + " " + decimal(line.getTaxPercentage()) + "%";
    }

    private void start(XMLStreamWriter xml, String name) throws XMLStreamException {
        xml.writeStartElement("", name, NAMESPACE);
    }

    private void end(XMLStreamWriter xml) throws XMLStreamException { xml.writeEndElement(); }

    private void element(XMLStreamWriter xml, String name, String value)
            throws XMLStreamException {
        start(xml, name);
        xml.writeCharacters(text(value));
        end(xml);
    }

    private String decimal(BigDecimal value) {
        return value == null ? "0.00" : value.toPlainString();
    }

    private String text(String value) { return value == null ? "" : value; }
    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
}
