package com.shtrih.util;

/**
 * @author P.Zhirkov
 */
public class ByteUtils
{
    public static byte[] byteArray(int... array) {
        byte[] result = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = (byte) array[i];
        }

        return result;
    }

    public static int byteToInt(int B) {
        if (B < 0) {
            B = (int) (256 + B);
        }
        return B;
    }

}
