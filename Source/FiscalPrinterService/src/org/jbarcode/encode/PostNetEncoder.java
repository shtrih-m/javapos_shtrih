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
public class PostNetEncoder implements BarcodeEncoder {

    protected final BarSet[] CODES = new BarSet[]{
        new BarSet("11000"), new BarSet("00011"), new BarSet("00101"),
        new BarSet("00110"), new BarSet("01001"), new BarSet("01010"),
        new BarSet("01100"), new BarSet("10001"), new BarSet("10010"),
        new BarSet("10100")
    };

    protected final BarSet START_STOP_CHAR = new BarSet("1");

    private static PostNetEncoder instance;

    private PostNetEncoder() {

    }

    public static PostNetEncoder getInstance() {
        if (instance == null) {
            instance = new PostNetEncoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        return text + computeCheckSum(text);
    }

    /* (non-Javadoc)
     * @see org.jbarcode.BarcodeEncoder#gerarCodigo(java.lang.String)
     */
    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() < 1) {
            throw new InvalidAtributeException("[PostNet] Tamanho de texto invalido (" + texto.length() + ").");
        }

        BarSet[] result = new BarSet[texto.length() + 2];
        try {
            result[0] = START_STOP_CHAR;
            //gera a primeira parte do cпїЅdigo
            for (int i = 0; i < texto.length(); i++) {
                int atual = Integer.parseInt(String.valueOf(texto.charAt(i)));
                result[i + 1] = CODES[atual];
            }
            result[result.length - 1] = START_STOP_CHAR;
        } catch (NumberFormatException nfexc) {
            throw new InvalidAtributeException("[PostNet] O padrao suporta apenas numeros.");
        }
        return result;

    }

    /* (non-Javadoc)
     * @see org.jbarcode.BarcodeEncoder#calcularDV(java.lang.String)
     */
    public String computeCheckSum(String texto) throws InvalidAtributeException {
        int check = 0;
        for (int i = 0; i < texto.length(); i++) {
            check += Integer.parseInt(String.valueOf(texto.charAt(i)));
        }
        check = 10 - (check % 10);
        return String.valueOf(check);
    }

    public String toString() {
        return "PostNet";
    }

}
