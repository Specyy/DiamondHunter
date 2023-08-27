package com.diamondhunter.util.general;

public class NumberUtils {

    NumberUtils() {
    }

    public <T extends Number> int sign(T value) {
        if (value.doubleValue() <= -1)
            return -1;
        else if (value.doubleValue() >= 1)
            return 1;
        else

        return 0;
    }

    public <T extends Number, V extends Number> T addTypes(T value, V add) {
        if (value instanceof Double) {
            Number n = value.doubleValue() + add.doubleValue();
            return (T) n;
        } else if (value instanceof Float) {
            Number n = value.floatValue() + add.floatValue();
            return (T) n;
        } else if (value instanceof Long) {
            Number n = value.longValue() + add.longValue();
            return (T) n;
        } else if (value instanceof Integer) {
            Number n = value.intValue() + add.intValue();
            return (T) n;
        } else if (value instanceof Byte) {
            Number n = value.byteValue() + add.byteValue();
            return (T) n;
        } else if (value instanceof Short) {
            Number n = value.shortValue() + add.shortValue();
            return (T) n;
        }

        return add(value, add);
    }

    public <T extends Number, V extends Number> T add(T value, V add) {
        Number n = value.doubleValue() + add.doubleValue();
        return (T) n;
    }

    public <T extends Number> boolean isValid(T value) {
        return Double.isNaN(value.doubleValue());
    }

    public <T extends Number> boolean isFinite(T value) {
        return Double.isFinite(value.doubleValue());
    }

    public <T extends Number> boolean isInfinity(T value) {
        return Double.isInfinite(value.doubleValue());
    }

    public <T extends Number> T clamp(T value, T min, T max) {
        if (value.doubleValue() < min.doubleValue()) {
            return min;
        } else if (value.doubleValue() > max.doubleValue()) {
            return max;
        } else {
            return value;
        }
    }

    public <T extends Number> T clampWhole(T value, T min, T max) {
        if (value.longValue() < min.longValue()) {
            return min;
        } else if (value.longValue() > max.longValue()) {
            return max;
        } else {
            return value;
        }
    }
}
