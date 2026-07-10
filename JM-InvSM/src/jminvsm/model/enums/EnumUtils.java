package jminvsm.model.enums;

public final class EnumUtils {

    private EnumUtils() {
    }

    public static <E extends Enum<E> & DatabaseEnum> E fromValue(Class<E> enumType, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalizedValue = value.trim();
        for (E item : enumType.getEnumConstants()) {
            if (item.getValor().equalsIgnoreCase(normalizedValue)
                    || item.name().equalsIgnoreCase(normalizedValue)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Valor invalido para " + enumType.getSimpleName() + ": " + value);
    }
}
