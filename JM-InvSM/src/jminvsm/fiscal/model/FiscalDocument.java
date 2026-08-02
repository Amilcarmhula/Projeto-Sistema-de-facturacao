package jminvsm.fiscal.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modelo canónico interno. Não reproduz um XSD oficial da AT.
 */
public class FiscalDocument {

    public static final String MODEL_VERSION = "JM-AT-BASE-1.0";

    private String issuerTaxId;
    private String issuerName;
    private String number;
    private String documentType;
    private String series;
    private LocalDate issueDate;
    private LocalDateTime systemEntryDate;
    private String customerTaxId;
    private String customerName;
    private String customerAddress;
    private String currencyCode = "MZN";
    private BigDecimal currencyAmount = BigDecimal.ONE;
    private String status = "N";
    private BigDecimal netTotal = BigDecimal.ZERO;
    private BigDecimal taxPayable = BigDecimal.ZERO;
    private BigDecimal grossTotal = BigDecimal.ZERO;
    private String sourceDocument;
    private String cancellationReason;
    private String documentHash;
    private final List<Line> lines = new ArrayList<Line>();

    public void addLine(Line line) {
        if (line != null) {
            lines.add(line);
        }
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public String getIssuerTaxId() { return issuerTaxId; }
    public void setIssuerTaxId(String value) { this.issuerTaxId = value; }
    public String getIssuerName() { return issuerName; }
    public void setIssuerName(String value) { this.issuerName = value; }
    public String getNumber() { return number; }
    public void setNumber(String value) { this.number = value; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String value) { this.documentType = value; }
    public String getSeries() { return series; }
    public void setSeries(String value) { this.series = value; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate value) { this.issueDate = value; }
    public LocalDateTime getSystemEntryDate() { return systemEntryDate; }
    public void setSystemEntryDate(LocalDateTime value) { this.systemEntryDate = value; }
    public String getCustomerTaxId() { return customerTaxId; }
    public void setCustomerTaxId(String value) { this.customerTaxId = value; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String value) { this.customerName = value; }
    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String value) { this.customerAddress = value; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String value) { this.currencyCode = value; }
    public BigDecimal getCurrencyAmount() { return currencyAmount; }
    public void setCurrencyAmount(BigDecimal value) { this.currencyAmount = safe(value); }
    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
    public BigDecimal getNetTotal() { return netTotal; }
    public void setNetTotal(BigDecimal value) { this.netTotal = safe(value); }
    public BigDecimal getTaxPayable() { return taxPayable; }
    public void setTaxPayable(BigDecimal value) { this.taxPayable = safe(value); }
    public BigDecimal getGrossTotal() { return grossTotal; }
    public void setGrossTotal(BigDecimal value) { this.grossTotal = safe(value); }
    public String getSourceDocument() { return sourceDocument; }
    public void setSourceDocument(String value) { this.sourceDocument = value; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String value) { this.cancellationReason = value; }
    public String getDocumentHash() { return documentHash; }
    public void setDocumentHash(String value) { this.documentHash = value; }

    private static BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static class Line {
        private int lineNumber;
        private String productCode;
        private String description;
        private BigDecimal quantity = BigDecimal.ZERO;
        private String unitOfMeasure;
        private BigDecimal unitPrice = BigDecimal.ZERO;
        private BigDecimal lineDiscount = BigDecimal.ZERO;
        private String taxCode;
        private BigDecimal taxPercentage = BigDecimal.ZERO;
        private String taxExemptionReason;
        private BigDecimal netTotal = BigDecimal.ZERO;
        private BigDecimal taxBase = BigDecimal.ZERO;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal grossTotal = BigDecimal.ZERO;
        private BigDecimal lineTotal = BigDecimal.ZERO;

        public int getLineNumber() { return lineNumber; }
        public void setLineNumber(int value) { this.lineNumber = value; }
        public String getProductCode() { return productCode; }
        public void setProductCode(String value) { this.productCode = value; }
        public String getDescription() { return description; }
        public void setDescription(String value) { this.description = value; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal value) { this.quantity = safe(value); }
        public String getUnitOfMeasure() { return unitOfMeasure; }
        public void setUnitOfMeasure(String value) { this.unitOfMeasure = value; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal value) { this.unitPrice = safe(value); }
        public BigDecimal getLineDiscount() { return lineDiscount; }
        public void setLineDiscount(BigDecimal value) { this.lineDiscount = safe(value); }
        public String getTaxCode() { return taxCode; }
        public void setTaxCode(String value) { this.taxCode = value; }
        public BigDecimal getTaxPercentage() { return taxPercentage; }
        public void setTaxPercentage(BigDecimal value) { this.taxPercentage = safe(value); }
        public String getTaxExemptionReason() { return taxExemptionReason; }
        public void setTaxExemptionReason(String value) { this.taxExemptionReason = value; }
        public BigDecimal getNetTotal() { return netTotal; }
        public void setNetTotal(BigDecimal value) { this.netTotal = safe(value); }
        public BigDecimal getTaxBase() { return taxBase; }
        public void setTaxBase(BigDecimal value) { this.taxBase = safe(value); }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal value) { this.taxAmount = safe(value); }
        public BigDecimal getGrossTotal() { return grossTotal; }
        public void setGrossTotal(BigDecimal value) { this.grossTotal = safe(value); }
        public BigDecimal getLineTotal() { return lineTotal; }
        public void setLineTotal(BigDecimal value) { this.lineTotal = safe(value); }
    }
}

