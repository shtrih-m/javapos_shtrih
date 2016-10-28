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
public class MSIPlesseyEncoder implements BarcodeEncoder {

    private static MSIPlesseyEncoder instance;

    protected BarSet[] CODES_WIDTH = new BarSet[]{
        new BarSet("100100100100"), new BarSet("100100100110"), new BarSet("100100110100"),
        new BarSet("100100110110"), new BarSet("100110100100"), new BarSet("100110100110"),
        new BarSet("100110110100"), new BarSet("100110110110"), new BarSet("110100100100"),
        new BarSet("110100100110")
    };

    protected BarSet START_CHAR = new BarSet("110");

    protected BarSet STOP_CHAR = new BarSet("1001");

    protected MSIPlesseyEncoder() {

    }

    public static BarcodeEncoder getInstance() {
        if (instance == null) {
            instance = new MSIPlesseyEncoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }

    public String computeCheckSum(String text) throws InvalidAtributeException {
        StringBuffer newNumber = new StringBuffer();
        StringBuffer numTotal = new StringBuffer();
        for (int i = text.length() - 1; i >= 0; i -= 2) {
            newNumber.insert(0, text.charAt(i));
            if (i > 0) {
                numTotal.append(text.charAt(i - 1));
            }
        }
        numTotal.append(2 * Integer.parseInt(newNumber.toString()));

        int sum = 0;
        String num = numTotal.toString();
        for (int i = 0; i < num.length(); i++) {
            sum += charToInt(num.charAt(i));
        }

        return Integer.toString(10 - (sum % 10 == 0 ? 10 : sum % 10));
    }

    public BarSet[] encode(String text) throws InvalidAtributeException {
        BarSet[] result = new BarSet[text.length() + 2];
        try {
            result[0] = START_CHAR;
            for (int i = 0; i < text.length(); i++) {
                result[i + 1] = CODES_WIDTH[charToInt(text.charAt(i))];
            }
            result[result.length - 1] = STOP_CHAR;
        } catch (Exception e) {
            throw new InvalidAtributeException("[MSIPlessey] Only numbers suported.");
        }
        return result;
    }

    protected static int charToInt(char val) {
        return val - '0';
    }

    public String toString() {
        return "MSI/Plessey";
    }
}
