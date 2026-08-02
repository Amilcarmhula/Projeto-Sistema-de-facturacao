package jminvsm.fiscal.model;

import java.nio.file.Path;

public class FiscalExportOutcome {
    private final Path packagePath;
    private final FiscalValidationResult validation;
    private final int documentCount;
    private final long packageSize;
    private final String sha256;

    public FiscalExportOutcome(Path packagePath, FiscalValidationResult validation,
            int documentCount, long packageSize, String sha256) {
        this.packagePath = packagePath;
        this.validation = validation;
        this.documentCount = documentCount;
        this.packageSize = packageSize;
        this.sha256 = sha256;
    }

    public Path getPackagePath() { return packagePath; }
    public FiscalValidationResult getValidation() { return validation; }
    public int getDocumentCount() { return documentCount; }
    public long getPackageSize() { return packageSize; }
    public String getSha256() { return sha256; }
}

