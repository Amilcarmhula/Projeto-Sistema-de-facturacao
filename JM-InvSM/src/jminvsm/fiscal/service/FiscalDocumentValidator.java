package jminvsm.fiscal.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jminvsm.fiscal.model.FiscalDocument;
import jminvsm.fiscal.model.FiscalValidationResult;

public class FiscalDocumentValidator {

    private static final BigDecimal TOLERANCE = new BigDecimal("0.02");

    public FiscalValidationResult validate(List<FiscalDocument> documents,
            LocalDate start, LocalDate end) {
        FiscalValidationResult result = new FiscalValidationResult();
        Set<String> numbers = new HashSet<String>();
        if (documents == null || documents.isEmpty()) {
            result.warning("", "periodo", "Nenhum documento encontrado no período.");
            return result;
        }

        for (FiscalDocument d : documents) {
            String number = text(d.getNumber());
            required(result, number, "NumeroDocumento", d.getNumber());
            required(result, number, "NUITEmitente", d.getIssuerTaxId());
            required(result, number, "NomeEmitente", d.getIssuerName());
            required(result, number, "TipoDocumento", d.getDocumentType());
            if (!numbers.add(number)) {
                result.error(number, "NumeroDocumento", "Número documental duplicado.");
            }
            if (d.getIssueDate() == null) {
                result.error(number, "DataEmissao", "Data de emissão obrigatória.");
            } else if (d.getIssueDate().isBefore(start) || d.getIssueDate().isAfter(end)) {
                result.error(number, "DataEmissao", "Documento fora do período solicitado.");
            }
            validateTaxId(result, number, "NUITEmitente", d.getIssuerTaxId(), true);
            validateTaxId(result, number, "NUITCliente", d.getCustomerTaxId(), false);
            if ("A".equals(d.getStatus()) && isBlank(d.getCancellationReason())) {
                result.error(number, "MotivoAnulacao", "Documento anulado sem motivo.");
            }
            if (isBlank(d.getDocumentHash())) {
                result.warning(number, "HashDocumento",
                        "Hash fiscal não disponível; requisito depende da especificação/certificação da AT.");
            }
            if (d.getLines().isEmpty()) {
                result.error(number, "Linhas", "Documento sem linhas.");
                continue;
            }

            BigDecimal lineNet = BigDecimal.ZERO;
            BigDecimal lineTax = BigDecimal.ZERO;
            BigDecimal lineGross = BigDecimal.ZERO;
            for (FiscalDocument.Line line : d.getLines()) {
                if (line.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                    result.error(number, "Quantidade", "Linha " + line.getLineNumber() + " sem quantidade positiva.");
                }
                if (line.getUnitPrice().compareTo(BigDecimal.ZERO) == 0) {
                    result.warning(number, "PrecoUnitario", "Linha " + line.getLineNumber()
                            + " sem snapshot de preço; documento histórico precisa de revisão.");
                }
                if (isBlank(line.getTaxCode())) {
                    result.warning(number, "CodigoImposto", "Linha " + line.getLineNumber()
                            + " sem código fiscal histórico.");
                }
                if (line.getTaxPercentage().compareTo(BigDecimal.ZERO) == 0
                        && "ISENTO".equalsIgnoreCase(text(line.getTaxCode()))
                        && isBlank(line.getTaxExemptionReason())) {
                    result.error(number, "MotivoIsencao", "Linha isenta sem motivo de isenção.");
                }
                lineNet = lineNet.add(line.getNetTotal());
                lineTax = lineTax.add(line.getTaxAmount());
                lineGross = lineGross.add(line.getLineTotal());
            }
            reconcile(result, number, "TotalLiquido", d.getNetTotal(), lineNet);
            reconcile(result, number, "TotalImposto", d.getTaxPayable(), lineTax);
            reconcile(result, number, "TotalDocumento", d.getGrossTotal(), lineGross);
        }
        return result;
    }

    private void reconcile(FiscalValidationResult result, String document, String field,
            BigDecimal header, BigDecimal lines) {
        if (header == null) {
            result.error(document, field, "Total fiscal não gravado.");
        } else if (header.subtract(lines).abs().compareTo(TOLERANCE) > 0) {
            result.error(document, field, "Total do cabeçalho (" + header
                    + ") difere da soma das linhas (" + lines + ").");
        }
    }

    private void validateTaxId(FiscalValidationResult result, String document,
            String field, String taxId, boolean required) {
        if (isBlank(taxId)) {
            if (required) result.error(document, field, "NUIT obrigatório.");
            return;
        }
        String normalized = taxId.replaceAll("\\s+", "");
        if (!normalized.matches("\\d{9}")) {
            result.warning(document, field, "Confirmar NUIT: esperado identificador com 9 dígitos.");
        }
    }

    private void required(FiscalValidationResult result, String document, String field, String value) {
        if (isBlank(value)) result.error(document, field, "Campo obrigatório não preenchido.");
    }

    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
    private String text(String value) { return value == null ? "" : value.trim(); }
}
