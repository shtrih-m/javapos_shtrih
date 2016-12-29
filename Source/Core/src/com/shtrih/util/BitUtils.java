/*
 * BitUtils.java
 *
 * Created on 20 March 2008, 17:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.util.BitSet;

public class BitUtils {

    /** Creates a new instance of BitUtils */
    private BitUtils() {
    }

    public static int setBit(int bit) {
        return (1 << bit);
    }

    public static boolean testBit(int data, int bit) {
        return (setBit(bit) & data) != 0;
    }

    // Returns a bitset containing the values in bytes.
    // The byte-ordering of bytes must be big-endian which means the most
    // significant bit is in element 0.
    public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    // Returns a byte array of at least length 1.
    // The most significant bit in the result is guaranteed not to be a 1
    // (since BitSet does not support sign extension).
    // The byte-ordering of the result is big-endian which means the most
    // significant bit is in element 0.
    // The bit at index 0 of the bit set is assumed to be the least significant
    // bit.
    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[(bits.size()+ 7) / 8];
        for (int i = 0; i < bits.size(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

    public static int swapBits(byte data) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            if (BitUtils.testBit(data, 7 - i)) {
                result += BitUtils.setBit(i);
            }
        }
        return result;
    }

    public static byte[] swapBits(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) swapBits(data[i]);
        }
        return result;
    }

    public static byte[] swap(byte[] data) {
        int count = data.length;
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = data[count - 1 - i];
        }
        return result;
    }
}
