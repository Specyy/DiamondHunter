package com.diamondhunter.util.general;

public final class StringUtils {

    public static String SPACE_STRING = "\\s+";

    public static String strip(String src){
        return src.trim().stripLeading().stripTrailing();
    }

    public static String reverse(String src){
        return new StringBuilder(src).reverse().toString();
    }

    public static String replaceLast(String src, String find, String replacement) {
        int pos = src.lastIndexOf(find);
        if (pos > -1) {
            return src.substring(0, pos)
                    + replacement
                    + src.substring(pos + find.length());
        } else {
            return src;
        }
    }

    public static String replaceLastRegex(String src, String regex, String replacement) {
        return src.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }
}
