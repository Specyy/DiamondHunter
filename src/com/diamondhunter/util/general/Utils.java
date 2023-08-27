package com.diamondhunter.util.general;

public final class Utils {
    private static final NumberUtils numberUtils = new NumberUtils();
    private static final StringUtils stringUtils = new StringUtils();
    private static final ImageUtils imageUtils = new ImageUtils();
    private static final ArrayUtils arrayUtils = new ArrayUtils();

    public static NumberUtils getNumberUtils() {
        return numberUtils;
    }

    public static StringUtils getStringUtils() {
        return stringUtils;
    }

    public static ImageUtils getImageUtils() {
        return imageUtils;
    }

    public static ArrayUtils getArrayUtils() {
        return arrayUtils;
    }
}
