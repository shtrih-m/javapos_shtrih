/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  Flavio Sampaio (flavio@ronisons.com);
 *
 * (C) Copyright 2005, by Flavio Sampaio
 *
 * This library is free software; you can redistribute it and/or modify it underthe terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jbarcode.encode;

import java.util.*;

/**
 * Implementation of the Code 128 format.
 * <br/>For the full spec on Code 128, see
 * {@link http://www.barcodeisland.com/code128.phtml}
 * <p/>
 * Due to the age of this project, the is Java 1.4 compliant
 *
 * @author Glen Edmonds
 * @since 0.2.0
 */
public class Code128Encoder implements BarcodeEncoder {

    private static Code128Encoder instance;

    // There are 3 character sets, each has 107 characters to chose from.
    // Many characters are available in multiple character sets.
    // Each character corresponds to an eleven-bit String .
    // The final data character is always a check digit, which is encoded into the barcode,
    // but is never printed if text is used.
    // Much of the characters in char set A have been omitted from this code
    // because they didn't look particularly useful
    protected static final int ENCODING_SET_B_ALPHA = 0;
    protected static final int ENCODING_SET_C_NUMERIC = 1;

    // These characters are the same in all character sets.
    protected static final int START_CODE_B_ALPHA = 104;
    protected static final int START_CODE_C_NUMERIC = 105;
    protected static final int SWITCH_TO_CODE_B_ALPHA = 100;
    protected static final int SWITCH_TO_CODE_C_NUMERIC = 99;
    protected static final int STOP = 106;

    // These are how the codes are to appear in the barcode.
    protected final static BarSet[] BARSETS = new BarSet[107];

    /**
     * A special "full stop" at teh end of the barcode
     */
    protected static final BarSet TERMINATION_BAR = new BarSet("11");

    static {

        // Load the barsets
        BARSETS[0] = new BarSet("11011001100");
        BARSETS[1] = new BarSet("11001101100");
        BARSETS[2] = new BarSet("11001100110");
        BARSETS[3] = new BarSet("10010011000");
        BARSETS[4] = new BarSet("10010001100");
        BARSETS[5] = new BarSet("10001001100");
        BARSETS[6] = new BarSet("10011001000");
        BARSETS[7] = new BarSet("10011000100");
        BARSETS[8] = new BarSet("10001100100");
        BARSETS[9] = new BarSet("11001001000");
        BARSETS[10] = new BarSet("11001000100");
        BARSETS[11] = new BarSet("11000100100");
        BARSETS[12] = new BarSet("10110011100");
        BARSETS[13] = new BarSet("10011011100");
        BARSETS[14] = new BarSet("10011001110");
        BARSETS[15] = new BarSet("10111001100");
        BARSETS[16] = new BarSet("10011101100");
        BARSETS[17] = new BarSet("10011100110");
        BARSETS[18] = new BarSet("11001110010");
        BARSETS[19] = new BarSet("11001011100");
        BARSETS[20] = new BarSet("11001001110");
        BARSETS[21] = new BarSet("11011100100");
        BARSETS[22] = new BarSet("11001110100");
        BARSETS[23] = new BarSet("11101101110");
        BARSETS[24] = new BarSet("11101001100");
        BARSETS[25] = new BarSet("11100101100");
        BARSETS[26] = new BarSet("11100100110");
        BARSETS[27] = new BarSet("11101100100");
        BARSETS[28] = new BarSet("11100110100");
        BARSETS[29] = new BarSet("11100110010");
        BARSETS[30] = new BarSet("11011011000");
        BARSETS[31] = new BarSet("11011000110");
        BARSETS[32] = new BarSet("11000110110");
        BARSETS[33] = new BarSet("10100011000");
        BARSETS[34] = new BarSet("10001011000");
        BARSETS[35] = new BarSet("10001000110");
        BARSETS[36] = new BarSet("10110001000");
        BARSETS[37] = new BarSet("10001101000");
        BARSETS[38] = new BarSet("10001100010");
        BARSETS[39] = new BarSet("11010001000");
        BARSETS[40] = new BarSet("11000101000");
        BARSETS[41] = new BarSet("11000100010");
        BARSETS[42] = new BarSet("10110111000");
        BARSETS[43] = new BarSet("10110001110");
        BARSETS[44] = new BarSet("10001101110");
        BARSETS[45] = new BarSet("10111011000");
        BARSETS[46] = new BarSet("10111000110");
        BARSETS[47] = new BarSet("10001110110");
        BARSETS[48] = new BarSet("11101110110");
        BARSETS[49] = new BarSet("11010001110");
        BARSETS[50] = new BarSet("11000101110");
        BARSETS[51] = new BarSet("11011101000");
        BARSETS[52] = new BarSet("11011100010");
        BARSETS[53] = new BarSet("11011101110");
        BARSETS[54] = new BarSet("11101011000");
        BARSETS[55] = new BarSet("11101000110");
        BARSETS[56] = new BarSet("11100010110");
        BARSETS[57] = new BarSet("11101101000");
        BARSETS[58] = new BarSet("11101100010");
        BARSETS[59] = new BarSet("11100011010");
        BARSETS[60] = new BarSet("11101111010");
        BARSETS[61] = new BarSet("11001000010");
        BARSETS[62] = new BarSet("11110001010");
        BARSETS[63] = new BarSet("10100110000");
        BARSETS[64] = new BarSet("10100001100");
        BARSETS[65] = new BarSet("10010110000");
        BARSETS[66] = new BarSet("10010000110");
        BARSETS[67] = new BarSet("10000101100");
        BARSETS[68] = new BarSet("10000100110");
        BARSETS[69] = new BarSet("10110010000");
        BARSETS[70] = new BarSet("10110000100");
        BARSETS[71] = new BarSet("10011010000");
        BARSETS[72] = new BarSet("10011000010");
        BARSETS[73] = new BarSet("10000110100");
        BARSETS[74] = new BarSet("10000110010");
        BARSETS[75] = new BarSet("11000010010");
        BARSETS[76] = new BarSet("11001010000");
        BARSETS[77] = new BarSet("11110111010");
        BARSETS[78] = new BarSet("11000010100");
        BARSETS[79] = new BarSet("10001111010");
        BARSETS[80] = new BarSet("10100111100");
        BARSETS[81] = new BarSet("10010111100");
        BARSETS[82] = new BarSet("10010011110");
        BARSETS[83] = new BarSet("10111100100");
        BARSETS[84] = new BarSet("10011110100");
        BARSETS[85] = new BarSet("10011110010");
        BARSETS[86] = new BarSet("11110100100");
        BARSETS[87] = new BarSet("11110010100");
        BARSETS[88] = new BarSet("11110010010");
        BARSETS[89] = new BarSet("11011011110");
        BARSETS[90] = new BarSet("11011110110");
        BARSETS[91] = new BarSet("11110110110");
        BARSETS[92] = new BarSet("10101111000");
        BARSETS[93] = new BarSet("10100011110");
        BARSETS[94] = new BarSet("10001011110");
        BARSETS[95] = new BarSet("10111101000");
        BARSETS[96] = new BarSet("10111100010");
        BARSETS[97] = new BarSet("11110101000");
        BARSETS[98] = new BarSet("11110100010");
        BARSETS[99] = new BarSet("10111011110");
        BARSETS[100] = new BarSet("10111101110");
        BARSETS[101] = new BarSet("11101011110");
        BARSETS[102] = new BarSet("11110101110");
        BARSETS[103] = new BarSet("11010000100");
        BARSETS[104] = new BarSet("11010010000");
        BARSETS[105] = new BarSet("11010011100");
        BARSETS[106] = new BarSet("11000111010");

    }

