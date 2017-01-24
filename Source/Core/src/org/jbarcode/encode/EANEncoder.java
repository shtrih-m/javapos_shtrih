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
public abstract class EANEncoder implements BarcodeEncoder {

    protected static final BarSet[] CODES = new BarSet[]{
        new BarSet("1110010"),
        new BarSet("1100110"),
        new BarSet("1101100"),
        new BarSet("1000010"),
        new BarSet("1011100"),
        new BarSet("1001110"),
        new BarSet("1010000"),
        new BarSet("1000100"),
        new BarSet("1001000"),
        new BarSet("1110100")
    };

    protected static final BarSet LEFT_GUARD = new BarSet("101");
    protected static final BarSet CENTER_GUARD = new BarSet("01010");
    protected static final BarSet RIGTH_GUARD = new BarSet("101");

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text;
    }

    public String computeCheckSum(String text) throws InvalidAtributeException {
        int sum = 0;
        boolean odd = true;
        for (int charPos = text.length() - 1; charPos >= 0; charPos--) {
            if (odd) {
                sum += 3 * charToInt(text.charAt(charPos));
            } else {
                sum += charToInt(text.charAt(charPos));
            }
            odd = !odd;
        }
        int result = sum % 10;
        if (result == 0) {
            return "0";
        } else {
            return Integer.toString(10 - result);
        }
    }

    protected static int charToInt(char val) throws NumberFormatException {
        if (!Character.isDigit(val)) {
            throw new NumberFormatException("Invalid number");
        }
        return val - '0';
    }
}
