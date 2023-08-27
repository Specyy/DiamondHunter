package com.diamondhunter.util.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class ArrayUtils {

    @SuppressWarnings({"unchecked"})
    public <T> T[] stripEmpty(T[] array) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null)
                list.add(array[i]);
        }

        return (T[]) Arrays.copyOf(list.toArray(), list.size(), array.getClass());
    }

    public String toConcatString(Object[] array) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            str.append(array[i].toString());
        }

        return str.toString();
    }

    public <T> List<T> toList(T[] array) {
        return new LinkedList<>(Arrays.asList(array));
    }
}