    /**
     * Hidden constructor
     */
    private Code128Encoder() {
    }

    /**
     * The checksum is never printed for this type of bar code
     *
     * @see org.jbarcode.encode.BarcodeEncoder#computeCheckSum(java.lang.String)
     */
    public String computeCheckSum(String stuff) {
        return "";
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text;
    }
    
    /**
     *
     * @return A reference to the singleton instance.
     */
    public static Code128Encoder getInstance() {
        if (instance == null) {
            instance = new Code128Encoder();
        }
        return instance;
    }

    /**
     * @param b a byte
     * @return <code>true</code> if the specified byte is a numeric digit
     * character
     */
    private boolean isDigit(byte b) {
        return b >= '0' && b <= '9';
    }

    /**
     * Finds the code to use for the specified String in the alpha char set
     *
     * @param s The charcater to encode
     * @return The code to use to encode the specified
     */
    private int getCode(byte s) {
        return s - 32;
    }

    /**
     * @see org.jbarcode.encode.BarcodeEncoder#encode(java.lang.String)
     */
    public BarSet[] encode(String texto) throws InvalidAtributeException {

        if (texto == null || texto.length() < 1) {
            throw new InvalidAtributeException("[Code128] Invalid text (" + texto + ").");
        }

        // We'll collect up the BarSets to return in a List
        List result = new ArrayList();

        // Turn into an array for convenience
        byte[] bytes = texto.getBytes();

        // Start using numeric
        boolean usingNumericEncoding = false;
        int code = START_CODE_B_ALPHA;

        if (bytes.length > 1 && isDigit(bytes[0]) && isDigit(bytes[1])) {
            usingNumericEncoding = true;
            code = START_CODE_C_NUMERIC;
        }

        int checksum = code;
        result.add(BARSETS[code]);    // Actually all are the same

        int checkMultiplier = 1;

        for (int i = 0; i < bytes.length; i++) {
            // If there are at least 2 remaining and they are digits
            if (i < bytes.length - 1 && isDigit(bytes[i]) && isDigit(bytes[i + 1])) {
                if (!usingNumericEncoding) {
                    usingNumericEncoding = true;
                    code = SWITCH_TO_CODE_C_NUMERIC;
                    // Update checksum
                    checksum += code * checkMultiplier;
                    checkMultiplier++;

                    result.add(BARSETS[code]);
                }
                // Encode the next two bytes as one encoding
                code = Integer.parseInt(new String(bytes, i, 2));
                // Mark the swallowing of an extra byte.
                i++;
            } else {
                if (usingNumericEncoding) {
                    usingNumericEncoding = false;
                    code = SWITCH_TO_CODE_B_ALPHA;
                    // Update checksum
                    checksum += code * checkMultiplier;
                    checkMultiplier++;

                    result.add(BARSETS[code]);
                }
                // Encode the character
                code = getCode(bytes[i]);
            }

            // Update checksum
            checksum += code * checkMultiplier;
            checkMultiplier++;

            // Insert the stripes
            result.add(BARSETS[code]);
        }

        // Add the check digit
        int checkDigit = checksum % 103;
        result.add(BARSETS[checkDigit]);

        // Add the STOP bar
        result.add(BARSETS[STOP]);

        // Add the TERMINATOR bar
        result.add(TERMINATION_BAR);

        return (BarSet[]) result.toArray(new BarSet[]{});
    }

    public String toString() {
        return "CODE 128";
    }

}
