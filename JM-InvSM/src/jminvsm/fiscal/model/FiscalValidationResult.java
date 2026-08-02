package jminvsm.fiscal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FiscalValidationResult {

    public enum Severity { ERROR, WARNING }

    public static class Issue {
        private final Severity severity;
        private final String documentNumber;
        private final String field;
        private final String message;

        public Issue(Severity severity, String documentNumber, String field, String message) {
            this.severity = severity;
            this.documentNumber = documentNumber;
            this.field = field;
            this.message = message;
        }

        public Severity getSeverity() { return severity; }
        public String getDocumentNumber() { return documentNumber; }
        public String getField() { return field; }
        public String getMessage() { return message; }
    }

    private final List<Issue> issues = new ArrayList<Issue>();

    public void error(String document, String field, String message) {
        issues.add(new Issue(Severity.ERROR, document, field, message));
    }

    public void warning(String document, String field, String message) {
        issues.add(new Issue(Severity.WARNING, document, field, message));
    }

    public boolean isValid() { return getErrorCount() == 0; }

    public int getErrorCount() {
        int count = 0;
        for (Issue issue : issues) if (issue.severity == Severity.ERROR) count++;
        return count;
    }

    public int getWarningCount() {
        int count = 0;
        for (Issue issue : issues) if (issue.severity == Severity.WARNING) count++;
        return count;
    }

    public List<Issue> getIssues() {
        return Collections.unmodifiableList(issues);
    }
}

