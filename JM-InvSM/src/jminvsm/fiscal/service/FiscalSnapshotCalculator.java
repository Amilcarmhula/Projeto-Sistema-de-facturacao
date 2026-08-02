package jminvsm.fiscal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;

/**
 * Captura os valores fiscais no momento da emissão.
 */
public final class FiscalSnapshotCalculator {

    private FiscalSnapshotCalculator() { }

    public static LineSnapshot line(Item item, int lineNumber) {
        BigDecimal quantity = BigDecimal.valueOf(item.getQuantidade());
        BigDecimal gross = safe(item.getSubtotalDecimal()).setScale(2, RoundingMode.HALF_UP);
        Imposto tax = item.getProduto() == null ? null : item.getProduto().getImposto();
        BigDecimal percentage = tax == null ? BigDecimal.ZERO : safe(tax.getPercentagemDecimal());
        BigDecimal divisor = BigDecimal.ONE.add(percentage.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
        BigDecimal net = divisor.compareTo(BigDecimal.ZERO) == 0
                ? gross : gross.divide(divisor, 2, RoundingMode.HALF_UP);
        BigDecimal taxAmount = gross.subtract(net).setScale(2, RoundingMode.HALF_UP);
        BigDecimal unitPrice = quantity.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO : net.divide(quantity, 6, RoundingMode.HALF_UP);
        BigDecimal discount = BigDecimal.ZERO;
        if (item.getPrecoProdutoArmazem() != null
                && item.getPrecoProdutoArmazem().getPrecoVendaDecimal() != null) {
            BigDecimal regularGross = item.getPrecoProdutoArmazem().getPrecoVendaDecimal().multiply(quantity);
            discount = regularGross.subtract(gross).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        }
        String taxName = tax == null ? "" : text(tax.getNome()).toUpperCase();
        String taxCode = percentage.compareTo(BigDecimal.ZERO) > 0 ? "IVA"
                : (taxName.contains("ISENT") ? "ISENTO" : "SEM_IMPOSTO");
        String exemption = percentage.compareTo(BigDecimal.ZERO) == 0 && tax != null
                ? text(tax.getDescricao()) : "";
        String unit = item.getUnidadeVendida();
        if (isBlank(unit) && item.getProduto() != null && item.getProduto().getUnidadeMedida() != null) {
            unit = item.getProduto().getUnidadeMedida().getSigla();
        }
        return new LineSnapshot(lineNumber, unit, unitPrice, discount, taxCode,
                percentage, exemption, net, net, taxAmount, gross, gross);
    }

    public static Totals totals(Factura invoice) {
        BigDecimal net = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal gross = BigDecimal.ZERO;
        List<Item> items = invoice == null || invoice.getItens() == null
                ? Collections.<Item>emptyList() : invoice.getItens();
        int number = 1;
        for (Item item : items) {
            LineSnapshot snapshot = line(item, number++);
            net = net.add(snapshot.netTotal);
            tax = tax.add(snapshot.taxAmount);
            gross = gross.add(snapshot.lineTotal);
        }
        if (items.isEmpty() && invoice != null) gross = safe(invoice.getTotalDecimal());
        return new Totals(net, tax, gross);
    }

    public static String internalHash(Factura invoice, Totals totals) {
        String canonical = text(invoice.getNumero_fac()) + "|"
                + text(invoice.getData_emissao()) + "|"
                + (invoice.getEmpresa() == null ? "" : invoice.getEmpresa().getNuit_emp()) + "|"
                + (invoice.getCliente() == null ? "" : invoice.getCliente().getNuit_cli()) + "|"
                + totals.grossTotal.setScale(2, RoundingMode.HALF_UP).toPlainString();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(canonical.getBytes(StandardCharsets.UTF_8));
            StringBuilder value = new StringBuilder(64);
            for (byte b : hash) value.append(String.format("%02x", b));
            return value.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não disponível.", ex);
        }
    }

    public static String deriveSeries(String documentNumber) {
        String value = text(documentNumber);
        int slash = value.lastIndexOf('/');
        return slash > 0 ? value.substring(0, slash) : "A";
    }

    private static BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
    private static String text(String value) { return value == null ? "" : value.trim(); }
    private static boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }

    public static final class Totals {
        private final BigDecimal netTotal;
        private final BigDecimal taxAmount;
        private final BigDecimal grossTotal;
        private Totals(BigDecimal netTotal, BigDecimal taxAmount, BigDecimal grossTotal) {
            this.netTotal = netTotal;
            this.taxAmount = taxAmount;
            this.grossTotal = grossTotal;
        }
        public BigDecimal getNetTotal() { return netTotal; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public BigDecimal getGrossTotal() { return grossTotal; }
    }

    public static final class LineSnapshot {
        private final int lineNumber;
        private final String unit;
        private final BigDecimal unitPrice;
        private final BigDecimal discount;
        private final String taxCode;
        private final BigDecimal taxPercentage;
        private final String exemptionReason;
        private final BigDecimal netTotal;
        private final BigDecimal taxBase;
        private final BigDecimal taxAmount;
        private final BigDecimal grossTotal;
        private final BigDecimal lineTotal;
        private LineSnapshot(int lineNumber, String unit, BigDecimal unitPrice,
                BigDecimal discount, String taxCode, BigDecimal taxPercentage,
                String exemptionReason, BigDecimal netTotal, BigDecimal taxBase,
                BigDecimal taxAmount, BigDecimal grossTotal, BigDecimal lineTotal) {
            this.lineNumber = lineNumber; this.unit = unit; this.unitPrice = unitPrice;
            this.discount = discount; this.taxCode = taxCode; this.taxPercentage = taxPercentage;
            this.exemptionReason = exemptionReason; this.netTotal = netTotal; this.taxBase = taxBase;
            this.taxAmount = taxAmount; this.grossTotal = grossTotal; this.lineTotal = lineTotal;
        }
        public int getLineNumber() { return lineNumber; }
        public String getUnit() { return unit; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getDiscount() { return discount; }
        public String getTaxCode() { return taxCode; }
        public BigDecimal getTaxPercentage() { return taxPercentage; }
        public String getExemptionReason() { return exemptionReason; }
        public BigDecimal getNetTotal() { return netTotal; }
        public BigDecimal getTaxBase() { return taxBase; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public BigDecimal getGrossTotal() { return grossTotal; }
        public BigDecimal getLineTotal() { return lineTotal; }
    }
}

