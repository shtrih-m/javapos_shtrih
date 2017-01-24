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
public class UPCEEncoder extends EANEncoder {

    private static UPCEEncoder instance;

    protected BarSet[] DIGIT_PARITY = new BarSet[]{
        new BarSet("111000"), new BarSet("110100"), new BarSet("110010"),
        new BarSet("110001"), new BarSet("101100"), new BarSet("100110"),
        new BarSet("100011"), new BarSet("101010"), new BarSet("101001"),
        new BarSet("100101")
    };

    private BarSet RIGTH_GUARD_EXT = new BarSet("010101");

    private UPCEEncoder() {
    }

    public static UPCEEncoder getInstance() {
        if (instance == null) {
            instance = new UPCEEncoder();
        }
        return instance;
    }

    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() != 8) {
            throw new InvalidAtributeException("[UPCE] Invalid text length (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[8];
        try {
            int check = charToInt(texto.charAt(7));
            int primeiro = charToInt(texto.charAt(0));
            BarSet par = (primeiro == 0 ? DIGIT_PARITY[check] : DIGIT_PARITY[check].xorTrue());

            //tres barras iniciais
            result[0] = LEFT_GUARD;
            //gera a primeira parte do codigo
            for (int i = 0; i < 6; i++) {
                int atual = charToInt(texto.charAt(i + 1));
                if (par.get(i)) {
                    result[i + 1] = CODES[atual].reverse();
                } else {
                    result[i + 1] = CODES[atual].xorTrue();
                }
            }
            //barras centrais que dividem o codigo
            result[7] = RIGTH_GUARD_EXT;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[UPCE] O padrao suporta apenas numeros.");
        }
        return result;
    }

    public String convertUPCEtoUPCA(String upce) throws InvalidAtributeException {
        if ((upce.length() != 7) && (upce.length() != 8)) {
            throw new InvalidAtributeException("[UPCE] Invalid text length (" + upce.length() + ").");
        }
        StringBuffer result = new StringBuffer();
        result.append(upce.charAt(0));
        switch (upce.charAt(6)) {
            case '0':
            case '1':
            case '2':
                result.append(upce.charAt(1));
                result.append(upce.charAt(2));
                result.append(upce.charAt(6));
                result.append("0000");
                result.append(upce.charAt(3));
                result.append(upce.charAt(4));
                result.append(upce.charAt(5));
                break;

            case '3':
                result.append(upce.charAt(1));
                result.append(upce.charAt(2));
                result.append(upce.charAt(3));
                result.append("00000");
                result.append(upce.charAt(4));
                result.append(upce.charAt(5));
                break;

            case '4':
                result.append(upce.charAt(1));
                result.append(upce.charAt(2));
                result.append(upce.charAt(3));
                result.append(upce.charAt(4));
                result.append("00000");
                result.append(upce.charAt(5));
                break;

            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                result.append(upce.charAt(1));
                result.append(upce.charAt(2));
                result.append(upce.charAt(3));
                result.append(upce.charAt(4));
                result.append(upce.charAt(5));
                result.append("0000");
                result.append(upce.charAt(6));
                break;

            default:
                throw new InvalidAtributeException("[UPCE] Invalid char (" + upce.charAt(6) + ").");
        }
        if (upce.length() == 8) {
            result.append(upce.charAt(7));
        }
        return result.toString();
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException 
    {
        text = text.substring(0, 7);
        return text + computeCheckSum(text);
    }
    
    public String computeCheckSum(String text) throws InvalidAtributeException {
        if (text.length() != 7) {
            throw new InvalidAtributeException("[UPCE] Invalid text length (" + text.length() + ").");
        }
        String upca = convertUPCEtoUPCA(text);
        return super.computeCheckSum(upca);
    }

    public String toString() {
        return "UPC-E";
    }

}
