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
public class Interleaved2of5Encoder extends Standard2of5Encoder {

    private BarSet START_CHAR = new BarSet("0000");

    private BarSet STOP_CHAR = new BarSet("100");

    private static Interleaved2of5Encoder instance;

    private Interleaved2of5Encoder() {
    }

    public static Standard2of5Encoder getInstance() {
        if (instance == null) {
            instance = new Interleaved2of5Encoder();
        }
        return instance;
    }

    public BarSet[] encode(String text) throws InvalidAtributeException {
        if (text.length() < 1) {
            throw new InvalidAtributeException("[Interleaved2of5] Invalid text length (" + text.length() + ").");
        }

        BarSet[] result = new BarSet[(text.length() / 2) + 2];
        try {
            //tres barras iniciais
            result[0] = START_CHAR;
            //gera a primeira parte do codigo
            for (int i = 0; i < text.length() / 2; i++) {
                int ind = charToInt(text.charAt(2 * i));
                int ind1 = charToInt(text.charAt((2 * i) + 1));
                result[i + 1] = new BarSet(10);
                for (int j = 0; j < CODES_WIDTH[ind].length(); j++) {
                    result[i + 1].set(j * 2, CODES_WIDTH[ind].get(j));
                    result[i + 1].set((j * 2) + 1, CODES_WIDTH[ind1].get(j));
                }
            }
            //barras centrais que dividem o codigo
            result[result.length - 1] = STOP_CHAR;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[Interleaved2of5] Only numbers suported.");
        }
        return result;
    }

    public String toString() {
        return "Interleaved 2of5";
    }

}
