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
public class CodabarEncoder implements BarcodeEncoder {

    protected final BarSet[] CODES_WIDTH = new BarSet[]{
        new BarSet("0000011"), new BarSet("0000110"), new BarSet("0001001"),
        new BarSet("1100000"), new BarSet("0010010"), new BarSet("1000010"),
        new BarSet("0100001"), new BarSet("0100100"), new BarSet("0110000"),
        new BarSet("1001000"), new BarSet("0001100"), new BarSet("0011000"),
        new BarSet("1000101"), new BarSet("1010001"), new BarSet("1010100"),
        new BarSet("0011111"), new BarSet("0011010"), new BarSet("0001011"),
        new BarSet("0101001"), new BarSet("0001110")
    };

    protected BarSet INTER_CHAR = new BarSet("0");

    public static final int START_CHAR_A = 16;
    public static final int START_CHAR_B = 17;
    public static final int START_CHAR_C = 18;
    public static final int START_CHAR_D = 19;

    public static final int STOP_CHAR_T = 16;
    public static final int STOP_CHAR_N = 17;
    public static final int STOP_CHAR_ASTERISC = 18;
    public static final int STOP_CHAR_E = 19;

    private static CodabarEncoder instance;

    private int startChar;
    private int stopChar;

    private CodabarEncoder() {
        this.startChar = START_CHAR_A;
        this.stopChar = STOP_CHAR_ASTERISC;
    }

    public static CodabarEncoder getInstance() {
        if (instance == null) {
            instance = new CodabarEncoder();
        }
        return instance;
    }

    /**
     * @see org.jbarcode.BarcodeEncoder#gerarCodigo(String)
     */
    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() < 1) {
            throw new InvalidAtributeException("[Codabar] Invalid text length (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[2 * texto.length() + 3];
        result[0] = CODES_WIDTH[startChar];
        for (int i = 0; i < texto.length(); i++) {
            result[(i * 2) + 1] = INTER_CHAR;
            result[(i * 2) + 2] = CODES_WIDTH[getCharIndex(texto.charAt(i))];
        }
        result[result.length - 2] = INTER_CHAR;
        result[result.length - 1] = CODES_WIDTH[stopChar];

        return result;
    }

    /**
     * @see org.jbarcode.BarcodeEncoder#calcularDV(String)
     */
    public String computeCheckSum(String texto) throws InvalidAtributeException {
        return "";
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text;
    }

    private int getCharIndex(char c) throws InvalidAtributeException {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c == '-') {
            return 10;
        } else if (c == '$') {
            return 11;
        } else if (c == ':') {
            return 12;
        } else if (c == '/') {
            return 13;
        } else if (c == '.') {
            return 14;
        } else if (c == '+') {
            return 15;
        } else {
            throw new InvalidAtributeException("[Codabar] The text contains not suported chars (" + c + ").");
        }
    }

    /**
     * @return Returns the startChar.
     */
    public int getStartChar() {
        return startChar;
    }

    /**
     * @param startChar The startChar to set.
     */
    public void setStartChar(int startChar) {
        this.startChar = startChar;
    }

    /**
     * @return Returns the stopChar.
     */
    public int getStopChar() {
        return stopChar;
    }

    /**
     * @param stopChar The stopChar to set.
     */
    public void setStopChar(int stopChar) {
        this.stopChar = stopChar;
    }

    public String toString() {
        return "CODABAR";
    }

}
