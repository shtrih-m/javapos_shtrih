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
 * @since 0.2
 */
public class Code11Encoder implements BarcodeEncoder {

    private static Code11Encoder instance;

    protected BarSet[] CODES_WIDTH = new BarSet[]{new BarSet("101011"),
        new BarSet("1101011"), new BarSet("1001011"),
        new BarSet("1100101"), new BarSet("1011011"),
        new BarSet("1101101"), new BarSet("1001101"),
        new BarSet("1010011"), new BarSet("1101001"), new BarSet("110101"),
        new BarSet("101101")};

    protected BarSet INTER_CHAR = new BarSet("0");

    protected BarSet START_STOP_CHAR = new BarSet("1011001");

    protected Code11Encoder() {

    }

    public static BarcodeEncoder getInstance() {
        if (instance == null) {
            instance = new Code11Encoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }
    
    public String computeCheckSum(String text) throws InvalidAtributeException {
        // Calc 'C' checkdigit char
        int check = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            int weight = (text.length() - i) % 10;
            weight = weight == 0 ? 10 : weight;
            check += getCharIndex(text.charAt(i)) * weight;
        }
        String result = Integer.toString(check % 11);

        if (text.length() + result.length() >= 10) {
            // Calc 'K' checkdigit char
            check = 0;
            String temp = text + result;
            for (int i = temp.length() - 1; i >= 0; i--) {
                int weight = (temp.length() - i) % 9;
                weight = weight == 0 ? 9 : weight;
                check += getCharIndex(temp.charAt(i)) * weight;
            }
            result += Integer.toString(check % 9);
        }
        return result;
    }

    public BarSet[] encode(String text) throws InvalidAtributeException {
        if (text.length() < 1) {
            throw new InvalidAtributeException("[Code11] Invalid text length (" + text.length() + ").");
        }

        BarSet[] result = new BarSet[(text.length() * 2) + 3];
        result[0] = START_STOP_CHAR;
        result[1] = INTER_CHAR;
        for (int i = 0; i < text.length(); i++) {
            result[(i * 2) + 2] = CODES_WIDTH[getCharIndex(text.charAt(i))];
            result[(i * 2) + 3] = INTER_CHAR;
        }
        result[result.length - 1] = START_STOP_CHAR;
        return result;
    }

    private int getCharIndex(char val) {
        return (val == '-' ? 10 : val - '0');
    }

    public String toString() {
        return "CODE 11";
    }

}
