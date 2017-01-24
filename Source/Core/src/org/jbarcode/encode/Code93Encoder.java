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
 * TODO: Description
 *
 * @author Flavio Sampaio
 * @since 0.2.0
 */
public class Code93Encoder implements BarcodeEncoder {

    protected static BarSet[] CODES = new BarSet[]{
        new BarSet("100010100"), new BarSet("101001000"), new BarSet("101000100"),
        new BarSet("101000010"), new BarSet("100101000"), new BarSet("100100100"),
        new BarSet("100100010"), new BarSet("101010000"), new BarSet("100010010"),
        new BarSet("100001010"), new BarSet("110101000"), new BarSet("110100100"),
        new BarSet("110100010"), new BarSet("110010100"), new BarSet("110010010"),
        new BarSet("110001010"), new BarSet("101101000"), new BarSet("101100100"),
        new BarSet("101100010"), new BarSet("100110100"), new BarSet("100011010"),
        new BarSet("101011000"), new BarSet("101001100"), new BarSet("101000110"),
        new BarSet("100101100"), new BarSet("100010110"), new BarSet("110110100"),
        new BarSet("110110010"), new BarSet("110101100"), new BarSet("110100110"),
        new BarSet("110010110"), new BarSet("110011010"), new BarSet("101101100"),
        new BarSet("101100110"), new BarSet("100110110"), new BarSet("100111010"),
        new BarSet("100101110"), new BarSet("111010100"), new BarSet("111010010"),
        new BarSet("111001010"), new BarSet("101101110"), new BarSet("101110110"),
        new BarSet("110101110"), new BarSet("100100110"), new BarSet("111011010"),
        new BarSet("111010110"), new BarSet("100110010")
    };

    protected BarSet START_STOP_CHAR = new BarSet("101011110");

    protected BarSet TERMINATION_BAR = new BarSet("1");

    private static Code93Encoder instance;

    protected Code93Encoder() {

    }

    public static BarcodeEncoder getInstance() {
        if (instance == null) {
            instance = new Code93Encoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }
    
    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() < 1) {
            throw new InvalidAtributeException("[Code93] Invalid text length (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[texto.length() + 3];
        result[0] = START_STOP_CHAR;
        for (int i = 0; i < texto.length(); i++) {
            result[i + 1] = CODES[getCharIndex(texto.charAt(i))];
        }

        result[result.length - 2] = START_STOP_CHAR;
        result[result.length - 1] = TERMINATION_BAR;

        return result;
    }

    protected int getCharIndex(char c) throws InvalidAtributeException {
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
        } else if (c == '\u00B9') { //special char ($)
            return 43;
        } else if (c == '\u00B3') { //special char (/)
            return 44;
        } else if (c == '\u00A3') { //special char (+)
            return 45;
        } else if (c == '\u00B2') { //special char (%)
            return 46;
        } else {
            throw new InvalidAtributeException("[Code93] The text contains unsuported chars.");
        }
    }

    protected char getChar(byte c) throws InvalidAtributeException {
        if (c >= 0 && c <= 9) {
            return (char) (c + 48);
        } else if (c >= 10 && c <= 35) {
            return (char) (c + 55);
        } else if (c == 36) {
            return '-';
        } else if (c == 37) {
            return '.';
        } else if (c == 38) {
            return ' ';
        } else if (c == 39) {
            return '$';
        } else if (c == 40) {
            return '/';
        } else if (c == 41) {
            return '+';
        } else if (c == 42) {
            return '%';
        } else if (c == 43) {
            return '\u00B9';
        } else if (c == 44) {
            return '\u00B3';
        } else if (c == 45) {
            return '\u00A3';
        } else if (c == 46) {
            return '\u00B2';
        } else {
            throw new InvalidAtributeException("[Code93] The text contains unsuported chars.");
        }
    }

    public String computeCheckSum(String text) throws InvalidAtributeException {
        //Calc 'C' checkdigit char
        int check = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            int weight = (text.length() - i) % 20;
            weight = weight == 0 ? 20 : weight;
            check += getCharIndex(text.charAt(i)) * weight;
        }
        String result = Character.toString(getChar((byte) (check % 47)));

        //Calc 'K' checkdigit char
        check = 0;
        String temp = text + result;
        for (int i = temp.length() - 1; i >= 0; i--) {
            int weight = (temp.length() - i) % 15;
            weight = weight == 0 ? 15 : weight;
            check += getCharIndex(temp.charAt(i)) * weight;
        }
        result += Character.toString(getChar((byte) (check % 47)));
        return result;
    }

    public String toString() {
        return "CODE 93";
    }

}
