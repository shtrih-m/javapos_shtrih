/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  FlпїЅvio Sampaio (flavio@ronisons.com);
 *
 * (C) Copyright 2005, by Favio Sampaio
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
public class EAN8Encoder extends EANEncoder {

    private static EAN8Encoder instance;

    private EAN8Encoder() {

    }

    public static EAN8Encoder getInstance() {
        if (instance == null) {
            instance = new EAN8Encoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException 
    {
        text = text.substring(0, 7);
        return text + computeCheckSum(text);
    }
    
    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() != 8) {
            throw new InvalidAtributeException("[EAN8] Invalid text length (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[11];
        try {
            //tres barras iniciais
            result[0] = LEFT_GUARD;
            //gera a primeira parte do codigo
            for (int i = 0; i < 4; i++) {
                int atual = charToInt(texto.charAt(i));
                result[i + 1] = CODES[atual].xorTrue();
            }
            //barras centrais que dividem o codigo
            result[5] = CENTER_GUARD;
            for (int i = 4; i < 8; i++) {
                int atual = charToInt(texto.charAt(i));
                result[i + 2] = CODES[atual];
            }
            //barras centrais que dividem o codigo
            result[10] = RIGTH_GUARD;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[EAN8] Only numbers suported.");
        }
        return result;
    }

    public String toString() {
        return "EAN 8";
    }

}
