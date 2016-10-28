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
public class EAN13Encoder extends EANEncoder {

    private static EAN13Encoder instance;

    protected BarSet[] DIGIT_PARITY = new BarSet[]{
        new BarSet("000000"), new BarSet("001011"), new BarSet("001101"),
        new BarSet("001110"), new BarSet("010011"), new BarSet("011001"),
        new BarSet("011100"), new BarSet("010101"), new BarSet("010110"),
        new BarSet("011010")
    };

    protected EAN13Encoder() {

    }

    public static EAN13Encoder getInstance() {
        if (instance == null) {
            instance = new EAN13Encoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException 
    {
        checkTextLength(text);
        if (text.length() == 13) return text;
        text = text.substring(0, 12);
        return text + computeCheckSum(text);
    }

    public void checkTextLength(String texto) throws InvalidAtributeException {
        if (texto.length() < 12 || texto.length() > 13) {
            throw new InvalidAtributeException("[EAN13] Invalid text length (" + texto.length() + ").");
        }
    }

    public BarSet[] encode(String texto) throws InvalidAtributeException {
        checkTextLength(texto);
        BarSet[] result = new BarSet[15];
        try {
            int primeiro = charToInt(texto.charAt(0));
            //tres barras iniciais
            result[0] = LEFT_GUARD;
            //gera a primeira parte do codigo
            for (int i = 1; i <= 6; i++) {
                int atual = charToInt(texto.charAt(i));
                if (DIGIT_PARITY[primeiro].get(i - 1)) {
                    result[i] = CODES[atual].reverse();
                } else {
                    result[i] = CODES[atual].xorTrue();
                }
            }
            //barras centrais que dividem o codigo
            result[7] = CENTER_GUARD;
            for (int i = 7; i <= 12; i++) {
                result[i + 1] = CODES[charToInt(texto.charAt(i))];
            }
            //barras centrais que dividem o codigo
            result[14] = RIGTH_GUARD;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[EAN13] Only numbers suported.");
        }
        return result;
    }

    public String toString() {
        return "EAN 13";
    }
}
