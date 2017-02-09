package com.shtrih.util;

import com.sun.istack.internal.NotNull;

/**
 * @author P.Zhirkov
 */
public class ByteUtils {
    public static byte[] byteArray(@NotNull int... array) {
        byte[] result = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = (byte) array[i];
        }

        return result;
    }

}
