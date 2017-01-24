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
public class Code39Encoder implements BarcodeEncoder {

    private static Code39Encoder instance;

    protected BarSet[] CODES_WIDTH = new BarSet[]{
        new BarSet("000110100"), new BarSet("100100001"), new BarSet("001100001"),
        new BarSet("101100000"), new BarSet("000110001"), new BarSet("100110000"),
        new BarSet("001110000"), new BarSet("000100101"), new BarSet("100100100"),
        new BarSet("001100100"), new BarSet("100001001"), new BarSet("001001001"),
        new BarSet("101001000"), new BarSet("000011001"), new BarSet("100011000"),
        new BarSet("001011000"), new BarSet("000001101"), new BarSet("100001100"),
        new BarSet("001001100"), new BarSet("000011100"), new BarSet("100000011"),
        new BarSet("001000011"), new BarSet("101000010"), new BarSet("000010011"),
        new BarSet("100010010"), new BarSet("001010010"), new BarSet("000000111"),
        new BarSet("100000110"), new BarSet("001000110"), new BarSet("000010110"),
        new BarSet("110000001"), new BarSet("011000001"), new BarSet("111000000"),
        new BarSet("010010001"), new BarSet("110010000"), new BarSet("011010000"),
        new BarSet("010000101"), new BarSet("110000100"), new BarSet("011000100"),
        new BarSet("010101000"), new BarSet("010100010"), new BarSet("010001010"),
        new BarSet("000101010")
    };

    protected BarSet START_STOP_CHAR = new BarSet("010010100");

    protected BarSet INTER_CHAR = new BarSet("0");

    protected Code39Encoder() {

    }

    public static BarcodeEncoder getInstance() {
        if (instance == null) {
            instance = new Code39Encoder();
        }
        return instance;
    }

    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() < 1) {
            throw new InvalidAtributeException("[Code39] Invalid text length (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[2 * texto.length() + 3];
        result[0] = START_STOP_CHAR;
        for (int i = 0; i < texto.length(); i++) {
            result[(i * 2) + 1] = INTER_CHAR;
            result[(i * 2) + 2] = CODES_WIDTH[getCharIndex(texto.charAt(i))];
        }
        result[result.length - 2] = INTER_CHAR;
        result[result.length - 1] = START_STOP_CHAR;

        return result;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }

    public String computeCheckSum(String texto) throws InvalidAtributeException {
        int check = 0;
        for (int i = 0; i < texto.length(); i++) {
            check += getCharIndex(texto.charAt(i));
        }
        char c = (char) getChar((byte) check % 43);
        return (new Character(c).toString());
    }

    private int getCharIndex(char c) throws InvalidAtributeException {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'A' && c <= 'Z') {
            return c - 55;
        } else if (c == '-') {
            return 36;
        } else if (c == '.') {
            return 37;
        } else if (c == ' ') {
            return 38;
        } else if (c == '$') {
            return 39;
        } else if (c == '/') {
            return 40;
        } else if (c == '+') {
            return 41;
        } else if (c == '%') {
            return 42;
        } else {
            throw new InvalidAtributeException("[Code39] The text contains unsuported chars.");
        }
    }

    private int getChar(int c) throws InvalidAtributeException {
        if (c >= 0 && c <= 9) {
            return c + 48;
        } else if (c >= 10 && c <= 35) {
            return c + 55;
        } else if (c == 36) {
            return 45;
        } else if (c == 37) {
            return 46;
        } else if (c == 38) {
            return 32;
        } else if (c == 39) {
            return 36;
        } else if (c == 40) {
            return 47;
        } else if (c == 41) {
            return 43;
        } else if (c == 42) {
            return 37;
        } else {
            throw new InvalidAtributeException("[Code39] The text contains unsuported chars.");
        }
    }

    public String toString() {
        return "CODE 39";
    }

}
