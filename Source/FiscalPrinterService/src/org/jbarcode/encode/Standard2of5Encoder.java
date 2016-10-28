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

/**
 * TODO: Description.
 *
 * @author Flavio Sampaio
 * @since 0.1
 */
public class Standard2of5Encoder implements BarcodeEncoder {

    protected final BarSet[] CODES_WIDTH = new BarSet[]{
        new BarSet("00110"), new BarSet("10001"), new BarSet("01001"),
        new BarSet("11000"), new BarSet("00101"), new BarSet("10100"),
        new BarSet("01100"), new BarSet("00011"), new BarSet("10010"),
        new BarSet("01010")
    };

    private static BarSet START_CHAR = new BarSet("101000");

    private static BarSet STOP_CHAR = new BarSet("10001");

    protected BarSet INTER_CHAR = new BarSet("0");

    private static Standard2of5Encoder instance;

    protected Standard2of5Encoder() {

    }

    public static Standard2of5Encoder getInstance() {
        if (instance == null) {
            instance = new Standard2of5Encoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }

    /* (non-Javadoc)
     * @see org.jbarcode.BarcodeEncoder#encode(java.lang.String)
     */
    public BarSet[] encode(String text) throws InvalidAtributeException {
        if (text.length() < 1) {
            throw new InvalidAtributeException("[Standard2of5] Invalid text length (" + text.length() + ").");
        }

        BarSet[] result = new BarSet[text.length() + 2];
        try {
            int codeSize = 10;
            //tres barras iniciais
            result[0] = START_CHAR;
            //gera a primeira parte do codigo
            for (int i = 0; i < text.length(); i++) {
                int ind = charToInt(text.charAt(i));
                result[i + 1] = new BarSet(codeSize);
                int z = 0;
                for (int j = 0; j < CODES_WIDTH[ind].length(); j++) {
                    if (CODES_WIDTH[ind].get(j)) {
                        result[i + 1].set(z);
                    }
                    z += 2;
                }
            }
            //barras centrais que dividem o codigo
            result[result.length - 1] = STOP_CHAR;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[Standard2of5] Only numbers suported.");
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.jbarcode.BarcodeEncoder#computeCheckSum(java.lang.String)
     */
    public String computeCheckSum(String text) throws InvalidAtributeException {
        int sum = 0;
        for (int charPos = 0; charPos < text.length(); charPos++) {
            if (charPos % 2 == 0) {
                sum += (charToInt(text.charAt(charPos)) * 3);
            } else {
                sum += charToInt(text.charAt(charPos));
            }
        }

        int result = sum % 10;
        if (result == 0) {
            return "0";
        } else {
            return String.valueOf(10 - result);
        }
    }

    public String toString() {
        return "Standard/Industrial 2of5";
    }

    protected static int charToInt(char val) {
        return val - '0';
    }

}
