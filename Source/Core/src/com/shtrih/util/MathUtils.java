/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 * @author V.Kravtsov
 */
public class MathUtils {

    private MathUtils() {
    }

    public static int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        } else {
            return i2;
        }
    }

    public static long round(double amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount > 0) {
            return (long) (amount + 0.5);
        } else {
            return (long) (amount - 0.5);
        }
    }

    public static int compare(int v1, int v2) {
        if (v1 > v2) {
            return 1;
        }
        if (v1 < v2) {
            return -1;
        }
        return 0;
    }

    public static int CRC16CCITT(byte[] bytes) 
    { 
        return CRC16(bytes, 0x1021, 0);
    }

    public static int CRC16(byte[] bytes, int polynomial, int crc) 
    {
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }
    
}
