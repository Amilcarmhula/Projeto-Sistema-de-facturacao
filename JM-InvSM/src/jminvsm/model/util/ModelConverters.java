package jminvsm.model.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public final class ModelConverters {

    private ModelConverters() {
    }

    public static BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    public static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }

    public static double toDouble(BigDecimal value) {
        return value == null ? 0.0 : value.doubleValue();
    }

    public static Double toNullableDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    public static LocalDate toLocalDate(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static String toDateString(LocalDate value) {
        return value == null ? null : value.toString();
    }

    public static LocalDateTime toLocalDateTime(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        String normalized = value.trim().replace(' ', 'T');
        try {
            return LocalDateTime.parse(normalized);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static String toDateTimeString(LocalDateTime value) {
        return value == null ? null : value.toString();
    }
}
