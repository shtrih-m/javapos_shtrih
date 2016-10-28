/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 * @author V.Kravtsov
 */

public class HexUtils {

    private HexUtils() {
    }

    public static byte[] hexToBytes(String text) {

        byte[] result = new byte[text.length() / 2];
        int j = 0;
        for (int i = 0; i < text.length(); i += 2) {
            int v = Integer.parseInt(text.substring(i, i + 2), 16);
            result[j++] = (byte) v;
        }
        return result;
    }

}
